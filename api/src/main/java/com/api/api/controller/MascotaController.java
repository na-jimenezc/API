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

    //GET : Obtener todas las mascotas 
    // http://localhost:8080/api/mascotas
    @GetMapping
    public ResponseEntity<List<Mascota>> listarMascotas() {
        
        List<Mascota> lista = mascotaService.obtenerTodasMascotas();

        ResponseEntity <List<Mascota>> responde = new ResponseEntity<>(lista, HttpStatus.OK);
        return responde;

    }

    //GET : Obtener todas las mascotas pero en DTO para el Front
    // http://localhost:8080/api/mascotas
    @GetMapping("/todas")
    public ResponseEntity<List<MascotaDTO>> listarTodasMascotas() {
        List<Mascota> mascotas = mascotaService.obtenerTodasMascotas();

        List<MascotaDTO> respuesta = mascotas.stream()
                .map(m -> new MascotaDTO(
                        m.getId(),
                        m.getNombre(),
                        m.getRaza(),
                        m.getEdad(),
                        m.getTipo(),
                        m.getEnfermedad(),
                        m.getPeso(),
                        m.getFotoURL(),
                        m.getActivo(),
                        m.getCliente() != null ? m.getCliente().getId() : null,
                        m.getEstado()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    //GET : Obtener todas la mascota por el id
    // http://localhost:8080/api/mascotas/1

    @GetMapping("/{id}")
    public ResponseEntity<?> verDetalleMascota(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id);

        if (mascota == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Mascota no encontrada con ID: " + id);
        }

        MascotaDTO dto = new MascotaDTO(
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

        return ResponseEntity.ok(dto);
    }

    //POST : Crear una nueva mascota
    @PostMapping
    public ResponseEntity<?> crearMascota(@RequestBody MascotaCreateDTO dto) {
        Cliente cliente = clienteService.obtenerClientePorId(dto.getClienteId());

        if(cliente==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado con ID: " + dto.getClienteId());
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

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    //PUT : Actualizar una mascota
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(@PathVariable Long id,@RequestBody MascotaUpdateDTO dto) {
        try{
            Mascota actualizada = mascotaService.actualizarMascota(
                    id,
                    dto.getNombre(),
                    dto.getTipo(),
                    dto.getRaza(),
                    dto.getEnfermedad(),
                    dto.getFotoURL(),
                    dto.isActivo());
                    return ResponseEntity.ok(actualizada);

        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mascota no encontrada con ID: " + id);
        }catch(Exception e){return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la mascota: " + e.getMessage());
        }
    }

    //DELETE : Desactivar una mascota
    @DeleteMapping("/{id}/desactivar")
    public void desactivarMascota(@PathVariable Long id) {
        mascotaService.desactivarMascota(id);
    }

    //DELETE : Eliminar  una mascota
    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascotaHard(id);
    }

    //GET : Obtener las mascotas por cliente
    // http://localhost:8080/api/mascotas/cliente/{clienteId}
    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> listarPorCliente(@PathVariable Long clienteId) {
        return mascotaService.obtenerMascotasPorClienteId(clienteId);
    }
}