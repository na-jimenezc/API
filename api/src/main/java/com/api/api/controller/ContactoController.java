package com.api.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.service.serviceInterface.ContactoService;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoController {

    @Autowired
    private ContactoService emailService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> procesarFormulario(@RequestBody Map<String, String> datos) {
        System.out.println("\nPETICIÓN RECIBIDA EN EL BACKEND");
        System.out.println("Datos: " + datos);
        
        try {
            String nombre = datos.get("nombre");
            String email = datos.get("email");
            String telefono = datos.get("telefono");
            String asunto = datos.get("asunto");
            String mensaje = datos.get("mensaje");

            if (nombre == null || nombre.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                asunto == null || asunto.trim().isEmpty() ||
                mensaje == null || mensaje.trim().isEmpty()) {
                
                System.out.println("Validación fallida - Campos vacíos");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("mensaje", "Todos los campos obligatorios deben ser completados");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            System.out.println("Enviando correo de confirmación a: " + email);
            emailService.enviarCorreoConfirmacion(email, nombre);
            System.out.println("Correo de confirmación enviado");

            System.out.println("Enviando notificación al admin");
            emailService.enviarNotificacionAdmin(nombre, email, telefono, asunto, mensaje);
            System.out.println("Notificación admin enviada");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("mensaje", "¡Gracias! Recibimos tu mensaje y te contactaremos pronto.");
            response.put("nombre", nombre);
            response.put("email", email);

            System.out.println("Respuesta exitosa enviada al frontend");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("ERROR EN PROCESARFORMULARIO");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("mensaje", "Hubo un error al procesar tu solicitud. Por favor, inténtalo nuevamente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}