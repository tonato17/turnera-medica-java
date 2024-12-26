package com.turneramedica.entidades;

import java.util.List;

public class ReporteMedico {
    private String nombreMedico; // Nombre completo del médico
    private String especialidad; // Especialidad del médico
    private int consultasRealizadas; // Número de consultas realizadas
    private double totalCobrado; // Suma total de los turnos cobrados
    private List<Turno> detallesTurnos; // Lista de turnos

    // Constructor vacio
    public ReporteMedico(){

    }
    // Constructor
    public ReporteMedico(String nombreMedico, String especialidad, int consultasRealizadas, double totalCobrado, List<Turno> detallesTurnos) {
        this.nombreMedico = nombreMedico;
        this.especialidad = especialidad;
        this.consultasRealizadas = consultasRealizadas;
        this.totalCobrado = totalCobrado;
        this.detallesTurnos = detallesTurnos;
    }

    // Getters y Setters
    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getConsultasRealizadas() {
        return consultasRealizadas;
    }

    public void setConsultasRealizadas(int consultasRealizadas) {
        this.consultasRealizadas = consultasRealizadas;
    }

    public double getTotalCobrado() {
        return totalCobrado;
    }

    public void setTotalCobrado(double totalCobrado) {
        this.totalCobrado = totalCobrado;
    }

    public List<Turno> getDetallesTurnos() {
        return detallesTurnos;
    }

    public void setDetallesTurnos(List<Turno> detallesTurnos) {
        this.detallesTurnos = detallesTurnos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Médico: ").append(nombreMedico).append(" (").append(especialidad).append(")\n");
        sb.append("Consultas: ").append(consultasRealizadas).append("\n");
        sb.append("Total Cobrado: $").append(totalCobrado).append("\n");
        sb.append("Detalle de Turnos:\n");
        sb.append("Fecha        Hora   Paciente     Monto\n");
        for (Turno turno : detallesTurnos) {
            sb.append(String.format("%s  %02d:00  %s  $%.2f",
                    turno.getFecha(),
                    turno.getHora(),
                    turno.getPaciente() != null ? turno.getPaciente().getNombre() : "Sin asignar",
                    turno.getPrecioTurno())).append("\n");
        }
        return sb.toString();
    }
}
