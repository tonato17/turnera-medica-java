
import java.sql.Connection;

import java.sql.SQLException;
import com.turneramedica.util.ConexionH2;
import com.turneramedica.DAO.MedicoDAO;
import com.turneramedica.DAO.DAOException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ConexionH2.getConnection()) {
            MedicoDAO medicoDAO = new MedicoDAO(); // Crear instancia de MedicoDAO
            Integer a = medicoDAO.obtenerIdMedico("1"); // Llamar al método usando la instancia
            System.out.println("El ID del médico es: " + a);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("ERROR de conexión: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("ERROR de DAO: " + e.getMessage());
        }
    }
}


