package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;

/**
 * Wrapper class for {@link SQLiteCursor} to be compatible with JDBC code.
 */

public class ResultSet extends SQLiteCursor {
    public ResultSet(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
    }

    public boolean next() {
        return super.moveToNext();
    }
}
