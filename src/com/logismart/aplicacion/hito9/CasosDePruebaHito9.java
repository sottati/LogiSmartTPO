package com.logismart.aplicacion.hito9;

import com.logismart.dominio.Envio;
import com.logismart.infraestructura.decorator.envio.DecoradorNotificacionesSMS;
import com.logismart.infraestructura.decorator.envio.DecoradorPrioritario;
import com.logismart.infraestructura.decorator.envio.DecoradorRastreoGPS;
import com.logismart.infraestructura.decorator.envio.DecoradorSeguro;
import com.logismart.infraestructura.flyweight.ubicacion.FabricaUbicaciones;
import com.logismart.infraestructura.flyweight.ubicacion.Ubicacion;
import com.logismart.infraestructura.proxy.envio.ProxyRepositorioEnvios;
import com.logismart.infraestructura.proxy.envio.RepositorioEnvios;
import java.util.ArrayList;
import java.util.List;

public final class CasosDePruebaHito9 {
    private static int total;
    private static int ok;

    private CasosDePruebaHito9() {
    }

    public static void ejecutar() {
        total = 0;
        ok = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  7. GOF - HITO 9");
        System.out.println("══════════════════════════════════════════════");

        probarDecorator();
        probarFacade();
        probarFlyweight();
        probarProxy();
        probarIntegracion();

        System.out.println("\n[Hito 9] Casos ejecutados: " + total + " | OK: " + ok);
        if (total != ok) {
            throw new IllegalStateException("Hay casos fallidos en Hito 9");
        }
    }

    private static void probarDecorator() {
        System.out.println("\n--- Decorator ---");
        com.logismart.infraestructura.decorator.envio.Envio envio1 =
                new com.logismart.infraestructura.decorator.envio.EnvioBasico("Buenos Aires", "Cordoba", 5.0);
        verificar(Math.abs(envio1.obtenerCosto() - 50.0) < 0.0001, "Caso 1: envio basico costo base");
        verificar(envio1.obtenerTiempoEntrega() == 3, "Caso 1: envio basico tiempo base");

        com.logismart.infraestructura.decorator.envio.Envio envio2 = new DecoradorSeguro(envio1);
        verificar(Math.abs(envio2.obtenerCosto() - 57.5) < 0.0001, "Caso 2: envio con seguro");

        com.logismart.infraestructura.decorator.envio.Envio envio3 = new DecoradorRastreoGPS(envio2);
        verificar(Math.abs(envio3.obtenerCosto() - 107.5) < 0.0001, "Caso 3: envio con seguro y rastreo");
        verificar(envio3.obtenerTiempoEntrega() == 2, "Caso 3: rastreo reduce tiempo");

        com.logismart.infraestructura.decorator.envio.Envio envio4 = new DecoradorPrioritario(
                new DecoradorNotificacionesSMS(
                        new DecoradorRastreoGPS(
                                new DecoradorSeguro(envio1))));
        verificar(Math.abs(envio4.obtenerCosto() - 232.5) < 0.0001, "Caso 4: envio con todos los servicios");
        verificar(envio4.obtenerTiempoEntrega() == 1, "Caso 4: tiempo minimo con prioritario");
        verificar(envio4.obtenerServicios().contains("Seguro")
                && envio4.obtenerServicios().contains("Rastreo GPS")
                && envio4.obtenerServicios().contains("Notificaciones SMS")
                && envio4.obtenerServicios().contains("Prioritario"), "Caso 5: servicios acumulados");
    }

    private static void probarFacade() {
        System.out.println("\n--- Facade ---");
        ServicioLogisticaFacade servicio = new ServicioLogisticaFacade();

        String numeroSeguimiento = servicio.crearEnvio("PROD-001", 150.0, "cliente@email.com", "+54911234567");
        verificar(numeroSeguimiento != null, "Caso 1: crear envio exitoso");
        verificar("CONFIRMADO".equals(servicio.obtenerEstadoEnvio(numeroSeguimiento)), "Caso 2: obtener estado confirmado");

        servicio.cancelarEnvio(numeroSeguimiento);
        verificar("CANCELADO".equals(servicio.obtenerEstadoEnvio(numeroSeguimiento)), "Caso 3: cancelar envio");

        String numeroFallido = servicio.crearEnvio("PROD-NO-EXISTE", 150.0, "cliente@email.com", "+54911234567");
        verificar(numeroFallido == null, "Caso 4: envio fallido por stock");

        String envioA = servicio.crearEnvio("PROD-001", 100.0, "a@email.com", "+549111");
        String envioB = servicio.crearEnvio("PROD-002", 200.0, "b@email.com", "+549112");
        String envioC = servicio.crearEnvio("PROD-003", 300.0, "c@email.com", "+549113");
        verificar(envioA != null && envioB != null && envioC != null, "Caso 5: multiples envios");
    }

    private static void probarFlyweight() {
        System.out.println("\n--- Flyweight ---");
        FabricaUbicaciones.limpiar();

        Ubicacion buenosAires1 = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
        Ubicacion buenosAires2 = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
        Ubicacion cordoba = FabricaUbicaciones.obtener("Cordoba", "Cordoba", "5000");
        verificar(buenosAires1 == buenosAires2, "Caso 1: misma referencia compartida");
        verificar(buenosAires1.equals(buenosAires2), "Caso 2: misma ubicacion por equals");
        verificar(!buenosAires1.equals(cordoba), "Caso 2b: ubicaciones distintas");

        for (int i = 0; i < 10000; i++) {
            FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
            FabricaUbicaciones.obtener("Cordoba", "Cordoba", "5000");
            FabricaUbicaciones.obtener("Rosario", "Santa Fe", "2000");
        }
        verificar(FabricaUbicaciones.totalUbicaciones() == 3, "Caso 3: 10000 referencias y solo 3 objetos");

        FabricaUbicaciones.mostrarEstadisticas();
        verificar(FabricaUbicaciones.totalUbicaciones() == 3, "Caso 4: estadisticas consistentes");

        List<Ubicacion> ubicacionesSin = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ubicacionesSin.add(new Ubicacion("Buenos Aires", "Buenos Aires", "1425"));
        }
        verificar(ubicacionesSin.size() == 1000 && FabricaUbicaciones.totalUbicaciones() == 3,
                "Caso 5: comparacion con y sin flyweight");
    }

    private static void probarProxy() {
        System.out.println("\n--- Proxy ---");
        ProxyRepositorioEnvios proxy = new ProxyRepositorioEnvios();
        RepositorioEnvios repo = proxy;

        verificar(!proxy.estaInicializado(), "Caso 1: proxy creado sin conectar BD");

        Envio envio1 = repo.obtenerEnvio("ENV-001");
        verificar(proxy.estaInicializado() && envio1 != null, "Caso 2: primera consulta inicializa BD");

        Envio envio1Bis = repo.obtenerEnvio("ENV-001");
        verificar(envio1 == envio1Bis && proxy.obtenerTamanoCache() == 1, "Caso 3: segunda consulta usa cache");

        List<Envio> envios = repo.obtenerEnvios();
        verificar(envios.size() == 5 && proxy.isCacheEnviosValido(), "Caso 4: obtener todos llena cache de lista");

        List<Envio> envios2 = repo.obtenerEnvios();
        verificar(envios2.size() == 5 && envios2 != envios, "Caso 5: segunda lista sale desde cache");

        Envio nuevoEnvio = new Envio.EnvioBuilder("ENV-006", "Rosario", "Mendoza")
                .peso(3.0)
                .descripcion("NUEVO")
                .build();
        repo.guardarEnvio(nuevoEnvio);
        verificar(!proxy.isCacheEnviosValido() && repo.obtenerEnvios().size() == 6, "Caso 6: guardar invalida cache y persiste");
    }

    private static void probarIntegracion() {
        System.out.println("\n--- Integracion ---");
        FabricaUbicaciones.limpiar();
        ServicioLogisticaCompleto servicio = new ServicioLogisticaCompleto();

        String numero = servicio.crearEnvioConServicios(
                "PROD-001",
                5.0,
                "cliente@email.com",
                true,
                true,
                true,
                true);
        verificar(numero != null, "Caso 1: integracion completa crea envio");
        verificar(servicio.obtenerRepositorio().obtenerEnvio(numero) != null, "Caso 2: envio integrado queda en proxy");
        verificar(servicio.obtenerCantidadUbicacionesCompartidas() == 2, "Caso 3: flyweight comparte origen y destino");

        String numero2 = servicio.crearEnvioConServicios(
                "PROD-002",
                5.0,
                "cliente2@email.com",
                false,
                true,
                false,
                false);
        verificar(numero2 != null && servicio.obtenerCantidadUbicacionesCompartidas() == 2,
                "Caso 4: segunda integracion reutiliza ubicaciones");
    }

    private static void verificar(boolean condicion, String descripcion) {
        total++;
        if (!condicion) {
            throw new IllegalStateException("Fallo: " + descripcion);
        }
        ok++;
        System.out.println("[OK] " + descripcion);
    }
}
