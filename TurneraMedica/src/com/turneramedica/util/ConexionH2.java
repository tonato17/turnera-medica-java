package com.turneramedica.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionH2 {

    // Método para obtener la conexión a la base de datos H2
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Levantamos el driver H2
        Class.forName("org.h2.Driver");

        // Configuramos la conexión. Cambia la ruta, usuario y contraseña según sea necesario.
        String url = "jdbc:h2:~/turneramedica"; // Puedes cambiar "turneramedica" por el nombre de tu base
        String user = "sa";
        String password = "";

        // Retornamos la conexión establecida
        return DriverManager.getConnection(url, user, password);
    }
}
