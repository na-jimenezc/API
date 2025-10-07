package com.api.api.service.serviceInterface;

import java.util.List;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;


public interface ClienteService {
    List<Cliente> obtenerTodos();
    Cliente obtenerClientePorId(Long id);
    Cliente guardarCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, String cedula, String nombre, String correo, String celular);
    void eliminarClienteHard(Long id);

    boolean existeClientePorCedula(String cedula);
    Cliente obtenerPorCedula(String cedula);
    Cliente obtenerClientePorCorreo(String correo);

    List<Mascota> obtenerMascotasPorClienteId(Long id);
    Cliente validarCliente(String correo, String cedula);
}

