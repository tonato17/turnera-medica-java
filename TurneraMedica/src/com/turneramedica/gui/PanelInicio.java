package com.turneramedica.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelInicio extends JPanel {
    public PanelInicio(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Título del panel
        JLabel labelTitulo = new JLabel("Bienvenido al Sistema de Gestión de Consultorios", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        // Crear el panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton botonGestionMedicos = new JButton("Gestionar Médicos");
        botonGestionMedicos.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Medicos"));
        panelBotones.add(botonGestionMedicos);

        JButton botonGestionPacientes = new JButton("Gestionar Pacientes");
        botonGestionPacientes.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Pacientes"));
        panelBotones.add(botonGestionPacientes);

        JButton botonGestionTurnos = new JButton("Gestionar Turnos");
        botonGestionTurnos.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Turnos"));
        panelBotones.add(botonGestionTurnos);

        JButton botonGenerarReportes = new JButton("Generar Reportes");
        botonGenerarReportes.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Reportes"));
        panelBotones.add(botonGenerarReportes);

        JButton botonAccesoMedico = new JButton("Acceso Médico");
        botonAccesoMedico.addActionListener(e -> {
            DialogoLoginMedico dialogoLoginMedico = new DialogoLoginMedico(mainFrame);
            dialogoLoginMedico.setVisible(true);
        });
        panelBotones.add(botonAccesoMedico);




        // Agregar el panel de botones al centro
        add(panelBotones, BorderLayout.CENTER);
    }
}
