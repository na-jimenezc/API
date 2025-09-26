package com.api.api.dto;

public class MascotaDTO {
    private Long id;
    private String nombre;
    private String raza;
    private int edad;
    private String tipo;
    private String enfermedad;
    private double peso;
    private String fotoURL;
    private boolean activo;
    private Long clienteId;
    private String estado;

    // Constructor completo
    public MascotaDTO(Long id, String nombre, String raza, int edad, String tipo,
                      String enfermedad, double peso, String fotoURL, boolean activo,
                      Long clienteId, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.tipo = tipo;
        this.enfermedad = enfermedad;
        this.peso = peso;
        this.fotoURL = fotoURL;
        this.activo = activo;
        this.clienteId = clienteId;
        this.estado = estado;
    }

    public MascotaDTO() {} 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEnfermedad() { return enfermedad; }
    public void setEnfermedad(String enfermedad) { this.enfermedad = enfermedad; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getFotoURL() { return fotoURL; }
    public void setFotoURL(String fotoURL) { this.fotoURL = fotoURL; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}