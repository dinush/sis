package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Replacement implementation for JDBC {@code Connection} class.
 */
public class Connection {

    private final SQLiteDatabase db;

    public Connection(SQLiteDatabase db) {
        this.db = db;
    }

    public Statement createStatement() {
        return new Statement(db);
    }
}