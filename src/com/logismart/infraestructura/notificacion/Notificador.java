package com.logismart.infraestructura.notificacion;

/**
 * GRASP Indirection + Polymorphism: Abstraccion para el envio de notificaciones.
 * Desacopla ServicioDeNotificaciones de la implementacion concreta (Email, SMS, Push).
 * Punto de extension para agregar nuevos canales sin modificar el servicio.
 */
public interface Notificador {

    /**
     * Envia una notificacion al destinatario dado.
     *
     * @param destinatario identificador del receptor (email, telefono, token, etc.)
     * @param asunto       titulo o asunto del mensaje
     * @param mensaje      cuerpo del mensaje
     */
    void notificar(String destinatario, String asunto, String mensaje);

    /**
     * Indica el canal que usa esta implementacion (EMAIL, SMS, PUSH).
     */
    String getCanal();
}
