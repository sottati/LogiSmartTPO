package com.logismart.infraestructura.estructural.notificacion;

/**
 * GRASP Polymorphism: Implementacion de Notificador para el canal Email.
 * El ServicioDeNotificaciones no necesita saber que es un email; solo invoca notificar().
 */
public class NotificadorEmail implements Notificador {

    @Override
    public void notificar(String destinatario, String asunto, String mensaje) {
        if (destinatario == null || !destinatario.contains("@")) {
            throw new IllegalArgumentException("Email invalido: " + destinatario);
        }
        // En produccion: integracion con proveedor SMTP / SendGrid / SES
        System.out.printf("[EMAIL] Para: %s | Asunto: %s | Cuerpo: %s%n",
                destinatario, asunto, mensaje);
    }

    @Override
    public String getCanal() {
        return "EMAIL";
    }
}
