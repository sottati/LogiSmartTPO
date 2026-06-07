package com.logismart.app;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.usuario.Usuario;
import com.logismart.dominio.vehiculo.Vehiculo;
import com.logismart.infraestructura.abstractfactory.CalculadorCostos;
import com.logismart.infraestructura.abstractfactory.LogiSmartFactory;
import com.logismart.infraestructura.abstractfactory.LogiSmartFactoryArgentina;
import com.logismart.infraestructura.abstractfactory.LogiSmartFactoryBrasil;
import com.logismart.infraestructura.abstractfactory.ProveedorMapas;
import com.logismart.infraestructura.fabrica.UsuarioFactory;
import com.logismart.infraestructura.singleton.ConfiguracionSistema;
import com.logismart.infraestructura.singleton.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor regional que integra los cinco patrones creacionales del sistema.
 *
 * Patrones usados:
 *   • Singleton        - Logger, ConfiguracionSistema
 *   • Abstract Factory - LogiSmartFactory (Argentina / Brasil)
 *   • Factory Method   - UsuarioFactory
 *   • Builder          - Envio.EnvioBuilder
 *   • Prototype        - Envio.clone()
 */
public class LogiSmartApp {

    private final LogiSmartFactory factory;
    private final Logger logger;
    private final ConfiguracionSistema config;
    private final List<Envio> envios;
    private final List<Usuario> usuarios;

    public LogiSmartApp(String region) {
        this.logger   = Logger.getInstance();
        this.config   = ConfiguracionSistema.getInstance();
        config.setRegion(region);
        this.factory  = crearFactory(region);
        this.envios   = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        logger.info("LogiSmartApp iniciado | " + config);
    }

    private LogiSmartFactory crearFactory(String region) {
        if (region.equalsIgnoreCase("Argentina")) return new LogiSmartFactoryArgentina();
        if (region.equalsIgnoreCase("Brasil"))    return new LogiSmartFactoryBrasil();
        throw new IllegalArgumentException("Región desconocida: " + region);
    }

    // Builder - crea un envío individual
    public void crearEnvio(String origen, String destino) {
        Envio envio = new Envio.EnvioBuilder("ENV-" + envios.size(), origen, destino)
                .descripcion("Envío estándar")
                .build();
        envios.add(envio);
        logger.info("Builder → envío creado: " + envio.getId() + " | " + origen + " → " + destino);
    }

    // Prototype - clona un prototipo base N veces
    public void crearEnviosMultiples(int cantidad) {
        Envio prototipo = new Envio.EnvioBuilder("PROTO", "Buenos Aires", "Córdoba")
                .descripcion("Estándar")
                .peso(1.0)
                .build();

        for (int i = 0; i < cantidad; i++) {
            Envio clon = prototipo.clone();
            clon.setId("ENV-" + (1000 + i));
            envios.add(clon);
        }
        logger.info("Prototype → " + cantidad + " envíos clonados (total: " + envios.size() + ")");
    }

    // Factory Method - crea un usuario por tipo usando las clases existentes del dominio
    public void crearUsuario(String tipo, String nombre) {
        Usuario usuario = UsuarioFactory.crearUsuario(tipo, nombre);
        usuarios.add(usuario);
        usuario.saludar();
        logger.info("FactoryMethod → usuario: " + usuario.getUsername() + " [" + usuario.getRol() + "]");
    }

    // Abstract Factory - procesa un envío con los objetos de la región
    public void procesarEnvio(String origen, String destino, double peso) {
        Vehiculo vehiculo     = factory.crearVehiculo();
        CalculadorCostos calc = factory.crearCalculadorCostos();
        ProveedorMapas mapas  = factory.crearProveedorMapas();

        Envio envio = new Envio.EnvioBuilder("ENV-" + envios.size(), origen, destino)
                .peso(peso).build();
        envios.add(envio);

        vehiculo.conducir();
        double costo = calc.calcularCosto(peso);
        mapas.obtenerRuta(origen, destino);
        logger.info("AbstractFactory → envío procesado | costo=$" + String.format("%.2f", costo));
    }

    public int totalEnvios()   { return envios.size(); }
    public int totalUsuarios() { return usuarios.size(); }
}

