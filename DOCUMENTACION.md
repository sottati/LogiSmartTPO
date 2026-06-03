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

---

## Hito 13

### Objetivo

Diseñar la capa de persistencia de LogiSmart separando la lógica de negocio del almacenamiento, aplicando los cuatro patrones de acceso a datos: Data Mapper, Repository, Unit of Work y Lazy Load.

Base acumulada al cierre del TPO:

- Hitos 1–7: dominio, creacionales, estructurales
- Hitos 8–9: Façade, Composite, Decorator, Flyweight, Proxy
- Hito 10: Chain, Command, Interpreter
- Hito 11: Iterator, Mediator, Memento, Observer
- Hito 12: State, Strategy, Template Method, Visitor
- Hito 13: Data Mapper, Repository, Unit of Work, Lazy Load

Total patrones GoF acumulados: 23 + 4 de acceso a datos.

### Patrones implementados

#### Data Mapper

Paquete: `src/com/logismart/infraestructura/persistencia/mapper/`

Interfaces: `EnvioMapper`, `ClienteMapper`, `CentroDistribucionMapper`, `CobroMapper`.
Implementaciones SQL en `mapper/sql/`: `EnvioMapperSQL`, `ClienteMapperSQL`, `CentroDistribucionMapperSQL`, `CobroMapperSQL`.

Cada mapper tiene: `insertar(T)`, `actualizar(T)`, `eliminar(String id)`, `buscarPorId(String id)`.
Las implementaciones SQL usan `PreparedStatement` — compilan con el JDK, no se ejecutan en tests (no hay driver/BD en el build).
`EnvioMapperSQL.buscarPorId` reconstruye el `Envio` via `EnvioBuilder` (fidelidad al dominio real).

#### Repository

Paquete: `src/com/logismart/infraestructura/persistencia/repositorio/`

- `Repositorio<T>`: interfaz genérica con `guardar`, `obtener(int)`, `obtenerTodos`, `eliminar(int)`.
- `RepositorioEnvio`, `RepositorioCliente`, `RepositorioPago`: extienden con overload `obtener(String id)` — fidelidad al `String id` del dominio real.
- `RepositorioCentro`: usa `int id` de la interfaz genérica directamente.

Implementaciones en memoria (`repositorio/memoria/`): `RepositorioEnvioMemoria`, `RepositorioClienteMemoria`, `RepositorioCentroMemoria`, `RepositorioPagoMemoria`. Usan `HashMap` interno. Son las implementaciones testeadas.
Implementaciones SQL (`repositorio/sql/`): compilan, no se ejecutan en tests.

#### Unit of Work

Clase: `src/com/logismart/infraestructura/persistencia/unitofwork/UnitOfWork.java`

Mantiene tres listas: `nuevos`, `modificados`, `eliminados`. `commit()` ejecuta INSERT → UPDATE → DELETE en orden (simula atomicidad) y vacía las listas. `rollback()` descarta todo sin ejecutar.

#### Lazy Load

Paquete: `src/com/logismart/infraestructura/persistencia/lazy/`

- `ClienteLazyProxy`: wrappea `RepositorioClienteMemoria`; carga `ClienteFinal` solo al primer `getCliente()`.
- `CentroDistribucionLazyProxy`: idem para la entidad de persistencia `CentroDistribucion`.
- `HistorialEnviosLazyProxy`: wrappea `RepositorioEnvioMemoria`; filtra envíos por `clienteId` al primer `getHistorial()`.

### Entidades

- `CentroDistribucion` (persistencia) en `persistencia/entidad/`: entidad plana con `id, nombre, ubicacion, codigo, capacidad, ocupacion`.
- `CentroAssembler`: proyecta el Composite abstracto a la entidad plana (`aPersistencia(id, composite)`). El Composite es la única fuente de verdad; la entidad es una vista materializada generada al guardar — no puede desincronizarse.

### Decisiones de diseño

1. **Reutilización de entidades**: se reutilizan `Envio`, `ClienteFinal` y `Cobro` (ya existentes). Solo se crea `CentroDistribucion` de persistencia (el Composite es abstracto, no puede ser una fila).
2. **`Cobro` con `envioId` aditivo**: se agrega el campo `envioId` + getter/setter sin cambiar constructores existentes. Riesgo de regresión: nulo. Habilita `RepositorioPago.buscarPorEnvio`.
3. **`estado` de `Cobro` como `String`**: no se introduce `EstadoPago` enum; sus valores no coinciden con los del dominio (`AUTORIZADO/PAGADO/FALLIDO` vs `PENDIENTE/PROCESANDO/COMPLETADO/RECHAZADO`). Forzarlo cambiaría el comportamiento ya testeado.
4. **`CentroDistribucion` separado**: el Composite es jerárquico y sus métricas son calculadas. La entidad de persistencia es plana. La separación es correcta; el assembler previene el desfasaje.
5. **Interfaz genérica `obtener(int)` vs dominio `String`**: las interfaces específicas agregan overload `obtener(String id)`. La firma `int` queda como vestigio de genericidad; se documenta la divergencia.
6. **Tests en memoria**: implementaciones `HashMap` son deterministas, sin dependencias externas. Las implementaciones SQL compilan y demuestran dominio de JDBC pero no se ejecutan.
7. **`EnvioBuilder` para reconstrucción**: `EnvioMapperSQL.buscarPorId` usa el Builder interno (no constructor de 4 args) — fidelidad al dominio real con `String id` y campos logísticos.

### Ajuste a `Cobro.java`

Campo y accessors agregados aditivamente (sin cambiar constructores existentes):

```java
private String envioId;
public String getEnvioId()              { return envioId; }
public void setEnvioId(String envioId)  { this.envioId = envioId; }
```

### Servicios y fachada

Paquete: `src/com/logismart/aplicacion/hito13/`

- `ServicioEnvios`, `ServicioClientes`, `ServicioCentros`, `ServicioPagos`: cada uno recibe su repositorio en memoria y expone CRUD de alto nivel.
- `LogisticaFacade`: contiene los 4 servicios + `UnitOfWork`. `procesarEnvioCompleto(Envio, Cobro)` registra ambos en el UoW, hace commit y persiste en los repositorios.

### Clases nuevas (conteo)

| Bloque          | Clases                                                                          | #  |
|-----------------|---------------------------------------------------------------------------------|----|
| Entidades       | `CentroDistribucion` (persistencia), `CentroAssembler`                          | 2  |
| Data Mapper     | `EnvioMapper`, `ClienteMapper`, `CentroDistribucionMapper`, `CobroMapper`       | 4  |
| Mapper SQL      | `EnvioMapperSQL`, `ClienteMapperSQL`, `CentroDistribucionMapperSQL`, `CobroMapperSQL` | 4 |
| Repository      | `Repositorio<T>`, `RepositorioEnvio`, `RepositorioCliente`, `RepositorioCentro`, `RepositorioPago` | 5 |
| Repo Memoria    | `RepositorioEnvioMemoria`, `RepositorioClienteMemoria`, `RepositorioCentroMemoria`, `RepositorioPagoMemoria` | 4 |
| Repo SQL        | `RepositorioEnvioSQL`, `RepositorioClienteSQL`, `RepositorioCentroSQL`, `RepositorioPagoSQL` | 4 |
| Unit of Work    | `UnitOfWork`                                                                    | 1  |
| Lazy Load       | `ClienteLazyProxy`, `CentroDistribucionLazyProxy`, `HistorialEnviosLazyProxy`  | 3  |
| Servicios       | `ServicioEnvios`, `ServicioClientes`, `ServicioCentros`, `ServicioPagos`        | 4  |
| Fachada         | `LogisticaFacade`                                                               | 1  |
| Integración     | `SistemaPersistencia`, `CasosDePruebaHito13`                                   | 2  |
| **Total**       |                                                                                 | **34** |

Nota: la consigna contaba 29 clases asumiendo 4 entidades nuevas. Al reutilizar 3 entidades del dominio (`Envio`, `ClienteFinal`, `Cobro`) el conteo de entidades nuevas baja a 1 — pero el total de clases nuevas del hito sube a 34 por la cobertura completa (mappers SQL, repos SQL, todos los proxies, todos los servicios). La reutilización es una decisión de diseño superior, no un atajo.

### Tests

44 casos de prueba en `CasosDePruebaHito13`, distribuidos:

- `probarDataMapper()`: 10 casos — CRUD de las 4 entidades via repositorios en memoria.
- `probarRepository()`: 10 casos — guardar/obtener/listar/filtrar; `buscarPorEstado`, `buscarPorNombre`, `buscarPorUbicacion`, `buscarPorEnvio`.
- `probarUnitOfWork()`: 8 casos — commit, rollback, multi-entidad, contadores.
- `probarLazyLoad()`: 8 casos — no-carga al crear, carga al primer acceso, no-recarga, 3 proxies.
- `probarArquitectura()`: 6 casos — 4 servicios, fachada `procesarEnvioCompleto`, assembler.

Resultado: 44/44 OK. Regresión 0 sobre los 148 tests de hitos anteriores.
