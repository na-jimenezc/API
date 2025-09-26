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
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    //Función para obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        return (cliente != null) ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    //Función para crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        if (clienteService.existeClientePorCedula(cliente.getCedula())) {
            return ResponseEntity.badRequest().body(null);
        }
        Cliente nuevo = clienteService.guardarCliente(cliente);
        return ResponseEntity.ok(nuevo);
    }

    //Función para actualizar un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
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
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Función para eliminar un cliente permanentemente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarClienteHard(id);
        return ResponseEntity.noContent().build();
    }

    //Función para listar mascotas por cliente
    @GetMapping("/{id}/mascotas")
    public ResponseEntity<List<Mascota>> obtenerMascotasPorCliente(@PathVariable Long id) {
        List<Mascota> mascotas = clienteService.obtenerMascotasPorClienteId(id);
        return ResponseEntity.ok(mascotas);
    }

    //Función para obtener un cliente por cédula
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Cliente> obtenerPorCedula(@PathVariable String cedula) {
        Cliente cliente = clienteService.obtenerPorCedula(cedula);
        return (cliente != null) ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    //Función para login de cliente
    @PostMapping("/login")
    public ResponseEntity<Cliente> loginCliente(@RequestBody Cliente loginRequest) {
        Cliente cliente = clienteService.obtenerClientePorCorreo(loginRequest.getCorreo());

        //Significa que no está autorizado
        if (cliente == null) {
            return ResponseEntity.status(401).build(); 
        }
        return ResponseEntity.ok(cliente);
    }
}