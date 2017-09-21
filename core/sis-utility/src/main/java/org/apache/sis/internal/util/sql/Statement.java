package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Replacement implementation for JDBC {@code Statement} class.
 */
public class Statement {

    private final Connection connection;
    private final SQLiteDatabase db;

    public Statement(final Connection connection) {
        this.connection = connection;
        db = connection.getDb();
    }

    public ResultSet executeQuery(final String query) {
        return (ResultSet) db.rawQuery(query, null);
    }

    public int executeUpdate(final String query) {
        db.beginTransaction();
        db.execSQL(query);
        return 1;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {

    }
}
