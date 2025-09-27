package com.api.api.dto;

public class VeterinarioDTO {
    private Long id;
    private String nombre;
    private String especialidad;
    private String nombreUsuario;
    private String imagen;
    private int activo;
    private int consultas;

    public VeterinarioDTO() {}

    public VeterinarioDTO(Long id, String nombre, String especialidad, String nombreUsuario, 
                         String imagen, int activo, int consultas) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.nombreUsuario = nombreUsuario;
        this.imagen = imagen;
        this.activo = activo;
        this.consultas = consultas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getConsultas() {
        return consultas;
    }

    public void setConsultas(int consultas) {
        this.consultas = consultas;
    }


}