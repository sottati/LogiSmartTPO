package com.logismart.aplicacion;

import com.logismart.aplicacion.demos.hito5.LogiSmartApp;
import com.logismart.aplicacion.demos.hito10.CasosDePruebaHito10;
import com.logismart.aplicacion.demos.hito11.CasosDePruebaHito11;
import com.logismart.aplicacion.demos.hito12.CasosDePruebaHito12;
import com.logismart.aplicacion.demos.hito13.CasosDePruebaHito13;
import com.logismart.aplicacion.demos.hito8.CasosDePruebaHito8;
import com.logismart.aplicacion.demos.hito9.CasosDePruebaHito9;
import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.estructural.singleton.ConfiguracionSistema;
import com.logismart.infraestructura.estructural.singleton.Logger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto de entrada único de LogiSmart.
 * Ejecuta los demos creacionales (Hito 5) y los casos de prueba de los hitos siguientes.
 */
public class Main {

    public static void main(String[] args) {

        sep("1. SINGLETON");

        Logger logger = Logger.getInstance();
        ConfiguracionSistema config = ConfiguracionSistema.getInstance();
        logger.info("Sistema iniciado: " + config);
        System.out.println("Logger es Singleton: " + (logger == Logger.getInstance()));
        System.out.println("Config es Singleton: " + (config == ConfiguracionSistema.getInstance()));

        sep("2. ABSTRACT FACTORY - LogiSmartApp Argentina");

        LogiSmartApp app = new LogiSmartApp("Argentina");

        sep("3. FACTORY METHOD - Crear usuarios");

        app.crearUsuario("cliente",  "Juan Pérez");
        app.crearUsuario("operador", "María García");
        app.crearUsuario("admin",    "Carlos López");

        sep("4. BUILDER - EnvioBuilder");

        Envio simple = new Envio.EnvioBuilder("ENV001", "Buenos Aires", "Córdoba")
                .build();
        System.out.println("Envío simple:   " + simple);

        Envio complejo = new Envio.EnvioBuilder("ENV002", "Buenos Aires", "Mendoza")
                .descripcion("Medicinas urgentes")
                .peso(2.5)
                .fragil(true)
                .requiereSignatura(true)
                .requiereRefrigeracion(true)
                .requiereAseguranza(true)
                .instruccionesEspeciales("Mantener en frío entre 2-8°C")
                .contactoEmergencia("Dr. García: 555-1234")
                .horaEntregaPreferida(LocalTime.of(14, 0))
                .build();
        System.out.println("Envío complejo: " + complejo);
        System.out.println("  fragil=" + complejo.isFragil()
                + ", refrigeración=" + complejo.isRequiereRefrigeracion()
                + ", hora=" + complejo.getHoraEntregaPreferida());

        app.crearEnvio("Buenos Aires", "Córdoba");

        sep("5. PROTOTYPE - Clonación masiva");

        Envio prototipo = new Envio.EnvioBuilder("ENV-PROTO", "Buenos Aires", "Córdoba")
                .descripcion("Medicinas")
                .peso(2.5)
                .fragil(true)
                .build();

        List<Envio> envios = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Envio clon = prototipo.clone();
            clon.setId("ENV-" + (1000 + i));
            envios.add(clon);
        }
        System.out.println("Clones creados: " + envios.size());
        System.out.println("Primer clon:    " + envios.get(0));
        System.out.println("Último clon:    " + envios.get(99));
        System.out.println("Prototipo (sin modificar): " + prototipo);

        app.crearEnviosMultiples(100);

        sep("6. ABSTRACT FACTORY - Procesar envíos regionales");

        app.procesarEnvio("Buenos Aires", "Mendoza", 5.0);

        LogiSmartApp appBR = new LogiSmartApp("Brasil");
        appBR.procesarEnvio("São Paulo", "Rio de Janeiro", 3.0);

        sep("7–13. CASOS DE PRUEBA POR HITO");

        CasosDePruebaHito8.ejecutar();
        CasosDePruebaHito9.ejecutar();
        CasosDePruebaHito10.ejecutar();
        CasosDePruebaHito11.ejecutar();
        CasosDePruebaHito12.ejecutar();
        CasosDePruebaHito13.ejecutar();

        sep("RESUMEN");
        logger.info("Demostración completada exitosamente");
        System.out.println("Total envíos AR: " + app.totalEnvios());
        System.out.println("Total usuarios AR: " + app.totalUsuarios());
    }

    private static void sep(String titulo) {
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  " + titulo);
        System.out.println("══════════════════════════════════════════════");
    }
}
