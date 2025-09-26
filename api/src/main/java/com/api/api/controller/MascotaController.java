package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.service.serviceInterface.ClienteService;
import com.api.api.service.serviceInterface.MascotaService;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "http://localhost:4200")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private ClienteService clienteService;

    //Función para obtener todas las mascotas
    @GetMapping
    public List<Mascota> listarMascotas() {
        return (mascotaService.obtenerTodasMascotas());
    }

    //Función para obtener mascotas paginadas
    @GetMapping("/paginadas")
    public Page<Mascota> listarMascotasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return mascotaService.obtenerMascotasPaginadas(PageRequest.of(page, size));
    }

    //Función para ver detalle de una mascota por ID
    @GetMapping("/{id}")
    public Mascota verDetalleMascota(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id);
        if (mascota == null) {
            return null;
        }
        return mascota;
    }

    //Función para crear una nueva mascota
    @PostMapping
    public Mascota crearMascota(@RequestBody Mascota mascota,
                                                @RequestParam("clienteId") Long clienteId) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        if (cliente == null) {
            return null;
        }
        mascota.setCliente(cliente);
        Mascota guardada = mascotaService.registrarMascota(mascota);
        return guardada;
    }

    //Función para actualizar una mascota existente
    @PutMapping("/{id}")
    public Mascota actualizarMascota(
            @PathVariable Long id,
            @RequestBody Mascota mascota) {

        Mascota actualizada = mascotaService.actualizarMascota(
                id,
                mascota.getNombre(),
                mascota.getTipo(),
                mascota.getRaza(),
                mascota.getEnfermedad(),
                mascota.getFotoURL(),
                mascota.getActivo()
        );
        return actualizada;
    }

    //Función para desactivar una mascota 
    @DeleteMapping("/{id}/desactivar")
    public void desactivarMascota(@PathVariable Long id) {
        mascotaService.desactivarMascota(id);
    }

    //Función para eliminar una mascota permanentemente
    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascotaHard(id);
    }

    //Función para listar mascotas por cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> listarPorCliente(@PathVariable Long clienteId) {
        return mascotaService.obtenerMascotasPorClienteId(clienteId);
    }
}
