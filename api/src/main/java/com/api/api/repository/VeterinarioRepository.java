package com.api.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.api.model.Veterinario;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Veterinario findByNombreUsuarioAndContrasenia(String nombreUsuario, String contrasenia);
    List<Veterinario> findByActivo(int activo);
    // Filtra por (cedula/id) o nombre o especialidad. Todos los params son opcionales.
    @Query("""
        SELECT v FROM Veterinario v
        WHERE
            (:cedula IS NOT NULL AND CAST(v.id AS string) LIKE CONCAT('%', :cedula, '%'))
         OR (:nombre IS NOT NULL AND LOWER(v.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
         OR (:especialidad IS NOT NULL AND LOWER(v.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')))
    """)
    List<Veterinario> buscar(
        @Param("cedula") String cedula,
        @Param("nombre") String nombre,
        @Param("especialidad") String especialidad
    );
}