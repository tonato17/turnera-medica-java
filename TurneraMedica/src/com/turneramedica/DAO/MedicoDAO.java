package com.turneramedica.DAO;

import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.ObraSocial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MedicoDAO extends BaseDAO implements MedicoDAOInterface {

    private ObraSocialDAO obraSocialDAO = new ObraSocialDAO(); // Para obtener la obra social si es necesario

    // METODOS AUXILIARES

    // Obtiene el ID de un médico a partir de su legajo. Si lo encuentra, retorna el ID; si no, retorna null.
    @Override
    public Integer obtenerIdMedico(String legajo) throws DAOException {
        String sql = "SELECT id_medico FROM medico WHERE legajo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, legajo);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retornar el id_medico si se encuentra
                    return rs.getObject("id_medico", Integer.class);
                } else {
                    // Retornar null si no se encuentra el médico
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener id de Medico", e);
        }
    }

    // Verifica si un médico existe según su legajo
    @Override
    public boolean existeMedico(String legajo) throws DAOException {
        String sql = "SELECT 1 FROM medico WHERE legajo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro de la consulta
            stmt.setString(1, legajo);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, significa que el médico existe
            }

        } catch (SQLException e) {
            throw new DAOException("Error al verificar existencia de Medico", e);
        }
    }

    // CRUD

    // Consultar un médico específico por su legajo
    public Medico consultarMedico(String legajo) throws DAOException {
        Medico medico = null;
        String sql = "SELECT nombre, apellido, domicilio, telefono, legajo, dni, password, especialidad, id_obra_social FROM MEDICO WHERE legajo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //generamos el statement, lo preparamos

            // Configurar el parámetro de la consulta
            stmt.setString(1, legajo);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    medico = new Medico();
                    medico.setNombre(rs.getString("nombre"));
                    medico.setApellido(rs.getString("apellido"));
                    medico.setDomicilio(rs.getString("domicilio"));
                    medico.setTelefono(rs.getString("telefono"));
                    medico.setLegajo(rs.getString("legajo"));
                    medico.setDni(rs.getString("dni"));
                    medico.setPassword(rs.getString("password"));
                    medico.setEspecialidad(rs.getString("especialidad"));

                    // Usar la instancia de ObraSocialDAO ya existente
                    int idObraSocial = rs.getInt("id_obra_social");
                    if (idObraSocial != 0) {
                        ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(idObraSocial);
                        medico.setObraSocial(obraSocial);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar Medico", e);
        }

        return medico; // Retorna el médico encontrado o null si no existe
    }

    // Guardar un médico. Retorna True o False si el medico fue guardado o no correctamente
    @Override
    public boolean guardarMedico(Medico medico) throws DAOException {
        String sql = "INSERT INTO MEDICO (nombre, apellido, domicilio, telefono, legajo, dni, password, especialidad, tarifa_por_turno, email, id_obra_social) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros del INSERT
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setString(3, medico.getDomicilio());
            stmt.setString(4, medico.getTelefono());
            stmt.setString(5, medico.getLegajo());
            stmt.setString(6, medico.getDni());
            stmt.setString(7, medico.getPassword());
            stmt.setString(8, medico.getEspecialidad()); // Asignar especialidad como String
            stmt.setDouble(9, medico.getTarifaPorTurno()); // Asignar tarifa por turno
            stmt.setString(10, medico.getEmail());
            stmt.setInt(11, medico.getObraSocial().getIdObraSocial());

            // Manejar el atributo id_obra_social
//            if (medico.getObraSocial() != null) {
//                stmt.setInt(11, medico.getObraSocial().getIdObraSocial()); // Usar el ID de ObraSocial
//            } else {
                //stmt.setNull(11, java.sql.Types.INTEGER); // Si no hay obra social, insertar NULL
            //}

            // Ejecutar el INSERT y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas == 1; //Retorna true si inserto 1 fila o false si no inserto.
        } catch (SQLException e) {
            throw new DAOException("Error al guardar Medico", e);
        }
    }


    // Eliminar un médico. Retornar true si se eliminó una fila, false si no se eliminó ninguna
    @Override
    public boolean eliminarMedico(String legajo) throws DAOException {
        String sql = "DELETE FROM MEDICO WHERE legajo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar el parámetro del legajo
            stmt.setString(1, legajo);

            // Ejecutar el DELETE y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila, false si no se eliminó ninguna
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar Medico", e);
        }
    }

    // Actualizar los datos de un médico
    @Override
    public boolean actualizarMedico(Medico medico) throws DAOException {
        String sql = "UPDATE MEDICO SET nombre = ?, apellido = ?, domicilio = ?, telefono = ?, password = ?, especialidad = ?, tarifa_por_turno = ?, email =? WHERE legajo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Obtenemos el objeto obraSocial del medico.
//            ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(medico.getObraSocial().getIdObraSocial());
//
//             //Validar si la obra social existe
//            if (obraSocial == null) {
//                throw new DAOException("La obra social con id '" + medico.getObraSocial().getIdObraSocial()
//                        + "' no existe.");
//            }
            // Configurar los parámetros de la actualización
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setString(3, medico.getDomicilio());
            stmt.setString(4, medico.getTelefono());
            stmt.setString(5, medico.getPassword());
            stmt.setString(6, medico.getEspecialidad());
            stmt.setDouble(7, medico.getTarifaPorTurno());
            stmt.setString(8, medico.getEmail());
            //stmt.setInt(7, medico.getObraSocial().getIdObraSocial()); // Asignar el ID de la obra social
            stmt.setString(9, medico.getLegajo());


            // Ejecutar la actualización
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas == 1; // Retorna true si se actualizó una fila

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar Médico", e);
        }
    }

    // Obtener todos los médicos
    @Override
    public List<Medico> obtenerTodosLosMedicos() throws DAOException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM MEDICO";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Iterar sobre los resultados
            while (rs.next()) {
                Medico medico = new Medico();
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setDomicilio(rs.getString("domicilio"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setEmail(rs.getString("email"));
                medico.setLegajo(rs.getString("legajo"));
                medico.setDni(rs.getString("dni"));
                medico.setEspecialidad(rs.getString("especialidad"));
                medico.setTarifaPorTurno(rs.getInt("tarifa_por_turno"));
                medico.setPassword(rs.getString("password"));
                int idObraSocial = rs.getInt("id_obra_social");
                //obtengo el objeto obra social por su id.
                ObraSocial obraSocial = new ObraSocial();
                obraSocial.setIdObraSocial(idObraSocial);
                medico.setObraSocial(obraSocial);


                 //Agregar el médico a la lista
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener todos los médicos", e);
        }

        return medicos; // Retorna la lista de médicos con sus obras sociales
    }

    @Override
    public Medico autenticarMedico(String legajo, String password) throws DAOException {
        String sql = "SELECT * FROM MEDICO WHERE legajo = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, legajo);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Medico medico = new Medico();
                    medico.setNombre(rs.getString("nombre"));
                    medico.setApellido(rs.getString("apellido"));
                    medico.setLegajo(rs.getString("legajo"));
                    medico.setPassword(rs.getString("password"));
                    // Otros campos del médico
                    return medico;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al autenticar médico", e);
        }
        return null; // Si no se encuentra el médico
    }


}

