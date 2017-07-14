package org.apache.sis.internal.metadata.sql;

/**
 * Holds property values related to SQLite Databases. Purpose of
 * this class is to maintain a central place for the properties.
 */
public class SQLiteConfiguration {

    /**
     * The quote character expected to be found in the SQL script.
     * This character shall not be a whitespace or a Unicode identifier part.
     */
    public static final char QUOTE = '\'';

    /**
     * The quote character for identifiers expected to be found in the SQL script.
     * This character shall not be a whitespace or a Unicode identifier part.
     */
    public static final char IDENTIFIER_QUOTE = '"';

    /**
     * The character at the end of statements.
     * This character shall not be a whitespace or a Unicode identifier part.
     */
    public static final char END_OF_STATEMENT = ';';

    /**
     * The string that can be used to escape wildcard characters.
     */
    public static final String ESCAPE_WILDCARD = "!";

    /**
     * The sequence for SQL comments.
     */
    public static final String COMMENT = "--";

    /**
     * Whether this dialect support table inheritance.
     */
    public static final boolean isTableInheritanceSupported = false;

    /**
     * Child tables inherit the index of their parent tables.
     */
    public static final boolean isIndexInheritanceSupported = true;

    /**
     * Database does not support enums.
     */
    public static final boolean isEnumTypeSupported = false;

    /**
     * Database does not support catalogs.
     */
    public static final boolean isCatalogSupported = false;

    /**
     * Database does not support schemas.
     */
    public static final boolean isSchemaSupported = false;

    /**
     * Database does not support {@code "GRANT USAGE ON SCHEMA"} statements.
     */
    public static final boolean isGrantOnSchemaSupported = false;

    /**
     * Database does not support {@code "GRANT SELECT ON TABLE"} statements.
     */
    public static final boolean isGrantOnTableSupported = false;

    /**
     * Database does not support the {@code COMMENT} statement.
     */
    public static final boolean isCommentSupported = false;

    /**
     * Database does not support create language.
     */
    public static final boolean isCreateLanguageRequired = false;

}
