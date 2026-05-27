# Clase 13: Patrones de Comportamiento III

Fuente: `clase_13.html` (contenido de `main-content`).

# Clase 13: Patrones de Comportamiento III

## State, Strategy, Template Method, Visitor

## Objetivo de la Clase

Estudiar patrones que definen cómo los objetos cambian comportamiento y se procesan:

- Cambiar comportamiento según el estado interno

- Seleccionar algoritmo en tiempo de ejecución

- Definir esqueleto de algoritmo en clase base

- Procesar elementos de una estructura sin cambiarla

## Diferencia con Clases Anteriores

| Clase 11 | Clase 12 | Clase 13 |

| Chain of Responsibility | Iterator | State |

| Command | Mediator | Strategy |

| Interpreter | Memento | Template Method |

| - | Observer | Visitor |

## Duración de la Clase

- Introducción: 5 minutos

- State: 20 minutos

- Strategy: 20 minutos

- Template Method: 15 minutos

- Visitor: 15 minutos

- Comparación: 5 minutos

- Ejercicios de Selección: 10 minutos

- Casos Reales: 5 minutos

- Total: 90 minutos

### Pregunta Central

¿Cómo cambio comportamiento según estado? ¿Cómo selecciono algoritmo? ¿Cómo defino esqueleto? ¿Cómo proceso estructuras?

# State Pattern

## Problema: Comportamiento Dependiente del Estado

LogiSmart tiene envíos con múltiples estados:

- CONFIRMADO: puede ser validado o cancelado

- EN_TRANSITO: puede ser entregado o retenido

- ENTREGADO: puede ser reclamado

- CANCELADO: no puede hacer nada

Problema: ¿Cómo cambiar comportamiento según estado sin if/else anidados?

Sin State (Acoplamiento Alto):
    if (estado == CONFIRMADO) { validar(); }
    else if (estado == EN_TRANSITO) { entregar(); }
    else if (estado == ENTREGADO) { reclamar(); }
    else if (estado == CANCELADO) { error(); }

Con State (Desacoplamiento):
    estado.validar();  // Comportamiento según estado

## Solución: State

Permite que un objeto altere su comportamiento cuando su estado interno cambia.

## Implementación

### Paso 1: Interfaz State

```
public interface EstadoEnvio {
    void validar(Envio envio);
    void entregar(Envio envio);
    void cancelar(Envio envio);
    String obtenerNombre();
}
```

### Paso 2: Estados Concretos

```
// Estado: CONFIRMADO
public class EstadoConfirmado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("✓ Envío validado");
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("✗ No se puede entregar, debe estar en tránsito");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("✓ Envío cancelado");
        envio.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public String obtenerNombre() {
        return "CONFIRMADO";
    }
}

// Estado: EN_TRANSITO
public class EstadoEnTransito implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("✗ Ya está validado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("✓ Envío entregado");
        envio.cambiarEstado(new EstadoEntregado());
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("✗ No se puede cancelar, está en tránsito");
    }

    @Override
    public String obtenerNombre() {
        return "EN_TRANSITO";
    }
}

// Estado: ENTREGADO
public class EstadoEntregado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("✗ Ya fue entregado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("✗ Ya está entregado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("✗ No se puede cancelar, ya fue entregado");
    }

    @Override
    public String obtenerNombre() {
        return "ENTREGADO";
    }
}

// Estado: CANCELADO
public class EstadoCancelado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("✗ Envío cancelado, no se puede validar");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("✗ Envío cancelado, no se puede entregar");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("✗ Ya está cancelado");
    }

    @Override
    public String obtenerNombre() {
        return "CANCELADO";
    }
}
```

### Paso 3: Contexto

```
public class Envio {
    private String id;
    private EstadoEnvio estado;

    public Envio(String id) {
        this.id = id;
        this.estado = new EstadoConfirmado();
    }

    public void cambiarEstado(EstadoEnvio nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("[Envio] Estado cambiado a: " + nuevoEstado.obtenerNombre());
    }

    public void validar() {
        estado.validar(this);
    }

    public void entregar() {
        estado.entregar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public String obtenerEstado() {
        return estado.obtenerNombre();
    }
}
```

### Paso 4: Uso del State

```
Envio envio = new Envio("ENV-001");
System.out.println("Estado: " + envio.obtenerEstado());

envio.validar();      // CONFIRMADO → EN_TRANSITO
envio.entregar();     // EN_TRANSITO → ENTREGADO
envio.cancelar();     // No se puede cancelar (ya entregado)

Envio envio2 = new Envio("ENV-002");
envio2.cancelar();    // CONFIRMADO → CANCELADO
envio2.validar();     // No se puede validar (cancelado)
```

## Ventajas

- Sin if/else: Comportamiento encapsulado en estados

- Fácil agregar estados: Solo crear nueva clase

- Principio Single Responsibility: Cada estado es responsable de sí

## Desventajas

- Más clases: Una por cada estado

- Complejidad: Puede ser excesivo para pocos estados

Tiempo estimado: 20 minutos

# Strategy Pattern

## Problema: Múltiples Algoritmos

LogiSmart necesita calcular costos de envío con diferentes estrategias:

- Por peso

- Por distancia

- Por urgencia

- Combinación de los anteriores

Problema: ¿Cómo seleccionar algoritmo en tiempo de ejecución sin if/else?

## Solución: Strategy

Define una familia de algoritmos, encapsula cada uno, y los hace intercambiables.

┌──────────────────────────────┐
│ Contexto (Envio)             │
│ - estrategia                 │
│ - calcularCosto()            │
└──────────────────────────────┘
         │
         ▼
┌──────────────────────────────┐
│ Strategy (Interfaz)          │
│ - calcular()                 │
└──────────────────────────────┘
    │
    ├─ EstrategiaDistancia
    ├─ EstrategiaPeso
    ├─ EstrategiaUrgencia
    └─ EstrategiaHibrida

## Implementación

### Paso 1: Interfaz Strategy

```
public interface EstrategiaCalculoCosto {
    double calcular(Envio envio);
    String obtenerNombre();
}
```

### Paso 2: Estrategias Concretas

```
// Estrategia: Por Distancia
public class EstrategiaDistancia implements EstrategiaCalculoCosto {
    private double costoPorKm = 10.0;

    @Override
    public double calcular(Envio envio) {
        double distancia = calcularDistancia(envio.getOrigen(), envio.getDestino());
        return distancia * costoPorKm;
    }

    @Override
    public String obtenerNombre() {
        return "Por Distancia";
    }

    private double calcularDistancia(String origen, String destino) {
        // Simulación
        return Math.random() * 500;
    }
}

// Estrategia: Por Peso
public class EstrategiaPeso implements EstrategiaCalculoCosto {
    private double costoPorKg = 5.0;

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * costoPorKg;
    }

    @Override
    public String obtenerNombre() {
        return "Por Peso";
    }
}

// Estrategia: Por Urgencia
public class EstrategiaUrgencia implements EstrategiaCalculoCosto {
    @Override
    public double calcular(Envio envio) {
        if ("URGENTE".equals(envio.getTipo())) {
            return 500.0;
        } else if ("NORMAL".equals(envio.getTipo())) {
            return 200.0;
        }
        return 100.0;
    }

    @Override
    public String obtenerNombre() {
        return "Por Urgencia";
    }
}

// Estrategia: Híbrida
public class EstrategiaHibrida implements EstrategiaCalculoCosto {
    @Override
    public double calcular(Envio envio) {
        double costoDistancia = new EstrategiaDistancia().calcular(envio);
        double costoPeso = new EstrategiaPeso().calcular(envio);
        double costoUrgencia = new EstrategiaUrgencia().calcular(envio);

        return (costoDistancia * 0.5) + (costoPeso * 0.3) + (costoUrgencia * 0.2);
    }

    @Override
    public String obtenerNombre() {
        return "Híbrida";
    }
}
```

### Paso 3: Contexto

```
public class Envio {
    private String id;
    private String origen;
    private String destino;
    private double peso;
    private String tipo;
    private EstrategiaCalculoCosto estrategia;

    public Envio(String id, String origen, String destino, double peso, String tipo) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.tipo = tipo;
        this.estrategia = new EstrategiaDistancia(); // Default
    }

    public void establecerEstrategia(EstrategiaCalculoCosto estrategia) {
        this.estrategia = estrategia;
        System.out.println("[Envio] Estrategia cambiada a: " + estrategia.obtenerNombre());
    }

    public double calcularCosto() {
        return estrategia.calcular(this);
    }

    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public String getTipo() { return tipo; }
}
```

### Paso 4: Uso del Strategy

```
Envio envio = new Envio("ENV-001", "Buenos Aires", "Córdoba", 5.0, "URGENTE");

// Usar estrategia por defecto (distancia)
System.out.println("Costo (Distancia): $" + envio.calcularCosto());

// Cambiar a estrategia por peso
envio.establecerEstrategia(new EstrategiaPeso());
System.out.println("Costo (Peso): $" + envio.calcularCosto());

// Cambiar a estrategia por urgencia
envio.establecerEstrategia(new EstrategiaUrgencia());
System.out.println("Costo (Urgencia): $" + envio.calcularCosto());

// Cambiar a estrategia híbrida
envio.establecerEstrategia(new EstrategiaHibrida());
System.out.println("Costo (Híbrida): $" + envio.calcularCosto());
```

## Ventajas

- Intercambiable: Cambiar algoritmo en tiempo de ejecución

- Sin if/else: Comportamiento encapsulado

- Fácil agregar: Solo crear nueva estrategia

## Desventajas

- Más clases: Una por cada estrategia

- Overhead: Si solo hay una estrategia, es excesivo

## Diferencia con State

| State | Strategy |

| Cambia el comportamiento según estado | Cambia el algoritmo según preferencia |

| El objeto cambia de estado automáticamente | El cliente elige la estrategia |

| Estados relacionados entre sí | Estrategias independientes |

Tiempo estimado: 20 minutos

# Template Method Pattern

## Problema: Algoritmos Similares

LogiSmart tiene procesos similares para diferentes tipos de envíos:

- Envío nacional: validar → calcular costo → procesar pago → notificar

- Envío internacional: validar → calcular costo + aduanas → procesar pago → notificar + trámites

- Envío urgente: validar → calcular costo prioritario → procesar pago → notificar urgente

Problema: ¿Cómo reutilizar estructura común sin duplicar código?

## Solución: Template Method

Define el esqueleto de un algoritmo en una clase base, permitiendo que subclases implementen pasos específicos.

┌──────────────────────────────────┐
│ ClaseProceso (Abstracta)         │
│ + procesarEnvio() [Template]     │
│   1. validar()                   │
│   2. calcularCosto()             │
│   3. procesarPago()              │
│   4. notificar()                 │
│ # validar() [Abstract]           │
│ # calcularCosto() [Abstract]     │
│ # procesarPago() [Abstract]      │
│ # notificar() [Abstract]         │
└──────────────────────────────────┘
    │
    ├─ ProcesoProcesosNacional
    ├─ ProcesoInternacional
    └─ ProcesoUrgente

## Implementación

### Paso 1: Clase Base Abstracta

```
public abstract class ProcesoProcesosEnvio {
    /**
     * Template Method: Define el esqueleto del algoritmo
     */
    public final void procesarEnvio(Envio envio) {
        System.out.println("[Proceso] Iniciando procesamiento...");
        validar(envio);
        calcularCosto(envio);
        procesarPago(envio);
        notificar(envio);
        System.out.println("[Proceso] ✓ Procesamiento completado\n");
    }

    // Métodos abstractos que las subclases deben implementar
    protected abstract void validar(Envio envio);
    protected abstract void calcularCosto(Envio envio);
    protected abstract void procesarPago(Envio envio);
    protected abstract void notificar(Envio envio);
}
```

### Paso 2: Subclases Concretas

```
// Proceso: Nacional
public class ProcesoNacional extends ProcesoProcesosEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("[Nacional] Validando envío nacional...");
        System.out.println("[Nacional] ✓ Validación OK");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costo = 100.0 + (envio.getPeso() * 5);
        envio.setCosto(costo);
        System.out.println("[Nacional] Costo calculado: $" + costo);
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Nacional] Procesando pago de $" + envio.getCosto());
        System.out.println("[Nacional] ✓ Pago confirmado");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Nacional] Enviando notificación al cliente...");
        System.out.println("[Nacional] ✓ Notificación enviada");
    }
}

// Proceso: Internacional
public class ProcesoInternacional extends ProcesoProcesosEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("[Internacional] Validando envío internacional...");
        System.out.println("[Internacional] Verificando documentación aduanal...");
        System.out.println("[Internacional] ✓ Validación OK");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costoBase = 200.0 + (envio.getPeso() * 10);
        double costoAduanas = costoBase * 0.15;
        double costoTotal = costoBase + costoAduanas;
        envio.setCosto(costoTotal);
        System.out.println("[Internacional] Costo base: $" + costoBase);
        System.out.println("[Internacional] Costo aduanas: $" + costoAduanas);
        System.out.println("[Internacional] Costo total: $" + costoTotal);
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Internacional] Procesando pago internacional de $" + envio.getCosto());
        System.out.println("[Internacional] Verificando cambio de moneda...");
        System.out.println("[Internacional] ✓ Pago confirmado");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Internacional] Enviando notificación al cliente...");
        System.out.println("[Internacional] Enviando información aduanal...");
        System.out.println("[Internacional] ✓ Notificación enviada");
    }
}

// Proceso: Urgente
public class ProcesoUrgente extends ProcesoProcesosEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("[Urgente] Validando envío urgente...");
        System.out.println("[Urgente] ✓ Validación acelerada OK");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costo = 500.0 + (envio.getPeso() * 15);
        envio.setCosto(costo);
        System.out.println("[Urgente] Costo prioritario: $" + costo);
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Urgente] Procesando pago urgente de $" + envio.getCosto());
        System.out.println("[Urgente] ✓ Pago confirmado inmediatamente");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Urgente] Enviando notificación urgente al cliente...");
        System.out.println("[Urgente] Enviando SMS de confirmación...");
        System.out.println("[Urgente] ✓ Notificación urgente enviada");
    }
}
```

### Paso 3: Uso del Template Method

```
Envio envio1 = new Envio("ENV-001", "Buenos Aires", "Córdoba", 5.0);
ProcesoProcesosEnvio procesoNacional = new ProcesoNacional();
procesoNacional.procesarEnvio(envio1);

Envio envio2 = new Envio("ENV-002", "Buenos Aires", "Madrid", 3.0);
ProcesoProcesosEnvio procesoInternacional = new ProcesoInternacional();
procesoInternacional.procesarEnvio(envio2);

Envio envio3 = new Envio("ENV-003", "Buenos Aires", "La Plata", 1.0);
ProcesoProcesosEnvio procesoUrgente = new ProcesoUrgente();
procesoUrgente.procesarEnvio(envio3);
```

## Ventajas

- Reutilización: Estructura común en clase base

- Control: Clase base controla el flujo

- Consistencia: Todos siguen el mismo patrón

## Desventajas

- Rigidez: Estructura fija en clase base

- Herencia: Requiere jerarquía de clases

Tiempo estimado: 15 minutos

# Visitor Pattern

## Problema: Operaciones en Estructuras Complejas

LogiSmart tiene una estructura jerárquica de centros de distribución:

- Centro Nacional

- ├─ Centro Regional

- │ ├─ Punto de Entrega

- │ └─ Punto de Entrega

- └─ Centro Regional

Problema: ¿Cómo realizar múltiples operaciones (calcular ocupación, generar reporte, etc.) sin modificar las clases?

## Solución: Visitor

Representa una operación a realizar sobre elementos de una estructura de objetos, permitiendo definir nuevas operaciones sin cambiar las clases de los elementos.

┌──────────────────────────────┐
│ Visitor (Interfaz)           │
│ + visitar(PuntoEntrega)      │
│ + visitar(CentroRegional)    │
└──────────────────────────────┘
    │
    ├─ VisitorCalculoOcupacion
    ├─ VisitorGeneradorReporte
    └─ VisitorCalculoCosto

┌──────────────────────────────┐
│ Elemento (Interfaz)          │
│ + aceptar(Visitor)           │
└──────────────────────────────┘
    │
    ├─ PuntoEntrega
    └─ CentroRegional

## Implementación

### Paso 1: Interfaz Visitor

```
public interface VisitorCentro {
    void visitar(PuntoEntrega punto);
    void visitar(CentroRegional centro);
}
```

### Paso 2: Interfaz Elemento

```
public interface CentroDistribucion {
    void aceptar(VisitorCentro visitor);
    String obtenerNombre();
}
```

### Paso 3: Elementos Concretos

```
// Elemento: Punto de Entrega
public class PuntoEntrega implements CentroDistribucion {
    private String nombre;
    private double ocupacion;

    public PuntoEntrega(String nombre, double ocupacion) {
        this.nombre = nombre;
        this.ocupacion = ocupacion;
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public double obtenerOcupacion() {
        return ocupacion;
    }
}

// Elemento: Centro Regional
public class CentroRegional implements CentroDistribucion {
    private String nombre;
    private List subcentros = new ArrayList<>();

    public CentroRegional(String nombre) {
        this.nombre = nombre;
    }

    public void agregarSubcentro(CentroDistribucion centro) {
        subcentros.add(centro);
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
        for (CentroDistribucion centro : subcentros) {
            centro.aceptar(visitor);
        }
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public List obtenerSubcentros() {
        return subcentros;
    }
}
```

### Paso 4: Visitors Concretos

```
// Visitor: Calcular Ocupación
public class VisitorCalculoOcupacion implements VisitorCentro {
    private double ocupacionTotal = 0;

    @Override
    public void visitar(PuntoEntrega punto) {
        ocupacionTotal += punto.obtenerOcupacion();
        System.out.println("[Ocupación] Punto: " + punto.obtenerNombre() +
                          " → " + punto.obtenerOcupacion() + "%");
    }

    @Override
    public void visitar(CentroRegional centro) {
        System.out.println("[Ocupación] Centro Regional: " + centro.obtenerNombre());
    }

    public double obtenerOcupacionTotal() {
        return ocupacionTotal;
    }
}

// Visitor: Generar Reporte
public class VisitorGeneradorReporte implements VisitorCentro {
    private StringBuilder reporte = new StringBuilder();
    private int nivel = 0;

    @Override
    public void visitar(PuntoEntrega punto) {
        agregarIndentacion();
        reporte.append("- Punto: ").append(punto.obtenerNombre())
               .append(" (Ocupación: ").append(punto.obtenerOcupacion()).append("%)\n");
    }

    @Override
    public void visitar(CentroRegional centro) {
        agregarIndentacion();
        reporte.append("+ Centro Regional: ").append(centro.obtenerNombre()).append("\n");
        nivel++;
    }

    private void agregarIndentacion() {
        for (int i = 0; i < nivel; i++) {
            reporte.append("  ");
        }
    }

    public String obtenerReporte() {
        return reporte.toString();
    }
}

// Visitor: Calcular Costo
public class VisitorCalculoCosto implements VisitorCentro {
    private double costoTotal = 0;

    @Override
    public void visitar(PuntoEntrega punto) {
        double costo = punto.obtenerOcupacion() * 10;
        costoTotal += costo;
        System.out.println("[Costo] Punto: " + punto.obtenerNombre() + " → $" + costo);
    }

    @Override
    public void visitar(CentroRegional centro) {
        System.out.println("[Costo] Centro Regional: " + centro.obtenerNombre());
    }

    public double obtenerCostoTotal() {
        return costoTotal;
    }
}
```

### Paso 5: Uso del Visitor

```
// Crear estructura
CentroRegional centroNacional = new CentroRegional("Centro Nacional");

CentroRegional centroCABA = new CentroRegional("Centro CABA");
centroCABA.agregarSubcentro(new PuntoEntrega("Punto San Telmo", 75.0));
centroCABA.agregarSubcentro(new PuntoEntrega("Punto Recoleta", 85.0));

CentroRegional centroMendoza = new CentroRegional("Centro Mendoza");
centroMendoza.agregarSubcentro(new PuntoEntrega("Punto Mendoza Centro", 60.0));

centroNacional.agregarSubcentro(centroCABA);
centroNacional.agregarSubcentro(centroMendoza);

// Usar Visitor: Calcular Ocupación
System.out.println("=== Cálculo de Ocupación ===");
VisitorCalculoOcupacion visitorOcupacion = new VisitorCalculoOcupacion();
centroNacional.aceptar(visitorOcupacion);
System.out.println("Ocupación Total: " + visitorOcupacion.obtenerOcupacionTotal() + "%\n");

// Usar Visitor: Generar Reporte
System.out.println("=== Reporte de Estructura ===");
VisitorGeneradorReporte visitorReporte = new VisitorGeneradorReporte();
centroNacional.aceptar(visitorReporte);
System.out.println(visitorReporte.obtenerReporte());

// Usar Visitor: Calcular Costo
System.out.println("=== Cálculo de Costo ===");
VisitorCalculoCosto visitorCosto = new VisitorCalculoCosto();
centroNacional.aceptar(visitorCosto);
System.out.println("Costo Total: $" + visitorCosto.obtenerCostoTotal());
```

## Ventajas

- Fácil agregar operaciones: Solo crear nuevo Visitor

- Separación de responsabilidades: Operación separada de estructura

- Sin modificar elementos: Elementos no cambian

## Desventajas

- Difícil agregar elementos: Requiere cambiar todos los Visitors

- Complejidad: Patrón complejo de entender

Tiempo estimado: 15 minutos

# Comparación de Patrones

## Tabla Comparativa

| Patrón | Propósito | Cuándo Usar |

| State | Cambiar comportamiento según estado | Objeto con múltiples estados con comportamiento diferente |

| Strategy | Seleccionar algoritmo en tiempo de ejecución | Múltiples algoritmos intercambiables |

| Template Method | Definir esqueleto de algoritmo | Algoritmos similares con pasos diferentes |

| Visitor | Realizar operaciones en estructuras complejas | Múltiples operaciones en elementos de estructura |

## Matriz de Decisión

- ¿Comportamiento según estado? → State

- ¿Algoritmo intercambiable? → Strategy

- ¿Esqueleto común? → Template Method

- ¿Operaciones en estructura? → Visitor

Tiempo estimado: 5 minutos

# Ejercicios de Selección de Patrones

## Instrucciones

Para cada problema, identifica cuál patrón es más apropiado y explica por qué.

## Ejercicio 1: Sistema de Pedidos

#### Problema

Un sistema de pedidos online necesita procesar pedidos de diferentes formas:

- Envío estándar: validar → calcular envío → procesar pago → confirmar

- Envío express: validar → calcular envío prioritario → procesar pago → confirmar urgente

- Envío internacional: validar → calcular envío + aduanas → procesar pago → confirmar + trámites

¿Cuál patrón usarías?

Ver Solución

#### Solución

Respuesta: Template Method

Justificación:

- Todos los procesos siguen un esqueleto similar (validar → calcular → pagar → confirmar)

- Los pasos son diferentes en cada tipo de envío

- La estructura del algoritmo es fija, solo varían los detalles

- Template Method permite definir el esqueleto en clase base y que subclases implementen los pasos

## Ejercicio 2: Máquina Expendedora

#### Problema

Una máquina expendedora tiene diferentes estados:

- ESPERANDO: espera que el usuario seleccione producto

- ESPERANDO_PAGO: espera que el usuario pague

- DISPENSANDO: entrega el producto

- FUERA_DE_SERVICIO: no hace nada

Cada estado tiene comportamientos diferentes para las mismas acciones (seleccionar, pagar, dispensar).

¿Cuál patrón usarías?

Ver Solución

#### Solución

Respuesta: State

Justificación:

- El comportamiento depende del estado actual

- Los estados son mutuamente excluyentes

- La máquina transiciona entre estados automáticamente

- State permite encapsular comportamiento por estado sin if/else

## Ejercicio 3: Generador de Reportes

#### Problema

Un sistema necesita generar reportes en múltiples formatos:

- PDF

- Excel

- HTML

- JSON

El cliente puede elegir el formato en tiempo de ejecución. Todos los formatos generan el mismo contenido, solo cambia la presentación.

¿Cuál patrón usarías?

Ver Solución

#### Solución

Respuesta: Strategy

Justificación:

- Hay múltiples algoritmos (formatos) intercambiables

- El cliente elige el algoritmo en tiempo de ejecución

- Los algoritmos son independientes entre sí

- Strategy permite cambiar el algoritmo sin cambiar el código del cliente

## Ejercicio 4: Análisis de Árbol de Carpetas

#### Problema

Un sistema de archivos tiene una estructura jerárquica de carpetas y archivos. Necesitas realizar múltiples operaciones:

- Calcular tamaño total

- Generar reporte de estructura

- Buscar archivos por extensión

- Calcular cantidad de archivos

Necesitas agregar nuevas operaciones frecuentemente sin modificar las clases de carpetas y archivos.

¿Cuál patrón usarías?

Ver Solución

#### Solución

Respuesta: Visitor

Justificación:

- Hay una estructura compleja (árbol de carpetas y archivos)

- Necesitas múltiples operaciones diferentes en la estructura

- Necesitas agregar nuevas operaciones frecuentemente

- Visitor permite agregar operaciones sin modificar las clases de elementos

## Ejercicio 5: Cálculo de Impuestos

#### Problema

Un sistema de facturación necesita calcular impuestos de diferentes formas según el tipo de cliente:

- Cliente común: 21% IVA

- Cliente mayorista: 21% IVA - 5% descuento

- Cliente exento: 0% IVA

- Cliente extranjero: 0% IVA + 5% tasa

El cálculo se realiza en tiempo de ejecución según el tipo de cliente.

¿Cuál patrón usarías?

Ver Solución

#### Solución

Respuesta: Strategy

Justificación:

- Hay múltiples algoritmos de cálculo (estrategias)

- El cliente se elige en tiempo de ejecución

- Los algoritmos son independientes entre sí

- Strategy permite cambiar el algoritmo de cálculo dinámicamente

Tiempo estimado: 10 minutos

# Casos de Aplicación en Sistemas Reales

## State

- Máquinas de estado: Semáforos, ascensores, máquinas expendedoras

- Workflows: Estados de documento, estados de pedido

- Juegos: Estados de personaje (idle, corriendo, saltando)

## Strategy

- Algoritmos de ordenamiento: QuickSort, MergeSort, BubbleSort

- Compresión: ZIP, RAR, 7Z

- Cálculos: Impuestos, comisiones, descuentos

## Template Method

- Frameworks: Spring, Android (Activity lifecycle)

- Procesamiento de datos: Lectura, transformación, escritura

- Parsers: XML, JSON, CSV

## Visitor

- Compiladores: Análisis de árbol de sintaxis

- Sistemas de archivos: Búsqueda, estadísticas

- Gráficos: Renderizado, animación

Tiempo estimado: 5 minutos

# Resumen de la Clase

## Conceptos Clave

### State

- Comportamiento según estado interno

- Estados mutuamente excluyentes

- Transiciones automáticas

### Strategy

- Algoritmos intercambiables

- Cliente elige algoritmo

- Algoritmos independientes

### Template Method

- Esqueleto de algoritmo

- Pasos implementados por subclases

- Estructura fija, detalles variables

### Visitor

- Operaciones en estructuras complejas

- Fácil agregar operaciones

- Difícil agregar elementos

## Patrones de Comportamiento III

| Patrón | Clase | Propósito |

| State | 13 | Cambio de comportamiento por estado |

| Strategy | 13 | Selección de algoritmo |

| Template Method | 13 | Esqueleto de algoritmo |

| Visitor | 13 | Operaciones en estructuras |

## Ejercicios de Selección

Se proporcionan 5 ejercicios con soluciones para practicar la selección del patrón correcto ante un problema dado.

## Preguntas de Reflexión

- ¿Cuándo usarías State vs Strategy?

- ¿Cómo combinarías Template Method con Strategy?

- ¿Qué ventajas tiene Visitor sobre if/else?

## Próximos Pasos

- Hito 12 (Práctica): Aplicar patrones de comportamiento III

- Clase 14: Patrones Arquitectónicos

## Recursos

- Refactoring Guru: https://refactoring.guru/design-patterns/behavioral-patterns

- Design Patterns: Elements of Reusable Object-Oriented Software - Gang of Four

### Duración Total de la Clase

90 minutos de teoría, cubriendo State (20 min), Strategy (20 min), Template Method (15 min), Visitor (15 min), Comparación (5 min), Ejercicios (10 min), Casos Reales (5 min).

La práctica del Hito 12 se realiza en una sesión separada con enunciado específico.

Fin de la Clase 13
