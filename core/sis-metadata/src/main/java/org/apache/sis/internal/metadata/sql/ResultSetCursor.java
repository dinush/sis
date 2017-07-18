package org.apache.sis.internal.metadata.sql;

import android.database.Cursor;

import java.sql.Date;
import java.sql.SQLException;

/**
 * {@link java.sql.ResultSet} wrapper class for {@link Cursor}
 */
public class ResultSetCursor extends AbstractResultSet {

    /**
     * Actual {@link Cursor} object which this class perform operations.
     */
    private final Cursor cursor;

    public ResultSetCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public boolean next() throws SQLException {
        return cursor.moveToNext();
    }

    @Override
    public void close() throws SQLException {
        cursor.close();
    }

    @Override
    public String getString(int i) throws SQLException {
        return cursor.getString(i);
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return cursor.getInt(i) == 1;
    }

    @Override
    public int getInt(int i) throws SQLException {
        return cursor.getInt(i);
    }

    @Override
    public long getLong(int i) throws SQLException {
        return cursor.getLong(i);
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return cursor.getFloat(i);
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return cursor.getDouble(i);
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return cursor.getBlob(i);
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return new Date(cursor.getInt(i));
    }

    @Override
    public String getString(String s) throws SQLException {
        return cursor.getString(cursor.getColumnIndex(s));
    }

    @Override
    public boolean getBoolean(String s) throws SQLException {
        return cursor.getInt(cursor.getColumnIndex(s)) == 1;
    }

    @Override
    public int getInt(String s) throws SQLException {
        return cursor.getInt(cursor.getColumnIndex(s));
    }

    @Override
    public long getLong(String s) throws SQLException {
        return cursor.getLong(cursor.getColumnIndex(s));
    }

    @Override
    public float getFloat(String s) throws SQLException {
        return cursor.getFloat(cursor.getColumnIndex(s));
    }

    @Override
    public double getDouble(String s) throws SQLException {
        return cursor.getDouble(cursor.getColumnIndex(s));
    }

    @Override
    public byte[] getBytes(String s) throws SQLException {
        return cursor.getBlob(cursor.getColumnIndex(s));
    }

    @Override
    public Date getDate(String s) throws SQLException {
        return new Date(cursor.getInt(cursor.getColumnIndex(s)));
    }

    @Override
    public Object getObject(int i) throws SQLException {
        return cursor.getBlob(i);
    }

    @Override
    public Object getObject(String s) throws SQLException {
        return cursor.getBlob(cursor.getColumnIndex(s));
    }

    @Override
    public int findColumn(String s) throws SQLException {
        return cursor.getColumnIndex(s);
    }

    @Override
    public boolean isFirst() throws SQLException {
        return cursor.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        return cursor.isLast();
    }

    @Override
    public boolean first() throws SQLException {
        return cursor.moveToFirst();
    }

    @Override
    public boolean last() throws SQLException {
        return cursor.moveToLast();
    }

    @Override
    public boolean previous() throws SQLException {
        return cursor.moveToPrevious();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return cursor.isClosed();
    }
}
