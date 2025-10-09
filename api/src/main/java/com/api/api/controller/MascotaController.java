package com.api.api.controller;

import java.util.List;
import java.util.stream.Collectors;
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
import com.api.api.dto.MascotaCreateDTO;
import com.api.api.dto.MascotaDTO;
import com.api.api.dto.MascotaUpdateDTO;
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

    @GetMapping
    public List<Mascota> listarMascotas() {
        return (mascotaService.obtenerTodasMascotas());
    }

     @GetMapping("/todas")
    public List<MascotaDTO> listarTodasMascotas() {
        List<Mascota> mascotas = mascotaService.obtenerTodasMascotas();
        
        return mascotas.stream()
                .map(mascota -> new MascotaDTO(
                    mascota.getId(),
                    mascota.getNombre(),
                    mascota.getRaza(),
                    mascota.getEdad(),
                    mascota.getTipo(),
                    mascota.getEnfermedad(),
                    mascota.getPeso(),
                    mascota.getFotoURL(),
                    mascota.getActivo(),
                    mascota.getCliente() != null ? mascota.getCliente().getId() : null,
                    mascota.getEstado()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MascotaDTO verDetalleMascota(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id);
        if (mascota == null) {
            return null;
        }
        return new MascotaDTO(
            mascota.getId(),
            mascota.getNombre(),
            mascota.getRaza(),
            mascota.getEdad(),
            mascota.getTipo(),
            mascota.getEnfermedad(),
            mascota.getPeso(),
            mascota.getFotoURL(),
            mascota.getActivo(),
            mascota.getCliente() != null ? mascota.getCliente().getId() : null,
            mascota.getEstado()
        );
    }

    @PostMapping
    public MascotaDTO crearMascota(@RequestBody MascotaCreateDTO dto) {
        Cliente cliente = clienteService.obtenerClientePorId(dto.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setRaza(dto.getRaza());
        mascota.setEdad(dto.getEdad());
        mascota.setTipo(dto.getTipo());
        mascota.setEnfermedad(dto.getEnfermedad());
        mascota.setPeso(dto.getPeso());
        mascota.setFotoURL(dto.getFotoURL());
        mascota.setActivo(dto.isActivo());
        mascota.setEstado(dto.getEstado());
        mascota.setCliente(cliente);

        Mascota guardada = mascotaService.registrarMascota(mascota);

        return new MascotaDTO(
            guardada.getId(),
            guardada.getNombre(),
            guardada.getRaza(),
            guardada.getEdad(),
            guardada.getTipo(),
            guardada.getEnfermedad(),
            guardada.getPeso(),
            guardada.getFotoURL(),
            guardada.getActivo(),
            guardada.getCliente().getId(),
            guardada.getEstado()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaUpdateDTO dto) { 
        try {
            Mascota actualizada = mascotaService.actualizarMascota(
                    id,
                    dto.getNombre(),
                    dto.getTipo(),
                    dto.getRaza(),
                    dto.getEnfermedad(),
                    dto.getFotoURL(),
                    dto.isActivo()
            );
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Mascota no encontrada con ID: " + id);
        }return ResponseEntity.ok(actualizada);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar mascota: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/desactivar")
    public void desactivarMascota(@PathVariable Long id) {
        mascotaService.desactivarMascota(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascotaHard(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> listarPorCliente(@PathVariable Long clienteId) {
        return mascotaService.obtenerMascotasPorClienteId(clienteId);
    }
}