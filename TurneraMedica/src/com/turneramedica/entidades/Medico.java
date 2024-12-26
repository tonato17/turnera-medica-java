package com.turneramedica.entidades;


public class Medico extends Persona {
    private String especialidad;
    private double tarifaPorTurno;
    private String legajo;
    private String dni;
    private String password;
    private String email;
    private ObraSocial obraSocial;



    // Constructor vacío, se crea la lista vacia para evitar NULL en la logica posterior
    public Medico() {
    }


    // Constructor con parámetros, incluyendo los de la clase base y la obra social
    public Medico(String nombre, String apellido, String domicilio, String telefono,
                  String especialidad, double tarifaPorTurno, String legajo, String dni, String password, String email, Integer idObraSocial) {
        super(nombre, apellido, domicilio, telefono);
        this.especialidad = especialidad;
        this.tarifaPorTurno = tarifaPorTurno;
        this.legajo = legajo;
        this.dni = dni;
        this.password = password;
        this.email = email;
    }


    // Getters y Setters para los atributos específicos de Medico
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public double getTarifaPorTurno() {
        return tarifaPorTurno;
    }

    public void setTarifaPorTurno(double tarifaPorTurno) {
        this.tarifaPorTurno = tarifaPorTurno;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", domicilio='" + getDomicilio() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", tarifaPorTurno=" + tarifaPorTurno +
                ", legajo='" + legajo + '\'' +
                ", dni='" + dni + '\'' +
                '}';
    }


    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public ObraSocial getObraSocial() {
        return this.obraSocial;

    }
}
