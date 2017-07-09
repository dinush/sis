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
package org.apache.sis.internal.metadata.sql;

import org.apache.sis.internal.system.DataDirectory;
import org.apache.sis.internal.system.Loggers;
import org.apache.sis.util.logging.Logging;
import org.apache.sis.util.resources.Messages;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;

// Branch specific imports
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Manages the unique {@link SQLiteDatabase} instance to the {@code $SIS_DATA/Databases/SpatialMetadata} database.
 * This includes initialization of a new database if none existed. The schemas will be created by subclasses of
 * this {@code Initializer} class, which must be registered in the following file:
 *
 * {@preformat text
 *   META-INF/services/org.apache.sis.internal.metadata.sql.Initializer
 * }
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 0.8
 * @since   0.7
 * @module
 */
public abstract class Initializer {
    /**
     * Name of the database to open in the {@code $SIS_DATA/Databases} directory or the directory given by
     * the {@code derby.system.home} property.
     */
    private static final String DATABASE = "SpatialMetadata";

    /**
     * The unique, SIS-wide, data source to the {@code $SIS_DATA/Databases/SpatialMetadata} database.
     * Created when first needed, and cleared on shutdown.
     *
     * @see #getDataSource(Context)
     */
    private static SQLiteDatabase source;

    /**
     * {@code true} if {@link #connected(String)} has been invoked at least once.
     * This is reset to {@code false} if the {@link #source} is changed.
     */
    private static boolean connected;

    /**
     * For subclasses only.
     */
    protected Initializer() {
    }

    /**
     * Invoked for populating an initially empty database.
     *
     * @param  connection  connection to the empty database.
     * @throws SQLException if an error occurred while populating the database.
     */
    protected abstract void createSchema(SQLiteDatabase connection) throws SQLException;

    /**
     * Invoked when the JNDI data source associated to {@code "jdbc/SpatialMetadata"} changed.
     */
    protected abstract void dataSourceChanged();

    /**
     * Returns the data source for the SIS-wide "SpatialMetadata" database.
     * This method returns the first of the following steps that succeed:
     *
     * <ol>
     *   <li>If the {@code SIS_DATA} environment variable is defined, {@code jdbc:derby:$SIS_DATA/Databases/SpatialMetadata}.
     *       This database will be created if it does not exist. Note that this is the only case where we allow database
     *       creation since we are in the directory managed by SIS.</li>
     *   <li>Otherwise (no JNDI, no environment variable, no Derby property set), {@code null}.</li>
     * </ol>
     *
     * @return the data source for the {@code $SIS_DATA/Databases/SpatialMetadata} or equivalent database, or {@code null} if none.
     * @throws Exception for any kind of errors. This include {@link RuntimeException} not documented above like
     *         {@link IllegalArgumentException}, {@link SecurityException}, <i>etc.</i>
     */
    public static synchronized SQLiteDatabase getDataSource(Context context) {
        if (source == null) {
            source = new DatabaseOpenHelper(context, DATABASE, null, 1).getWritableDatabase();
        }
        source.enableWriteAheadLogging();
        return source;
    }

    /**
     * Prepares a log record saying that a connection to the spatial metadata database has been created.
     * This method can be invoked after {@link DatabaseOpenHelper}. When invoked for the first time,
     * the record level is set to {@link Level#CONFIG}. On next calls, the level become {@link Level#FINE}.
     *
     * @param  metadata  the value of {@code DataSource.getConnection().getMetaData()} or equivalent.
     * @return the record to log. Caller should set the source class name and source method name.
     * @throws SQLException if an error occurred while fetching the database URL.
     *
     * @since 0.8
     */
    public static LogRecord connected(final String metadata) throws SQLException {
        final Level level;
        synchronized (Initializer.class) {
            level = connected ? Level.FINE : Level.CONFIG;
            connected = true;
        }
        final LogRecord record = Messages.getResources(null).getLogRecord(level,
                Messages.Keys.ConnectedToGeospatialDatabase_1, SQLUtilities.getSimplifiedURL(metadata));
        record.setLoggerName(Loggers.SYSTEM);
        return record;
    }

    /**
     * Returns a message for unspecified data source. The message will depend on whether a JNDI context exists or not.
     * This message can be used for constructing an exception when {@link #getDataSource(Context)} returned {@code null}.
     *
     * @param  locale  the locale for the message to produce, or {@code null} for the default one.
     * @return message for unspecified data source.
     */
    public static String unspecified(final Locale locale) {
        final short key;
        final String value;
        key = Messages.Keys.DataDirectoryNotSpecified_1;
        value = DataDirectory.ENV;
        return Messages.getResources(locale).getString(key, value);
    }

    /**
     * Invoked when the JVM is shutting down, or when the Servlet or OSGi bundle is uninstalled.
     * This method shutdowns the Derby database.
     *
     * @throws ReflectiveOperationException if an error occurred while
     *         setting the shutdown property on the Derby data source.
     */
    private static synchronized void shutdown() throws ReflectiveOperationException {
        final SQLiteDatabase ds = source;
        if (ds != null) {                       // Should never be null, but let be safe.
            source = null;                      // Clear now in case of failure in remaining code.
            connected = false;
            ds.getClass().getMethod("setShutdownDatabase", String.class).invoke(ds, "shutdown");
            try {
                ds.close();                     // Does the actual shutdown.
            } catch (SQLException e) {          // This is the expected exception.
                final LogRecord record = new LogRecord(Level.FINE, e.getMessage());
                if (!isSuccessfulShutdown()) {
                    record.setLevel(Level.WARNING);
                    record.setThrown(e);
                }
                record.setLoggerName(Loggers.SQL);
                Logging.log(Initializer.class, "shutdown", record);
            }
        }
    }

    /**
     * Returns {@code true} if the database is closed.
     *
     * @return {@code true} if the database is not open.
     */
    static boolean isSuccessfulShutdown() {
        return !source.isOpen();
    }
}
