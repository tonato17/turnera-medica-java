package com.turneramedica.gui;

import com.turneramedica.entidades.ObraSocial;
import com.turneramedica.entidades.Paciente;
import com.turneramedica.service.PacienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelPacientes extends JPanel {
    private PacienteService pacienteService;
    private DefaultTableModel tablaModelo;
    private JTable tablaPacientes;

    public PanelPacientes(MainFrame mainFrame) {
        this.pacienteService = new PacienteService();
        setLayout(new BorderLayout());

        // Título del panel
        JLabel labelTitulo = new JLabel("Gestión de Pacientes", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        // Tabla para mostrar pacientes
        tablaModelo = new DefaultTableModel(new String[]{"Nro Afiliado", "ID Paciente","Nombre", "Apellido", "Domicilio", "Teléfono", "ID Obra Social"}, 0);
        tablaPacientes = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para acciones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton botonAgregar = new JButton("Agregar Paciente");
        botonAgregar.addActionListener((ActionEvent e) -> agregarPaciente());
        panelBotones.add(botonAgregar);

        JButton botonEditar = new JButton("Editar Paciente");
        botonEditar.addActionListener((ActionEvent e) -> editarPaciente());
        panelBotones.add(botonEditar);

        JButton botonEliminar = new JButton("Eliminar Paciente");
        botonEliminar.addActionListener((ActionEvent e) -> eliminarPaciente());
        panelBotones.add(botonEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Botón para volver al inicio
        JButton botonVolver = new JButton("Volver al Inicio");
        botonVolver.addActionListener((ActionEvent e) -> mainFrame.mostrarPanel("Inicio"));
        add(botonVolver, BorderLayout.WEST);

        // Cargar la tabla con los datos de los pacientes
        actualizarTablaPacientes();
    }

    private void actualizarTablaPacientes() {
        // Limpiar la tabla
        tablaModelo.setRowCount(0);

        try {
            // Obtener los pacientes desde el servicio
            List<Paciente> pacientes = pacienteService.obtenerTodosLosPacientes();
            for (Paciente paciente : pacientes) {
                tablaModelo.addRow(new Object[]{
                        paciente.getNroAfiliado(),
                        paciente.getIdPaciente(),
                        paciente.getNombre(),
                        paciente.getApellido(),
                        paciente.getDomicilio(),
                        paciente.getTelefono(),
                        paciente.getObraSocial().getIdObraSocial()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarPaciente() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del paciente:");
        String apellido = JOptionPane.showInputDialog(this, "Ingrese el apellido del paciente:");
        String domicilio = JOptionPane.showInputDialog(this, "Ingrese el domicilio del paciente:");
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el teléfono del paciente:");
        String nroAfiliado = JOptionPane.showInputDialog(this, "Ingrese el número de afiliado del paciente:");
        String password = JOptionPane.showInputDialog(this, "Ingrese el password del paciente:");
        String idObraSocial= JOptionPane.showInputDialog(this, "Ingrese el id de la obra social:");

        // Validar que los campos obligatorios no sean nulos o vacíos
        if (nombre == null || nombre.isEmpty() ||
                apellido == null || apellido.isEmpty() ||
                nroAfiliado == null || nroAfiliado.isEmpty() ||
                password == null || password.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben ser completados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el objeto Paciente y asignar los valores ingresados
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDomicilio(domicilio);
        paciente.setTelefono(telefono);
        paciente.setNroAfiliado(nroAfiliado);
        paciente.setPassword(password);
        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setIdObraSocial(Integer.parseInt(idObraSocial));
        paciente.setObraSocial(obraSocial);

        try {
            // Guardar el paciente usando el servicio
            pacienteService.guardarPaciente(paciente);
            actualizarTablaPacientes(); // Actualizar la tabla después de agregar
            JOptionPane.showMessageDialog(this, "Paciente agregado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void editarPaciente() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para editar.");
            return;
        }

        String nroAfiliado = tablaModelo.getValueAt(filaSeleccionada, 0).toString();
        Paciente pacienteActual;
        try {
            // Obtener los datos actuales del paciente desde el servicio
            pacienteActual = pacienteService.obtenerPacientePorAfiliado(nroAfiliado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos del paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar cuadros de diálogo para editar cada atributo
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre del paciente:", pacienteActual.getNombre());
        String apellido = JOptionPane.showInputDialog(this, "Ingrese el nuevo apellido del paciente:", pacienteActual.getApellido());
        String domicilio = JOptionPane.showInputDialog(this, "Ingrese el nuevo domicilio del paciente:", pacienteActual.getDomicilio());
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el nuevo teléfono del paciente:", pacienteActual.getTelefono());
        String password = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña del paciente (dejar en blanco para no cambiar):");

        // Validar que los campos obligatorios no sean nulos o vacíos
        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y apellido del paciente no pueden ser nulos o vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password == null || password.isEmpty()) {
            // Si el usuario no ingresó una nueva contraseña, mantener la existente
            password = pacienteActual.getPassword();
        }

        // Crear un objeto Paciente con los nuevos datos
        Paciente pacienteEditado = new Paciente();
        pacienteEditado.setNroAfiliado(nroAfiliado);
        pacienteEditado.setNombre(nombre);
        pacienteEditado.setApellido(apellido);
        pacienteEditado.setDomicilio(domicilio);
        pacienteEditado.setTelefono(telefono);
        pacienteEditado.setPassword(password);

        try {
            // Actualizar los datos del paciente
            pacienteService.actualizarPaciente(pacienteEditado);
            actualizarTablaPacientes();
            JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminarPaciente() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.");
            return;
        }

        String nroAfiliado = tablaModelo.getValueAt(filaSeleccionada, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al paciente con número de afiliado: " + nroAfiliado + "?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                pacienteService.eliminarPaciente(nroAfiliado);
                actualizarTablaPacientes();
                JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
