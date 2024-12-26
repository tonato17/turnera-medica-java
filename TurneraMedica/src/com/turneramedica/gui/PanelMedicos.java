package com.turneramedica.gui;

import com.turneramedica.entidades.Medico;
import com.turneramedica.entidades.ObraSocial;
import com.turneramedica.service.MedicoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelMedicos extends JPanel {
    private MedicoService medicoService;
    private DefaultTableModel tablaModelo;
    private JTable tablaMedicos;

    public PanelMedicos(MainFrame mainFrame) {
        this.medicoService = new MedicoService(); // Servicio para gestionar médicos
        setLayout(new BorderLayout());

        // Título del panel
        JLabel labelTitulo = new JLabel("Gestión de Médicos", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        // Tabla para mostrar médicos
        tablaModelo = new DefaultTableModel(new String[]{"Legajo", "Nombre", "Apellido", "Especialidad", "Email", "Domicilio","Tarifa por Turno", "ID_Obra_Social"}, 0);
        tablaMedicos = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaMedicos);
        add(scrollPane, BorderLayout.CENTER);

        // Botones para acciones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton botonAgregar = new JButton("Agregar Médico");
        botonAgregar.addActionListener((ActionEvent e) -> agregarMedico());
        panelBotones.add(botonAgregar);

        JButton botonEditar = new JButton("Editar Médico");
        botonEditar.addActionListener((ActionEvent e) -> editarMedico());
        panelBotones.add(botonEditar);

        JButton botonEliminar = new JButton("Eliminar Médico");
        botonEliminar.addActionListener((ActionEvent e) -> eliminarMedico());
        panelBotones.add(botonEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Botón para volver
        JButton botonVolver = new JButton("Volver al Inicio");
        botonVolver.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Inicio"));
        add(botonVolver, BorderLayout.WEST);

        // Cargar la tabla con los datos de los médicos
        actualizarTablaMedicos();
    }

    private void actualizarTablaMedicos() {
        // Limpiar la tabla
        tablaModelo.setRowCount(0);

        try {
            // Obtener los médicos desde el servicio
            List<Medico> medicos = medicoService.obtenerTodosLosMedicos();
            for (Medico medico : medicos) {
                tablaModelo.addRow(new Object[]{
                        medico.getLegajo(),
                        medico.getNombre(),
                        medico.getApellido(),
                        medico.getEspecialidad(),
                        medico.getEmail(),
                        medico.getDomicilio(),
                        medico.getTarifaPorTurno(),
                        medico.getObraSocial().getIdObraSocial()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarMedico() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del médico:");
        String apellido = JOptionPane.showInputDialog(this, "Ingrese el apellido del médico:");
        String domicilio = JOptionPane.showInputDialog(this, "Ingrese el domicilio del médico:");
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el teléfono del médico:");
        String legajo = JOptionPane.showInputDialog(this, "Ingrese el legajo del médico:");
        String dni = JOptionPane.showInputDialog(this, "Ingrese el DNI del médico:");
        String password = JOptionPane.showInputDialog(this, "Ingrese el password del médico:");
        String especialidad = JOptionPane.showInputDialog(this, "Ingrese la especialidad del médico:");
        Double tarifaPorTurno = Double.parseDouble(JOptionPane.showInputDialog(this, "Ingrese la tarifa por turno del médico:"));
        String email = JOptionPane.showInputDialog(this, "Ingrese el email del médico:");
        int idOBraSocial = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el id de la obra social"));

        if (nombre != null && !nombre.isEmpty()) {
            Medico medico = new Medico();
            medico.setNombre(nombre);
            medico.setApellido(apellido);
            medico.setDomicilio(domicilio);
            medico.setTelefono(telefono);
            medico.setLegajo(legajo);
            medico.setDni(dni);
            medico.setPassword(password);
            medico.setEspecialidad(especialidad);
            medico.setTarifaPorTurno(tarifaPorTurno);
            medico.setEmail(email);
            ObraSocial obraSocial = new ObraSocial();
            obraSocial.setIdObraSocial(idOBraSocial);
            medico.setObraSocial(obraSocial);


            try {
                medicoService.guardarMedico(medico);
                actualizarTablaMedicos();
                JOptionPane.showMessageDialog(this, "Médico agregado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarMedico() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para editar.");
            return;
        }

        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del médico:");
        String apellido = JOptionPane.showInputDialog(this, "Ingrese el apellido del médico:");
        String especialidad = JOptionPane.showInputDialog(this, "Ingrese la especialidad del médico:");
        String email = JOptionPane.showInputDialog(this, "Ingrese el email del médico:");
        String domicilio = JOptionPane.showInputDialog(this, "Ingrese el domicilio del médico:");
        Double tarifaPorTurno = Double.parseDouble(JOptionPane.showInputDialog(this, "Ingrese la tarifa por turno del médico:"));
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el teléfono del médico:");
        //String legajo = JOptionPane.showInputDialog(this, "Ingrese el legajo del médico:");
        String dni = JOptionPane.showInputDialog(this, "Ingrese el DNI del médico:");
        String password = JOptionPane.showInputDialog(this, "Ingrese el password del médico:");

        String legajo = tablaModelo.getValueAt(filaSeleccionada, 0).toString();

        if (nombre != null && !nombre.isEmpty()) {
            Medico medico = new Medico();
            medico.setNombre(nombre);
            medico.setApellido(apellido);
            medico.setDomicilio(domicilio);
            medico.setTelefono(telefono);
            medico.setLegajo(legajo);
            medico.setDni(dni);
            medico.setPassword(password);
            medico.setEspecialidad(especialidad);
            medico.setTarifaPorTurno(tarifaPorTurno);
            medico.setEmail(email);

            try {
                medicoService.actualizarMedico(medico);
                actualizarTablaMedicos();
                JOptionPane.showMessageDialog(this, "Médico modificado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al modificar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarMedico() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para eliminar.");
            return;
        }

        String legajo = tablaModelo.getValueAt(filaSeleccionada, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al médico con legajo: " + legajo + "?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                medicoService.eliminarMedico(legajo);
                actualizarTablaMedicos();
                JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
