package com.turneramedica.service;

import com.turneramedica.DAO.PacienteDAO;
import com.turneramedica.DAO.DAOException;
import com.turneramedica.entidades.Paciente;

import java.util.List;

public class PacienteService {

    private PacienteDAO pacienteDAO;

    // Constructor
    public PacienteService() {
        this.pacienteDAO = new PacienteDAO();
    }

    // Método para consultar todos los pacientes
    public List<Paciente> obtenerTodosLosPacientes() throws DAOException {
        return pacienteDAO.obtenerTodosLosPacientes();
    }

    // Método para consultar un paciente por su número de afiliado
    public Paciente obtenerPacientePorAfiliado(String nroAfiliado) throws DAOException {
        return pacienteDAO.consultarPaciente(nroAfiliado);
    }

    // Método para guardar un nuevo paciente
    public boolean guardarPaciente(Paciente paciente) throws DAOException {
        return pacienteDAO.guardarPaciente(paciente);
    }

    // Método para actualizar la información de un paciente
    public boolean actualizarPaciente(Paciente paciente) throws DAOException {
        Paciente pacienteExistente = pacienteDAO.consultarPaciente(paciente.getNroAfiliado());
        if (pacienteExistente == null) {
            throw new DAOException("El paciente con número de afiliado " + paciente.getNroAfiliado() + " no existe.");
        }
        return pacienteDAO.actualizarPaciente(paciente);
    }

    // Método para eliminar un paciente
    public boolean eliminarPaciente(String nroAfiliado) throws DAOException {
        Paciente pacienteExistente = pacienteDAO.consultarPaciente(nroAfiliado);
        if (pacienteExistente == null) {
            throw new DAOException("El paciente con número de afiliado " + nroAfiliado + " no existe.");
        }
        return pacienteDAO.eliminarPaciente(nroAfiliado);
    }
}
