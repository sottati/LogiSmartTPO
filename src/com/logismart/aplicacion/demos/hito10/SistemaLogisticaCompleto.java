package com.logismart.aplicacion.demos.hito10;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.comportamiento.chain.CadenaValidadores;
import com.logismart.infraestructura.comportamiento.chain.ContextoValidacion;
import com.logismart.infraestructura.comportamiento.chain.SistemaCapacidad;
import com.logismart.infraestructura.comportamiento.chain.SistemaInventario;
import com.logismart.infraestructura.comportamiento.command.ColaComandos;
import com.logismart.infraestructura.comportamiento.command.ComandoCrearEnvio;
import com.logismart.infraestructura.comportamiento.command.ServicioEnvios;
import com.logismart.infraestructura.comportamiento.interpreter.Expresion;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionAND;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionCosto;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionDestino;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionNOT;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionPeso;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionRestringido;

import java.util.HashMap;
import java.util.Map;

/**
 * Integra los tres patrones de comportamiento:
 * 1. Chain of Responsibility - valida el envío antes de procesarlo.
 * 2. Command - encapsula la creación en un comando reversible con historial.
 * 3. Interpreter - evalúa reglas de negocio sobre el envío.
 */
public class SistemaLogisticaCompleto {

    private final CadenaValidadores validadores;
    private final ColaComandos cola;
    private final ServicioEnvios servicio;
    private final Map<String, Expresion> reglas;

    public SistemaLogisticaCompleto() {
        SistemaInventario inventario = new SistemaInventario();
        SistemaCapacidad capacidad = new SistemaCapacidad(1000.0);
        this.validadores = new CadenaValidadores(inventario, capacidad);
        this.servicio = new ServicioEnvios();
        this.cola = new ColaComandos();
        this.reglas = new HashMap<>();
        inicializarReglas();
    }

    private void inicializarReglas() {
        // REGLA 1: destino Córdoba con peso liviano
        reglas.put("REGLA_CORDOBA_LIVIANO", new ExpresionAND(
                new ExpresionDestino("Córdoba"),
                new ExpresionPeso(10, "<")));

        // REGLA 2: envío costoso
        reglas.put("REGLA_ENVIO_COSTOSO", new ExpresionCosto(100, ">"));

        // REGLA 3: destino no restringido
        reglas.put("REGLA_NO_RESTRINGIDO", new ExpresionNOT(new ExpresionRestringido()));
    }

    /**
     * Procesa un envío pasándolo por los tres patrones integrados.
     * Devuelve el número de seguimiento, o null si fue rechazado.
     */
    public String procesarEnvio(ContextoValidacion ctx) {
        System.out.println("\n=== Procesando Envío ===");
        Envio envio = ctx.getEnvio();

        // 1. Chain: validar
        if (!validadores.validarEnvio(ctx)) {
            System.out.println("Envío rechazado en validación");
            return null;
        }

        // 2. Command: crear con historial
        ComandoCrearEnvio cmd = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmd);
        String numero = cmd.getNumeroSeguimiento();

        // 3. Interpreter: evaluar reglas de negocio
        System.out.println("\n--- Reglas de Negocio ---");
        for (Map.Entry<String, Expresion> entry : reglas.entrySet()) {
            String resultado = entry.getValue().evaluar(envio) ? "✓ Cumple" : "  No cumple";
            System.out.println("  " + resultado + " " + entry.getKey());
        }

        return numero;
    }

    public ColaComandos getCola()          { return cola; }
    public ServicioEnvios getServicio()    { return servicio; }
    public CadenaValidadores getValidadores() { return validadores; }
}
