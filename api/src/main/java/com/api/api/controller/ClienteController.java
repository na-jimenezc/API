package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.service.serviceInterface.ClienteService;
//import com.api.api.service.serviceInterface.MascotaService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //@Autowired
    //private MascotaService mascotaService;

    //Función para obtener todos los clientes
    @GetMapping
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerTodos();
    }

    //Función para obtener un cliente por ID
    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        return (cliente != null) ? cliente : null;
    }

    //Función para crear un nuevo cliente
    @PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        if (clienteService.existeClientePorCedula(cliente.getCedula())) {
           return null;
        }
        Cliente nuevo = clienteService.guardarCliente(cliente);
        return nuevo;
    }

    //Función para actualizar un cliente existente
    @PutMapping("/{id}")
    public Cliente actualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente clienteRequest) {
        try {
            Cliente actualizado = clienteService.actualizarCliente(
                    id,
                    clienteRequest.getCedula(),
                    clienteRequest.getNombre(),
                    clienteRequest.getCorreo(),
                    clienteRequest.getCelular()
            );
            return actualizado;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    //Función para eliminar un cliente permanentemente
    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarClienteHard(id);
    }

    //Función para listar mascotas por cliente
    @GetMapping("/{id}/mascotas")
    public List<Mascota> obtenerMascotasPorCliente(@PathVariable Long id) {
        List<Mascota> mascotas = clienteService.obtenerMascotasPorClienteId(id);
        return mascotas;
    }

    //Función para obtener un cliente por cédula
    @GetMapping("/cedula/{cedula}")
    public Cliente obtenerPorCedula(@PathVariable String cedula) {
        Cliente cliente = clienteService.obtenerPorCedula(cedula);
        return (cliente != null) ? cliente : null;
    }

    // === LOGIN DE CLIENTE ===
    @PostMapping("/login")
    public ResponseEntity<?> loginCliente(@RequestBody Cliente loginRequest) {
        if (loginRequest == null || loginRequest.getCorreo() == null || loginRequest.getCedula() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios");
        }

        Cliente cliente = clienteService.validarCliente(loginRequest.getCorreo(), loginRequest.getCedula());
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}