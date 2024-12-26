package com.turneramedica.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Sistema de Gestión de Consultorios");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Configuración del CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Crear y agregar los paneles
        mainPanel.add(new PanelInicio(this), "Inicio");
        mainPanel.add(new PanelMedicos(this), "Medicos");
        mainPanel.add(new PanelPacientes(this), "Pacientes");
        mainPanel.add(new PanelTurnos(this), "Turnos");
        mainPanel.add(new PanelReportes(this), "Reportes");

        // Agregar el panel principal al JFrame
        add(mainPanel);

        // Mostrar el panel de inicio al abrir la aplicación
        cardLayout.show(mainPanel, "Inicio");
    }

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }
    public void mostrarPanelDinamico(String nombrePanel, JPanel panel) {
        mainPanel.add(panel, nombrePanel);
        cardLayout.show(mainPanel, nombrePanel);
    }

}
