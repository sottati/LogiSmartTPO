package com.logismart.infraestructura.estructural.fabrica;

import com.logismart.infraestructura.estructural.notificacion.Notificador;
import com.logismart.infraestructura.estructural.notificacion.NotificadorEmail;
import com.logismart.infraestructura.estructural.notificacion.NotificadorPush;
import com.logismart.infraestructura.estructural.notificacion.NotificadorSMS;

public enum TipoNotificador {
    EMAIL {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorEmail();
        }
    },
    SMS {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorSMS();
        }
    },
    PUSH {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorPush();
        }
    };

    public abstract Notificador crearNotificador();
}
