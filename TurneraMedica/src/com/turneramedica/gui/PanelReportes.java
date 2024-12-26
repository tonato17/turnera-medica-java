package com.turneramedica.gui;

import com.turneramedica.entidades.ReporteMedico;
import com.turneramedica.entidades.Turno;
import com.turneramedica.entidades.Medico;
import com.turneramedica.service.TurnoService;
import com.turneramedica.service.MedicoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelReportes extends JPanel {
    private JComboBox<String> comboBoxMedicos;
    private JTextField fechaInicioField;
    private JTextField fechaFinField;
    private JTextArea reporteArea;
    private TurnoService turnoService;
    private MedicoService medicoService;
    private JScrollPane scrollPaneReporte;

    public PanelReportes(MainFrame mainFrame) {
        this.turnoService = new TurnoService();
        this.medicoService = new MedicoService();

        setLayout(new BorderLayout());

        // Título del panel
        JLabel labelTitulo = new JLabel("Generación de Reportes", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        // Panel principal para el formulario
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // ComboBox para seleccionar médico
        comboBoxMedicos = new JComboBox<>();
        cargarMedicos();
        formPanel.add(new JLabel("Seleccione el Médico:"));
        formPanel.add(comboBoxMedicos);

        // Campo para la fecha de inicio
        fechaInicioField = new JTextField();
        formPanel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        formPanel.add(fechaInicioField);

        // Campo para la fecha de fin
        fechaFinField = new JTextField();
        formPanel.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        formPanel.add(fechaFinField);

        // Botón para generar el reporte
        JButton botonGenerarReporte = new JButton("Generar Reporte");
        botonGenerarReporte.addActionListener(this::generarReporte);
        formPanel.add(botonGenerarReporte);

        // Botón para volver al inicio
        JButton botonVolverInicio = new JButton("Volver al Inicio");
        botonVolverInicio.addActionListener(e -> mainFrame.mostrarPanel("Inicio"));
        formPanel.add(botonVolverInicio);

        add(formPanel, BorderLayout.CENTER);

        // Área de texto para mostrar el reporte
        reporteArea = new JTextArea();
        reporteArea.setEditable(false);
        reporteArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPaneReporte = new JScrollPane(reporteArea);
        add(scrollPaneReporte, BorderLayout.SOUTH);
    }

    private void cargarMedicos() {
        try {
            List<Medico> medicos = medicoService.obtenerTodosLosMedicos();
            for (Medico medico : medicos) {
                comboBoxMedicos.addItem(medico.getLegajo() + " - " + medico.getNombre() + " " + medico.getApellido());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporte(ActionEvent e) {
        try {
            // Validar selección del médico
            if (comboBoxMedicos.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un médico.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener legajo del médico seleccionado
            String medicoSeleccionado = (String) comboBoxMedicos.getSelectedItem();
            String legajoMedico = medicoSeleccionado.split(" - ")[0];

            // Validar fechas
            LocalDate fechaInicio = LocalDate.parse(fechaInicioField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate fechaFin = LocalDate.parse(fechaFinField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Generar el reporte
            ReporteMedico reporte = turnoService.generarReporteMedico(legajoMedico, fechaInicio, fechaFin);

            // Construir el texto del reporte
            StringBuilder reporteTexto = new StringBuilder();
            reporteTexto.append("Médico: ").append(reporte.getNombreMedico()).append("\n");
            reporteTexto.append("Especialidad: ").append(reporte.getEspecialidad()).append("\n");
            reporteTexto.append("Consultas Realizadas: ").append(reporte.getConsultasRealizadas()).append("\n");
            reporteTexto.append("Total Cobrado: $").append(reporte.getTotalCobrado()).append("\n\n");
            reporteTexto.append("Detalle de Turnos:\n");
            reporteTexto.append("Fecha        Hora   Paciente        Monto\n");
            for (Turno turno : reporte.getDetallesTurnos()) {
                reporteTexto.append(String.format("%-12s %-6s %-15s $%.2f\n",
                        turno.getFecha(),
                        turno.getHora(),
                        turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido(),
                        turno.getPrecioTurno()));
            }

            // Mostrar el texto en el área de texto
            reporteArea.setText(reporteTexto.toString());
            reporteArea.setCaretPosition(0); // Desplazar el scroll al inicio

            // Establecer un tamaño preferido para el área de texto si aún no lo tiene
            reporteArea.setRows(15); // Número de filas visibles
            reporteArea.setColumns(50); // Número de columnas visibles
            scrollPaneReporte.setPreferredSize(new Dimension(600, 200)); // Ancho x Alto

            // Revalidar el diseño del panel después de actualizar el texto
            this.revalidate();
            this.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
