package com.turneramedica.entidades;

public class Paciente extends Persona {
    private Integer idPaciente;  // Nuevo atributo idPaciente
    private String nroAfiliado;
    private ObraSocial obraSocial;
    private String password;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor con todos los parámetros, incluyendo una ObraSocial específica
    public Paciente(Integer idPaciente, String nombre, String apellido, String domicilio, String telefono,
                    String nroAfiliado, ObraSocial obraSocial, String password) {
        super(nombre, apellido, domicilio, telefono);
        this.idPaciente = idPaciente;
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        this.password = password;
    }

    // Constructor solo con idPaciente
    public Paciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    // Getters y Setters para los atributos específicos de Paciente
    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNroAfiliado() {
        return nroAfiliado;
    }

    public void setNroAfiliado(String nroAfiliado) {
        this.nroAfiliado = nroAfiliado;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nroAfiliado='" + nroAfiliado + '\'' +
                ", obraSocial=" + obraSocial +
                ", nombre=" + getNombre() +
                ", apellido=" + getApellido() +
                ", domicilio=" + getDomicilio() +
                ", telefono=" + getTelefono() +
                '}';
    }
}
