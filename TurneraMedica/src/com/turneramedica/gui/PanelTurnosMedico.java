package com.turneramedica.gui;

import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.Turno;
import com.turneramedica.service.TurnoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelTurnosMedico extends JPanel {
    private TurnoService turnoService;
    private DefaultTableModel tablaModelo;
    private JTable tablaTurnos;

    public PanelTurnosMedico(Medico medico, MainFrame mainFrame) {
        this.turnoService = new TurnoService();
        setLayout(new BorderLayout());

        // Título del panel
        JLabel labelTitulo = new JLabel("Turnos del Médico: " + medico.getNombre() + " " + medico.getApellido(), JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        // Tabla para mostrar los turnos
        tablaModelo = new DefaultTableModel(new String[]{"ID Turno", "Fecha", "Hora", "Paciente", "Estado"}, 0);
        tablaTurnos = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaTurnos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonVolver = new JButton("Volver al Inicio");
        botonVolver.addActionListener(e -> mainFrame.mostrarPanel("Inicio"));
        panelBotones.add(botonVolver);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar los turnos del médico
        cargarTurnosMedico(medico);
    }

    private void cargarTurnosMedico(Medico medico) {
        try {
            // Solicitar la fecha al usuario
            String fechaTexto = JOptionPane.showInputDialog(this, "Ingrese la fecha (YYYY-MM-DD):");
            if (fechaTexto == null || fechaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fecha = LocalDate.parse(fechaTexto);
            List<Turno> turnos = turnoService.obtenerTurnosMedicoFecha(medico.getLegajo(), fecha);

            // Limpiar la tabla
            tablaModelo.setRowCount(0);

            // Llenar la tabla con los turnos
            for (Turno turno : turnos) {
                tablaModelo.addRow(new Object[]{
                        turno.getIdTurno(),
                        turno.getFecha(),
                        turno.getHora(),
                        turno.getPaciente() != null ? turno.getPaciente().getNombre() : "Sin asignar",
                        turno.getEstadoTurno()
                });
            }

            if (turnos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron turnos para la fecha ingresada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar turnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
