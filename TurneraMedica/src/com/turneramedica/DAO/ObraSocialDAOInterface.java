package com.turneramedica.DAO;

import com.turneramedica.entidades.ObraSocial;
import java.util.List;

public interface ObraSocialDAOInterface {

    // Métodos auxiliares
    boolean existeObraSocial(String nombre);
    Integer obtenerIdObraSocial(String nombre) throws DAOException;

    // CRUD
    ObraSocial consultarObraSocialPorId(Integer id) throws DAOException;
    ObraSocial consultarObraSocial(String nombre);
    boolean guardarObraSocial(ObraSocial obraSocial) throws DAOException;
    boolean actualizarObraSocial(ObraSocial obraSocial) throws DAOException;
    boolean eliminarObraSocialPorNombre(String nombre) throws DAOException;
    boolean eliminarObraSocialPorId(Integer idObraSocial) throws DAOException;

    // Recuperar todas las obras sociales
    List<ObraSocial> obtenerTodasLasObrasSociales() throws DAOException;

}
