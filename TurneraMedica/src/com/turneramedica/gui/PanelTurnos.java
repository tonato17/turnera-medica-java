package com.turneramedica.gui;

import com.turneramedica.entidades.*;
import com.turneramedica.service.*;
import com.turneramedica.DAO.ConsultorioDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PanelTurnos extends JPanel {
    private JComboBox<String> comboBoxMedicos;
    private JComboBox<String> comboBoxPacientes;
    private JComboBox<String> comboBoxConsultorios;
    private JSpinner dateSpinner;
    private JComboBox <Integer>horaField;
    private JLabel precioLabel;
    private TurnoService turnoService;
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private ConsultorioDAO consultorioDAO;

    public PanelTurnos(MainFrame mainFrame) {
        // Inicializar los servicios y DAO
        this.turnoService = new TurnoService();
        this.medicoService = new MedicoService();
        this.pacienteService = new PacienteService();
        this.consultorioDAO = new ConsultorioDAO();

        inicializarComponentes(mainFrame);
    }

    private void inicializarComponentes(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        // ComboBox para seleccionar médico
        comboBoxMedicos = new JComboBox<>();
        cargarMedicos();
        comboBoxMedicos.addActionListener(e -> actualizarPrecio());
        formPanel.add(new JLabel("Seleccione el Médico:"));
        formPanel.add(comboBoxMedicos);

        // ComboBox para seleccionar consultorio
        comboBoxConsultorios = new JComboBox<>();
        cargarConsultorios();
        formPanel.add(new JLabel("Seleccione el Consultorio:"));
        formPanel.add(comboBoxConsultorios);
        horaField = new JComboBox<>();
        cargarHoras();



        // Campo para ingresar ID del paciente
        //pacienteField = new JTextField();
        //formPanel.add(new JLabel("Ingrese el ID del Paciente:"));
        //formPanel.add(pacienteField);
        comboBoxPacientes = new JComboBox<>();
        cargarPacientes();
        formPanel.add(new JLabel("Seleccione el Paciente:"));
        formPanel.add(comboBoxPacientes);

        // JSpinner para seleccionar la fecha
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(new JLabel("Seleccione la Fecha:"));
        formPanel.add(dateSpinner);

        // Campo para ingresar hora

        formPanel.add(new JLabel("Ingrese la Hora:"));
        formPanel.add(horaField);

        // Etiqueta para mostrar el precio calculado
        precioLabel = new JLabel("Precio: ");
        formPanel.add(new JLabel("Precio del Turno:"));
        formPanel.add(precioLabel);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Botón para crear el turno
        JButton btnCrearTurno = new JButton("Crear Turno");
        btnCrearTurno.addActionListener(e -> crearTurno());
        buttonPanel.add(btnCrearTurno);

        // Botón para volver al inicio
        JButton btnVolverInicio = new JButton("Volver al Inicio");
        btnVolverInicio.addActionListener(e -> mainFrame.mostrarPanel("Inicio"));
        buttonPanel.add(btnVolverInicio);

        // Botón para visualizar todos los turnos
        JButton btnVerTurnos = new JButton("Ver Todos los Turnos");
        btnVerTurnos.addActionListener(e -> verTurnos());
        buttonPanel.add(btnVerTurnos);

        //JButton btnVerTurnosMedico = new JButton("Ver Turnos Medico en Fecha");
        //btnVerTurnosMedico.addActionListener(e -> verTurnosMedico());
        //buttonPanel.add(btnVerTurnosMedico);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarPacientes() {
        try {
            List<Paciente> pacientes = pacienteService.obtenerTodosLosPacientes();
            for (Paciente paciente : pacientes) {
                comboBoxPacientes.addItem(paciente.getIdPaciente() + " - " + paciente.getNombre() + " " + paciente.getApellido());
            }
        } catch (Exception e) {
            mostrarError("Error al cargar médicos: " + e.getMessage());
        }
    }


    private void verTurnosMedico() {
        PanelFormularioTurnos listaTurnos = new PanelFormularioTurnos();
        listaTurnos.setVisible(true);
    }

    private void cargarHoras() {
        for (int i = 10; i<20; i++){
            horaField.addItem(i);
        }
    }


    private void cargarMedicos() {
        try {
            List<Medico> medicos = medicoService.obtenerTodosLosMedicos();
            for (Medico medico : medicos) {
                comboBoxMedicos.addItem(medico.getLegajo() + " - " + medico.getNombre() + " " + medico.getApellido() + " - " + medico.getEspecialidad() + " - Tarifa: $" + medico.getTarifaPorTurno());
            }
        } catch (Exception e) {
            mostrarError("Error al cargar médicos: " + e.getMessage());
        }
    }

    private void cargarConsultorios() {
        try {
            List<Consultorio> consultorios = consultorioDAO.obtenerTodosLosConsultorios();
            for (Consultorio consultorio : consultorios) {
                comboBoxConsultorios.addItem(consultorio.getIdConsultorio() + " - " + consultorio.getNombre() + " - " + consultorio.getDireccion());
            }
        } catch (Exception e) {
            mostrarError("Error al cargar consultorios: " + e.getMessage());
        }
    }

    private void actualizarPrecio() {
        int selectedIndex = comboBoxMedicos.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedMedico = (String) comboBoxMedicos.getSelectedItem();
            String[] parts = selectedMedico.split(" - ");
            String tarifaStr = parts[parts.length - 1].replace("Tarifa: $", "");
            double tarifa = Double.parseDouble(tarifaStr);
            precioLabel.setText("Precio: $" + tarifa);
        }
    }

    private void verTurnos() {
        PanelFormularioTurnos listaTurnos = new PanelFormularioTurnos();
        listaTurnos.setVisible(true);

    }

    private void crearTurno() {
        try {
            // Validar y obtener datos
            if (comboBoxMedicos.getSelectedIndex() == -1 || comboBoxConsultorios.getSelectedIndex() == -1 ||  horaField.getSelectedIndex() == -1) {
                mostrarError("Todos los campos deben estar completos.");
                return;
            }

            String medicoSeleccionado = (String) comboBoxMedicos.getSelectedItem();
            String legajoMedico = medicoSeleccionado.split(" - ")[0];

            String consultorioSeleccionado = (String) comboBoxConsultorios.getSelectedItem();
            int idConsultorio = Integer.parseInt(consultorioSeleccionado.split(" - ")[0]);

            int idPaciente;

            String pacienteSeleccionado = (String) comboBoxPacientes.getSelectedItem();
            String idPacienteString = pacienteSeleccionado.split(" - ")[0];

            try {
                idPaciente = Integer.parseInt(idPacienteString);
            } catch (NumberFormatException e) {
                mostrarError("El ID del paciente debe ser un número válido.");
                return;
            }

            Date fechaSeleccionada = (Date) dateSpinner.getValue();
            LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int hora = Integer.parseInt(horaField.getSelectedItem().toString());

            double precio = Double.parseDouble(precioLabel.getText().replace("Precio: $", ""));

            // Crear el turno
            Turno turno = new Turno();
            turno.setMedico(new Medico());
            turno.getMedico().setLegajo(legajoMedico);
            turno.setPaciente(new Paciente(idPaciente));
            turno.setConsultorio(new Consultorio());
            turno.getConsultorio().setIdConsultorio(idConsultorio);
            turno.setFecha(fecha);
            turno.setHora(hora);
            turno.setPrecio(precio);
            turno.setEstadoTurno(EstadoTurno.ASIGNADO);

            boolean existeTurno = turnoService.existeTurno(legajoMedico, fecha, hora);
            if (existeTurno){
                JOptionPane.showMessageDialog(null, "NO ES POSIBLE CREAR EL TURNO. Ya existe un turno para ese medico en esa fecha");
            }
            else{
                turnoService.crearTurno(turno);
                JOptionPane.showMessageDialog(this, "Turno creado exitosamente.");
            }

        } catch (Exception e) {
            mostrarError("Error al crear turno: " + e.getMessage());
        }
    }


    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
