# Plan de Trabajo — Hito 13: Patrones de Acceso a Datos

**Proyecto:** LogiSmart — Sistema de Gestión de Logística
**Patrones:** Data Mapper · Repository · Unit of Work · Lazy Load
**Carácter:** Hito final del TPO (integra todo lo anterior)
**Estado de este documento:** planificación previa a la implementación
**Última revisión:** incorpora reutilización de entidades, mitigación de desfasaje y foco de diseño del TPO.

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

## 2) Qué pide realmente el TPO (encuadre)

El documento maestro del TPO es explícito:

> "La implementación no es el foco, pero el diseño debe ser expresado principalmente
> a través de diagramas UML (Clases, Secuencia, Casos de Uso)... y la justificación
> de las decisiones de diseño."

Y el hito de persistencia está descrito como:

> "Diseño de la Capa de Persistencia: **Diagrama de clases** para la capa de datos
> aplicando patrones como Repository o Data Mapper."

**Conclusión:** el entregable que el profesor puntúa es el **diagrama de clases de la
capa de datos + la justificación de decisiones**. El código JDBC de la consigna es
ilustrativo. Implementar y ejecutar el proyecto de verdad (con `javac`/`java`) es una
**decisión propia del equipo, por encima de lo pedido** — y, como tal, fuente de
aprendizaje real y de los desafíos que se documentan abajo.

---

## 3) Metodología (la de siempre)

Replica del flujo usado en hitos 8–12:

1. **Código** en `src/com/logismart/...`
   - Patrones nuevos → bajo `infraestructura/persistencia/`.
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

## 4) Decisiones de diseño

### 4.1 Reutilizar el dominio existente (decisión central)

La consigna trae código de ejemplo de 4 entidades "puras" (`Envio`, `Cliente`,
`CentroDistribucion`, `Pago`), pero **no exige crearlas nuevas**: es una plantilla
genérica que asume que no existen. Tras 12 hitos, LogiSmart ya modela casi todas.
Decisión: **reutilizar lo que ya existe; crear solo lo que falta.**

| Entidad consigna     | Resolución                              | Acción                                  |
|----------------------|-----------------------------------------|-----------------------------------------|
| `Envio`              | Reutilizar `dominio/Envio` (Builder, `String id`, estado GoF) | Mapper adaptado al `Envio` real |
| `Cliente`            | Reutilizar `dominio/ClienteFinal` (es un `Usuario`) | Mapper persiste subconjunto      |
| `Pago`               | Reutilizar `dominio/Cobro`              | Agregar campo `envioId` (aditivo)        |
| `CentroDistribucion` | **Crear** entidad de persistencia nueva | Única clase de entidad nueva del hito    |

Resultado: se reutilizan **3** entidades y se crea **1**.

### 4.2 `Envio`: adaptar el mapper al dominio real

El `Envio` real no es el de la consigna:

| Aspecto      | `Envio` consigna       | `Envio` real del repo                              |
|--------------|------------------------|----------------------------------------------------|
| `id`         | `int`                  | `String`                                           |
| Construcción | constructor 4 args     | `EnvioBuilder` + estado GoF                        |
| `estado`     | `EstadoEnvio` (enum)   | `String estado` + `EstadoEnvio estadoGoF` (State)  |
| Campos       | 6                      | ~20 (empresa, prioridad, órdenes, etc.)            |

- `EnvioMapper` mapea el **subconjunto persistente**: `id (VARCHAR)`, `origen`,
  `destino`, `peso`, `estado (String)`, `costo`, `metodoPago`, `tipo`.
- `findById` reconstruye el `Envio` vía `EnvioBuilder` (no con `new Envio(int,...)`).
- La columna `id` de la tabla `envios` es `VARCHAR`, no `INT`.

### 4.3 `ClienteFinal` como `Cliente`

`ClienteFinal extends Usuario` ya tiene `nombre`, `email` (heredado de `Usuario`) y
`telefono` — los tres campos del `Cliente` de la consigna — más `direccionEntrega`.
Conceptualmente son lo mismo; la consigna lo modela como registro plano y el proyecto
como usuario con login + permisos.

- `ClienteMapper` persiste el subconjunto `id (VARCHAR)`, `nombre`, `email`, `telefono`.
- Ignora `passwordHash`, `rol`, `estado` y permisos (no son responsabilidad de este
  mapper). Se documenta la decisión.

### 4.4 `Cobro` como `Pago`

`Cobro` ya existe con `id`, `monto`, `estado`, `fecha`, `medioPago` y comportamiento
(`autorizar`, `registrarPago`, `marcarFallido`, `emitirComprobante`). Es el concepto
"pago" ya resuelto. Ajustes:

- **Estado**: se mantiene como `String`. **No** se introduce el enum `EstadoPago`:
  sus valores ni coinciden (`AUTORIZADO/PAGADO/FALLIDO` vs
  `PENDIENTE/PROCESANDO/COMPLETADO/RECHAZADO`) y forzarlo cambiaría el comportamiento
  ya testeado de `Cobro`, con riesgo de regresión. (Nota histórica: `EstadoPago`
  no fue pedido en ninguna consigna previa; solo aparece en el ejemplo del Hito 13.
  El Hito 2 dejó abierta la pregunta "¿Pago clase o atributo?", resuelta con `Cobro`.)
- **Vínculo con el envío**: se agrega un campo `envioId` a `Cobro` de forma **aditiva**
  (constructor/​setter opcional; `Cobro` es una clase de datos plana sin subclases →
  riesgo mínimo). Habilita `RepositorioPago.buscarPorEnvio(envioId)` limpio.

### 4.5 `CentroDistribucion`: separar dominio de persistencia, **con mitigación de desfasaje**

Los `CentroDistribucion` existentes no sirven como entidad de persistencia:

- `composite/centro/CentroDistribucion` es **abstracto** (patrón Composite); su
  `ocupacion`/`capacidad` se **calculan** sumando hijos, no son campos almacenados.
- `comportamiento/mediator/CentroDistribucion` es un colega del Mediator, no un dato.

Decisión: crear una entidad de persistencia plana `CentroDistribucion` en el paquete
de persistencia (los paquetes Java desambiguan; sin sufijos). Esto **separa el modelo
de dominio** (Composite, rico, jerárquico) del **modelo de persistencia** (fila plana).

**Trade-off reconocido:** la separación introduce duplicación y un riesgo de
**desfasaje** (el snapshot plano podría divergir del Composite).

**Mitigación (no se asume el riesgo, se neutraliza):**

- El **Composite es la única fuente de verdad** de `capacidad`/`ocupacion`.
- La entidad de persistencia **no se edita por su cuenta**; se **genera como
  proyección** desde el Composite al momento de guardar, vía un *assembler*
  (`CentroAssembler.aPersistencia(centroComposite)`) que lee `obtenerCapacidad()` y
  `obtenerOcupacion()` y los vuelca a la fila.
- Por construcción, el snapshot siempre se recalcula desde el dominio → no puede
  divergir. La fila es una vista materializada, no un dato editable en paralelo.

### 4.6 Interfaz genérica `obtener(int)` vs `id` String

`Repositorio<T>` define `T obtener(int id)`, pero `Envio`, `ClienteFinal` y `Cobro`
usan `String id`. Tensión entre la genericidad "linda" y la fidelidad al dominio.

- Resolución: para las entidades con `String id`, los repositorios específicos
  exponen un overload `obtener(String id)`; la firma `int` queda para la entidad
  nueva (`CentroDistribucion`) o se documenta la divergencia. Se justifica en
  `DOCUMENTACION.md`.

### 4.7 JDBC vs base de datos: SQL ilustrativo, tests en memoria

`java.sql.*` (Connection, PreparedStatement) **es parte del JDK**: los Data Mapper SQL
**compilan con `javac` pelado** y cuentan como clases del hito. Solo no pueden
**conectarse** sin un driver y una base corriendo.

- Las impl **SQL** se escriben y compilan (demuestran dominio de JDBC) pero **no se
  ejecutan** en los tests (no hay base en el build).
- Los tests automáticos corren contra las impl **en memoria** (`HashMap`),
  deterministas y sin dependencias externas.
- El **diagrama de clases** muestra la arquitectura completa respaldada por SQL; las
  impl en memoria quedan anotadas como dobles de prueba.
- Mismo criterio que el Hito 12 (distancia fija 500 para tests deterministas).

### 4.8 Ubicación de paquetes (propuesta)

```
src/com/logismart/infraestructura/persistencia/
├── entidad/        # CentroDistribucion (persistencia) + assembler
│                   #  (Envio, ClienteFinal y Cobro se reutilizan del dominio)
├── mapper/         # EnvioMapper, ClienteMapper, CentroDistribucionMapper, CobroMapper
├── repositorio/    # interfaces Repositorio<T> + Repositorio{Envio,Cliente,Centro,Pago}
│   ├── sql/        # impl SQL (compiladas, no ejecutadas en tests)
│   └── memoria/    # impl en memoria (testeadas)
├── unitofwork/     # UnitOfWork
└── lazy/           # ClienteLazyProxy, CentroDistribucionLazyProxy, HistorialEnviosLazyProxy

src/com/logismart/aplicacion/hito13/
├── ServicioEnvios, ServicioClientes, ServicioCentros, ServicioPagos
├── LogisticaFacade
├── SistemaPersistencia            # demo de integración (estilo SistemaLogisticaAvanzada)
└── CasosDePruebaHito13            # runner de tests del hito
```

---

## 5) Inventario de clases

| Bloque        | Clases                                                                                   | #  |
|---------------|-------------------------------------------------------------------------------------------|----|
| Entidades     | `CentroDistribucion` (persistencia) + `CentroAssembler` (Envio/ClienteFinal/Cobro reusados) | 2 |
| Data Mapper   | `EnvioMapper`, `ClienteMapper`, `CentroDistribucionMapper`, `CobroMapper`                 | 4  |
| Repository    | `Repositorio<T>` + 4 interfaces específicas                                               | 5  |
| Repo SQL      | `RepositorioEnvioSQL`, `...ClienteSQL`, `...CentroSQL`, `...PagoSQL`                       | 4  |
| Repo Memoria  | `RepositorioEnvioMemoria`, `...ClienteMemoria`, `...CentroMemoria`, `...PagoMemoria`      | 4  |
| Unit of Work  | `UnitOfWork`                                                                               | 1  |
| Lazy Load     | `ClienteLazyProxy`, `CentroDistribucionLazyProxy`, `HistorialEnviosLazyProxy`             | 3  |
| Servicios     | `ServicioEnvios`, `ServicioClientes`, `ServicioCentros`, `ServicioPagos`                  | 4  |
| Fachada       | `LogisticaFacade`                                                                          | 1  |
| Integración   | `SistemaPersistencia`, `CasosDePruebaHito13`                                              | 2  |

> La consigna dice "Total: 29 clases" contando 4 entidades nuevas. Al reutilizar 3
> entidades, el conteo de **clases nuevas** baja (~30 con mappers/repos/proxies/servicios,
> pero solo 1 entidad nueva + 1 assembler). Esto se documenta como **decisión de diseño
> superior** (reutilización), no como un atajo. Mismo criterio de aclaración de conteo
> que en el H12. Además, las entidades reutilizadas **no son "puras"** (arrastran
> herencia/comportamiento), lo cual es más realista y se justifica en la doc.

---

## 6) Plan de tests (meta: 40+ casos)

| Método en `CasosDePruebaHito13`   | Cubre                                                    | Casos |
|-----------------------------------|----------------------------------------------------------|-------|
| `probarDataMapper()`              | insert/find/update/delete sobre las 4 entidades (memoria)| 8–10  |
| `probarRepository()`              | guardar/obtener/obtenerTodos + búsquedas; SQL vs memoria | 10    |
| `probarUnitOfWork()`              | commit, modificaciones, rollback, multi-entidad, consistencia | 8 |
| `probarLazyLoad()`                | no-carga inicial, carga al 1er acceso, no recarga, 3 proxies | 8 |
| `probarArquitectura()`            | 4 servicios + fachada `procesarEnvioCompleto` + lazy + assembler | 8 |
| **Total**                         |                                                          | **42+** |

Cada caso: aserción simple + impresión ✓/✗. Cierre con conteo "42 casos, 42 OK".
Tests corren contra impl en memoria (sin BD).

---

## 7) Orden de trabajo

1. **Entidad `CentroDistribucion`** (persistencia) + **`CentroAssembler`** (proyección
   desde el Composite, mitigación de desfasaje).
2. **Ajuste aditivo a `Cobro`**: campo `envioId` (constructor/​setter opcional).
3. **Data Mappers** (4) — `EnvioMapper` adaptado al `Envio` real; `ClienteMapper`
   sobre `ClienteFinal`; `CobroMapper`; `CentroDistribucionMapper`.
4. **Interfaces Repository** (genérica + 4, con overload `obtener(String)` donde aplique).
5. **Repos en Memoria** (4) — habilitan los tests desde temprano.
6. **Repos SQL** (4) — compilan; no requeridos para correr tests.
7. **Unit of Work** (1) + flujo commit/rollback verificable sin BD.
8. **Lazy Proxies** (3).
9. **Servicios** (4) + **Fachada** (1).
10. **`SistemaPersistencia`** + **`CasosDePruebaHito13`** (42+ casos).
11. Enganchar en `Main.java`; compilar y correr hasta "42/42 OK" **sin romper los
    148 tests previos**.
12. **Diagrama de clases de la capa de persistencia** (entregable estrella) +
    actualización del diagrama global, `DOCUMENTACION.md`, `hitos/HITO_13.html`,
    `index.html`.
13. **Skills**: convertir cualquier `clase_*.html` nuevo a skill local.
14. **Verificación final**: re-ejecutar todo el `Main`, confirmar regresión 0.

---

## 8) Riesgos y mitigaciones

| Riesgo                                                        | Mitigación                                                                                 |
|--------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| `Envio` real ≠ `Envio` consigna (`String` id, Builder)       | Mapper adaptado + reconstrucción con `EnvioBuilder`; `id` VARCHAR                            |
| Colisión de nombre `CentroDistribucion`                      | Entidad de persistencia en paquete dedicado; imports calificados                            |
| **Desfasaje** entidad de persistencia ↔ Composite           | Composite = única fuente de verdad; entidad generada por *assembler* como proyección, nunca editada por su cuenta |
| `Cobro` sin `envioId` / estados distintos al enum            | `envioId` aditivo; `estado` se mantiene `String` (no se fuerza `EstadoPago`)                |
| JDBC sin base de datos en el build                           | SQL compila (java.sql es JDK) pero no se ejecuta; tests sobre impl en memoria               |
| Romper los 148 tests de H1–H12                               | Solo **agregar**, nunca modificar firmas del dominio; correr suite completa al final         |
| Interfaz genérica `obtener(int)` vs `id` String              | Overload `obtener(String)` documentado en `DOCUMENTACION.md`                                |
| Conteo de clases (29 vs reutilización)                       | Documentar reutilización como decisión de diseño superior, con aclaración de conteo (precedente H12) |
| Entidades reutilizadas no son "puras"                        | Justificar que la fidelidad al dominio real prima sobre la pureza del ejemplo de cátedra    |

---

## 9) Entregables finales del hito

- **Diagrama de clases de la capa de persistencia** (entregable que el TPO puntúa):
  5 capas (Presentación → Aplicación → Dominio → Persistencia → Datos), mostrando
  Data Mapper, Repository, Unit of Work, Lazy Load y la relación dominio↔persistencia.
- **Documento Markdown**: descripción de cada patrón, implementación paso a paso,
  casos de prueba, decisiones de diseño, ventajas/desventajas, integración y reflexión
  (sección en `DOCUMENTACION.md`).
- **Código Java**: ~30 clases (1 entidad nueva + assembler + mappers/repos/UoW/proxies/
  servicios/fachada), compilando y con 42+ tests en verde, regresión 0.
- **Diagrama global y `hitos/HITO_13.html`** actualizados.
- **Skills** actualizadas si corresponde.

---

## 10) Checklist de cierre (rúbrica "Excelente")

- [ ] Data Mapper: 4 mappers completos
- [ ] Repository: 4 interfaces + 8 implementaciones (SQL compiladas + memoria testeadas)
- [ ] Unit of Work: completo, con transacciones (commit/rollback)
- [ ] Lazy Load: 3 proxies funcionales
- [ ] Servicios: 4 + fachada
- [ ] Entidad `CentroDistribucion` + assembler (mitigación de desfasaje implementada)
- [ ] `Cobro` con `envioId` aditivo, sin romper comportamiento existente
- [ ] Diagrama de clases de la capa de persistencia (entregable estrella)
- [ ] Código limpio y documentado con decisiones justificadas
- [ ] 40+ casos de prueba, todos en verde
- [ ] Regresión 0 sobre los 148 tests de hitos previos

---

## 11) Material de "experiencia" para la defensa (a desarrollar aparte)

Momentos del Hito 13 que sirven para contar el *proceso vivido* (no el trabajo, sino
las decisiones y desafíos):

- **"La consigna asumía entidades nuevas; descubrimos que ya teníamos casi todas."**
  El hito de persistencia se volvió un ejercicio de reutilización.
- **Una pregunta del Hito 2 que se cierra recién acá**: "¿Pago clase o atributo?" →
  resuelta con `Cobro`, confirmada al mapearlo en el Hito 13.
- **"Implementamos más de lo que el profe pedía"**: él pedía diseño en papel; correr
  el código de verdad nos dio aprendizaje, pero también el problema de testear sin
  base de datos, que resolvimos con impl en memoria.
- **El choque dominio vs persistencia** en `CentroDistribucion`: aceptamos la
  separación, detectamos el riesgo de desfasaje y lo **mitigamos** con el assembler.
- **Disciplina sostenida 13 hitos**: regresión 0, "solo agregar, nunca romper".
