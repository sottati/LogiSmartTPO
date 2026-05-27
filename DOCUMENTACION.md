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
- constructor `Envio(String id, String origen, String destino, double peso, String tipo)`

Se mantienen intactos:

- `cambiarEstado(String)`
- `iniciar()`
- `cancelar()`
- `cerrar()`
- Memento y Observer de Hito 11

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

## 9) Documentación actualizada

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
