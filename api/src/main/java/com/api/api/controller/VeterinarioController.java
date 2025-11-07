package com.api.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.dto.VeterinarioCreateDTO;
import com.api.api.dto.VeterinarioUpdateDTO;
import com.api.api.model.UserEntity;
import com.api.api.model.Veterinario;
import com.api.api.repository.UserEntityRepository;
import com.api.api.security.CustomUserDetailService;
import com.api.api.service.serviceInterface.VeterinarioService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;



    // http://localhost:8080/api/veterinarios/login?nombreUsuario=vet1&contrasenia=pass123
    @PostMapping("/login")
    public ResponseEntity<?> loginVeterinario(@RequestParam String nombreUsuario,
                                            @RequestParam String contrasenia) {

    /*Se valida el veterinario y si tiene la cuenta activa o inactiva */
        Veterinario vet = veterinarioService.validarVeterinario(nombreUsuario, contrasenia);

        if(vet==null){
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        if(vet.getActivo()==0){
            return ResponseEntity.status(403).body("Tu cuenta está inactiva. Comunícate con el administrador para activarla.");
        }
        return ResponseEntity.ok(vet);
    }

    // http://localhost:8080/api/veterinarios
    /* Obtener a todos los veterinarios (o filtrados si vienen query params) */
    @GetMapping
    public ResponseEntity<List<Veterinario>> obtenerTodos(
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String especialidad) {

        List<Veterinario> data = veterinarioService.buscar(cedula, nombre, especialidad);
        return ResponseEntity.ok(data);
    }

    // http://localhost:8080/api/veterinarios/activos
    /*Obtener a todos los veterinarios activos */
    @GetMapping("/activos")
    public List<Veterinario> obtenerActivos() {
        return veterinarioService.obtenerVeterinariosActivos();
    }

    // http://localhost:8080/api/veterinarios/1
     /*Obtener un veterinario por su ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Veterinario vet = veterinarioService.obtenerVeterinarioPorId(id);
        return vet == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(vet);
    }

    // http://localhost:8080/api/veterinarios
    /* Crear un nuevo veterinario */
    @PostMapping
    public ResponseEntity<Veterinario> crear(@RequestBody VeterinarioCreateDTO dto) {
        try {


            //Se verifica si ya existe el veterinario
            if(userEntityRepository.existsByUsername(dto.getNombreUsuario())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        
            }

            /*Se utiliza el DTO que acepta solamente nombre, usuario
             * , especialidad, contraseña e imagen. De resto se pone en default y se manda al 
             * servicio para guardarlo en la base de datos.
             */
            Veterinario nuevo = new Veterinario();
            nuevo.setNombre(dto.getNombre());
            nuevo.setEspecialidad(dto.getEspecialidad());
            nuevo.setNombreUsuario(dto.getNombreUsuario());
            nuevo.setContrasenia(dto.getContrasenia());
            nuevo.setImagen(dto.getImagen());
            nuevo.setActivo(1);
            nuevo.setConsultas(0);

            //Se guarda y crea el UserEntity
             UserEntity userEntity = customUserDetailService.saveVeterinario(nuevo);
            nuevo.setUserEntity(userEntity);

            Veterinario guardado = veterinarioService.guardarVeterinario(nuevo);

            if(guardado==null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // http://localhost:8080/api/veterinarios/1
    /* Actualizar un veterinario existente */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody VeterinarioUpdateDTO veterinarioDTO) {
        try {
            Veterinario existente = veterinarioService.obtenerVeterinarioPorId(id);
            if(existente==null){
                return ResponseEntity.notFound().build();
            }
            
            //Se actualizan solo los campos que se aceptan en el DTO
            if(veterinarioDTO.getNombre() != null){
                existente.setNombre(veterinarioDTO.getNombre());
            }

            if(veterinarioDTO.getEspecialidad() != null){
                existente.setEspecialidad(veterinarioDTO.getEspecialidad());
            }

            if(veterinarioDTO.getNombreUsuario() != null){
                existente.setNombreUsuario(veterinarioDTO.getNombreUsuario());
            }
            if(veterinarioDTO.getImagen() != null){
                existente.setImagen(veterinarioDTO.getImagen());
            }
            
            //Solo se actualiza la contraseña si no está vacía
            if (veterinarioDTO.getContrasenia() != null && !veterinarioDTO.getContrasenia().trim().isEmpty()) {
                existente.setContrasenia(veterinarioDTO.getContrasenia());
            }
            
            Veterinario actualizado = veterinarioService.guardarVeterinario(existente);
            return ResponseEntity.ok(actualizado);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar veterinario: " + e.getMessage());
        }
    }

    // http://localhost:8080/api/veterinarios/1
    /* Eliminar un veterinario, pero se desvinculan sus relaciones primero */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVeterinario(@PathVariable Long id) {

        //Se intenta eliminar el veterinario y se manejan las excepciones
        try{
            veterinarioService.eliminarVeterinario(id);
            return ResponseEntity.ok("Veterinario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Veterinario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar veterinario: " + e.getMessage());
        }
    }

    // http://localhost:8080/api/veterinarios/1/estado
    /* Actualizar el estado (activo/inactivo) de un veterinario */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        try {
            Integer nuevoEstado = request.get("activo");

            //SE VALIDA QUE EL ESTADO SEA 0 O 1 mandado desde el front
            if (nuevoEstado==null || (nuevoEstado!= 0 && nuevoEstado!=1)) {
                return ResponseEntity.badRequest().body("El estado debe ser 0 o 1");
            }
            
            //Se actualiza el estado por el servicio
            Veterinario veterinarioActualizado = veterinarioService.actualizarEstado(id, nuevoEstado);
            if (veterinarioActualizado == null) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(veterinarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el estado: " + e.getMessage());
        }
    }
}