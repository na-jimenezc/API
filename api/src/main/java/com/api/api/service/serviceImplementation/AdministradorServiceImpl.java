package com.api.api.service.serviceImplementation;
import java.util.List;

import org.springframework.stereotype.Service;

import com.api.api.model.Administrador;
import com.api.api.repository.AdministradorRepository;
import com.api.api.service.serviceInterface.AdministradorService;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorServiceImpl(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public boolean validar(String correo, String clave) {
        Administrador admin = administradorRepository.findByCorreo(correo);
        return admin != null && admin.getClave().equals(clave);
    }

    @Override
    public Administrador obtenerPorCorreo(String correo) {
        return administradorRepository.findByCorreo(correo);
    }

    @Override
    public Administrador guardar(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    @Override
    public Administrador obtenerPorId(Long id) {
        return administradorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }
}