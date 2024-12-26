package com.turneramedica.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.turneramedica.entidades.*;
import com.turneramedica.DAO.BaseDAO;
import com.turneramedica.DAO.DAOException;

public class TurnoDAO extends BaseDAO {

    private MedicoDAO medicoDAO = new MedicoDAO();
    private Medico medico = new Medico();

    // METODO AUXILIAR


    // Método para verificar si un turno está disponible
    public boolean turnoDisponible(Turno turno) throws DAOException {
        String sql = "SELECT COUNT(*) FROM TURNO WHERE fecha = ? AND hora = ? AND id_medico = ? AND estado_turno = 'DISPONIBLE'";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int idMedico;
            // Obtener el ID del médico
            try {
                idMedico = medicoDAO.obtenerIdMedico(turno.getMedico().getLegajo());
            } catch (DAOException e) {
                throw new DAOException("No se pudo obtener el ID del médico: " + turno.getMedico().getLegajo(), e);
            }

            // Asignar parámetros
            stmt.setDate(1, Date.valueOf(turno.getFecha()));  // Fecha del turno
            stmt.setInt(2, turno.getHora());   // Hora del turno
            stmt.setInt(3, idMedico);                         // ID del médico

            // Ejecutar la consulta y verificar el resultado
            try (ResultSet rs = stmt.executeQuery()) {
                return (rs.next());        // Retorna true si el conteo es mayor a 0
            }

        } catch (SQLException e) {
            throw new DAOException("Error al verificar si el turno está disponible", e);
        }
    }

    //Creamos turnos DISPONIBLES
    public boolean crearTurno(Turno turno) throws DAOException {

        String sql = "INSERT INTO TURNO (fecha, hora, estado_turno, id_medico, id_paciente, id_consultorio, precio_turno) " +
                "VALUES (?, ?, ?, (SELECT id_medico FROM MEDICO WHERE legajo = ?), ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configuramos los parámetros de la consulta
            stmt.setDate(1, Date.valueOf(turno.getFecha()));  // Fecha del turno
            stmt.setInt(2, turno.getHora());   // Hora del turno
            stmt.setString(3, EstadoTurno.ASIGNADO.name());  // Estado inicial: DISPONIBLE
            stmt.setString(4, turno.getMedico().getLegajo());  // Legajo del médico
            stmt.setInt(5, turno.getPaciente().getIdPaciente()) ;
            stmt.setInt(6, turno.getConsultorio().getIdConsultorio());
            stmt.setDouble(7, turno.getPrecioTurno());


            // Ejecutamos la inserción
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 1) {
                // El turno se ha insertado correctamente, devolvemos true
                return true;
            } else {
                // Si no se insertó, devolvemos false
                return false;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al crear el turno", e);
        }
    }

    private boolean turnoExistente(String legajo, LocalDate fecha, LocalTime hora) throws DAOException {
        String sql = "SELECT COUNT(*) FROM TURNO " +
                "WHERE id_medico = (SELECT id_medico FROM MEDICO WHERE legajo = ?) " +
                "AND fecha = ? " +
                "AND hora = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, legajo);  // Legajo del médico
            stmt.setDate(2, Date.valueOf(fecha));  // Fecha del turno
            stmt.setTime(3, Time.valueOf(hora));  // Hora del turno

            try (ResultSet rs = stmt.executeQuery()) {

                    return rs.next();  // Si hay al menos un turno, significa que ya está asignado
                }

        } catch (SQLException e) {
            throw new DAOException("Error al verificar la disponibilidad del turno", e);
        }

    }

    // Método para obtener los turnos de un médico en un rango de fechas
    public List<Turno> consultarTurnosPorFecha(String legajo, LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT t.fecha as tfecha, t.hora as thora, t.precio_turno as tprecio_turno, t.estado_turno as testado_turno, m.nombre as mnombre, m.apellido as mapellido, m.legajo as mlegajo, m.especialidad as mespecialidad, p.nombre as pnombre, p.apellido as papellido, c.nombre as cnombre " +
                "FROM TURNO t " +
                "JOIN MEDICO m ON t.id_medico = m.id_medico " +
                "JOIN PACIENTE p ON t.id_paciente = p.id_paciente " +
                "JOIN CONSULTORIO c ON t.id_consultorio = c.id_consultorio " +
                "WHERE m.legajo = ? " +
                "AND t.fecha >= ? AND t.fecha <= ? " +
                "ORDER BY t.fecha, t.hora";  // Ordenamos por fecha y hora para que aparezcan en orden cronológico

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configuramos los parámetros del SQL
            stmt.setString(1, legajo);
            stmt.setDate(2, Date.valueOf(fechaInicio));  // Convierte LocalDate a SQL Date
            stmt.setDate(3, Date.valueOf(fechaFin));     // Convierte LocalDate a SQL Date

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Creamos el objeto Turno
                    Turno turno = new Turno();
                    turno.setFecha(rs.getDate("tfecha").toLocalDate());
                    turno.setHora(rs.getInt("thora"));
                    turno.setPrecio(rs.getDouble("tprecio_turno"));
                    turno.setEstadoTurno(EstadoTurno.valueOf(rs.getString("testado_turno")));

                    // Asignamos el paciente
                    Paciente paciente = new Paciente();
                    paciente.setNombre(rs.getString("pnombre"));
                    paciente.setApellido(rs.getString("papellido"));
                    turno.setPaciente(paciente);

                    // Asignamos el médico
                    Medico medico = new Medico();
                    medico.setLegajo(rs.getString("mlegajo"));
                    medico.setNombre(rs.getString("mnombre"));
                    medico.setApellido(rs.getString("mapellido"));
                    medico.setEspecialidad(rs.getString("mespecialidad"));
                    turno.setMedico(medico);

                    // Asignamos consultorio
                    Consultorio consultorio = new Consultorio();
                    consultorio.setNombre(rs.getString("cnombre"));
                    turno.setConsultorio(consultorio);


                    // Agregamos el turno a la lista
                    turnos.add(turno);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar los turnos del médico", e);
        }

        return turnos;
    }

    //Metodo para obtener todos los turnos de un medico en una fecha determinada


    public List<Turno> consultarTurnosFecha(String legajo, LocalDate fecha) throws DAOException {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT t.*, m.legajo as mlegajo, m.nombre as mnombre, m.apellido as mapellido, m.especialidad as mespecialidad, p.nombre as pnombre, p.apellido as papellido, p.nro_afiliado as pnro_afiliado, c.nombre as cnombre " +
                "FROM TURNO t " +
                "JOIN MEDICO m ON t.id_medico = m.id_medico " +
                "JOIN PACIENTE p ON t.id_paciente = p.id_paciente " +
                "JOIN CONSULTORIO c ON t.id_consultorio = c.id_consultorio " +
                "WHERE m.legajo = ? " +
                "AND t.fecha = ? " +
                "ORDER BY t.fecha, t.hora desc";  // Ordenamos por fecha y hora para que aparezcan en orden cronológico

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configuramos los parámetros del SQL
            stmt.setString(1, legajo);
            stmt.setDate(2, Date.valueOf(fecha));  // Convierte LocalDate a SQL Date

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Creamos el objeto Turno
                    Turno turno = new Turno();
                    turno.setFecha(rs.getDate("fecha").toLocalDate());
                    turno.setHora(rs.getInt("hora"));
                    turno.setIdTurno(rs.getInt("id_turno"));
                    turno.setEstadoTurno(EstadoTurno.valueOf(rs.getString("estado_turno")));

                    // Asignamos el paciente
                    Paciente paciente = new Paciente();
                    paciente.setNombre(rs.getString("pnombre"));
                    paciente.setApellido(rs.getString("papellido"));
                    paciente.setNroAfiliado(rs.getString("pnro_afiliado"));
                    turno.setPaciente(paciente);

                    // Asignamos el médico
                    Medico medico = new Medico();
                    medico.setLegajo(rs.getString("mlegajo"));
                    medico.setNombre(rs.getString("mnombre"));
                    medico.setApellido(rs.getString("mapellido"));
                    medico.setEspecialidad(rs.getString("mespecialidad"));
                    turno.setMedico(medico);

                    // Asignamos consultorio
                    Consultorio consultorio = new Consultorio();
                    consultorio.setNombre(rs.getString("cnombre"));
                    turno.setConsultorio(consultorio);


                    // Agregamos el turno a la lista
                    turnos.add(turno);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar los turnos del médico", e);
        }

        return turnos;
    }




    public List <Turno> consultarTurnoPorFechaHora(String legajo, LocalDate fecha, int hora) throws DAOException {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT t.*, m.*, p.*, c.* " +
                "FROM TURNO t " +
                "JOIN MEDICO m ON t.id_medico = m.id_medico " +
                "JOIN PACIENTE p ON t.id_paciente = p.id_paciente " +
                "JOIN CONSULTORIO c ON t.id_consultorio = c.id_consultorio " +
                "WHERE m.legajo = ? " +
                "AND t.fecha = ? AND t.hora = ? ";
                  // Ordenamos por fecha y hora para que aparezcan en orden cronológico

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configuramos los parámetros del SQL
            stmt.setString(1, legajo);
            stmt.setDate(2, Date.valueOf(fecha));  // Convierte LocalDate a SQL Date
            stmt.setInt(3, hora);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Creamos el objeto Turno
                    Turno turno = new Turno();
                    turno.setFecha(rs.getDate("fecha").toLocalDate());
                    turno.setHora(rs.getInt("hora"));
                    turno.setEstadoTurno(EstadoTurno.valueOf(rs.getString("estado_turno")));




                    // Agregamos el turno a la lista
                    turnos.add(turno);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar los turnos del médico en esa fecha y hora", e);
        }

        return turnos;
    }



    public boolean actualizarTurno(Turno turno) throws DAOException {
        String sql = "UPDATE TURNO SET fecha = ?, hora = ?, estado_turno = ?, id_paciente = ?, id_consultorio = ? WHERE id_turno = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(turno.getFecha()));
            stmt.setInt(2, turno.getHora());
            stmt.setString(3, turno.getEstadoTurno().name());
            stmt.setInt(4, turno.getPaciente().getIdPaciente());
            stmt.setInt(5, turno.getConsultorio().getIdConsultorio());
            stmt.setInt(6, turno.getIdTurno());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el turno", e);
        }
    }

// aca obtenemos todos los turnos
    public List<Turno> consultarTurnos() throws DAOException {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT t.*, m.*, p.nombre as pnombre, p.apellido as papellido, p.nro_afiliado as pafiliado, c.nombre as cnombre " +
                "FROM TURNO t " +
                "JOIN MEDICO m ON t.id_medico = m.id_medico " +
                "JOIN PACIENTE p ON t.id_paciente = p.id_paciente " +
                "JOIN CONSULTORIO c ON t.id_consultorio = c.id_consultorio " +
                "ORDER BY t.fecha, t.hora";  // Ordenamos por fecha y hora para que aparezcan en orden cronológico

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Creamos el objeto Turno
                    Turno turno = new Turno();
                    turno.setFecha(rs.getDate("fecha").toLocalDate());
                    turno.setIdTurno(rs.getInt("id_turno"));
                    turno.setHora(rs.getInt("hora"));
                    turno.setPrecio(rs.getDouble("precio_turno"));
                    turno.setEstadoTurno(EstadoTurno.valueOf(rs.getString("estado_turno")));

                    // Asignamos el paciente
                    Paciente paciente = new Paciente();
                    paciente.setNombre(rs.getString("pnombre"));
                    paciente.setApellido(rs.getString("papellido"));
                    paciente.setNroAfiliado(rs.getString("pafiliado"));
                    turno.setPaciente(paciente);

                    // Asignamos el médico
                    Medico medico = new Medico();
                    medico.setLegajo(rs.getString("legajo"));
                    medico.setNombre(rs.getString("nombre"));
                    medico.setApellido(rs.getString("apellido"));
                    medico.setEspecialidad(rs.getString("especialidad"));
                    turno.setMedico(medico);

                    // Asignamos consultorio
                    Consultorio consultorio = new Consultorio();
                    consultorio.setNombre(rs.getString("cnombre"));
                    turno.setConsultorio(consultorio);


                    // Agregamos el turno a la lista
                    turnos.add(turno);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar los turnos del médico", e);
        }

        return turnos;
    }





}
