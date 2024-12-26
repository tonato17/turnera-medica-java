package com.turneramedica.service;

import com.turneramedica.DAO.MedicoDAO;
import com.turneramedica.DAO.TurnoDAO;
import com.turneramedica.DAO.DAOException;
import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.Turno;

import java.util.List;
import java.time.LocalDate;

public class MedicoService {

    private MedicoDAO medicoDAO;
    private TurnoDAO turnoDAO;

    // Constructor
    public MedicoService() {
        this.medicoDAO = new MedicoDAO();
        this.turnoDAO = new TurnoDAO();  // Inicializamos el TurnoDAO
    }

    // Método para consultar todos los médicos
    public List<Medico> obtenerTodosLosMedicos() throws DAOException {
        return medicoDAO.obtenerTodosLosMedicos();
    }

    // Método para consultar un médico por su legajo
    public Medico obtenerMedicoPorLegajo(String legajo) throws DAOException {
        return medicoDAO.consultarMedico(legajo);
    }

    // Método para guardar un nuevo médico
    public boolean guardarMedico(Medico medico) throws DAOException {
        return medicoDAO.guardarMedico(medico);
    }

    // Método para actualizar la información de un médico
    public boolean actualizarMedico(Medico medico) throws DAOException {
        // Verificar si el médico existe
        Medico medicoExistente = medicoDAO.consultarMedico(medico.getLegajo());
        if (medicoExistente == null) {
            throw new DAOException("El médico con legajo " + medico.getLegajo() + " no existe.");
        }

        // Llamar al método actualizarMedico en el DAO, pasando los parámetros adicionales
        return medicoDAO.actualizarMedico(medico);
    }


    // Método para eliminar un médico
    public boolean eliminarMedico(String legajo) throws DAOException {
        Medico medicoExistente = medicoDAO.consultarMedico(legajo);
        if (medicoExistente == null) {
            throw new DAOException("El médico con legajo " + legajo + " no existe.");
        }
        return medicoDAO.eliminarMedico(legajo);
    }

    // Método para calcular los ingresos de un médico entre dos fechas
    public List<Turno> calcularIngresos(String legajo, LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        // Delegamos la llamada al TurnoDAO para calcular los ingresos
        return turnoDAO.consultarTurnosPorFecha(legajo, fechaInicio, fechaFin);
    }

    public Medico autenticarMedico(String legajo, String password) throws DAOException {
        return medicoDAO.autenticarMedico(legajo, password);
    }

}
