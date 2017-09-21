package org.apache.sis.internal.util.sql;

public class DatabaseMetaData {

    public String getIdentifierQuoteString() {
        return "'";
    }

    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    public String getURL() {
        return null;
    }

    public int getDatabaseMajorVersion() {
        return 0;   // pseudo value. just need to provide the function.
    }

    public int getDatabaseMinorVersion() {
        return 0;   // pseudo value. just need to provide the function.
    }
}
