package com.api.api.dto;

public class MascotaCreateDTO {
    private String nombre;
    private String raza;
    private int edad;
    private String tipo;
    private String enfermedad;
    private double peso;
    private String fotoURL;
    private boolean activo;
    private String estado;
    private Long clienteId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getClienteId() { 
    return clienteId; 
    }

    public void setClienteId(Long clienteId) { 
        this.clienteId = clienteId; 
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty() || estado.equalsIgnoreCase("ninguno")) {
            this.estado = "Sano";
        } else {
            this.estado = "Enfermo";
        }
    }

    public String getEstado() { 
        return this.estado;
    }
}