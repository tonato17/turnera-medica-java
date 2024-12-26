/*
package com.turneramedica.gui;

import com.turneramedica.gui.PanelReportes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaPrincipal extends JFrame {
    public PantallaPrincipal() {
        setTitle("Sistema de Gestión Médica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel con botones para acceder a las funcionalidades
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Botón para gestionar médicos
        JButton btnMedicos = new JButton("Administrar Médicos");
        btnMedicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelMedicos();
            }
        });

        // Botón para gestionar pacientes
        JButton btnPacientes = new JButton("Administrar Pacientes");
        btnPacientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelPacientes();
            }
        });

        // Botón para gestionar turnos
        JButton btnTurnos = new JButton("Administrar Turnos");
        btnTurnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelTurnos();
            }
        });

        // Botón para generar reportes
        JButton btnReportes = new JButton("Generar Reportes");
        btnReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelReportes();
            }
        });

        // Añadimos los botones al panel
        panel.add(btnMedicos);
        panel.add(btnPacientes);
        panel.add(btnTurnos);
        panel.add(btnReportes);

        // Añadimos el panel al JFrame
        add(panel);
    }

    // Método para mostrar el panel de médicos
    private void mostrarPanelMedicos() {
        // Crear y mostrar el panel de médicos
        PanelMedicos panelMedicos = new PanelMedicos();
        setContentPane(panelMedicos);  // Cambia el contenido de la ventana
        revalidate();  // Recarga el panel para que se vea correctamente
    }

    // Método para mostrar el panel de pacientes
    private void mostrarPanelPacientes() {
        // Crear y mostrar el panel de pacientes
        PanelPacientes panelPacientes = new PanelPacientes();
        setContentPane(panelPacientes);
        revalidate();
    }

    // Método para mostrar el panel de turnos
    private void mostrarPanelTurnos() {
        // Crear y mostrar el panel de turnos
        PanelTurnos panelTurnos = new PanelTurnos();
        setContentPane(panelTurnos);
        revalidate();
    }

    // Método para mostrar el panel de reportes
    private void mostrarPanelReportes() {
        // Crear y mostrar el panel de reportes
        PanelReportes panelReportes = new PanelReportes();
        setContentPane(panelReportes);
        revalidate();
    }

    public static void main(String[] args) {
        // Crear una instancia de la pantalla principal y hacerla visible
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                pantallaPrincipal.setVisible(true);  // Muestra la ventana principal
            }
        });
    }
}
*/