package com.logismart.aplicacion.hito8;

import com.logismart.aplicacion.ServicioLogisticaUnificado;
import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.adapter.envio.AdapterDHL;
import com.logismart.infraestructura.adapter.envio.AdapterFedEx;
import com.logismart.infraestructura.adapter.envio.AdapterUPS;
import com.logismart.infraestructura.adapter.pago.AdapterPayPal;
import com.logismart.infraestructura.adapter.pago.AdapterStripe;
import com.logismart.infraestructura.bridge.reporte.GeneradorCSV;
import com.logismart.infraestructura.bridge.reporte.GeneradorJSON;
import com.logismart.infraestructura.bridge.reporte.GeneradorPDF;
import com.logismart.infraestructura.bridge.reporte.Reporte;
import com.logismart.infraestructura.bridge.reporte.ReporteEnvios;
import com.logismart.infraestructura.composite.centro.CentroRegional;
import com.logismart.infraestructura.composite.centro.PuntoEntrega;

import java.util.List;

public final class CasosDePruebaHito8 {

    private static int total;
    private static int ok;

    private CasosDePruebaHito8() {}

    public static void ejecutar() {
        total = 0;
        ok = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  6. GOF - HITO 8");
        System.out.println("══════════════════════════════════════════════");

        probarAdapter();
        probarBridge();
        probarComposite();
        probarIntegracion();

        System.out.println("\n[Hito 8] Casos ejecutados: " + total + " | OK: " + ok);
        if (total != ok) {
            throw new IllegalStateException("Hay casos fallidos en Hito 8");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADAPTER (5 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarAdapter() {
        System.out.println("\n--- Adapter ---");

        Envio envio = envio("ENV-H8-A", 4.0);

        AdapterDHL dhl = new AdapterDHL();
        verificar(dhl.crearEnvio(envio), "Caso 1: Adapter DHL crea envio");
        verificar(dhl.calcularCosto(envio) == 60.0, "Caso 2: Adapter DHL traduce costo");

        AdapterFedEx fedEx = new AdapterFedEx();
        verificar(fedEx.crearEnvio(envio), "Caso 3: Adapter FedEx crea envio");
        verificar("DELIVERED".equals(fedEx.obtenerEstado("123")), "Caso 4: Adapter FedEx normaliza estado");

        AdapterUPS ups = new AdapterUPS();
        verificar(ups.crearEnvio(envio), "Caso 5: Adapter UPS crea envio");
        verificar(ups.calcularCosto(envio) == 40.0, "Caso 6: Adapter UPS estima costo");

        verificar(new AdapterPayPal().procesarPago(1500, "ENV-H8-A"), "Caso 7: Adapter PayPal procesa pago");
        verificar(new AdapterStripe().procesarPago(1500, "ENV-H8-A"), "Caso 8: Adapter Stripe procesa pago en centavos");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BRIDGE (3 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarBridge() {
        System.out.println("\n--- Bridge ---");

        Reporte reporte = new ReporteEnvios(new GeneradorPDF(), List.of(envio("ENV-H8-B", 2.0)));
        verificar(reporte.generar().startsWith("%PDF-1.4"), "Caso 1: Bridge genera PDF");
        verificar("envios.pdf".equals(reporte.obtenerNombreArchivo("envios")), "Caso 2: Bridge nombre archivo PDF");

        reporte.setGenerador(new GeneradorJSON());
        verificar(reporte.generar().startsWith("{\"reporte\""), "Caso 3: Bridge cambia generador en runtime");

        reporte.setGenerador(new GeneradorCSV());
        verificar("envios.csv".equals(reporte.obtenerNombreArchivo("envios")), "Caso 4: Bridge nombre archivo CSV");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // COMPOSITE (3 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarComposite() {
        System.out.println("\n--- Composite ---");

        CentroRegional nacional = arbolCentros();
        verificar(nacional.obtenerCapacidad() == 180, "Caso 1: Composite calcula capacidad recursiva");
        verificar(nacional.obtenerOcupacion() == 2, "Caso 2: Composite calcula ocupacion recursiva");
        verificar(nacional.obtenerPorcentajeOcupacion() > 1.0, "Caso 3: Composite calcula porcentaje");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INTEGRACIÓN (3 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarIntegracion() {
        System.out.println("\n--- Integracion ---");

        ServicioLogisticaUnificado servicio = new ServicioLogisticaUnificado(arbolCentros());
        Envio envio = envio("ENV-H8-I", 3.0);

        verificar(servicio.crearEnvio("DHL", envio), "Caso 1: Integracion crea envio con Adapter");
        verificar(servicio.obtenerCentroDistribucion().obtenerCapacidad() == 180, "Caso 2: Integracion expone Composite");
        verificar(servicio.generarReporte("envios", "json").generar().contains("ENV-H8-I"), "Caso 3: Integracion genera reporte con Bridge");
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static CentroRegional arbolCentros() {
        CentroRegional nacional = new CentroRegional("Centro Argentina", "Buenos Aires", "ARG-001");
        CentroRegional regional = new CentroRegional("Centro CABA", "CABA", "CABA-001");
        CentroRegional local = new CentroRegional("Centro San Telmo", "San Telmo", "ST-001");
        PuntoEntrega punto1 = new PuntoEntrega("Punto San Telmo 1", "San Telmo", "ST-P1", 100);
        PuntoEntrega punto2 = new PuntoEntrega("Punto San Telmo 2", "San Telmo", "ST-P2", 80);

        punto1.agregarEnvio(envio("ENV-H8-C1", 1.0));
        punto2.agregarEnvio(envio("ENV-H8-C2", 1.0));
        local.agregar(punto1);
        local.agregar(punto2);
        regional.agregar(local);
        nacional.agregar(regional);
        return nacional;
    }

    private static Envio envio(String id, double peso) {
        return new Envio.EnvioBuilder(id, "Buenos Aires", "Cordoba")
                .descripcion("Prueba Hito 8")
                .peso(peso)
                .build();
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

