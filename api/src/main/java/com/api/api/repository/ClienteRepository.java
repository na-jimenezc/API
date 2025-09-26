package com.api.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.api.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCedula(String cedula);
    Cliente findByCorreo(String correo);
    Cliente findByCedula(String cedula);
}