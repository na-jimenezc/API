package com.api.api.service.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.api.model.Administrador;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.VeterinarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VeterinarioServiceImpl implements VeterinarioService {

    @Autowired
    private final VeterinarioRepository veterinarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    //Guardar un nuevo veterinario definiendo su contraseña
    @Override
    public Veterinario guardarVeterinario(Veterinario veterinario) {
        if (veterinario.getUserEntity() != null) {
            if (veterinario.getContrasenia() != null && !veterinario.getContrasenia().isEmpty()) {
                veterinario.getUserEntity().setPassword(
                    passwordEncoder.encode(veterinario.getContrasenia())
                );
            }
        }

        return veterinarioRepository.save(veterinario);
    }

    //Eliminar un veterinario, pero se rompen las relaciones primero para que no llore el JPA
    @Override
    @Transactional
    public void eliminarVeterinario(Long id) {
        System.out.println("Eliminando veterinario con ID: " + id);

        Veterinario veterinario = veterinarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + id));

        System.out.println("Veterinario encontrado: " + veterinario.getNombre());

        if (veterinario.getAdministradores() != null && !veterinario.getAdministradores().isEmpty()) {
            System.out.println("Rompiendo relación con administradores...");
            for (Administrador admin : veterinario.getAdministradores()) {
                admin.getVeterinarios().remove(veterinario);
            }
            veterinario.getAdministradores().clear();
        }

        if (veterinario.getTratamientos() != null && !veterinario.getTratamientos().isEmpty()) {
            System.out.println("Rompiendo relación con tratamientos...");
            for (Tratamiento t : veterinario.getTratamientos()) {
                t.setVeterinario(null);
            }
            veterinario.getTratamientos().clear();
        }

        if (veterinario.getUserEntity() != null) {
            System.out.println("Rompiendo relación con UserEntity...");
            veterinario.setUserEntity(null);
        }

        veterinarioRepository.save(veterinario);
        System.out.println("Relaciones rotas, procediendo a eliminar...");
        
        veterinarioRepository.delete(veterinario);
        System.out.println("Veterinario eliminado correctamente");
    }

    //Actualizar el estado (activo/inactivo) de un veterinario
    @Override
    public Veterinario actualizarEstado(Long id, int estado) {
        Veterinario veterinario = veterinarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id " + id));

        veterinario.setActivo(estado);
        return veterinarioRepository.save(veterinario);
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