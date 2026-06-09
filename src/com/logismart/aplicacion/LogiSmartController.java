package com.logismart.aplicacion;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.vehiculo.Flota;
import com.logismart.dominio.usuario.IPermisos;
import com.logismart.dominio.envio.Orden;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.vehiculo.Vehiculo;
import com.logismart.infraestructura.comportamiento.memento.HistorialEnvios;
import com.logismart.infraestructura.comportamiento.observer.CentroDistribucionObservador;
import com.logismart.infraestructura.comportamiento.observer.DashboardObservador;
import com.logismart.infraestructura.comportamiento.observer.SistemaNotificacionObservador;
import com.logismart.infraestructura.comportamiento.state.EstadoCancelado;
import com.logismart.infraestructura.comportamiento.state.EstadoConfirmado;
import com.logismart.infraestructura.comportamiento.state.EstadoEnTransito;
import com.logismart.infraestructura.comportamiento.template.ProcesoEnvio;
import com.logismart.infraestructura.estructural.fabrica.FabricaDeEnvios;
import com.logismart.infraestructura.estructural.fabrica.FabricaDeNotificadores;
import com.logismart.infraestructura.estructural.fabrica.FabricaDeVehiculos;
import com.logismart.infraestructura.estructural.fabrica.TipoEnvio;
import com.logismart.infraestructura.estructural.fabrica.TipoNotificador;
import com.logismart.infraestructura.estructural.fabrica.TipoVehiculo;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaCalculoCosto;
import com.logismart.infraestructura.persistencia.unitofwork.UnitOfWork;
import com.logismart.infraestructura.estructural.tiempo.CalculadorDeTiempo;
import com.logismart.infraestructura.estructural.vehiculo.AsignadorDeVehiculos;
import com.logismart.infraestructura.estructural.singleton.Logger;
import com.logismart.aplicacion.servicios.RepositorioDeEnvios;
import com.logismart.aplicacion.servicios.ValidadorDeEnvios;
import com.logismart.aplicacion.servicios.ServicioDeNotificaciones;
import com.logismart.aplicacion.servicios.CalculadorDeRutas;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GRASP Controller: intermediario entre la UI (o API REST) y el dominio.
 * Coordina operaciones que involucran múltiples objetos de dominio.
 * No contiene lógica de negocio propia: delega en objetos del dominio
 * y en servicios de aplicación (Pure Fabrication).
 *
 * Patrones integrados:
 *   Hito  5 – Factory Method (FabricaDeEnvios, FabricaDeVehiculos)
 *   Hito 11 – Observer (DashboardObservador, SistemaNotificacionObservador, CentroDistribucionObservador)
 *   Hito 11 – Memento  (HistorialEnvios como Caretaker por envío)
 *   Hito 12 – State    (EstadoConfirmado → EstadoEnTransito → EstadoCancelado …)
 *   Hito 12 – Template Method (ProcesoEnvio en procesarConTemplate)
 *   Hito 13 – Unit of Work   (UnitOfWork agrupa INSERT/UPDATE antes del commit)
 *
 * Responsabilidades:
 *  - Recibir y validar solicitudes de la UI
 *  - Coordinar la colaboración entre objetos del dominio
 *  - Utilizar Pure Fabrications para notificar, persistir y validar
 *  - Devolver resultados o lanzar excepciones significativas
 */
public class LogiSmartController {

    private final Logger logger = Logger.obtenerInstancia();

    // ── GRASP Pure Fabrication services ──────────────────────────────────────
    private final RepositorioDeEnvios repositorioEnvios;
    private final ServicioDeNotificaciones notificaciones;
    private final ValidadorDeEnvios validador;
    private final CalculadorDeRutas calculadorRutas;
    private final AsignadorDeVehiculos asignadorVehiculos;
    private final EstrategiaCalculoCosto calculadorCosto;
    private final CalculadorDeTiempo calculadorTiempo;

    // ── GoF Behavioral (Hitos 11–13) ─────────────────────────────────────────
    private final Map<String, HistorialEnvios> historiales;  // Memento Caretaker por envío
    private final UnitOfWork unitOfWork;                     // Unit of Work

    public LogiSmartController(
            RepositorioDeEnvios repositorioEnvios,
            ServicioDeNotificaciones notificaciones,
            ValidadorDeEnvios validador,
            CalculadorDeRutas calculadorRutas,
            AsignadorDeVehiculos asignadorVehiculos,
            EstrategiaCalculoCosto calculadorCosto,
            CalculadorDeTiempo calculadorTiempo) {
        this.repositorioEnvios  = repositorioEnvios;
        this.notificaciones     = notificaciones;
        this.validador          = validador;
        this.calculadorRutas    = calculadorRutas;
        this.asignadorVehiculos = asignadorVehiculos;
        this.calculadorCosto    = calculadorCosto;
        this.calculadorTiempo   = calculadorTiempo;
        this.historiales        = new HashMap<>();
        this.unitOfWork         = new UnitOfWork();
    }

    public LogiSmartController(
            RepositorioDeEnvios repositorioEnvios,
            TipoNotificador tipoNotificador,
            ValidadorDeEnvios validador,
            CalculadorDeRutas calculadorRutas,
            AsignadorDeVehiculos asignadorVehiculos,
            EstrategiaCalculoCosto calculadorCosto,
            CalculadorDeTiempo calculadorTiempo) {
        this(
                repositorioEnvios,
                crearServicioDeNotificaciones(tipoNotificador),
                validador,
                calculadorRutas,
                asignadorVehiculos,
                calculadorCosto,
                calculadorTiempo
        );
    }

    // -------------------------------------------------------------------------
    // CU-01: Crear envío
    // -------------------------------------------------------------------------

    public Envio crearEnvio(IPermisos solicitante, Empresa empresa,
                            ClienteFinal cliente, String prioridad, LocalDateTime fecha) {
        return crearEnvio(solicitante, empresa, cliente, TipoEnvio.desdePrioridad(prioridad), fecha);
    }

    public Envio crearEnvio(IPermisos solicitante, Empresa empresa,
                            ClienteFinal cliente, TipoEnvio tipoEnvio, LocalDateTime fecha) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para crear envios");
        }

        var resultado = validador.validarCreacion(cliente.getId(), tipoEnvio.getPrioridadAsociada(), fecha);
        if (!resultado.esValido()) {
            throw new IllegalArgumentException("Datos invalidos: " + resultado.errores());
        }

        Envio envio = FabricaDeEnvios.crearEnvio(tipoEnvio, empresa, fecha);

        // Observer (Hito 11): suscribir los tres canales estándar
        envio.adjuntarObservador(new DashboardObservador());
        envio.adjuntarObservador(new SistemaNotificacionObservador());
        envio.adjuntarObservador(new CentroDistribucionObservador());

        // State (Hito 12): sincronizar estado GoF y String al inicio del ciclo de vida
        envio.cambiarEstado(new EstadoConfirmado());

        // Memento (Hito 11): snapshot inicial del envío
        historialDe(envio).guardarEstado(envio);

        // Unit of Work (Hito 13): registrar INSERT y persistir
        unitOfWork.registrarNuevo(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();

        logger.log("Envio creado: " + envio.getId() + " tipo=" + tipoEnvio.name());
        notificaciones.notificarCreacionDeEnvio(envio, cliente);

        return envio;
    }

    // -------------------------------------------------------------------------
    // CU-02: Agregar orden a envío
    // -------------------------------------------------------------------------

    public void agregarOrden(IPermisos solicitante, String idEnvio, Orden orden) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para modificar envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);

        // Memento: snapshot antes de modificar
        historialDe(envio).guardarEstado(envio);

        envio.agregarOrden(orden);

        // Unit of Work: registrar UPDATE y persistir
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();
    }

    // -------------------------------------------------------------------------
    // CU-03: Asignar ruta a envío
    // -------------------------------------------------------------------------

    public Ruta asignarRuta(IPermisos solicitante, String idEnvio,
                            List<Ruta> rutasDisponibles, Flota flota, ClienteFinal cliente) {
        if (!solicitante.puedeAsignarRuta()) {
            throw new SecurityException("El usuario no tiene permiso para asignar rutas");
        }
        try {
            Envio envio = buscarEnvioOFallar(idEnvio);

            // Memento: snapshot antes de la transición de estado
            historialDe(envio).guardarEstado(envio);

            Vehiculo vehiculo = asignadorVehiculos.asignar(flota, envio)
                    .orElseThrow(() -> new IllegalStateException("No hay vehiculos disponibles para el envio " + idEnvio));

            Ruta ruta = calculadorRutas.seleccionarRuta(envio, rutasDisponibles);

            var resultadoValidacion = validador.validarAsignacionRuta(envio, ruta, vehiculo);
            if (!resultadoValidacion.esValido()) {
                throw new IllegalArgumentException("Asignacion invalida: " + resultadoValidacion.errores());
            }

            ruta.asignarVehiculo(vehiculo);
            vehiculo.asignarRuta();

            int minutosEstimados = calculadorTiempo.estimarMinutos(ruta);
            envio.getSeguimiento().actualizarETA(minutosEstimados, 0.85);

            // State (Hito 12): transición a EN_TRANSITO al asignar ruta
            envio.cambiarEstado(new EstadoEnTransito());

            // Unit of Work
            unitOfWork.registrarModificado(envio);
            repositorioEnvios.guardar(envio);
            unitOfWork.commit();

            logger.log("Ruta asignada: envio=" + idEnvio + " ruta=" + ruta.getId() + " vehiculo=" + vehiculo.getId());
            notificaciones.notificarAsignacionDeRuta(envio, ruta, cliente);

            return ruta;
        } catch (Exception e) {
            unitOfWork.rollback();
            logger.logError("asignarRuta", e);
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // CU-04: Calcular costo del envío
    // -------------------------------------------------------------------------

    public double calcularCosto(String idEnvio) {
        Envio envio = buscarEnvioOFallar(idEnvio);
        return calculadorCosto.calcular(envio);
    }

    // -------------------------------------------------------------------------
    // CU-05: Obtener estado de un envío
    // -------------------------------------------------------------------------

    public String obtenerEstado(String idEnvio) {
        return buscarEnvioOFallar(idEnvio).getEstado();
    }

    // -------------------------------------------------------------------------
    // CU-06: Cancelar envío
    // -------------------------------------------------------------------------

    public void cancelarEnvio(IPermisos solicitante, String idEnvio, ClienteFinal cliente) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para cancelar envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);

        // Memento: punto de restauración antes de cancelar (permite deshacer)
        historialDe(envio).guardarEstado(envio);

        // State (Hito 12): transición a CANCELADO vía GoF State
        envio.cambiarEstado(new EstadoCancelado());

        // Unit of Work
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();

        logger.log("Envio cancelado: " + envio.getId());
        notificaciones.notificarCancelacion(envio, cliente);
    }

    // -------------------------------------------------------------------------
    // CU-07: Registrar vehículo en flota
    // -------------------------------------------------------------------------

    public Vehiculo registrarVehiculoEnFlota(IPermisos solicitante, Flota flota,
                                             TipoVehiculo tipoVehiculo, String patente) {
        if (!solicitante.puedeGestionarFlota()) {
            throw new SecurityException("El usuario no tiene permiso para gestionar flota");
        }
        Vehiculo vehiculo = FabricaDeVehiculos.crearVehiculo(tipoVehiculo, patente);
        flota.agregarVehiculo(vehiculo);
        logger.log("Vehiculo creado por factory: " + vehiculo.getId() + " tipo=" + tipoVehiculo.name());
        return vehiculo;
    }

    // -------------------------------------------------------------------------
    // CU-07b: Verificar permiso de reportes
    // -------------------------------------------------------------------------

    public void verificarPermisoReporte(IPermisos solicitante) {
        if (!solicitante.puedeVerReportes()) {
            throw new SecurityException("El usuario no tiene permiso para ver reportes");
        }
    }

    // -------------------------------------------------------------------------
    // Transiciones de State (Hito 12)
    // -------------------------------------------------------------------------

    /** Transiciona el envío a ENTREGADO a través del Estado GoF. */
    public void entregarEnvio(IPermisos solicitante, String idEnvio) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para registrar entregas");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).guardarEstado(envio);
        envio.entregar();  // delega a estadoGoF.entregar(envio)
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();
        logger.log("Envio entregado: " + idEnvio);
    }

    /** Transiciona el envío a RETENIDO a través del Estado GoF. */
    public void retenerEnvio(IPermisos solicitante, String idEnvio) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para retener envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).guardarEstado(envio);
        envio.retener();   // delega a estadoGoF.retener(envio)
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();
        logger.log("Envio retenido: " + idEnvio);
    }

    /** Transiciona el envío de regreso al estado anterior (devolución). */
    public void devolverEnvio(IPermisos solicitante, String idEnvio) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para devolver envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).guardarEstado(envio);
        envio.devolver();  // delega a estadoGoF.devolver(envio)
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();
        logger.log("Envio devuelto: " + idEnvio);
    }

    // -------------------------------------------------------------------------
    // Template Method (Hito 12)
    // -------------------------------------------------------------------------

    /**
     * Procesa un envío aplicando el flujo completo del Template Method elegido
     * (ProcesoNacional, ProcesoInternacional o ProcesoUrgente).
     * Retorna el costo calculado por el proceso.
     */
    public double procesarConTemplate(String idEnvio, ProcesoEnvio proceso) {
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).guardarEstado(envio);
        proceso.procesarEnvio(envio);   // validar → calcularCosto → procesarPago → notificar
        unitOfWork.registrarModificado(envio);
        repositorioEnvios.guardar(envio);
        unitOfWork.commit();
        logger.log("Template procesado para envio: " + idEnvio + " | costo=" + envio.getCosto());
        return envio.getCosto();
    }

    // -------------------------------------------------------------------------
    // Memento: deshacer / rehacer (Hito 11)
    // -------------------------------------------------------------------------

    /** Restaura el envío al estado inmediatamente anterior (undo). */
    public void deshacerEstado(String idEnvio) {
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).irAlEstadoAnterior(envio);
        repositorioEnvios.guardar(envio);
        logger.log("Memento: estado anterior restaurado para envio=" + idEnvio);
    }

    /** Avanza al estado siguiente en el historial (redo). */
    public void rehacerEstado(String idEnvio) {
        Envio envio = buscarEnvioOFallar(idEnvio);
        historialDe(envio).irAlEstadoSiguiente(envio);
        repositorioEnvios.guardar(envio);
        logger.log("Memento: estado siguiente restaurado para envio=" + idEnvio);
    }

    /** Imprime el historial de estados de un envío. */
    public void mostrarHistorial(String idEnvio) {
        buscarEnvioOFallar(idEnvio);
        historialDe(idEnvio).mostrarHistorial();
    }

    // -------------------------------------------------------------------------
    // helpers privados
    // -------------------------------------------------------------------------

    private Envio buscarEnvioOFallar(String idEnvio) {
        return repositorioEnvios.buscarPorId(idEnvio)
                .orElseThrow(() -> new IllegalArgumentException("Envio no encontrado: " + idEnvio));
    }

    private HistorialEnvios historialDe(Envio envio) {
        return historiales.computeIfAbsent(envio.getId(), id -> new HistorialEnvios());
    }

    private HistorialEnvios historialDe(String idEnvio) {
        return historiales.computeIfAbsent(idEnvio, id -> new HistorialEnvios());
    }

    private static ServicioDeNotificaciones crearServicioDeNotificaciones(TipoNotificador tipoNotificador) {
        return new ServicioDeNotificaciones(FabricaDeNotificadores.crearNotificador(tipoNotificador));
    }
}
