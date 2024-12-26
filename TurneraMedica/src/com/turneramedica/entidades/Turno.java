package com.turneramedica.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private int idTurno;
    private Medico medico;
    private Paciente paciente;
    private LocalDate fecha;
    private int hora;
    private Consultorio consultorio;
    private double precioTurno;
    private EstadoTurno estadoTurno;


    // Constructor vacío
    public Turno() {}

    // Constructor con parámetros
    public Turno(int idTurno, Medico medico, Paciente paciente, LocalDate fecha, int hora, EstadoTurno estadoTurno, Consultorio consultorio) {
        this.idTurno = idTurno;
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoTurno = estadoTurno;
        this.consultorio = consultorio;
    }

    // Getters y setters
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getHora() {
        return hora;
    }
    public void setHora(int hora) {
        this.hora = hora;
    }

    public EstadoTurno getEstadoTurno() {
        return estadoTurno;
    }
    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }
    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public double getPrecioTurno() {
        return precioTurno;
    }

    public void setPrecio(double precioTurno) {
        this.precioTurno = precioTurno;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "idTurno=" + idTurno +
                ", medico=" + medico.getNombre() + " " + medico.getApellido() +
                ", paciente=" + paciente.getNombre() + " " + paciente.getApellido() +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estadoTurno=" + estadoTurno +
                ", consultorio=" + consultorio.toString() +
                ", precio=" + precioTurno +
                '}';
    }
}
