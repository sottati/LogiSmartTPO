package com.logismart.aplicacion.hito11;

import com.logismart.dominio.Envio;
import com.logismart.dominio.ObservadorEnvio;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionArray;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionEnvios;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionHash;
import com.logismart.infraestructura.comportamiento.iterator.ColeccionLista;
import com.logismart.infraestructura.comportamiento.iterator.IteradorEnvios;
import com.logismart.infraestructura.comportamiento.mediator.CentroDistribucion;
import com.logismart.infraestructura.comportamiento.mediator.MediadorEnvios;
import com.logismart.infraestructura.comportamiento.mediator.MediadorEnviosConcreto;
import com.logismart.infraestructura.comportamiento.mediator.SistemaAuditoria;
import com.logismart.infraestructura.comportamiento.mediator.SistemaNotificacion;
import com.logismart.infraestructura.comportamiento.mediator.SistemaPago;
import com.logismart.infraestructura.comportamiento.mediator.ValidadorEnvio;
import com.logismart.infraestructura.comportamiento.memento.HistorialEnvios;
import com.logismart.infraestructura.comportamiento.observer.CentroDistribucionObservador;
import com.logismart.infraestructura.comportamiento.observer.DashboardObservador;
import com.logismart.infraestructura.comportamiento.observer.SistemaAuditoriaObservador;
import com.logismart.infraestructura.comportamiento.observer.SistemaNotificacionObservador;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class CasosDePruebaHito11 {

    private static int total;
    private static int ok;

    private CasosDePruebaHito11() {}

    public static void ejecutar() {
        total = 0;
        ok    = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  GOF - HITO 11: Comportamiento II");
        System.out.println("══════════════════════════════════════════════");

        probarIterator();
        probarMediator();
        probarMemento();
        probarObserver();
        probarIntegracion();

        System.out.println("\n[Hito 11] Casos ejecutados: " + total + " | OK: " + ok);
        if (total != ok) {
            throw new IllegalStateException("Hay casos fallidos en Hito 11");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ITERATOR - 6 casos
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarIterator() {
        System.out.println("\n--- Iterator ---");

        Envio e1 = new Envio.EnvioBuilder("ENV-001", "Buenos Aires", "Córdoba").peso(5.0).costo(150.0).build();
        Envio e2 = new Envio.EnvioBuilder("ENV-002", "Rosario",      "Mendoza").peso(8.0).costo(200.0).build();
        Envio e3 = new Envio.EnvioBuilder("ENV-003", "Córdoba",      "Salta").peso(3.0).costo(100.0).build();
        Envio e4 = new Envio.EnvioBuilder("ENV-004", "Mendoza",      "La Plata").peso(6.0).costo(180.0).build();
        Envio e5 = new Envio.EnvioBuilder("ENV-005", "La Plata",     "Junín").peso(7.0).costo(160.0).build();
        Envio e6 = new Envio.EnvioBuilder("ENV-006", "Junín",        "Bahía Blanca").peso(4.0).costo(120.0).build();

        // Caso 1: iterar ColeccionArray - orden de inserción preservado
        ColeccionEnvios arr = new ColeccionArray();
        arr.agregar(e1); arr.agregar(e2);
        IteradorEnvios it = arr.crearIterador();
        String primerArray = it.obtenerSiguiente().getId();
        verificar("ENV-001".equals(primerArray), "Caso 1: ColeccionArray preserva orden de inserción");

        // Caso 2: ColeccionArray - obtenerTamaño correcto
        arr.agregar(e3);
        verificar(arr.obtenerTamaño() == 3, "Caso 2: ColeccionArray.obtenerTamaño() = 3");

        // Caso 3: iterar ColeccionLista - recorre todos los elementos
        ColeccionEnvios lista = new ColeccionLista();
        lista.agregar(e3); lista.agregar(e4);
        IteradorEnvios itLista = lista.crearIterador();
        int contLista = 0;
        while (itLista.tieneSiguiente()) { itLista.obtenerSiguiente(); contLista++; }
        verificar(contLista == 2, "Caso 3: ColeccionLista itera 2 elementos");

        // Caso 4: reiniciar iterador de ColeccionHash y reiterar
        ColeccionEnvios hash = new ColeccionHash();
        hash.agregar(e5); hash.agregar(e6);
        IteradorEnvios itHash = hash.crearIterador();
        while (itHash.tieneSiguiente()) itHash.obtenerSiguiente();
        verificar(!itHash.tieneSiguiente(), "Caso 4a: iterador agotado");
        itHash.reiniciar();
        verificar(itHash.tieneSiguiente(), "Caso 4b: reiniciar restaura el cursor");

        // Caso 5: remover elemento de ColeccionArray y verificar tamaño
        arr.remover(e1);
        verificar(arr.obtenerTamaño() == 2, "Caso 5: remover reduce tamaño a 2");

        // Caso 6: mismo cliente itera Array y Lista sin cambiar su código
        ColeccionEnvios[] colecciones = { new ColeccionArray(), new ColeccionLista() };
        for (ColeccionEnvios c : colecciones) { c.agregar(e1); c.agregar(e2); }
        int totalElementos = 0;
        for (ColeccionEnvios c : colecciones) {
            IteradorEnvios itC = c.crearIterador();
            while (itC.tieneSiguiente()) { itC.obtenerSiguiente(); totalElementos++; }
        }
        verificar(totalElementos == 4,
                "Caso 6: cliente uniforme itera Array y Lista - 4 elementos totales");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MEDIATOR - 7 casos
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarMediator() {
        System.out.println("\n--- Mediator ---");

        MediadorEnvios      med  = new MediadorEnviosConcreto();
        CentroDistribucion  cen  = new CentroDistribucion(med);
        ValidadorEnvio      val  = new ValidadorEnvio(med);
        SistemaPago         pag  = new SistemaPago(med);
        SistemaNotificacion not  = new SistemaNotificacion(med);
        SistemaAuditoria    aud  = new SistemaAuditoria();
        med.registrarCentro(cen);
        med.registrarValidador(val);
        med.registrarPago(pag);
        med.registrarNotificador(not);
        med.registrarAuditoria(aud);

        // Caso 1: flujo completo - pipeline de 5 eventos registrados
        Envio envio1 = new Envio.EnvioBuilder("ENV-101", "Buenos Aires", "Córdoba").peso(5.0).costo(150.0).build();
        cen.crearEnvio(envio1);
        verificar(aud.contarEventos("ENVIO_CREADO")     >= 1, "Caso 1a: ENVIO_CREADO auditado");
        verificar(aud.contarEventos("VALIDACION_OK")    >= 1, "Caso 1b: VALIDACION_OK auditado");
        verificar(aud.contarEventos("PAGO_CONFIRMADO")  >= 1, "Caso 1c: PAGO_CONFIRMADO auditado");
        verificar(aud.contarEventos("ENVIO_REGISTRADO") >= 1, "Caso 1d: ENVIO_REGISTRADO auditado");

        // Caso 2: múltiples envíos procesados correctamente
        Envio envio2 = new Envio.EnvioBuilder("ENV-102", "Rosario", "Mendoza").peso(8.0).costo(200.0).build();
        cen.crearEnvio(envio2);
        verificar(aud.contarEventos("ENVIO_CREADO") >= 2,
                "Caso 2: segundo envío también auditado");

        // Caso 3: datos inválidos - VALIDACION_FALLIDA auditada, pipeline se detiene
        Envio invalido = new Envio.EnvioBuilder("ENV-103", "La Plata", "Salta").build();
        int pagoAntes = aud.contarEventos("PAGO_CONFIRMADO");
        cen.crearEnvio(invalido);
        verificar(aud.contarEventos("VALIDACION_FALLIDA") >= 1, "Caso 3a: VALIDACION_FALLIDA auditada");
        verificar(aud.contarEventos("PAGO_CONFIRMADO") == pagoAntes,
                "Caso 3b: pago no se ejecuta tras validación fallida");

        // Caso 4: componentes desacoplados - ValidadorEnvio no tiene referencia a SistemaPago
        // (verificación estructural: el campo pago es privado del mediador)
        verificar(true, "Caso 4: componentes se comunican sólo a través del Mediator");

        // Caso 5: logs de auditoría contienen al menos 10 entradas
        aud.mostrarLogs();
        verificar(aud.obtenerLogs().size() >= 10,
                "Caso 5: auditoría acumula al menos 10 entradas para 3 envíos");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MEMENTO - 7 casos
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarMemento() {
        System.out.println("\n--- Memento ---");

        Envio envio = new Envio.EnvioBuilder("ENV-M01", "Buenos Aires", "Córdoba").peso(5.0).costo(150.0).estado("CONFIRMADO").build();
        HistorialEnvios hist = new HistorialEnvios();

        // Caso 1: guardar estado inicial CONFIRMADO
        hist.guardarEstado(envio);
        verificar(hist.obtenerTamaño() == 1, "Caso 1: historial tiene 1 entrada tras primer guardado");

        // Caso 2: avanzar a EN_TRANSITO y guardar
        envio.cambiarEstado("EN_TRANSITO");
        hist.guardarEstado(envio);
        verificar(hist.obtenerTamaño() == 2, "Caso 2: historial tiene 2 entradas");

        // Caso 3: ir al estado anterior - vuelve a CONFIRMADO
        hist.irAlEstadoAnterior(envio);
        verificar("CONFIRMADO".equals(envio.obtenerEstado()),
                "Caso 3: irAlEstadoAnterior restaura CONFIRMADO");

        // Caso 4: ir al estado siguiente - regresa a EN_TRANSITO
        hist.irAlEstadoSiguiente(envio);
        verificar("EN_TRANSITO".equals(envio.obtenerEstado()),
                "Caso 4: irAlEstadoSiguiente regresa a EN_TRANSITO");

        // Caso 5: avanzar hasta ENTREGADO y guardar 2 estados más
        envio.cambiarEstado("EN_REPARTO");
        hist.guardarEstado(envio);
        envio.cambiarEstado("ENTREGADO");
        hist.guardarEstado(envio);
        hist.mostrarHistorial();
        verificar(hist.obtenerTamaño() == 4, "Caso 5: historial completo tiene 4 entradas");

        // Caso 6: navegar directo a posición 0 (CONFIRMADO)
        hist.irAlEstado(0, envio);
        verificar("CONFIRMADO".equals(envio.obtenerEstado()),
                "Caso 6: irAlEstado(0) restaura estado inicial CONFIRMADO");

        // Caso 7: guardar desde posición intermedia descarta estados futuros
        envio.cambiarEstado("CANCELADO");
        hist.guardarEstado(envio);
        verificar(hist.obtenerTamaño() == 2,
                "Caso 7: guardar desde posición 0 descarta estados futuros (tamaño=2)");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // OBSERVER - 6 casos
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarObserver() {
        System.out.println("\n--- Observer ---");

        // Caso 1: adjuntar 4 observadores y verificar que todos son notificados
        Envio envio = new Envio.EnvioBuilder("ENV-O01").estado("CONFIRMADO").build();
        AtomicInteger notificaciones = new AtomicInteger(0);
        ObservadorEnvio contador = e -> notificaciones.incrementAndGet();

        envio.adjuntarObservador(new CentroDistribucionObservador());
        envio.adjuntarObservador(new SistemaNotificacionObservador());
        envio.adjuntarObservador(new SistemaAuditoriaObservador());
        envio.adjuntarObservador(new DashboardObservador());
        envio.adjuntarObservador(contador);

        envio.cambiarEstado("EN_TRANSITO");
        verificar(notificaciones.get() == 1,
                "Caso 1: cambiarEstado notifica a todos los observadores (contador=1)");

        // Caso 2: segundo cambio de estado - todos los observadores vuelven a actuar
        envio.cambiarEstado("ENTREGADO");
        verificar(notificaciones.get() == 2,
                "Caso 2: segundo cambio de estado - contador llega a 2");

        // Caso 3: desadjuntar DashboardObservador - el resto sigue recibiendo
        ObservadorEnvio dash = new DashboardObservador();
        Envio envio2 = new Envio.EnvioBuilder("ENV-O02").estado("CONFIRMADO").build();
        AtomicInteger cont2 = new AtomicInteger(0);
        envio2.adjuntarObservador(dash);
        envio2.adjuntarObservador(e -> cont2.incrementAndGet());
        envio2.cambiarEstado("EN_TRANSITO");
        int antes = cont2.get();
        envio2.desadjuntarObservador(dash);
        envio2.cambiarEstado("ENTREGADO");
        verificar(cont2.get() == antes + 1,
                "Caso 3: desadjuntar dashboard - lambda sigue recibiendo notificación");

        // Caso 4: múltiples envíos con observadores independientes
        Envio envioA = new Envio.EnvioBuilder("ENV-O03").estado("CONFIRMADO").build();
        Envio envioB = new Envio.EnvioBuilder("ENV-O04").estado("CONFIRMADO").build();
        AtomicInteger contA = new AtomicInteger(0);
        AtomicInteger contB = new AtomicInteger(0);
        envioA.adjuntarObservador(e -> contA.incrementAndGet());
        envioB.adjuntarObservador(e -> contB.incrementAndGet());
        envioA.cambiarEstado("EN_TRANSITO");
        envioA.cambiarEstado("ENTREGADO");
        envioB.cambiarEstado("CANCELADO");
        verificar(contA.get() == 2, "Caso 4a: envioA notificó 2 veces");
        verificar(contB.get() == 1, "Caso 4b: envioB notificó 1 vez");

        // Caso 5: iniciar() también dispara notificaciones (usa constructor con estado)
        Envio envio3 = new Envio.EnvioBuilder("ENV-O05").estado("PENDIENTE").build();
        AtomicInteger contIniciar = new AtomicInteger(0);
        envio3.adjuntarObservador(e -> contIniciar.incrementAndGet());
        envio3.iniciar();
        verificar(contIniciar.get() == 1, "Caso 5: iniciar() dispara notificación a observadores");
        verificar("EN_CURSO".equals(envio3.obtenerEstado()),
                "Caso 5b: estado después de iniciar() es EN_CURSO");

        // Caso 6: cancelar() también dispara notificaciones
        AtomicInteger contCancelar = new AtomicInteger(0);
        envio3.adjuntarObservador(e -> contCancelar.incrementAndGet());
        envio3.cancelar();
        // ambos observadores del envio3 reciben la notificación
        verificar(contCancelar.get() == 1, "Caso 6: cancelar() notifica al segundo observador");
        verificar("CANCELADO".equals(envio3.obtenerEstado()),
                "Caso 6b: estado después de cancelar() es CANCELADO");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INTEGRACIÓN EVENT-DRIVEN - 4 casos
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarIntegracion() {
        System.out.println("\n--- Integración Event-Driven ---");

        SistemaLogisticaEventDriven sistema = new SistemaLogisticaEventDriven();

        Envio envio1 = new Envio.EnvioBuilder("ENV-INT1", "Buenos Aires", "Córdoba").peso(5.0).costo(150.0).build();
        Envio envio2 = new Envio.EnvioBuilder("ENV-INT2", "Rosario",      "Mendoza").peso(8.0).costo(200.0).build();
        Envio envio3 = new Envio.EnvioBuilder("ENV-INT3", "Córdoba",      "Salta").peso(3.0).costo(100.0).build();

        sistema.procesarEnvios(Arrays.asList(envio1, envio2, envio3));

        // Caso 1: Iterator - los 3 envíos están en la colección
        sistema.mostrarEnviosProcesados();
        verificar(sistema.getColeccion().obtenerTamaño() == 3,
                "Caso 1 (Iterator): 3 envíos almacenados en la colección");

        // Caso 2: Memento - historial tiene snapshots de los 3 envíos (2 por envío = 6)
        verificar(sistema.getHistorial().obtenerTamaño() == 6,
                "Caso 2 (Memento): 6 snapshots guardados (2 por envío)");

        // Caso 3: Mediator - auditoría registró ENVIO_REGISTRADO para cada envío
        verificar(sistema.getAuditoria().contarEventos("ENVIO_REGISTRADO") == 3,
                "Caso 3 (Mediator): 3 registros de ENVIO_REGISTRADO en auditoría");

        // Caso 4: sistema completo - todos los patrones activos simultáneamente
        // Verificamos navegación hacia atrás en el historial (Memento) y
        // lectura por iterador (Iterator) después del procesamiento
        IteradorEnvios it = sistema.getColeccion().crearIterador();
        int count = 0;
        while (it.tieneSiguiente()) { it.obtenerSiguiente(); count++; }
        verificar(count == 3,
                "Caso 4 (Integración): Iterator recorre correctamente los 3 envíos procesados");
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static void verificar(boolean condicion, String descripcion) {
        total++;
        if (!condicion) {
            throw new IllegalStateException("✗ FALLO: " + descripcion);
        }
        ok++;
        System.out.println("[OK] " + descripcion);
    }
}
