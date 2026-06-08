package com.logismart.infraestructura.comportamiento.command;

import com.logismart.dominio.envio.Envio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ServicioEnvios {
    private final Map<String, String> estados = new HashMap<>();
    private final Map<String, String> metodosPago = new HashMap<>();
    private final Map<String, Set<String>> servicios = new HashMap<>();
    private final AtomicInteger contador = new AtomicInteger(1);

    public String crearEnvio(Envio envio) {
        String numero = "ENV-" + String.format("%03d", contador.getAndIncrement());
        estados.put(numero, "CONFIRMADO");
        metodosPago.put(numero, "");
        servicios.put(numero, new HashSet<>());
        System.out.println("[ServicioEnvios] Envío creado: " + numero
                + " de " + envio.getOrigen() + " a " + envio.getDestino());
        return numero;
    }

    public void cancelarEnvio(String numero) {
        estados.put(numero, "CANCELADO");
        System.out.println("[ServicioEnvios] Envío cancelado: " + numero);
    }

    public void reactivarEnvio(String numero) {
        estados.put(numero, "CONFIRMADO");
        System.out.println("[ServicioEnvios] Envío reactivado: " + numero);
    }

    public String obtenerEstado(String numero) {
        return estados.getOrDefault(numero, "DESCONOCIDO");
    }

    public void actualizarEstado(String numero, String estado) {
        estados.put(numero, estado);
        System.out.println("[ServicioEnvios] Estado " + numero + " → " + estado);
    }

    public String obtenerMetodoPago(String numero) {
        return metodosPago.getOrDefault(numero, "DESCONOCIDO");
    }

    public void cambiarMetodoPago(String numero, String metodo) {
        metodosPago.put(numero, metodo);
        System.out.println("[ServicioEnvios] Método de pago " + numero + " → " + metodo);
    }

    public void agregarServicio(String numero, String nombreServicio) {
        servicios.computeIfAbsent(numero, k -> new HashSet<>()).add(nombreServicio);
        System.out.println("[ServicioEnvios] Servicio '" + nombreServicio + "' agregado a " + numero);
    }

    public void removerServicio(String numero, String nombreServicio) {
        if (servicios.containsKey(numero)) {
            servicios.get(numero).remove(nombreServicio);
        }
        System.out.println("[ServicioEnvios] Servicio '" + nombreServicio + "' removido de " + numero);
    }

    public Set<String> obtenerServicios(String numero) {
        return servicios.getOrDefault(numero, new HashSet<>());
    }
}

