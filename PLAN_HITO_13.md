# Plan de Trabajo — Hito 13: Patrones de Acceso a Datos

**Proyecto:** LogiSmart — Sistema de Gestión de Logística
**Patrones:** Data Mapper · Repository · Unit of Work · Lazy Load
**Carácter:** Hito final del TPO (integra todo lo anterior)
**Estado de este documento:** planificación previa a la implementación

---

## 1) Objetivo del hito

Diseñar la **capa de persistencia** de LogiSmart separando la lógica de negocio
del almacenamiento, usando los cuatro patrones de acceso a datos:

- **Data Mapper** — mapea objetos de dominio ↔ filas de BD (SQL aislado del dominio).
- **Repository** — acceso uniforme y orientado a colecciones sobre los mappers.
- **Unit of Work** — agrupa cambios y los confirma en una sola transacción atómica.
- **Lazy Load** — carga datos bajo demanda para optimizar rendimiento.

Más la **arquitectura lógica completa** (servicios de aplicación + fachada) que
amarra esta capa con el dominio ya construido en los hitos 1–12.

---

## 2) Metodología (la de siempre)

Replica exacta del flujo usado en hitos 8–12:

1. **Código** en `src/com/logismart/...`
   - Patrones nuevos → bajo `infraestructura/` (paquete dedicado al hito).
   - Integración + casos de prueba → `aplicacion/hito13/`.
2. **Runner**: agregar `CasosDePruebaHito13.ejecutar()` a `app/Main.java`.
3. **Compilación/test** (mismo comando que H12):
   ```bash
   javac -d out $(find src -name '*.java') && java -cp out com.logismart.app.Main
   ```
4. **Documentación a actualizar**:
   - `DOCUMENTACION.md` (sección Hito 13 + conteo de clases).
   - `hitos/HITO_13.html` (entregable visual del hito).
   - `DIAGRAMA_DE_CLASES_ACTUAL.html` (fuente de verdad del diagrama completo).
   - `index.html`.
5. **Skills**: si aparece/cambia algún `clase_*.html`, convertirlo a skill local en
   `skills/<slug-clase>/` con `SKILL.md` + `REFERENCE.md` (regla de `AGENTS.md`).
6. **Convención de tests**: cada caso imprime ✓/✗ y `CasosDePruebaHito13` reporta
   "N casos ejecutados, N OK" al final (igual que H12).

---

## 3) Decisiones de diseño (claves del hito)

### 3.1 Reutilizar el `Envio` del dominio (decisión tomada)

El `Envio` real del proyecto **no es** el `Envio` simplificado de la consigna:

| Aspecto            | `Envio` consigna        | `Envio` real del repo                          |
|--------------------|-------------------------|------------------------------------------------|
| `id`               | `int`                   | `String`                                       |
| Construcción       | constructor de 4 args   | `EnvioBuilder` + estado GoF                     |
| `estado`           | `EstadoEnvio` (enum)    | `String estado` + `EstadoEnvio estadoGoF` (State)|
| Campos             | 6                       | ~20 (empresa, prioridad, órdenes, etc.)         |

**Implicancias para el plan:**

- `EnvioMapper` mapea sólo el **subconjunto persistente** del `Envio` real:
  `id (VARCHAR)`, `origen`, `destino`, `peso`, `estado (String)`, `costo`,
  `metodoPago`, `tipo`. El resto de la riqueza del dominio queda fuera del mapeo
  (no se rompe nada de H1–H12).
- `findById` reconstruye el `Envio` vía `EnvioBuilder` (no con `new Envio(int,...)`).
- La columna `id` de la tabla `envios` es `VARCHAR`, no `INT`.
- `RepositorioEnvio.obtener(...)` y los proxies usan `String id` para envíos.
  → **Atención:** la interfaz genérica de la consigna define `T obtener(int id)`.
  Resolución: mantener `int` para las entidades nuevas (Cliente, Centro, Pago) y
  ofrecer en `RepositorioEnvio` un overload `obtener(String id)`, documentando la
  divergencia. (Alternativa más simple: que `EnvioMapper` use un `int` sintético;
  se evita para no falsear el dominio.)

### 3.2 Entidades nuevas vs. colisiones

- **`Envio`** → se reutiliza el del dominio (ver 3.1).
- **`Cliente`** → **no existe** como entidad de dominio (sólo `ClienteFinal`, que es
  un `Usuario`). Se crea `Cliente` nuevo como entidad de persistencia simple.
- **`CentroDistribucion`** → **ya existe** en `composite/centro/` y en
  `comportamiento/mediator/`. Para evitar colisión se crea la entidad de
  persistencia en un paquete propio (import calificado) o se renombra a
  `CentroDistribucionDatos` dentro del paquete del hito. **Recomendación:** paquete
  dedicado + nombre `CentroDistribucion` calificado por paquete (como se hizo con
  `Nodo*` en Visitor del H12).
- **`Pago`** → no existe entidad (sólo `ProveedorPago`, `ValidadorPago`). Se crea
  `Pago` nuevo.

### 3.3 Ubicación de paquetes (propuesta)

```
src/com/logismart/infraestructura/persistencia/
├── entidad/        # Cliente, CentroDistribucion(datos), Pago, EstadoPago
│                   #  (Envio se reutiliza del dominio)
├── mapper/         # EnvioMapper, ClienteMapper, CentroDistribucionMapper, PagoMapper
├── repositorio/    # interfaces Repositorio<T> + Repositorio{Envio,Cliente,Centro,Pago}
│   ├── sql/        # impl SQL
│   └── memoria/    # impl en memoria (para tests sin BD)
├── unitofwork/     # UnitOfWork
└── lazy/           # ClienteLazyProxy, CentroDistribucionLazyProxy, HistorialEnviosLazyProxy

src/com/logismart/aplicacion/hito13/
├── ServicioEnvios, ServicioClientes, ServicioCentros, ServicioPagos
├── LogisticaFacade
├── SistemaPersistencia            # demo de integración (estilo SistemaLogisticaAvanzada)
└── CasosDePruebaHito13            # runner de tests del hito
```

### 3.4 Tests sin base de datos real

La consigna usa `java.sql.Connection`. El proyecto se compila/ejecuta con
`javac/java` plano (sin dependencias externas, ver H12). **Decisión:**

- Las implementaciones **SQL** se escriben completas (cumplen la consigna y la
  rúbrica "4 interfaces + 8 impl"), pero **los tests corren contra las
  implementaciones en memoria**, que no necesitan driver JDBC.
- Para `UnitOfWork`, que recibe `Connection`, los tests usan un repositorio en
  memoria + una `Connection` nula/stub donde el commit/rollback se simula sobre las
  colecciones internas (o se aísla el flujo transaccional para que sea verificable
  sin BD). Esto se documenta como decisión (igual criterio que "distancia fija 500"
  en Strategy del H12: priorizar tests deterministas).

---

## 4) Inventario de clases (objetivo: 29 según rúbrica)

| Bloque        | Clases                                                                                  | #  |
|---------------|------------------------------------------------------------------------------------------|----|
| Entidades     | `Cliente`, `CentroDistribucion`(datos), `Pago` + enum `EstadoPago` (`Envio` reutilizado) | 3 (+enum) |
| Data Mapper   | `EnvioMapper`, `ClienteMapper`, `CentroDistribucionMapper`, `PagoMapper`                 | 4  |
| Repository    | `Repositorio<T>` + 4 interfaces específicas                                              | 5  |
| Repo SQL      | `RepositorioEnvioSQL`, `...ClienteSQL`, `...CentroSQL`, `...PagoSQL`                      | 4  |
| Repo Memoria  | `RepositorioEnvioMemoria`, `...ClienteMemoria`, `...CentroMemoria`, `...PagoMemoria`     | 4  |
| Unit of Work  | `UnitOfWork`                                                                              | 1  |
| Lazy Load     | `ClienteLazyProxy`, `CentroDistribucionLazyProxy`, `HistorialEnviosLazyProxy`            | 3  |
| Servicios     | `ServicioEnvios`, `ServicioClientes`, `ServicioCentros`, `ServicioPagos`                 | 4  |
| Fachada       | `LogisticaFacade`                                                                         | 1  |
| Integración   | `SistemaPersistencia`, `CasosDePruebaHito13`                                             | 2  |

> La consigna dice "Total: 29 clases" contando entidades=4. Como reutilizamos
> `Envio`, el conteo de **clases nuevas** baja a ~28 + el enum `EstadoPago`. Se
> documenta la equivalencia en `DOCUMENTACION.md` (mismo criterio de aclaración de
> conteo que en el H12).

---

## 5) Plan de tests (meta: 40+ casos, rúbrica "Excelente")

Distribución por actividad (alineada con los entregables de la consigna):

| Método en `CasosDePruebaHito13`   | Cubre                                              | Casos |
|-----------------------------------|----------------------------------------------------|-------|
| `probarDataMapper()`              | insert/find/update/delete de las 4 entidades       | 8–10  |
| `probarRepository()`              | guardar/obtener/obtenerTodos + búsquedas; SQL vs memoria | 10 |
| `probarUnitOfWork()`              | commit exitoso, modificaciones, rollback, multi-entidad, consistencia | 8 |
| `probarLazyLoad()`                | no-carga inicial, carga al 1er acceso, no recarga, 3 proxies | 8 |
| `probarArquitectura()`            | 4 servicios + fachada `procesarEnvioCompleto` + lazy | 8 |
| **Total**                         |                                                    | **42+** |

Cada caso: aserción simple + impresión ✓/✗. Cierre con conteo "42 casos, 42 OK".

---

## 6) Orden de trabajo (cuando se implemente)

1. **Entidades** (`Cliente`, `CentroDistribucion`datos, `Pago`, `EstadoPago`).
2. **Data Mappers** (4) — incluyendo el `EnvioMapper` adaptado al `Envio` real.
3. **Interfaces Repository** (genérica + 4).
4. **Repos en Memoria** (4) — habilitan los tests desde temprano.
5. **Repos SQL** (4) — cumplen rúbrica; no requeridos para correr tests.
6. **Unit of Work** (1) + su flujo commit/rollback verificable sin BD.
7. **Lazy Proxies** (3).
8. **Servicios** (4) + **Fachada** (1).
9. **`SistemaPersistencia`** + **`CasosDePruebaHito13`** (42+ casos).
10. Enganchar en `Main.java`; compilar y correr hasta "42/42 OK" sin romper
    los 148 tests previos.
11. **Documentación**: `DOCUMENTACION.md`, `hitos/HITO_13.html`,
    `DIAGRAMA_DE_CLASES_ACTUAL.html`, `index.html`.
12. **Skills**: convertir cualquier `clase_*.html` nuevo a skill local.
13. **Verificación final**: re-ejecutar todo el `Main`, confirmar regresión 0.

---

## 7) Riesgos y mitigaciones

| Riesgo                                                        | Mitigación                                                                 |
|--------------------------------------------------------------|----------------------------------------------------------------------------|
| `Envio` real ≠ `Envio` consigna (`String` id, Builder)       | Mapper adaptado + overload `obtener(String)`; documentar divergencia       |
| Colisión de nombre `CentroDistribucion`                      | Paquete dedicado de persistencia; imports calificados                      |
| `Connection`/JDBC sin driver en el entorno de compilación    | Tests sobre impl en memoria; SQL escrito pero no ejecutado en tests        |
| Romper los 148 tests de H1–H12                               | No tocar firmas del dominio; sólo **agregar**; correr suite completa        |
| Interfaz genérica `obtener(int)` vs `Envio.id String`        | Decisión explícita (overload) registrada en `DOCUMENTACION.md`             |
| Conteo de clases (29 vs ~28 por reutilizar `Envio`)          | Aclaración de conteo en la doc (precedente del H12)                        |

---

## 8) Entregables finales del hito

- **Código Java**: ~28 clases nuevas + enum, compilando y con 42+ tests en verde.
- **Documento Markdown**: descripción de cada patrón, implementación paso a paso,
  casos de prueba, decisiones de diseño, ventajas/desventajas, integración y
  reflexión sobre arquitectura (sección en `DOCUMENTACION.md`).
- **Diagrama arquitectónico**: 5 capas (Presentación → Aplicación → Dominio →
  Persistencia → Datos) en `hitos/HITO_13.html` + actualización del diagrama global.
- **Skills** actualizadas si corresponde.

---

## 9) Checklist de cierre (rúbrica "Excelente")

- [ ] Data Mapper: 4 mappers completos
- [ ] Repository: 4 interfaces + 8 implementaciones (SQL + memoria)
- [ ] Unit of Work: completo, con transacciones (commit/rollback)
- [ ] Lazy Load: 3 proxies funcionales
- [ ] Servicios: 4 + fachada
- [ ] Diagrama: arquitectura de 5 capas completa
- [ ] Código limpio y documentado
- [ ] Documentación completa con decisiones de diseño
- [ ] 40+ casos de prueba, todos en verde
- [ ] Regresión 0 sobre los 148 tests de hitos previos
