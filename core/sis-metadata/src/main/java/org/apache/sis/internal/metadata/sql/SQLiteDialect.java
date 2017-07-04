package org.apache.sis.internal.metadata.sql;

/**
 * Holds property values related to SQLite Databases. Purpose of
 * this class is to maintain a central place for the properties.
 */
public class SQLiteDialect {
    /**
     * The characters used for quoting identifiers.
     */
    public static final String quote = "\"";

    /**
     * The string that can be used to escape wildcard characters.
     */
    public static final String escape = "!";

    /**
     * Whether this dialect support table inheritance.
     */
    public static final boolean isTableInheritanceSupported = false;

    /**
     * {@code true} if child tables inherit the index of their parent tables.
     */
    public final boolean isIndexInheritanceSupported = false;
}
