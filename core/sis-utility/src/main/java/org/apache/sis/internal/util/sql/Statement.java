package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Replacement implementation for JDBC {@code Statement} class.
 */
public class Statement {

    private final SQLiteDatabase db;

    public Statement(final SQLiteDatabase db) {
        this.db = db;
    }

    public ResultSet executeQuery(final String query) {
        return (ResultSet) db.rawQuery(query, null);
    }
}
