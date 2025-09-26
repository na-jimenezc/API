package com.api.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.api.model.Veterinario;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Veterinario findByNombreUsuarioAndContrasenia(String nombreUsuario, String contrasenia);
    List<Veterinario> findByActivo(int activo);

}
