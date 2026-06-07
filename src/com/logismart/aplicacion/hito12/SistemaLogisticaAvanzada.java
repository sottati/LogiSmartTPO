package com.logismart.aplicacion.hito12;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.comportamiento.state.EstadoConfirmado;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaCalculoCosto;
import com.logismart.infraestructura.comportamiento.template.ProcesoEnvio;
import com.logismart.infraestructura.comportamiento.visitor.NodoCentroRegional;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCalculoCostoOperativo;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCalculoOcupacion;

public class SistemaLogisticaAvanzada {
    private final NodoCentroRegional redDistribucion;

    public SistemaLogisticaAvanzada(NodoCentroRegional redDistribucion) {
        this.redDistribucion = redDistribucion;
    }

    public double procesarEnvio(Envio envio, EstrategiaCalculoCosto estrategia, ProcesoEnvio proceso) {
        envio.cambiarEstado(new EstadoConfirmado());
        envio.validar();
        envio.entregar();
        envio.establecerEstrategia(estrategia);
        double costo = envio.calcularCostoConEstrategia();
        proceso.procesarEnvio(envio);
        envio.entregar();
        return costo;
    }

    public double calcularOcupacionRed() {
        VisitorCalculoOcupacion visitor = new VisitorCalculoOcupacion();
        redDistribucion.aceptar(visitor);
        return visitor.obtenerOcupacion();
    }

    public double calcularCostoOperativoRed() {
        VisitorCalculoCostoOperativo visitor = new VisitorCalculoCostoOperativo();
        redDistribucion.aceptar(visitor);
        return visitor.obtenerCostoTotal();
    }

    public NodoCentroRegional obtenerRedDistribucion() {
        return redDistribucion;
    }
}

