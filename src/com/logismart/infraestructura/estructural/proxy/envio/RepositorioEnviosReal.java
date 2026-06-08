package com.logismart.infraestructura.estructural.proxy.envio;

import com.logismart.dominio.envio.Envio;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEnviosReal implements RepositorioEnvios {
    private final List<Envio> envios = new ArrayList<>();

    public RepositorioEnviosReal() {
        System.out.println("[BD Real] Conectando a servidor remoto...");
        dormir(300);
        System.out.println("[BD Real] Conexion establecida");
        inicializarDatos();
    }

    private void inicializarDatos() {
        for (int i = 1; i <= 5; i++) {
            Envio envio = new Envio.EnvioBuilder("ENV-00" + i, "Buenos Aires", "Cordoba")
                    .peso(5.0 * i)
                    .descripcion("Envio inicial " + i)
                    .build();
            envio.iniciar();
            envios.add(envio);
        }
    }

    @Override
    public Envio obtenerEnvio(String id) {
        System.out.println("[BD Real] Consultando envio: " + id);
        dormir(120);
        for (Envio envio : envios) {
            if (envio.getId().equals(id)) {
                return envio;
            }
        }
        return null;
    }

    @Override
    public List<Envio> obtenerEnvios() {
        System.out.println("[BD Real] Consultando todos los envios");
        dormir(180);
        return new ArrayList<>(envios);
    }

    @Override
    public void guardarEnvio(Envio envio) {
        System.out.println("[BD Real] Guardando envio: " + envio.getId());
        dormir(120);
        envios.add(envio);
    }

    @Override
    public void eliminarEnvio(String id) {
        System.out.println("[BD Real] Eliminando envio: " + id);
        dormir(120);
        envios.removeIf(envio -> envio.getId().equals(id));
    }

    private void dormir(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

