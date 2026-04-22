package com.logismart.infraestructura.fabrica;

import com.logismart.infraestructura.notificacion.Notificador;
import com.logismart.infraestructura.notificacion.NotificadorEmail;
import com.logismart.infraestructura.notificacion.NotificadorPush;
import com.logismart.infraestructura.notificacion.NotificadorSMS;

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
