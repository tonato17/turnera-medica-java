package com.turneramedica.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuración de la base de datos H2
    private static final String URL = "jdbc:h2:~/turneramedica"; // URL de la base de datos
    private static final String USER = "sa"; // Usuario de H2
    private static final String PASSWORD = ""; // Contraseña de H2

    // Singleton para la conexión
    private static Connection connection;

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Cargar el driver de H2 si es necesario (opcional, para Java 8+ ya no es obligatorio)
                Class.forName("org.h2.Driver");
                // Crear la conexión
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Error al cargar el driver de H2", e);
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null; // Asegurar que el objeto se reinicia
        }
    }
}
