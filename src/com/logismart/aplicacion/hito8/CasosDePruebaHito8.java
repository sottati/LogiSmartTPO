package com.logismart.aplicacion.hito8;

import com.logismart.aplicacion.ServicioLogisticaUnificado;
import com.logismart.dominio.Envio;
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
import com.logismart.infraestructura.composite.centro.CentroDistribucion;
import com.logismart.infraestructura.composite.centro.CentroRegional;
import com.logismart.infraestructura.composite.centro.PuntoEntrega;

import java.util.List;

public class CasosDePruebaHito8 {

    public static void ejecutar() {
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  HITO 8 - ADAPTER, BRIDGE Y COMPOSITE");
        System.out.println("══════════════════════════════════════════════");

        probarAdapter();
        probarBridge();
        probarComposite();
        probarIntegracion();
    }

    private static void probarAdapter() {
        Envio envio = envio("ENV-H8-A", 4.0);

        AdapterDHL dhl = new AdapterDHL();
        verificar("Adapter DHL crea envio", dhl.crearEnvio(envio));
        verificar("Adapter DHL traduce costo", dhl.calcularCosto(envio) == 60.0);

        AdapterFedEx fedEx = new AdapterFedEx();
        verificar("Adapter FedEx crea envio", fedEx.crearEnvio(envio));
        verificar("Adapter FedEx normaliza estado", "DELIVERED".equals(fedEx.obtenerEstado("123")));

        AdapterUPS ups = new AdapterUPS();
        verificar("Adapter UPS crea envio", ups.crearEnvio(envio));
        verificar("Adapter UPS estima costo", ups.calcularCosto(envio) == 40.0);

        verificar("Adapter PayPal procesa pago", new AdapterPayPal().procesarPago(1500, "ENV-H8-A"));
        verificar("Adapter Stripe procesa pago en centavos", new AdapterStripe().procesarPago(1500, "ENV-H8-A"));
    }

    private static void probarBridge() {
        Reporte reporte = new ReporteEnvios(new GeneradorPDF(), List.of(envio("ENV-H8-B", 2.0)));
        verificar("Bridge genera PDF", reporte.generar().startsWith("%PDF-1.4"));
        verificar("Bridge nombre archivo PDF", "envios.pdf".equals(reporte.obtenerNombreArchivo("envios")));

        reporte.setGenerador(new GeneradorJSON());
        verificar("Bridge cambia generador en runtime", reporte.generar().startsWith("{\"reporte\""));

        reporte.setGenerador(new GeneradorCSV());
        verificar("Bridge nombre archivo CSV", "envios.csv".equals(reporte.obtenerNombreArchivo("envios")));
    }

    private static void probarComposite() {
        CentroRegional nacional = arbolCentros();
        verificar("Composite calcula capacidad recursiva", nacional.obtenerCapacidad() == 180);
        verificar("Composite calcula ocupacion recursiva", nacional.obtenerOcupacion() == 2);
        verificar("Composite calcula porcentaje", nacional.obtenerPorcentajeOcupacion() > 1.0);
    }

    private static void probarIntegracion() {
        ServicioLogisticaUnificado servicio = new ServicioLogisticaUnificado(arbolCentros());
        Envio envio = envio("ENV-H8-I", 3.0);

        verificar("Integracion crea envio con Adapter", servicio.crearEnvio("DHL", envio));
        verificar("Integracion expone Composite", servicio.obtenerCentroDistribucion().obtenerCapacidad() == 180);
        verificar("Integracion genera reporte con Bridge", servicio.generarReporte("envios", "json").generar().contains("ENV-H8-I"));
    }

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

    private static void verificar(String caso, boolean condicion) {
        if (!condicion) {
            throw new IllegalStateException("Fallo: " + caso);
        }
        System.out.println("[OK] " + caso);
    }
}
