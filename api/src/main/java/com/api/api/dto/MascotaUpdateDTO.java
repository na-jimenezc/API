
package com.api.api.dto;

public class MascotaUpdateDTO {
    private String nombre;
    private String tipo;
    private String raza;
    private String enfermedad;
    private String fotoURL;
    private boolean activo;

    public MascotaUpdateDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getEnfermedad() { return enfermedad; }
    public void setEnfermedad(String enfermedad) { this.enfermedad = enfermedad; }

    public String getFotoURL() { return fotoURL; }
    public void setFotoURL(String fotoURL) { this.fotoURL = fotoURL; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}