package com.turneramedica.gui;

import com.turneramedica.service.MedicoService;
import com.turneramedica.entidades.Medico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DialogoLoginMedico extends JDialog {
    private JTextField legajoField;
    private JPasswordField passwordField;
    private MedicoService medicoService;
    private MainFrame mainFrame;

    public DialogoLoginMedico(MainFrame mainFrame) {
        super(mainFrame, "Acceso Médico", true);
        this.medicoService = new MedicoService();
        this.mainFrame = mainFrame;

        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(400, 200);
        setLocationRelativeTo(mainFrame);

        // Campos para legajo y contraseña
        add(new JLabel("Legajo:"));
        legajoField = new JTextField();
        add(legajoField);

        add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Botones para ingresar o cancelar
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(this::autenticarMedico);
        add(btnLogin);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    private void autenticarMedico(ActionEvent e) {
        String legajo = legajoField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Medico medico = medicoService.autenticarMedico(legajo, password);
            if (medico != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, Dr. " + medico.getNombre() + " " + medico.getApellido());
                dispose();
                // Redirigir al panel de turnos para el médico
                PanelTurnosMedico panelTurnosMedico = new PanelTurnosMedico(medico, mainFrame);
                mainFrame.mostrarPanelDinamico("PanelTurnosMedico", panelTurnosMedico);

            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al autenticar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
