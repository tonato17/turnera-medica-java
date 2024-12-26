package com.turneramedica.DAO;

import com.turneramedica.entidades.ObraSocial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;


public class ObraSocialDAO extends BaseDAO implements ObraSocialDAOInterface{

    // METODOS AUXILIARES:

    // este metodo corrobora si una obra social se encuentra en la bbdd, recibe nombre y cobertura y devuelve True si se encuentra y False si no se encuentra.
    public boolean existeObraSocial(String nombre) {
        String sql = "SELECT 1 FROM OBRA_SOCIAL WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, significa que existe
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejamos la excepción en el DAO
            return false; // En caso de error, podríamos lanzar una excepción o devolver false
        }
    }

    // Obtiene el id de una obra social a partir de nombre y cobertura. Si NO lo encuentra retorna NULL
    public Integer obtenerIdObraSocial(String nombre) throws DAOException {
        String sql = "SELECT id_obra_social FROM OBRA_SOCIAL WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la consulta
            stmt.setString(1, nombre);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retornar el id_obra_social si se encuentra
                    return rs.getInt("id_obra_social");
                }
                else {
                    // Retornar null si no se encuentra la obra social
                    return null;
                }
            }
        }
        catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al obtener id de ObraSocial", e);
        }
    }


    //-------------------------------------------------------------------------------------
    //TESTEADA y FUNCIONANDO OK
    // Este metodo obtiene el objeto obrasocial a partir de su id. Si no la encuentra retorna null
    @Override
    public ObraSocial consultarObraSocialPorId(Integer id) throws DAOException {
        String sql = "SELECT * FROM OBRA_SOCIAL WHERE id_obra_social = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ObraSocial obraSocial = new ObraSocial();
                    obraSocial.setIdObraSocial(rs.getInt("id_obra_social"));
                    obraSocial.setNombre(rs.getString("nombre"));
                    obraSocial.setTelefonoContacto(rs.getString("telefono_contacto"));
                    obraSocial.setDireccion(rs.getString("direccion"));
                    obraSocial.setEmailContacto(rs.getString("email_contacto"));
                    obraSocial.setEstado(rs.getString("estado"));
                    return obraSocial;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar ObraSocial por ID", e);
        }

        return null; // Retorna null si no se encuentra la obra social
    }

    // TESTEADA y FUNCIONANDO OK
    // Retorna el objeto ObraSocial si lo encuentra o NULL si no se encuentra/no hay obra social con los parametros recibidos.
    @Override
    public ObraSocial consultarObraSocial(String nombre) {
        ObraSocial obraSocial = null;
        String sql = "SELECT * FROM OBRA_SOCIAL WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la consulta
            stmt.setString(1, nombre);


            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si se encuentra un resultado, crear un objeto ObraSocial y completar todos sus campos
                    obraSocial = new ObraSocial();
                    obraSocial.setIdObraSocial(rs.getInt("id_obra_social"));
                    obraSocial.setNombre(rs.getString("nombre"));
                    obraSocial.setTelefonoContacto(rs.getString("telefono_contacto"));
                    obraSocial.setDireccion(rs.getString("direccion"));
                    obraSocial.setEmailContacto(rs.getString("email_contacto"));
                    obraSocial.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // O manejar la excepción con un logger
        }

        return obraSocial; // Retorna el objeto o null si no se encuentra
    }

    //TESTEADO Y FUNCIONANDO OK
    // Guarda una nueva obra social en la base de datos. Retorna True si se guardo y False si no se pudo guardar
    @Override
    public boolean guardarObraSocial(ObraSocial obraSocial) throws DAOException {
        String sql = "INSERT INTO OBRA_SOCIAL (nombre, telefono_contacto, direccion, email_contacto, estado) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros de la inserción
            stmt.setString(1, obraSocial.getNombre());
            stmt.setString(2, obraSocial.getTelefonoContacto());
            stmt.setString(3, obraSocial.getDireccion());
            stmt.setString(4, obraSocial.getEmailContacto());
            stmt.setString(5, obraSocial.getEstado());

            // Ejecutar el INSERT y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            System.out.println(filasAfectadas);

            // Retornar true si se insertó una fila correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada para manejar problemas técnicos
            throw new DAOException("Error al guardar ObraSocial", e);
        }
    }

    // TESTEADA Y FUNCIONANDO OK
    // Actualiza un registro en obra_social. Recibe el objeto obraSocial y retorna True si lo logra actualizar y False si no.
    @Override
    public boolean actualizarObraSocial(ObraSocial obraSocial) throws DAOException {
        String sql = "UPDATE OBRA_SOCIAL SET telefono_contacto = ?, direccion = ?, email_contacto = ?, estado = ?, nombre = ? WHERE id_obra_social = ? ";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros para la actualización
            stmt.setString(1, obraSocial.getTelefonoContacto());
            stmt.setString(2, obraSocial.getDireccion());
            stmt.setString(3, obraSocial.getEmailContacto());
            stmt.setString(4, obraSocial.getEstado()); // En caso de que el estado cambie
            stmt.setString(5, obraSocial.getNombre());
            stmt.setInt(6, obraSocial.getIdObraSocial());

            // Ejecutar la actualización y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            System.out.println(filasAfectadas);

            // Retornar true si una fila fue actualizada correctamente
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada en caso de problemas técnicos
            throw new DAOException("Error al actualizar ObraSocial", e);
        }
    }

    // Elimina una obra social basada en nombre su nombre
    public boolean eliminarObraSocialPorNombre(String nombre) throws DAOException {
        String sql = "DELETE FROM OBRA_SOCIAL WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros para el DELETE
            stmt.setString(1, nombre);

            // Ejecutar el DELETE y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila, false si no se eliminó ninguna
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al eliminar ObraSocial", e);
        }
    }

    // Elimina una obra social basada en su id
    @Override
    public boolean eliminarObraSocialPorId(Integer id_obra_social) throws DAOException {
        if (id_obra_social == null) {
            throw new DAOException("El ID de la obra social no puede ser nulo.");
        }

        String sql = "DELETE FROM OBRA_SOCIAL WHERE id_obra_social = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar los parámetros para el DELETE
            stmt.setInt(1, id_obra_social);

            // Ejecutar el DELETE y verificar filas afectadas
            int filasAfectadas = stmt.executeUpdate();

            // Retornar true si se eliminó una fila, false si no se eliminó ninguna
            return filasAfectadas == 1;

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al eliminar ObraSocial", e);
        }
    }



    // Recupera todas las obras sociales en la base de datos. Retorna un tipo de dato Lista.
    @Override
    public List<ObraSocial> obtenerTodasLasObrasSociales() throws DAOException {
        List<ObraSocial> obrasSociales = new ArrayList<>();
        String sql = "SELECT * FROM OBRA_SOCIAL";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObraSocial obraSocial = new ObraSocial();
                obraSocial.setNombre(rs.getString("nombre"));
                obraSocial.setTelefonoContacto(rs.getString("telefono_contacto"));
                obraSocial.setDireccion(rs.getString("direccion"));
                obraSocial.setEmailContacto(rs.getString("email_contacto"));
                obraSocial.setEstado(rs.getString("estado"));

                obrasSociales.add(obraSocial);
            }

        } catch (SQLException e) {
            // Lanzar una excepción personalizada si ocurre un error
            throw new DAOException("Error al obtener todas las obras sociales", e);
        }

        return obrasSociales;
    }

}

