package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.api.dto.LoginRequest;
import com.api.api.model.Administrador;
import com.api.api.model.Veterinario;
import com.api.api.service.serviceInterface.AdministradorService;
import com.api.api.service.serviceInterface.VeterinarioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdministradorController {

    @Autowired private VeterinarioService veterinarioService;
    @Autowired private AdministradorService administradorService;

    @PostMapping("/login")
    public ResponseEntity<Administrador> loginAdmin(@RequestBody LoginRequest login) {
        Administrador admin = administradorService.obtenerPorCorreo(login.getCorreo());
        if (admin != null && admin.getClave().equals(login.getClave())) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/veterinarios")
    public ResponseEntity<List<Veterinario>> listarVeterinarios() {
        List<Veterinario> veterinarios = veterinarioService.obtenerVeterinariosActivos();
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/veterinarios/{id}")
    public ResponseEntity<?> detalleVeterinario(@PathVariable Long id) {
        Veterinario vet = veterinarioService.obtenerVeterinarioPorId(id);
        if (vet == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(vet);
    }

    @GetMapping("/dashboard")
    public String dashboardAdmin() {
        return "¡Bienvenido al Dashboard del Administrador!";
    }
}