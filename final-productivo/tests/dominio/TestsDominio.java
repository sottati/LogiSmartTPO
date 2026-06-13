package dominio;

import com.logismart.dominio.centro.CentroRegional;
import com.logismart.dominio.centro.SucursalEntrega;
import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstrategiaCalculoCosto;
import com.logismart.dominio.envio.HistorialEnvios;
import com.logismart.dominio.envio.MementoEnvio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.dominio.ruta.PuntoEntrega;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.usuario.AdminEmpresa;
import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.usuario.OperadorLogistico;
import com.logismart.dominio.usuario.Transportista;
import com.logismart.dominio.vehiculo.Auto;
import com.logismart.dominio.vehiculo.Moto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestsDominio {

    private static int ok  = 0;
    private static int fail = 0;

    public static void main(String[] args) {
        System.out.println("=== Tests de Dominio — LogiSmart (final-productivo) ===\n");

        testBuilderCreatesEnvio();
        testStateTransitionsHappyPath();
        testStateCancelFromConfirmado();
        testStateRetener();
        testObserverNotified();
        testMementoSnapshotAndRestore();
        testHistorialNavigation();
        testPrototypeClone();
        testStrategyCostCalculation();
        testRolPermisos();
        testCompositeCapacity();
        testRutaDistancia();

        System.out.println("\n═══════════════════════════════════════");
        System.out.println(ok + fail + " casos ejecutados — " + ok + " OK  " + fail + " FAIL");
        System.out.println("═══════════════════════════════════════");
        if (fail > 0) System.exit(1);
    }

    // ── Builder ─────────────────────────────────────────────────────────────────

    static void testBuilderCreatesEnvio() {
        Envio e = new Envio.EnvioBuilder("E-001", "Buenos Aires", "Córdoba")
                .peso(12.5)
                .tipo("NACIONAL")
                .fragil(true)
                .prioridad("ALTA")
                .build();
        ok("Builder — campos básicos",
                "E-001".equals(e.getId())
                && "Buenos Aires".equals(e.getOrigen())
                && 12.5 == e.getPeso()
                && e.isFragil()
                && "CONFIRMADO".equals(e.getEstado()));
    }

    // ── State ───────────────────────────────────────────────────────────────────

    static void testStateTransitionsHappyPath() {
        Envio e = buildEnvio("E-002");
        ok("State — inicial CONFIRMADO", "CONFIRMADO".equals(e.getEstado()));
        e.validar();
        ok("State — CONFIRMADO → EN_TRANSITO", "EN_TRANSITO".equals(e.getEstado()));
        e.entregar();
        ok("State — EN_TRANSITO → EN_REPARTO", "EN_REPARTO".equals(e.getEstado()));
        e.entregar();
        ok("State — EN_REPARTO → ENTREGADO", "ENTREGADO".equals(e.getEstado()));
    }

    static void testStateCancelFromConfirmado() {
        Envio e = buildEnvio("E-003");
        e.cancelar();
        ok("State — CONFIRMADO → CANCELADO", "CANCELADO".equals(e.getEstado()));
        e.cancelar();
        ok("State — CANCELADO no cambia en re-cancelar", "CANCELADO".equals(e.getEstado()));
    }

    static void testStateRetener() {
        Envio e = buildEnvio("E-004");
        e.validar();
        e.retener();
        ok("State — EN_TRANSITO → RETENIDO", "RETENIDO".equals(e.getEstado()));
        e.validar();
        ok("State — RETENIDO → EN_TRANSITO (liberar retención)", "EN_TRANSITO".equals(e.getEstado()));
    }

    // ── Observer ────────────────────────────────────────────────────────────────

    static void testObserverNotified() {
        Envio e = buildEnvio("E-005");
        List<String> eventos = new ArrayList<>();
        ObservadorEnvio obs = envio -> eventos.add(envio.getEstado());
        e.adjuntarObservador(obs);
        e.validar();
        e.entregar();
        ok("Observer — notificado en cada cambio de estado",
                eventos.size() == 2
                && "EN_TRANSITO".equals(eventos.get(0))
                && "EN_REPARTO".equals(eventos.get(1)));
        e.desadjuntarObservador(obs);
        e.entregar();
        ok("Observer — silencioso tras desuscribir", eventos.size() == 2);
    }

    // ── Memento ─────────────────────────────────────────────────────────────────

    static void testMementoSnapshotAndRestore() {
        Envio e = new Envio.EnvioBuilder("E-006", "Rosario", "Mendoza")
                .peso(5.0).costo(300.0).build();
        MementoEnvio snap = e.crearMemento();
        ok("Memento — snapshot captura estado",
                "CONFIRMADO".equals(snap.obtenerEstado())
                && "Rosario".equals(snap.obtenerOrigen())
                && 300.0 == snap.obtenerCosto());
        e.validar();
        e.restaurarDesdeMemento(snap);
        ok("Memento — restaura estado anterior", "CONFIRMADO".equals(e.getEstado()));
    }

    static void testHistorialNavigation() {
        Envio e = buildEnvio("E-007");
        HistorialEnvios h = new HistorialEnvios();
        h.guardarEstado(e);
        e.validar();
        h.guardarEstado(e);
        ok("Historial — tamaño correcto", h.obtenerTamaño() == 2);
        h.irAlEstadoAnterior(e);
        ok("Historial — retrocede a CONFIRMADO", "CONFIRMADO".equals(e.getEstado()));
        h.irAlEstadoSiguiente(e);
        ok("Historial — avanza a EN_TRANSITO", "EN_TRANSITO".equals(e.getEstado()));
    }

    // ── Prototype ───────────────────────────────────────────────────────────────

    static void testPrototypeClone() {
        Envio prototipo = new Envio.EnvioBuilder("TMPL-001", "Buenos Aires", "Córdoba")
                .peso(2.0).tipo("NACIONAL").build();
        Envio clon1 = prototipo.clone();
        clon1.setId("E-101");
        clon1.setDestino("Rosario");
        Envio clon2 = prototipo.clone();
        clon2.setId("E-102");
        clon2.setDestino("Mendoza");
        ok("Prototype — clon independiente del original",
                "TMPL-001".equals(prototipo.getId())
                && "Córdoba".equals(prototipo.getDestino()));
        ok("Prototype — clones tienen IDs y destinos distintos",
                "E-101".equals(clon1.getId()) && "Rosario".equals(clon1.getDestino())
                && "E-102".equals(clon2.getId()) && "Mendoza".equals(clon2.getDestino()));
    }

    // ── Strategy ────────────────────────────────────────────────────────────────

    static void testStrategyCostCalculation() {
        Envio e = new Envio.EnvioBuilder("E-010", "BsAs", "Cordoba")
                .peso(10.0).build();
        EstrategiaCalculoCosto porPeso = envio -> envio.getPeso() * 50.0;
        e.establecerEstrategia(porPeso);
        double costo = e.calcularCosto();
        ok("Strategy — costo calculado por peso (10 kg × $50)", costo == 500.0);

        EstrategiaCalculoCosto fijo = envio -> 999.0;
        e.establecerEstrategia(fijo);
        ok("Strategy — intercambio de estrategia en tiempo de ejecución",
                e.calcularCosto() == 999.0);
    }

    // ── Rol / IPermisos ─────────────────────────────────────────────────────────

    static void testRolPermisos() {
        OperadorLogistico op = new OperadorLogistico("u1", "op", "op@ls.com", "h", "ACTIVO");
        AdminEmpresa      ae = new AdminEmpresa("u2", "ae", "ae@ls.com", "h", "ACTIVO");
        ClienteFinal      cf = new ClienteFinal("u3", "cf", "cf@ls.com", "h", "ACTIVO");

        ok("Permisos — Operador puede crear envío", op.puedeCrearEnvio());
        ok("Permisos — Operador puede asignar ruta", op.puedeAsignarRuta());
        ok("Permisos — AdminEmpresa NO puede asignar ruta", !ae.puedeAsignarRuta());
        ok("Permisos — Cliente NO puede crear envío", !cf.puedeCrearEnvio());
        ok("Permisos — Cliente NO puede ver reportes", !cf.puedeVerReportes());

        Transportista tr = new Transportista("u4", "tr", "tr@ls.com", "h", "ACTIVO", "LIC-001");
        ok("Permisos — Transportista NO puede crear envío", !tr.puedeCrearEnvio());
    }

    // ── Composite ───────────────────────────────────────────────────────────────

    static void testCompositeCapacity() {
        SucursalEntrega s1 = new SucursalEntrega("Palermo",  "CABA",  "S-01", 100);
        SucursalEntrega s2 = new SucursalEntrega("Belgrano", "CABA",  "S-02", 50);
        SucursalEntrega s3 = new SucursalEntrega("San Telmo","CABA",  "S-03", 80);

        Envio e1 = buildEnvio("E-C01");
        Envio e2 = buildEnvio("E-C02");
        s1.agregarEnvio(e1);
        s2.agregarEnvio(e2);

        CentroRegional cr = new CentroRegional("Centro CABA", "Buenos Aires", "CR-01");
        cr.agregar(s1);
        cr.agregar(s2);
        cr.agregar(s3);

        ok("Composite — capacidad total regional (100+50+80)", cr.obtenerCapacidad() == 230);
        ok("Composite — ocupación agregada (1+1+0)", cr.obtenerOcupacion() == 2);
        ok("Composite — porcentaje (2/230 ≈ 0.87%)",
                cr.obtenerPorcentajeOcupacion() > 0.8 && cr.obtenerPorcentajeOcupacion() < 0.9);

        CentroRegional nacional = new CentroRegional("LogiSmart AR", "Argentina", "NAC-01");
        nacional.agregar(cr);
        ok("Composite — jerarquía anidada agrega correctamente",
                nacional.obtenerCapacidad() == 230 && nacional.obtenerOcupacion() == 2);
    }

    // ── Ruta ────────────────────────────────────────────────────────────────────

    static void testRutaDistancia() {
        Ruta r = new Ruta("R-001", 0, 0, "PENDIENTE");
        r.agregarParada("Buenos Aires", -34.6037, -58.3816, "08:00-10:00", 1);
        r.agregarParada("Rosario",      -32.9468, -60.6393, "12:00-14:00", 2);
        r.agregarParada("Córdoba",      -31.4201, -64.1888, "16:00-18:00", 3);
        double dist = r.calcularDistanciaTotal();
        ok("Ruta — distancia BsAs→Rosario→Córdoba (~580 km)",
                dist > 500.0 && dist < 700.0);
        Auto auto = new Auto("V-01", "AA000AA");
        r.asignarVehiculo(auto);
        double costo = r.calcularCostoEstimado();
        ok("Ruta — costo estimado con Auto ($1/km × dist)", costo > 0 && costo < 1000.0);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    static void ok(String descripcion, boolean condicion) {
        if (condicion) {
            System.out.println("  ✓ " + descripcion);
            ok++;
        } else {
            System.out.println("  ✗ " + descripcion + "  ← FALLO");
            fail++;
        }
    }

    static Envio buildEnvio(String id) {
        return new Envio.EnvioBuilder(id, "BsAs", "Cordoba")
                .peso(5.0).tipo("NACIONAL").build();
    }
}
