package com.api.api.service.serviceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.api.api.service.serviceInterface.ContactoService;

import jakarta.mail.internet.MimeMessage;

@Service
public class ContactoServiceImpl implements ContactoService{

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.admin.email}")
    private String adminEmail;

    public void enviarCorreoConfirmacion(String destinatario, String nombre) {
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

                helper.setTo(destinatario);
                helper.setFrom(adminEmail);
                helper.setSubject("Confirmación de contacto - Animalheart");

                String cuerpo =
                        "Hola " + nombre + ",\n\n" +
                        "¡Gracias por contactarnos!\n\n" +
                        "Hemos recibido tu mensaje y uno de nuestros veterinarios se pondrá en contacto contigo muy pronto.\n\n" +
                        "Horarios de atención:\n" +
                        "• Lunes a Viernes: 7:00 am – 8:00 pm\n" +
                        "• Sábados: 9:00 am – 2:00 pm\n" +
                        "• Emergencias: 24/7\n\n" +
                        "Si tienes una emergencia, no dudes en llamarnos al +57 320 534 4512.\n\n" +
                        "Atentamente,\n" +
                        "Equipo Animalheart\n" +
                        "Carrera 77#20-40, Bogotá, Colombia\n" +
                        "animalheart@gmail.com";

                helper.setText(cuerpo, false); 
            });

        } catch (Exception e) {
            System.err.println("Error al enviar correo de confirmación: " + e.getMessage());
            throw new RuntimeException("Error al enviar correo de confirmación");
        }
    }

    public void enviarNotificacionAdmin(String nombre, String email, String telefono, String asunto, String mensajeUsuario) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(adminEmail);
            helper.setSubject("Nueva solicitud de contacto - " + asunto);
            helper.setFrom(adminEmail);
            helper.setReplyTo(email);

            String cuerpo =
                    "Se ha recibido una nueva solicitud de contacto:\n\n" +
                    "DATOS DEL CLIENTE:\n" +
                    "Nombre: " + nombre + "\n" +
                    "Email: " + email + "\n" +
                    "Teléfono: " + (telefono != null && !telefono.isEmpty() ? telefono : "No proporcionado") + "\n" +
                    "Asunto: " + asunto + "\n\n" +
                    "MENSAJE:\n" +
                    mensajeUsuario + "\n" +
                    "\n\nCorreo enviado automáticamente desde el formulario";

            helper.setText(cuerpo, false); 

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación al administrador: " + e.getMessage());
            throw new RuntimeException("Error al enviar notificación al administrador");
        }
    }
}