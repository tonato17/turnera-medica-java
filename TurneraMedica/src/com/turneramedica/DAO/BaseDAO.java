package com.turneramedica.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO {

    // Método para obtener la conexión desde DatabaseConnection
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    // Método para cerrar los recursos de conexión, Statement y ResultSet
    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes usar un logger en lugar de printStackTrace
        }
    }

    // Sobrecarga del método closeResources para cerrar solo conexión y Statement
    protected void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }
}
