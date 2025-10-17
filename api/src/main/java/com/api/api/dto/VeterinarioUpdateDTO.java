package com.api.api.dto;

public class VeterinarioUpdateDTO {
     private String nombre;
    private String especialidad;
    private String nombreUsuario;
    private String contrasenia;
    private String imagen;

    public VeterinarioUpdateDTO() {}

    public VeterinarioUpdateDTO(String nombre, String especialidad, String nombreUsuario, 
                               String contrasenia, String imagen) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
