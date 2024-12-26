package com.turneramedica.DAO;

import com.turneramedica.entidades.Consultorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

public class ConsultorioDAO extends BaseDAO{

    // FUNCIONES AUXILIARES

    // Corrobora si existe un consultorio por su nombre. Si existe retorna True, si no existe retorna False
    public boolean existeConsultorio(String nombre) {
        String sql = "SELECT * FROM CONSULTORIO WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, significa que existe
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepción en el DAO
            return false; // Retorna false en caso de error
        }
    }

    // Obtiene el ID de un consultorio con su nombre. Si lo encuentra retorna el id, si NO lo encuentra retorna NULL
    public Integer obtenerIdConsultorio(String nombre) throws DAOException {
        String sql = "SELECT id_consultorio FROM CONSULTORIO WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, nombre);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retornar el id_consultorio si se encuentra
                    return rs.getInt("id_consultorio");
                } else {
                    // Retornar null si no se encuentra el consultorio
                    return null;
                }
            }
        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al obtener id de Consultorio", e);
        }
    }

    //------------------------------------------------------------------------------------

    // Consultar un consultorio por nombre. Retorna el consultorio o NULL si no lo encuentra.
    public Consultorio consultarConsultorio(String nombre) throws DAOException {
        Consultorio consultorio = null;
        String sql = "SELECT * FROM CONSULTORIO WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de búsqueda
            stmt.setString(1, nombre);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si se encuentra un resultado, crear un objeto Consultorio
                    consultorio = new Consultorio();
                    consultorio.setNombre(rs.getString("nombre"));
                    consultorio.setDireccion(rs.getString("direccion"));
                    consultorio.setTelefonoContacto(rs.getString("telefono"));
                    consultorio.setCapacidad(rs.getInt("capacidad"));
                }
            }
        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al consultar Consultorio", e);
        }

        return consultorio; // Retorna el objeto o null si no se encuentra
    }

    // GUARDAR CONSULTORIO. Retorna True si lo guarda. False si no lo guarda.
    public boolean guardarConsultorio(Consultorio consultorio) throws DAOException {
        String sql = "INSERT INTO CONSULTORIO (nombre, direccion, telefono, capacidad) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la inserción
            stmt.setString(1, consultorio.getNombre());
            stmt.setString(2, consultorio.getDireccion());
            stmt.setString(3, consultorio.getTelefonoContacto());
            stmt.setInt(4, consultorio.getCapacidad());

            // Ejecutar el INSERT y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se insertó una fila correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada para manejar problemas técnicos
            throw new DAOException("Error al guardar Consultorio", e);
        }
    }

    // ELIMINA un consultorio por su nombre. Retorna True si lo elimina False si no lo elimina
    public boolean eliminarConsultorio(String nombre) throws DAOException {
        String sql = "DELETE FROM CONSULTORIO WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro para la eliminación
            stmt.setString(1, nombre);

            // Ejecutar la sentencia DELETE y verificar si afectó alguna fila
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al eliminar Consultorio", e);
        }
    }

    // Actualiza un registro en la tabla CONSULTORIO. Recibe el objeto consultorio y retorna True si logra actualizarlo, o False si no.
    public boolean actualizarConsultorio(Consultorio consultorio) throws DAOException {
        // se permite actualizar el nombre, recordar hacer la validacion en el service para que el nombre del consultorio no este repetido
        String sql = "UPDATE CONSULTORIO SET direccion = ?, telefono = ?, capacidad = ? WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros para la actualización
            stmt.setString(1, consultorio.getDireccion());
            stmt.setString(2, consultorio.getTelefonoContacto());
            stmt.setInt(3, consultorio.getCapacidad());
            stmt.setString(4, consultorio.getNombre());

            // Ejecutar la actualización y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si una fila fue actualizada correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de problemas técnicos
            throw new DAOException("Error al actualizar Consultorio", e);
        }
    }

    // OBtenemos todos los consultorios
    public List<Consultorio> obtenerTodosLosConsultorios() throws DAOException {
        List<Consultorio> consultorios = new ArrayList<>();
        String sql = "SELECT * FROM CONSULTORIO";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consultorio consultorio = new Consultorio();
                consultorio.setIdConsultorio(rs.getInt("id_consultorio"));
                consultorio.setNombre(rs.getString("nombre"));
                consultorio.setDireccion(rs.getString("direccion"));
                consultorio.setTelefonoContacto(rs.getString("telefono"));
                consultorio.setCapacidad(rs.getInt("capacidad"));

                consultorios.add(consultorio);
            }

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al obtener todos los consultorios", e);
        }

        return consultorios;
    }


}
