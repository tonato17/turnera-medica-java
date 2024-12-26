package com.turneramedica.entidades;

public class Consultorio {
    private int idConsultorio;
    private String nombre;
    private String direccion;
    private String telefonoContacto;
    private int capacidad;

    // Constructor vacío
    public Consultorio() {}

    // Constructor completo
    public Consultorio(String nombre, String direccion, String telefonoContacto, int capacidad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefonoContacto = telefonoContacto;
        this.capacidad = capacidad;
    }

    // Getters y Setters


    public int getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(int idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Consultorio{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", capacidad=" + capacidad +
                '}';
    }
}
