package com.logismart.aplicacion.hito9;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.decorator.envio.DecoradorNotificacionesSMS;
import com.logismart.infraestructura.decorator.envio.DecoradorPrioritario;
import com.logismart.infraestructura.decorator.envio.DecoradorRastreoGPS;
import com.logismart.infraestructura.decorator.envio.DecoradorSeguro;
import com.logismart.infraestructura.flyweight.ubicacion.FabricaUbicaciones;
import com.logismart.infraestructura.flyweight.ubicacion.Ubicacion;
import com.logismart.infraestructura.proxy.envio.ProxyRepositorioEnvios;

public class ServicioLogisticaCompleto {
    private final ServicioLogisticaFacade facade;
    private final ProxyRepositorioEnvios repositorio;

    public ServicioLogisticaCompleto() {
        this.facade = new ServicioLogisticaFacade();
        this.repositorio = new ProxyRepositorioEnvios();
    }

    public String crearEnvioConServicios(
            String productoId,
            double peso,
            String email,
            boolean conSeguro,
            boolean conRastreo,
            boolean conNotificaciones,
            boolean prioritario) {
        Ubicacion origen = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
        Ubicacion destino = FabricaUbicaciones.obtener("Cordoba", "Cordoba", "5000");

        com.logismart.infraestructura.decorator.envio.ComponenteEnvio envio =
                new com.logismart.infraestructura.decorator.envio.EnvioBasico(
                        origen.getCiudad(), destino.getCiudad(), peso);

        if (conSeguro) {
            envio = new DecoradorSeguro(envio);
        }
        if (conRastreo) {
            envio = new DecoradorRastreoGPS(envio);
        }
        if (conNotificaciones) {
            envio = new DecoradorNotificacionesSMS(envio);
        }
        if (prioritario) {
            envio = new DecoradorPrioritario(envio);
        }

        String numeroSeguimiento = facade.crearEnvio(productoId, envio.obtenerCosto(), email, "+54911234567");
        if (numeroSeguimiento != null) {
            Envio envioGuardado = new Envio.EnvioBuilder(numeroSeguimiento, origen.getCiudad(), destino.getCiudad())
                    .peso(peso)
                    .descripcion(envio.obtenerServicios())
                    .build();
            repositorio.guardarEnvio(envioGuardado);
        }
        return numeroSeguimiento;
    }

    public ProxyRepositorioEnvios obtenerRepositorio() {
        return repositorio;
    }

    public int obtenerCantidadUbicacionesCompartidas() {
        return FabricaUbicaciones.totalUbicaciones();
    }
}

