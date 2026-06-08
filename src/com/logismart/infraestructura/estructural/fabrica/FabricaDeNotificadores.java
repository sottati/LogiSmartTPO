package com.logismart.infraestructura.estructural.fabrica;

import com.logismart.infraestructura.estructural.notificacion.Notificador;

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
        return tipo.crearNotificador();
    }
}
