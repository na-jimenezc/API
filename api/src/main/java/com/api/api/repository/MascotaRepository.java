package com.api.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.api.api.model.Mascota;
import com.api.api.model.Veterinario;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

   //NOTA: ESTA QUERY FUE MODIFICADA PORQUE NO TIENE SENTIDO EN LA JPA TENER OPTIONAL
    @Query("SELECT DISTINCT m FROM Mascota m " +
       "JOIN m.tratamientos t " +
       "JOIN t.veterinario v " +
       "WHERE v.id = :veterinarioId")
   List<Mascota> findByVeterinarioIdIncluyendoTratamientos(@Param("veterinarioId") Long veterinarioId);


    @Query("SELECT m FROM Mascota m WHERE m.cliente.id = :clienteId")
       List<Mascota> findByClienteId(@Param("clienteId") Long clienteId);
       @Modifying
       @Transactional
       @Query("DELETE FROM Mascota m WHERE m.cliente.id = :clienteId")
       void deleteByClienteId(@Param("clienteId") Long clienteId);

   @EntityGraph(attributePaths = {"cliente"})
   @Override
   Page<Mascota> findAll(Pageable pageable);
}