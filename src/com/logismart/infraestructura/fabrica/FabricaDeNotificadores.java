package com.logismart.infraestructura.fabrica;

import com.logismart.infraestructura.notificacion.Notificador;
import com.logismart.infraestructura.notificacion.NotificadorEmail;
import com.logismart.infraestructura.notificacion.NotificadorPush;
import com.logismart.infraestructura.notificacion.NotificadorSMS;

/**
 * Factory Method para desacoplar creacion de canales de notificacion.
 */
public final class FabricaDeNotificadores {

    private FabricaDeNotificadores() {
    }

    public static Notificador crearNotificador(TipoNotificador tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de notificador invalido");
        }
        return switch (tipo) {
            case EMAIL -> new NotificadorEmail();
            case SMS -> new NotificadorSMS();
            case PUSH -> new NotificadorPush();
        };
    }
}
