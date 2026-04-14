# Clase 6: Patrones de Diseño GoF – Creacionales

## Qué es un patrón de diseño

Un patrón de diseño es una solución reutilizable a un problema común de diseño de software. Características de un buen patrón:

- Resuelve un problema específico en un contexto concreto
- Está probado y documentado
- Tiene nombre, estructura y consecuencias conocidas
- Es reutilizable en distintos proyectos

La estructura canónica de un patrón incluye: nombre, problema, solución, consecuencias (ventajas y desventajas) y cuándo usarlo.

## Gang of Four (GoF)

En 1994, Gamma, Helm, Johnson y Vlissides documentaron 23 patrones de diseño clasificados en tres grupos:

1. **Creacionales**: cómo crear objetos
2. **Estructurales**: cómo componer clases y objetos
3. **De comportamiento**: cómo asignar responsabilidades y comunicación

Esta clase cubre los patrones creacionales. GRASP nos dio principios; los patrones GoF nos dan soluciones concretas para aplicar esos principios.

## Patrones Creacionales

Los patrones creacionales resuelven cómo crear objetos sin acoplar el código al tipo concreto. Si el cliente instancia directamente subclases, queda acoplado a todas ellas. Los patrones creacionales encapsulan esa lógica de creación.

Los 5 patrones creacionales son: Singleton, Factory Method, Abstract Factory, Builder y Prototype. En esta clase se profundiza en los primeros dos.

## Singleton

Singleton garantiza que una clase tenga una única instancia y ofrece un único punto de acceso global a ella.

**Problema**: no queremos múltiples instancias de configuración, conexión a base de datos o logger. Múltiples instancias generan inconsistencias.

**Solución clásica**:

```java
public class ConexionBD {
    private static ConexionBD instancia;

    private ConexionBD() { } // constructor privado

    public static ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
}
```

**Thread safety**: en sistemas multi-hilo, hay que proteger la creación de la instancia. Soluciones:

- Sincronizar el método `getInstance()`
- Eager initialization (instanciar al cargar la clase)
- Double-checked locking

**Ventajas**:
- Único punto de acceso
- Control centralizado
- Inicialización diferida

**Desventajas**:
- Dificulta el testing (las dependencias ocultas son difíciles de mockear)
- Puede convertirse en anti-patrón si se abusa de él
- Oculta dependencias

**Usos típicos**: conexión a base de datos, logger, configuración global, caché.

## Factory Method

Factory Method crea objetos sin acoplar el código cliente a las clases concretas. El cliente no sabe qué clase exacta se instancia; solo conoce la interfaz.

**Problema**: si el cliente instancia directamente subclases, cada nuevo tipo requiere modificar el cliente.

**Solución**:

```java
// Interfaz
public interface Notificador {
    void enviar(String mensaje);
}

// Implementaciones concretas
public class EmailNotificador implements Notificador { ... }
public class SMSNotificador implements Notificador { ... }

// Factory
public class NotificadorFactory {
    public static Notificador crear(String tipo) {
        if (tipo.equals("email")) return new EmailNotificador();
        if (tipo.equals("sms")) return new SMSNotificador();
        throw new IllegalArgumentException("Tipo desconocido: " + tipo);
    }
}

// Cliente
Notificador n = NotificadorFactory.crear("email");
n.enviar("Envío despachado");
```

**Ventajas**:
- Desacopla el cliente de las clases concretas
- Centraliza la lógica de creación
- Facilita agregar nuevos tipos sin tocar el cliente
- Mejora la testabilidad

**Ejemplos en LogiSmart**:
- Fábrica de envíos según tipo (express, normal, refrigerado)
- Fábrica de notificadores (email, SMS, push)
- Fábrica de vehículos según zona

## Singleton vs. Factory Method

| Patrón | Cuándo usar |
|---|---|
| Singleton | Cuando necesitás exactamente 1 instancia de un recurso global |
| Factory Method | Cuando la creación varía según el contexto y querés desacoplarla |

Regla práctica: usá Singleton para acceso único a un recurso compartido. Usá Factory Method cuando la creación cambia según el contexto.

## Arquitectura propuesta para LogiSmart

```
ConexionBD (Singleton)
    └── accedida por todos los repositorios

NotificadorFactory (Factory Method)
    ├── EmailNotificador
    ├── SMSNotificador
    └── PushNotificador

VehiculoFactory (Factory Method)
    ├── Moto
    ├── Auto
    └── Camion

LogiSmartController
    └── coordina el uso de todos los anteriores
```

## Hito 6

El Hito 6 pide aplicar patrones creacionales en LogiSmart:

- Identificar dónde corresponde Singleton
- Decidir qué objetos se crean con factories
- Justificar el uso de cada patrón
- Integrar la solución en el controlador principal

---

## Perspectiva del profesor

### Insights clave

El profesor explicó que los patrones de diseño son el resultado de trabajo colectivo de décadas: alguien se quemó la cabeza resolviendo un problema, lo documentó y lo compartió. Usarlos bien no es memorizar su estructura: es entender qué problema resuelven.

> "Hubo un grupo de personas que se quemaron la cabeza para resolver un problema de diseño, documentaron todo ese proceso y lo compartieron con el mundo."

GRASP nos enseña principios de asignación de responsabilidades. Los patrones GoF nos dan soluciones concretas para problemas comunes que aparecen al aplicar esos principios. Son capas complementarias, no alternativas.

Los patrones no son reglas ni obligaciones: son guías y buenas prácticas. No significa que deban aplicarse en todos los proyectos. Son mejoras incrementales que se aplican cuando el contexto lo justifica.

El Singleton tiene un riesgo real: si se abusa de él, el sistema se llena de estado global difícil de testear. A veces la inyección de dependencias es una mejor alternativa. El profesor fue explícito en señalar tanto las ventajas como las limitaciones.

> "Los patrones no son reglas. Son guías. Son buenas prácticas. Un buen analista y diseñador es más valioso que alguien que conoce todas las herramientas pero no sabe cuándo usarlas."

### Analogías y ejemplos reales

**Singleton para la base de datos**: si cada operación abre una conexión nueva a la base de datos, el sistema colapsa bajo carga. Singleton garantiza que todos los módulos compartan la misma conexión (o pool). Es la diferencia entre un sistema que escala y uno que no.

**Factory Method para tipos de envío**: un sistema logístico real tiene envíos de 24 horas, 3 días y 7 días. Cada uno tiene reglas distintas de tarifa, prioridad y seguimiento. Si el código del cliente conoce cada tipo directamente, cada vez que agregás un tipo nuevo, modificás el cliente. Con Factory Method, agregás una nueva clase y registrás el tipo en la factory: el cliente no cambia.

**Los 23 patrones GoF en 2026**: el libro se publicó en 1994 y sigue siendo la referencia estándar. Algunos problemas de software son tan fundamentales que las soluciones son atemporales. El lenguaje cambia, los frameworks cambian, los patrones permanecen.

### Consejo profesional

- Antes de aplicar un patrón, entendé el problema que resuelve. Aplicar un patrón sin necesidad es over-engineering.
- Singleton es útil para recursos globales compartidos, pero cuidado con el testing: una clase que depende de un Singleton es difícil de probar de forma aislada. Considerá inyección de dependencias como alternativa.
- Factory Method es tu mejor herramienta cuando el tipo exacto de objeto a crear varía según el contexto. No necesitás saber el tipo concreto en el cliente.
- Siempre documentá por qué elegiste un patrón, no solo cómo lo implementaste. La justificación es parte del diseño.
- Los patrones creacionales son el primer paso hacia una arquitectura desacoplada. Lo que construís en el Hito 6 es la base para los patrones estructurales y de comportamiento que vienen después.