package com.logismart.presentacion;

import com.logismart.aplicacion.facade.FacadeProveedoresExternos;
import com.logismart.aplicacion.facade.FachadaReportes;
import com.logismart.aplicacion.PedidoEcommerce;
import com.logismart.aplicacion.ServicioImportacion;
import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.empresa.Suscripcion;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.HistorialEnvios;
import com.logismart.dominio.ruta.PuntoEntrega;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.usuario.IPermisos;
import com.logismart.dominio.vehiculo.Vehiculo;
import com.logismart.aplicacion.cadena.CadenaValidadores;
import com.logismart.aplicacion.cadena.ContextoValidacion;
import com.logismart.aplicacion.cadena.SistemaCapacidad;
import com.logismart.aplicacion.cadena.SistemaInventario;
import com.logismart.infraestructura.fabrica.CalculadorCostos;
import com.logismart.infraestructura.fabrica.LogiSmartFactory;
import com.logismart.infraestructura.fabrica.LogiSmartFactoryArgentina;
import com.logismart.infraestructura.flyweight.FabricaUbicaciones;
import com.logismart.infraestructura.flyweight.Ubicacion;
import com.logismart.infraestructura.observer.AuditoriaObservador;
import com.logismart.infraestructura.observer.DashboardObservador;
import com.logismart.infraestructura.observer.NotificadorClienteObservador;
import com.logismart.infraestructura.singleton.Logger;
import com.logismart.persistencia.EnvioMapperMemoria;
import com.logismart.persistencia.ProxyRepositorioEnvio;
import com.logismart.persistencia.RepositorioEnvioMemoria;
import com.logismart.persistencia.UnitOfWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GRASP Controller — punto de entrada único de la capa Presentación.
 * Recibe eventos del sistema, verifica permisos (IPermisos / Polymorphism)
 * y coordina los subsistemas sin contener lógica de negocio propia.
 * Implementa los 8 Casos de Uso del Hito 1.
 */
public class LogiSmartController {

    private final Logger                    log             = Logger.obtenerInstancia();
    private final FacadeProveedoresExternos facade          = new FacadeProveedoresExternos();
    private final FachadaReportes           fachadaReportes = new FachadaReportes();
    private final CadenaValidadores         cadena;
    private final ProxyRepositorioEnvio     repositorio;
    private final UnitOfWork                uow;
    private final LogiSmartFactory          factory;
    private final String                    region;
    private final ServicioImportacion       servicioImportacion;

    private final Map<String, Vehiculo>        flota      = new HashMap<>();
    private final Map<String, Ruta>            rutas      = new HashMap<>();
    private final Map<String, HistorialEnvios> historiales = new HashMap<>();

    public LogiSmartController() {
        this(new LogiSmartFactoryArgentina(), "AR");
    }

    /** Constructor configurable para multi-región (Abstract Factory + region ISO-3166). */
    public LogiSmartController(LogiSmartFactory factory, String region) {
        RepositorioEnvioMemoria base = new RepositorioEnvioMemoria();
        EnvioMapperMemoria   mapper  = new EnvioMapperMemoria();
        this.repositorio            = new ProxyRepositorioEnvio(base);
        this.uow                    = new UnitOfWork(repositorio, mapper);
        this.cadena                 = new CadenaValidadores(new SistemaInventario(), new SistemaCapacidad(10_000));
        this.factory                = factory;
        this.region                 = region;
        this.servicioImportacion    = new ServicioImportacion(cadena, uow);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-01  Importar Pedidos desde E-commerce  (Actor: Operador Logístico)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Sincroniza órdenes de TiendaNube / MercadoShops clonando un prototipo base
     * (Prototype GoF). Cada clon recibe id y destino del pedido; el resto de los
     * atributos (origen, peso, empresa, prioridad) viene del prototipo.
     */
    public List<String> importarPedidos(IPermisos actor, Envio prototipo,
                                        List<PedidoEcommerce> pedidos, Cobro cobro) {
        if (!actor.puedeCrearEnvio()) {
            log.log("CU-01 denegado: sin permiso puedeCrearEnvio");
            return new ArrayList<>();
        }
        return servicioImportacion.importarPedidos(prototipo, pedidos, cobro);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-02  Gestionar Flota y Capacidades  (Actor: Operador Logístico)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Registra un vehículo regional en la flota.
     * Usa Abstract Factory para crear el tipo correcto según la región configurada;
     * verifica capacidad y marca el vehículo como disponible.
     *
     * @return descripción del vehículo registrado, o null si sin permiso.
     */
    public String gestionarFlota(IPermisos actor, String vehiculoId) {
        if (!actor.puedeGestionarFlota()) {
            log.log("CU-02 denegado: sin permiso puedeGestionarFlota");
            return null;
        }
        Vehiculo v = factory.crearVehiculo();   // Abstract Factory
        flota.put(vehiculoId, v);
        String desc = v.getClass().getSimpleName()
                + " | cap=" + v.getCapacidadKg() + "kg"
                + " | disponible=" + v.isDisponible()
                + " | puedeCargar10kg=" + v.puedeCargar(10.0);
        log.log("CU-02: vehículo registrado — " + vehiculoId + " → " + desc);
        return desc;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-03  Planificar Rutas Óptimas  (Actor: Operador Logístico)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Genera el recorrido más eficiente para un envío dado.
     * Flujo real de patrones:
     *   – ProveedorMapas (Abstract Factory): distancia de referencia entre origen y destino.
     *   – CalculadorCostos (Abstract Factory): costo logístico regional (distancia + peso).
     *   – EstrategiaCalculoCosto (Strategy): encapsula el calculador regional en el Envío.
     *   – Ruta.optimizar(): ordena paradas y recalcula distancia total con Haversine.
     *
     * @return Ruta planificada, o null si sin permiso o envío no encontrado.
     */
    public Ruta planificarRuta(IPermisos actor, String envioId, List<PuntoEntrega> paradas) {
        if (!actor.puedeAsignarRuta()) {
            log.log("CU-03 denegado: sin permiso puedeAsignarRuta");
            return null;
        }
        return repositorio.obtener(envioId).map(envio -> {
            // Abstract Factory: proveedor de mapas regional para distancia de referencia
            double distRef = factory.crearProveedorMapas()
                    .calcularDistancia(envio.getOrigen(), envio.getDestino());

            // Abstract Factory + Strategy: calculador regional envuelto como estrategia de costo
            CalculadorCostos calc = factory.crearCalculadorCostos();
            envio.establecerEstrategia(e -> calc.calcular(distRef, e.getPeso()));
            double costoLogistico = envio.calcularCosto();

            // Ruta: construye y optimiza el recorrido usando Haversine (PosicionGPS)
            Ruta ruta = new Ruta("R-" + envioId, distRef, (int)(distRef * 2), "PLANIFICADA");
            paradas.forEach(ruta::agregarParada);
            ruta.optimizar();   // ordena por orden + recalcula con Haversine

            rutas.put(ruta.getId(), ruta);
            log.log("CU-03: ruta " + ruta.getId() + " planificada — "
                    + String.format("%.1f", ruta.getDistanciaKm()) + " km"
                    + " | costo estimado $" + String.format("%.2f", costoLogistico)
                    + " (" + calc.obtenerNombreRegion() + ")");
            return ruta;
        }).orElse(null);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-04  Asignar Hoja de Ruta a Chofer  (Actor: Operador Logístico)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Vincula la ruta planificada a un transportista y avanza el envío a EN_TRANSITO.
     * Usa Memento para guardar el estado anterior; si el commit falla, lo restaura.
     * El historial persiste entre llamadas para auditoría completa del ciclo de vida.
     */
    public boolean asignarHojaDeRuta(IPermisos actor, String envioId, String transportistaId) {
        if (!actor.puedeAsignarRuta()) {
            log.log("CU-04 denegado: sin permiso puedeAsignarRuta");
            return false;
        }
        return repositorio.obtener(envioId).map(envio -> {
            HistorialEnvios historial = obtenerHistorial(envioId);
            historial.guardarEstado(envio);                 // Memento: snapshot previo
            adjuntarObserversIfNone(envio);
            try {
                envio.validar();                            // State: CONFIRMADO → EN_TRANSITO → notifica observers
                uow.registrarModificado(envio);
                boolean ok = uow.commit();
                if (!ok) { historial.irAlEstadoAnterior(envio); }
                log.log("CU-04: envío " + envioId + " asignado a transportista " + transportistaId);
                return ok;
            } catch (IllegalStateException e) {
                historial.irAlEstadoAnterior(envio);        // Memento: rollback
                log.logError("CU-04.asignarHojaDeRuta", e);
                return false;
            }
        }).orElse(false);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-05  Iniciar Recorrido de Entrega  (Actor: Chofer / Transportista)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * El Chofer activa su ruta: el envío pasa de EN_TRANSITO a EN_REPARTO.
     * Dispara el Observer chain (dashboard + auditoría).
     */
    public boolean iniciarRecorrido(IPermisos actor, String envioId) {
        if (!actor.puedeRegistrarEntregas()) {
            log.log("CU-05 denegado: sin permiso puedeRegistrarEntregas");
            return false;
        }
        return repositorio.obtener(envioId).map(envio -> {
            HistorialEnvios historial = obtenerHistorial(envioId);
            historial.guardarEstado(envio);
            try {
                envio.entregar();                           // State: EN_TRANSITO → EN_REPARTO → notifica observers
                uow.registrarModificado(envio);
                boolean ok = uow.commit();
                if (!ok) { historial.irAlEstadoAnterior(envio); }
                log.log("CU-05: recorrido iniciado para envío " + envioId);
                return ok;
            } catch (IllegalStateException e) {
                historial.irAlEstadoAnterior(envio);
                log.logError("CU-05.iniciarRecorrido", e);
                return false;
            }
        }).orElse(false);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-06  Registrar Entrega de Pedido  (Actor: Chofer / Transportista)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * El Chofer confirma la entrega exitosa en el sistema.
     *
     * Flujo completo de patrones:
     *   – Proxy:    obtener() usa la cache TTL del ProxyRepositorioEnvio.
     *   – Memento:  snapshot del estado previo antes de la transición.
     *   – State:    envio.entregar() → EN_REPARTO → ENTREGADO.
     *   – Observer: notificarObservadores() → NotificadorCliente + Dashboard + Auditoria.
     *   – UoW:      persiste el cambio atomicamente; si falla, Memento revierte.
     */
    public boolean registrarEntrega(IPermisos actor, String envioId) {
        if (!actor.puedeRegistrarEntregas()) {
            log.log("CU-06 denegado: sin permiso puedeRegistrarEntregas");
            return false;
        }
        return repositorio.obtener(envioId).map(envio -> {  // Proxy: cache TTL
            HistorialEnvios historial = obtenerHistorial(envioId);
            historial.guardarEstado(envio);                 // Memento: guarda estado previo (EN_REPARTO)

            try {
                envio.entregar();                           // State: EN_REPARTO → ENTREGADO
                                                            // → cambiarEstado → notificarObservadores (Observer)
                uow.registrarModificado(envio);
                boolean ok = uow.commit();                  // UoW: atomicidad
                if (!ok) {
                    historial.irAlEstadoAnterior(envio);    // Memento: rollback si falla BD
                    log.log("CU-06: rollback Memento para envío " + envioId);
                }
                log.log("CU-06: entrega registrada — envío " + envioId + " → " + envio.getEstado());
                return ok;
            } catch (IllegalStateException e) {
                historial.irAlEstadoAnterior(envio);        // Memento: rollback por estado inválido
                log.logError("CU-06.registrarEntrega", e);
                return false;
            }
        }).orElse(false);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-07  Consultar Ubicación del Pedido  (Actor: Cliente Final)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * El Cliente visualiza el estado actual y la ubicación de su paquete.
     * Usa FabricaUbicaciones (Flyweight) para obtener la instancia compartida
     * de la ciudad de destino sin crear objetos duplicados.
     *
     * @return descripción con estado y ubicación, o null si sin permiso.
     */
    public String consultarUbicacion(IPermisos actor, String envioId) {
        if (!actor.puedeConsultarUbicacion()) {
            log.log("CU-07 denegado: sin permiso puedeConsultarUbicacion");
            return null;
        }
        return repositorio.obtener(envioId).map(envio -> {
            Ubicacion ubi = FabricaUbicaciones.obtener(    // Flyweight: instancia compartida
                    envio.getDestino(), this.region, "0000");
            String info = "Envio " + envioId
                    + " | estado: " + envio.getEstado()
                    + " | destino: " + ubi.getCiudad()
                    + " (" + ubi.getProvincia() + ")";
            log.log("CU-07: consulta de ubicacion — " + info);
            return info;
        }).orElse("Envio no encontrado: " + envioId);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CU-08  Administrar Suscripción / Billing  (Actor: Administrador de Plataforma)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * El AdminPlataforma gestiona el alta/baja/renovación de suscripciones de PyMEs.
     * Operaciones soportadas: ACTIVAR, PAUSAR, RENOVAR, CAMBIAR_PLAN.
     *
     * @return nuevo estado de la suscripción, o null si sin permiso.
     */
    public String administrarSuscripcion(IPermisos actor, Suscripcion suscripcion,
                                         String operacion) {
        if (!actor.puedeAdministrarEmpresas()) {
            log.log("CU-08 denegado: sin permiso puedeAdministrarEmpresas");
            return null;
        }
        switch (operacion.toUpperCase()) {
            case "ACTIVAR":      suscripcion.activar();  break;
            case "PAUSAR":       suscripcion.pausar();   break;
            case "RENOVAR":      suscripcion.renovar();  break;
            case "CAMBIAR_PLAN": suscripcion.cambiarPlan("PREMIUM", 9999.0); break;
            default:
                log.log("CU-08: operacion desconocida — " + operacion);
                return null;
        }
        log.log("CU-08: suscripcion " + suscripcion.getId()
                + " → " + operacion + " → estado=" + suscripcion.getEstado());
        return suscripcion.getEstado();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Operaciones de soporte (no son CU numerados)
    // ══════════════════════════════════════════════════════════════════════════

    /** Crea un envío individual, lo valida con la cadena y lo persiste con observers adjuntos. */
    public String crearEnvio(IPermisos actor, Envio envio, Cobro cobro) {
        if (!actor.puedeCrearEnvio()) {
            log.log("crearEnvio denegado: sin permiso"); return null;
        }
        if (!cadena.validar(new ContextoValidacion(envio, cobro))) {
            log.log("crearEnvio rechazado por cadena: " + envio.getId()); return null;
        }
        adjuntarObserversIfNone(envio);
        uow.registrarNuevo(envio);
        return uow.commit() ? envio.getId() : null;
    }

    /** Genera reporte de envíos en el formato indicado (compatibilidad hacia atrás). */
    public String generarReporteAdmin(IPermisos actor, String formato) {
        return generarReporteAdmin(actor, "ENVIOS", formato);
    }

    /** Genera reporte del tipo y formato indicados (ENVIOS | INGRESOS | DESEMPENO) × (PDF | JSON | EXCEL | CSV). */
    public String generarReporteAdmin(IPermisos actor, String tipo, String formato) {
        if (!actor.puedeVerReportes()) {
            log.log("generarReporte denegado: sin permiso"); return null;
        }
        return fachadaReportes.generarReporte(repositorio.obtenerTodos(), tipo, formato);
    }

    /**
     * Devuelve los envíos de una empresa aplicando aislamiento multi-tenant.
     * La empresa del actor se obtiene de {@link IPermisos#getEmpresaId()} — no la suministra
     * el caller, lo que garantiza el bound a nivel de tipo.
     * Solo el actor de la misma empresa (o ADMIN_PLATAFORMA) puede acceder;
     * cualquier otro intento retorna lista vacía y queda logueado.
     */
    public List<Envio> consultarEnviosPorEmpresa(IPermisos actor, String empresaIdSolicitada) {
        if (!actor.puedeVerReportes() && !actor.puedeAdministrarEmpresas()) {
            log.log("consultarEnviosPorEmpresa denegado: sin permiso");
            return new ArrayList<>();
        }
        if (!actor.puedeAdministrarEmpresas()) {
            String empresaIdActor = actor.getEmpresaId();
            if (!empresaIdSolicitada.equals(empresaIdActor)) {
                log.log("TENANT GUARD: acceso denegado — actor empresa=" + empresaIdActor
                        + " solicitó empresa=" + empresaIdSolicitada);
                return new ArrayList<>();
            }
        }
        return repositorio.buscarPorEmpresa(empresaIdSolicitada);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Helpers privados
    // ══════════════════════════════════════════════════════════════════════════

    private void adjuntarObserversIfNone(Envio envio) {
        if (envio.getObservadores().isEmpty()) {
            envio.adjuntarObservador(new NotificadorClienteObservador());
            envio.adjuntarObservador(new DashboardObservador());
            envio.adjuntarObservador(new AuditoriaObservador());
        }
    }

    private HistorialEnvios obtenerHistorial(String envioId) {
        return historiales.computeIfAbsent(envioId, id -> new HistorialEnvios());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Accessors para tests
    // ══════════════════════════════════════════════════════════════════════════

    public ProxyRepositorioEnvio       getRepositorio()              { return repositorio; }
    public Map<String, Vehiculo>       getFlota()                    { return flota; }
    public Map<String, Ruta>           getRutas()                    { return rutas; }
    public HistorialEnvios             getHistorial(String envioId)  { return historiales.get(envioId); }
}
