# DOCUMENTACION - Hito 9

## 1) Objetivo

Consolidar el proyecto LogiSmart hasta Hito 9, incorporando patrones GoF creacionales y estructurales sobre una base ya modelada con GRASP.

## 2) Hitos cubiertos

- Hito 6: Singleton y Factory Method
- Hito 7: Abstract Factory, Builder y Prototype
- Hito 8: Adapter, Bridge y Composite
- Hito 9: Decorator, Facade, Flyweight y Proxy

## 3) Hito 9 - Patrones implementados

### 3.1 Decorator

Clases:

- `src/com/logismart/infraestructura/decorator/envio/Envio.java`
- `src/com/logismart/infraestructura/decorator/envio/EnvioBasico.java`
- `src/com/logismart/infraestructura/decorator/envio/DecoradorEnvio.java`
- `src/com/logismart/infraestructura/decorator/envio/DecoradorSeguro.java`
- `src/com/logismart/infraestructura/decorator/envio/DecoradorRastreoGPS.java`
- `src/com/logismart/infraestructura/decorator/envio/DecoradorNotificacionesSMS.java`
- `src/com/logismart/infraestructura/decorator/envio/DecoradorPrioritario.java`

Uso:

- agrega servicios opcionales sobre un envio basico
- evita explosion de subclases combinatorias
- mantiene cada servicio encapsulado en su propio decorador

Decisiones:

- se uso una interfaz separada del `Envio` de dominio para no romper hitos anteriores
- el costo porcentual del seguro se aplica sobre el costo acumulado al momento de envolver

### 3.2 Facade

Clase principal:

- `src/com/logismart/aplicacion/hito9/ServicioLogisticaFacade.java`

Subsistemas de soporte en el mismo archivo:

- `SistemaInventario`
- `SistemaPagos`
- `SistemaNotificaciones`
- `SistemaRastreo`
- `SistemaReportes`

Uso:

- expone `crearEnvio`, `cancelarEnvio` y `obtenerEstadoEnvio`
- encapsula el orden correcto de inventario, pago, rastreo, notificacion y reporte

Decisiones:

- los subsistemas quedaron package-private en el mismo archivo para mantener el entregable centrado en una sola fachada publica
- se priorizo una implementacion academica autocontenida y compilable

### 3.3 Flyweight

Clases:

- `src/com/logismart/infraestructura/flyweight/ubicacion/Ubicacion.java`
- `src/com/logismart/infraestructura/flyweight/ubicacion/FabricaUbicaciones.java`

Uso:

- comparte instancias inmutables de ubicaciones
- reduce objetos repetidos en memoria para origenes y destinos recurrentes

Decisiones:

- `Ubicacion` es inmutable
- la fabrica usa `LinkedHashMap` para que las estadisticas salgan en orden estable

### 3.4 Proxy

Clases:

- `src/com/logismart/infraestructura/proxy/envio/RepositorioEnvios.java`
- `src/com/logismart/infraestructura/proxy/envio/RepositorioEnviosReal.java`
- `src/com/logismart/infraestructura/proxy/envio/ProxyRepositorioEnvios.java`

Uso:

- lazy loading del repositorio real
- cache por id y cache de listado completo
- validacion simple de acceso e inputs

Decisiones:

- se reutilizo `com.logismart.dominio.Envio` para no duplicar entidades
- se agregaron helpers de inspeccion (`estaInicializado`, `obtenerTamanoCache`) para poder probar lazy loading y cache sin framework externo

### 3.5 Integracion

Clase:

- `src/com/logismart/aplicacion/hito9/ServicioLogisticaCompleto.java`

Uso:

- arma un envio decorado con servicios opcionales
- reutiliza ubicaciones flyweight
- delega la orquestacion operativa al facade
- persiste el resultado via proxy

## 4) Casos de prueba

Clase ejecutable:

- `src/com/logismart/aplicacion/hito9/CasosDePruebaHito9.java`

Cobertura:

- 7 verificaciones de Decorator
- 5 verificaciones de Facade
- 5 verificaciones de Flyweight
- 6 verificaciones de Proxy
- 4 verificaciones de integracion

Total:

- 29 verificaciones automaticas por excepcion si algo falla

## 5) Skill de Clase 10

Ubicacion:

- `skills/pds-clase-10/SKILL.md`
- `skills/pds-clase-10/REFERENCE.md`

Fuente:

- `skills/clases/Clase_10.html`

Criterio:

- se parseo solo el contenido de `main-content`
- se removio ruido visual de sidebar, CSS y JS
- se preservaron secciones, tablas, listas y ejemplos tecnicos

## 6) Entregables visuales

- `hitos/HITO_9.html`
- `DIAGRAMA_DE_CLASES_ACTUAL.html`
- `index.html`

## 7) Ejecucion

Compilar:

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

Ejecutar demo:

```bash
java -cp bin com.logismart.app.Main
```

## 8) Riesgos y limites

- la persistencia del proxy es en memoria y solo simula una BD remota
- la fachada modela pagos, inventario y rastreo de forma academica, no transaccional
- el diagrama acumulativo prioriza claridad conceptual sobre detalle exhaustivo de cada helper interno
