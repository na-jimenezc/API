// src/main/java/com/api/api/dto/VeterinarioLoginRequest.java
package com.api.api.dto;

public class VeterinarioLoginRequest {
    private String nombreUsuario;
    private String contrasenia;

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}
