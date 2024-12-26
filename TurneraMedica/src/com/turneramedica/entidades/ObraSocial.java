package com.turneramedica.entidades;

public class ObraSocial {
    private int idObraSocial;
    private String nombre;
    private String telefonoContacto;
    private String direccion;
    private String emailContacto;
    private String estado;

    public ObraSocial(){

    }

    //Constructor explicito, sin estado ya que al crearlo sera 'ACTIVA' por defecto en la base de datos
    public ObraSocial(int idObraSocial,String nombre, String telefonoContacto, String direccion, String emailContacto) {
        this.idObraSocial = idObraSocial;
        this.nombre = nombre;
        this.telefonoContacto = telefonoContacto;
        this.direccion = direccion;
        this.emailContacto = emailContacto;
    }

    public int getIdObraSocial() {
        return idObraSocial;
    }

    public void setIdObraSocial(int idObraSocial) {
        this.idObraSocial = idObraSocial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ObraSocial{" +
                "nombre='" + nombre + '\'' +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", direccion='" + direccion + '\'' +
                ", emailContacto='" + emailContacto + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}