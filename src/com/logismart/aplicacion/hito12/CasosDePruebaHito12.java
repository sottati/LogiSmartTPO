package com.logismart.aplicacion.hito12;

import com.logismart.dominio.Envio;
import com.logismart.infraestructura.comportamiento.state.EstadoCancelado;
import com.logismart.infraestructura.comportamiento.state.EstadoConfirmado;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaDistancia;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaHibrida;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaPeso;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaUrgencia;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaVolumen;
import com.logismart.infraestructura.comportamiento.template.ProcesoEnvio;
import com.logismart.infraestructura.comportamiento.template.ProcesoInternacional;
import com.logismart.infraestructura.comportamiento.template.ProcesoNacional;
import com.logismart.infraestructura.comportamiento.template.ProcesoUrgente;
import com.logismart.infraestructura.comportamiento.visitor.NodoCentroRegional;
import com.logismart.infraestructura.comportamiento.visitor.NodoPuntoEntrega;
import com.logismart.infraestructura.comportamiento.visitor.VisitorBusquedaPuntosCriticos;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCalculoCostoOperativo;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCalculoOcupacion;
import com.logismart.infraestructura.comportamiento.visitor.VisitorGeneradorReporte;

public final class CasosDePruebaHito12 {
    private static int total;
    private static int ok;

    private CasosDePruebaHito12() {}

    public static void ejecutar() {
        total = 0;
        ok = 0;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  GOF - HITO 12: Comportamiento III");
        System.out.println("══════════════════════════════════════════════");

        probarState();
        probarStrategy();
        probarTemplateMethod();
        probarVisitor();
        probarIntegracion();

        System.out.println("\n[Hito 12] Casos ejecutados: " + total + " | OK: " + ok);
        if (total != ok) {
            throw new IllegalStateException("Hay casos fallidos en Hito 12");
        }
    }

    private static void probarState() {
        System.out.println("\n--- State ---");

        Envio normal = nuevoEnvio("ENV-S01");
        normal.validar();
        verificar("EN_TRANSITO".equals(normal.obtenerNombreEstadoGoF()), "Caso 1: validar pasa a EN_TRANSITO");
        normal.entregar();
        verificar("EN_REPARTO".equals(normal.obtenerNombreEstadoGoF()), "Caso 2: entregar en transito pasa a EN_REPARTO");
        normal.entregar();
        verificar("ENTREGADO".equals(normal.obtenerNombreEstadoGoF()), "Caso 3: entregar en reparto pasa a ENTREGADO");

        Envio cancelado = nuevoEnvio("ENV-S02");
        cancelado.cambiarEstado(new EstadoCancelado());
        verificar("CANCELADO".equals(cancelado.obtenerNombreEstadoGoF()), "Caso 4: cancelacion deja estado CANCELADO");

        Envio retenido = nuevoEnvio("ENV-S03");
        retenido.retener();
        verificar("RETENIDO".equals(retenido.obtenerNombreEstadoGoF()), "Caso 5: retener deja estado RETENIDO");
        retenido.devolver();
        verificar("CONFIRMADO".equals(retenido.obtenerNombreEstadoGoF()), "Caso 6: devolver retenido vuelve a CONFIRMADO");
        retenido.validar();
        retenido.reclamar();
        verificar("EN_TRANSITO".equals(retenido.obtenerNombreEstadoGoF()), "Caso 7: reclamar no cambia estado en transito");

        normal.validar();
        verificar("ENTREGADO".equals(normal.obtenerNombreEstadoGoF()), "Caso 8a: validar entregado es transicion invalida");
        cancelado.entregar();
        verificar("CANCELADO".equals(cancelado.obtenerNombreEstadoGoF()), "Caso 8b: entregar cancelado es transicion invalida");
        normal.retener();
        verificar("ENTREGADO".equals(normal.obtenerNombreEstadoGoF()), "Caso 8c: retener entregado es transicion invalida");
    }

    private static void probarStrategy() {
        System.out.println("\n--- Strategy ---");

        Envio envio = new Envio.EnvioBuilder("ENV-ST01", "Buenos Aires", "Cordoba").peso(10.0).tipo("URGENTE").build();

        envio.establecerEstrategia(new EstrategiaDistancia());
        verificar(envio.calcularCostoConEstrategia() == 5000.0, "Caso 1: estrategia distancia usa 500 km fijos");

        envio.establecerEstrategia(new EstrategiaPeso());
        verificar(envio.calcularCostoConEstrategia() == 50.0, "Caso 2: estrategia peso calcula peso x 5");

        envio.establecerEstrategia(new EstrategiaUrgencia());
        verificar(envio.calcularCostoConEstrategia() == 500.0, "Caso 3: estrategia urgencia URGENTE = 500");

        envio.establecerEstrategia(new EstrategiaVolumen());
        verificar(envio.calcularCostoConEstrategia() == 40.0, "Caso 4: estrategia volumen calcula peso x 2 x 2");

        envio.establecerEstrategia(new EstrategiaHibrida());
        verificar(envio.calcularCostoConEstrategia() == 2165.0, "Caso 5: estrategia hibrida combina 40/30/30");

        envio.establecerEstrategia(new EstrategiaPeso());
        double peso = envio.calcularCostoConEstrategia();
        envio.establecerEstrategia(new EstrategiaUrgencia());
        double urgencia = envio.calcularCostoConEstrategia();
        verificar(peso != urgencia, "Caso 6: cambio dinamico de estrategia modifica resultado");
        verificar(urgencia > 0.0, "Caso 7: resultado positivo garantizado");
    }

    private static void probarTemplateMethod() {
        System.out.println("\n--- Template Method ---");

        Envio nacional = nuevoEnvio("ENV-T01");
        new ProcesoNacional().procesarEnvio(nacional);
        verificar(nacional.getCosto() == 500.0, "Caso 1: proceso nacional calcula costo");

        Envio internacional = nuevoEnvio("ENV-T02");
        new ProcesoInternacional().procesarEnvio(internacional);
        verificar(internacional.getCosto() == 2250.0, "Caso 2: proceso internacional calcula costo");

        Envio urgente = nuevoEnvio("ENV-T03");
        new ProcesoUrgente().procesarEnvio(urgente);
        verificar(urgente.getCosto() == 1400.0, "Caso 3: proceso urgente calcula costo");

        ProcesoAuditable auditable = new ProcesoAuditable();
        auditable.procesarEnvio(nuevoEnvio("ENV-T04"));
        verificar(auditable.log.equals("validar>calcularCosto>procesarPago>notificar>"),
                "Caso 4: procesarEnvio llama los 4 pasos en orden");
        verificar(auditable.validarLlamado, "Caso 5: paso validar ejecutado");
        verificar(auditable.calcularLlamado, "Caso 6: paso calcularCosto ejecutado");
        verificar(auditable.notificarLlamado, "Caso 7: extension por subclase concreta funciona");
    }

    private static void probarVisitor() {
        System.out.println("\n--- Visitor ---");

        NodoCentroRegional red = redDePrueba();

        VisitorCalculoOcupacion ocupacion = new VisitorCalculoOcupacion();
        red.aceptar(ocupacion);
        verificar(ocupacion.obtenerOcupacion() == 80.0, "Caso 1: ocupacion total correcta");
        verificar(ocupacion.obtenerNodosVisitados() == 4, "Caso 2: ocupacion recorre toda la estructura");

        VisitorGeneradorReporte reporte = new VisitorGeneradorReporte();
        red.aceptar(reporte);
        verificar(reporte.obtenerReporte().contains("Punto Norte"), "Caso 3: reporte incluye punto de entrega");
        verificar(reporte.obtenerNodosVisitados() == 4, "Caso 4: reporte recorre toda la estructura");

        VisitorCalculoCostoOperativo costo = new VisitorCalculoCostoOperativo();
        red.aceptar(costo);
        verificar(costo.obtenerCostoTotal() == 5000.0, "Caso 5: costo operativo total correcto");
        verificar(costo.obtenerNodosVisitados() == 4, "Caso 6: costo recorre toda la estructura");

        VisitorBusquedaPuntosCriticos criticos = new VisitorBusquedaPuntosCriticos();
        red.aceptar(criticos);
        verificar(criticos.obtenerPuntosCriticos().contains("Punto Norte"), "Caso 7: busqueda detecta punto critico");
        verificar(criticos.obtenerNodosVisitados() == 4, "Caso 8: busqueda recorre toda la estructura");
    }

    private static void probarIntegracion() {
        System.out.println("\n--- Integracion ---");

        SistemaLogisticaAvanzada sistema = new SistemaLogisticaAvanzada(redDePrueba());
        Envio envio = new Envio.EnvioBuilder("ENV-I01", "Buenos Aires", "Cordoba").peso(10.0).tipo("EXPRESS").build();
        double costo = sistema.procesarEnvio(envio, new EstrategiaUrgencia(), new ProcesoNacional());

        verificar(costo == 300.0, "Caso 1: integracion calcula costo por Strategy");
        verificar("ENTREGADO".equals(envio.obtenerNombreEstadoGoF()), "Caso 2: integracion avanza State hasta ENTREGADO");
        verificar(envio.getCosto() == 1000.0, "Caso 3: integracion ejecuta Template Method");
        verificar(sistema.calcularOcupacionRed() == 80.0
                && sistema.calcularCostoOperativoRed() == 5000.0,
                "Caso 4: integracion ejecuta Visitor sobre la red");
    }

    private static Envio nuevoEnvio(String id) {
        Envio envio = new Envio.EnvioBuilder(id, "Buenos Aires", "Cordoba").peso(5.0).tipo("NORMAL").build();
        envio.cambiarEstado(new EstadoConfirmado());
        return envio;
    }

    private static NodoCentroRegional redDePrueba() {
        NodoCentroRegional raiz = new NodoCentroRegional("Centro AMBA");
        NodoCentroRegional regional = new NodoCentroRegional("Centro Norte");
        regional.agregar(new NodoPuntoEntrega("Punto Norte", 90, 100, 1200.0));
        regional.agregar(new NodoPuntoEntrega("Punto Oeste", 70, 100, 1800.0));
        raiz.agregar(regional);
        return raiz;
    }

    private static void verificar(boolean condicion, String descripcion) {
        total++;
        if (!condicion) {
            throw new IllegalStateException("Fallo: " + descripcion);
        }
        ok++;
        System.out.println("[OK] " + descripcion);
    }

    private static class ProcesoAuditable extends ProcesoEnvio {
        private final StringBuilder builder = new StringBuilder();
        private String log = "";
        private boolean validarLlamado;
        private boolean calcularLlamado;
        private boolean notificarLlamado;

        @Override
        protected void validar(Envio envio) {
            validarLlamado = true;
            registrar("validar");
        }

        @Override
        protected void calcularCosto(Envio envio) {
            calcularLlamado = true;
            registrar("calcularCosto");
        }

        @Override
        protected void procesarPago(Envio envio) {
            registrar("procesarPago");
        }

        @Override
        protected void notificar(Envio envio) {
            notificarLlamado = true;
            registrar("notificar");
            log = builder.toString();
        }

        private void registrar(String paso) {
            builder.append(paso).append('>');
        }
    }
}
