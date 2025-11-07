package com.api.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.api.api.model.UserEntity;
import com.api.api.repository.UserEntityRepository;
import com.api.api.security.CustomUserDetailService;
import com.api.api.service.serviceInterface.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserEntityRepository userRepository;

    // http://localhost:8080/api/clientes
    @GetMapping
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerTodos();
    }

    // http://localhost:8080/api/clientes/1
    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        return (cliente != null) ? cliente : null;
    }


    // http://localhost:8080/api/clientes
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        try {
            System.out.println("INICIO CREACIÓN CLIENTE");
            System.out.println("Correo: " + cliente.getCorreo());
            System.out.println("Cédula: " + cliente.getCedula());
            
            if(userRepository.existsByUsername(cliente.getCorreo())) {
                System.out.println("ERROR: Username ya existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if(clienteService.existeClientePorCedula(cliente.getCedula())) {
                System.out.println("ERROR: Cédula ya existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            System.out.println("Guardando UserEntity...");
            UserEntity userEntity = customUserDetailService.saveCliente(cliente);
            System.out.println("UserEntity guardado con éxito. ID: " + userEntity.getId());

            cliente.setUserEntity(userEntity);
            System.out.println("UserEntity asignado al cliente");
            
            System.out.println("Guardando Cliente...");
            Cliente guardado = clienteService.guardarCliente(cliente);
            System.out.println("Cliente guardado con éxito. ID: " + guardado.getId());
            
            if(guardado == null){
                System.out.println("ERROR: Cliente no se pudo guardar");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            System.out.println("=== FIN CREACIÓN CLIENTE ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
            
        } catch (Exception e) {
            System.err.println("ERROR EN CREACIÓN: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



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

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarClienteHard(id);
    }


    // http://localhost:8080/api/clientes/{id}/mascotas
    // http://localhost:8080/api/clientes/1/mascotas
    @GetMapping("/{id}/mascotas")
    public ResponseEntity<List<Mascota>> obtenerMascotasPorCliente(@PathVariable Long id) {
        List<Mascota> mascotas = clienteService.obtenerMascotasPorClienteId(id);
        if (mascotas == null) {
            mascotas = Collections.emptyList();
        }
        return ResponseEntity.ok(mascotas);
    }


    // http://localhost:8080/api/clientes/cedula/{cedula}
    // http://localhost:8080/api/clientes/cedula/1234567890
    @GetMapping("/cedula/{cedula}")
    public Cliente obtenerPorCedula(@PathVariable String cedula) {
        Cliente cliente = clienteService.obtenerPorCedula(cedula);
        return (cliente != null) ? cliente : null;
    }

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