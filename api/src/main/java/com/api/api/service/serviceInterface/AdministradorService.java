package com.api.api.service.serviceInterface;

import java.util.List;
import com.api.api.model.Administrador;

public interface AdministradorService {
    boolean validar(String correo, String clave);
    Administrador obtenerPorCorreo(String correo);
    List<Administrador> obtenerTodos();
    Administrador obtenerPorId(Long id);
    Administrador guardar(Administrador admin);   
}