# Clase 10: Patrones Estructurales II

Fuente: `Clase_10.html` (contenido de `main-content`).

## Estructura de la clase

1. Introduccion a patrones estructurales II.
2. Decorator.
3. Facade.
4. Flyweight.
5. Proxy.
6. Comparacion de patrones estructurales.
7. Combinacion de patrones.
8. Casos reales.
9. Resumen y relacion con Hito 9.

## Introduccion

La clase completa el bloque de patrones estructurales con cuatro patrones avanzados:

- Decorator: agrega responsabilidades dinamicamente.
- Facade: simplifica interfaces complejas.
- Flyweight: comparte objetos para optimizar memoria.
- Proxy: controla acceso a objetos costosos.

La pregunta central es como agregar funcionalidad sin modificar clases, simplificar subsistemas, ahorrar memoria y controlar acceso a recursos costosos.

## Decorator

### Problema

Si un `Envio` necesita servicios opcionales como seguro, rastreo, notificaciones y prioridad, una solucion ingenua con herencia genera explosion combinatoria de subclases.

Ejemplo conceptual:

- `EnvioConSeguro`
- `EnvioConRastreo`
- `EnvioConSeguroYRastreo`
- `EnvioConSeguroYRastreoYNotificaciones`

Cada nueva combinacion multiplica clases.

### Solucion

Decorator envuelve un objeto base y agrega comportamiento sin modificarlo.

Participantes:

- Componente: interfaz `Envio`.
- Componente concreto: `EnvioBasico`.
- Decorador base: `DecoradorEnvio`.
- Decoradores concretos: seguro, rastreo GPS, notificaciones, prioridad.

### Implementacion clave

Interfaz base:

```java
public interface Envio {
    double obtenerCosto();
    int obtenerTiempoEntrega();
    String obtenerDescripcion();
    String obtenerServicios();
}
```

`EnvioBasico` calcula costo base por peso y tiempo base de entrega.

`DecoradorEnvio` delega en `envioDecorado`.

Decoradores concretos:

- `DecoradorSeguro`: +15% costo.
- `DecoradorRastreoGPS`: +50 costo y reduce tiempo.
- `DecoradorNotificacionesSMS`: +25 costo.
- `DecoradorPrioritario`: +100 costo y reduce mas el tiempo.

### Ventajas

- evita explosion de subclases
- permite combinar servicios en cualquier orden
- cada decorador tiene responsabilidad unica
- favorece composicion sobre herencia

### Desventajas

- muchas clases pequenas
- el orden de aplicacion puede importar
- debugging mas dificil por capas de envoltura

### Cuando usarlo

- funcionalidades opcionales e independientes
- necesidad de agregar responsabilidades dinamicamente
- deseo de evitar nuevas subclases por cada combinacion

## Facade

### Problema

Un flujo logistico real coordina varios subsistemas: inventario, pagos, notificaciones, rastreo y reportes. Sin una fachada, el cliente debe conocer cada subsistema y el orden exacto de uso.

### Solucion

Facade crea una interfaz unificada que encapsula la complejidad.

Participantes:

- Cliente.
- `ServicioEnviosFacade` o `ServicioLogisticaFacade`.
- Subsistemas internos.

### Implementacion clave

Subsistemas conceptuales:

- `SistemaInventario`
- `SistemaPagos`
- `SistemaNotificaciones`
- `SistemaRastreo`
- `SistemaReportes`

Fachada:

```java
public class ServicioEnviosFacade {
    public String crearEnvio(String productoId, double monto, String email) { ... }
    public void cancelarEnvio(String numeroSeguimiento) { ... }
    public String obtenerEstado(String numeroSeguimiento) { ... }
}
```

El flujo tipico es:

1. verificar inventario
2. procesar pago
3. restar stock
4. crear seguimiento
5. notificar al cliente
6. generar reporte

### Ventajas

- simplifica la interfaz al cliente
- desacopla al cliente de subsistemas concretos
- mejora mantenibilidad
- concentra operaciones repetidas de coordinacion

### Desventajas

- puede crecer demasiado y transformarse en un objeto dios
- oculta complejidad que a veces conviene exponer
- menos flexible que usar subsistemas directo

### Cuando usarlo

- subsistemas complejos con orden de coordinacion
- necesidad de interfaz unificada
- deseo de desacoplar clientes de detalles internos

## Flyweight

### Problema

Crear millones de objetos `Ubicacion` identicos desperdicia memoria. El ejemplo de clase plantea millones de envios con origenes y destinos repetidos.

### Solucion

Flyweight comparte objetos inmutables. Las instancias repetidas no se crean de nuevo; se reutilizan desde una factory o pool.

Participantes:

- Flyweight: `Ubicacion`.
- Factory: `FabricaUbicaciones`.

### Implementacion clave

`Ubicacion` debe ser inmutable.

`FabricaUbicaciones.obtener(ciudad, provincia, codigoPostal)` construye una clave y:

- crea una nueva ubicacion si no existe
- reutiliza la instancia si ya esta en el mapa

Tambien ofrece:

- `mostrarEstadisticas()`
- `limpiar()`

### Ventajas

- reduce consumo de memoria
- disminuye garbage collection
- escala mejor con objetos repetidos

### Desventajas

- agrega complejidad de fabrica/pool
- requiere inmutabilidad
- en escenarios concurrentes la fabrica debe ser thread-safe

### Cuando usarlo

- hay muchisimos objetos similares
- memoria critica
- alto potencial de reutilizacion

### Ejemplos estandar mencionados

- `String.intern()`
- `Integer.valueOf()`
- `Boolean.TRUE` y `Boolean.FALSE`

## Proxy

### Problema

Acceder a una BD remota, archivo pesado o servicio externo puede ser costoso. Cargarlo todo al inicio es ineficiente.

### Solucion

Proxy se interpone entre el cliente y el objeto real para controlar el acceso.

Tipos mencionados:

- Proxy virtual: lazy loading.
- Proxy de proteccion: validacion/permisos.
- Proxy remoto: acceso a objetos remotos.
- Proxy de cache: reutiliza resultados.

### Implementacion clave

Interfaz comun:

```java
public interface BaseDatos {
    List<Envio> obtenerEnvios();
    Envio obtenerEnvio(String id);
    void guardarEnvio(Envio envio);
}
```

`BaseDatosReal` simula conexion costosa.

`ProxyBaseDatos`:

- crea la BD real solo al primer uso
- cachea envios por id
- puede cachear listados
- mantiene al cliente desacoplado del detalle real

### Ventajas

- lazy loading
- cache
- control de acceso
- transparencia para el cliente

### Desventajas

- capa adicional de indirecciones
- leve sobrecarga por acceso
- riesgo de cache desactualizada

### Cuando usarlo

- recursos costosos
- necesidad de lazy loading
- cache
- validacion o control de acceso

## Comparacion de patrones

| Patron | Proposito | Complejidad | Cuando usar |
|---|---|---|---|
| Decorator | Agregar responsabilidades dinamicamente | Media | Funcionalidades opcionales e independientes |
| Facade | Simplificar interfaces complejas | Baja | Subsistemas complejos con operaciones comunes |
| Flyweight | Optimizar memoria compartiendo objetos | Media | Muchos objetos similares, memoria critica |
| Proxy | Controlar acceso a recursos costosos | Media | Lazy loading, cache, validacion |

Matriz de decision:

- agregar funcionalidad dinamica -> Decorator
- simplificar subsistemas -> Facade
- optimizar memoria -> Flyweight
- controlar acceso a recursos -> Proxy

## Comparacion con Clase 9

La clase 9 cubre Adapter, Bridge y Composite.

La clase 10 agrega:

- Decorator: responsabilidades opcionales
- Facade: interfaz unificada
- Flyweight: memoria compartida
- Proxy: acceso controlado

Todos siguen siendo patrones estructurales, no creacionales.

## Combinacion de patrones

La clase muestra combinaciones utiles:

- Decorator + Proxy
- Facade + Decorator
- Composite + Flyweight
- Adapter + Facade

Principios para combinarlos:

- composicion sobre herencia
- responsabilidad unica
- interfaz consistente
- evitar complejidad excesiva

## Casos reales

Decorator:

- `BufferedInputStream`, `DataInputStream`
- componentes GUI
- middleware web

Facade:

- frameworks y APIs publicas
- wrappers de JDBC/JPA/transacciones
- librerias de mapas

Flyweight:

- editores de texto
- videojuegos
- pooling o reutilizacion de recursos

Proxy:

- ORM con lazy loading
- cache distribuido
- servicios remotos

## Resumen

Conceptos clave:

- Decorator envuelve y agrega funcionalidad.
- Facade simplifica y coordina.
- Flyweight comparte y ahorra memoria.
- Proxy controla, cachea y difiere carga.

La clase conecta directamente con el Hito 9, donde estos cuatro patrones deben aplicarse en LogiSmart con codigo, casos de prueba, documento markdown y diagrama UML.
