package com.logismart.aplicacion.servicios;

import com.logismart.dominio.envio.SeguimientoEnvio;

public class ServicioSeguimiento {
    public void publicarTracking(SeguimientoEnvio seguimiento) {
        if (seguimiento == null) {
            throw new IllegalArgumentException("El seguimiento no puede ser nulo");
        }
    }
}
