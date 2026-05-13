package com.logismart.aplicacion.hito10;

import com.logismart.dominio.Envio;
import com.logismart.infraestructura.comportamiento.chain.CadenaValidadores;
import com.logismart.infraestructura.comportamiento.chain.SistemaCapacidad;
import com.logismart.infraestructura.comportamiento.chain.SistemaInventario;
import com.logismart.infraestructura.comportamiento.command.ColaComandos;
import com.logismart.infraestructura.comportamiento.command.ComandoActualizarEstado;
import com.logismart.infraestructura.comportamiento.command.ComandoAgregarServicio;
import com.logismart.infraestructura.comportamiento.command.ComandoCambiarMetodoPago;
import com.logismart.infraestructura.comportamiento.command.ComandoCancelarEnvio;
import com.logismart.infraestructura.comportamiento.command.ComandoCrearEnvio;
import com.logismart.infraestructura.comportamiento.command.ServicioEnvios;
import com.logismart.infraestructura.comportamiento.interpreter.Expresion;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionAND;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionCosto;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionDestino;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionNOT;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionOR;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionOrigen;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionPeso;
import com.logismart.infraestructura.comportamiento.interpreter.ExpresionRestringido;

public final class CasosDePruebaHito10 {

    private static int total;
    private static int ok;

    private CasosDePruebaHito10() {}

    public static void ejecutar() {
        total = 0;
        ok = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  8. GOF - HITO 10");
        System.out.println("══════════════════════════════════════════════");

        probarChain();
        probarCommand();
        probarInterpreter();
        probarIntegracion();

        System.out.println("\n[Hito 10] Casos ejecutados: " + total + " | OK: " + ok);
        if (total != ok) {
            throw new IllegalStateException("Hay casos fallidos en Hito 10");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CHAIN OF RESPONSIBILITY (6 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarChain() {
        System.out.println("\n--- Chain of Responsibility ---");

        SistemaInventario inv = new SistemaInventario();
        SistemaCapacidad  cap = new SistemaCapacidad(100.0);
        CadenaValidadores cadena = new CadenaValidadores(inv, cap);

        // Caso 1: envío con todos los datos válidos pasa la cadena completa
        Envio valido = new Envio("Buenos Aires", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");
        verificar(cadena.validarEnvio(valido), "Caso 1: envío válido aprobado");

        // Caso 2: origen vacío — falla en ValidadorDatos
        Envio origenVacio = new Envio("", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");
        verificar(!cadena.validarEnvio(origenVacio), "Caso 2: origen vacío rechazado");

        // Caso 3: peso cero — falla en ValidadorDatos
        Envio pesoInvalido = new Envio("Buenos Aires", "Córdoba", 0.0, 150.0, "TARJETA", "PROD-001");
        verificar(!cadena.validarEnvio(pesoInvalido), "Caso 3: peso inválido rechazado");

        // Caso 4: costo cero — falla en ValidadorPago
        Envio costoInvalido = new Envio("Buenos Aires", "Córdoba", 5.0, 0.0, "TARJETA", "PROD-001");
        verificar(!cadena.validarEnvio(costoInvalido), "Caso 4: costo inválido rechazado");

        // Caso 5: destino restringido — falla en ValidadorSeguridad
        Envio restringido = new Envio("Buenos Aires", "Zona Restringido", 5.0, 150.0, "TARJETA", "PROD-001");
        verificar(!cadena.validarEnvio(restringido), "Caso 5: destino restringido rechazado");

        // Caso 6: producto sin stock — falla en ValidadorInventario
        Envio sinStock = new Envio("Buenos Aires", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-SIN-STOCK");
        verificar(!cadena.validarEnvio(sinStock), "Caso 6: sin stock rechazado");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // COMMAND (9 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarCommand() {
        System.out.println("\n--- Command ---");

        ServicioEnvios servicio = new ServicioEnvios();
        ColaComandos   cola     = new ColaComandos();

        Envio envio = new Envio("Buenos Aires", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");

        // Caso 1: crear envío
        ComandoCrearEnvio cmdCrear = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmdCrear);
        String numero = cmdCrear.getNumeroSeguimiento();
        verificar(numero != null, "Caso 1: crear envío genera número de seguimiento");

        // Caso 2: actualizar estado a EN TRÁNSITO
        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN TRÁNSITO"));
        verificar("EN TRÁNSITO".equals(servicio.obtenerEstado(numero)), "Caso 2: estado actualizado a EN TRÁNSITO");

        // Caso 3: cambiar método de pago
        cola.ejecutar(new ComandoCambiarMetodoPago(servicio, numero, "EFECTIVO"));
        verificar("EFECTIVO".equals(servicio.obtenerMetodoPago(numero)), "Caso 3: método de pago cambiado a EFECTIVO");

        // Caso 4: agregar servicio Seguro
        cola.ejecutar(new ComandoAgregarServicio(servicio, numero, "Seguro"));
        verificar(servicio.obtenerServicios(numero).contains("Seguro"), "Caso 4: servicio Seguro agregado");

        // Caso 5: historial tiene 4 comandos
        cola.mostrarHistorial();
        verificar(cola.obtenerTamano() == 4, "Caso 5: historial tiene 4 comandos");

        // Caso 6: deshacer — remueve Seguro
        cola.deshacer();
        verificar(!servicio.obtenerServicios(numero).contains("Seguro"), "Caso 6: deshacer remueve Seguro");

        // Caso 7: deshacer — vuelve a TARJETA
        cola.deshacer();
        verificar("TARJETA".equals(servicio.obtenerMetodoPago(numero)), "Caso 7: deshacer restaura método TARJETA");

        // Caso 8: rehacer — vuelve a EFECTIVO
        cola.rehacer();
        verificar("EFECTIVO".equals(servicio.obtenerMetodoPago(numero)), "Caso 8: rehacer restaura método EFECTIVO");

        // Caso 9: cancelar envío y deshacer (reactivar)
        ColaComandos cola2 = new ColaComandos();
        ServicioEnvios srv2 = new ServicioEnvios();
        ComandoCrearEnvio cmdCrear2 = new ComandoCrearEnvio(srv2, envio);
        cola2.ejecutar(cmdCrear2);
        String num2 = cmdCrear2.getNumeroSeguimiento();
        cola2.ejecutar(new ComandoCancelarEnvio(srv2, num2));
        verificar("CANCELADO".equals(srv2.obtenerEstado(num2)), "Caso 9a: cancelar envío");
        cola2.deshacer();
        verificar("CONFIRMADO".equals(srv2.obtenerEstado(num2)), "Caso 9b: deshacer cancelación reactiva envío");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INTERPRETER (7 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarInterpreter() {
        System.out.println("\n--- Interpreter ---");

        Envio envio1 = new Envio("Buenos Aires", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");
        Envio envio2 = new Envio("Rosario", "Córdoba", 15.0, 200.0, "EFECTIVO", "PROD-002");
        Envio envio3 = new Envio("Buenos Aires", "Zona Restringido", 5.0, 150.0, "TARJETA", "PROD-001");

        // Caso 1: expresión simple — ORIGEN = "Buenos Aires"
        Expresion regla1 = new ExpresionOrigen("Buenos Aires");
        verificar(regla1.evaluar(envio1), "Caso 1: origen correcto → true");
        verificar(!regla1.evaluar(envio2), "Caso 1b: origen incorrecto → false");

        // Caso 2: AND — ORIGEN = "Buenos Aires" AND PESO < 10
        Expresion regla2 = new ExpresionAND(
                new ExpresionOrigen("Buenos Aires"),
                new ExpresionPeso(10, "<"));
        verificar(regla2.evaluar(envio1), "Caso 2: AND cumplido → true");
        verificar(!regla2.evaluar(envio2), "Caso 2b: AND fallido (origen y peso) → false");

        // Caso 3: OR — DESTINO = "Córdoba" OR DESTINO = "Mendoza"
        Expresion regla3 = new ExpresionOR(
                new ExpresionDestino("Córdoba"),
                new ExpresionDestino("Mendoza"));
        verificar(regla3.evaluar(envio1), "Caso 3: OR cumplido → true");

        // Caso 4: NOT RESTRINGIDO
        Expresion regla4 = new ExpresionNOT(new ExpresionRestringido());
        verificar(regla4.evaluar(envio1), "Caso 4: NOT restringido → true");
        verificar(!regla4.evaluar(envio3), "Caso 4b: destino restringido → false");

        // Caso 5: expresión compleja — ORIGEN AND COSTO > 100 AND NOT RESTRINGIDO
        Expresion regla5 = new ExpresionAND(
                new ExpresionAND(
                        new ExpresionOrigen("Buenos Aires"),
                        new ExpresionCosto(100, ">")),
                new ExpresionNOT(new ExpresionRestringido()));
        verificar(regla5.evaluar(envio1), "Caso 5: regla compleja cumplida → true");
        verificar(!regla5.evaluar(envio3), "Caso 5b: regla compleja con restringido → false");

        // Caso 6: COSTO con operador "="
        Expresion reglaCostoExacto = new ExpresionCosto(150.0, "=");
        verificar(reglaCostoExacto.evaluar(envio1), "Caso 6: costo exacto → true");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INTEGRACIÓN (3 casos)
    // ─────────────────────────────────────────────────────────────────────────
    private static void probarIntegracion() {
        System.out.println("\n--- Integración ---");

        SistemaLogisticaCompleto sistema = new SistemaLogisticaCompleto();

        // Caso 1: envío válido se procesa y genera número de seguimiento
        Envio envioValido = new Envio("Buenos Aires", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");
        String numero = sistema.procesarEnvio(envioValido);
        verificar(numero != null, "Caso 1: integración — envío válido genera número");

        // Caso 2: el estado del envío creado es CONFIRMADO
        verificar("CONFIRMADO".equals(sistema.getServicio().obtenerEstado(numero)),
                "Caso 2: integración — estado inicial CONFIRMADO");

        // Caso 3: envío inválido (origen vacío) es rechazado por la cadena
        Envio invalido = new Envio("", "Córdoba", 5.0, 150.0, "TARJETA", "PROD-001");
        String numeroInvalido = sistema.procesarEnvio(invalido);
        verificar(numeroInvalido == null, "Caso 3: integración — envío inválido rechazado");
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static void verificar(boolean condicion, String descripcion) {
        total++;
        if (!condicion) {
            throw new IllegalStateException("Fallo: " + descripcion);
        }
        ok++;
        System.out.println("[OK] " + descripcion);
    }
}
