package com.turneramedica.DAO;

import com.turneramedica.entidades.ObraSocial;
import com.turneramedica.entidades.Paciente;

import java.util.List;

public interface PacienteDAOInterface {

    // Métodos auxiliares
    Integer obtenerIdPaciente(String nroAfiliado) throws DAOException;
    boolean existePaciente(String nroAfiliado) throws DAOException;

    // CRUD
    Paciente consultarPaciente(String nroAfiliado) throws DAOException;
    boolean guardarPaciente(Paciente paciente) throws DAOException;
    boolean eliminarPaciente(String nroAfiliado) throws DAOException;
    boolean actualizarPaciente(Paciente paciente) throws DAOException;
    List<Paciente> obtenerTodosLosPacientes() throws DAOException;
}
