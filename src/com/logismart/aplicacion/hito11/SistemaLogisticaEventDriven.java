package com.logismart.aplicacion.hito11;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionArray;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionEnvios;
import com.logismart.infraestructura.comportamiento.iterator.IteradorEnvios;
import com.logismart.infraestructura.comportamiento.mediator.CentroDistribucionMediator;
import com.logismart.infraestructura.comportamiento.mediator.MediadorEnvios;
import com.logismart.infraestructura.comportamiento.mediator.MediadorEnviosConcreto;
import com.logismart.infraestructura.comportamiento.mediator.SistemaAuditoria;
import com.logismart.infraestructura.comportamiento.mediator.SistemaNotificacion;
import com.logismart.infraestructura.comportamiento.mediator.SistemaPago;
import com.logismart.infraestructura.comportamiento.mediator.ValidadorEnvioMediator;
import com.logismart.infraestructura.comportamiento.memento.HistorialEnvios;
import com.logismart.infraestructura.comportamiento.observer.CentroDistribucionObservador;
import com.logismart.infraestructura.comportamiento.observer.SistemaNotificacionObservador;

import java.util.List;

/**
 * Integración de los 4 patrones de comportamiento II en una arquitectura event-driven.
 *
 * Los 4 patrones colaboran:
 *   - ITERATOR: los envíos se almacenan en ColeccionArray y se recorren con IteradorEnvios
 *   - MEDIATOR: el pipeline Centro→Validador→Pago→Notificador→Auditoria se orquesta
 *               sin acoplamiento directo entre componentes
 *   - MEMENTO:  cada envío snapshottea su estado antes y después del pipeline
 *   - OBSERVER: los observadores reaccionan automáticamente a cada cambio de estado
 *
 * Patrón: Integración Event-Driven - Hito 11
 */
public class SistemaLogisticaEventDriven {

    private final MediadorEnvios  mediador;
    private final ColeccionEnvios coleccion;
    private final HistorialEnvios historial;
    private final CentroDistribucionMediator centro;
    private final SistemaAuditoria   auditoria;

    public SistemaLogisticaEventDriven() {
        // ── Mediator: construir y registrar todos los componentes ────────────
        this.mediador   = new MediadorEnviosConcreto();
        this.centro     = new CentroDistribucionMediator(mediador);
        ValidadorEnvioMediator validador = new ValidadorEnvioMediator(mediador);
        SistemaPago         pago        = new SistemaPago(mediador);
        SistemaNotificacion notificador = new SistemaNotificacion(mediador);
        this.auditoria  = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notificador);
        mediador.registrarAuditoria(auditoria);

        // ── Iterator: colección principal ────────────────────────────────────
        this.coleccion = new ColeccionArray();

        // ── Memento: historial de estados ────────────────────────────────────
        this.historial = new HistorialEnvios();
    }

    /**
     * Procesa una lista de envíos activando los 4 patrones en secuencia:
     * 1. MEMENTO  - snapshot del estado inicial
     * 2. ITERATOR - se agrega a la colección
     * 3. OBSERVER - se suscriben los observadores reactivos
     * 4. MEDIATOR - se lanza el pipeline completo
     */
    public void procesarEnvios(List<Envio> envios) {
        ObservadorEnvio centroObs = new CentroDistribucionObservador();
        ObservadorEnvio notifObs  = new SistemaNotificacionObservador();

        for (Envio envio : envios) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Procesando: " + envio.getId());

            // 1. Memento - guardar estado inicial
            historial.guardarEstado(envio);

            // 2. Iterator - incorporar a la colección
            coleccion.agregar(envio);

            // 3. Observer - suscribir observadores (auditoria cubre ambos roles)
            envio.adjuntarObservador(centroObs);
            envio.adjuntarObservador(notifObs);
            envio.adjuntarObservador(auditoria);

            // 4. Mediator - lanzar el pipeline event-driven
            centro.crearEnvio(envio);

            // 5. Memento - guardar estado post-pipeline
            historial.guardarEstado(envio);
        }
    }

    /** Recorre todos los envíos procesados usando el Iterator. */
    public void mostrarEnviosProcesados() {
        System.out.println("\n=== Envíos en colección ===");
        IteradorEnvios it = coleccion.crearIterador();
        int i = 1;
        while (it.tieneSiguiente()) {
            Envio e = it.obtenerSiguiente();
            System.out.println("  " + i++ + ". " + e.getId()
                    + " [" + e.obtenerEstado() + "]");
        }
    }

    // Accesores para los tests
    public ColeccionEnvios getColeccion()  { return coleccion;  }
    public HistorialEnvios getHistorial()  { return historial;  }
    public MediadorEnvios  getMediador()   { return mediador;   }
    public SistemaAuditoria getAuditoria() { return auditoria;  }
}

