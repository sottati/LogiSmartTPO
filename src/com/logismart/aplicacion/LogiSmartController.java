package com.logismart.aplicacion;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.vehiculo.Flota;
import com.logismart.dominio.usuario.IPermisos;
import com.logismart.dominio.envio.Orden;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.vehiculo.Vehiculo;
import com.logismart.infraestructura.fabrica.FabricaDeEnvios;
import com.logismart.infraestructura.fabrica.FabricaDeNotificadores;
import com.logismart.infraestructura.fabrica.FabricaDeVehiculos;
import com.logismart.infraestructura.fabrica.TipoEnvio;
import com.logismart.infraestructura.fabrica.TipoNotificador;
import com.logismart.infraestructura.fabrica.TipoVehiculo;
import com.logismart.infraestructura.costo.CalculadorDeCosto;
import com.logismart.infraestructura.tiempo.CalculadorDeTiempo;
import com.logismart.infraestructura.vehiculo.AsignadorDeVehiculos;
import com.logismart.infraestructura.singleton.ConexionBD;
import com.logismart.infraestructura.singleton.Logger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GRASP Controller: Intermediario entre la UI (o API REST) y el dominio.
 * Coordina operaciones que involucran multiples objetos de dominio.
 * No contiene logica de negocio propia: delega en los objetos del dominio
 * y en los servicios de aplicacion (Pure Fabrication).
 *
 * Responsabilidades:
 *  - Recibir y validar solicitudes de la UI
 *  - Coordinar la colaboracion entre objetos del dominio
 *  - Utilizar Pure Fabrications para notificar, persistir y validar
 *  - Devolver resultados o lanzar excepciones significativas
 */
public class LogiSmartController {

    private final ConexionBD bd = ConexionBD.obtenerInstancia();
    private final Logger logger = Logger.obtenerInstancia();

    private final RepositorioDeEnvios repositorioEnvios;
    private final ServicioDeNotificaciones notificaciones;
    private final ValidadorDeEnvios validador;
    private final CalculadorDeRutas calculadorRutas;
    private final AsignadorDeVehiculos asignadorVehiculos;
    private final CalculadorDeCosto calculadorCosto;
    private final CalculadorDeTiempo calculadorTiempo;

    public LogiSmartController(
            RepositorioDeEnvios repositorioEnvios,
            ServicioDeNotificaciones notificaciones,
            ValidadorDeEnvios validador,
            CalculadorDeRutas calculadorRutas,
            AsignadorDeVehiculos asignadorVehiculos,
            CalculadorDeCosto calculadorCosto,
            CalculadorDeTiempo calculadorTiempo) {
        this.repositorioEnvios   = repositorioEnvios;
        this.notificaciones      = notificaciones;
        this.validador           = validador;
        this.calculadorRutas     = calculadorRutas;
        this.asignadorVehiculos  = asignadorVehiculos;
        this.calculadorCosto     = calculadorCosto;
        this.calculadorTiempo    = calculadorTiempo;
    }

    public LogiSmartController(
            RepositorioDeEnvios repositorioEnvios,
            TipoNotificador tipoNotificador,
            ValidadorDeEnvios validador,
            CalculadorDeRutas calculadorRutas,
            AsignadorDeVehiculos asignadorVehiculos,
            CalculadorDeCosto calculadorCosto,
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
    // CU-01: Crear envio
    // -------------------------------------------------------------------------

    /**
     * Crea un nuevo envio para la empresa solicitante y notifica al cliente.
     *
     * @param solicitante usuario que realiza la operacion (debe tener permiso)
     * @param empresa     empresa a la que pertenece el envio
     * @param cliente     cliente final que recibira el envio
     * @param prioridad   ALTA | MEDIA | BAJA
     * @param fecha       fecha programada de entrega
     * @return el envio creado y persistido
     */
    public Envio crearEnvio(IPermisos solicitante, Empresa empresa,
                            ClienteFinal cliente, String prioridad, LocalDateTime fecha) {
        return crearEnvio(solicitante, empresa, cliente, TipoEnvio.desdePrioridad(prioridad), fecha);
    }

    public Envio crearEnvio(IPermisos solicitante, Empresa empresa,
                            ClienteFinal cliente, TipoEnvio tipoEnvio, LocalDateTime fecha) {
        // Validar permisos (Polymorphism: no hay instanceof, cada tipo sabe su permiso)
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para crear envios");
        }

        // Validar datos de entrada (Pure Fabrication: ValidadorDeEnvios)
        var resultado = validador.validarCreacion(cliente.getId(), tipoEnvio.getPrioridadAsociada(), fecha);
        if (!resultado.esValido()) {
            throw new IllegalArgumentException("Datos invalidos: " + resultado.errores());
        }

        // Crear envio (Factory Method: el controller no conoce subclases)
        Envio envio = FabricaDeEnvios.crearEnvio(tipoEnvio, empresa, fecha);

        // Persistir (Pure Fabrication: RepositorioDeEnvios)
        repositorioEnvios.guardar(envio);
        bd.ejecutarQuery("INSERT INTO envios (id, prioridad) VALUES ('" + envio.getId() + "', '" + envio.getPrioridad() + "')");
        logger.log("Envio creado: " + envio.getId() + " tipo=" + tipoEnvio.name());

        // Notificar (Pure Fabrication + Indirection: ServicioDeNotificaciones -> Notificador)
        notificaciones.notificarCreacionDeEnvio(envio, cliente);

        return envio;
    }

    // -------------------------------------------------------------------------
    // CU-02: Agregar orden a envio
    // -------------------------------------------------------------------------

    /**
     * Asocia una orden a un envio existente.
     */
    public void agregarOrden(IPermisos solicitante, String idEnvio, Orden orden) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para modificar envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);
        envio.agregarOrden(orden);
        repositorioEnvios.guardar(envio);
    }

    // -------------------------------------------------------------------------
    // CU-03: Asignar ruta a envio
    // -------------------------------------------------------------------------

    /**
     * Selecciona y asigna la mejor ruta disponible al envio, y actualiza el ETA.
     *
     * @param solicitante      usuario que realiza la operacion
     * @param idEnvio          identificador del envio
     * @param rutasDisponibles candidatas a asignar
     * @param flota            flota de la empresa para seleccionar vehiculo
     * @param cliente          cliente a notificar
     * @return la ruta asignada
     */
    public Ruta asignarRuta(IPermisos solicitante, String idEnvio,
                            List<Ruta> rutasDisponibles, Flota flota, ClienteFinal cliente) {
        if (!solicitante.puedeAsignarRuta()) {
            throw new SecurityException("El usuario no tiene permiso para asignar rutas");
        }
        try {
            Envio envio = buscarEnvioOFallar(idEnvio);

            // Seleccionar vehiculo (Protected Variations: AsignadorDeVehiculos)
            Vehiculo vehiculo = asignadorVehiculos.asignar(flota, envio)
                    .orElseThrow(() -> new IllegalStateException("No hay vehiculos disponibles para el envio " + idEnvio));

            // Seleccionar ruta optima (Pure Fabrication + Polymorphism: CalculadorDeRutas)
            Ruta ruta = calculadorRutas.seleccionarRuta(envio, rutasDisponibles);

            // Validar la asignacion (Pure Fabrication: ValidadorDeEnvios)
            var resultado = validador.validarAsignacionRuta(envio, ruta, vehiculo);
            if (!resultado.esValido()) {
                throw new IllegalArgumentException("Asignacion invalida: " + resultado.errores());
            }

            ruta.asignarVehiculo(vehiculo);
            vehiculo.asignarRuta();

            // Calcular y actualizar ETA (Protected Variations: CalculadorDeTiempo)
            int minutosEstimados = calculadorTiempo.estimarMinutos(ruta);
            envio.getSeguimiento().actualizarETA(minutosEstimados, 0.85);

            // Iniciar el envio (Expert: Envio gestiona su propio estado)
            envio.iniciar();
            repositorioEnvios.guardar(envio);
            bd.ejecutarQuery("UPDATE envios SET estado='EN_CURSO' WHERE id='" + envio.getId() + "'");
            logger.log("Ruta asignada: envio=" + idEnvio + " ruta=" + ruta.getId() + " vehiculo=" + vehiculo.getId());

            // Notificar (Pure Fabrication + Indirection)
            notificaciones.notificarAsignacionDeRuta(envio, ruta, cliente);

            return ruta;
        } catch (Exception e) {
            logger.logError("asignarRuta", e);
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // CU-04: Calcular costo del envio
    // -------------------------------------------------------------------------

    /**
     * Calcula el costo estimado del envio segun la politica configurada.
     * (Protected Variations: el Controller no sabe como se calcula)
     */
    public double calcularCosto(String idEnvio) {
        Envio envio = buscarEnvioOFallar(idEnvio);
        return calculadorCosto.calcular(envio);
    }

    // -------------------------------------------------------------------------
    // CU-05: Obtener estado de un envio
    // -------------------------------------------------------------------------

    public String obtenerEstado(String idEnvio) {
        return buscarEnvioOFallar(idEnvio).getEstado();
    }

    // -------------------------------------------------------------------------
    // CU-06: Cancelar envio
    // -------------------------------------------------------------------------

    public void cancelarEnvio(IPermisos solicitante, String idEnvio, ClienteFinal cliente) {
        if (!solicitante.puedeCrearEnvio()) {
            throw new SecurityException("El usuario no tiene permiso para cancelar envios");
        }
        Envio envio = buscarEnvioOFallar(idEnvio);
        envio.cancelar();
        repositorioEnvios.guardar(envio);
        bd.ejecutarQuery("UPDATE envios SET estado='CANCELADO' WHERE id='" + envio.getId() + "'");
        logger.log("Envio cancelado: " + envio.getId());
        notificaciones.notificarCancelacion(envio, cliente);
    }

    public Vehiculo registrarVehiculoEnFlota(IPermisos solicitante, Flota flota, TipoVehiculo tipoVehiculo, String patente) {
        if (!solicitante.puedeGestionarFlota()) {
            throw new SecurityException("El usuario no tiene permiso para gestionar flota");
        }
        Vehiculo vehiculo = FabricaDeVehiculos.crearVehiculo(tipoVehiculo, patente);
        flota.agregarVehiculo(vehiculo);
        bd.ejecutarQuery("INSERT INTO vehiculos (id, tipo, patente) VALUES ('"
                + vehiculo.getId() + "', '" + vehiculo.getTipo() + "', '" + vehiculo.getPatente() + "')");
        logger.log("Vehiculo creado por factory: " + vehiculo.getId() + " tipo=" + tipoVehiculo.name());
        return vehiculo;
    }

    // -------------------------------------------------------------------------
    // CU-07: Ver reportes
    // -------------------------------------------------------------------------

    public void verificarPermisoReporte(IPermisos solicitante) {
        if (!solicitante.puedeVerReportes()) {
            throw new SecurityException("El usuario no tiene permiso para ver reportes");
        }
    }

    // -------------------------------------------------------------------------
    // helpers privados
    // -------------------------------------------------------------------------

    private Envio buscarEnvioOFallar(String idEnvio) {
        return repositorioEnvios.buscarPorId(idEnvio)
                .orElseThrow(() -> new IllegalArgumentException("Envio no encontrado: " + idEnvio));
    }

    private static ServicioDeNotificaciones crearServicioDeNotificaciones(TipoNotificador tipoNotificador) {
        return new ServicioDeNotificaciones(FabricaDeNotificadores.crearNotificador(tipoNotificador));
    }
}

