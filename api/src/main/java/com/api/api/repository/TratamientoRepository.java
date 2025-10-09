package com.api.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.api.api.model.Mascota;
import com.api.api.model.Tratamiento;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Tratamiento t WHERE t.mascota.cliente.id = :clienteId")
    void deleteByClienteId(@Param("clienteId") Long clienteId);

    List<Tratamiento> findByMascota(Mascota mascota);
    List<Tratamiento> findByMascotaId(Long idMascota);

    @Query("SELECT t FROM Tratamiento t " +
           "LEFT JOIN FETCH t.medicamento " +
           "LEFT JOIN FETCH t.veterinario " +
           "WHERE t.mascota.id = :mascotaId " +
           "ORDER BY t.fecha DESC")
    List<Tratamiento> findByMascotaIdWithRelations(@Param("mascotaId") Long mascotaId);
}