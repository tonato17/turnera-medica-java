package com.turneramedica.DAO;

import com.turneramedica.entidades.Consultorio;

import com.turneramedica.entidades.Sala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;


public class SalaDAO extends BaseDAO {

    //FUNCIONES AUXILIARES:

    //Recibe nro de sala y nombre de un consultorio y Retorna True si la sala existe y False si no existe
    public boolean existeSala(int nroSala, String nombreConsultorio) throws DAOException {
        String sql = "SELECT 1 FROM SALA WHERE nro_sala = ? AND id_consultorio = ?";

        try {
            // Obtenemos el ID del consultorio
            Integer idConsultorio = new ConsultorioDAO().obtenerIdConsultorio(nombreConsultorio);

            // Si el consultorio no existe, no puede existir la sala
            if (idConsultorio == null) {
                return false;
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Configuramos los parámetros de la consulta
                stmt.setInt(1, nroSala);
                stmt.setInt(2, idConsultorio);

                // Ejecutamos la consulta y verificamos si hay algún resultado
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Si hay un resultado, significa que la sala existe
                }

            }
        } catch (SQLException e) {
            // Lanzamos una excepción personalizada en caso de error
            throw new DAOException("Error al verificar la existencia de la Sala", e);
        }
    }

    // Obtiene el ID de una sala a partir del número de sala y el nombre del consultorio.
    // Si lo encuentra, retorna el ID de la sala; si no, retorna null.
    public Integer obtenerIdSala(int nroSala, String nombreConsultorio) throws DAOException {
        String sql = "SELECT id_sala FROM SALA WHERE nro_sala = ? AND id_consultorio = ?";

        try {
            // Obtener el ID del consultorio usando el nombre
            Integer idConsultorio = new ConsultorioDAO().obtenerIdConsultorio(nombreConsultorio);

            // Si el consultorio no existe, no se puede encontrar la sala
            if (idConsultorio == null) {
                return null;
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Configurar los parámetros de la consulta
                stmt.setInt(1, nroSala);
                stmt.setInt(2, idConsultorio);

                // Ejecutar la consulta
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Retornar el id_sala si se encuentra
                        return rs.getObject("id_sala", Integer.class);
                    } else {
                        // Retornar null si no se encuentra la sala
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al obtener id de Sala", e);
        }
    }
    //----------------------------------------------------------------------------------------

    //FUNCIONES CRUD

    // Consultar una sala específica en un consultorio
    //Si la sala existe retorna el objeto Sala. Si no existe retorna NULL
    public Sala consultarSala(int nroSala, String nombreConsultorio) throws DAOException {
        Sala sala = null;

        // Instanciamos ConsultorioDAO para obtener el ID del consultorio
        ConsultorioDAO consultorioDAO = new ConsultorioDAO();
        Integer idConsultorio = consultorioDAO.obtenerIdConsultorio(nombreConsultorio);

        // Si el consultorio no existe, retorna null directamente
        if (idConsultorio == null) {
            return null;
        }

        String sql = "SELECT * FROM SALA WHERE nro_sala = ? AND id_consultorio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configuramos los parámetros de la consulta
            stmt.setInt(1, nroSala);
            stmt.setInt(2, idConsultorio);

            // Ejecutamos la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si se encuentra la sala, construimos el objeto Sala
                    sala = new Sala();
                    sala.setNroSala(rs.getInt("nro_sala"));

                    // Asignamos el consultorio a la sala
                    Consultorio consultorio = consultorioDAO.consultarConsultorio(nombreConsultorio);
                    sala.setConsultorio(consultorio);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar Sala", e);
        }

        return sala;
    }

    // Guarda una sala. Si el guardado es exitoso retorna True, sino retorna False
    public boolean guardarSala(int nroSala, String nombreConsultorio) throws DAOException {
        String sql = "INSERT INTO SALA (nro_sala, id_consultorio) VALUES (?, ?)";

        // Crear una instancia de ConsultorioDAO y obtener el id del consultorio
        ConsultorioDAO consultorioDAO = new ConsultorioDAO();
        Integer idConsultorio = consultorioDAO.obtenerIdConsultorio(nombreConsultorio);

        // Si el idConsultorio es nulo, no podemos proceder
        if (idConsultorio == null) {
            throw new DAOException("No se pudo encontrar el consultorio con nombre: " + nombreConsultorio);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la inserción
            stmt.setInt(1, nroSala);
            stmt.setInt(2, idConsultorio);

            // Ejecutar el INSERT y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se insertó una fila correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada para manejar problemas técnicos
            throw new DAOException("Error al guardar Sala", e);
        }
    }

    public boolean eliminarSala(int nroSala, String nombreConsultorio) throws DAOException {
        ConsultorioDAO consultorioDAO = new ConsultorioDAO();
        Integer idConsultorio = consultorioDAO.obtenerIdConsultorio(nombreConsultorio);

        // Si el consultorio no existe, retornamos false o lanzamos una excepción
        if (idConsultorio == null) {
            throw new DAOException("No se encontró el consultorio con el nombre: " + nombreConsultorio);
        }

        // Preparar el SQL para eliminar la sala con nroSala y el idConsultorio obtenido
        String sql = "DELETE FROM SALA WHERE nro_sala = ? AND id_consultorio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros del DELETE
            stmt.setInt(1, nroSala);
            stmt.setInt(2, idConsultorio);

            // Ejecutar el DELETE y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila, false si no se eliminó ninguna
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al eliminar la sala", e);
        }
    }

    public List<Sala> obtenerTodasLasSalas() throws DAOException {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT s.nro_sala, c.nombre AS nombre_consultorio, c.direccion, c.telefono, c.capacidad " +
                "FROM SALA s " +
                "JOIN CONSULTORIO c ON s.id_consultorio = c.id_consultorio";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Crear el objeto Consultorio con la información obtenida
                Consultorio consultorio = new Consultorio();
                consultorio.setNombre(rs.getString("nombre_consultorio"));
                consultorio.setDireccion(rs.getString("direccion"));
                consultorio.setTelefonoContacto(rs.getString("telefono"));
                consultorio.setCapacidad(rs.getInt("capacidad"));

                // Crear el objeto Sala con el nro_sala y el objeto Consultorio
                Sala sala = new Sala();
                sala.setNroSala(rs.getInt("nro_sala"));
                sala.setConsultorio(consultorio);

                // Agregar la sala a la lista
                salas.add(sala);
            }

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al obtener todas las salas", e);
        }

        return salas;
    }

}
