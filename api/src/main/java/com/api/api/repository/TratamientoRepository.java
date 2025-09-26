package com.api.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.api.model.Tratamiento;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Tratamiento t WHERE t.mascota.cliente.id = :clienteId")
    void deleteByClienteId(@Param("clienteId") Long clienteId);
    
}