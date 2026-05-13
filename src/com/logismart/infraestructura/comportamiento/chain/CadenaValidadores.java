package com.logismart.infraestructura.comportamiento.chain;

import com.logismart.dominio.Envio;

public class CadenaValidadores {
    private final ValidadorEnvio primerValidador;

    public CadenaValidadores(SistemaInventario inventario, SistemaCapacidad capacidad) {
        ValidadorEnvio v1 = new ValidadorDatos();
        ValidadorEnvio v2 = new ValidadorInventario(inventario);
        ValidadorEnvio v3 = new ValidadorPago();
        ValidadorEnvio v4 = new ValidadorSeguridad();
        ValidadorEnvio v5 = new ValidadorCapacidad(capacidad);

        v1.setSiguiente(v2);
        v2.setSiguiente(v3);
        v3.setSiguiente(v4);
        v4.setSiguiente(v5);

        this.primerValidador = v1;
    }

    public boolean validarEnvio(Envio envio) {
        System.out.println("\n=== Validando Envío ===");
        return primerValidador.validar(envio);
    }
}
