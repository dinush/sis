package org.apache.sis.internal.util.sql;

import android.database.Cursor;

/**
 * Wrapper class for {@link Cursor} to be compatible with JDBC {@code ResultSet}.
 */

public interface ResultSet extends Cursor {

    boolean next();
    ResultSetMetaData getMetaData();
    Object getObject(String columnName);
}
