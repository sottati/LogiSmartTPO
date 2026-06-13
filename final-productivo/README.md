# LogiSmart — Versión Final Productiva

LogiSmart es una plataforma SaaS de logística para PyMEs que permite gestionar flotas, planificar rutas, importar pedidos desde e-commerce, registrar entregas y administrar suscripciones. Desarrollada en Java como TPO de Proceso de Desarrollo de Software (UADE 2026), esta carpeta contiene la versión **productiva final**: código limpio, arquitectura en 5 capas, 20 patrones activos (11 núcleo + 9 secundarios, incluyendo GRASP, GoF y PoEAA) y 125 tests organizados por capa. El código fuente académico acumulado (14 hitos, 36 patrones explorados) vive en `src/` en la raíz del repositorio y no debe tocarse.

---

## Arquitectura en 5 capas

```
presentacion/   LogiSmartController — GRASP Controller; único punto de entrada.
                Recibe eventos, verifica permisos (IPermisos/Polymorphism) y delega.

aplicacion/     FacadeProveedoresExternos — orquesta adapters y bridge.
                ServicioImportacion      — flujo CU-01 (Prototype + validación + UoW).
                cadena/                  — Chain of Responsibility: reglas de negocio
                                          (ValidadorDatos → Inventario → Pago → Seguridad → Capacidad).

dominio/        Envio  — Builder, Prototype, State, Observer, Memento, Strategy.
                Usuario/Rol/IPermisos  — Factory Method, GRASP Controller, GRASP Polymorphism.
                centro/  — CentroDistribucion, CentroRegional, SucursalEntrega (Composite).
                Ruta, PuntoEntrega, PosicionGPS — Haversine.
                Empresa, Suscripcion, Cobro, Vehiculo.

persistencia/   RepositorioEnvio + RepositorioEnvioMemoria — Repository.
                EnvioMapper + EnvioMapperMemoria          — Data Mapper.
                ProxyRepositorioEnvio                     — Proxy (cache TTL).
                ClienteLazyProxy                          — Lazy Load.
                UnitOfWork                                — transaccionalidad atómica.

infraestructura/ adapter/   — Adapter: DHL/FedEx/UPS, PayPal/Stripe.
                 bridge/    — Bridge: GeneradorPDF / GeneradorJSON × Reporte.
                 decorator/ — Decorator: Seguro, Prioritario, RastreoGPS.
                 estrategia/— Strategy: Distancia, Peso, Urgencia, Híbrida.
                 fabrica/   — Abstract Factory (AR/BR) + Factory Method (UsuarioFactory).
                 flyweight/ — Flyweight: FabricaUbicaciones (instancias compartidas).
                 observer/  — Observer: NotificadorCliente, Dashboard, Auditoria.
                 singleton/ — Singleton: Logger, ConexionBD.
```

Regla de dependencia: `presentacion → aplicacion → dominio ← persistencia ← infraestructura`. El dominio no conoce a ninguna otra capa.

---

## Patrones aplicados

Criterio de clasificación: **núcleo** = sin estos patrones al menos un CU no puede cumplirse; **secundarios** = suman atributos de calidad pero el sistema puede cumplir los CUs sin ellos.

### 11 patrones núcleo

| Patrón | Tipo | CU / Atributo de calidad |
|---|---|---|
| Controller + Polymorphism (IPermisos) | GRASP | Todos los CUs: `LogiSmartController` es el único punto de entrada; `IPermisos`/`Rol` despacha permisos por tipo de actor sin `instanceof` |
| Chain of Responsibility | GoF Comportamiento | CU-01/crearEnvio: datos → inventario → pago → seguridad → capacidad; falla rápido en el primer rechazo |
| Facade | GoF Estructural | Todos los CUs: `FacadeProveedoresExternos` unifica adapters de envío, pago y reportes en una interfaz simple |
| Abstract Factory | GoF Creacional | CU-02: `LogiSmartFactoryArgentina/Brasil` crea `Vehiculo` + `CalculadorCostos` + `ProveedorMapas` coherentes por región |
| Strategy | GoF Comportamiento | CU-03: `EstrategiaDistancia`/`Peso`/`Urgencia`/`Híbrida` intercambiables en runtime para calcular el costo de ruta |
| State | GoF Comportamiento | CU-04/05/06: `CONFIRMADO → EN_TRANSITO → EN_REPARTO → ENTREGADO`; cada estado concreto decide qué transiciones son válidas |
| Observer | GoF Comportamiento | CU-04/05/06: `NotificadorCliente`, `Dashboard`, `Auditoria` se disparan automáticamente en cada cambio de estado |
| Adapter | GoF Estructural | CU-01/04/06: adapta APIs de DHL/FedEx/UPS y PayPal/Stripe a interfaces internas uniformes sin tocar el dominio |
| Repository + Data Mapper | PoEAA | CU-01/04/05/06: `RepositorioEnvio` abstrae la colección; `EnvioMapper` mapea objetos ↔ filas sin contaminar el dominio |
| Unit of Work | PoEAA | CU-01/04/05/06: agrupa inserciones y modificaciones en una transacción atómica (BEGIN/COMMIT/ROLLBACK) |
| Proxy + Lazy Load | PoEAA | CU-06/07: `ProxyRepositorioEnvio` cachea con TTL=60s; `ClienteLazyProxy` difiere la carga de datos pesados al primer acceso |

### 9 patrones secundarios

| Patrón | Tipo | CU / Atributo de calidad |
|---|---|---|
| Builder | GoF Creacional | Todos los CUs: `Envio.EnvioBuilder` garantiza construcción válida e inmutabilidad de campos obligatorios |
| Memento | GoF Comportamiento | CU-04/05/06: `HistorialEnvios` guarda snapshot antes de cada transición; revierte si `UoW.commit()` falla |
| Prototype | GoF Creacional | CU-01: `prototipo.clone()` por cada `PedidoEcommerce`; preserva origen/empresa sin reconstruir desde cero |
| Singleton | GoF Creacional | Sistema: `Logger` y `ConexionBD` garantizan instancia única thread-safe en toda la aplicación |
| Bridge | GoF Estructural | `generarReporteAdmin`: desacopla tipo de reporte (`ReporteEnvios`/`ReporteIngresos`) de formato de salida (`PDF`/`JSON`) |
| Decorator | GoF Estructural | Servicios opcionales sobre `Envio` (Seguro ×1.15, Prioritario ×1.30, GPS +$50) sin explotar subclases |
| Composite | GoF Estructural | CU-02/04: `CentroRegional` y `SucursalEntrega` forman árbol de distribución con capacidad y ocupación recursivas |
| Flyweight | GoF Estructural | CU-07: `FabricaUbicaciones` comparte instancias de ciudad/provincia; evita duplicados por consulta de ubicación |
| Factory Method | GoF Creacional | `UsuarioFactory.crear(tipo)`: desacopla la creación de usuarios de su tipo concreto (GRASP Polymorphism) |

> Para las decisiones de descarte de 6 patrones GoF, ver [DECISIONES.html](../DECISIONES.html).

---

## 8 Casos de Uso implementados

| # | Caso de Uso | Actor principal | Patrones clave |
|---|---|---|---|
| CU-01 | Importar Pedidos desde E-commerce | Operador Logístico | Prototype, Chain, UoW |
| CU-02 | Gestionar Flota y Capacidades | Operador Logístico | Abstract Factory, Composite |
| CU-03 | Planificar Rutas Óptimas | Operador Logístico | Strategy, ProveedorMapas, Haversine |
| CU-04 | Asignar Hoja de Ruta a Chofer | Operador Logístico | State, Memento, Observer, UoW, Composite |
| CU-05 | Iniciar Recorrido de Entrega | Chofer / Transportista | State, Memento, Observer, UoW |
| CU-06 | Registrar Entrega de Pedido | Chofer / Transportista | Proxy, State, Memento, Observer, UoW |
| CU-07 | Consultar Ubicación del Pedido | Cliente Final | Flyweight |
| CU-08 | Administrar Suscripción / Billing | Administrador de Plataforma | Suscripcion domain object |

---

## Cómo compilar y ejecutar

```bash
# Desde final-productivo/
javac -d bin -cp bin @sources.txt

# Correr suite por capa
java -cp bin dominio.TestsDominio
java -cp bin tests.infraestructura.TestsInfraestructura
java -cp bin tests.aplicacion.TestsAplicacion
java -cp bin tests.persistencia.TestsPersistencia
java -cp bin tests.presentacion.TestsPresentacion
```

`sources.txt` lista todos los `.java` en orden correcto (no requiere herramienta de build externa). Las suites son auto-contenidas: no dependen de JUnit, imprimen `[OK]` / `[FALLO]` y cierran con el conteo total.

---

## Resultado de tests

| Suite | Capa | Casos | Resultado |
|---|---|---|---|
| `TestsDominio` | dominio | 32 | ✓ 32 OK |
| `TestsInfraestructura` | infraestructura | 28 | ✓ 28 OK |
| `TestsAplicacion` | aplicacion | 17 | ✓ 17 OK |
| `TestsPersistencia` | persistencia | 18 | ✓ 18 OK |
| `TestsPresentacion` | presentacion | 30 | ✓ 30 OK |
| **Total** | **5 capas** | **125** | **✓ 125 OK — 0 FAIL** |

---

## Entregable productivo vs evidencia académica

| Carpeta | Rol |
|---|---|
| `final-productivo/` | **Entregable productivo** — arquitectura limpia, 5 capas, 20 patrones activos, 125 tests |
| `src/` (raíz) | **Evidencia académica** — 14 hitos acumulados, 36 patrones explorados; no tocar |
| `hitos/` | Documentación de proceso por hito (HTML navegables) |
| `index.html` | Página de presentación del TPO |
| `DECISIONES.html` | Justificaciones de diseño y descartes |
