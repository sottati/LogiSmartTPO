package tests.infraestructura;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.adapter.envio.AdapterDHL;
import com.logismart.infraestructura.adapter.envio.AdapterFedEx;
import com.logismart.infraestructura.adapter.envio.AdapterUPS;
import com.logismart.infraestructura.adapter.envio.ProveedorEnvio;
import com.logismart.infraestructura.adapter.pago.AdapterPayPal;
import com.logismart.infraestructura.adapter.pago.AdapterStripe;
import com.logismart.infraestructura.adapter.pago.ProveedorPago;
import com.logismart.infraestructura.bridge.GeneradorJSON;
import com.logismart.infraestructura.bridge.GeneradorPDF;
import com.logismart.infraestructura.bridge.ReporteEnvios;
import com.logismart.infraestructura.bridge.ReporteIngresos;
import com.logismart.infraestructura.decorator.ComponenteEnvio;
import com.logismart.infraestructura.decorator.DecoradorPrioritario;
import com.logismart.infraestructura.decorator.DecoradorRastreoGPS;
import com.logismart.infraestructura.decorator.DecoradorSeguro;
import com.logismart.infraestructura.decorator.EnvioBasico;
import com.logismart.infraestructura.estrategia.EstrategiaDistancia;
import com.logismart.infraestructura.estrategia.EstrategiaHibrida;
import com.logismart.infraestructura.estrategia.EstrategiaPeso;
import com.logismart.infraestructura.estrategia.EstrategiaUrgencia;
import com.logismart.infraestructura.fabrica.CalculadorCostos;
import com.logismart.infraestructura.fabrica.LogiSmartFactory;
import com.logismart.infraestructura.fabrica.LogiSmartFactoryArgentina;
import com.logismart.infraestructura.fabrica.LogiSmartFactoryBrasil;
import com.logismart.infraestructura.fabrica.UsuarioFactory;
import com.logismart.infraestructura.flyweight.FabricaUbicaciones;
import com.logismart.infraestructura.flyweight.Ubicacion;
import com.logismart.infraestructura.singleton.ConexionBD;
import com.logismart.infraestructura.singleton.Logger;
import java.util.Arrays;
import java.util.List;

public class TestsInfraestructura {

    private static int total = 0;
    private static int ok    = 0;

    public static void main(String[] args) {
        testAbstractFactory();
        testFactoryMethod();
        testEstrategias();
        testAdaptersEnvio();
        testAdaptersPago();
        testBridge();
        testDecorator();
        testFlyweight();
        testSingleton();

        System.out.println("\n========================================");
        System.out.println(total + " casos ejecutados, " + ok + " OK");
        System.out.println("========================================");
    }

    // ── Abstract Factory ─────────────────────────────────────────────────────

    static void testAbstractFactory() {
        System.out.println("\n--- Abstract Factory ---");
        LogiSmartFactory ar = new LogiSmartFactoryArgentina();
        LogiSmartFactory br = new LogiSmartFactoryBrasil();

        assertar("Factory AR crea Auto", ar.crearVehiculo().getClass().getSimpleName().equals("Auto"));
        assertar("Factory BR crea Moto", br.crearVehiculo().getClass().getSimpleName().equals("Moto"));

        CalculadorCostos calcAR = ar.crearCalculadorCostos();
        CalculadorCostos calcBR = br.crearCalculadorCostos();
        // AR: (100*1.0 + 10*5.0)*1.21 = 181.5
        assertar("Factory AR calcula con IVA 21%",  Math.abs(calcAR.calcular(100.0, 10.0) - 181.5) < 0.01);
        // BR: (100*0.9 + 10*4.5)*1.12 = 151.2
        assertar("Factory BR calcula con ICMS 12%", Math.abs(calcBR.calcular(100.0, 10.0) - 151.2) < 0.01);
    }

    // ── Factory Method (UsuarioFactory) ──────────────────────────────────────

    static void testFactoryMethod() {
        System.out.println("\n--- Factory Method (UsuarioFactory) ---");
        assertar("UsuarioFactory crea Operador",
                UsuarioFactory.crear("operador","op1","op1@t.com").getClass().getSimpleName().equals("OperadorLogistico"));
        assertar("UsuarioFactory crea Cliente",
                UsuarioFactory.crear("cliente","cli","cli@t.com").getClass().getSimpleName().equals("ClienteFinal"));
        boolean ex = false;
        try { UsuarioFactory.crear("DESCONOCIDO","x","x@x.com"); } catch (IllegalArgumentException e) { ex = true; }
        assertar("UsuarioFactory tipo desconocido lanza excepcion", ex);
    }

    // ── Strategy ─────────────────────────────────────────────────────────────

    static void testEstrategias() {
        System.out.println("\n--- Strategy ---");
        Envio env = new Envio.EnvioBuilder("E-S01", "A", "B").peso(10.0).build();

        env.establecerEstrategia(new EstrategiaDistancia(1.5));
        assertar("EstrategiaDistancia: peso*500*tarifa", Math.abs(env.calcularCosto() - 10.0*500.0*1.5) < 0.01);

        env.establecerEstrategia(new EstrategiaPeso(5.0));
        assertar("EstrategiaPeso: peso*tarifa", Math.abs(env.calcularCosto() - 50.0) < 0.01);

        env.establecerEstrategia(new EstrategiaUrgencia());
        assertar("EstrategiaUrgencia: peso*5*2.5", Math.abs(env.calcularCosto() - 10.0*5.0*2.5) < 0.01);

        env.establecerEstrategia(new EstrategiaHibrida(2.0, 3.0));
        // Formula: peso*500*tarifaKm + peso*tarifaKg = 10*500*2 + 10*3 = 10030
        assertar("EstrategiaHibrida: peso*500*tarKm + peso*tarKg",
                Math.abs(env.calcularCosto() - (10.0*500.0*2.0 + 10.0*3.0)) < 0.01);
    }

    // ── Adapter Envío ─────────────────────────────────────────────────────────

    static void testAdaptersEnvio() {
        System.out.println("\n--- Adapter (Envio) ---");
        Envio env = new Envio.EnvioBuilder("E-A01", "Rosario", "Mendoza").peso(8.0).build();

        assertar("AdapterDHL tracking DHL-",   new AdapterDHL().crearEnvio(env).startsWith("DHL-"));
        assertar("AdapterFedEx tracking FX-",  new AdapterFedEx().crearEnvio(env).startsWith("FX-"));
        assertar("AdapterUPS tracking UPS-",   new AdapterUPS().crearEnvio(env).startsWith("UPS-"));
    }

    // ── Adapter Pago ──────────────────────────────────────────────────────────

    static void testAdaptersPago() {
        System.out.println("\n--- Adapter (Pago) ---");
        assertar("AdapterPayPal txn PP-TXN", new AdapterPayPal().procesarPago(1500.0,"REF-001").startsWith("PP-TXN"));
        assertar("AdapterStripe txn ch_",    new AdapterStripe().procesarPago(1500.0,"REF-001").startsWith("ch_"));
    }

    // ── Bridge ────────────────────────────────────────────────────────────────

    static void testBridge() {
        System.out.println("\n--- Bridge ---");
        List<Envio> envios = Arrays.asList(new Envio.EnvioBuilder("E-B01","BsAs","Mdza").peso(3.0).build());

        String pdf  = new ReporteEnvios(new GeneradorPDF(),  envios).generar();
        String json = new ReporteEnvios(new GeneradorJSON(), envios).generar();
        assertar("Bridge: PDF contiene header",      pdf.startsWith("%PDF-1.4"));
        assertar("Bridge: JSON contiene llave",      json.contains("\"reporte\""));

        Envio eRI = new Envio.EnvioBuilder("E-RI","A","B").peso(1.0).costo(100.0).build();
        assertar("Bridge: ReporteIngresos JSON",
                new ReporteIngresos(new GeneradorJSON(), Arrays.asList(eRI)).generar().contains("\"reporte\""));
    }

    // ── Decorator ─────────────────────────────────────────────────────────────

    static void testDecorator() {
        System.out.println("\n--- Decorator ---");
        ComponenteEnvio base = new EnvioBasico("A","B",10.0); // costo=100
        assertar("Decorator Seguro x1.15",     Math.abs(new DecoradorSeguro(base).obtenerCosto() - 115.0) < 0.01);
        assertar("Decorator Prioritario x1.30",Math.abs(new DecoradorPrioritario(base).obtenerCosto() - 130.0) < 0.01);
        assertar("Decorator GPS +50",          Math.abs(new DecoradorRastreoGPS(base).obtenerCosto() - 150.0) < 0.01);
        // Combo: Prio(100)*1.30=130, Seguro(130)*1.15=149.5
        assertar("Decorator Prio+Seguro",
                Math.abs(new DecoradorSeguro(new DecoradorPrioritario(base)).obtenerCosto() - 149.5) < 0.01);
    }

    // ── Flyweight ─────────────────────────────────────────────────────────────

    static void testFlyweight() {
        System.out.println("\n--- Flyweight ---");
        FabricaUbicaciones.limpiar();
        Ubicacion u1 = FabricaUbicaciones.obtener("Buenos Aires","CABA","1000");
        Ubicacion u2 = FabricaUbicaciones.obtener("Buenos Aires","CABA","1000");
        Ubicacion u3 = FabricaUbicaciones.obtener("Cordoba","Cordoba","5000");

        assertar("Flyweight: misma clave → misma instancia",     u1 == u2);
        assertar("Flyweight: distinta clave → distinta instancia",u1 != u3);
        assertar("Flyweight: cache tiene 2 instancias",          FabricaUbicaciones.totalInstancias() == 2);
    }

    // ── Singleton ─────────────────────────────────────────────────────────────

    static void testSingleton() {
        System.out.println("\n--- Singleton ---");
        assertar("ConexionBD: misma instancia", ConexionBD.obtenerInstancia() == ConexionBD.obtenerInstancia());
        assertar("Logger: misma instancia",     Logger.obtenerInstancia()    == Logger.obtenerInstancia());
    }

    // ── Utilidad ──────────────────────────────────────────────────────────────

    static void assertar(String nombre, boolean condicion) {
        total++;
        if (condicion) { ok++; System.out.println("  [OK] " + nombre); }
        else           {       System.out.println("  [FALLO] " + nombre); }
    }
}
