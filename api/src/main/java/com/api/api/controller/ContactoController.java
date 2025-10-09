package com.api.api.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoController {

    @GetMapping
    public ResponseEntity<Map<String, String>> mostrarMensaje(@RequestParam(value = "ok", required = false) String ok) {
        Map<String, String> response = new HashMap<>();
        if (ok != null) {
            response.put("mensajeExito", "¡Gracias! Recibimos tu mensaje y te contactaremos pronto.");
        } else {
            response.put("mensaje", "Puedes enviar tu mensaje mediante este endpoint.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> procesarFormulario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam String asunto,
            @RequestParam String mensaje) {

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("mensaje", "¡Gracias! Recibimos tu mensaje y te contactaremos pronto.");
        response.put("nombre", nombre);
        response.put("email", email);
        if (telefono != null) response.put("telefono", telefono);
        response.put("asunto", asunto);

        return ResponseEntity.ok(response);
    }
}