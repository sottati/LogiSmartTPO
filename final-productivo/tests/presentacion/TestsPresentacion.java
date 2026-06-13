package tests.presentacion;

import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.empresa.Suscripcion;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.dominio.ruta.PuntoEntrega;
import com.logismart.dominio.usuario.AdminPlataforma;
import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.usuario.OperadorLogistico;
import com.logismart.dominio.usuario.Transportista;
import com.logismart.aplicacion.PedidoEcommerce;
import com.logismart.presentacion.LogiSmartController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestsPresentacion {

    private static int total = 0;
    private static int ok    = 0;

    // Actores reutilizados
    private static final OperadorLogistico operador      = new OperadorLogistico("U-01","ops","ops@t.com","hash","ACTIVO");
    private static final Transportista     transportista  = new Transportista("U-02","cho","cho@t.com","hash","ACTIVO","LIC-01");
    private static final ClienteFinal      cliente        = new ClienteFinal("U-03","cli","cli@t.com","pass","ACTIVO");
    private static final AdminPlataforma   adminPlat      = new AdminPlataforma("U-04","adm","adm@t.com","hash","ACTIVO");

    public static void main(String[] args) {
        testCU01();
        testCU02();
        testCU03();
        testCU04();
        testCU05();
        testCU06();
        testCU07();
        testCU08();
        testCU06EndToEnd();

        System.out.println("\n========================================");
        System.out.println(total + " casos ejecutados, " + ok + " OK");
        System.out.println("========================================");
    }

    // ── CU-01: Importar Pedidos ────────────────────────────────────────────────

    static void testCU01() {
        System.out.println("\n--- CU-01: Importar Pedidos ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-01");
        Envio proto = envioBase("PROTO");

        List<PedidoEcommerce> pedidos = Arrays.asList(
                new PedidoEcommerce("P-A","Salta","Libro"),
                new PedidoEcommerce("P-B","Jujuy","Auricular")
        );
        List<String> ids = ctrl.importarPedidos(operador, proto, pedidos, cobro);
        assertar("CU-01 con Operador: 2 pedidos importados", ids.size() == 2);

        // permiso denegado: Cliente no puede crear envios
        List<String> negado = ctrl.importarPedidos(cliente, proto, pedidos, cobro);
        assertar("CU-01 denegado a Cliente", negado.isEmpty());
    }

    // ── CU-02: Gestionar Flota ─────────────────────────────────────────────────

    static void testCU02() {
        System.out.println("\n--- CU-02: Gestionar Flota ---");
        LogiSmartController ctrl = new LogiSmartController();

        String desc = ctrl.gestionarFlota(operador, "VH-001");
        assertar("CU-02 con Operador: vehiculo registrado (no null)", desc != null);
        assertar("CU-02: descripcion contiene 'cap='",                 desc != null && desc.contains("cap="));
        assertar("CU-02: vehiculo en flota",                           ctrl.getFlota().containsKey("VH-001"));

        // permiso denegado: Transportista no puede gestionar flota
        String negado = ctrl.gestionarFlota(transportista, "VH-002");
        assertar("CU-02 denegado a Transportista", negado == null);
    }

    // ── CU-03: Planificar Ruta ─────────────────────────────────────────────────

    static void testCU03() {
        System.out.println("\n--- CU-03: Planificar Ruta ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-03");
        Envio envio = envioBase("ENV-03");

        // Primero crear el envio en el repositorio
        ctrl.crearEnvio(operador, envio, cobro);

        List<PuntoEntrega> paradas = Arrays.asList(
                new PuntoEntrega("Av. Corrientes 1234", -34.6037, -58.3816, "09:00-12:00", 1),
                new PuntoEntrega("Calle Falsa 742",     -34.6118, -58.4173, "13:00-17:00", 2)
        );
        com.logismart.dominio.ruta.Ruta ruta = ctrl.planificarRuta(operador, "ENV-03", paradas);
        assertar("CU-03 con Operador: ruta no null",               ruta != null);
        assertar("CU-03: ruta tiene 2 paradas",                    ruta != null && ruta.getParadas().size() == 2);
        assertar("CU-03: ruta en mapa de rutas del controller",    ctrl.getRutas().containsKey("R-ENV-03"));

        // permiso denegado: ClienteFinal no puede planificar rutas
        com.logismart.dominio.ruta.Ruta negada = ctrl.planificarRuta(cliente, "ENV-03", paradas);
        assertar("CU-03 denegado a Cliente", negada == null);
    }

    // ── CU-04: Asignar Hoja de Ruta ───────────────────────────────────────────

    static void testCU04() {
        System.out.println("\n--- CU-04: Asignar Hoja de Ruta ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-04");
        Envio envio = envioBase("ENV-04");
        ctrl.crearEnvio(operador, envio, cobro);

        boolean ok = ctrl.asignarHojaDeRuta(operador, "ENV-04", "T-001");
        assertar("CU-04 con Operador: retorna true",           ok);

        // El envio deberia avanzar a EN_TRANSITO
        String estado = ctrl.getRepositorio().obtener("ENV-04")
                .map(Envio::getEstado).orElse("?");
        assertar("CU-04: estado EN_TRANSITO tras asignacion",  "EN_TRANSITO".equals(estado));

        // permiso denegado: Transportista no puede asignar rutas
        assertar("CU-04 denegado a Transportista",
                !ctrl.asignarHojaDeRuta(transportista, "ENV-04", "T-999"));
    }

    // ── CU-05: Iniciar Recorrido ───────────────────────────────────────────────

    static void testCU05() {
        System.out.println("\n--- CU-05: Iniciar Recorrido ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-05");

        // Avanzar: CONFIRMADO → EN_TRANSITO
        Envio envio = envioBase("ENV-05");
        ctrl.crearEnvio(operador, envio, cobro);
        ctrl.asignarHojaDeRuta(operador, "ENV-05", "T-001");

        // Ahora EN_TRANSITO → EN_REPARTO
        boolean ok = ctrl.iniciarRecorrido(transportista, "ENV-05");
        assertar("CU-05 con Transportista: retorna true", ok);

        String estado = ctrl.getRepositorio().obtener("ENV-05")
                .map(Envio::getEstado).orElse("?");
        assertar("CU-05: estado EN_REPARTO tras iniciar recorrido", "EN_REPARTO".equals(estado));

        // permiso denegado: Operador no puede registrar entregas / iniciar recorrido
        assertar("CU-05 denegado a Operador",
                !ctrl.iniciarRecorrido(operador, "ENV-05"));
    }

    // ── CU-06: Registrar Entrega ───────────────────────────────────────────────

    static void testCU06() {
        System.out.println("\n--- CU-06: Registrar Entrega ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-06");

        // Avanzar: CONFIRMADO → EN_TRANSITO → EN_REPARTO
        Envio envio = envioBase("ENV-06");
        ctrl.crearEnvio(operador, envio, cobro);
        ctrl.asignarHojaDeRuta(operador, "ENV-06", "T-001");
        ctrl.iniciarRecorrido(transportista, "ENV-06");

        // EN_REPARTO → ENTREGADO
        boolean ok = ctrl.registrarEntrega(transportista, "ENV-06");
        assertar("CU-06 con Transportista: retorna true", ok);

        String estado = ctrl.getRepositorio().obtener("ENV-06")
                .map(Envio::getEstado).orElse("?");
        assertar("CU-06: estado ENTREGADO tras registro",  "ENTREGADO".equals(estado));

        // permiso denegado: Operador no puede registrar entregas
        assertar("CU-06 denegado a Operador",
                !ctrl.registrarEntrega(operador, "ENV-06"));
    }

    // ── CU-07: Consultar Ubicacion ─────────────────────────────────────────────

    static void testCU07() {
        System.out.println("\n--- CU-07: Consultar Ubicacion ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-07");
        Envio envio = envioBase("ENV-07");
        ctrl.crearEnvio(operador, envio, cobro);

        String info = ctrl.consultarUbicacion(cliente, "ENV-07");
        assertar("CU-07 con ClienteFinal: retorna info no null",   info != null);
        assertar("CU-07: info contiene id del envio",              info != null && info.contains("ENV-07"));
        assertar("CU-07: info contiene estado del envio",          info != null && info.contains("CONFIRMADO"));

        // Envio inexistente → mensaje descriptivo
        String noExiste = ctrl.consultarUbicacion(cliente, "INEXISTENTE");
        assertar("CU-07: envio inexistente → mensaje no null", noExiste != null);
    }

    // ── CU-08: Administrar Suscripcion ────────────────────────────────────────

    static void testCU08() {
        System.out.println("\n--- CU-08: Administrar Suscripcion ---");
        LogiSmartController ctrl = new LogiSmartController();
        Suscripcion suscripcion  = new Suscripcion("SUS-01","BASICO","PENDIENTE",1500.0,
                LocalDate.now().plusDays(30));

        String estadoActivar = ctrl.administrarSuscripcion(adminPlat, suscripcion, "ACTIVAR");
        assertar("CU-08 ACTIVAR: estado ACTIVA",   "ACTIVA".equals(estadoActivar));

        String estadoPausar  = ctrl.administrarSuscripcion(adminPlat, suscripcion, "PAUSAR");
        assertar("CU-08 PAUSAR: estado PAUSADA",   "PAUSADA".equals(estadoPausar));

        // permiso denegado: Operador no puede administrar empresas
        String negado = ctrl.administrarSuscripcion(operador, suscripcion, "ACTIVAR");
        assertar("CU-08 denegado a Operador",      negado == null);
    }

    // ── CU-06 End-to-End: State + Observer + Memento ─────────────────────────

    static void testCU06EndToEnd() {
        System.out.println("\n--- CU-06 End-to-End: State + Observer + Memento ---");
        LogiSmartController ctrl = new LogiSmartController();
        Cobro cobro = cobro("CB-E2E");

        // Observer de captura (anonimo)
        int[] disparos = {0};
        String[] ultimoEstado = {null};
        ObservadorEnvio captura = envio -> {
            disparos[0]++;
            ultimoEstado[0] = envio.getEstado();
        };

        // Crear envio y pre-adjuntar observer de captura ANTES de crearEnvio
        // → adjuntarObserversIfNone no sobreescribe (ve la lista no vacía)
        Envio envio = envioBase("ENV-E2E");
        envio.adjuntarObservador(captura);

        ctrl.crearEnvio(operador, envio, cobro);
        // crearEnvio: adjuntarObserversIfNone ve captura → no agrega standard ones

        // Avanzar estado CONFIRMADO → EN_TRANSITO (CU-04) → captura dispara
        ctrl.asignarHojaDeRuta(operador, "ENV-E2E", "T-001");
        assertar("CU-06 E2E: Observer disparado en CU-04 (EN_TRANSITO)", disparos[0] >= 1);

        // EN_TRANSITO → EN_REPARTO (CU-05) → captura dispara de nuevo
        ctrl.iniciarRecorrido(transportista, "ENV-E2E");
        assertar("CU-06 E2E: Observer disparado en CU-05 (EN_REPARTO)", disparos[0] >= 2);

        // EN_REPARTO → ENTREGADO (CU-06) → captura dispara de nuevo
        ctrl.registrarEntrega(transportista, "ENV-E2E");
        assertar("CU-06 E2E: Observer disparado en CU-06 (ENTREGADO)",   disparos[0] >= 3);
        assertar("CU-06 E2E: ultimo estado notificado es ENTREGADO",      "ENTREGADO".equals(ultimoEstado[0]));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Envio envioBase(String id) {
        return new Envio.EnvioBuilder(id,"Buenos Aires","Cordoba").peso(5.0).costo(300.0).build();
    }

    private static Cobro cobro(String id) {
        return new Cobro(id, 300.0, "PENDIENTE", LocalDateTime.now(), "TARJETA");
    }

    static void assertar(String nombre, boolean condicion) {
        total++;
        if (condicion) { ok++; System.out.println("  [OK] " + nombre); }
        else           {       System.out.println("  [FALLO] " + nombre); }
    }
}
