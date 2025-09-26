package com.api.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity   
public class Cliente {

    @Id
    @GeneratedValue
    private Long id;

    private String cedula;
    private String nombre;
    private String correo;
    private String celular;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Mascota> mascotas = new ArrayList<>();

    public Cliente() {}
    public Cliente(String cedula, String nombre, String correo, String celular) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
    }

    public Cliente(Long id, String cedula, String nombre, String correo, String celular) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
    }

        public Long getId() {
        return id;
        }

        public void setId(Long id) {
        this.id = id;
        }

        public String getCedula() {
        return cedula;
        }

        public void setCedula(String cedula) {
        this.cedula = cedula;
        }

        public String getNombre() {
        return nombre;
        }

        public void setNombre(String nombre) {
        this.nombre = nombre;
        }

        public String getCorreo() {
        return correo;
        }

        public void setCorreo(String correo) {
        this.correo = correo;
        }

        public String getCelular() {
        return celular;
        }

        public void setCelular(String celular) {
        this.celular = celular;
        }

        public List<Mascota> getMascotas() {
        return mascotas;
        }

        public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
        }

}
