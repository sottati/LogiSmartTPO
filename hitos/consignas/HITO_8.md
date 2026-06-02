<!-- pagina 1 -->
Hito 8 del TPO: Patrones Estructurales I 
Adapter, Bridge y Composite en LogiSmart 
Duración: 90 minutos de práctica 
Objetivo: Aplicar Adapter, Bridge y Composite al diseño de LogiSmart 
Entregable: Documento Markdown + Código Java + Diagramas   
 
 
Contexto 
 
En los Hitos 6 y 7, aplicaste patrones creacionales para mejorar cómo se crean los objetos 
en LogiSmart: 
Hito 6: Singleton y Factory Method 
Hito 7: Abstract Factory, Builder y Prototype 
 
Ahora, en el Hito 8, vas a aplicar patrones estructurales para mejorar cómo se organizan y 
relacionan los objetos en tu sistema. 
 
Los tres patrones que aplicarás son: 
 
• Adapter: Para integrar múltiples proveedores logísticos externos con APIs diferentes 
• Bridge: Para separar tipos de reportes de motores de generación 
• Composite: Para modelar la jerarquía de centros de distribución 
 
 
 
 


<!-- pagina 2 -->
Actividades  
 
Actividad 1: Identificar Candidatos a Adapter  
Revisa tu diseño actual e identifica dónde necesitas integrar componentes externos con 
interfaces incompatibles. 
 
Preguntas a responder: 
¿Dónde hay APIs externas que tu sistema necesita usar? 
¿Tienen interfaces diferentes a la que espera tu código? 
¿Puedes cambiar las APIs externas? (Respuesta: No) 
¿Hay múltiples proveedores que necesitan ser tratados uniformemente? 
 
Candidatos Típicos en LogiSmart: 
Proveedores de Envío: DHL, FedEx, UPS (cada uno con su API) 
Proveedores de Pago: PayPal, Stripe, MercadoPago 
Proveedores de Notificación: SendGrid, Twilio, Firebase 
Proveedores de Mapas: Google Maps, OpenStreetMap 
Proveedores de Geolocalización: GPS, Celular, WiFi 
 
Entregable: Lista de 2-3 candidatos a Adapter con justificación. 
 
 
 
 
 


<!-- pagina 3 -->
Actividad 2: Implementar Adapter  
Implementa al menos 2 Adapters en tu sistema. 
 
Ejemplo 1: Adapter para Proveedores de Envío 
 
// Interfaz que tu sistema espera 
public interface ProveedorEnvio { 
    boolean crearEnvio(Envio envio); 
    String obtenerEstado(String numeroSeguimiento); 
    double calcularCosto(Envio envio); 
    String obtenerNombre(); 
} 
  
// APIs externas (simuladas) 
public class DHLAPI { 
    public String registrarPaquete(String origen, String destino, double peso) { 
        return "DHL-" + System.currentTimeMillis(); 
    } 
     
    public String consultarEstadoPaquete(String codigo) { 
        return "En tránsito"; 
    } 
     
    public double calcularTarifa(String origen, String destino, double peso) { 
        return peso * 15.0; 
    } 
} 
  
public class FedExAPI { 
    public int crearShipment(String from, String to, double weight) { 
        return (int) System.currentTimeMillis(); 
    } 


<!-- pagina 4 -->
     
    public String getShipmentStatus(int shipmentId) { 
        return "DELIVERED"; 
    } 
     
    public float getShippingRate(String from, String to, double weight) { 
        return (float) (weight * 12.0); 
    } 
} 
  
public class UPSConnector { 
    public boolean sendPackage(String sourceLocation, String destinationLocation, double 
packageWeight) { 
        return true; 
    } 
     
    public String trackPackage(String trackingCode) { 
        return "Out for delivery"; 
    } 
     
    public double estimateCost(String from, String to, double weight) { 
        return weight * 10.0; 
    } 
} 
  
// Adaptadores 
public class AdapterDHL implements ProveedorEnvio { 
    private DHLAPI dhlAPI = new DHLAPI(); 
     
    @Override 
    public boolean crearEnvio(Envio envio) { 
        String codigo = dhlAPI.registrarPaquete( 


<!-- pagina 5 -->
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
        envio.setNumeroSeguimiento(codigo); 
        return true; 
    } 
     
    @Override 
    public String obtenerEstado(String numeroSeguimiento) { 
        return dhlAPI.consultarEstadoPaquete(numeroSeguimiento); 
    } 
     
    @Override 
    public double calcularCosto(Envio envio) { 
        return dhlAPI.calcularTarifa( 
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
    } 
     
    @Override 
    public String obtenerNombre() { 
        return "DHL"; 
    } 
} 
  
public class AdapterFedEx implements ProveedorEnvio { 
    private FedExAPI fedexAPI = new FedExAPI(); 
     
    @Override 


<!-- pagina 6 -->
    public boolean crearEnvio(Envio envio) { 
        int shipmentId = fedexAPI.crearShipment( 
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
        envio.setNumeroSeguimiento("FDX-" + shipmentId); 
        return true; 
    } 
     
    @Override 
    public String obtenerEstado(String numeroSeguimiento) { 
        return 
fedexAPI.getShipmentStatus(Integer.parseInt(numeroSeguimiento.substring(4))); 
    } 
     
    @Override 
    public double calcularCosto(Envio envio) { 
        return fedexAPI.getShippingRate( 
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
    } 
     
    @Override 
    public String obtenerNombre() { 
        return "FedEx"; 
    } 
} 
  
public class AdapterUPS implements ProveedorEnvio { 


<!-- pagina 7 -->
    private UPSConnector upsConnector = new UPSConnector(); 
     
    @Override 
    public boolean crearEnvio(Envio envio) { 
        boolean resultado = upsConnector.sendPackage( 
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
        if (resultado) { 
            envio.setNumeroSeguimiento("UPS-" + System.currentTimeMillis()); 
        } 
        return resultado; 
    } 
     
    @Override 
    public String obtenerEstado(String numeroSeguimiento) { 
        return upsConnector.trackPackage(numeroSeguimiento); 
    } 
     
    @Override 
    public double calcularCosto(Envio envio) { 
        return upsConnector.estimateCost( 
            envio.getOrigen().getDireccion(), 
            envio.getDestino().getDireccion(), 
            envio.getPeso() 
        ); 
    } 
     
    @Override 
    public String obtenerNombre() { 
        return "UPS"; 


<!-- pagina 8 -->
    } 
} 
 
Ejemplo 2: Adapter para Proveedores de Pago 
 
// Interfaz que tu sistema espera 
public interface ProveedorPago { 
    boolean procesarPago(double monto, String referencia); 
    String obtenerEstado(String idTransaccion); 
    void reembolsar(String idTransaccion, double monto); 
} 
  
// APIs externas (simuladas) 
public class PayPalAPI { 
    public String crearTransaccion(double cantidad, String descripcion) { 
        return "PP-" + System.currentTimeMillis(); 
    } 
     
    public String consultarTransaccion(String id) { 
        return "COMPLETADA"; 
    } 
} 
  
public class StripeAPI { 
    public boolean charge(double amountInCents, String description) { 
        return true; 
    } 
     
    public String getChargeStatus(String chargeId) { 
        return "succeeded"; 
    } 
} 


<!-- pagina 9 -->
  
// Adaptadores 
public class AdapterPayPal implements ProveedorPago { 
    private PayPalAPI paypalAPI = new PayPalAPI(); 
     
    @Override 
    public boolean procesarPago(double monto, String referencia) { 
        String id = paypalAPI.crearTransaccion(monto, referencia); 
        return id != null; 
    } 
     
    @Override 
    public String obtenerEstado(String idTransaccion) { 
        return paypalAPI.consultarTransaccion(idTransaccion); 
    } 
     
    @Override 
    public void reembolsar(String idTransaccion, double monto) { 
        System.out.println("Reembolso en PayPal: " + monto); 
    } 
} 
  
public class AdapterStripe implements ProveedorPago { 
    private StripeAPI stripeAPI = new StripeAPI(); 
     
    @Override 
    public boolean procesarPago(double monto, String referencia) { 
        // Stripe trabaja en centavos 
        return stripeAPI.charge(monto * 100, referencia); 
    } 
     
    @Override 


<!-- pagina 10 -->
    public String obtenerEstado(String idTransaccion) { 
        String estado = stripeAPI.getChargeStatus(idTransaccion); 
        return estado.equals("succeeded") ? "COMPLETADA" : "PENDIENTE"; 
    } 
     
    @Override 
    public void reembolsar(String idTransaccion, double monto) { 
        System.out.println("Reembolso en Stripe: " + monto); 
    } 
} 
 
Preguntas: 
¿Por qué el adapter traduce entre interfaces? 
¿Qué pasa si agregas un nuevo proveedor? 
¿Cómo testearías un adapter? 
 
Entregable: Código Java de 2-3 Adapters implementados. 
 
 
 
 
 


<!-- pagina 11 -->
Actividad 3: Identificar Candidatos a Bridge 
Revisa tu diseño e identifica dónde hay dos dimensiones de variación que están acopladas. 
 
Preguntas a responder: 
¿Hay múltiples tipos de algo? (ej: reportes) 
¿Hay múltiples formas de implementarlo? (ej: formatos) 
¿Terminaría con muchas combinaciones? (ej: 4 tipos × 4 formatos = 16 clases) 
¿Necesito variar ambas dimensiones independientemente? 
 
Candidatos Típicos en LogiSmart: 
Reportes × Formatos: ReporteEnvios/PDF, ReporteEnvios/Excel, etc. 
Notificaciones × Canales: NotificacionUrgente/Email, NotificacionMarketing/SMS, etc. 
Vehículos × Motores: AutoGasolina, AutoDiésel, AutoEléctrico, MotoGasolina, etc. 
 
Entregable: Lista de 2-3 candidatos a Bridge con justificación. 
 
 
 
 
 


<!-- pagina 12 -->
Actividad 4: Implementar Bridge  
Implementa al menos 1 Bridge en tu sistema. 
 
Ejemplo: Bridge para Reportes 
 
// Implementación (Generador de formato) 
public interface GeneradorReporte { 
    String formatear(String contenido); 
    String obtenerExtension(); 
} 
  
public class GeneradorPDF implements GeneradorReporte { 
    @Override 
    public String formatear(String contenido) { 
        return "%PDF-1.4\n" + contenido + "\n%%EOF"; 
    } 
     
    @Override 
    public String obtenerExtension() { 
        return "pdf"; 
    } 
} 
  
public class GeneradorExcel implements GeneradorReporte { 
    @Override 
    public String formatear(String contenido) { 
        return "<?xml version=\"1.0\"?>\n<Workbook>\n" + contenido + "\n</Workbook>"; 
    } 
     
    @Override 
    public String obtenerExtension() { 
        return "xlsx"; 


<!-- pagina 13 -->
    } 
} 
  
public class GeneradorJSON implements GeneradorReporte { 
    @Override 
    public String formatear(String contenido) { 
        return "{\"reporte\": \"" + contenido.replace("\n", "\\n") + "\"}"; 
    } 
     
    @Override 
    public String obtenerExtension() { 
        return "json"; 
    } 
} 
  
public class GeneradorCSV implements GeneradorReporte { 
    @Override 
    public String formatear(String contenido) { 
        return contenido.replace("\n", "\r\n"); 
    } 
     
    @Override 
    public String obtenerExtension() { 
        return "csv"; 
    } 
} 
  
// Abstracción (Tipo de reporte) 
public abstract class Reporte { 
    protected GeneradorReporte generador; 
     
    public Reporte(GeneradorReporte generador) { 


<!-- pagina 14 -->
        this.generador = generador; 
    } 
     
    public abstract String generarContenido(); 
     
    public String generar() { 
        String contenido = generarContenido(); 
        return generador.formatear(contenido); 
    } 
     
    public void setGenerador(GeneradorReporte generador) { 
        this.generador = generador; 
    } 
     
    public String obtenerNombreArchivo(String nombre) { 
        return nombre + "." + generador.obtenerExtension(); 
    } 
} 
  
// Reportes concretos 
public class ReporteEnvios extends Reporte { 
    private List<Envio> envios; 
     
    public ReporteEnvios(GeneradorReporte generador, List<Envio> envios) { 
        super(generador); 
        this.envios = envios; 
    } 
     
    @Override 
    public String generarContenido() { 
        StringBuilder sb = new StringBuilder(); 
        sb.append("=== REPORTE DE ENVIOS ===\n"); 


<!-- pagina 15 -->
        sb.append("Total de envíos: ").append(envios.size()).append("\n\n"); 
         
        for (Envio envio : envios) { 
            sb.append("Número: ").append(envio.getNumeroSeguimiento()).append("\n"); 
            sb.append("Origen: ").append(envio.getOrigen().getDireccion()).append("\n"); 
            sb.append("Destino: ").append(envio.getDestino().getDireccion()).append("\n"); 
            sb.append("Estado: ").append(envio.getEstado()).append("\n"); 
            sb.append("Costo: $").append(envio.getCosto()).append("\n"); 
            sb.append("---\n"); 
        } 
         
        return sb.toString(); 
    } 
} 
  
public class ReporteIngresos extends Reporte { 
    private List<Envio> envios; 
     
    public ReporteIngresos(GeneradorReporte generador, List<Envio> envios) { 
        super(generador); 
        this.envios = envios; 
    } 
     
    @Override 
    public String generarContenido() { 
        StringBuilder sb = new StringBuilder(); 
        sb.append("=== REPORTE DE INGRESOS ===\n"); 
         
        double totalIngresos = 0; 
        for (Envio envio : envios) { 
            totalIngresos += envio.getCosto(); 
        } 


<!-- pagina 16 -->
         
        sb.append("Total de envíos: ").append(envios.size()).append("\n"); 
        sb.append("Ingresos totales: $").append(totalIngresos).append("\n"); 
        sb.append("Promedio por envío: $").append(totalIngresos / envios.size()).append("\n"); 
         
        return sb.toString(); 
    } 
} 
  
public class ReporteDesempenoProveedores extends Reporte { 
    private Map<String, Integer> desempenoProveedores; 
     
    public ReporteDesempenoProveedores(GeneradorReporte generador, Map<String, 
Integer> desempenoProveedores) { 
        super(generador); 
        this.desempenoProveedores = desempenoProveedores; 
    } 
     
    @Override 
    public String generarContenido() { 
        StringBuilder sb = new StringBuilder(); 
        sb.append("=== REPORTE DE DESEMPEÑO DE PROVEEDORES ===\n"); 
         
        for (Map.Entry<String, Integer> entry : desempenoProveedores.entrySet()) { 
            sb.append("Proveedor: ").append(entry.getKey()).append("\n"); 
            sb.append("Envíos completados: ").append(entry.getValue()).append("\n"); 
            sb.append("---\n"); 
        } 
         
        return sb.toString(); 
    } 
} 


<!-- pagina 17 -->
  
// Uso 
List<Envio> envios = obtenerEnvios(); 
  
// Mismo reporte en diferentes formatos 
Reporte reportePDF = new ReporteEnvios(new GeneradorPDF(), envios); 
Reporte reporteExcel = new ReporteEnvios(new GeneradorExcel(), envios); 
Reporte reporteJSON = new ReporteEnvios(new GeneradorJSON(), envios); 
  
System.out.println(reportePDF.generar()); 
System.out.println(reporteExcel.generar()); 
System.out.println(reporteJSON.generar()); 
  
// Cambiar generador en tiempo de ejecución 
reportePDF.setGenerador(new GeneradorCSV()); 
System.out.println(reportePDF.generar()); 
 
Preguntas: 
¿Por qué separar abstracción de implementación? 
¿Qué pasa si agregas un nuevo formato? 
¿Qué pasa si agregas un nuevo tipo de reporte? 
 
Entregable: Código Java de 1-2 Bridges implementados. 
 
 
 
 
 


<!-- pagina 18 -->
Actividad 5: Identificar Candidatos a Composite 
Revisa tu diseño e identifica dónde hay jerarquías parte-todo. 
 
Preguntas a responder: 
¿Hay estructuras jerárquicas? 
¿Necesitas operaciones que funcionen en toda la jerarquía? 
¿Hay hojas y contenedores? 
¿Necesitas tratar ambos uniformemente? 
 
Candidatos Típicos en LogiSmart: 
Centros de Distribución: Centro Nacional → Centro Regional → Centro Local → Punto de 
Entrega 
Rutas: Ruta Principal → Subrutas → Puntos de Entrega 
Permisos: Grupo de Permisos → Permisos Individuales 
Categorías de Productos: Categoría Principal → Subcategorías → Productos 
 
Entregable: Lista de 2-3 candidatos a Composite con justificación. 
 
 
 
 
 


<!-- pagina 19 -->
Actividad 6: Implementar Composite 
Implementa al menos 1 Composite en tu sistema. 
 
Ejemplo: Composite para Centros de Distribución 
 
// Componente base 
public abstract class CentroDistribucion { 
    protected String nombre; 
    protected String ubicacion; 
    protected String codigo; 
     
    public CentroDistribucion(String nombre, String ubicacion, String codigo) { 
        this.nombre = nombre; 
        this.ubicacion = ubicacion; 
        this.codigo = codigo; 
    } 
     
    public abstract int obtenerCapacidad(); 
    public abstract int obtenerOcupacion(); 
     
    public double obtenerPorcentajeOcupacion() { 
        return (double) obtenerOcupacion() / obtenerCapacidad() * 100; 
    } 
     
    public abstract void mostrar(int profundidad); 
     
    public String getNombre() { return nombre; } 
    public String getUbicacion() { return ubicacion; } 
    public String getCodigo() { return codigo; } 
} 
  
// Hoja: Punto de Entrega 


<!-- pagina 20 -->
public class PuntoEntrega extends CentroDistribucion { 
    private int capacidad; 
    private int ocupacion; 
    private List<Envio> enviosAlmacenados; 
     
    public PuntoEntrega(String nombre, String ubicacion, String codigo, int capacidad) { 
        super(nombre, ubicacion, codigo); 
        this.capacidad = capacidad; 
        this.ocupacion = 0; 
        this.enviosAlmacenados = new ArrayList<>(); 
    } 
     
    public void agregarEnvio(Envio envio) { 
        if (ocupacion < capacidad) { 
            enviosAlmacenados.add(envio); 
            ocupacion++; 
        } else { 
            throw new IllegalStateException("Capacidad excedida"); 
        } 
    } 
     
    public void removerEnvio(Envio envio) { 
        if (enviosAlmacenados.remove(envio)) { 
            ocupacion--; 
        } 
    } 
     
    @Override 
    public int obtenerCapacidad() { 
        return capacidad; 
    } 
     


<!-- pagina 21 -->
    @Override 
    public int obtenerOcupacion() { 
        return ocupacion; 
    } 
     
    @Override 
    public void mostrar(int profundidad) { 
        System.out.println("  ".repeat(profundidad) +  
            "📍 " + nombre + " (" + ocupacion + "/" + capacidad + ")"); 
    } 
} 
  
// Composite: Centro Regional/Local 
public class CentroRegional extends CentroDistribucion { 
    private List<CentroDistribucion> subcentros; 
     
    public CentroRegional(String nombre, String ubicacion, String codigo) { 
        super(nombre, ubicacion, codigo); 
        this.subcentros = new ArrayList<>(); 
    } 
     
    public void agregar(CentroDistribucion centro) { 
        subcentros.add(centro); 
    } 
     
    public void remover(CentroDistribucion centro) { 
        subcentros.remove(centro); 
    } 
     
    public List<CentroDistribucion> obtenerSubcentros() { 
        return new ArrayList<>(subcentros); 


<!-- pagina 22 -->
    } 
     
    @Override 
    public int obtenerCapacidad() { 
        int totalCapacidad = 0; 
        for (CentroDistribucion centro : subcentros) { 
            totalCapacidad += centro.obtenerCapacidad(); 
        } 
        return totalCapacidad; 
    } 
     
    @Override 
    public int obtenerOcupacion() { 
        int totalOcupacion = 0; 
        for (CentroDistribucion centro : subcentros) { 
            totalOcupacion += centro.obtenerOcupacion(); 
        } 
        return totalOcupacion; 
    } 
     
    @Override 
    public void mostrar(int profundidad) { 
        System.out.println("  ".repeat(profundidad) +  
            "🏢 " + nombre + " (" + obtenerOcupacion() + "/" + obtenerCapacidad() + ")"); 
        for (CentroDistribucion centro : subcentros) { 
            centro.mostrar(profundidad + 1); 
        } 
    } 
} 
  
// Uso 


<!-- pagina 23 -->
CentroRegional argentina = new CentroRegional("Centro Argentina", "Buenos Aires", "ARG-
001"); 
  
CentroRegional caba = new CentroRegional("Centro CABA", "CABA", "CABA-001"); 
CentroRegional sanTelmoLocal = new CentroRegional("Centro San Telmo", "San Telmo", 
"ST-001"); 
  
PuntoEntrega sanTelmo1 = new PuntoEntrega("Punto San Telmo 1", "San Telmo", "ST-P1", 
100); 
PuntoEntrega sanTelmo2 = new PuntoEntrega("Punto San Telmo 2", "San Telmo", "ST-P2", 
80); 
  
sanTelmoLocal.agregar(sanTelmo1); 
sanTelmoLocal.agregar(sanTelmo2); 
caba.agregar(sanTelmoLocal); 
argentina.agregar(caba); 
  
// Calcular capacidad total recursivamente 
System.out.println("Capacidad total: " + argentina.obtenerCapacidad()); 
  
// Mostrar estructura 
argentina.mostrar(0); 
 
Preguntas: 
¿Por qué los cálculos son recursivos? 
¿Qué pasa si agregas un nuevo nivel? 
¿Cómo testearías esto? 
 
Entregable: Código Java de 1-2 Composites implementados. 
 
 
 


<!-- pagina 24 -->
Actividad 7: Integración Completa 
Integra los tres patrones en un servicio unificado. 
 
public class ServicioLogisticaUnificado { 
    private Map<String, ProveedorEnvio> proveedoresEnvio; 
    private Map<String, ProveedorPago> proveedoresPago; 
    private CentroDistribucion centroDistribucion; 
    private List<Envio> enviosRegistrados; 
     
    public ServicioLogisticaUnificado(CentroDistribucion centroDistribucion) { 
        this.centroDistribucion = centroDistribucion; 
        this.enviosRegistrados = new ArrayList<>(); 
        this.proveedoresEnvio = new HashMap<>(); 
        this.proveedoresPago = new HashMap<>(); 
         
        // Registrar adaptadores de envío 
        proveedoresEnvio.put("DHL", new AdapterDHL()); 
        proveedoresEnvio.put("FedEx", new AdapterFedEx()); 
        proveedoresEnvio.put("UPS", new AdapterUPS()); 
         
        // Registrar adaptadores de pago 
        proveedoresPago.put("PayPal", new AdapterPayPal()); 
        proveedoresPago.put("Stripe", new AdapterStripe()); 
    } 
     
    public boolean crearEnvio(String nombreProveedor, Envio envio) { 
        ProveedorEnvio proveedor = proveedoresEnvio.get(nombreProveedor); 
        if (proveedor == null) { 
            throw new IllegalArgumentException("Proveedor no soportado: " + 
nombreProveedor); 
        } 
         


<!-- pagina 25 -->
        if (proveedor.crearEnvio(envio)) { 
            enviosRegistrados.add(envio); 
            return true; 
        } 
        return false; 
    } 
     
    public boolean procesarPago(String nombreProveedor, double monto, String referencia) { 
        ProveedorPago proveedor = proveedoresPago.get(nombreProveedor); 
        if (proveedor == null) { 
            throw new IllegalArgumentException("Proveedor de pago no soportado: " + 
nombreProveedor); 
        } 
         
        return proveedor.procesarPago(monto, referencia); 
    } 
     
    public Reporte generarReporte(String tipoReporte, String formato) { 
        GeneradorReporte generador = obtenerGenerador(formato); 
         
        switch (tipoReporte.toLowerCase()) { 
            case "envios": 
                return new ReporteEnvios(generador, enviosRegistrados); 
            case "ingresos": 
                return new ReporteIngresos(generador, enviosRegistrados); 
            case "desempeño": 
                Map<String, Integer> desempeño = new HashMap<>(); 
                for (String proveedor : proveedoresEnvio.keySet()) { 
                    desempeño.put(proveedor, 0); 
                } 
                return new ReporteDesempenoProveedores(generador, desempeño); 
            default: 


<!-- pagina 26 -->
                throw new IllegalArgumentException("Tipo de reporte no soportado: " + 
tipoReporte); 
        } 
    } 
     
    private GeneradorReporte obtenerGenerador(String formato) { 
        switch (formato.toLowerCase()) { 
            case "pdf": 
                return new GeneradorPDF(); 
            case "excel": 
                return new GeneradorExcel(); 
            case "json": 
                return new GeneradorJSON(); 
            case "csv": 
                return new GeneradorCSV(); 
            default: 
                throw new IllegalArgumentException("Formato no soportado: " + formato); 
        } 
    } 
     
    public CentroDistribucion obtenerCentroDistribucion() { 
        return centroDistribucion; 
    } 
} 
 
Entregable: Código Java del servicio unificado. 
 
 
 
 
 


<!-- pagina 27 -->
3. Entregables 
3.1 Documento Markdown 
Crea un documento Hito_8_Patrones_Estructurales_I.md con: 
 
Análisis de Candidatos a Adapter 
Lista de integraciones externas 
Justificación de por qué usar Adapter 
Problemas que resuelve 
Implementación de Adapter 
Código Java de cada adapter 
Explicación de la traducción entre interfaces 
Ventajas y desventajas 
Análisis de Candidatos a Bridge 
Lista de dos dimensiones de variación 
Justificación de por qué usar Bridge 
Problemas que resuelve (explosión de clases) 
Implementación de Bridge 
Código Java de cada bridge 
Explicación de separación de abstracción e implementación 
Ventajas y desventajas 
Análisis de Candidatos a Composite 
Lista de estructuras jerárquicas 
Justificación de por qué usar Composite 
Problemas que resuelve 
Implementación de Composite 
Código Java de cada composite 
Explicación de cálculos recursivos 
Ventajas y desventajas 
Integración en ServicioLogisticaUnificado 
Cómo se usan los tres patrones juntos 
Casos de uso completos 
Conclusiones 


<!-- pagina 28 -->
Cómo cambió el diseño 
Beneficios de los patrones 
Próximos pasos 
 
3.2 Código Java 
Entrega los siguientes archivos: 
 
Adapters: 
AdapterDHL.java 
AdapterFedEx.java 
AdapterUPS.java 
AdapterPayPal.java 
AdapterStripe.java 
 
Bridges: 
GeneradorReporte.java (interfaz) 
GeneradorPDF.java 
GeneradorExcel.java 
GeneradorJSON.java 
GeneradorCSV.java 
Reporte.java (abstracta) 
ReporteEnvios.java 
ReporteIngresos.java 
ReporteDesempenoProveedores.java 
 
Composites: 
CentroDistribucion.java (abstracta) 
CentroRegional.java 
PuntoEntrega.java 
 
Servicio Unificado: 
ServicioLogisticaUnificado.java 


<!-- pagina 29 -->
 
3.3 Diagramas 
Diagrama de Clases - Adapter 
Mostrando interfaces, adaptadores y APIs externas 
Diagrama de Clases - Bridge 
Mostrando abstracción, implementación y concretos 
Diagrama de Clases - Composite 
Mostrando componente base, hoja y composite 
Diagrama de Secuencia - Caso de Uso Completo 
Mostrando cómo se integran los tres patrones 
 
 
 
 
 


<!-- pagina 30 -->
4. Criterios de Evaluación 
Criterio 
Excelente (90-
100) 
Bueno (80-89) 
Aceptable (70-
79) 
Insuficiente 
(<70) 
Identificación de 
Adapter 
3 integraciones 
identificadas 
correctamente 
2 
integraciones 
1 integración 
No 
identificadas 
Implementación 
de Adapter 
3+ adaptadores, 
traducción 
correcta 
2 adaptadores 
1 adaptador 
No 
implementado 
Identificación de 
Bridge 
2-3 dimensiones 
de variación 
identificadas 
2 dimensiones 1 dimensión 
No 
identificadas 
Implementación 
de Bridge 
3+ formatos, 3+ 
tipos de reportes 
2 formatos, 2 
tipos 
1 formato, 1 
tipo 
No 
implementado 
Identificación de 
Composite 
2-3 estructuras 
jerárquicas 
identificadas 
2 estructuras 
1 estructura 
No 
identificadas 
Implementación 
de Composite 
Jerarquía 4+ 
niveles, cálculos 
recursivos 
correctos 
3 niveles 
2 niveles 
No 
implementado 
Integración 
Los 3 patrones 
integrados sin 
problemas 
2 patrones 
integrados 
1 patrón 
integrado 
Sin integración 


<!-- pagina 31 -->
Criterio 
Excelente (90-
100) 
Bueno (80-89) 
Aceptable (70-
79) 
Insuficiente 
(<70) 
Código 
Limpio, bien 
estructurado, 
sigue 
convenciones 
Java 
Bien 
estructurado 
Funciona pero 
desorganizado 
Difícil de 
entender 
Documentación 
Completa, clara, 
diagramas 
correctos 
Buena, 
diagramas 
correctos 
Básica, 
diagramas 
incompletos 
Incompleta 
Casos de Prueba 
10+ casos, todos 
pasan 
8-9 casos 
5-7 casos 
<5 casos 
 
 
5. Preguntas Guía 
Mientras trabajas en el Hito 8: 
 
Adapter: ¿Cómo traduzco entre interfaces incompatibles? ¿Qué pasa si agrego un nuevo 
proveedor? 
Bridge: ¿Cómo evito la explosión de combinaciones? ¿Puedo cambiar el generador en 
tiempo de ejecución? 
Composite: ¿Cómo calculo recursivamente en toda la jerarquía? ¿Qué pasa si agrego un 
nuevo nivel? 
Integración: ¿Cómo funcionan los tres patrones juntos? ¿Dónde se usan en el servicio 
unificado? 
Escalabilidad: ¿Es fácil agregar un nuevo proveedor? ¿Un nuevo formato? ¿Un nuevo nivel? 
 
 
 
 
 


<!-- pagina 32 -->
6. Recursos 
Presentación: clase_9_presentacion_v1.html 
Documentación: Refactoring Guru - Structural Patterns 
Libro: Design Patterns: Elements of Reusable Object-Oriented Software (Gang of Four) 
 
 
 
 
