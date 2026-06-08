package com.logismart.aplicacion.demos.hito13;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;
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
import java.util.List;

/**
 * Suite de pruebas del Hito 13 — Patrones de Acceso a Datos.
 * 42 casos distribuidos en 5 grupos:
 *   probarDataMapper      (10 casos)
 *   probarRepository      (10 casos)
 *   probarUnitOfWork       (8 casos)
 *   probarLazyLoad         (8 casos)
 *   probarArquitectura     (6 casos)
 *
 * Cada caso imprime "  ✓ descripcion" o "  ✗ descripcion [detalle]".
 * Cierra con "Hito 13: N casos ejecutados, N OK".
 */
public class CasosDePruebaHito13 {

    private static int total = 0;
    private static int ok    = 0;

    public static void ejecutar() {
        total = 0;
        ok    = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  HITO 13 — Patrones de Acceso a Datos");
        System.out.println("══════════════════════════════════════════════");

        probarDataMapper();
        probarRepository();
        probarUnitOfWork();
        probarLazyLoad();
        probarArquitectura();

        System.out.println("\nHito 13: " + total + " casos ejecutados, " + ok + " OK");
        if (ok < total) {
            System.out.println("  ADVERTENCIA: " + (total - ok) + " caso(s) fallaron.");
        }

        SistemaPersistencia.demostrar();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 1. DATA MAPPER (10 casos)
    // ─────────────────────────────────────────────────────────────────────────

    private static void probarDataMapper() {
        System.out.println("\n── Data Mapper ──────────────────────────────");

        RepositorioEnvioMemoria   repoE = new RepositorioEnvioMemoria();
        RepositorioClienteMemoria repoC = new RepositorioClienteMemoria();
        RepositorioCentroMemoria  repoX = new RepositorioCentroMemoria();
        RepositorioPagoMemoria    repoP = new RepositorioPagoMemoria();

        // 1. Insertar Envio via mapper (repositorio en memoria simula el mapper)
        Envio e1 = new Envio.EnvioBuilder("DM-ENV-001", "BA", "Cordoba")
                .peso(2.0).estado("PENDIENTE").costo(500.0).tipo("ESTANDAR").build();
        repoE.guardar(e1);
        verificar("DM-01: insertar Envio", repoE.obtener("DM-ENV-001") != null);

        // 2. Buscar Envio por id
        Envio encontrado = repoE.obtener("DM-ENV-001");
        verificar("DM-02: buscar Envio por id", "DM-ENV-001".equals(encontrado.getId()));

        // 3. Actualizar Envio (re-guardar con estado modificado)
        encontrado.cambiarEstado("EN_CURSO");
        repoE.guardar(encontrado);
        verificar("DM-03: actualizar Envio", "EN_CURSO".equals(repoE.obtener("DM-ENV-001").getEstado()));

        // 4. Eliminar Envio
        repoE.eliminar("DM-ENV-001");
        verificar("DM-04: eliminar Envio", repoE.obtener("DM-ENV-001") == null);

        // 5. Insertar ClienteFinal via mapper
        ClienteFinal c1 = new ClienteFinal("DM-CLI-001", "lucia", "lucia@test.com",
                "hash", "CLIENTE", "ACTIVO", "Lucia Gomez", "555-9999", "Calle Falsa 123");
        repoC.guardar(c1);
        verificar("DM-05: insertar ClienteFinal", repoC.obtener("DM-CLI-001") != null);

        // 6. Buscar ClienteFinal por id
        verificar("DM-06: buscar ClienteFinal por id",
                "Lucia Gomez".equals(repoC.obtener("DM-CLI-001").getNombre()));

        // 7. Insertar CentroDistribucionEntity (entidad de persistencia)
        CentroDistribucionEntity cd = new CentroDistribucionEntity("DM-CEN-001", "Centro Sur", "Rosario", "RS-01", 200, 80);
        repoX.guardar(cd);
        verificar("DM-07: insertar CentroDistribucionEntity", repoX.obtener("DM-CEN-001") != null);

        // 8. Datos del centro son correctos
        CentroDistribucionEntity cdLeido = repoX.obtener("DM-CEN-001");
        verificar("DM-08: campos de CentroDistribucionEntity", cdLeido.getCapacidad() == 200 && cdLeido.getOcupacion() == 80);

        // 9. Insertar Cobro via mapper
        Cobro cobro = new Cobro("DM-COB-001", 750.0, "PENDIENTE", LocalDateTime.now(), "DEBITO");
        cobro.setEnvioId("DM-ENV-X");
        repoP.guardar(cobro);
        verificar("DM-09: insertar Cobro con envioId", repoP.obtener("DM-COB-001") != null);

        // 10. envioId persistido correctamente
        verificar("DM-10: envioId en Cobro", "DM-ENV-X".equals(repoP.obtener("DM-COB-001").getEnvioId()));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 2. REPOSITORY (10 casos)
    // ─────────────────────────────────────────────────────────────────────────

    private static void probarRepository() {
        System.out.println("\n── Repository ───────────────────────────────");

        RepositorioEnvioMemoria   repoE = new RepositorioEnvioMemoria();
        RepositorioClienteMemoria repoC = new RepositorioClienteMemoria();
        RepositorioCentroMemoria  repoX = new RepositorioCentroMemoria();
        RepositorioPagoMemoria    repoP = new RepositorioPagoMemoria();

        // 11. guardar y obtenerTodos Envio
        Envio e1 = new Envio.EnvioBuilder("REP-ENV-001", "BA", "Mendoza").estado("PENDIENTE").build();
        Envio e2 = new Envio.EnvioBuilder("REP-ENV-002", "BA", "Rosario").estado("EN_CURSO").build();
        repoE.guardar(e1); repoE.guardar(e2);
        verificar("REP-11: obtenerTodos Envio", repoE.obtenerTodos().size() == 2);

        // 12. buscarPorEstado
        List<Envio> pendientes = repoE.buscarPorEstado("PENDIENTE");
        verificar("REP-12: buscarPorEstado=PENDIENTE", pendientes.size() == 1);

        // 13. buscarPorEstado sin resultados
        verificar("REP-13: buscarPorEstado sin resultados", repoE.buscarPorEstado("CERRADO").isEmpty());

        // 14. eliminar Envio por String id
        repoE.eliminar("REP-ENV-001");
        verificar("REP-14: eliminar Envio", repoE.obtener("REP-ENV-001") == null);

        // 15. guardar y obtener ClienteFinal
        ClienteFinal c1 = new ClienteFinal("REP-CLI-001", "pedro", "pedro@test.com",
                "hash", "CLIENTE", "ACTIVO", "Pedro Ruiz", "555-1111", "Av. 9 de Julio");
        repoC.guardar(c1);
        verificar("REP-15: guardar y obtener ClienteFinal",
                "Pedro Ruiz".equals(repoC.obtener("REP-CLI-001").getNombre()));

        // 16. buscarPorNombre
        List<ClienteFinal> lista = repoC.buscarPorNombre("Pedro Ruiz");
        verificar("REP-16: buscarPorNombre", lista.size() == 1);

        // 17. guardar CentroDistribucionEntity y obtenerTodos
        CentroDistribucionEntity cd1 = new CentroDistribucionEntity("REP-CEN-001", "C-Norte", "Tucuman", "TUC-01", 150, 30);
        CentroDistribucionEntity cd2 = new CentroDistribucionEntity("REP-CEN-002", "C-Sur", "Mendoza", "MDZ-01", 120, 60);
        repoX.guardar(cd1); repoX.guardar(cd2);
        verificar("REP-17: obtenerTodos Centros", repoX.obtenerTodos().size() == 2);

        // 18. buscarPorUbicacion
        List<CentroDistribucionEntity> enTucuman = repoX.buscarPorUbicacion("Tucuman");
        verificar("REP-18: buscarPorUbicacion", enTucuman.size() == 1);

        // 19. guardar Cobros y buscarPorEnvio
        Cobro c1p = new Cobro("REP-COB-001", 300.0, "PENDIENTE", LocalDateTime.now(), "EFECTIVO");
        Cobro c2p = new Cobro("REP-COB-002", 600.0, "PAGADO", LocalDateTime.now(), "TARJETA");
        c1p.setEnvioId("REP-ENV-002");
        c2p.setEnvioId("REP-ENV-002");
        repoP.guardar(c1p); repoP.guardar(c2p);
        List<Cobro> cobrosEnvio = repoP.buscarPorEnvio("REP-ENV-002");
        verificar("REP-19: buscarPorEnvio retorna 2 cobros", cobrosEnvio.size() == 2);

        // 20. obtenerTodos Cobros
        verificar("REP-20: obtenerTodos Cobros", repoP.obtenerTodos().size() == 2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 3. UNIT OF WORK (8 casos)
    // ─────────────────────────────────────────────────────────────────────────

    private static void probarUnitOfWork() {
        System.out.println("\n── Unit of Work ─────────────────────────────");

        UnitOfWork uow = new UnitOfWork();

        // 21. estado inicial vacío
        verificar("UOW-21: estado inicial vacio", uow.cantidadTotal() == 0);

        // 22. registrarNuevo
        Envio e = new Envio.EnvioBuilder("UOW-ENV-001", "BA", "Salta").build();
        uow.registrarNuevo(e);
        verificar("UOW-22: registrarNuevo incrementa contador", uow.cantidadNuevos() == 1);

        // 23. registrarModificado
        Cobro c = new Cobro("UOW-COB-001", 100.0, "PENDIENTE", LocalDateTime.now(), "EFECTIVO");
        uow.registrarModificado(c);
        verificar("UOW-23: registrarModificado incrementa contador", uow.cantidadModificados() == 1);

        // 24. registrarEliminado
        ClienteFinal cli = new ClienteFinal("UOW-CLI-001", "x", "x@x.com",
                "h", "CLIENTE", "ACTIVO", "X", "0", "");
        uow.registrarEliminado(cli);
        verificar("UOW-24: registrarEliminado incrementa contador", uow.cantidadEliminados() == 1);

        // 25. cantidadTotal antes de commit
        verificar("UOW-25: cantidadTotal = 3 antes de commit", uow.cantidadTotal() == 3);

        // 26. commit vacia las listas
        uow.commit();
        verificar("UOW-26: commit() vacia listas", uow.cantidadTotal() == 0);

        // 27. rollback descarta cambios
        uow.registrarNuevo(e);
        uow.registrarModificado(c);
        verificar("UOW-27: pendientes antes de rollback = 2", uow.cantidadTotal() == 2);
        uow.rollback();
        verificar("UOW-27b: rollback() vacia listas", uow.cantidadTotal() == 0);

        // 28. multi-entidad: varios tipos en un commit
        UnitOfWork uow2 = new UnitOfWork();
        CentroDistribucionEntity cd = new CentroDistribucionEntity("UOW-CEN-001", "C", "L", "C-01", 10, 5);
        uow2.registrarNuevo(e);
        uow2.registrarNuevo(c);
        uow2.registrarNuevo(cd);
        uow2.registrarModificado(cli);
        verificar("UOW-28: multi-entidad commit (4 ops)", uow2.cantidadTotal() == 4);
        uow2.commit();
        verificar("UOW-28b: listas vacias tras commit multi-entidad", uow2.cantidadTotal() == 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4. LAZY LOAD (8 casos)
    // ─────────────────────────────────────────────────────────────────────────

    private static void probarLazyLoad() {
        System.out.println("\n── Lazy Load ────────────────────────────────");

        RepositorioClienteMemoria repoC = new RepositorioClienteMemoria();
        RepositorioCentroMemoria  repoX = new RepositorioCentroMemoria();
        RepositorioEnvioMemoria   repoE = new RepositorioEnvioMemoria();

        ClienteFinal cliente = new ClienteFinal("LL-CLI-001", "marta", "marta@test.com",
                "h", "CLIENTE", "ACTIVO", "Marta Diaz", "555-7777", "Bv. Oroño 500");
        repoC.guardar(cliente);

        CentroDistribucionEntity centro = new CentroDistribucionEntity("LL-CEN-001", "Hub Este", "Posadas", "PSS-01", 80, 20);
        repoX.guardar(centro);

        Envio envio1 = new Envio.EnvioBuilder("LL-CLI-001-ENV-A", "Posadas", "Corrientes").build();
        Envio envio2 = new Envio.EnvioBuilder("LL-CLI-001-ENV-B", "Posadas", "Misiones").build();
        repoE.guardar(envio1);
        repoE.guardar(envio2);

        // 29. ClienteLazyProxy no carga en creacion
        ClienteLazyProxy clienteProxy = new ClienteLazyProxy(repoC, "LL-CLI-001");
        verificar("LL-29: proxy cliente no cargado al crear", !clienteProxy.estaCargado());

        // 30. ClienteLazyProxy carga al primer acceso
        ClienteFinal cargado = clienteProxy.getCliente();
        verificar("LL-30: proxy cliente cargado tras getCliente()", clienteProxy.estaCargado());

        // 31. ClienteLazyProxy retorna objeto correcto
        verificar("LL-31: proxy cliente retorna objeto correcto",
                cargado != null && "Marta Diaz".equals(cargado.getNombre()));

        // 32. No recarga en segundo acceso (misma referencia)
        ClienteFinal cargado2 = clienteProxy.getCliente();
        verificar("LL-32: proxy cliente no recarga (misma referencia)", cargado == cargado2);

        // 33. CentroDistribucionLazyProxy no carga en creacion
        CentroDistribucionLazyProxy centroProxy = new CentroDistribucionLazyProxy(repoX, "LL-CEN-001");
        verificar("LL-33: proxy centro no cargado al crear", !centroProxy.estaCargado());

        // 34. CentroDistribucionLazyProxy carga al primer acceso
        CentroDistribucionEntity centroLazy = centroProxy.getCentro();
        verificar("LL-34: proxy centro cargado y datos correctos",
                centroProxy.estaCargado() && centroLazy != null && centroLazy.getCapacidad() == 80);

        // 35. HistorialEnviosLazyProxy no carga en creacion
        HistorialEnviosLazyProxy histProxy = new HistorialEnviosLazyProxy(repoE, "LL-CLI-001");
        verificar("LL-35: proxy historial no cargado al crear", !histProxy.estaCargado());

        // 36. HistorialEnviosLazyProxy carga y filtra por clienteId
        List<Envio> historial = histProxy.getHistorial();
        verificar("LL-36: proxy historial cargado con envios del cliente",
                histProxy.estaCargado() && historial.size() == 2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5. ARQUITECTURA INTEGRADA (6 casos)
    // ─────────────────────────────────────────────────────────────────────────

    private static void probarArquitectura() {
        System.out.println("\n── Arquitectura integrada ───────────────────");

        RepositorioEnvioMemoria   repoE = new RepositorioEnvioMemoria();
        RepositorioClienteMemoria repoC = new RepositorioClienteMemoria();
        RepositorioCentroMemoria  repoX = new RepositorioCentroMemoria();
        RepositorioPagoMemoria    repoP = new RepositorioPagoMemoria();
        UnitOfWork uow = new UnitOfWork();

        ServicioEnvios   svcE = new ServicioEnvios(repoE);
        ServicioClientes svcC = new ServicioClientes(repoC);
        ServicioCentros  svcX = new ServicioCentros(repoX);
        ServicioPagos    svcP = new ServicioPagos(repoP);

        LogisticaFacade facade = new LogisticaFacade(svcE, svcC, svcX, svcP, uow);

        // 37. ServicioClientes crea y recupera cliente
        ClienteFinal cli = new ClienteFinal("ARQ-CLI-001", "jorge", "jorge@test.com",
                "h", "CLIENTE", "ACTIVO", "Jorge Torres", "555-3333", "San Martin 200");
        svcC.crearCliente(cli);
        verificar("ARQ-37: ServicioClientes crear/obtener", svcC.obtenerCliente("ARQ-CLI-001") != null);

        // 38. CentroAssembler proyecta Composite a entidad plana
        SucursalEntrega punto = new SucursalEntrega("Deposito Central", "Cordoba", "CBA-DC", 500);
        CentroDistribucionEntity entidad = CentroAssembler.aPersistencia("ARQ-CEN-001", punto);
        verificar("ARQ-38: CentroAssembler proyecta capacidad correcta", entidad.getCapacidad() == 500);

        // 39. ServicioCentros persiste la proyeccion
        svcX.crearCentro(entidad);
        verificar("ARQ-39: ServicioCentros persiste entidad assembler",
                svcX.obtenerCentro("ARQ-CEN-001") != null);

        // 40. LogisticaFacade.procesarEnvioCompleto persiste envio y pago
        Envio envio = new Envio.EnvioBuilder("ARQ-ENV-001", "Cordoba", "Rosario")
                .peso(5.0).costo(2000.0).tipo("EXPRESS").build();
        Cobro pago = new Cobro("ARQ-COB-001", 2000.0, "PENDIENTE", LocalDateTime.now(), "TARJETA");
        facade.procesarEnvioCompleto(envio, pago);
        verificar("ARQ-40: facade persiste envio", svcE.obtenerEnvio("ARQ-ENV-001") != null);

        // 41. facade vincula pago con envio
        Cobro pagoLeido = svcP.obtenerPago("ARQ-COB-001");
        verificar("ARQ-41: facade vincula envioId en pago",
                pagoLeido != null && "ARQ-ENV-001".equals(pagoLeido.getEnvioId()));

        // 42. buscarPagosPorEnvio via ServicioPagos
        List<Cobro> pagosEnvio = svcP.buscarPagosPorEnvio("ARQ-ENV-001");
        verificar("ARQ-42: buscarPagosPorEnvio encuentra el pago", pagosEnvio.size() == 1);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            ok++;
            System.out.println("  ✓ " + nombre);
        } else {
            System.out.println("  ✗ " + nombre + " [FALLO]");
        }
    }
}
