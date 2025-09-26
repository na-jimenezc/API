package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.model.Veterinario;
import com.api.api.service.serviceInterface.VeterinarioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    //Post para el login del veterinario
    @PostMapping("/login")
    public Veterinario loginVeterinario(@RequestParam String nombreUsuario,
                                              @RequestParam String contrasenia) {
        Veterinario vet = veterinarioService.validarVeterinario(nombreUsuario, contrasenia);
        if (vet != null) {
            return vet;
        }
        return null;
    }

    //Función para obtener todos los veterinarios
    @GetMapping
    public List<Veterinario> obtenerTodos() {
        return veterinarioService.obtenerTodos();
    }

    //Función para obtener todos los veterinarios activos
    @GetMapping("/activos")
    public List<Veterinario> obtenerActivos() {
        return veterinarioService.obtenerVeterinariosActivos();
    }

    //Función para obtener un veterinario por ID
    @GetMapping("/{id}")
    public Veterinario obtenerPorId(@PathVariable Long id) {
        Veterinario vet = veterinarioService.obtenerVeterinarioPorId(id);
        if (vet == null) {
           return null;
        }
        return vet;
    }

    //Función para crear un nuevo veterinario
    @PostMapping
    public Veterinario crearVeterinario(@RequestBody Veterinario veterinario) {
        Veterinario nuevoVet = veterinarioService.guardarVeterinario(veterinario);
        return nuevoVet;
    }

    //Función para actualizar un veterinario existente
    @PutMapping("/{id}")
    public Veterinario actualizarVeterinario(@PathVariable Long id,
                                                   @RequestBody Veterinario veterinario) {
        Veterinario existente = veterinarioService.obtenerVeterinarioPorId(id);
        if (existente == null) {
            return null;
        }
        veterinario.setId(id); 
        Veterinario actualizado = veterinarioService.guardarVeterinario(veterinario);
        return actualizado;
    }

    //Función para eliminar un veterinario por ID
    @DeleteMapping("/{id}")
    public void eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
    }
}
