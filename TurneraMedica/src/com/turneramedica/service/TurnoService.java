package com.turneramedica.service;

import com.turneramedica.DAO.TurnoDAO;
import com.turneramedica.DAO.DAOException;
import com.turneramedica.entidades.EstadoTurno;
import com.turneramedica.entidades.Turno;
import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.Paciente;
import com.turneramedica.entidades.ReporteMedico;
import com.turneramedica.DAO.PacienteDAO;


import java.time.LocalDate;
import java.util.List;

public class TurnoService {
    private PacienteDAO pacienteDAO = new PacienteDAO(); // O lo inyectas si usas inyección de dependencias


    private TurnoDAO turnoDAO;

    // Constructor
    public TurnoService() {
        this.turnoDAO = new TurnoDAO();
    }

    // 1. **Consultar los turnos disponibles de un médico según una fecha**
    public List<Turno> obtenerTurnosDisponibles(Medico medico, LocalDate fecha) throws DAOException {
        return turnoDAO.consultarTurnosPorFecha(medico.getLegajo(), fecha, fecha);  // Consulta los turnos de un solo día
    }

    // 2. **Crear un nuevo turno para un médico**
    public boolean crearTurno(Turno turno) throws DAOException {
        return turnoDAO.crearTurno(turno);  // Crea un turno para el médico en la fecha y hora indicada
    }

    public boolean existeTurno(String legajo, LocalDate fecha, int hora) throws DAOException {
        return turnoDAO.consultarTurnoPorFechaHora(legajo, fecha, hora).size() > 0;
    }


    // 4. **Calcular los ingresos de un médico entre dos fechas**
    public double calcularIngresosMedico(Medico medico, LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        List<Turno> turnos = turnoDAO.consultarTurnosPorFecha(medico.getLegajo(), fechaInicio, fechaFin);

        double totalIngresos = 0;
        for (Turno turno : turnos) {
            // Solo sumamos ingresos si el turno está ocupado
            if (turno.getEstadoTurno() == EstadoTurno.ASIGNADO) {
                totalIngresos += medico.getTarifaPorTurno();  // Sumamos la tarifa del médico por cada turno ocupado
            }
        }
        return totalIngresos;
    }

    public boolean tomarTurno(Turno turno, String nroAfiliado) throws DAOException {
        // Primero verificamos si el turno está disponible
        if (!turnoDAO.turnoDisponible(turno)) {
            throw new DAOException("El turno no está disponible.");
        }

        // Obtener el ID del paciente a partir del número de afiliado
        Integer idPaciente = pacienteDAO.obtenerIdPaciente(nroAfiliado);

        // Verificar si el paciente existe
        if (idPaciente == null) {
            throw new DAOException("No se encontró el paciente con el número de afiliado proporcionado.");
        }

        // Asignar el paciente al turno y cambiar el estado a OCUPADO
        turno.setPaciente(new Paciente(idPaciente));  // Suponiendo que el constructor de Paciente acepta un ID
        turno.setEstadoTurno(EstadoTurno.ASIGNADO);

        // Actualizar el turno en la base de datos
        return turnoDAO.actualizarTurno(turno);  // Actualizamos el turno con el paciente asignado
    }

    public void actualizarTurno(Turno turno) throws DAOException {
        turnoDAO.actualizarTurno(turno);
    }

    public List<Turno> obtenerTodosLosTurnos() throws DAOException {
        return turnoDAO.consultarTurnos();
    }
    public List<Turno> obtenerTurnosPorMedicoYFecha(String legajoMedico, LocalDate fecha) throws DAOException {
        return turnoDAO.consultarTurnosPorFecha(legajoMedico, fecha, fecha);
    }

    public List<Turno> obtenerTurnosMedicoFecha(String legajoMedico, LocalDate fecha) throws DAOException {
        return turnoDAO.consultarTurnosFecha(legajoMedico, fecha);
    }

    public ReporteMedico generarReporteMedico(String legajoMedico, LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        // 1. Obtener los turnos del médico en el rango de fechas
        List<Turno> turnos = turnoDAO.consultarTurnosPorFecha(legajoMedico, fechaInicio, fechaFin);

        // 2. Verificar si hay turnos
        if (turnos.isEmpty()) {
            throw new DAOException("No se encontraron turnos para el médico en el rango de fechas especificado.");
        }

        // 3. Calcular los datos del reporte
        int consultasRealizadas = turnos.size();
        double totalCobrado = turnos.stream()
                .mapToDouble(Turno::getPrecioTurno)
                .sum();
        Medico medico = turnos.get(0).getMedico(); // Obtener información del médico desde el primer turno

        // 4. Crear y devolver el objeto ReporteMedico
        return new ReporteMedico(
                medico.getNombre() + " " + medico.getApellido(), // Nombre completo del médico
                medico.getEspecialidad(), // Especialidad del médico
                consultasRealizadas, // Número de consultas
                totalCobrado, // Total cobrado
                turnos // Lista de turnos
        );
    }

}
