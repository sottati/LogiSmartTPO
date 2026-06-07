package com.logismart.aplicacion.demos.hito13;

import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.persistencia.unitofwork.UnitOfWork;

/**
 * Facade de la capa logistica de persistencia.
 * Patron: Facade (GoF) — simplifica el acceso a los 4 servicios + UnitOfWork.
 *
 * procesarEnvioCompleto: registra el envio y el cobro en el UnitOfWork,
 * hace commit (transaccion atomica simulada) y luego persiste ambos en sus
 * respectivos repositorios en memoria.
 */
public class LogisticaFacade {

    private final ServicioEnvios    servicioEnvios;
    private final ServicioClientes  servicioClientes;
    private final ServicioCentros   servicioCentros;
    private final ServicioPagos     servicioPagos;
    private final UnitOfWork        unitOfWork;

    public LogisticaFacade(ServicioEnvios servicioEnvios,
                           ServicioClientes servicioClientes,
                           ServicioCentros servicioCentros,
                           ServicioPagos servicioPagos,
                           UnitOfWork unitOfWork) {
        this.servicioEnvios   = servicioEnvios;
        this.servicioClientes = servicioClientes;
        this.servicioCentros  = servicioCentros;
        this.servicioPagos    = servicioPagos;
        this.unitOfWork       = unitOfWork;
    }

    /**
     * Procesa un envio completo con su pago asociado como una operacion atomica.
     * 1. Vincula el pago con el envio (campo envioId aditivo de Hito 13).
     * 2. Registra ambos en el UnitOfWork como nuevos.
     * 3. Hace commit (simula transaccion).
     * 4. Persiste ambos en sus repositorios en memoria.
     */
    public void procesarEnvioCompleto(Envio envio, Cobro pago) {
        System.out.println("[LogisticaFacade] Procesando envio completo: " + envio.getId());
        pago.setEnvioId(envio.getId());
        unitOfWork.registrarNuevo(envio);
        unitOfWork.registrarNuevo(pago);
        unitOfWork.commit();
        servicioEnvios.crearEnvio(envio);
        servicioPagos.crearPago(pago);
        System.out.println("[LogisticaFacade] Envio " + envio.getId() + " procesado OK.");
    }

    // Delegadores a servicios individuales

    public ServicioEnvios   getServicioEnvios()   { return servicioEnvios; }
    public ServicioClientes getServicioClientes()  { return servicioClientes; }
    public ServicioCentros  getServicioCentros()   { return servicioCentros; }
    public ServicioPagos    getServicioPagos()     { return servicioPagos; }
    public UnitOfWork       getUnitOfWork()        { return unitOfWork; }
}

