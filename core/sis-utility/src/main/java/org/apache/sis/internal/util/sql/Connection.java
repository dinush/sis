package org.apache.sis.internal.util.sql;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Replacement implementation for JDBC {@code Connection} class.
 */
public class Connection implements AutoCloseable {

    private final SQLiteDatabase db;

    public Connection(SQLiteDatabase db) {
        this.db = db;
    }

    public Statement createStatement() {
        return new StatementImpl(this);
    }

    public DatabaseMetaData getMetaData() {
        return new DatabaseMetaData();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void close() throws SQLException {
        db.close();
    }

    class StatementImpl implements Statement {

        Connection connection;

        StatementImpl(Connection connection) {
            this.connection = connection;
        }

        @Override
        public ResultSet executeQuery(String query) {
            return (ResultSet) db.rawQuery(query, null);
        }

        @Override
        public int executeUpdate(String query) {
            db.execSQL(query);
            return 1;   // Pseudo value (for now)
        }

        @Override
        public Connection getConnection() {
            return connection;
        }

        @Override
        public void close() throws SQLException {
            connection.close();
        }
    }
}