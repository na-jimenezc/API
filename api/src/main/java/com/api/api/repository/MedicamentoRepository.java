package com.api.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.api.model.Medicamento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    Optional<Medicamento> findByNombreIgnoreCase(String nombre);    
}