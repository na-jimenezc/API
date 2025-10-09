package com.api.api.service.serviceImplementation;

import java.util.List;
import org.springframework.stereotype.Service;
import com.api.api.model.Veterinario;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.VeterinarioService;

@Service
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioServiceImpl(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    public List<Veterinario> obtenerTodos() {
        return veterinarioRepository.findAll();
    }

    @Override
    public Veterinario validarVeterinario(String nombreUsuario, String contrasenia) {
        return veterinarioRepository.findByNombreUsuarioAndContrasenia(nombreUsuario, contrasenia);
    }

    @Override
    public List<Veterinario> obtenerVeterinariosActivos() {
        return veterinarioRepository.findByActivo(1);
    }

    @Override
    public Veterinario obtenerVeterinarioPorId(Long id) {
        return veterinarioRepository.findById(id).orElse(null);
    }

    @Override
    public Veterinario guardarVeterinario(Veterinario veterinario) {
        return veterinarioRepository.save(veterinario);
    }

    @Override
    public void eliminarVeterinario(Long id) {
        veterinarioRepository.deleteById(id);
    }
}