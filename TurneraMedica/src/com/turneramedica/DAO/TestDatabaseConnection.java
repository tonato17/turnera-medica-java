package com.turneramedica.DAO;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            // Intentar obtener la conexión
            Connection connection = DatabaseConnection.getConnection();
            System.out.println("Conexión establecida exitosamente.");

            // Cerrar la conexión
            DatabaseConnection.closeConnection();
            System.out.println("Conexión cerrada exitosamente.");

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}
