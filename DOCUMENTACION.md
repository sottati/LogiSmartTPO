# DOCUMENTACION - Hito 12

## 1) Objetivo

Consolidar LogiSmart hasta Hito 12, incorporando cuatro patrones GoF de comportamiento III: State, Strategy, Template Method y Visitor.

Base acumulada:

- Hito 10: Chain of Responsibility, Command, Interpreter
- Hito 11: Iterator, Mediator, Memento, Observer
- Hito 12: State, Strategy, Template Method, Visitor

Total GoF acumulado: 23 patrones.

## 2) Cambios en dominio

### `src/com/logismart/dominio/Envio.java`

Adiciones compatibles con hitos anteriores:

- `private EstadoEnvio estadoGoF = new EstadoConfirmado()`
- overload `cambiarEstado(EstadoEnvio)`
- delegadores State: `validar()`, `entregar()`, `retener()`, `devolver()`, `reclamar()`
- helper de test: `obtenerNombreEstadoGoF()`
- `private String tipo`
- `private EstrategiaCalculoCosto estrategia`
- `getTipo()`
- `establecerEstrategia(EstrategiaCalculoCosto)`
- `calcularCostoConEstrategia()`

Se mantienen intactos:

- `cambiarEstado(String)`
- `iniciar()`
- `cancelar()`
- `cerrar()`
- Memento y Observer de Hito 11

Nota: el campo `tipo` se asigna vía `EnvioBuilder.tipo(String)` - el constructor especializado `Envio(id, origen, destino, peso, tipo)` fue consolidado en el Builder (ver refactorización post-Hito 12).

## 3) State

Paquete: `src/com/logismart/infraestructura/comportamiento/state/`

Clases:

- `EstadoEnvio`
- `EstadoConfirmado`
- `EstadoEnTransito`
- `EstadoEnReparto`
- `EstadoEntregado`
- `EstadoCancelado`
- `EstadoRetenido`

Diseño:

- `Envio` es el contexto.
- Cada estado decide qué hacer ante `validar`, `entregar`, `cancelar`, `retener`, `devolver` y `reclamar`.
- Las transiciones válidas llaman `envio.cambiarEstado(new EstadoX())`.
- Las transiciones inválidas informan por consola y no cambian estado.

Evidencia:

- `CasosDePruebaHito12.probarState()`
- Flujo normal: CONFIRMADO → EN_TRANSITO → EN_REPARTO → ENTREGADO
- Cancelación, retención, devolución, reclamo y transiciones inválidas.

## 4) Strategy

Paquete: `src/com/logismart/infraestructura/comportamiento/strategy/`

Clases:

- `EstrategiaCalculoCosto`
- `EstrategiaDistancia`
- `EstrategiaPeso`
- `EstrategiaUrgencia`
- `EstrategiaVolumen`
- `EstrategiaHibrida`

Estrategias:

- Distancia: `peso × 500`
- Peso: `peso × 5.0`
- Urgencia: `URGENTE=500`, `EXPRESS=300`, default `100`
- Volumen: `peso × 2 × 2.0`
- Híbrida: `40% distancia + 30% peso + 30% urgencia`

Decisión:

- Se usa distancia fija de 500 km en lugar de `Math.random() * 500`.
- Motivo: tests deterministas y verificables.

Evidencia:

- `CasosDePruebaHito12.probarStrategy()`
- 7 verificaciones: 5 estrategias, cambio dinámico y resultado positivo.

## 5) Template Method

Paquete: `src/com/logismart/infraestructura/comportamiento/template/`

Clases:

- `ProcesoEnvio`
- `ProcesoNacional`
- `ProcesoInternacional`
- `ProcesoUrgente`

Diseño:

- `ProcesoEnvio.procesarEnvio(Envio)` es `final`.
- Orden fijo:
  1. `validar`
  2. `calcularCosto`
  3. `procesarPago`
  4. `notificar`
- Las subclases concretas implementan los pasos.

Evidencia:

- `CasosDePruebaHito12.probarTemplateMethod()`
- Verifica 3 procesos, orden de pasos y extensibilidad.

## 6) Visitor

Paquete: `src/com/logismart/infraestructura/comportamiento/visitor/`

Clases:

- `VisitorCentro`
- `ElementoDistribucion`
- `NodoPuntoEntrega`
- `NodoCentroRegional`
- `VisitorCalculoOcupacion`
- `VisitorGeneradorReporte`
- `VisitorCalculoCostoOperativo`
- `VisitorBusquedaPuntosCriticos`

Decisión de naming:

- La consigna usa `CentroDistribucion` y `PuntoEntrega`.
- Esos nombres ya existen en el proyecto.
- Para evitar colisión semántica se usan `ElementoDistribucion`, `NodoPuntoEntrega` y `NodoCentroRegional` dentro del paquete visitor.

Evidencia:

- `CasosDePruebaHito12.probarVisitor()`
- 8 verificaciones: resultado correcto y recorrido completo para cada visitor.

## 7) Integración

Paquete: `src/com/logismart/aplicacion/hito12/`

Clases:

- `SistemaLogisticaAvanzada`
- `CasosDePruebaHito12`

`SistemaLogisticaAvanzada` integra:

- State: avanza estados del envío.
- Strategy: calcula costo con estrategia recibida.
- Template Method: procesa envío con proceso recibido.
- Visitor: analiza red de distribución.

Evidencia:

- `CasosDePruebaHito12.probarIntegracion()`
- 4 verificaciones combinadas.

## 8) Ejecución

`Main.java` ejecuta:

- `CasosDePruebaHito8.ejecutar()`
- `CasosDePruebaHito9.ejecutar()`
- `CasosDePruebaHito10.ejecutar()`
- `CasosDePruebaHito11.ejecutar()`
- `CasosDePruebaHito12.ejecutar()`

Verificación realizada:

```bash
javac -d out $(find src -name '*.java') && java -cp out com.logismart.app.Main
```

Resultado relevante:

- Hito 12: 36 casos ejecutados, 36 OK.

## 9) Refactoring post-Hito 12

Correcciones de diseño aplicadas al dominio base tras completar el Hito 12. 148/148 tests siguen en verde.

### `Rol.java` (nuevo enum)

Paquete: `src/com/logismart/dominio/`

Centraliza la matriz de permisos 5×5 (crearEnvio, asignarRuta, verReportes, gestionarFlota, administrarEmpresas) para los 5 roles. Elimina 25 booleanos duplicados distribuidos en las subclases de `Usuario`.

### Subclases de `Usuario`

`OperadorLogistico`, `AdminEmpresa`, `AdminPlataforma`, `ClienteFinal`, `Transportista`:

- Añaden `private static final Rol ROL = Rol.CLASE`.
- Los 5 métodos de `IPermisos` delegan en `ROL.puedeXxx()`.
- Los métodos stub vacíos reciben implementaciones mínimas con `System.out.println`.
- `AdminPlataforma` agrega el `saludar()` que faltaba.
- `Transportista` agrega `saludar()` y marca `disponibilidad` en `iniciarRecorrido()` / `registrarEntrega()`.

### `PosicionGPS.java`

- `distanciaA(PosicionGPS)` reemplaza la fórmula euclidea incorrecta por delegación a `haversineKm()`.
- Nuevo método estático `haversineKm(lat1, lng1, lat2, lng2)` con la fórmula de Haversine (radio = 6371 km).

### `Vehiculo.java`

- Nuevo método `getCostoBaseKm()`: CAMION=1.8, UTILITARIO=1.3, default=1.0. Aplica GRASP Information Expert.

### `Ruta.java`

- `calcularDistanciaTotal()` usa `PosicionGPS.haversineKm()` en lugar del método privado duplicado.
- `calcularCostoEstimado()` delega en `vehiculo.getCostoBaseKm()`.
- `optimizar()` implementado: ordena `paradas` por `ordenParada` y llama `recalcular()`.
- Eliminado método privado `distanciaEntre()` (lógica movida al experto `PosicionGPS`).

### `SeguimientoEnvio.java`

- Constructor de 4 argumentos delega en el de 2 argumentos via `this(id, estadoActual)`. Elimina inicialización duplicada de `eta` e `historialPosiciones`.

### `Entrega.java`

- Ambos constructores inicializan `this.pruebaAdjunta = ""`. Garantiza que `getPruebaAdjunta()` nunca retorne `null`.

### `Usuario.java`

- `iniciarSesion(username, password)` y `cerrarSesion()` implementados con `System.out.println`.

## 10) Documentación actualizada

- `hitos/HITO_12.html`
- `DIAGRAMA_DE_CLASES_ACTUAL.html`
- `index.html`
- `DOCUMENTACION.md`

## 10) Conteo Hito 12

Clases Java agregadas:

- State: 7 clases
- Strategy: 6 clases
- Template Method: 4 clases
- Visitor: 8 clases
- Integración + tests: 2 clases

Total: 27 clases nuevas.

Total de elementos del hito contando el contexto State modificado: 28 (`Envio` + 27 clases nuevas).

Nota: el conteo pedido decía State 8, pero listaba 1 interfaz + 6 estados y aclaraba que el contexto es `Envio`, por lo que no hay octava clase nueva de State. La implementación mantiene esa decisión: `Envio` es el contexto existente.
