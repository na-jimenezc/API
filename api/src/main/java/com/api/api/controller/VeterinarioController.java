package com.api.api.controller;

import com.api.api.dto.VeterinarioLoginRequest;
import com.api.api.model.Veterinario;
import com.api.api.service.serviceInterface.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @PostMapping("/login")
    public ResponseEntity<?> loginVeterinario(@RequestParam String nombreUsuario,
                                            @RequestParam String contrasenia) {
        Veterinario vet = veterinarioService.validarVeterinario(nombreUsuario, contrasenia);
        if (vet != null) {
            return ResponseEntity.ok(vet);
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }

    @GetMapping
    public List<Veterinario> obtenerTodos() {
        return veterinarioService.obtenerTodos();
    }

    @GetMapping("/activos")
    public List<Veterinario> obtenerActivos() {
        return veterinarioService.obtenerVeterinariosActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Veterinario vet = veterinarioService.obtenerVeterinarioPorId(id);
        return vet == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(vet);
    }

    @PostMapping
    public Veterinario crear(@RequestBody Veterinario veterinario) {
        return veterinarioService.guardarVeterinario(veterinario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        Veterinario existente = veterinarioService.obtenerVeterinarioPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        veterinario.setId(id);
        return ResponseEntity.ok(veterinarioService.guardarVeterinario(veterinario));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
    }
}