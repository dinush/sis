/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.referencing.factory.sql;

import java.util.LinkedHashMap;
import java.io.Serializable;
import java.io.ObjectStreamException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.lang.ref.Reference;
import org.opengis.referencing.operation.Projection;
import org.opengis.util.NoSuchIdentifierException;
import org.apache.sis.util.collection.BackingStoreException;
import org.apache.sis.internal.util.AbstractMap;
import org.apache.sis.internal.system.Loggers;
import org.apache.sis.util.collection.IntegerList;
import org.apache.sis.util.logging.Logging;


/**
 * A map of EPSG authority codes as keys and object names as values.
 * This map requires a living connection to the EPSG database.
 *
 * <p>Serialization of this class stores a copy of all authority codes.
 * The serialization does not preserve any connection to the database.</p>
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @since   0.7
 * @version 0.7
 * @module
 */
final class AuthorityCodes extends AbstractMap<String,String> implements Serializable, AutoCloseable {
    /**
     * For compatibility with different versions.
     */
    private static final long serialVersionUID = 6118171679321975503L;

    /**
     * Highest code value (inclusive) that this {@code AuthorityCodes} support during iterations.
     * This is based on the upper value of the highest range of codes once used by EPSG.
     */
    private static final int MAX_CODE = 69999999;

    /**
     * Index in the {@link #sql} and {@link #statements} arrays.
     */
    private static final int ALL = 0, ONE = 1;

    /**
     * The factory which is the owner of this map. One purpose of this field is to prevent garbage collection
     * of that factory as long as this map is in use. This is required because {@link EPSGFactory#finalize()}
     * closes the JDBC connections.
     */
    private final transient EPSGFactory factory;

    /**
     * The interface of referencing objects for which this map contains the code.
     * May be a super-interface of the type specified to the constructor.
     */
    final Class<?> type;

    /**
     * {@code true} if {@link #type} is assignable to {@link Projection}.
     */
    private final transient boolean isProjection;

    /**
     * The SQL commands that this {@code AuthorityCodes} may need to execute.
     * In this array:
     *
     * <ul>
     *   <li>{@code sql[ALL]} is a statement for querying all codes.</li>
     *   <li>{@code sql[ONE]} is a statement for querying a single code.
     *       This statement is similar to {@code sql[ALL]} with the addition of a {@code WHERE} clause.</li>
     * </ul>
     */
    private final transient String[] sql = new String[2];

    /**
     * The JDBC statements for the SQL commands in the {@link #sql} array, created when first needed.
     * All usages of those statements shall be synchronized on the {@linkplain #factory}.
     * This array will also be stored in {@link CloseableReference} for closing the statements
     * when the garbage collector detected that {@code AuthorityCodes} is no longer in use.
     */
    private final transient Statement[] statements = new Statement[2];

    /**
     * The result of {@code statements[ALL]}, created only if requested.
     * The codes will be queried at most once and cached in the {@link #codes} list.
     *
     * <p>Note that if this result set is not closed explicitely, it will be closed implicitly when
     * {@code statements[ALL]} will be closed. This is because JDBC specification said that closing
     * a statement also close its result set.</p>
     */
    private transient ResultSet results;

    /**
     * A cache of integer codes. Created only if the user wants to iterate over all codes or asked for the map size.
     */
    private transient IntegerList codes;

    /**
     * Creates a new map of authority codes for the specified type.
     *
     * @param  connection The connection to the EPSG database.
     * @param  table      The table to query.
     * @param  type       The type to query.
     * @param  factory    The factory originator.
     */
    AuthorityCodes(final Connection connection, final TableInfo table, final Class<?> type, final EPSGFactory factory) {
        this.factory = factory;
        /*
         * Build the SQL query for fetching the codes of all object. It is of the form:
         *
         *     SELECT code FROM table ORDER BY code;
         */
        final StringBuilder buffer = new StringBuilder(100);
        final int columnNameStart = buffer.append("SELECT ").length();
        final int columnNameEnd = buffer.append(table.codeColumn).length();
        buffer.append(" FROM ").append(table.table);
        boolean hasWhere = false;
        Class<?> tableType = table.type;
        if (table.typeColumn != null) {
            for (int i=0; i<table.subTypes.length; i++) {
                final Class<?> candidate = table.subTypes[i];
                if (candidate.isAssignableFrom(type)) {
                    buffer.append(" WHERE (").append(table.typeColumn)
                          .append(" LIKE '").append(table.typeNames[i]).append("%'");
                    hasWhere = true;
                    tableType = candidate;
                    break;
                }
            }
            if (hasWhere) {
                buffer.append(')');
            }
        }
        final int conditionStart = buffer.length();
        buffer.append(" ORDER BY ").append(table.codeColumn);
        sql[ALL] = factory.adaptSQL(buffer.toString());
        /*
         * Build the SQL query for fetching the name of a single object for a given code.
         * This query will also be used for testing object existence. It is of the form:
         *
         *     SELECT name FROM table WHERE code = ?
         */
        buffer.setLength(conditionStart);
        if (table.nameColumn != null) {
            buffer.replace(columnNameStart, columnNameEnd, table.nameColumn);
        }
        buffer.append(hasWhere ? " AND " : " WHERE ").append(table.codeColumn).append(" = ?");
        sql[ONE] = factory.adaptSQL(buffer.toString());
        /*
         * Other information opportunistically computed from above search.
         */
        this.type = tableType;
        isProjection = Projection.class.isAssignableFrom(tableType);
    }

    /**
     * Creates a weak reference to this map. That reference will also be in charge of closing the JDBC statements
     * if they were not closed.
     */
    final Reference<AuthorityCodes> createReference() {
        return new CloseableReference<>(this, factory, statements);
    }

    /**
     * Returns {@code true} if the specified code should be included in this map.
     */
    private boolean filter(final int code) throws SQLException {
        assert Thread.holdsLock(factory);
        if (!isProjection) {
            return true;
        }
        try {
            return factory.isProjection(code);
        } catch (NoSuchIdentifierException e) {
            /*
             * This is not a fatal error since we can consider that the CRS is not a projection if we did not found it.
             * However since this exception should never happen, there is probably a problem with the database content.
             * Logs a warning pretending to come from the EPSGFactory.getAuthorityCodes() method since the later is the
             * public facade by which the user can iterate over the entries in this AuthorityCodes map.
             */
            Logging.unexpectedException(Logging.getLogger(Loggers.CRS_FACTORY), EPSGFactory.class, "getAuthorityCodes", e);
            return false;
        }
    }

    /**
     * Returns the code at the given index, or -1 if the index is out of bounds.
     *
     * @param  index index of the code to fetch.
     * @return The code at the given index, or -1 if out of bounds.
     * @throws SQLException if an error occurred while querying the database.
     */
    private int getCodeAt(final int index) throws SQLException {
        int code;
        synchronized (factory) {
            if (codes == null) {
                codes = new IntegerList(100, MAX_CODE);
                results = (statements[ALL] = factory.connection.createStatement()).executeQuery(sql[ALL]);
                sql[ALL] = null;                // Not needed anymore.
            }
            int more = index - codes.size();    // Positive as long as we need more data.
            if (more < 0) {
                code = codes.getInt(index);     // Get a previously cached value.
            } else {
                final ResultSet r = results;
                if (r == null) {
                    code = -1;                  // Already reached iteration end in a previous call.
                } else do {
                    if (!r.next()) {
                        results = null;
                        r.close();
                        statements[ALL].close();
                        statements[ALL] = null;
                        return -1;
                    }
                    code = r.getInt(1);
                    if (filter(code)) {
                        codes.addInt(code);
                        more--;
                    }
                } while (more >= 0);
            }
        }
        return code;
    }

    /**
     * Returns {@code true} if this map contains no element.
     * This method fetches at most one row instead of counting all rows.
     */
    @Override
    public boolean isEmpty() {
        try {
            return getCodeAt(0) >= 0;
        } catch (SQLException exception) {
            throw factoryFailure(exception);
        }
    }

    /**
     * Counts the number of elements in this map.
     */
    @Override
    public int size() {
        try {
            getCodeAt(Integer.MAX_VALUE);       // Force counting all elements, if not already done.
        } catch (SQLException exception) {
            throw factoryFailure(exception);
        }
        return codes.size();
    }

    /**
     * Returns the description associated to the given authority code, or {@code null} if none.
     *
     * @param  code  The code for which to get the description. May be a string or an integer.
     * @return The description for the given code, or {@code null} if none.
     */
    @Override
    public String get(final Object code) {
        if (code != null) {
            final int n;
            if (code instanceof Number) {
                n = ((Number) code).intValue();
            } else try {
                n = Integer.parseInt(code.toString());
            } catch (NumberFormatException e) {
                return null;    // Okay by this method contract (the given key does not exist in this map).
            }
            try {
                synchronized (factory) {
                    if (filter(n)) {
                        PreparedStatement statement = (PreparedStatement) statements[ONE];
                        if (statement == null) {
                            statements[ONE] = statement = factory.connection.prepareStatement(sql[ONE]);
                            sql[ONE] = null;    // Not needed anymore.
                        }
                        statement.setInt(1, n);
                        try (ResultSet results = statement.executeQuery()) {
                            while (results.next()) {
                                String name = results.getString(1);
                                if (name != null) {
                                    return name;
                                }
                            }
                        }
                    }
                }
            } catch (SQLException exception) {
                throw factoryFailure(exception);
            }
        }
        return null;
    }

    /**
     * Returns an iterator over the entries.
     */
    @Override
    public EntryIterator<String,String> entryIterator() {
        return new EntryIterator<String,String>() {
            /** Index of current position. */
            private int index = -1;

            /** The authority code at current position of the iterator, or -1 if we reached iteration end. */
            private int code;

            /** Moves to the next element in the iteration. */
            @Override protected boolean next() {
                try {
                    code = getCodeAt(++index);
                } catch (SQLException exception) {
                    throw factoryFailure(exception);
                }
                return code >= 0;
            }

            /** Returns the key at the current iterator position. */
            @Override protected String getKey() {
                return String.valueOf(code);
            }

            /**
             * Returns pseudo-value at the current iterator position. We do not query the real value because it
             * is costly and useless in the context where this method is used. This is because the users should
             * never see the map directly, but only the key set.
             */
            @Override protected String getValue() {
                return "";
            }
        };
    }

    /**
     * Invoked when a SQL statement can not be executed, or the result retrieved.
     */
    private BackingStoreException factoryFailure(final SQLException exception) {
        return new BackingStoreException(exception.getLocalizedMessage(), exception);
    }

    /**
     * Returns a serializable copy of this set. This method is invoked automatically during serialization.
     * The serialized map of authority codes is disconnected from the underlying database.
     */
    protected Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap<>(this);
    }

    /**
     * Closes the JDBC statement used by the {@code AuthorityCodes}. Note that if this method is never invoked,
     * {@link CloseableReference} will perform the same work after the garbage collector detected that this map
     * is not referenced anymore.
     */
    @Override
    public void close() throws SQLException {
        SQLException exception = null;
        synchronized (factory) {
            if (results != null) try {
                results.close();
            } catch (SQLException e) {
                exception = e;
            }
            results = null;
            exception = CloseableReference.close(statements, exception);
        }
        if (exception != null) {
            throw exception;
        }
    }
}
