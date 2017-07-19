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

    /**
     * For {@link java.sql.ResultSet#wasNull()} implementation
     */
    private boolean wasNull = true;

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
    public String getString(int i) {
        String ret = null;
        try {
            ret = cursor.getString(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public boolean getBoolean(int i) {
        Integer val = null;
        try {
            val = cursor.getInt(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return val == 1;
    }

    @Override
    public int getInt(int i) {
        Integer ret = null;
        try {
            ret = cursor.getInt(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public long getLong(int i) {
        Long ret = null;
        try {
            ret = cursor.getLong(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public float getFloat(int i) {
        Float ret = null;
        try {
            ret = cursor.getFloat(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public double getDouble(int i) {
        Double ret = null;
        try {
            ret = cursor.getDouble(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public byte[] getBytes(int i) {
        byte[] ret = null;
        try {
            ret = cursor.getBlob(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public Date getDate(int i) {
        Integer timestamp = null;
        try {
            timestamp = cursor.getInt(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return new Date(timestamp);
    }

    @Override
    public String getString(String s) {
        String ret = null;
        try {
            ret = cursor.getString(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public boolean getBoolean(String s) {
        Integer i = null;
        try {
            i = cursor.getInt(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return i == 1;
    }

    @Override
    public int getInt(String s) {
        Integer ret = null;
        try {
            ret = cursor.getInt(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public long getLong(String s) {
        Long ret = null;
        try {
            ret = cursor.getLong(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public float getFloat(String s) {
        Float ret = null;
        try {
            ret = cursor.getFloat(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public double getDouble(String s) {
        Double ret = null;
        try {
            ret = cursor.getDouble(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public byte[] getBytes(String s) {
        byte[] ret = null;
        try {
            ret = cursor.getBlob(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public Date getDate(String s) {
        Integer timestamp = null;
        try {
            timestamp = cursor.getInt(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return new Date(timestamp);
    }

    @Override
    public Object getObject(int i) {
        Object ret = null;
        try {
            ret = cursor.getBlob(i);
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public Object getObject(String s) {
        Object ret = null;
        try {
            ret = cursor.getBlob(cursor.getColumnIndex(s));
            wasNull = false;
        } catch (Exception e) {
            wasNull = true;
        }
        return ret;
    }

    @Override
    public int findColumn(String s) {
        return cursor.getColumnIndex(s);
    }

    @Override
    public boolean isFirst() {
        return cursor.isFirst();
    }

    @Override
    public boolean isLast() {
        return cursor.isLast();
    }

    @Override
    public boolean first() {
        return cursor.moveToFirst();
    }

    @Override
    public boolean last() {
        return cursor.moveToLast();
    }

    @Override
    public boolean previous() {
        return cursor.moveToPrevious();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }

    @Override
    public boolean wasNull() {
        return wasNull;
    }

    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }
}
