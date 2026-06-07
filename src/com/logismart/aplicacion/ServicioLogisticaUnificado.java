package com.logismart.aplicacion;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.adapter.envio.AdapterDHL;
import com.logismart.infraestructura.adapter.envio.AdapterFedEx;
import com.logismart.infraestructura.adapter.envio.AdapterUPS;
import com.logismart.infraestructura.adapter.envio.ProveedorEnvio;
import com.logismart.infraestructura.adapter.pago.AdapterPayPal;
import com.logismart.infraestructura.adapter.pago.AdapterStripe;
import com.logismart.infraestructura.adapter.pago.ProveedorPago;
import com.logismart.infraestructura.bridge.reporte.GeneradorCSV;
import com.logismart.infraestructura.bridge.reporte.GeneradorExcel;
import com.logismart.infraestructura.bridge.reporte.GeneradorJSON;
import com.logismart.infraestructura.bridge.reporte.GeneradorPDF;
import com.logismart.infraestructura.bridge.reporte.GeneradorReporte;
import com.logismart.infraestructura.bridge.reporte.Reporte;
import com.logismart.infraestructura.bridge.reporte.ReporteDesempenoProveedores;
import com.logismart.infraestructura.bridge.reporte.ReporteEnvios;
import com.logismart.infraestructura.bridge.reporte.ReporteIngresos;
import com.logismart.infraestructura.composite.centro.CentroDistribucion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioLogisticaUnificado {
    private final Map<String, ProveedorEnvio> proveedoresEnvio = new HashMap<>();
    private final Map<String, ProveedorPago> proveedoresPago = new HashMap<>();
    private final Map<String, Integer> desempenoProveedores = new HashMap<>();
    private final CentroDistribucion centroDistribucion;
    private final List<Envio> enviosRegistrados = new ArrayList<>();

    public ServicioLogisticaUnificado(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
        registrarProveedorEnvio(new AdapterDHL());
        registrarProveedorEnvio(new AdapterFedEx());
        registrarProveedorEnvio(new AdapterUPS());
        proveedoresPago.put("PayPal", new AdapterPayPal());
        proveedoresPago.put("Stripe", new AdapterStripe());
    }

    public boolean crearEnvio(String nombreProveedor, Envio envio) {
        ProveedorEnvio proveedor = proveedoresEnvio.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor no soportado: " + nombreProveedor);
        }
        if (proveedor.crearEnvio(envio)) {
            enviosRegistrados.add(envio);
            desempenoProveedores.merge(nombreProveedor, 1, Integer::sum);
            return true;
        }
        return false;
    }

    public boolean procesarPago(String nombreProveedor, double monto, String referencia) {
        ProveedorPago proveedor = proveedoresPago.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor de pago no soportado: " + nombreProveedor);
        }
        return proveedor.procesarPago(monto, referencia);
    }

    public Reporte generarReporte(String tipoReporte, String formato) {
        GeneradorReporte generador = obtenerGenerador(formato);
        switch (tipoReporte.toLowerCase()) {
            case "envios":
                return new ReporteEnvios(generador, enviosRegistrados);
            case "ingresos":
                return new ReporteIngresos(generador, enviosRegistrados);
            case "desempeno":
                return new ReporteDesempenoProveedores(generador, desempenoProveedores);
            default:
                throw new IllegalArgumentException("Tipo de reporte no soportado: " + tipoReporte);
        }
    }

    public CentroDistribucion obtenerCentroDistribucion() {
        return centroDistribucion;
    }

    private void registrarProveedorEnvio(ProveedorEnvio proveedor) {
        proveedoresEnvio.put(proveedor.obtenerNombre(), proveedor);
        desempenoProveedores.put(proveedor.obtenerNombre(), 0);
    }

    private GeneradorReporte obtenerGenerador(String formato) {
        switch (formato.toLowerCase()) {
            case "pdf":
                return new GeneradorPDF();
            case "excel":
                return new GeneradorExcel();
            case "json":
                return new GeneradorJSON();
            case "csv":
                return new GeneradorCSV();
            default:
                throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }
}

