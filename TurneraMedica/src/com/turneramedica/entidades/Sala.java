package com.turneramedica.entidades;

public class Sala {
    private int nroSala;
    private Consultorio consultorio;

    // Constructor vacío
    public Sala() {
    }

    // Constructor con parámetros
    public Sala(int nroSala, Consultorio consultorio) {
        this.nroSala = nroSala;
        this.consultorio = consultorio;
    }

    // Getters y Setters
    public int getNroSala() {
        return nroSala;
    }

    public void setNroSala(int nroSala) {
        this.nroSala = nroSala;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }
    @Override
    public String toString() {
        return "Sala{" +
                "nroSala=" + nroSala +
                ", consultorio=" + consultorio.getNombre() +
                ", direccion=" + consultorio.getDireccion() +
                ", telefonoContacto=" + consultorio.getTelefonoContacto() +
                ", capacidad=" + consultorio.getCapacidad() +
                '}';
    }
}
