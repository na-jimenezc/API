package com.api.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veterinario {

    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String especialidad;
    private String nombreUsuario;

    //@Transient
    private String contrasenia;

    private String imagen;
    private int activo;
    private int consultas;

    @JsonIgnore
    @ManyToMany(mappedBy = "veterinarios", fetch = FetchType.LAZY)
    @JsonBackReference
    @Builder.Default
    private List<Administrador> administradores = new ArrayList<>();
    
    //NOTA: SI NO SE QUIERE QUITAR A SUS TRATAMIENTOS AL ELIMINAR UN VETERINARIO, QUITAR orphanRemoval = true
    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Tratamiento> tratamientos = new ArrayList<>();

    @OneToOne(
    cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
    fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JsonIgnore
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;

}