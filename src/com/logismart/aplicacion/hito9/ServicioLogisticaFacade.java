package com.logismart.aplicacion.hito9;

import java.util.HashMap;
import java.util.Map;

public class ServicioLogisticaFacade {
    private final SistemaInventario inventario;
    private final SistemaPagos pagos;
    private final SistemaNotificaciones notificaciones;
    private final SistemaRastreo rastreo;
    private final SistemaReportes reportes;

    public ServicioLogisticaFacade() {
        this.inventario = new SistemaInventario();
        this.pagos = new SistemaPagos();
        this.notificaciones = new SistemaNotificaciones();
        this.rastreo = new SistemaRastreo();
        this.reportes = new SistemaReportes();
    }

    public String crearEnvio(String productoId, double monto, String email, String telefono) {
        System.out.println("\n=== Creando Envio ===");
        try {
            if (!inventario.verificarStock(productoId)) {
                throw new IllegalStateException("Stock insuficiente");
            }
            if (!pagos.procesarPago("TARJETA", monto)) {
                throw new IllegalStateException("Pago rechazado");
            }
            inventario.restarStock(productoId, 1);
            String numeroSeguimiento = rastreo.crearNumeroSeguimiento();
            notificaciones.enviarEmail(email, "Tu envio ha sido confirmado. Numero: " + numeroSeguimiento);
            notificaciones.enviarSMS(telefono, "Envio confirmado: " + numeroSeguimiento);
            reportes.generarReporte("ENVIO", numeroSeguimiento, "PDF");
            System.out.println("Envio creado exitosamente");
            return numeroSeguimiento;
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void cancelarEnvio(String numeroSeguimiento) {
        System.out.println("\n=== Cancelando Envio ===");
        rastreo.cancelarRastreo(numeroSeguimiento);
        pagos.reembolsar(numeroSeguimiento);
        notificaciones.enviarEmail("cliente@email.com", "Envio cancelado: " + numeroSeguimiento);
        System.out.println("Envio cancelado");
    }

    public String obtenerEstadoEnvio(String numeroSeguimiento) {
        return rastreo.obtenerEstado(numeroSeguimiento);
    }
}

class SistemaInventario {
    private final Map<String, Integer> stock = new HashMap<>();

    SistemaInventario() {
        stock.put("PROD-001", 5);
        stock.put("PROD-002", 3);
        stock.put("PROD-003", 2);
    }

    boolean verificarStock(String productoId) {
        return stock.getOrDefault(productoId, 0) > 0;
    }

    void restarStock(String productoId, int cantidad) {
        int disponible = stock.getOrDefault(productoId, 0);
        if (disponible < cantidad) {
            throw new IllegalStateException("No hay stock para restar");
        }
        stock.put(productoId, disponible - cantidad);
    }
}

class SistemaPagos {
    boolean procesarPago(String metodo, double monto) {
        System.out.println("[Pagos] Procesando " + metodo + " por $" + monto);
        return monto > 0 && monto <= 50000;
    }

    void reembolsar(String numeroSeguimiento) {
        System.out.println("[Pagos] Reembolso emitido para " + numeroSeguimiento);
    }
}

class SistemaNotificaciones {
    void enviarEmail(String email, String mensaje) {
        System.out.println("[Notificaciones] Email a " + email + ": " + mensaje);
    }

    void enviarSMS(String telefono, String mensaje) {
        System.out.println("[Notificaciones] SMS a " + telefono + ": " + mensaje);
    }
}

class SistemaRastreo {
    private static long secuencia = System.currentTimeMillis();
    private final Map<String, String> estados = new HashMap<>();

    String crearNumeroSeguimiento() {
        String numero = "TRK-" + (++secuencia);
        estados.put(numero, "CONFIRMADO");
        System.out.println("[Rastreo] Numero generado: " + numero);
        return numero;
    }

    void cancelarRastreo(String numeroSeguimiento) {
        estados.put(numeroSeguimiento, "CANCELADO");
        System.out.println("[Rastreo] Seguimiento cancelado: " + numeroSeguimiento);
    }

    String obtenerEstado(String numeroSeguimiento) {
        return estados.getOrDefault(numeroSeguimiento, "DESCONOCIDO");
    }
}

class SistemaReportes {
    void generarReporte(String tipo, String referencia, String formato) {
        System.out.println("[Reportes] " + tipo + " -> " + referencia + " (" + formato + ")");
    }
}
