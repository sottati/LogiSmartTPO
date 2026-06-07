package com.logismart.aplicacion.demos.hito13;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.composite.centro.PuntoEntrega;
import com.logismart.infraestructura.persistencia.entidad.CentroAssembler;
import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;
import com.logismart.infraestructura.persistencia.lazy.CentroDistribucionLazyProxy;
import com.logismart.infraestructura.persistencia.lazy.ClienteLazyProxy;
import com.logismart.infraestructura.persistencia.lazy.HistorialEnviosLazyProxy;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioCentroMemoria;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioClienteMemoria;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioEnvioMemoria;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioPagoMemoria;
import com.logismart.infraestructura.persistencia.unitofwork.UnitOfWork;

import java.time.LocalDateTime;

/**
 * Demo de integracion de la capa de persistencia completa.
 * Muestra la arquitectura funcionando: Data Mapper, Repository, Unit of Work y Lazy Load.
 * Estilo similar a SistemaLogisticaAvanzada del Hito 12.
 */
public class SistemaPersistencia {

    public static void demostrar() {
        System.out.println("\n----------------------------------------------");
        System.out.println("  HITO 13 — Sistema de Persistencia");
        System.out.println("----------------------------------------------");

        // -- Repositorios en memoria -------------------------------------------
        RepositorioEnvioMemoria  repoEnvios   = new RepositorioEnvioMemoria();
        RepositorioClienteMemoria repoClientes = new RepositorioClienteMemoria();
        RepositorioCentroMemoria repoCentros  = new RepositorioCentroMemoria();
        RepositorioPagoMemoria   repoPagos    = new RepositorioPagoMemoria();

        // -- Servicios de aplicacion -------------------------------------------
        ServicioEnvios   svcEnvios   = new ServicioEnvios(repoEnvios);
        ServicioClientes svcClientes = new ServicioClientes(repoClientes);
        ServicioCentros  svcCentros  = new ServicioCentros(repoCentros);
        ServicioPagos    svcPagos    = new ServicioPagos(repoPagos);
        UnitOfWork       uow         = new UnitOfWork();

        LogisticaFacade facade = new LogisticaFacade(
                svcEnvios, svcClientes, svcCentros, svcPagos, uow);

        // -- CentroAssembler: proyeccion Composite ? persistencia --------------
        System.out.println("\n[1] CentroAssembler — proyeccion de Composite a entidad plana");
        PuntoEntrega nodo = new PuntoEntrega("Centro BA", "Buenos Aires", "CBA-01", 100);
        CentroDistribucionEntity entidadCentro = CentroAssembler.aPersistencia("C001", nodo);
        svcCentros.crearCentro(entidadCentro);
        System.out.println("  Centro persistido: " + entidadCentro);

        // -- Facade: procesarEnvioCompleto -------------------------------------
        System.out.println("\n[2] LogisticaFacade.procesarEnvioCompleto");
        Envio envio = new Envio.EnvioBuilder("ENV-H13-001", "Buenos Aires", "Cordoba")
                .peso(3.5).estado("PENDIENTE").costo(1500.0).tipo("ESTANDAR")
                .build();
        Cobro pago = new Cobro("COB-H13-001", 1500.0, "PENDIENTE",
                LocalDateTime.now(), "TARJETA");
        facade.procesarEnvioCompleto(envio, pago);

        // -- Lazy Load demos ---------------------------------------------------
        System.out.println("\n[3] Lazy Load proxies");

        ClienteFinal cliente = new ClienteFinal("CLI-001", "ana", "ana@test.com",
                "hash", "CLIENTE", "ACTIVO", "Ana Lopez", "555-1234", "Av. Corrientes 100");
        repoClientes.guardar(cliente);

        ClienteLazyProxy clienteProxy = new ClienteLazyProxy(repoClientes, "CLI-001");
        System.out.println("  Proxy creado — cargado: " + clienteProxy.estaCargado());
        ClienteFinal cargado = clienteProxy.getCliente();
        System.out.println("  Tras getCliente() — cargado: " + clienteProxy.estaCargado()
                + " | nombre: " + (cargado != null ? cargado.getNombre() : "null"));

        CentroDistribucionLazyProxy centroProxy = new CentroDistribucionLazyProxy(repoCentros, "C001");
        System.out.println("  CentroProxy creado — cargado: " + centroProxy.estaCargado());
        CentroDistribucionEntity centroLazy = centroProxy.getCentro();
        System.out.println("  Tras getCentro() — cargado: " + centroProxy.estaCargado()
                + " | nombre: " + (centroLazy != null ? centroLazy.getNombre() : "null"));

        HistorialEnviosLazyProxy historialProxy = new HistorialEnviosLazyProxy(repoEnvios, "ENV");
        System.out.println("  Historial creado — cargado: " + historialProxy.estaCargado());
        int cant = historialProxy.getHistorial().size();
        System.out.println("  Tras getHistorial() — cargado: " + historialProxy.estaCargado()
                + " | envios: " + cant);

        // -- Unit of Work: rollback demo ---------------------------------------
        System.out.println("\n[4] Unit of Work — rollback");
        uow.registrarNuevo(envio);
        uow.registrarModificado(pago);
        System.out.println("  Pendientes antes rollback: " + uow.cantidadTotal());
        uow.rollback();
        System.out.println("  Pendientes tras rollback:  " + uow.cantidadTotal());

        System.out.println("\n[SistemaPersistencia] Demostracion completada.");
    }
}

