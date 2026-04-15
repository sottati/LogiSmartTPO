package com.logismart.infraestructura.notificacion;

/**
 * GRASP Polymorphism: Implementacion de Notificador para el canal SMS.
 * El destinatario es un numero de telefono en formato internacional.
 */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(String destinatario, String asunto, String mensaje) {
        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("Numero de telefono invalido");
        }
        // En produccion: integracion con Twilio / AWS SNS
        System.out.printf("[SMS] Para: %s | Mensaje: %s%n", destinatario, mensaje);
    }

    @Override
    public String getCanal() {
        return "SMS";
    }
}
