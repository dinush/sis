package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Replacement implementation for JDBC {@code Connection} class.
 */
public class Connection implements AutoCloseable {

    private final SQLiteDatabase db;

    public Connection(SQLiteDatabase db) {
        this.db = db;
    }

    public Statement createStatement() {
        return new Statement(this);
    }

    public DatabaseMetaData getMetaData() {
        return new DatabaseMetaData();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}