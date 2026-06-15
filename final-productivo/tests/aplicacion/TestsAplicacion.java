package tests.aplicacion;

import com.logismart.aplicacion.facade.FacadeProveedoresExternos;
import com.logismart.aplicacion.facade.FachadaReportes;
import com.logismart.aplicacion.PedidoEcommerce;
import com.logismart.aplicacion.ServicioImportacion;
import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;
import com.logismart.aplicacion.cadena.CadenaValidadores;
import com.logismart.aplicacion.cadena.ContextoValidacion;
import com.logismart.aplicacion.cadena.SistemaCapacidad;
import com.logismart.aplicacion.cadena.SistemaInventario;
import com.logismart.persistencia.EnvioMapperMemoria;
import com.logismart.persistencia.ProxyRepositorioEnvio;
import com.logismart.persistencia.RepositorioEnvioMemoria;
import com.logismart.persistencia.UnitOfWork;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestsAplicacion {

    private static int total = 0;
    private static int ok    = 0;

    public static void main(String[] args) {
        testCadena();
        testFacadeProveedores();
        testFachadaReportes();
        testServicioImportacion();

        System.out.println("\n========================================");
        System.out.println(total + " casos ejecutados, " + ok + " OK");
        System.out.println("========================================");
    }

    // ── Chain of Responsibility (capa aplicacion) ─────────────────────────────

    static void testCadena() {
        System.out.println("\n--- Chain of Responsibility ---");
        CadenaValidadores cadena = new CadenaValidadores(new SistemaInventario(), new SistemaCapacidad(10_000));
        Cobro cobro = new Cobro("CB-01", 500.0, "PENDIENTE", LocalDateTime.now(), "TARJETA");

        Envio valido = new Envio.EnvioBuilder("E-C01","Buenos Aires","Cordoba").peso(5.0).costo(500.0).build();
        assertar("Cadena: envio valido pasa", cadena.validar(new ContextoValidacion(valido, cobro)));

        Envio sinOrigen = new Envio.EnvioBuilder("E-C02","","Cordoba").peso(5.0).costo(200.0).build();
        assertar("Cadena: origen vacio rechazado", !cadena.validar(new ContextoValidacion(sinOrigen, cobro)));

        Envio restringido = new Envio.EnvioBuilder("E-C03","BsAs","Zona Restringido Norte").peso(5.0).costo(300.0).build();
        assertar("Cadena: destino restringido rechazado", !cadena.validar(new ContextoValidacion(restringido, cobro)));
    }

    // ── FacadeProveedoresExternos (envío y pago) ──────────────────────────────

    static void testFacadeProveedores() {
        System.out.println("\n--- Facade (FacadeProveedoresExternos) ---");
        FacadeProveedoresExternos facade = new FacadeProveedoresExternos();
        Envio env = new Envio.EnvioBuilder("FA-01","BsAs","Rosario").peso(3.0).build();

        assertar("Facade: DHL tracking empieza DHL-",   facade.registrarEnvioExterno(env,"DHL").startsWith("DHL-"));
        assertar("Facade: FedEx tracking empieza FX-",  facade.registrarEnvioExterno(env,"FEDEX").startsWith("FX-"));
        assertar("Facade: UPS tracking empieza UPS-",   facade.registrarEnvioExterno(env,"UPS").startsWith("UPS-"));
        assertar("Facade: PayPal txn PP-TXN",           facade.procesarPago(500.0,"REF-PAY","PAYPAL").startsWith("PP-TXN"));
        assertar("Facade: Stripe txn ch_",              facade.procesarPago(500.0,"REF-STR","STRIPE").startsWith("ch_"));
    }

    // ── FachadaReportes (Bridge) ──────────────────────────────────────────────

    static void testFachadaReportes() {
        System.out.println("\n--- FachadaReportes (Bridge) ---");
        FachadaReportes fachada = new FachadaReportes();
        Envio env = new Envio.EnvioBuilder("FR-01","BsAs","Cordoba").peso(2.0).costo(150.0).build();
        List<Envio> envios = Arrays.asList(env);

        String json = fachada.generarReporte(envios, "ENVIOS", "JSON");
        assertar("FachadaReportes: ReporteEnvios JSON contiene llave", json.contains("\"reporte\""));

        String pdf = fachada.generarReporte(envios, "INGRESOS", "PDF");
        assertar("FachadaReportes: ReporteIngresos PDF header",        pdf.startsWith("%PDF-1.4"));

        String csv = fachada.generarReporte(envios, "DESEMPENO", "CSV");
        assertar("FachadaReportes: ReporteDesempeno CSV contiene comillas", csv.contains("\""));

        String xlsx = fachada.generarReporte(envios, "ENVIOS", "EXCEL");
        assertar("FachadaReportes: ReporteEnvios Excel contiene Workbook",  xlsx.contains("Workbook"));
    }

    // ── CU-01 ServicioImportacion (Prototype) ─────────────────────────────────

    static void testServicioImportacion() {
        System.out.println("\n--- CU-01: ServicioImportacion (Prototype) ---");

        CadenaValidadores       cadena  = new CadenaValidadores(new SistemaInventario(), new SistemaCapacidad(10_000));
        RepositorioEnvioMemoria base    = new RepositorioEnvioMemoria();
        ProxyRepositorioEnvio   proxy   = new ProxyRepositorioEnvio(base);
        EnvioMapperMemoria      mapper  = new EnvioMapperMemoria();
        UnitOfWork              uow     = new UnitOfWork(proxy, mapper);
        ServicioImportacion     servicio = new ServicioImportacion(cadena, uow);

        Cobro cobro     = new Cobro("CB-01",1000.0,"PENDIENTE", LocalDateTime.now(),"TARJETA");
        Envio prototipo = new Envio.EnvioBuilder("PROTO","BsAs","___").peso(4.0).costo(300.0).build();

        List<PedidoEcommerce> pedidos = Arrays.asList(
                new PedidoEcommerce("P-001","Cordoba","Remera talle M"),
                new PedidoEcommerce("P-002","Rosario","Zapatillas"),
                new PedidoEcommerce("P-003","Mendoza","Mochila")
        );

        List<String> importados = servicio.importarPedidos(prototipo, pedidos, cobro);
        assertar("CU-01: 3 pedidos validos → 3 importados", importados.size() == 3);
        assertar("CU-01: ids de pedidos presentes", importados.contains("P-001") && importados.contains("P-003"));

        // Prototype: prototipo no fue alterado
        assertar("CU-01: prototipo mantiene id PROTO",  "PROTO".equals(prototipo.getId()));
        assertar("CU-01: prototipo mantiene destino ___","___".equals(prototipo.getDestino()));

        // Pedido con destino inválido (rechazado por CadenaValidadores)
        List<PedidoEcommerce> conRestringido = Arrays.asList(
                new PedidoEcommerce("P-OK","Tucuman","Mate"),
                new PedidoEcommerce("P-BAD","Zona Restringido Norte","Algo")
        );
        List<String> resultado = servicio.importarPedidos(prototipo, conRestringido, cobro);
        assertar("CU-01: destino restringido rechazado → solo 1 importado", resultado.size() == 1);
        assertar("CU-01: P-OK importado", resultado.contains("P-OK"));
        assertar("CU-01: P-BAD excluido", !resultado.contains("P-BAD"));

        // Lista vacía → retorna vacío
        List<String> vaciosResult = servicio.importarPedidos(prototipo, Collections.emptyList(), cobro);
        assertar("CU-01: lista vacia → importados vacio", vaciosResult.isEmpty());
    }

    // ── Utilidad ──────────────────────────────────────────────────────────────

    static void assertar(String nombre, boolean condicion) {
        total++;
        if (condicion) { ok++; System.out.println("  [OK] " + nombre); }
        else           {       System.out.println("  [FALLO] " + nombre); }
    }
}
