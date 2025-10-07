package com.api.api.service.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.repository.ClienteRepository;
import com.api.api.service.serviceInterface.ClienteService;
//import com.api.api.service.serviceInterface.MascotaService;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //@Autowired
    //private MascotaService mascotaService;

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizarCliente(Long id, String cedula, String nombre, String correo, String celular) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setCedula(cedula);
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        cliente.setCelular(celular);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarClienteHard(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existeClientePorCedula(String cedula) {
        return clienteRepository.existsByCedula(cedula);
    }

    @Override
    public Cliente obtenerPorCedula(String cedula) {
        return clienteRepository.findByCedula(cedula);
    }

    @Override
    public Cliente obtenerClientePorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    public List<Mascota> obtenerMascotasPorClienteId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            return cliente.getMascotas(); 
        }
        return List.of();
    }

    @Override
    public Cliente validarCliente(String correo, String cedula) {
        Cliente c = clienteRepository.findByCorreo(correo);
        if (c != null && c.getCedula() != null && c.getCedula().equals(cedula)) {
            return c;
        }
        return null;
    }
}