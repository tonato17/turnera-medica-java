package com.turneramedica.DAO;

import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.ObraSocial;

import java.util.List;

public interface MedicoDAOInterface {

    // Métodos auxiliares
    Integer obtenerIdMedico(String legajo) throws DAOException;
    boolean existeMedico(String legajo) throws DAOException;
    Medico autenticarMedico(String legajo, String password) throws DAOException;

    // CRUD
    Medico consultarMedico(String legajo) throws DAOException;
    boolean guardarMedico(Medico medico) throws DAOException;
    boolean eliminarMedico(String legajo) throws DAOException;
    boolean actualizarMedico(Medico medico) throws DAOException;
    List<Medico> obtenerTodosLosMedicos() throws DAOException;

}
