package com.turneramedica.DAO;

import com.turneramedica.entidades.Turno;
import com.turneramedica.entidades.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoDAOInterface {

    // Métodos auxiliares
    boolean turnoDisponible(Turno turno) throws DAOException;

    // CRUD
    boolean crearTurno(Turno turno) throws DAOException;
    boolean actualizarTurno(Turno turno) throws DAOException;

    // Consultas
    List<Turno> consultarTurnos() throws DAOException;
    List<Turno> consultarTurnosPorFecha(String legajo, LocalDate fechaInicio, LocalDate fechaFin) throws DAOException;
    List<Turno> consultarTurnosFecha(String legajo, LocalDate fecha) throws DAOException;
    List<Turno> consultarTurnoPorFechaHora(String legajo, LocalDate fecha, int hora) throws DAOException;

}
