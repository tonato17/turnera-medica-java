package com.turneramedica.DAO;

import com.turneramedica.entidades.ObraSocial;
import com.turneramedica.entidades.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends BaseDAO implements PacienteDAOInterface {

    private ObraSocialDAO obraSocialDAO = new ObraSocialDAO(); // Para obtener el objeto ObraSocial si es necesario

    // METODOS AUXILIARES

    // Obtiene el ID de un paciente a partir de su número de afiliado. Si lo encuentra, retorna el ID; si no, retorna null.
    @Override
    public Integer obtenerIdPaciente(String nroAfiliado) throws DAOException {
        String sql = "SELECT id_paciente FROM paciente WHERE nro_afiliado = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, nroAfiliado);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retornar el id_paciente si se encuentra
                    return rs.getObject("id_paciente", Integer.class);
                } else {
                    // Retornar null si no se encuentra el paciente
                    return null;
                }
            }
        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al obtener id de Paciente", e);
        }
    }
    @Override
    public boolean existePaciente(String nroAfiliado) throws DAOException {
        String sql = "SELECT 1 FROM paciente WHERE nro_afiliado = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, nroAfiliado);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, significa que el paciente existe
            }

        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al verificar existencia de Paciente", e);
        }
    }

    // CRUD
    @Override
    public Paciente consultarPaciente(String nroAfiliado) throws DAOException {
        Paciente paciente = null;
        String sql = "SELECT * FROM PACIENTE WHERE nro_afiliado = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, nroAfiliado);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Crear el objeto Paciente y asignar los datos
                    paciente = new Paciente();
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setApellido(rs.getString("apellido"));
                    paciente.setDomicilio(rs.getString("domicilio"));
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setNroAfiliado(rs.getString("nro_afiliado"));
                    paciente.setPassword(rs.getString("password"));

                    // Obtener id_obra_social si está presente
                    Integer idObraSocial = (Integer) rs.getObject("id_obra_social");
                    if (idObraSocial != null) {
                        // Consultar la obra social en base al ID
                        ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(idObraSocial);
                        ;
                        paciente.setObraSocial(obraSocial);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar paciente", e);
        }

        return paciente; // Retorna el paciente encontrado o null si no existe
    }
    @Override
    public boolean guardarPaciente(Paciente paciente) throws DAOException {
        String sql = "INSERT INTO PACIENTE (nombre, apellido, domicilio, telefono, nro_afiliado, id_obra_social, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros del INSERT
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDomicilio());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getNroAfiliado());

            // Manejar el atributo id_obra_social
            if (paciente.getObraSocial() != null) {
                stmt.setInt(6, paciente.getObraSocial().getIdObraSocial()); // Usar el ID de ObraSocial
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER); // Si no hay obra social, insertar NULL
            }

            stmt.setString(7, paciente.getPassword());

            // Ejecutar el INSERT y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se insertó una fila correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada para manejar problemas técnicos
            throw new DAOException("Error al guardar Paciente", e);
        }
    }
    @Override
    public boolean eliminarPaciente(String nroAfiliado) throws DAOException {
        String sql = "DELETE FROM PACIENTE WHERE nro_afiliado = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro del número de afiliado
            stmt.setString(1, nroAfiliado);

            // Ejecutar el DELETE y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila, false si no se eliminó ninguna
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada para manejar problemas técnicos
            throw new DAOException("Error al eliminar Paciente", e);
        }
    }
    @Override
    public boolean actualizarPaciente(Paciente paciente) throws DAOException {
        String sql = "UPDATE PACIENTE SET nombre = ?, apellido = ?, domicilio = ?, telefono = ?, password = ?, id_obra_social = ? WHERE nro_afiliado = ?";

        // Validar que los campos obligatorios no sean nulos o vacíos
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
            throw new DAOException("El nombre del paciente no puede ser nulo o vacío.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().isEmpty()) {
            throw new DAOException("El apellido del paciente no puede ser nulo o vacío.");
        }
        if (paciente.getPassword() == null || paciente.getPassword().isEmpty()) {
            throw new DAOException("La contraseña del paciente no puede ser nula o vacía.");
        }
        if (paciente.getNroAfiliado() == null || paciente.getNroAfiliado().isEmpty()) {
            throw new DAOException("El número de afiliado del paciente no puede ser nulo o vacío.");
        }

        Integer idObraSocial = null;

        // Validar obra social si está presente
        if (paciente.getObraSocial() != null) {
            idObraSocial = new ObraSocialDAO().obtenerIdObraSocial(paciente.getObraSocial().getNombre());
            if (idObraSocial == null) {
                throw new DAOException("La obra social especificada no existe: " + paciente.getObraSocial().getNombre());
            }
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la actualización
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDomicilio());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getPassword());
            stmt.setObject(6, idObraSocial); // Será null si el paciente no tiene obra social
            stmt.setString(7, paciente.getNroAfiliado());

            // Ejecutar la actualización
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar Paciente. SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode(), e);
        }
    }
    @Override
    public List<Paciente> obtenerTodosLosPacientes() throws DAOException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM PACIENTE";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Crear un nuevo paciente y cargar sus datos desde la base
                Paciente paciente = new Paciente();
                paciente.setNombre(rs.getString("nombre"));
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setApellido(rs.getString("apellido"));
                paciente.setDomicilio(rs.getString("domicilio"));
                paciente.setTelefono(rs.getString("telefono"));
                paciente.setNroAfiliado(rs.getString("nro_afiliado"));
                paciente.setPassword(rs.getString("password"));
                int idObraSocialPaciente = (rs.getInt("id_obra_social"));
                ObraSocial obraSocial= new ObraSocial();
                obraSocial.setIdObraSocial(idObraSocialPaciente);
                paciente.setObraSocial(obraSocial);


                // Verificar si el paciente tiene una obra social asociada
//                Integer idObraSocial = (Integer) rs.getObject("id_obra_social");
//                if (idObraSocial != null) {
//                    ObraSocialDAO obraSocialDAO = new ObraSocialDAO();
//                    ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(idObraSocial);
//                    paciente.setObraSocial(obraSocial);
//                }

                // Agregar el paciente a la lista
                pacientes.add(paciente);
            }

        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de error
            throw new DAOException("Error al obtener todos los pacientes", e);
        }

        return pacientes;
    }

}


