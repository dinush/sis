package org.apache.sis.internal.util.sql;

import java.sql.SQLException;

/**
 * Replacement interface for JDBC {@code Statement} class.
 */
public interface Statement {

    ResultSet executeQuery(final String query);

    int executeUpdate(final String query);

    Connection getConnection();

    void close() throws SQLException;
}
