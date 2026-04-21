package com.logismart.infraestructura.notificacion;

/**
 * GRASP Polymorphism: Implementacion de Notificador para notificaciones Push (mobile/web).
 * El destinatario es el device token del dispositivo registrado.
 */
public class NotificadorPush implements Notificador {

    @Override
    public void notificar(String destinatario, String asunto, String mensaje) {
        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("Device token invalido");
        }
        // En produccion: integracion con FCM / APNs
        System.out.printf("[PUSH] Token: %s | Titulo: %s | Cuerpo: %s%n",
                destinatario, asunto, mensaje);
    }

    @Override
    public String getCanal() {
        return "PUSH";
    }
}
