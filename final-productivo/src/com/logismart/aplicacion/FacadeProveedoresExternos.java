package com.logismart.aplicacion;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.adapter.envio.AdapterDHL;
import com.logismart.infraestructura.adapter.envio.AdapterFedEx;
import com.logismart.infraestructura.adapter.envio.AdapterUPS;
import com.logismart.infraestructura.adapter.envio.ProveedorEnvio;
import com.logismart.infraestructura.adapter.pago.AdapterPayPal;
import com.logismart.infraestructura.adapter.pago.AdapterStripe;
import com.logismart.infraestructura.adapter.pago.ProveedorPago;
import com.logismart.infraestructura.bridge.GeneradorJSON;
import com.logismart.infraestructura.bridge.GeneradorPDF;
import com.logismart.infraestructura.bridge.GeneradorReporte;
import com.logismart.infraestructura.bridge.ReporteEnvios;
import com.logismart.infraestructura.singleton.Logger;

import java.util.List;

/**
 * Facade: simplifica el acceso a los subsistemas de providers externos.
 * Orquesta Adapters (DHL/FedEx/UPS, PayPal/Stripe) y Bridge (generación de reportes).
 * Pertenece a la capa Aplicación — el Controller es el único cliente directo.
 */
public class FacadeProveedoresExternos {

    private final Logger log = Logger.obtenerInstancia();

    private final ProveedorEnvio dhl   = new AdapterDHL();
    private final ProveedorEnvio fedex = new AdapterFedEx();
    private final ProveedorEnvio ups   = new AdapterUPS();
    private final ProveedorPago paypal = new AdapterPayPal();
    private final ProveedorPago stripe = new AdapterStripe();

    // ── Shipping ──────────────────────────────────────────────────────────────

    public String registrarEnvioExterno(Envio envio, String proveedor) {
        ProveedorEnvio p = seleccionarProveedorEnvio(proveedor);
        String tracking  = p.crearEnvio(envio);
        log.log("Facade: envio " + envio.getId() + " → " + p.obtenerNombre() + " tracking=" + tracking);
        return tracking;
    }

    public String consultarEstadoExterno(String tracking, String proveedor) {
        return seleccionarProveedorEnvio(proveedor).obtenerEstado(tracking);
    }

    public double cotizarCostoExterno(Envio envio, String proveedor) {
        ProveedorEnvio p = seleccionarProveedorEnvio(proveedor);
        double costo = p.calcularCosto(envio);
        log.log("Facade: cotizacion " + p.obtenerNombre() + " = $" + costo);
        return costo;
    }

    private ProveedorEnvio seleccionarProveedorEnvio(String proveedor) {
        if (proveedor == null) return dhl;
        switch (proveedor.toUpperCase()) {
            case "FEDEX": return fedex;
            case "UPS":   return ups;
            default:      return dhl;
        }
    }

    // ── Pago ──────────────────────────────────────────────────────────────────

    public String procesarPago(double monto, String referencia, String proveedor) {
        ProveedorPago p = "STRIPE".equalsIgnoreCase(proveedor) ? stripe : paypal;
        String txnId    = p.procesarPago(monto, referencia);
        log.log("Facade: pago " + p.obtenerNombre() + " txn=" + txnId + " $" + monto);
        return txnId;
    }

    public String consultarEstadoPago(String txnId, String proveedor) {
        return ("STRIPE".equalsIgnoreCase(proveedor) ? stripe : paypal).obtenerEstado(txnId);
    }

    public void reembolsar(String txnId, double monto, String proveedor) {
        ProveedorPago p = "STRIPE".equalsIgnoreCase(proveedor) ? stripe : paypal;
        p.reembolsar(txnId, monto);
        log.log("Facade: reembolso " + p.obtenerNombre() + " txn=" + txnId + " $" + monto);
    }

    // ── Reportes (Bridge) ─────────────────────────────────────────────────────

    public String generarReporteEnvios(List<Envio> envios, String formato) {
        GeneradorReporte gen = "PDF".equalsIgnoreCase(formato) ? new GeneradorPDF() : new GeneradorJSON();
        String resultado = new ReporteEnvios(gen, envios).generar();
        log.log("Facade: reporte " + formato + " — " + envios.size() + " envios");
        return resultado;
    }
}
