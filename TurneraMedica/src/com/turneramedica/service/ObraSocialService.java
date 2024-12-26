package com.turneramedica.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.turneramedica.DAO.BaseDAO;
import com.turneramedica.DAO.ObraSocialDAO;
import com.turneramedica.DAO.DAOException;
import com.turneramedica.entidades.ObraSocial;
import java.util.List;

public class ObraSocialService extends BaseDAO{

    private ObraSocialDAO obraSocialDAO;

    public ObraSocialService() {
        obraSocialDAO = new ObraSocialDAO();  // Inicialización del DAO
    }

    // Crear una nueva obra social
    public boolean crearObraSocial(ObraSocial obraSocial) throws DAOException {
        // Verificar si ya existe una obra social con el mismo nombre y cobertura
        if (obraSocialDAO.existeObraSocial(obraSocial.getNombre())) {
            throw new DAOException("La obra social ya existe.");
        }

        // Si no existe, la guardamos
        return obraSocialDAO.guardarObraSocial(obraSocial);
    }

    // Consultar una obra social por nombre y cobertura
    public ObraSocial obtenerObraSocial(String nombre, String cobertura) throws DAOException {
        ObraSocial obraSocial = obraSocialDAO.consultarObraSocial(nombre);
        if (obraSocial == null) {
            throw new DAOException("No se encontró la obra social.");
        }
        return obraSocial;
    }

    // Consultar una obra social por ID
    public ObraSocial obtenerObraSocialPorId(int id) throws DAOException {
        ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(id);
        if (obraSocial == null) {
            throw new DAOException("No se encontró la obra social con ese ID.");
        }
        return obraSocial;
    }

    // Actualizar una obra social
    public boolean actualizarObraSocial(ObraSocial obraSocial) throws DAOException {
        // Verificar si la obra social existe antes de actualizar
        if (!obraSocialDAO.existeObraSocial(obraSocial.getNombre())) {
            throw new DAOException("No existe la obra social a actualizar.");
        }

        return obraSocialDAO.actualizarObraSocial(obraSocial);
    }

    // Eliminar una obra social
    public boolean eliminarObraSocial(int id) throws DAOException {
        // Verificar si la obra social existe antes de eliminar
        ObraSocial obraSocial = obraSocialDAO.consultarObraSocialPorId(id);
        if (obraSocial == null) {
            throw new DAOException("No se encontró la obra social para eliminar.");
        }
        obraSocialDAO.eliminarObraSocialPorId(id);
        return true;

    }

    // Obtener todas las obras sociales
    public List<ObraSocial> obtenerTodasLasObrasSociales() throws DAOException {
        return obraSocialDAO.obtenerTodasLasObrasSociales();
    }

}
