package com.api.api.service.serviceInterface;

import java.util.List;

import com.api.api.model.Veterinario;

public interface VeterinarioService {
    List<Veterinario> obtenerTodos();
    Veterinario validarVeterinario(String nombreUsuario, String contrasenia);
    List<Veterinario> obtenerVeterinariosActivos();
    Veterinario obtenerVeterinarioPorId(Long id);
    Veterinario guardarVeterinario(Veterinario veterinario);
    void eliminarVeterinario(Long id);
}
