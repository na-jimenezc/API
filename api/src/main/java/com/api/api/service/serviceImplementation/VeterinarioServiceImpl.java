package com.api.api.service.serviceImplementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.api.model.Administrador;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.VeterinarioService;
import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    //Obtrener todos los veterinarios
    @Override
    public List<Veterinario> obtenerTodos() {
        return veterinarioRepository.findAll();
    }

    //Validar veterinario por nombre de usuario y contraseña para el login
    @Override
    public Veterinario validarVeterinario(String nombreUsuario, String contrasenia) {
        return veterinarioRepository.findByNombreUsuarioAndContrasenia(nombreUsuario, contrasenia);
    }

    //Obtener todos los veterinarios activos
    @Override
    public List<Veterinario> obtenerVeterinariosActivos() {
        return veterinarioRepository.findByActivo(1);
    }

    //Obtener veterinario por ID
    @Override
    public Veterinario obtenerVeterinarioPorId(Long id) {
        return veterinarioRepository.findById(id).orElse(null);
    }

    //Guardar un nuevo veterinario
    @Override
    public Veterinario guardarVeterinario(Veterinario veterinario) {
        return veterinarioRepository.save(veterinario);
    }

    //Eliminar un veterinario, pero se rompen las relaciones primero para que no llore el JPA
    @Override
    @Transactional
    public void eliminarVeterinario(Long id) {

    //Se rompen las relaciones antes de eliminar el veterinario con administradores y tratamientos si queda alguna
        Veterinario veterinario = veterinarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + id));

        if(veterinario.getAdministradores() != null && !veterinario.getAdministradores().isEmpty()) {
            for (Administrador admin : veterinario.getAdministradores()) {
                admin.getVeterinarios().remove(veterinario);
            }
            veterinario.getAdministradores().clear();
        }

        if(veterinario.getTratamientos() != null && !veterinario.getTratamientos().isEmpty()) {
            for (Tratamiento t : veterinario.getTratamientos()) {
                t.setVeterinario(null); 
            }
            veterinario.getTratamientos().clear();
        }

        veterinarioRepository.delete(veterinario);
    }

    //Actualizar el estado (activo/inactivo) de un veterinario
    @Override
    public Veterinario actualizarEstado(Long id, int estado) {
        Veterinario veterinario = veterinarioRepository.findById(id).orElse(null);
        if(veterinario != null){
            veterinario.setActivo(estado);
            return veterinarioRepository.save(veterinario);
        }
        return null;
    }

    @Override
    public List<Veterinario> buscar(String cedula, String nombre, String especialidad) {
        boolean hayFiltro =
            (cedula != null && !cedula.isBlank()) ||
            (nombre != null && !nombre.isBlank()) ||
            (especialidad != null && !especialidad.isBlank());

        if (!hayFiltro) {
            return obtenerTodos(); // mismo comportamiento de siempre
        }

        String c = (cedula == null || cedula.isBlank()) ? null : cedula.trim();
        String n = (nombre == null || nombre.isBlank()) ? null : nombre.trim();
        String e = (especialidad == null || especialidad.isBlank()) ? null : especialidad.trim();

        return veterinarioRepository.buscar(c, n, e);
    }
}