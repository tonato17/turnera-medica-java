package TEST;

import com.turneramedica.DAO.MedicoDAO;
import com.turneramedica.DAO.DAOException;

public class TestObraSocialDAO {

    public static void main(String[] args) {
        // Creamos una instancia del MedicoDAO
        MedicoDAO medicoDAO = new MedicoDAO();

        // Definimos un legajo para probar
        String legajo = "12345"; // Modifica según tu base de datos

        try {
            // Llamamos al método auxiliar obtenerIdMedico
            Integer idMedico = medicoDAO.obtenerIdMedico(legajo);

            // Imprimimos el resultado
            if (idMedico != null) {
                System.out.println("El ID del médico con legajo " + legajo + " es: " + idMedico);
            } else {
                System.out.println("No se encontró un médico con el legajo " + legajo);
            }

        } catch (DAOException e) {
            // Capturamos cualquier excepción que se pueda generar
            System.err.println("Ocurrió un error al intentar obtener el ID del médico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
