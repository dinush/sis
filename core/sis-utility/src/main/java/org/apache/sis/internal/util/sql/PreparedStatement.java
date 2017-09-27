package org.apache.sis.internal.util.sql;

/**
 * Replacement interface for JDBC {@code Statement} class.
 */
public interface PreparedStatement extends Statement {

    ResultSet executeQuery();
    void setString(int paramIndex, String param);
}
