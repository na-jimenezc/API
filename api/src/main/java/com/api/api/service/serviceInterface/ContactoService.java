package com.api.api.service.serviceInterface;

public interface ContactoService {

    public void enviarCorreoConfirmacion(String destinatario, String nombre);
    public void enviarNotificacionAdmin(String nombre, String email, String telefono, String asunto, String mensaje);
}
