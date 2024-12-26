package com.turneramedica.gui;

import com.turneramedica.entidades.*;
import com.turneramedica.service.*;
import com.turneramedica.DAO.ConsultorioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


public class PanelFormularioTurnos extends JFrame {

    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    private TurnoService turnoService;

    public PanelFormularioTurnos() {
        turnoService = new TurnoService();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Visualización de Turnos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuración de la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID Turno", "Paciente", "Médico", "Consultorio", "Fecha", "Hora", "Precio", "Estado"}, 0);
        tablaTurnos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTurnos);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botón para actualizar la lista
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarTurnos());
        panelPrincipal.add(btnActualizar, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Cargar datos iniciales
        cargarTurnos();
    }

    private void cargarTurnos() {
        try {
            modeloTabla.setRowCount(0); // Limpiar la tabla
            List<Turno> turnos = turnoService.obtenerTodosLosTurnos(); // Obtener todos los turnos
            for (Turno turno : turnos) {
                modeloTabla.addRow(new Object[]{
                        turno.getIdTurno(),
                        turno.getPaciente().getNombre(),
                        turno.getMedico().getNombre(),
                        turno.getConsultorio().getNombre(),
                        turno.getFecha(),
                        turno.getHora(),
                        turno.getPrecioTurno(),
                        turno.getEstadoTurno()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los turnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}