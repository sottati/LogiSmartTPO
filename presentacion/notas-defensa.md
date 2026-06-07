# LogiSmart — Notas de defensa TPO
*Hilo conductor: Consigna → Planteamos → Código*

---

## 0. La consigna en una línea

El TPO pide aplicar **RUP** (proceso iterativo e incremental, guiado por CUs, centrado en arquitectura) para diseñar una solución **flexible, mantenible y escalable**. El foco no es la implementación final sino la **calidad del diseño y la justificación de las decisiones**. El caso de negocio lo fijó la cátedra: *"LogiSmart — Plataforma de Optimización Logística para PyMEs"*.

---

## 1. El negocio — consigna → planteamos → código

### Consigna
La cátedra proveyó el caso en la Clase 1. El punto de partida: una PyME que gestiona su logística a mano (planillas, WhatsApp) y necesita un SaaS que la digitalice.

### Qué planteamos (Hito 1)
Definimos un modelo de negocio con dos capas:

**Capa de negocio:**
```
Consultora Logística (inversora) → contrata → Consultora IT (nosotros)
                                              → desarrolla → LogiSmart
                                                             → sirve a → PyMEs
```

**Capa de sistema:** LogiSmart como SaaS multitenant — múltiples PyMEs comparten la misma plataforma, cada una con sus datos aislados.

**Aclaración importante que surgió al analizar:**
La "Consultora Logística" es solo la **inversora/sponsor**. No opera la plataforma. Su interés es el retorno de inversión vía el fee mensual que pagan las PyMEs. No es un actor del sistema — es un stakeholder.

**¿Qué es una PyME en este contexto?** No es una persona — es una empresa cliente del SaaS (una distribuidora, una empresa de mensajería). Dentro de cada PyME hay personas con distintos roles en el sistema.

### Cómo se tradujo al código
El concepto de multitenant se implementó con el enum `Rol` y la interfaz `IPermisos` en la capa de Presentación. El `LogiSmartController` valida permisos antes de cualquier operación — ninguna PyME puede ver datos de otra porque el acceso se filtra en la entrada.

---

## 2. Los actores — consigna → planteamos → código

### Consigna
RUP requiere identificar actores antes de definir CUs. La cátedra no especificó quiénes eran — eso era parte del análisis.

### Qué planteamos (Hito 1)
Definimos 5 actores internos y 3 sistemas externos:

| Actor | Tipo | Descripción |
|---|---|---|
| Operador Logístico | Primario | Gestiona envíos, rutas, flota |
| Administrador PyME | Primario | Dueño/gerente — configura cuenta, ve reportes |
| Administrador de Plataforma (LogiSmart) | Primario | Equipo LogiSmart — alta/baja PyMEs, billing |
| Transportista / Conductor | Primario | Ejecuta rutas, actualiza posición GPS |
| Cliente Final | Primario | Sigue su envío en tiempo real |
| Sistemas E-commerce | Externo | TiendaNube, MercadoShops |
| API de Mapas | Externo | Google Maps, etc. |
| Pasarela de Pagos | Externo | Cobro del fee mensual |

**Bug encontrado al revisar:** el CU-08 tenía como actor "Admin Consultora" en vez de "Administrador de Plataforma (LogiSmart)". La palabra "Consultora" se coló porque en el contexto del TPO el equipo de desarrollo era una consultora IT — pero ese rol no opera el sistema productivo, lo construyó. **Corregido en HITO_1.html.**

**Distinción clave — AdminEmpresa vs. AdminPlataforma:**
- **AdminEmpresa** = dueño de *una* PyME. Es cliente del SaaS.
- **AdminPlataforma** = equipo de LogiSmart que opera *toda* la plataforma. Es el que cobra el fee y da de alta a las PyMEs.
Son análogos a "inquilino" vs. "dueño del edificio".

### Cómo se tradujo al código

Cada actor tiene su propia clase Java que extiende `Usuario` e implementa `IPermisos`:

| Actor planteado | Clase Java | Rol enum | Métodos que lo definen |
|---|---|---|---|
| Operador Logístico | `OperadorLogistico` | `OPERADOR` | `importarOrdenes()` · `planificarRuta()` · `crearEnvio()` · `gestionarFlota()` |
| Administrador PyME | `AdminEmpresa` | `ADMIN_EMPRESA` | `configurarEmpresa()` · `consultarReportes()` · `gestionarSuscripcion()` |
| Admin. de Plataforma (LogiSmart) | `AdminPlataforma` | `ADMIN_PLATAFORMA` | `darAltaEmpresa()` · `darBajaEmpresa()` · `gestionarBilling()` · `monitorearServicio()` |
| Transportista | `Transportista` | `TRANSPORTISTA` | `iniciarRecorrido()` · `actualizarPosicion()` · `registrarEntrega()` · `reportarIncidente()` |
| Cliente Final | `ClienteFinal` | `CLIENTE` | `consultarTracking()` · `verETA()` · `confirmarRecepcion()` |

La matriz de permisos completa (único lugar donde vive — GRASP Information Expert):

```java
//                              crear  asignar reportes flota  adminEmpresas
OPERADOR         (true,  true,  true,  true,  false)
ADMIN_EMPRESA    (true,  false, true,  false, false)
ADMIN_PLATAFORMA (true,  true,  true,  true,  true)   // ← CU-08
CLIENTE          (false, false, false, false, false)
TRANSPORTISTA    (false, false, false, false, false)
```

**Alineación perfecta:** los métodos de cada clase corresponden exactamente a lo que el Hito 1 describió para ese actor. El `AdminPlataforma` tiene `darAltaEmpresa()` + `gestionarBilling()` que mapean directo al CU-08.

---

## 3. Los Casos de Uso — consigna → planteamos → código

### Consigna
RUP pide identificar CUs para guiar el diseño. La cátedra pidió un catálogo completo en el Hito 1 y el desarrollo detallado de los 3 más importantes.

### Qué planteamos (Hito 1)
Definimos 8 CUs. Seleccionamos 3 como foco del MVP por su impacto de negocio visible:

| CU | Nombre | Actor | ¿MVP? |
|---|---|---|---|
| CU-01 | Importar Pedidos E-commerce | Operador Logístico | ✓ MVP |
| CU-02 | Gestionar Flota y Capacidades | Operador Logístico | |
| CU-03 | Planificar Rutas Óptimas | Operador Logístico | ✓ MVP |
| CU-04 | Asignar Hoja de Ruta a Chofer | Operador Logístico | |
| CU-05 | Iniciar Recorrido de Entrega | Transportista | |
| CU-06 | Registrar Entrega de Pedido | Transportista | |
| CU-07 | Consultar Ubicación del Pedido | Cliente Final | ✓ MVP |
| CU-08 | Administrar Suscripción (Billing) | **Admin. de Plataforma** | |

Los 3 del MVP son los de mayor impacto visible: eliminan la carga manual (CU-01), optimizan rutas (CU-03) y dan trazabilidad al cliente final (CU-07).

### Cómo se tradujo al código

La implementación fue mucho más allá de los 3 CUs seleccionados. Al verificar el código, el resultado fue:

| CU | Estado en código | Evidencia |
|---|---|---|
| CU-01 | ⚠️ Parcial | `importarOrdenes()` y `agregarOrden()` existen, pero no hay adapter de TiendaNube/MercadoShops |
| CU-02 | ✓ Completo | `registrarVehiculoEnFlota()` · `Flota` · `FabricaDeVehiculos` · `FabricaDeNotificadores` |
| CU-03 | ✓ Completo | `CalculadorDeRutas.seleccionarRuta()` · 3 estrategias de ruta |
| CU-04 | ✓ Completo | `LogiSmartController.asignarRuta()` vincula ruta a `Transportista` |
| CU-05 | ✓ Completo | `Transportista.iniciarRecorrido()` · `actualizarPosicion()` |
| CU-06 | ✓ Completo | `Transportista.registrarEntrega()` · `reportarIncidente()` |
| CU-07 | ✓ Completo | `ClienteFinal.consultarTracking()` · `verETA()` · `TrackingService` |
| CU-08 | ✓ Completo | `AdminPlataforma.darAltaEmpresa/Baja()` · `gestionarBilling()` |

**Prometimos 3, entregamos 7.** Eso es algo a destacar en la defensa.

**¿Por qué quedó incompleto CU-01?** Porque la cátedra nunca pidió explícitamente un adapter de e-commerce. Los adapters que aparecieron en las consignas fueron para carriers (DHL/FedEx/UPS) y pagos (PayPal/Stripe). El comportamiento del dominio existe, pero falta el conector con TiendaNube/MercadoShops.

---

## 4. Los hitos — cómo la consigna generó la implementación

### La pregunta que surgió
> ¿La implementación de los 8 CUs fue una decisión propia o fue consecuencia de lo que la cátedra fue pidiendo en cada hito?

Sí. No fue una decisión deliberada de "vamos a implementar todos los CUs" — fue consecuencia natural del proceso iterativo. Cada patrón pedido requería modelar una parte del dominio, y esa parte era funcionalidad de algún CU.

### La cadena causal

| Hito | Qué pidió la consigna | CUs que se habilitaron como consecuencia |
|---|---|---|
| 1 | Catálogo de CUs + actores | Define el roadmap |
| 2 | Identificar bad smells en diseño inicial | Refuerza el diseño base |
| 4–5 | GRASP: Expert, Creator, Controller, Low Coupling | Para modelar `Envio`, `Ruta`, `Flota`, `Vehiculo`, `Transportista` con Expert/Creator → CU-02, 03, 04, 05, 06 |
| 6–7 | Creacionales: Singleton, Factory Method, Abstract Factory, Builder | `FabricaDeVehiculos` → CU-02. `Builder` para `Envio` → CU-03. `AbstractFactory` regional (AR/BR) → dimensión geográfica |
| 8–9 | Estructurales: Adapter, Bridge, Composite, Decorator, Facade, Proxy | `Adapter` DHL/FedEx/UPS + PayPal/Stripe → CU-08. `Chain` de validadores → prerequisito de todos |
| 10–12 | Comportamiento I/II/III: Chain, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template, Visitor | `State` + `Observer` + `TrackingService` → CU-07. `Command` con undo → CU-06 |
| 13 | Persistencia: Repository, Data Mapper, Unit of Work, Lazy Load | `darAltaEmpresa()` + `gestionarBilling()` con capa de datos → CU-08 completo |

**La conclusión para la defensa:** esto demuestra fidelidad al enfoque iterativo e incremental de RUP. Cada iteración agregó funcionalidad sin romper lo anterior — la prueba son los 148 tests siempre en verde.

---

## 5. Los patrones — consigna → planteamos → código

### Consigna
La cátedra pedía patrones opcionales por hito ("Singleton **o** Factory Method"). Implementamos **todos**, no solo uno.

### Distribución final

| Familia | Patrones | Cantidad |
|---|---|---|
| GRASP | Expert · Creator · Low Coupling · High Cohesion · Controller · Polymorphism · Pure Fabrication · Indirection · Protected Variations | 9 |
| GoF Creacionales | Singleton · Factory Method · Abstract Factory · Builder · Prototype | 5 |
| GoF Estructurales | Adapter · Bridge · Composite · Decorator · Facade · Flyweight · Proxy | 7 |
| GoF Comportamiento | Chain · Command · Interpreter · Iterator · Mediator · Memento · Observer · State · Strategy · Template Method · Visitor | 11 |
| Persistencia (PoEAA) | Repository · Data Mapper · Unit of Work · Lazy Load | 4 |
| **Total** | | **36** |

El Hito 12 lo confirma internamente: *"23 patrones GoF acumulados"* (sin contar GRASP ni persistencia).

### Cómo vive en el código — estructura de paquetes

```
src/com/logismart/
├── dominio/              ← entidades + reglas de negocio
├── aplicacion/           ← orquestación de CUs (Controller, Facade, Services)
└── infraestructura/
    ├── singleton/        ← ConexionBD, Logger
    ├── abstractfactory/  ← FactoryArgentina, FactoryBrasil
    ├── adapter/envio/    ← DHL, FedEx, UPS
    ├── adapter/pago/     ← PayPal, Stripe
    ├── bridge/reporte/   ← PDF, Excel, JSON, CSV
    ├── composite/centro/ ← red de centros de distribución
    ├── decorator/envio/  ← Seguro, GPS, Prioritario, SMS
    ├── proxy/envio/      ← cache + lazy loading
    ├── flyweight/        ← Ubicaciones compartidas
    ├── comportamiento/   ← chain, command, iterator, mediator, memento,
    │                        observer, state, strategy, template, visitor
    └── persistencia/     ← repositorio, mapper, unitofwork, lazy
```

---

## 6. Decisiones de diseño — las más importantes para mencionar

### La arquitectura en 5 capas y sus atributos de calidad

| Capa | Atributo que sostiene | Patrón clave |
|---|---|---|
| Presentación | Aislamiento de datos (multitenant) | Controller + Polymorphism (Rol) |
| Aplicación | Alta disponibilidad | Facade + Chain of Responsibility |
| Dominio | Trazabilidad en tiempo real | State + Observer + Memento |
| Persistencia | Eficiencia de respuesta | Repository + Data Mapper + Proxy |
| Infraestructura | Interoperabilidad + Elasticidad | Adapter + Abstract Factory + Flyweight |

### El refactoring post-Hito 12 (la "tensión del final")
Llegando al Hito 13 había mucho código que no queríamos romper. El refactoring se hizo con 148 tests en verde. Cambios concretos:
- **Enum `Rol`**: eliminó 25 booleanos duplicados. Matriz de permisos 5×5 en un único lugar.
- **`Haversine` en `PosicionGPS`**: reemplazó fórmula euclidiana por cálculo real sobre la esfera.
- **Information Expert**: `Vehiculo.getCostoBaseKm()` y `Ruta` delegando al experto real.

### Sacrificios deliberados (importante para la defensa — demuestra madurez técnica)
- Distancia fija de **500 km** en una Strategy → para tener **tests deterministas** en vez de aleatorios.
- Naming del **Visitor** elegido para evitar colisión con el **Composite** de centros.
- Desfasaje dominio jerárquico (Composite) vs. registro plano de persistencia → aceptado y neutralizado con única fuente de verdad.

### La decisión que conecta el Hito 7 con el Hito 13
En el Hito 13 (Persistencia), el `Envio` se reconstruye vía el **Builder** del Hito 7. El dominio nunca toca SQL — el Data Mapper traduce. Esto significa que una decisión de diseño de hace 6 hitos pagó dividendos al final. Eso es arquitectura bien pensada.

---

## 7. Preguntas probables de la cátedra

### Sobre el negocio
**"¿Quién es la Consultora Logística?"**
> La empresa inversora que identificó el problema y financió el desarrollo. No usa el sistema — su retorno viene del fee mensual que pagan las PyMEs.

**"¿Qué es una PyME en este contexto?"**
> Una empresa cliente del SaaS — una distribuidora, mensajería, etc. Dentro de cada PyME hay tres tipos de usuarios: Operador Logístico (opera día a día), Admin PyME (el dueño) y Transportistas (choferes).

**"¿Qué es el modelo multitenant?"**
> Una sola instancia del sistema sirve a múltiples empresas, cada una con sus datos aislados. El aislamiento se garantiza en la capa de Presentación — el Controller valida permisos antes de cualquier operación.

### Sobre actores y CUs
**"¿Cuál es la diferencia entre AdminEmpresa y AdminPlataforma?"**
> AdminEmpresa es el dueño de una PyME individual — es cliente del SaaS. AdminPlataforma es el equipo de LogiSmart que opera toda la plataforma. Son como "inquilino" vs. "dueño del edificio".

**"¿Por qué solo presentaron 3 CUs si implementaron más?"**
> Presentamos los 3 de mayor impacto de negocio visible. Implementamos 7 de 8 — el octavo (importar desde e-commerce) quedó parcial porque la cátedra nunca pidió ese adapter específicamente.

### Sobre patrones
**"¿Por qué usaron Facade?"**
> La capa de Aplicación necesitaba orquestar 5 subsistemas en orden sin que el cliente conociera ese orden — eso sostiene la Alta Disponibilidad. El trade-off es el riesgo de God Object; lo mitigamos haciendo que la Facade solo coordine y delegue, nunca calcule ni valide ella misma.

**"¿Cuál es la diferencia entre Strategy y State?"**
> Strategy: el **cliente** elige el algoritmo e intercambia en runtime. State: el **objeto** cambia solo, siguiendo su máquina de estados interna. En LogiSmart: Strategy para calcular costos (se inyecta desde afuera), State para el ciclo de vida del Envio (evoluciona por sí mismo).

**"¿Cuál es la diferencia entre Adapter, Decorator y Proxy?"**
> Adapter: traduce una interfaz incompatible (DHL → ProveedorEnvio). Decorator: misma interfaz, agrega comportamiento apilable (DecoradorGPS + DecoradorSeguro). Proxy: misma interfaz, controla cuándo y cómo se accede (caché, lazy loading).

**"¿Por qué eligieron Repository + Data Mapper en vez de acceso directo?"**
> Para que el dominio nunca toque SQL. El Envio se reconstruye vía Builder — el mismo que usamos desde el Hito 7. Si mañana cambiamos de base de datos, el dominio no cambia. El trade-off es el bookkeeping del mapper, justificado por la criticidad del dato logístico.

**"¿Qué es RUP y cómo lo aplicaron?"**
> Proceso iterativo e incremental, guiado por CUs y centrado en arquitectura. Lo aplicamos entregando 13 hitos donde cada uno agregó funcionalidad sobre el anterior — los patrones pedidos por la cátedra en cada hito habilitaron CUs como consecuencia natural. Los 148 tests en verde son la evidencia de que el proceso fue disciplinado.

---

## 8. Análisis de la problemática original y técnica de sustantivos/verbos

### La pregunta
> ¿Qué problemática planteaba específicamente la consigna inicial? ¿Cómo se aplica el análisis de sustantivos y verbos para derivar el modelo de dominio, los casos de uso y los requerimientos funcionales?

### La problemática original (texto exacto de la consigna)

> *"Una consultora de logística ha detectado una brecha en el mercado: las pequeñas y medianas empresas (PyMEs) de manufactura en Argentina luchan por competir con grandes corporaciones debido a ineficiencias en su cadena de suministro. Estas PyMEs gestionan sus envíos y entregas con herramientas dispares (email, planillas de Excel, WhatsApp), lo que resulta en altos costos de transporte, falta de trazabilidad de la mercadería, rutas de entrega ineficientes y una pobre comunicación con los clientes finales.*
>
> *La consultora quiere financiar el desarrollo de una plataforma SaaS llamada 'LogiSmart' para abordar este problema. La visión es ofrecer una solución 'todo en uno' que sea accesible económicamente y fácil de implementar. La plataforma debe integrarse con sistemas de e-commerce populares (como TiendaNube o MercadoShops) para importar pedidos automáticamente. Debe permitir a las PyMEs planificar rutas de entrega óptimas para su flota de vehículos (o para transportistas tercerizados), considerando variables como el tráfico en tiempo real, la capacidad de los vehículos y las ventanas horarias de entrega de los clientes. Los clientes finales deben poder ver en un mapa y en tiempo real dónde está su pedido.*
>
> *La monetización del sistema será un fee mensual basado en la cantidad de envíos procesados. La seguridad de los datos y la alta disponibilidad del servicio son cruciales, ya que una falla en el sistema podría detener las operaciones de sus clientes."*

**El problema en una línea:** las PyMEs gestionan su logística con herramientas informales (Excel, WhatsApp), lo que les genera costos altos, falta de trazabilidad y mala comunicación con sus clientes. LogiSmart resuelve esto con un SaaS centralizado.

---

### La técnica: análisis de sustantivos y verbos

Es una técnica de análisis de dominio del Proceso Unificado. La idea es escanear el texto del problema en lenguaje natural y extraer:

- **Sustantivos** → candidatos a **clases/entidades** del dominio
- **Verbos** → candidatos a **métodos** (comportamiento de las clases) o **Casos de Uso** (metas de los actores)
- **Adjetivos** → candidatos a **atributos** de las clases o **atributos de calidad** del sistema

Después se filtra: se eliminan los que son demasiado generales, redundantes o que pertenecen al contexto de negocio y no al sistema.

---

### Aplicado al texto de LogiSmart

#### Sustantivos → clases del dominio

| Sustantivo en el texto | Clase en el código | Observación |
|---|---|---|
| PyME / empresa | `Empresa.java` | Entidad central del tenant |
| envío | `Envio.java` | La entidad más importante del sistema |
| entrega | `Entrega.java` | El acto de completar un envío |
| pedido / orden | `Orden.java` | Lo que importa del e-commerce |
| ruta de entrega | `Ruta.java` | Secuencia de puntos de entrega |
| flota | `Flota.java` | Conjunto de vehículos de una PyME |
| vehículo | `Vehiculo.java` → Auto, Camion, Moto | Jerarquía de tipos |
| transportista | `Transportista.java` | Chofer propio o tercerizado |
| cliente final | `ClienteFinal.java` | Destinatario del envío |
| fee mensual / cobro | `Cobro.java` | Monetización del SaaS |
| cantidad de envíos | `Metrica.java` | Base del cálculo del fee |
| tráfico en tiempo real | sistema externo (API Mapas) | No es una clase propia |
| e-commerce (TiendaNube) | adapter externo | Se integra vía Adapter pattern |
| posición GPS | `PosicionGPS.java` | Coordenada del tracking |
| punto de entrega | `PuntoEntrega.java` | Parada en una ruta |
| seguridad de datos | atributo de calidad | No es una clase, es un NFR |

#### Verbos → Casos de Uso y métodos

| Verbo en el texto | CU o método | Dónde vive |
|---|---|---|
| importar pedidos automáticamente | **CU-01** | `OperadorLogistico.importarOrdenes()` |
| planificar rutas óptimas | **CU-03** | `OperadorLogistico.planificarRuta()` |
| ver dónde está su pedido en tiempo real | **CU-07** | `ClienteFinal.consultarTracking()` |
| gestionar envíos y entregas | **CU-02, 04, 05, 06** | múltiples métodos |
| considerar capacidad de los vehículos | método interno | `Vehiculo.puedeCargar()` |
| considerar ventanas horarias | atributo de Orden | `Orden.ventanaHoraria` |
| calcular el fee mensual | **CU-08** | `AdminPlataforma.gestionarBilling()` |
| detener operaciones (riesgo) | atributo de calidad | → Alta disponibilidad |

#### Adjetivos → atributos y NFRs

| Adjetivo en el texto | Lo que genera |
|---|---|
| óptimas (rutas) | Strategy pattern para calcular la ruta más eficiente |
| en tiempo real (tráfico, tracking) | Observer pattern + TrackingService |
| tercerizados (transportistas) | El Transportista puede ser externo — mismo modelo que interno |
| mensual (fee) | atributo `periodo` en Cobro |
| alta disponibilidad | NFR → Facade + Chain para que nada quede a medias |
| seguridad de datos | NFR → Rol enum + IPermisos + aislamiento multitenant |

---

### Del texto al modelo — la cadena completa

```
TEXTO DEL PROBLEMA
       ↓
  Sustantivos → clases candidatas → filtrar → Diagrama de Clases (Dominio)
       ↓
    Verbos   → metas de actores  → filtrar → Catálogo de CUs
       ↓
  Adjetivos  → restricciones     → filtrar → Atributos de Calidad (NFRs)
       ↓
  Actores    → quién hace qué    → asignar → Responsabilidades (GRASP)
       ↓
  Patrones   → cómo se resuelve  → decidir → Arquitectura
```

### Por qué esto importa en la defensa

La consigna pedía exactamente este razonamiento: *"guiado por Casos de Uso y centrado en la arquitectura"*. Si en la defensa preguntan por qué eligieron determinada clase o determinado patrón, la respuesta siempre puede rastrearse hasta el texto original del problema:

- ¿Por qué existe `Ruta.java`? → porque el texto dice "planificar rutas de entrega".
- ¿Por qué existe el patrón Strategy para calcular rutas? → porque el texto dice "rutas óptimas *considerando* tráfico, capacidad y ventanas horarias" — hay múltiples algoritmos posibles.
- ¿Por qué existe el Observer? → porque el texto dice "ver en tiempo real" — el sistema debe notificar cambios automáticamente.
- ¿Por qué existe el Adapter? → porque el texto dice "integrarse con sistemas de e-commerce" — hay APIs externas incompatibles con el dominio.

---

## 9. ¿Qué es "bookkeeping"?

### La pregunta
> El trade-off del Unit of Work dice "mantener la unidad de trabajo agrega bookkeeping". ¿Qué significa eso?

### La respuesta

"Bookkeeping" viene de contabilidad — literalmente "llevar los libros". En software significa el **trabajo de registro y seguimiento interno** que un componente hace para funcionar, más allá de la lógica de negocio pura.

En el contexto del **Unit of Work**, el patrón tiene que mantener tres listas internas durante toda la operación:

| Lista interna | Contenido |
|---|---|
| **new** | objetos que hay que insertar en la base |
| **dirty** | objetos que fueron modificados |
| **removed** | objetos que hay que eliminar |

Eso es el bookkeeping — no es lógica de negocio, es infraestructura de seguimiento. Al final de la operación, el UoW recorre esas listas y ejecuta todo en una única transacción (o hace rollback si algo falla).

El trade-off: ese overhead de registro extra vale la pena porque garantiza consistencia en una operación crítica como crear un envío logístico.

---

## 10. "El dominio quede testeable en aislamiento" — qué significa

### La pregunta
> En el slide de Capa 01 Presentación, el panel "Multitenant por diseño" dice: "El control de acceso vive arriba para que el dominio quede testeable en aislamiento". ¿A qué se refiere?

### La respuesta

Significa que las clases del dominio (`Envio`, `Ruta`, `Flota`, etc.) se pueden **instanciar y probar como objetos Java puros**, sin necesitar ningún contexto de seguridad, usuario autenticado ni mock de permisos.

El contraste con lo que podría haber sido:

**Sin aislamiento — si los permisos vivieran en el dominio:**
```java
// Envio.java
public void cambiarEstado(Usuario usuario, EstadoEnvio nuevo) {
    if (!usuario.tienePermiso("cambiar_estado")) // dominio conoce permisos
        throw new SecurityException(...);
    this.estado = nuevo;
}
// Para testear: necesitás crear un Usuario, asignarle permisos, simular sesión...
```

**Con aislamiento — lo que se implementó:**
```java
// Envio.java — dominio puro
public void cambiarEstado(EstadoEnvio nuevo) {
    this.estado = nuevo; // solo lógica de negocio
}
// El Controller ya validó permisos antes de llamar esto.
// Para testear: new Envio(...).cambiarEstado(ENTREGADO) — sin infraestructura.
```

El `LogiSmartController` valida permisos antes de llamar al dominio. El dominio nunca sabe *quién* está llamando — solo *qué* se le pide. Por eso los 148 tests son tests de lógica pura: rápidos, sin servidor, sin base de datos, sin usuarios mockeados.

Esto también es el motivo del `IPermisos` como interfaz: el Controller recibe cualquier objeto que la implemente — no le importa si es `OperadorLogistico` o `AdminEmpresa`. El dominio es completamente agnóstico de quién lo usa.

### La conexión con los principios
- **GRASP Low Coupling**: el dominio no depende de la capa de seguridad.
- **SOLID Single Responsibility**: `Envio` tiene una sola razón para cambiar — reglas de negocio, no reglas de acceso.
- **Clean Architecture**: el dominio está en el centro y no conoce las capas externas.

---

## 10. El atributo de calidad Elasticidad — qué significa "recursos"

### La pregunta
> El atributo de Elasticidad dice: "Capacidad del sistema para adaptar sus recursos automáticamente según la demanda de pedidos procesados para optimizar costos operativos. Se logra mediante el uso de hosting escalable que permite pagar solo por lo consumido." ¿A qué se refiere con "recursos"?

### La respuesta

"Recursos" en este contexto son **recursos de infraestructura cloud**:
- **CPU** — poder de procesamiento
- **RAM** — memoria disponible
- **Instancias del servidor** — cuántas copias de la aplicación corren en paralelo
- **Conexiones a base de datos** — capacidad de atención simultánea

La idea concreta: si hoy una PyME procesa 100 envíos y mañana hay un pico de 10.000 (temporada alta), el sistema *escala automáticamente* sumando instancias. Cuando baja la demanda, las libera. El "pagar solo por lo consumido" apunta a modelos cloud como AWS, Google Cloud o Azure donde se paga por segundo de CPU/RAM usado, no por un servidor fijo.

### La tensión que hay que saber explicar en la defensa

El atributo dice que elasticidad se logra con "hosting escalable". Eso es una decisión de **infraestructura/deployment**, no de código. Sin embargo, en la presentación se asoció Elasticidad al patrón **Abstract Factory** (FactoryArgentina / FactoryBrasil).

Esas dos cosas no son lo mismo:
- **Abstract Factory → Adaptabilidad regional**: produce objetos coherentes por país (AR vs BR). Es *configurabilidad geográfica*, no *escalabilidad de carga*.
- **Elasticidad real (cloud)**: auto-scaling de servidores según carga. Es una decisión de infraestructura.

**Cómo responder si preguntan en la defensa:**
> "Elasticidad tiene dos dimensiones. La infraestructural (auto-scaling) queda fuera del alcance del diseño de clases — es una decisión de hosting que tomaría el equipo de operaciones. La dimensión de diseño que abordamos es la adaptabilidad regional: el Abstract Factory garantiza que agregar una nueva región (Uruguay, Chile) sea añadir una familia de objetos coherente, sin tocar el dominio ni los patrones existentes."

---

## 11. Preguntas del Hito 2 — con el diario del lunes

### ¿Debería haber una clase `Usuario` base de la que hereden `Cliente` y `Operador`? ¿Por qué?

**Sí, y así quedó implementado.**

`Usuario.java` es la clase base. De ella heredan (y todas implementan `IPermisos`):

| Subclase | Rol enum |
|---|---|
| `OperadorLogistico` | `OPERADOR` |
| `AdminEmpresa` | `ADMIN_EMPRESA` |
| `AdminPlataforma` | `ADMIN_PLATAFORMA` |
| `Transportista` | `TRANSPORTISTA` |
| `ClienteFinal` | `CLIENTE` |

**Por qué es correcto:**
- `Usuario` concentra lo que *todos* los actores comparten: `id`, `username`, `email`, `passwordHash`, `estado`, y los métodos de sesión (`iniciarSesion`, `cambiarPassword`, `cerrarSesion`). Eso es GRASP Creator e Information Expert en acción.
- Los permisos **no** viven en `Usuario` — viven en `IPermisos`. Esto es clave: la herencia da atributos comunes, el polimorfismo por interfaz da el comportamiento diferenciado de permisos. Si los permisos estuvieran en la base, agregar un rol nuevo obligaría a tocar `Usuario`.
- El `LogiSmartController` recibe `IPermisos` — no sabe si es `OperadorLogistico` o `AdminEmpresa`. Ese es el bajo acoplamiento que habilita la herencia correctamente planteada.

**Trade-off a mencionar:** el riesgo de herencia es la rigidez. Lo mitigamos con `IPermisos` como segunda "columna vertebral" — si mañana un rol no puede heredar de `Usuario` (ej. un sistema externo), solo necesita implementar `IPermisos`.

---

### ¿Es `Vehículo` una clase o un atributo de `Operador`? Justifiquen.

**Es una clase, con asociación hacia `Transportista` (no hacia `Operador` directamente).**

`Vehiculo.java` tiene identidad propia: `id`, `patente`, `capacidadKg`, `tipo`, `disponibilidad`. Tiene comportamiento propio: `puedeCargar()`, `estaOperativo()`, `asignarRuta()`, `liberar()`, `getCostoBaseKm()`. Tiene jerarquía propia: `Auto`, `Camion`, `Moto` extienden `Vehiculo`.

Nada de eso puede existir en un atributo. Un atributo es un dato (`String patente`), no una entidad con ciclo de vida y jerarquía.

En el código final la relación es:
```
Transportista → vehiculoAsignado : Vehiculo   (asociación, no herencia ni atributo primitivo)
Flota          → List<Vehiculo>               (composición)
```

`Transportista` *tiene* un `Vehiculo` asignado — la referencia puede ser `null` si está sin asignar, puede cambiar durante la operación. Eso confirma que `Vehiculo` tiene su propio ciclo de vida independiente del `Transportista`.

**Argumento de negocio:** un vehículo puede estar en la flota sin estar asignado a ningún transportista. Un transportista puede cambiar de vehículo entre rutas. Esa independencia de ciclos de vida exige que sean dos clases.

---

### ¿Debería haber una clase `Pago` separada o es un atributo de `Envío`?

**Clase separada — en la implementación se llama `Cobro`.**

En `Envio.java` solo viven `metodoPago` (String) y `costo` (double) — el medio y el monto, datos que el envío necesita conocer para su propio cálculo. No hay un objeto `Cobro` dentro de `Envio`.

`Cobro.java` es una clase independiente con:
- Su propio estado: `PENDIENTE → AUTORIZADO → PAGADO / FALLIDO`
- Su propio comportamiento: `autorizar()`, `registrarPago()`, `marcarFallido()`, `emitirComprobante()`
- Su propia referencia al envío: `envioId` (apunta hacia arriba, no al revés)

**Por qué es correcto tenerla separada:**
1. **Ciclos de vida distintos.** Un envío puede crearse sin pago (pago diferido, crédito). Un cobro puede existir sin un envío puntual (fee mensual sobre volumen de envíos → `Cobro` sin `envioId`).
2. **Responsabilidades distintas.** `Envio` gestiona la logística de un paquete. `Cobro` gestiona la transacción financiera. Mezclarlas viola Single Responsibility.
3. **Los adapters de pago (`AdapterPayPal`, `AdapterStripe`) trabajan contra `ProveedorPago`, no contra `Envio`.** El dominio de pago es lo suficientemente complejo como para merecer su propia jerarquía.

**Trade-off:** si solo existiera un modelo simple donde cada envío tiene exactamente un pago inmediato, podría justificarse como atributo. En LogiSmart el fee es mensual y agregado → la separación es obligatoria.

---

## 12. Encapsulación y GRASP en la implementación real

### ¿Cómo aplicamos encapsulación (visibilidad)?

**Regla general:** todos los campos de todas las clases son `private`. El estado solo cambia a través de métodos con semántica de negocio.

Ejemplos concretos del código:

| Clase | Campo privado | Cómo se expone |
|---|---|---|
| `Envio` | `estado` | `cambiarEstado()` · `iniciar()` · `cancelar()` — nunca setter directo |
| `Envio` | `ordenes` (lista) | `getOrdenes()` devuelve `Collections.unmodifiableList()` — no se puede mutar desde afuera |
| `Envio` | `observadores` (lista) | `private final` — solo `adjuntarObservador()` / `desadjuntarObservador()` |
| `Envio` | `notificarObservadores()` | Método `private` — detalle interno, no es API pública |
| `Vehiculo` | `disponibilidad` | Solo cambia vía `asignarRuta()` y `liberar()` — sin setter |
| `Rol` (enum) | Matriz de permisos 5×5 | Expuesta solo a través de `puedeCrearEnvio()`, `puedeAsignarRuta()`, etc. |
| `Envio.EnvioBuilder` | Constructor de `Envio` con Builder | `private Envio(EnvioBuilder)` — nadie puede construir un Envio por ese camino sin pasar por el Builder |

La `IPermisos` es la "ventana pública" de los permisos — el `Controller` ve solo los métodos booleanos, nunca el enum `Rol` directamente.

---

### ¿Cómo aseguramos alta cohesión y bajo acoplamiento?

**Alta cohesión — cada clase tiene una sola razón para cambiar:**

| Clase | Su única responsabilidad |
|---|---|
| `Envio` | Ciclo de vida de un envío logístico |
| `Cobro` | Transacción financiera |
| `Vehiculo` | Capacidad y disponibilidad del vehículo |
| `Usuario` | Datos de autenticación y sesión |
| `Rol` | Matriz de permisos por tipo de usuario |
| `CadenaValidadores` | Orquestación del fail-fast antes de crear un envío |

**Bajo acoplamiento — las dependencias son hacia abstracciones, no hacia concretos:**

- `LogiSmartController` depende de `IPermisos` → no sabe si hay un `Operador` o un `AdminEmpresa`. Agregar un rol nuevo no toca el Controller.
- `Envio` depende de `EstrategiaCalculoCosto` (interfaz) → no sabe si calcula por peso, distancia o tarifa fija.
- `Envio` depende de `EstadoEnvio` (interfaz) → no tiene `switch` sobre estados; cada estado sabe sus transiciones válidas.
- `Envio` depende de `ObservadorEnvio` (interfaz) → no sabe si hay un Dashboard, una Auditoría o un Notificador escuchando.
- El dominio entero no tiene ningún `import` de paquetes de persistencia o infraestructura — la inversión de dependencias es total.

**La evidencia empírica:** 148 tests sobre el dominio puro, sin mocks de base de datos ni de seguridad. Si el acoplamiento fuera alto, esos tests serían imposibles.

---

## 13. Responsabilidades de Ruta y Envío — diseño con diario del lunes

### ¿La clase `Ruta` debería calcular distancias? ¿Debería validar puntos de entrega?

**Calcular distancias: sí, y así está implementado — con matices.**

`Ruta` tiene `calcularDistanciaTotal()`. Itera sobre sus propias `PuntoEntrega` y llama a `PosicionGPS.haversineKm()`. Esto es GRASP Information Expert: `Ruta` tiene la colección de paradas con sus coordenadas — es el experto natural en su propio recorrido espacial. No sabe *cómo* funciona Haversine (eso vive en `PosicionGPS`, que es la experta en operaciones geográficas), pero sí sabe *cuál es la secuencia* de paradas. La responsabilidad está bien distribuida.

La alternativa — delegarlo a un `CalculadorDeDistancia` externo — agregaría indirección sin beneficio real, porque la distancia total de una ruta no varía según estrategia: siempre es la suma de las distancias entre paradas consecutivas.

**Validar puntos de entrega: la responsabilidad está en `PuntoEntrega`, no en `Ruta`.**

`PuntoEntrega.validarVentana()` valida si tiene ventana horaria asignada. `Ruta` delega: no duplica la validación, simplemente confía en que cada parada se valida a sí misma. Eso es Information Expert llevado al nivel correcto de granularidad.

---

### ¿`Envío` debería tener `calcularTiempoEstimado()`? ¿O es responsabilidad de otra clase?

**No. Y así quedó implementado — `CalculadorDeTiempo` es quien calcula, no `Envio`.**

El tiempo estimado de entrega depende de:
- La **ruta** y su secuencia de paradas (distancia)
- El **vehículo** y su velocidad promedio
- El **tráfico** en tiempo real (variable externa)

`Envio` no tiene acceso a ninguna de esas tres variables directamente. Ponerle `calcularTiempoEstimado()` violaría Low Coupling: `Envio` tendría que conocer `Ruta`, `Vehiculo` y APIs de tráfico — tres dependencias externas que no le corresponden.

En la implementación, `CalculadorDeTiempo` es una interfaz en infraestructura (`estimarMinutos(Ruta ruta)`) con dos implementaciones:
- `CalculadorDeTiempoSimple` — solo usa distancia
- `CalculadorDeTiempoConTrafico` — incorpora datos de tráfico real

Esto es GRASP Protected Variations: el algoritmo de tiempo puede cambiar sin tocar `Envio`. Si el cálculo estuviera en `Envio`, cambiar de simple a tráfico requeriría modificar la entidad central del dominio.

---

### ¿`Envío` debería tener `guardarEnBaseDatos()`? ¿Por qué no?

**No. Nunca. Es la violación más grave que se puede cometer en un diseño por capas.**

Razones desde el código:

1. **Single Responsibility:** `Envio` tiene una sola razón para cambiar: las reglas del negocio logístico. Agregar persistencia le da una segunda razón: el esquema de la base de datos.

2. **Clean Architecture / Dependency Inversion:** si `Envio` tuviera `guardarEnBaseDatos()`, el paquete `dominio` tendría que importar `java.sql` o alguna librería ORM. Eso invierte la dependencia: el núcleo del sistema dependería de un detalle de infraestructura. En la implementación, `Envio.java` no tiene ni un solo import de persistencia.

3. **El Repository pattern resuelve exactamente esto:** `RepositorioEnvio` (interfaz en infraestructura) define el contrato. `RepositorioEnvioMemoria` y `RepositorioEnvioSQL` lo implementan. El dominio nunca sabe qué backend usa. Si mañana cambiamos de SQL a MongoDB, `Envio.java` no se toca.

4. **Evidencia empírica:** los 148 tests de dominio puro pasan sin ninguna base de datos. Eso solo es posible porque `Envio` es una POJO — sin efectos colaterales de persistencia.

**Cómo responderlo en la defensa:**
> "Si `Envio` tuviera `guardarEnBaseDatos()`, no podríamos testear el dominio sin una base de datos real. El Repository hace exactamente lo contrario: permite que el dominio sea completamente ignorante de cómo se persiste, y que los tests sean rápidos y deterministas."

---

### ¿`Ruta` debería conocer cómo se calcula el costo? ¿O delegar?

**Delegar — y así está implementado en dos niveles.**

`Ruta.calcularCostoEstimado()` hace esto:
```java
double costoBaseKm = vehiculoAsignado != null ? vehiculoAsignado.getCostoBaseKm() : 1.0;
return calcularDistanciaTotal() * costoBaseKm;
```

`Ruta` sabe dos cosas: su propia distancia (Information Expert) y que el costo por km depende del vehículo (delega a `Vehiculo.getCostoBaseKm()`, que sí es el experto en su propio costo operativo). `Ruta` **no sabe** qué tipo de vehículo es ni cuál es la tarifa — solo le pregunta al objeto que sí sabe.

Para los cálculos de negocio completos (con impuestos, seguro, volumen), la responsabilidad sube a `Envio.calcularCostoConEstrategia()` vía el patrón Strategy (`EstrategiaCalculoCosto`). Hay tres implementaciones: por peso, por distancia, fija.

El diseño distingue dos conceptos:
- `Ruta.calcularCostoEstimado()` = estimación operativa para planificar (distancia × tarifa base del vehículo)
- `Envio.calcularCostoConEstrategia()` = cálculo de negocio con reglas configurables

`Ruta` no conoce las reglas de negocio de pricing — solo delega al experto real (`Vehiculo`) el único dato que necesita.

---

---

## 14. Auditoría de cohesión — preguntas de defensa

### ¿Hay clases que hacen demasiadas cosas no relacionadas?

**La única candidata real es `Envio`** — y tiene una explicación defendible.

`Envio` acumula campos de múltiples hitos (los comentarios en el propio archivo dicen "Hito 10", "Hito 11", "Hito 12"). En total tiene ~20 campos y soporta 6 patrones: Builder, Prototype, Observer, State, Strategy, Memento. 

Sin embargo, todas esas responsabilidades giran alrededor de **un solo concepto**: el ciclo de vida de un envío. Builder construye el envío, State gestiona sus transiciones, Observer notifica los cambios, Memento permite deshacer, Strategy calcula su costo. No hay nada "no relacionado" en sentido estricto.

**Los campos `metodoPago` y `productoId` en `Envio` son un design smell real.**

Semánticamente `productoId` pertenece a `Orden` (un envío tiene una lista de órdenes, y cada orden tiene un producto), y `metodoPago` pertenece a `Cobro`. El diseño correcto sería que los validadores del Chain naveguen al objeto que realmente tiene el dato:

```java
// ValidadorInventario — diseño correcto:
for (Orden orden : envio.getOrdenes()) {
    if (!inventario.verificarStock(orden.getProductoId())) { ... }
}

// ValidadorPago — diseño correcto: recibir el Cobro explícitamente
// o trabajar con un ContextoValidacion que agrupe Envio + Cobro
```

**Lo que pasó en Hito 10:** para que el Chain tuviera una firma simple y uniforme (`validar(Envio envio)`), se desnormalizaron esos datos directamente en `Envio`. Es un shortcut — el validador puede leer todo desde un solo objeto sin navegar relaciones. Funciona, pero crea una abstracción con fugas: `Envio` termina cargando datos que no le corresponden por identidad de dominio.

El razonamiento "el Chain los usa, por eso está bien que estén en `Envio`" es circular — el Chain los usa desde `Envio` precisamente porque alguien los puso ahí, pero no deberían estar ahí.

**Cómo se corrigió:** se introdujo `ContextoValidacion(Envio envio, Cobro cobro)` como Value Object de transporte del Chain. `productoId` se movió a `Orden` (con getter/setter). `ValidadorInventario` ahora itera `envio.getOrdenes()`. `ValidadorPago` lee `ctx.getCobro().getMedioPago()`. Los campos fueron eliminados de `Envio` y su Builder.

**Cómo responderlo en la defensa:**
> "En Hito 10 pusimos `metodoPago` y `productoId` en `Envio` para simplificar la firma del Chain — un shortcut pragmático que generó una fuga de diseño. Lo identificamos y lo corregimos: `productoId` vive ahora en `Orden`, `metodoPago` en `Cobro`, y el Chain recibe un `ContextoValidacion` que agrupa los dos objetos. `Envio` volvió a tener solo su responsabilidad."

**¿Este refactoring representa algún patrón conocido?**

Sí, varios a la vez:

- **Introduce Parameter Object (Fowler, "Refactoring"):** cuando varios parámetros siempre viajan juntos, se los agrupa en un objeto. Aquí `Envio` y `Cobro` siempre se necesitan juntos para validar — la firma `validar(Envio, Cobro)` habría sido el paso previo; `ContextoValidacion` es exactamente ese objeto.
- **Value Object (DDD / Fowler PoEAA):** `ContextoValidacion` es inmutable (ambos campos `final`), sin identidad propia — su significado es el conjunto de sus atributos. Es el patrón más preciso para describirlo.
- **Protected Variations (GRASP):** al encapsular el contexto de validación en un único punto, si mañana hay que agregar un `Cliente` o una `Politica` al contexto, solo se toca `ContextoValidacion` — ninguno de los cinco validadores cambia su firma. El punto de variación queda protegido.
- **Pure Fabrication (GRASP):** `ContextoValidacion` no existe en el dominio del negocio (no hay un concepto de negocio llamado "contexto de validación") — es una clase inventada exclusivamente para mejorar el diseño técnico del Chain.

**Cómo responderlo en la defensa:**
> "El refactoring aplica *Introduce Parameter Object* de Fowler — `Envio` y `Cobro` siempre viajan juntos en la cadena, así que los agrupamos. El objeto resultante es un *Value Object* inmutable y también encarna *Protected Variations* de GRASP: si el contexto de validación crece, solo cambia `ContextoValidacion` y los validadores concretos quedan intactos."

---

### ¿Hay responsabilidades que no tienen sentido en esa clase?

**`OperadorLogistico` instancia objetos de dominio directamente.**

```java
public Envio crearEnvio(String prioridad, LocalDateTime fechaProgramada) {
    return new Envio(UUID.randomUUID().toString(), empresa, prioridad, fechaProgramada);
}
public Ruta planificarRuta(Vehiculo vehiculo, Transportista transportista) {
    Ruta ruta = new Ruta(UUID.randomUUID().toString(), 0.0, 0, "PLANIFICADA");
    ruta.asignarVehiculo(vehiculo);
    ruta.asignarTransportista(transportista);
    return ruta;
}
```

**GRASP Creator** define con precisión cuándo una clase B puede (y debe) crear instancias de A. Son 4 condiciones — con que se cumpla una alcanza:

1. B **contiene o agrega** objetos de tipo A
2. B **registra/almacena** objetos de tipo A
3. B **usa directamente** y de forma cercana a A
4. B **tiene los datos de inicialización** necesarios para crear A

Para `OperadorLogistico.crearEnvio()`:
- Condición 3: el operador trabaja directamente con envíos — los crea, les asigna rutas, los gestiona
- Condición 4: `OperadorLogistico` tiene `this.empresa` (campo de instancia, necesario para el constructor de `Envio`). Los demás parámetros (`prioridad`, `fechaProgramada`) llegan como argumentos del método — el operador es quien los conoce en el flujo de negocio

Para `OperadorLogistico.planificarRuta()`:
- Condición 4: recibe `Vehiculo` y `Transportista` como parámetros — tiene todo lo que necesita para inicializar `Ruta`
- Condición 3: el operador es quien planifica rutas en el dominio del negocio

**¿Por qué no un Factory?** En el sistema también existe `FabricaDeEnvios` y sus subclases para los casos donde la lógica de creación es suficientemente compleja (herencia, variantes, múltiples configuraciones). GRASP Creator y Factory coexisten: cuando la creación es directa y el creador ya tiene los datos, Creator; cuando la lógica de construcción es compleja o variable, Factory.

**¿Por qué no un servicio de aplicación?** Los servicios de aplicación coordinan múltiples clases de dominio. `crearEnvio()` es una operación atómica dentro del dominio — no necesita un orquestador. Un servicio de aplicación lo invocaría si luego necesitara guardar, notificar o coordinar con otros dominios.

**Cómo responderlo en la defensa:**
> "GRASP Creator dice que la clase que tiene los datos de inicialización es la responsable de crear el objeto. `OperadorLogistico` tiene `empresa` como campo propio y es quien recibe `prioridad` y `fechaProgramada` en el flujo de negocio. Eso lo convierte en el Creator natural. Para casos de construcción más compleja usamos `FabricaDeEnvios` — ambas técnicas coexisten según la complejidad de la creación."

---

### ¿Hay clases que mezclan lógica de negocio con persistencia o notificaciones?

**No — esta es una fortaleza del diseño.**

**Persistencia:** el paquete `dominio` nunca importa SQL, JPA, ni ninguna clase de infraestructura de base de datos. Toda la persistencia está en `infraestructura/persistencia/repositorios/`. Los 148 tests del dominio corren sin ninguna base de datos — eso es la prueba empírica de que el dominio está limpio.

**Notificaciones:** `Envio` tiene el Observer integrado, pero `notificarObservadores()` es `private`. `Envio` no sabe *qué* notifica ni *a quién* — solo invoca el contrato `ObservadorEnvio.actualizar(this)`. Las notificaciones reales (email, push, log) están en las implementaciones concretas de `ObservadorEnvio` en infraestructura.

**Un matiz menor a conocer: `new EstadoConfirmado()` en `Envio`.**

```java
// Envio.java — com.logismart.dominio.envio
import com.logismart.infraestructura.comportamiento.state.EstadoConfirmado;  // ← esta línea
import com.logismart.infraestructura.comportamiento.state.EstadoEnvio;       // ← y esta

private EstadoEnvio estadoGoF = new EstadoConfirmado();
```

Para entender por qué esto es un problema, hace falta entender la regla de dependencia de Clean Architecture:

```
Dominio (círculo interno) ← puede ser usado por → Infraestructura (círculo externo)
Dominio (círculo interno) → NO puede depender de → Infraestructura (círculo externo)
```

Las flechas van hacia adentro: la infraestructura conoce al dominio, pero no al revés. Es como una cebolla — las capas externas conocen a las internas, nunca al revés.

`Envio` vive en `com.logismart.dominio.envio` (círculo interno). `EstadoConfirmado` y la interfaz `EstadoEnvio` viven en `com.logismart.infraestructura.comportamiento.state` (círculo externo). Al importarlos, `Envio` viola la regla: el círculo interno está dependiendo del externo.

**¿Por qué pasó?** El patrón State fue implementado en Hito 12 con la interfaz y sus implementaciones en el paquete `infraestructura`. Cuando se agregó State a `Envio`, se importó directamente en lugar de mover la interfaz al dominio.

**¿Cuál sería el fix correcto?** Mover `EstadoEnvio` (la interfaz) al paquete `dominio.envio` — ahí sí pertenece, porque define comportamiento de dominio. Las implementaciones concretas (`EstadoConfirmado`, `EstadoEnTransito`, etc.) pueden quedarse en infraestructura. Solo la interfaz tiene que estar en el dominio.

**¿Por qué es "menor"?** Porque `EstadoConfirmado` no trae ninguna preocupación de infraestructura real — no hay SQL, no hay HTTP, no hay email. Es puro Java con lógica de negocio. El impacto práctico es cero: los tests corren igual, el comportamiento es correcto. Es un problema de organización de paquetes, no de diseño de comportamiento.

**Cómo responderlo en la defensa:**
> "La interfaz `EstadoEnvio` debería vivir en el paquete `dominio.envio` porque define comportamiento del dominio, no de la infraestructura. Las implementaciones concretas sí corresponden a infraestructura. En Hito 12 colocamos todo el State en infraestructura por practicidad — es una deuda técnica de packaging que no afecta el comportamiento ni la testabilidad."

---

## 17. Refactors aplicados post-análisis de cohesión

### LogiSmartController — SQL directo mezclado con coordinación de CU

**Problema:** El Controller llamaba directamente a `ConexionBD.obtenerInstancia()` y ejecutaba SQL crudo (`bd.ejecutarQuery("INSERT INTO envios...")`) en los mismos métodos donde coordina los casos de uso. El repositorio (`RepositorioDeEnvios.guardar(envio)`) ya manejaba la persistencia — el SQL era redundante y acoplaba el Controller a la infraestructura de base de datos.

**Corrección:** Se eliminaron todos los `bd.ejecutarQuery(...)` del Controller y el campo `ConexionBD bd`. La persistencia queda exclusivamente en el repositorio (Pure Fabrication correcta). El `Logger` se mantuvo — logging de aplicación es cross-cutting y apropiado en el Controller.

**Cómo responderlo en la defensa:**
> "El Controller tenía llamadas SQL directas además de usar el repositorio — doble camino de persistencia. Lo corregimos: el Controller solo coordina, el repositorio persiste. El SQL crudo también era un injection risk al concatenar strings sin parametrizar."

---

### Vehiculo.getCostoBaseKm() — condicional de tipo en lugar de polimorfismo

**Problema:** `Vehiculo.getCostoBaseKm()` usaba `if ("CAMION".equalsIgnoreCase(tipo)) return 1.8` — exactamente el smell que GRASP Polymorphism evita. Peor: `FabricaDeVehiculos` creaba instancias de `Vehiculo` directamente con un String de tipo, ignorando las subclases `Camion`, `Auto`, `Moto` que ya existían (creadas para el Abstract Factory de hitos anteriores).

**Corrección:**
- Cada subclase override `getCostoBaseKm()`: `Camion → 1.8`, `Auto → 1.0`, `Moto → 0.9`
- Se agregó constructor `(String id, String patente)` a cada subclase
- `FabricaDeVehiculos` ahora crea `new Camion(id, patente)` / `new Moto(...)` / `new Auto(...)` según el `TipoVehiculo`
- `Vehiculo.getCostoBaseKm()` queda como `return 1.0` (default polimórfico)

**Cómo responderlo en la defensa:**
> "Teníamos subclases `Camion`, `Auto`, `Moto` para el Abstract Factory, pero la otra factory creaba `new Vehiculo(...)` directamente con un String. Eso hacía que `getCostoBaseKm()` necesitara un if sobre el tipo. Lo corregimos: la factory ahora crea la subclase correcta y el costo base es polimórfico."

---

### SistemaAuditoria — instanceof para extraer ID

**Problema:** `registrar(String evento, Object datos)` hacía `if (datos instanceof Envio) getId() else toString()` — chequeo de tipo explícito, violación de Polymorphism.

**Corrección:** Se usa `datos.toString()` directamente. `Envio` ya tiene `toString()` implementado (`"Envio{id='...', origen='...', ...}"`), así que el log sigue siendo informativo. El método acepta cualquier `Object` sin saber su tipo concreto.

---

### Dependencias directas — análisis y decisión

**`Logger` (Singleton):** Se mantiene accedido via `Logger.obtenerInstancia()` en el Controller. Es una Pure Fabrication de infraestructura transversal — el acceso por Singleton es apropiado; inyectarlo no añadiría valor testable.

**Factories estáticas (`FabricaDeEnvios`, `FabricaDeVehiculos`):** El Controller las llama directamente. Son el patrón Factory Method — el acoplamiento es intencional. Inyectar una interfaz `FactoriaEnvios` sería over-engineering para el contexto del TPO.

---

## 15. GRASP — cómo quedó aplicado en LogiSmart

GRASP (General Responsibility Assignment Software Patterns) tiene 9 patrones. Responden una pregunta fundamental: **¿a qué clase le asignamos esta responsabilidad?**

### 1. Information Expert
Principio: la clase que tiene los datos necesarios es quien debe realizar la tarea.

- `Ruta.calcularDistanciaTotal()` — Ruta tiene las paradas, ella recorre la lista y suma usando `PosicionGPS.haversineKm()`
- `Ruta.calcularCostoEstimado()` — Ruta conoce la distancia; le pregunta a `Vehiculo.getCostoBaseKm()` porque el vehículo es el experto en su propio costo operativo
- `Rol.puedeCrearEnvio()` / `puedeAsignarRuta()` / etc. — Rol tiene la tabla de permisos 5×5, él evalúa
- `Envio.crearMemento()` — Envio tiene su propio estado, él lo encapsula en el snapshot

### 2. Creator
Principio: B debe crear A si B tiene los datos de inicialización, usa A directamente, o agrega/contiene A.

- `OperadorLogistico.crearEnvio()` — tiene `empresa` como campo propio; recibe `prioridad` y `fechaProgramada`
- `OperadorLogistico.planificarRuta()` — recibe `Vehiculo` y `Transportista`, tiene todo lo necesario
- `FabricaDeEnvios` y subclases — para casos donde la creación tiene variantes (Factory Method sobre Creator simple)
- `FabricaRegionalAbstracta` — familias de objetos coherentes por región (Abstract Factory)

### 3. Controller
Principio: primer objeto detrás de la UI que recibe y coordina una operación del sistema; no tiene lógica de dominio.

- `ServicioLogisticaCompleto` — coordina repositorios y servicios para operaciones de alto nivel
- `LogisticaFacade` — punto de entrada único para la capa de aplicación (Facade + Controller)

### 4. Low Coupling
Principio: minimizar dependencias entre clases para que cambiar una no obligue a cambiar otras.

- `IPermisos` interface — el Controller valida permisos sin saber si el usuario es `AdminEmpresa`, `OperadorLogistico` o `Transportista`
- `ObservadorEnvio` interface — `Envio` notifica sin saber si el suscriptor es un logger, un emailer o un push notifier
- `RepositorioEnvio` interface — la aplicación persiste sin saber si hay SQL, en memoria o un mock
- `EstrategiaCalculoCosto` interface — `Envio` calcula el costo sin saber qué algoritmo está activo (por peso, distancia o fija)
- `EstadoEnvio` interface — `Envio` transiciona sin conocer las clases concretas de estado

### 5. High Cohesion
Principio: cada clase tiene responsabilidades relacionadas y bien definidas.

- `Cobro` — solo gestiona el ciclo de una transacción financiera (autorizar, registrar pago, marcar fallido)
- `CalculadorDeTiempo` — solo estima tiempos de entrega (ver Pure Fabrication)
- `Rol` — solo evalúa la matriz de permisos 5×5
- `ETA` — solo calcula tiempos de llegada estimados
- `MementoEnvio` — solo almacena un snapshot inmutable del estado de un envío

### 6. Polymorphism
Principio: usar polimorfismo para manejar variaciones de comportamiento en lugar de `if/switch` sobre tipos.

- Jerarquía `Usuario` → `AdminEmpresa`, `AdminPlataforma`, `OperadorLogistico`, `Transportista`, `ClienteFinal` — cada uno implementa `IPermisos` con su propia lógica de permisos
- Jerarquía `Vehiculo` → `Auto`, `Camion`, `Moto` — cada uno con su `getCostoBaseKm()`, capacidad, disponibilidad
- `EstadoEnvio` y sus 6 implementaciones — `EstadoConfirmado.validar()` hace algo diferente que `EstadoEnTransito.validar()`; sin `if (estado.equals("CONFIRMADO"))`

### 7. Pure Fabrication
Principio: crear una clase que no existe en el dominio real, para asignar responsabilidades que no pertenecen a ninguna entidad de negocio.

- `CalculadorDeTiempo` — no existe una "calculadora de tiempos" en la logística real; es una abstracción computacional pura
- `ServicioLogisticaCompleto` — coordinador que no tiene equivalente en el mundo real
- `RepositorioEnvio` — abstracción de acceso a datos; no es una entidad de negocio

### 8. Indirection
Principio: introducir un objeto intermediario para que dos clases no dependan directamente una de la otra.

- `IPermisos` entre Controller y los roles concretos — el Controller no necesita saber qué tipo de usuario es
- `ObservadorEnvio` entre `Envio` y sus suscriptores — `Envio` no necesita saber qué notifica ni a quién
- `RepositorioEnvio` entre la aplicación y la base de datos — la aplicación no necesita saber cómo se almacena

### 9. Protected Variations
Principio: identificar dónde el diseño va a cambiar y encapsular esa variación detrás de una interfaz estable.

- `EstrategiaCalculoCosto` — el algoritmo de pricing puede cambiar sin tocar `Envio`; agregar "tarifa nocturna" es una clase nueva
- `EstadoEnvio` — agregar un nuevo estado del ciclo de vida es una clase nueva; `Envio` no cambia
- `FabricaRegionalAbstracta` — soporte para Argentina, Brasil, etc. sin modificar el código existente
- Adapters (`AdaptadorDHL`, `AdaptadorFedEx`) — integrar un nuevo carrier es implementar la interfaz; el dominio no se toca
- `RepositorioEnvio` interface — cambiar de SQL a NoSQL es cambiar la implementación; la capa de aplicación no se entera

---

## 16. RUP — metodología aplicada a lo largo de los 13 hitos

RUP (Rational Unified Process) tiene 4 fases que no son períodos de tiempo estrictos sino estadios de madurez del proyecto. La clave de RUP: **cada iteración agrega valor concreto** (un CU funcional o una mejora arquitectural) sin romper lo construido antes.

### Fase 1 — Inception (Concepción) · Hitos 1-2

**Objetivo:** entender el problema, establecer viabilidad, identificar actores y CUs críticos.

**Hito 1:**
- Caso de negocio: PyME gestiona logística a mano (planillas, WhatsApp) → LogiSmart como SaaS multitenant
- Modelo de negocio: fee mensual por tenant → viabilidad económica demostrada
- Stakeholders: inversora (Consultora Logística), equipo IT, PyMEs como tenants, usuarios finales
- Primer modelo de dominio con entidades clave identificadas

**Hito 2:**
- 28 clases de dominio: `Envio`, `Ruta`, `Vehiculo`, `Usuario`, `Empresa`, etc.
- 3 CUs núcleo: CU-01 (importar pedidos), CU-03 (planificar rutas óptimas), CU-07 (seguimiento en vivo)
- Jerarquía de usuarios con roles y permisos

Al terminar Inception sabíamos **qué** construir y **por qué**.

---

### Fase 2 — Elaboration (Elaboración) · Hitos 3-6

**Objetivo:** estabilizar la arquitectura de referencia, mitigar riesgos técnicos mayores.

**Hito 3:**
- Arquitectura en 5 capas: Presentación → Aplicación → Dominio → Persistencia → Infraestructura
- Regla fundamental: el dominio no conoce la infraestructura (Clean Architecture)
- Esta decisión arquitectural fue el "esqueleto" que no cambió en los 10 hitos siguientes

**Hito 4-5 — Patrones creacionales:**
- `Singleton`: conexión a base de datos, una sola instancia por tenant
- `Factory Method`: `FabricaDeEnvios` con subclases para tipos de envío
- `Abstract Factory`: fábricas por región (AR/BR) que crean familias de objetos coherentes
- `Prototype`: clonación de `Envio` para pedidos similares sin reconstruir desde cero

**Hito 6 — Patrones estructurales iniciales:**
- `Adapter`: integración con TiendaNube/MercadoShops → **CU-01 habilitado completamente**
- `Facade`: punto de entrada único (`LogisticaFacade`)
- `Composite`: `Ruta` como colección de `PuntoEntrega` con comportamiento uniforme

Al terminar Elaboration: arquitectura estable, CU-01 funcional, riesgos mitigados.

---

### Fase 3 — Construction (Construcción) · Hitos 7-12

**Objetivo:** construir el sistema completo de forma iterativa; cada iteración habilita más funcionalidad.

**Hito 7-8:**
- `Builder`: construcción de `Envio` con muchos atributos opcionales (peso, fragil, requiereSignatura, etc.)
- `Bridge`: separar abstracción de vehículo de su implementación (tipos de servicio independientes de tipos de vehículo)
- `Proxy`: acceso controlado a recursos costosos
- `Decorator`: agregar comportamiento (seguro, refrigeración) sin cambiar la clase base

**Hito 9:**
- `Flyweight`: compartir objetos `PosicionGPS` inmutables para eficiencia en memoria
- `Facade` completo: `ServicioLogisticaFacade` con inventario, pagos, notificaciones integrados

**Hito 10 — Chain of Responsibility + Command + Interpreter:**
- `Chain of Responsibility`: validación de envíos en cadena secuencial (ValidadorDatos → ValidadorPago → ValidadorSeguridad → ValidadorInventario)
- `Command`: operaciones encapsuladas como objetos (crear, cancelar, reprogramar)
- `Interpreter`: expresiones de búsqueda y filtrado de envíos

**Hito 11 — Observer + Mediator + Iterator + Memento:**
- `Observer`: `Envio` notifica cambios de estado a suscriptores → **CU-07 habilitado completamente** (seguimiento en vivo)
- `Memento`: snapshots inmutables del estado de `Envio` para historial y undo

**Hito 12 — State + Strategy + Template Method + Visitor:**
- `State`: ciclo de vida completo de `Envio` (Confirmado → EnTransito → EnReparto → Entregado) sin `if/switch`
- `Strategy`: algoritmos de costo intercambiables (por peso, por distancia, fija) → **CU-03 completado**
- `Template Method`: flujo genérico con pasos especializables por tipo de servicio
- `Visitor`: operaciones sobre la jerarquía de clases sin modificarlas

Al terminar Construction: 35+ patrones, 148 tests, los 3 CUs núcleo completamente funcionales.

---

### Fase 4 — Transition (Transición) · Hito 13

**Objetivo:** preparar para producción, integrar persistencia real, pruebas de integración completas.

**Hito 13 — PoEAA (Patterns of Enterprise Application Architecture):**
- `Repository`: interfaz de dominio para acceso a datos — el dominio pide "dame el envío con ID X" sin saber cómo se busca
- `Data Mapper`: transforma entre objetos de dominio (`Envio`) y tablas relacionales (`envios`) sin que el dominio lo sepa
- `Unit of Work`: agrupa operaciones (guardar envío + guardar cobro) en una transacción atómica
- `Lazy Load`: carga diferida — si no se necesitan las paradas de la ruta, no se cargan hasta que se pidan

**El resultado:** 148 tests del dominio que corren sin base de datos real. Eso es la evidencia concreta de que la separación dominio/infraestructura funcionó durante 13 hitos de desarrollo iterativo.

---

### Lo que el código mismo muestra sobre RUP

Los comentarios en `Envio.java` dicen literalmente "Hito 10", "Hito 11", "Hito 12". No es documentación — es la arqueología del proceso iterativo:

```java
// ─── Campos Hito 10 (Chain / Command / Interpreter) ──────────────────────
private double costo;
private String metodoPago;
private String productoId;

// ─── Campos Hito 11 (Observer) ───────────────────────────────────────────
private final List<ObservadorEnvio> observadores = new ArrayList<>();

// ─── Campos Hito 12 (State / Strategy) ───────────────────────────────────
private EstadoEnvio estadoGoF = new EstadoConfirmado();
private EstrategiaCalculoCosto estrategia;
```

Cada iteración agregó campos, implementaciones y comportamiento **sin romper lo anterior**. En Hito 10 `Envio` no sabía nada de estados ni estrategias — en Hito 12 ya tenía los dos. Eso es exactamente lo que RUP llama "arquitectura evolutiva": el sistema crece en capas, y cada capa habilita algo nuevo.

---

---

## 18. Hito 6 — Singleton y Factory Method

### Actividad 1: ¿Por qué el constructor de Singleton es privado?

Para que ninguna clase externa pueda hacer `new Logger()`. Si el constructor fuera público, cualquier clase podría crear su propia instancia y el patrón se rompe — ya no hay "única instancia" sino tantas como alguien quiera. El constructor privado obliga a pasar por el método estático `getInstance()` / `obtenerInstancia()`, que es el único camino de acceso controlado.

---

### Actividad 2: ¿Qué problema resuelve `volatile` en el Singleton?

Sin `volatile`, el compilador o la JVM puede reordenar las instrucciones de `new Logger()`. La construcción de un objeto Java tiene 3 pasos: (1) reservar memoria, (2) inicializar los campos, (3) asignar la referencia al campo estático `instancia`. Sin `volatile`, otro hilo puede ver el campo `instancia` como no-null (paso 3 completado) antes de que los campos internos estén inicializados (paso 2 no terminado). El resultado es un objeto "a medias" que produce comportamiento impredecible.

`volatile` garantiza que las escrituras al campo sean visibles inmediatamente a todos los hilos y que no se reordenen con respecto a las lecturas y escrituras previas.

---

### Actividad 3: ¿Qué es el "double-checked locking" y por qué se usa?

Es la técnica de verificar la instancia dos veces — una fuera del bloque `synchronized` y otra dentro — para evitar el costo de sincronización en cada llamada una vez que la instancia ya existe:

```java
public static Logger obtenerInstancia() {
    if (instancia == null) {                    // primer check (sin lock — rápido)
        synchronized (Logger.class) {
            if (instancia == null) {            // segundo check (con lock — seguro)
                instancia = new Logger();
            }
        }
    }
    return instancia;
}
```

**Por qué dos checks:** el primero evita entrar al bloque `synchronized` en el caso común (instancia ya creada). El segundo evita que dos hilos que pasaron el primer check simultáneamente creen dos instancias. Sin el segundo check, si dos hilos llegan al `synchronized` al mismo tiempo cuando `instancia == null`, ambos crearían una instancia. Sin `volatile` en el campo, la visibilidad entre hilos tampoco está garantizada.

**Por qué `volatile` es necesario junto con DCL:** en Java 5+, `volatile` garantiza la visibilidad del campo entre hilos y evita el reordenamiento de instrucciones. Sin él, el DCL no es seguro aunque el código parezca correcto.

---

### Actividad 4: ¿Cuáles son los riesgos del Singleton?

| Riesgo | Explicación | Mitigación en LogiSmart |
|---|---|---|
| **God Object** | La instancia única acumula responsabilidades diversas | `Logger` solo loggea, `ConfiguracionSistema` solo configura — cada Singleton tiene una sola responsabilidad |
| **Testing difícil** | Los tests comparten la misma instancia — efectos colaterales entre tests | Los Singletons de LogiSmart son stateless o de solo lectura; no modifican estado entre tests |
| **Acoplamiento global** | Cualquier clase puede acceder al Singleton → dependencia implícita | Se accede vía `Logger.obtenerInstancia()` — explícito y rastreable |
| **Race conditions** | Sin DCL + volatile, múltiples instancias en entorno multihilo | Implementado con `volatile` + double-checked locking |

**Cómo responderlo en la defensa:**
> "El Singleton es apropiado para `Logger` y `ConfiguracionSistema` porque son genuinamente únicos en el sistema: una sola conexión de logs, una sola configuración global. El riesgo de God Object lo mitigamos con responsabilidad única. El riesgo de testing lo mitigamos con Singletons sin estado mutable."

---

### Actividad 6: ¿Por qué el Factory Method retorna el supertipo?

`FabricaDeEnvios.crearEnvio(TipoEnvio tipo, ...)` retorna `Envio` (la clase base), nunca `EnvioUrgente` ni `EnvioEstandar` directamente. Esto tiene tres consecuencias:

1. **El cliente no conoce las subclases:** quien llama a `crearEnvio()` recibe un `Envio` y lo usa como `Envio`. Nunca hace `EnvioUrgente e = (EnvioUrgente) fabrica.crearEnvio(...)`.

2. **Open/Closed via enum + switch:** agregar un tipo nuevo de envío (ej. `EnvioRefrigerado`) es:
   - Agregar `REFRIGERADO` al enum `TipoEnvio`
   - Crear la clase `EnvioRefrigerado extends Envio`
   - Agregar el `case REFRIGERADO: return new EnvioRefrigerado(...)` en el switch de la factory
   
   Ningún código existente que use `FabricaDeEnvios` necesita cambiar. Eso es Open/Closed.

3. **Polimorfismo sin instanceof:** el `LogiSmartController` llama `FabricaDeEnvios.crearEnvio(tipoEnvio, empresa, fecha)` y usa el resultado como `Envio`. Si en el futuro `EnvioRefrigerado` necesita validación especial, se implementa dentro de esa subclase — el Controller no cambia.

---

### Sección 5: ¿Cuándo usar Singleton vs. Factory Method?

| Criterio | Singleton | Factory Method |
|---|---|---|
| **Propósito** | Garantizar una única instancia | Centralizar la creación de objetos con variantes |
| **Cuándo** | Recurso único y compartido (logger, config, conexión BD) | Cuando hay varios tipos del mismo concepto y el creador no debe conocerlos |
| **Trade-off principal** | Acoplamiento global; testing más difícil | Requiere jerarquía de clases; indirección extra |
| **En LogiSmart** | `Logger`, `ConfiguracionSistema` | `FabricaDeEnvios`, `FabricaDeVehiculos`, `FabricaDeNotificadores` |

**La distinción clave para la defensa:**
- Singleton es sobre *cuántas instancias* (una sola)
- Factory Method es sobre *cómo se crean* (el creador retorna supertipo, las subclases deciden qué construir)

Son ortogonales: una Factory puede ser Singleton si tiene sentido que haya una sola factory en el sistema.

---

### ¿Por qué no hay slide específico para Singleton y Factory Method en la presentación?

La presentación cubre Abstract Factory con slide propio porque es el patrón más visualmente rico: dos familias de objetos coherentes por región (AR/BR) que se ven naturalmente en un diagrama comparativo. Singleton y Factory Method aparecen como patrones "transversales" en el pie del slide de Infraestructura — lo cual es correcto. Son decisiones de implementación que están por debajo del nivel de abstracción que la presentación quiere comunicar. En la defensa oral, estos detalles se responden a demanda.

---

---

## 19. Hito 6 — Abstract Factory, Builder, Prototype e Integración

### Abstract Factory

#### ¿Cuál es la diferencia entre Factory Method y Abstract Factory? ¿Por qué el Hito 6 no alcanzaba?

**Factory Method** crea *un* tipo de objeto con variantes. `FabricaDeEnvios.crearEnvio(TipoEnvio.EXPRESS, ...)` retorna un `EnvioExpress` — es un solo producto, con subclases por tipo.

**Abstract Factory** crea una *familia* de objetos relacionados que deben ser coherentes entre sí. `LogiSmartFactory` define tres métodos: `crearVehiculo()`, `crearCalculadorCostos()`, `crearProveedorMapas()`. Una implementación concreta garantiza que los tres objetos pertenezcan a la misma región.

Por qué el Hito 6 no alcanzaba: cuando apareció el requisito de soporte multiregional (Argentina y Brasil), ya no alcanzaba con una factory que creara un solo tipo de objeto. Se necesitaba garantizar que si la región es Brasil, el `CalculadorCostos` use ICMS 12% *y* el proveedor de mapas sea HereMaps *y* el vehículo predeterminado sea Moto — no una combinación arbitraria. Esa coherencia de familia es exactamente lo que Factory Method no garantiza.

---

#### ¿Qué garantiza la Abstract Factory que un Factory Method no? La coherencia de la familia

`LogiSmartFactoryArgentina` siempre retorna:
- `Auto` (vehículo de referencia AR)
- `CalculadorCostosArgentina` → base 100 + peso × 15 + **IVA 21%**
- `GoogleMapsArgentina` → API de mapas local

`LogiSmartFactoryBrasil` siempre retorna:
- `Moto` (vehículo de referencia BR)
- `CalculadorCostosBrasil` → **ICMS 12%**
- `HereMaps` → API de mapas alternativa

Con Factory Method, nada impediría que alguien construya a mano un `CalculadorCostosArgentina` y lo use junto a un `HereMaps` de Brasil. Con Abstract Factory, la única vía de obtener los tres objetos es a través de la misma factory concreta — y ella garantiza que todos pertenecen a la misma región.

---

#### ¿Qué pasa si mañana agregás Chile? Open/Closed: nueva factory, cero cambios en el cliente

Clases que se **crean** (solo adiciones):
1. `CalculadorCostosChile implements CalculadorCostos` → con IVA 19% chileno
2. `MapasChile implements ProveedorMapas` → API de mapas regional
3. `LogiSmartFactoryChile implements LogiSmartFactory` → combina los dos anteriores con el vehículo de referencia CL

Clases que se **modifican**:
- `LogiSmartApp.crearFactory(region)`: agregar el case `"Chile" → new LogiSmartFactoryChile()`

Clases que **no cambian en absoluto**:
- `LogiSmartFactory` (interfaz) — sin tocar
- `LogiSmartApp.procesarEnvio()` — sin tocar: llama `factory.crearVehiculo()`, `factory.crearCalculadorCostos()`, `factory.crearProveedorMapas()` exactamente igual
- Todo el código de Argentina y Brasil — sin tocar

Eso es Open/Closed: abierto para extensión (nueva región), cerrado para modificación (el cliente existente no cambia).

---

#### ¿El código cliente conoce las clases concretas? ¿Cómo decide qué factory usar?

`LogiSmartApp` declara el campo como `LogiSmartFactory factory` — el supertipo (interfaz). El método `procesarEnvio()` llama `factory.crearVehiculo()` sin saber si recibe un `Auto` argentino o una `Moto` brasileña.

El único lugar donde aparecen los nombres concretos es `crearFactory(region)`:

```java
private LogiSmartFactory crearFactory(String region) {
    if (region.equalsIgnoreCase("Argentina")) return new LogiSmartFactoryArgentina();
    if (region.equalsIgnoreCase("Brasil"))    return new LogiSmartFactoryBrasil();
    throw new IllegalArgumentException("Región desconocida: " + region);
}
```

Este método es el "punto de acoplamiento necesario". Una vez que la factory está asignada al campo, el resto de la clase trabaja completamente contra la abstracción `LogiSmartFactory`.

---

#### ¿Dónde queda la diferencia IVA 21% vs ICMS 12%? ¿Por qué no es un if(region) en el cliente?

Vive en las implementaciones concretas:

```java
// CalculadorCostosArgentina.java
public double calcularCosto(double peso) {
    double subtotal = 100 + peso * 15;
    return subtotal * 1.21;  // IVA 21%
}

// CalculadorCostosBrasil.java
public double calcularCosto(double peso) {
    double subtotal = 100 + peso * 15;
    return subtotal * 1.12;  // ICMS 12%
}
```

El cliente (`procesarEnvio`) hace simplemente `double costo = calc.calcularCosto(peso)`. No sabe qué impuesto se aplica — esa responsabilidad vive en el objeto que sí la conoce.

Si fuera un `if(region == "Argentina") costo *= 1.21 else costo *= 1.12` en el cliente, agregar Chile requeriría tocar el cliente. Con Abstract Factory, agregar Chile solo requiere una nueva clase `CalculadorCostosChile`. El cliente no cambia.

---

### Builder

#### ¿Qué problema resuelve el Builder? ¿Por qué no un constructor con 8 parámetros?

`Envio` tiene más de 12 atributos configurables. Sin Builder, el constructor telescópico sería:

```java
// Ilegible: ¿qué es true? ¿qué es null? ¿en qué orden van los booleans?
new Envio("ENV001", "Buenos Aires", "Córdoba", null, null, 2.5, false, true, false, true, null, null)
```

Con Builder:

```java
new Envio.EnvioBuilder("ENV001", "Buenos Aires", "Córdoba")
    .peso(2.5)
    .fragil(true)
    .requiereRefrigeracion(true)
    .instruccionesEspeciales("Mantener entre 2-8°C")
    .build()
```

Tres problemas concretos que resuelve:
1. **Legibilidad**: cada parámetro tiene nombre explícito en el método setter
2. **Opcionalidad**: los campos no configurados usan defaults (`false`, `""`, `null`) — no hay que pasar nulls
3. **Consistencia**: el objeto solo existe en estado válido (sale de `build()`) — no hay instancias medio construidas

---

#### ¿Por qué EnvioBuilder es una clase interna estática de Envio?

Dos razones que se complementan:

1. **Acceso al constructor privado**: `Envio` tiene `private Envio(EnvioBuilder builder)`. Solo el código dentro del archivo `Envio.java` puede llamarlo — lo que incluye a la clase interna `EnvioBuilder`. Si `EnvioBuilder` fuera una clase separada en otro archivo, no podría invocar ese constructor.

2. **`static`**: el Builder no necesita una instancia previa de `Envio` para existir — al contrario, es quien *crea* esa instancia. Si fuera una clase interna no-estática, necesitaría un `Envio` externo ya construido para instanciarse, lo cual sería una contradicción circular.

---

#### ¿Dónde validás que el objeto esté completo: en cada setter o en build()? ¿Por qué?

En `build()`. Los setters retornan `this` para fluency sin validar:

```java
public EnvioBuilder peso(double peso) { this.peso = peso; return this; }
// ...
public Envio build() { return new Envio(this); }  // aquí va la validación
```

Por qué en `build()` y no en cada setter:
- Las reglas de negocio pueden involucrar *combinaciones* de campos: "si `requiereRefrigeracion`, debe tener `horaEntregaPreferida`" — esa regla no puede validarse en el setter de `requiereRefrigeracion` porque `horaEntregaPreferida` todavía no fue seteada.
- El error ocurre en el momento justo: cuando el objeto *va a existir*, no antes. Un Builder sin llamar a `build()` no es un `Envio` — es solo un conjunto de intenciones.

---

#### ¿El objeto construido puede ser inmutable? ¿Qué ventaja tiene eso?

Técnicamente sí: si todos los campos del `Envio` construido por Builder fueran `final`, el objeto sería inmutable. En la implementación actual `Envio` no es completamente inmutable porque tiene `setId()` (usado en Prototype) y permite agregar observadores y ordenes post-construcción.

La ventaja de la inmutabilidad: thread-safe por construcción (sin race conditions), cacheable sin riesgo de que alguien modifique el cache, sin efectos colaterales en métodos que reciben el objeto. El trade-off es la inflexibilidad — para cambiar cualquier campo hay que construir un nuevo objeto. En `Envio`, la mutabilidad es deliberada porque el ciclo de vida del envío requiere transiciones de estado (State pattern).

---

#### ¿Por qué EnvioMapperSQL reutiliza el mismo Builder? Un solo punto de construcción válida

`EnvioMapperSQL.buscarPorId()` reconstruye el `Envio` desde SQL así:

```java
return new Envio.EnvioBuilder(
        rs.getString("id"),
        rs.getString("origen"),
        rs.getString("destino"))
    .peso(rs.getDouble("peso"))
    .estado(rs.getString("estado"))
    .costo(rs.getDouble("costo"))
    .tipo(rs.getString("tipo"))
    .build();
```

El motivo es garantía de consistencia entre hitos: si mañana `Envio` agrega un campo requerido con validación en `build()`, la única forma de reconstruir un `Envio` válido desde SQL es pasar por ese `build()`. Si el mapper creara el `Envio` con un constructor propio (de 4 argumentos), ese constructor podría omitir el nuevo campo y el error se vería en runtime, no en compilación. Con Builder, el mapper falla en compilación si no pasa el campo nuevo.

Esta decisión conecta Hito 7 (Builder diseñado para construcción del dominio) con Hito 13 (mapper que respeta esa restricción). Una decisión de hace 6 hitos paga dividendos en el último.

---

### Prototype

#### ¿Cuándo conviene clonar en vez de hacer new?

Dos escenarios:

1. **Configuración base repetida**: el caso de LogiSmart. Se tienen 100 envíos de medicamentos urgentes — todos frágiles, todos con refrigeración requerida, todos con los mismos flags. En vez de llamar al Builder 100 veces con los mismos atributos opcionales, se arma el prototipo una vez y se clonan. El ahorro es legibilidad y consistencia, no rendimiento.

2. **Creación costosa**: si construir el objeto requiriera consultas a la base de datos, validaciones externas o cálculos pesados, clonar ahorra esa inicialización en cada copia. En LogiSmart este caso no aplica porque el Builder es puro Java — pero es el caso de negocio canónico del patrón.

---

#### Diferencia entre shallow copy y deep copy. ¿Cuál usaste para Envio y por qué importa?

La implementación usa **shallow copy con deep copy selectiva** de la lista de órdenes:

```java
@Override
public Envio clone() {
    try {
        Envio clonado = (Envio) super.clone();       // shallow: copia primitivos y referencias
        clonado.ordenes = new ArrayList<>(this.ordenes); // deep: lista propia
        return clonado;
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("Error al clonar Envio", e);
    }
}
```

**Por qué la lista necesita deep copy**: si fuera shallow, clon y prototipo compartirían la misma referencia `List<Orden>`. Agregar una orden al clon la agregaría automáticamente al prototipo — y a todos los demás clones que comparten esa lista. Bug silencioso y difícil de rastrear.

**Por qué los demás campos no necesitan deep copy**: `String`, `LocalTime`, `double`, `boolean` son primitivos o tipos inmutables en Java. Compartir sus referencias es seguro — nadie puede mutar un `String`. Los objetos `SeguimientoEnvio` y `Entrega` sí se comparten en la shallow copy, lo cual podría ser un issue si se modifican — en la implementación actual es aceptable porque el clon recibe ID propio vía `setId()` que actualiza solo el campo `id`, no los objetos anidados.

---

#### ¿Qué problemas tiene Cloneable en Java? ¿Qué alternativa hay?

`Cloneable` es una *marker interface* (sin métodos) que activa el comportamiento de `Object.clone()`. Sus problemas:

1. No declara el método `clone()` — si la clase no lo sobreescribe como `public`, el método heredado sigue siendo `protected` y no es invocable desde afuera
2. Lanza `CloneNotSupportedException` checked aunque la clase implemente `Cloneable` — hay que capturarla o relanzarla
3. No hay contrato explícito: la semántica de qué se copia profundo y qué superficial queda en la documentación informal
4. Si alguna superclase no implementa `Cloneable`, `super.clone()` lanza la excepción

**Alternativa**: constructor de copia o método `copiar()` propio:

```java
// Constructor de copia
public Envio(Envio original) {
    this.id = original.id;
    this.ordenes = new ArrayList<>(original.ordenes);
    // ...campos explícitos
}
```

Ventaja: completamente explícito, sin dependencia en `Object.clone()`, sin excepción checked, legible. En LogiSmart se usa `Cloneable` para demostrar el patrón según lo pedido por la cátedra; en código productivo se preferiría el constructor de copia.

---

### Integración — los 5 patrones creacionales en un solo flujo

El flujo concreto que ocurre en `LogiSmartApp` cuando se procesa un envío argentino:

```
Main.main()
  │
  ├─ Logger.getInstance()                       ← SINGLETON
  │   └─ una sola instancia de Logger en toda la JVM
  │
  ├─ new LogiSmartApp("Argentina")
  │    └─ crearFactory("Argentina")             ← ABSTRACT FACTORY
  │        └─ new LogiSmartFactoryArgentina()
  │            garantiza: Auto + IVA 21% + GoogleMaps (coherencia de familia)
  │
  ├─ app.crearUsuario("operador", "María")       ← FACTORY METHOD
  │    └─ UsuarioFactory.crearUsuario("operador", nombre)
  │        retorna OperadorLogistico — el cliente nunca referencia la subclase
  │
  ├─ app.crearEnvio("Buenos Aires", "Córdoba")   ← BUILDER
  │    └─ new Envio.EnvioBuilder(id, origen, destino)
  │            .descripcion("Estándar").build()
  │        construcción expresiva con defaults seguros para los 10 campos no seteados
  │
  ├─ app.crearEnviosMultiples(100)               ← PROTOTYPE
  │    ├─ armar prototipo con Builder
  │    └─ prototipo.clone() × 100
  │        cada clon tiene su propia lista de órdenes (deep copy)
  │
  └─ app.procesarEnvio("Buenos Aires", "Mendoza", 5.0)
       └─ factory.crearVehiculo()    → Auto          (sin saber que es argentino)
          factory.crearCalculadorCostos() → IVA 21%  (sin if/else en el cliente)
          factory.crearProveedorMapas()  → GoogleMaps
          new Envio.EnvioBuilder(...).peso(5.0).build()   ← BUILDER de nuevo
```

---

#### ¿Por qué cada patrón y no otro? El problema concreto que resolvió cada uno

| Patrón | Problema concreto de LogiSmart | Alternativa descartada y por qué |
|---|---|---|
| **Singleton** | Logger y ConfiguracionSistema deben ser únicos — dos instancias darían logs desordenados o configuraciones inconsistentes | Variable global: en Java no existe sin clase; campo estático: sin control de acceso ni lazy init |
| **Abstract Factory** | Agregar soporte Brasil requería garantizar que IVA, mapas y vehículo fueran siempre coherentes para la misma región | `if(region)` disperso en todo el código: agregar Chile requeriría buscar y modificar N lugares |
| **Factory Method** | El Controller necesita crear `OperadorLogistico`, `ClienteFinal`, etc. sin conocer las subclases concretas de Usuario | `switch(tipo)` en el Controller: viola Open/Closed, agrega responsabilidad al Controller |
| **Builder** | `Envio` tiene 12+ atributos opcionales; constructor con todos forzaría pasar null en cada llamada | Constructor telescópico: ilegible, error-prone, no escala cuando se agregan campos |
| **Prototype** | Clonar 100 envíos con la misma configuración base sin repetir toda la cadena del Builder | `new Envio.EnvioBuilder(...)...build()` × 100: repetición y riesgo de inconsistencia si se olvida un flag |

---

---

## 20. Hito 8 — Adapter, Bridge, Composite

### Adapter

#### Actividad 1 — ¿Dónde hay APIs externas? ¿Tienen interfaces incompatibles?

Hay dos grupos de APIs externas en LogiSmart:

**Carriers de envío:**
- `DHLAPI` → método `registrarPaquete(String origen, String destino, double peso)` retorna `String`
- `FedExAPI` → método `enviarPaquete(String destino, double peso)` retorna `String`
- `UPSConnector` → API similar con nombres distintos

**Pasarelas de pago:**
- `PayPalAPI` → método `crearTransaccion(double monto, String referencia)` retorna `String`
- `StripeAPI` → API similar con nombres distintos

El dominio de LogiSmart espera:
- `ProveedorEnvio.crearEnvio(Envio envio)` — recibe un objeto `Envio`, no strings sueltos
- `ProveedorPago.procesarPago(double monto, String referencia)` — interfaz uniforme para cualquier pasarela

Las APIs externas no se pueden cambiar — pertenecen a DHL, FedEx, PayPal, Stripe. El Adapter resuelve esa brecha.

---

#### ¿La interfaz objetivo la definió el dominio o el proveedor? ¿Por qué importa esa dirección?

La definió **el dominio de LogiSmart**. `ProveedorEnvio` y `ProveedorPago` son interfaces en el paquete `infraestructura` del proyecto — no dependen de ninguna API externa. Los carriers se adaptan al dominio, no al revés.

Por qué importa: si la interfaz la definiera DHL, el dominio dependería del contrato de DHL. Cada vez que DHL cambia su API (versión nueva, parámetros distintos), hay que tocar código de dominio. Con la dirección correcta, DHL cambia su API → se actualiza solo `AdapterDHL` → el resto del sistema no se entera.

---

#### Actividad 2 — ¿Por qué el adapter traduce entre interfaces? ¿Qué pasa si agregás un nuevo proveedor?

El adapter traduce porque las APIs externas hablan un "idioma" diferente al que espera el dominio. `AdapterDHL` en concreto:

```java
@Override
public boolean crearEnvio(Envio envio) {
    // El dominio da un Envio. La DHLAPI quiere tres strings sueltos.
    String codigo = dhlAPI.registrarPaquete(
        envio.getOrigen(), envio.getDestino(), envio.getPeso());
    envio.getSeguimiento().actualizarEstado("DHL:" + codigo);
    return true;
}
```

El adapter extrae los datos del `Envio` y los pasa a la API en el formato que ella espera. En la dirección opuesta, convierte el `String` de retorno de la API en una actualización del `SeguimientoEnvio`.

**Nuevo proveedor (ej. MercadoEnvíos):**
1. Crear `MercadoEnviosAPI` (la API del proveedor — o ya existe, es externa)
2. Crear `AdapterMercadoEnvios implements ProveedorEnvio`
3. Inyectar el nuevo adapter donde se usa `ProveedorEnvio`

Ninguna otra clase cambia. Open/Closed: extensión sin modificación.

---

#### ¿Cómo testearías un adapter?

Dos niveles:

1. **Testear la lógica de traducción**: mockear la API externa (`DHLAPI`) y verificar que el adapter la llama con los parámetros correctos extraídos del `Envio`. Si `envio.getOrigen() = "Buenos Aires"` y `envio.getPeso() = 2.5`, el mock verifica que `dhlAPI.registrarPaquete("Buenos Aires", destino, 2.5)` fue invocado.

2. **Testear contra la API real**: test de integración que se activa en CI con credenciales reales. Verifica que la comunicación con DHL funciona end-to-end. Separado de los tests unitarios porque es lento y tiene efectos externos.

El adapter tiene poca lógica propia (solo traduce) — la mayor parte de los bugs están en que los parámetros se extraigan del `Envio` en el orden correcto.

---

### Bridge

#### Actividad 3 — ¿Hay múltiples tipos y múltiples formas? ¿Terminarías con explosión de combinaciones?

**Tipos de reporte (la abstracción):**
- `ReporteEnvios` — lista todos los envíos del período
- `ReporteIngresos` — muestra facturación
- `ReporteDesempenoProveedores` — métricas por carrier

**Formatos de salida (la implementación):**
- `GeneradorPDF`, `GeneradorExcel`, `GeneradorJSON`, `GeneradorCSV`

Sin Bridge, necesitarías 3 tipos × 4 formatos = **12 clases**:
`ReporteEnviosPDF`, `ReporteEnviosExcel`, `ReporteEnviosJSON`, `ReporteEnviosCSV`,
`ReporteIngresosPDF`, `ReporteIngresosExcel`, ... y así.

Agregar un formato nuevo (XML) requeriría 3 clases nuevas. Agregar un tipo nuevo (Auditoría) requeriría 4 clases nuevas. El problema crece multiplicativamente.

Con Bridge: **3 + 4 = 7 clases**. Agregar XML: 1 clase. Agregar Auditoría: 1 clase. Crece sumando, no multiplicando.

---

#### ¿Cuál es la abstracción y cuál la implementación?

**Abstracción:** `Reporte` (clase abstracta) y sus subclases — definen *qué* se reporta (los datos, el contenido de negocio)

```java
public abstract class Reporte {
    protected GeneradorReporte generador;  // referencia al "lado implementación"

    public abstract String generarContenido();  // subclases definen el qué

    public String generar() {
        return generador.formatear(generarContenido());  // delega el cómo
    }
}
```

**Implementación:** `GeneradorReporte` (interfaz) y sus clases concretas — definen *cómo* se formatea

```java
public interface GeneradorReporte {
    String formatear(String contenido);
    String obtenerExtension();
}
```

El bridge es el campo `protected GeneradorReporte generador` dentro de `Reporte`. Es la referencia que cruza del "lado abstracción" al "lado implementación".

---

#### Actividad 4 — Si agregás reporte "Auditoría" en XML, ¿cuántas clases nuevas? (2, no 5)

- `ReporteAuditoria extends Reporte` — implementa `generarContenido()` con los datos de auditoría
- `GeneradorXML implements GeneradorReporte` — implementa `formatear()` con salida XML

Total: **2 clases nuevas**. `ReporteEnvios`, `ReporteIngresos`, `ReporteDesempenoProveedores`, `GeneradorPDF`, `GeneradorExcel`, `GeneradorJSON`, `GeneradorCSV` — sin tocar.

---

#### ¿Puedo cambiar el generador en tiempo de ejecución?

Sí. `Reporte` expone `setGenerador(GeneradorReporte generador)`. En el demo de hito 8 se puede hacer:

```java
ReporteEnvios reporte = new ReporteEnvios(new GeneradorPDF(), envios);
reporte.generar();                          // genera PDF

reporte.setGenerador(new GeneradorExcel()); // mismo reporte, formato distinto
reporte.generar();                          // genera Excel
```

Esto es lo que Bridge llama "variar las dos dimensiones independientemente". El contenido del reporte no cambia — solo el formato de salida.

---

#### Actividad 4 — ¿Por qué separar abstracción de implementación?

El *qué* (contenido del reporte) y el *cómo* (formato de salida) cambian por **razones distintas** y en **momentos distintos**:

- El área de negocio puede pedir un nuevo tipo de reporte (Auditoría) → toca solo el lado abstracción
- TI puede agregar soporte para XML → toca solo el lado implementación
- Un usuario puede querer el mismo reporte en distintos formatos → se cambia el generador en runtime

Si los fusionáramos en una jerarquía única (`ReporteEnviosPDF extends Reporte`), cada eje de cambio afecta a la otra dimensión. Eso es exactamente lo que GRASP Protected Variations identifica como punto de variación a proteger.

---

### Composite

#### Actividad 5 — Estructura jerárquica, hojas y contenedores

La red de distribución de LogiSmart tiene tres niveles:

```
CentroRegional "Argentina"          ← contenedor (tiene hijos)
  CentroRegional "Zona Norte"       ← contenedor (tiene hijos)
    PuntoEntrega "Rosario" (50/100) ← hoja (sin hijos, almacena Envios)
    PuntoEntrega "Córdoba" (30/80)  ← hoja
  PuntoEntrega "Buenos Aires" (200/500) ← hoja
```

**Hoja:** `PuntoEntrega` — capacidad fija, almacena `Envio`s, no tiene subcentros
**Contenedor:** `CentroRegional` — tiene `List<CentroDistribucionComposite> subcentros`, puede contener hojas u otros contenedores

**Tratamiento uniforme:** el cliente llama `centro.obtenerCapacidad()` sobre cualquier nodo — no sabe ni le importa si es hoja o contenedor. `CentroDistribucionComposite` es el tipo común para ambos.

---

#### Actividad 6 — ¿Por qué los cálculos son recursivos?

Porque la estructura es árbol. `CentroRegional.obtenerCapacidad()` suma la capacidad de sus hijos:

```java
// CentroRegional.java
@Override
public int obtenerCapacidad() {
    int total = 0;
    for (CentroDistribucionComposite centro : subcentros) {
        total += centro.obtenerCapacidad();  // llamada recursiva — puede ser otro CentroRegional
    }
    return total;
}

// PuntoEntrega.java — caso base, termina la recursión
@Override
public int obtenerCapacidad() {
    return capacidad;  // retorna su valor fijo, sin hijos que recorrer
}
```

El patrón completo: el nodo raíz llama al método → propaga hacia abajo → las hojas retornan valores → los contenedores suman y retornan hacia arriba. La recursión es la estructura del árbol mismo.

---

#### ¿Qué hace `agregar()` en una hoja: excepción o no-op? ¿Por qué?

En la implementación de LogiSmart, `PuntoEntrega` directamente **no tiene** el método `agregar(CentroDistribucionComposite)`. Solo `CentroRegional` lo tiene.

Este es el diseño "safe Composite": la interfaz base (`CentroDistribucionComposite`) solo declara los métodos de componente (`obtenerCapacidad`, `obtenerOcupacion`, `mostrar`). Los métodos de gestión de hijos (`agregar`, `remover`) solo viven en el contenedor concreto.

La alternativa es declarar `agregar()` en la interfaz base y que la hoja lance `UnsupportedOperationException`. Trade-off:

| Enfoque | Ventaja | Desventaja |
|---|---|---|
| **Safe (el nuestro)**: `agregar()` solo en CentroRegional | El compilador impide llamar `agregar()` en una hoja — error en compilación, no en runtime | El cliente necesita hacer cast si quiere agregar hijos |
| **Transparent**: `agregar()` en la interfaz base | El cliente trata a todos por igual para siempre | Si llama `agregar()` en una hoja, explota en runtime |

---

#### ¿Qué pasa si agregás un nuevo nivel?

Si hay que agregar "Zona" entre `CentroRegional` y `PuntoEntrega`:

1. Crear `Zona extends CentroDistribucionComposite` con `List<CentroDistribucionComposite>` y su lógica recursiva
2. `agregar(CentroDistribucionComposite)` en `Zona`

Ningún cliente que use `CentroDistribucionComposite` cambia — llaman `obtenerCapacidad()` igual que antes. El árbol crece un nivel pero la interfaz es la misma.

---

### Adapter vs. Bridge — la diferencia de intención

La confusión más frecuente: los dos "separan interfaces". La diferencia es el *momento* y la *intención*:

| | Adapter | Bridge |
|---|---|---|
| **Cuándo** | Algo ya existe y no lo podés cambiar | Lo diseñás desde cero |
| **Intención** | Arreglar incompatibilidad entre dos interfaces existentes | Evitar explosión de combinaciones desde el diseño |
| **Dirección** | El cliente no cambia; la clase adaptada no cambia; el adapter es el pegamento | Abstracción e implementación se diseñan para variar independientemente |
| **En LogiSmart** | DHL, FedEx, PayPal ya existen con sus APIs — se adaptan al dominio | El sistema de reportes se diseñó desde el principio con la separación tipo/formato |

Clave para la defensa: **Adapter es una solución a un problema que ya ocurrió. Bridge es una decisión de diseño preventiva.**

---

### Integración — los tres patrones en el flujo de un envío

```
1. OperadorLogistico crea el envío
       │
2. LogiSmartController llama al ProveedorEnvio (ADAPTER)
       └── AdapterDHL.crearEnvio(envio)
           └── dhlAPI.registrarPaquete(origen, destino, peso)

3. El envío se asigna a un PuntoEntrega de la red (COMPOSITE)
       └── centroRegional.obtenerCapacidad()  ← recursivo por toda la jerarquía
           └── verifica si hay espacio antes de aceptar el envío

4. AdminEmpresa solicita reporte de estado del envío (BRIDGE)
       └── new ReporteEnvios(new GeneradorPDF(), envios).generar()
           └── generarContenido() → contenido del reporte de envíos
           └── generador.formatear(contenido) → salida en PDF
```

Los tres patrones aparecen en el mismo flujo de negocio (gestión de un envío) pero en momentos distintos y resolviendo problemas distintos: Adapter al comunicarse con servicios externos, Composite al gestionar la capacidad de la red, Bridge al generar la documentación.

---

#### Escalabilidad — nuevo proveedor, nuevo formato, nuevo nivel

| Cambio | Clases nuevas | Clases modificadas |
|---|---|---|
| Nuevo carrier (MercadoEnvíos) | `AdapterMercadoEnvios` | ninguna |
| Nuevo formato de reporte (XML) | `GeneradorXML` | ninguna |
| Nueva pasarela de pago (Mercado Pago) | `AdapterMercadoPago` | ninguna |
| Nuevo tipo de reporte (Auditoría) | `ReporteAuditoria` | ninguna |
| Nuevo nivel en la red (Zona) | `Zona` | ninguna |

El sistema es O(1) en modificaciones — siempre cero clases existentes modificadas, independientemente de cuántas extensiones se hagan.

---

## 22. Hito 10 — Chain of Responsibility, Command, Interpreter

### Chain of Responsibility

#### ¿Por qué encadenar validadores en vez de un método con 5 ifs seguidos?

**Single Responsibility:** con 5 ifs en un método, esa clase tiene 5 razones para cambiar — si cambia la lógica de validación de datos, de pago, de inventario, de seguridad o de capacidad, todas tocan el mismo método. Con la cadena, cada validador tiene una sola razón para cambiar.

**Open/Closed:** para agregar `ValidadorAduana`, se crea la clase y se inserta en `CadenaValidadores`. El método monolítico requeriría abrirlo y editarlo.

Con ifs también hay un problema de testeo: el método completo necesita que todas las condiciones sean satisfacibles para testear cada rama. Con la cadena, cada validador se testea en aislamiento instanciándolo directamente.

---

#### ¿Quién decide el orden de la cadena? ¿Importa?

`CadenaValidadores` en su constructor: `ValidadorDatos → ValidadorInventario → ValidadorPago → ValidadorSeguridad → ValidadorCapacidad`.

Sí importa — el orden implementa **fail-fast**: las validaciones más baratas y generales van primero.

- `ValidadorDatos` va primero porque valida campos básicos (origen, destino, peso no vacíos). Si el origen es null y `ValidadorInventario` fuera primero, haría una consulta al sistema de inventario con datos nulos — trabajo inútil que puede lanzar NullPointerException.
- `ValidadorInventario` va antes que `ValidadorPago` porque consultar stock es más barato que llamar a una pasarela de pago externa.

El principio: validaciones locales sin efectos secundarios van antes que validaciones que consultan sistemas externos.

---

#### ¿La cadena corta al primer fallo o siguen todos los validadores? ¿Por qué?

Corta al primer fallo. En cada validador:

```java
// ValidadorDatos.java
public boolean validar(ContextoValidacion ctx) {
    if (envio.getOrigen() == null || envio.getOrigen().isEmpty()) {
        return false;  // retorna false, siguiente nunca se llama
    }
    // ...validaciones pasan...
    return siguiente == null || siguiente.validar(ctx);  // propaga solo si pasó
}
```

Si falla, retorna `false` directamente sin llamar a `siguiente`. Si pasa, propaga. Si no hay siguiente, retorna `true`.

¿Por qué fail-fast y no ejecutar todos? Dos razones:
1. **Eficiencia**: no tiene sentido consultar inventario si los datos son inválidos.
2. **Efectos secundarios**: si un validador tuviera efectos (ej. reservar stock temporalmente), ejecutarlo con datos inválidos generaría estado inconsistente.

---

#### ¿Qué pasa si agregás un sexto validador (aduana)?

1. Crear `ValidadorAduana extends ValidadorEnvio` — implementa `validar()` con la lógica de aduana
2. En `CadenaValidadores`, insertar con `v5.setSiguiente(v6 = new ValidadorAduana())` en el lugar correcto del orden

Solo se toca `CadenaValidadores` para el ensamblaje. Los otros 5 validadores no cambian.

---

#### ¿Cómo testear un validador en aislamiento, sin armar la cadena entera?

Instanciar directamente el validador. Como `siguiente` es `null` por defecto, evalúa solo su responsabilidad:

```java
ValidadorDatos v = new ValidadorDatos();
// siguiente es null → solo valida datos, no propaga
assert v.validar(new ContextoValidacion(envioConDatosOk, cobro)) == true;
assert v.validar(new ContextoValidacion(envioSinOrigen, cobro)) == false;
```

No es necesario construir la cadena completa. Cada validador es una unidad testeable independiente.

---

### Command

#### ¿Qué encapsula exactamente un Comando?

La operación como objeto: **la solicitud + el receptor + los parámetros**. En `ComandoCrearEnvio`:

```java
private final ServicioEnvios servicio;  // receptor — contiene la lógica real
private final Envio envio;              // parámetro — dato de la operación
private String numeroSeguimiento;       // estado resultante — necesario para deshacer
```

El receptor (`ServicioEnvios`) es quien sabe cómo crear un envío. El comando no tiene lógica de negocio — solo sabe a quién llamar y con qué. Esta separación es lo que permite diferir la ejecución, encolarla y revertirla.

---

#### ¿Cómo se implementó deshacer()? ¿Estado anterior u operación inversa? ¿Diferencia con Memento?

Dos estrategias según el comando:

- `ComandoCrearEnvio.deshacer()` → **operación inversa**: llama `servicio.cancelarEnvio(numeroSeguimiento)`. Guarda el número resultante del `ejecutar()` para poder referenciar qué cancelar.
- `ComandoCancelarEnvio.deshacer()` → también operación inversa: llama `servicio.reactivarEnvio(numeroSeguimiento)`.

**Diferencia con Memento:** Memento guarda un snapshot del estado interno completo del objeto (`MementoEnvio` captura todos los campos del `Envio`). Es útil cuando la operación inversa no existe o sería costosa. Command conoce y ejecuta la inversa directamente — es suficiente cuando la inversa es clara y barata. Son complementarios: Memento (Hito 11) guarda el estado del `Envio` antes del pipeline independientemente de si hay un Command.

---

#### ¿Por qué Command habilita auditoría/logging "gratis"?

`ColaComandos` mantiene `List<Comando> historial`. Cada comando tiene `obtenerDescripcion()`. `mostrarHistorial()` imprime toda la secuencia con `→` marcando el estado actual:

```
  1. Crear envío de Buenos Aires a Córdoba
→ 2. Actualizar estado: ENV-001 → EN TRÁNSITO
  3. Cambiar método de pago: ENV-001 → EFECTIVO
```

Para agregar logging real (a base de datos, a archivo), basta con iterar `historial` en `ejecutar()`. El cliente no necesita instrumentar nada — el patrón ya captura todo.

---

#### ¿Qué estructura usa la cola para undo/redo? ¿Qué pasa con redo al ejecutar algo nuevo?

No son dos pilas sino **una lista con un índice** (`List<Comando> historial + int indiceActual`):

- `undo()` → decrementa `indiceActual`
- `redo()` → incrementa `indiceActual` y re-ejecuta el comando
- `ejecutar()` nuevo → **trunca la lista** desde `indiceActual+1` en adelante antes de agregar el nuevo

¿Qué pasa con redo al ejecutar algo nuevo? Se pierde. Si el historial tiene [A, B, C], se deshacen C y B (índice apunta a A), y se ejecuta D → la lista queda [A, D]. B y C desaparecen — no se puede rehacer algo que ya no está en la línea de tiempo activa.

---

#### ¿Quién es el Invoker, el Receiver y el ConcreteCommand?

| Rol | Clase | Responsabilidad |
|---|---|---|
| **Invoker** | `ColaComandos` | Llama `ejecutar()` y `deshacer()` sin saber qué hace el comando |
| **Receiver** | `ServicioEnvios` | Lógica real: `crearEnvio()`, `cancelarEnvio()`, `reactivarEnvio()` |
| **ConcreteCommand** | `ComandoCrearEnvio`, `ComandoCancelarEnvio`, ... | Conoce al Receiver y los parámetros; encapsula la operación |
| **Client** | `SistemaLogisticaCompleto` | Crea el comando concreto y lo pasa al Invoker |

---

### Interpreter

#### Expresión terminal vs no-terminal. Ejemplos del proyecto

**Terminales** (hojas del árbol — evalúan directamente sin sub-expresiones):
- `ExpresionDestino("Córdoba")` → `valor.equals(envio.getDestino())`
- `ExpresionPeso(10, "<")` → `envio.getPeso() < 10`
- `ExpresionCosto(100, ">")` → `envio.getCosto() > 100`
- `ExpresionOrigen`, `ExpresionRestringido`

**No-terminales** (contenedores — combinan otras expresiones):
- `ExpresionAND(izq, der)` → `izq.evaluar() && der.evaluar()`
- `ExpresionOR(izq, der)` → `||`
- `ExpresionNOT(expresion)` → `!expresion.evaluar()`

---

#### La regla "envíos a Córdoba con peso < 10" como árbol

```
ExpresionAND
├── ExpresionDestino("Córdoba")    ← terminal izquierda
└── ExpresionPeso(10, "<")         ← terminal derecha
```

En el código (`SistemaLogisticaCompleto.inicializarReglas()`):
```java
new ExpresionAND(
    new ExpresionDestino("Córdoba"),
    new ExpresionPeso(10, "<"))
```

Evaluación: `ExpresionAND` evalúa `izquierda.evaluar(envio)` primero. Si es `false`, el `&&` de Java hace short-circuit y no evalúa la derecha. Los terminales retornan el booleano sin recursión adicional — son el caso base.

---

#### ¿Qué pasa si necesitás OR o NOT? ¿Cuántas clases?

Ya están implementados: `ExpresionOR` y `ExpresionNOT` existen. Si faltara alguno, sería **1 clase nueva** que implementa `Expresion`. Las expresiones existentes no se tocan.

---

#### ¿Por qué no escribir las reglas como ifs? ¿Cuándo se justifica Interpreter?

Con ifs hardcodeados, cada cambio de regla requiere modificar el código y hacer un deploy. Las reglas de negocio (qué destinos son elegibles, umbrales de peso o costo) cambian más frecuentemente que el código estructural.

Con Interpreter, las reglas son objetos que se pueden construir desde una base de datos, un archivo de configuración o una UI. Un operador puede configurar "bloquear envíos a zona X con peso > 50 kg" sin tocar el código.

**Se justifica:** reglas que varían en producción, gramática definida (AND/OR/NOT sobre atributos), usuarios de negocio que configuran reglas.

**Es sobre-ingeniería:** reglas fijas que raramente cambian, filtros simples sin composición, sin necesidad de configuración dinámica.

---

#### ¿Qué relación tiene con Composite?

El árbol de expresiones **es** un árbol Composite:
- Terminales = **hojas** (sin hijos, retornan valor directamente)
- No-terminales = **contenedores** (tienen hijos `Expresion`, combinan sus resultados)

`Expresion` hace el papel de `CentroDistribucionComposite`: el cliente llama `evaluar(envio)` sin saber si tiene una terminal o un AND con tres niveles de profundidad.

La diferencia de propósito: Composite modela una jerarquía de objetos del dominio. Interpreter modela una gramática para evaluar reglas. La estructura es la misma; el objetivo es distinto.

---

### Integración — los tres patrones en el flujo de `procesarEnvio()`

El orden está explícito en `SistemaLogisticaCompleto.procesarEnvio()`:

```java
// 1. Chain: validar — si falla, retorna null SIN encolar nada
if (!validadores.validarEnvio(ctx)) {
    return null;
}

// 2. Command: encolar y ejecutar — solo si pasó la validación
ComandoCrearEnvio cmd = new ComandoCrearEnvio(servicio, envio);
cola.ejecutar(cmd);
String numero = cmd.getNumeroSeguimiento();

// 3. Interpreter: evaluar reglas — no bloquea, solo informa
for (Map.Entry<String, Expresion> entry : reglas.entrySet()) {
    entry.getValue().evaluar(envio);
}
```

**¿Se encola el comando si falla la validación?** No. El guard clause (`return null`) garantiza que `cola.ejecutar(cmd)` nunca se ejecuta con datos inválidos. La decisión vive en el punto de integración, no en ninguno de los tres patrones.

---

#### ¿Por qué los tres son "de comportamiento" y no estructurales?

Los patrones estructurales definen **cómo se componen las clases** (Adapter traduce interfaces, Bridge separa jerarquías).

Los patrones de comportamiento definen **cómo los objetos interactúan y se reparten responsabilidades en runtime**:
- Chain distribuye el procesamiento entre handlers — cada uno decide si procesa o pasa
- Command encapsula una operación para diferirla, revertirla o encolarla — el Invoker no sabe qué hace el comando
- Interpreter evalúa una gramática componible — el árbol se recorre recursivamente en runtime

No es sobre la estructura estática de las clases sino sobre quién hace qué, cuándo y cómo se comunican durante la ejecución.

---

---

## 23. Hito 11 — Iterator, Mediator, Memento, Observer

### Iterator

#### ¿Qué le oculta el Iterator al cliente?

La estructura interna de almacenamiento. El cliente nunca sabe si está recorriendo:
- `ColeccionArray` → array de tamaño fijo con cursor de índice
- `ColeccionHash` → `HashMap<String, Envio>` con iterador del map subyacente
- `ColeccionLista` → `LinkedList` con recorrido encadenado

El cliente solo ve `tieneSiguiente()` y `obtenerSiguiente()`. Puede cambiar la implementación de `ColeccionEnvios` sin tocar el código de recorrido.

---

#### ¿Por qué el cliente puede recorrer un array y un hash con el mismo código?

Porque ambas implementaciones de `ColeccionEnvios` proveen un `IteradorEnvios` a través de `crearIterador()`. El cliente escribe:

```java
IteradorEnvios it = coleccion.crearIterador();
while (it.tieneSiguiente()) {
    Envio e = it.obtenerSiguiente();
    // procesar
}
```

No importa si `coleccion` es `ColeccionArray` o `ColeccionHash`. La interfaz `IteradorEnvios` garantiza el contrato — `tieneSiguiente()` y `obtenerSiguiente()` siempre funcionan igual desde afuera.

---

#### Para el árbol de envíos por región: ¿qué recorrido implementa? ¿Dónde vive esa decisión?

La implementación no tiene un árbol de envíos por región — tiene `ColeccionArray` (recorrido secuencial por índice creciente) y `ColeccionHash` (recorrido según orden interno del `HashMap`, sin garantías de orden).

La decisión del recorrido vive en el **iterador interno de cada colección**, no en el cliente. `IteradorArray` incrementa un índice; `IteradorHash` delega en el iterador nativo del Map:

```java
// IteradorHash
private Iterator<Envio> cursor = envios.values().iterator();
```

Si hubiera un árbol, el iterador interno decidiría si hace DFS o BFS — el cliente usaría exactamente el mismo `while (it.tieneSiguiente())` sin saber cuál es.

---

#### ¿Qué pasa si se modifica la colección mientras iterás? ¿Cómo lo maneja Java y cómo el nuestro?

**Java estándar** (`ArrayList`, `HashMap`): usa un `modCount` interno que se incrementa en cada modificación estructural. El iterador guarda el `modCount` al crearse y lo verifica en cada `next()`. Si cambió, lanza `ConcurrentModificationException` — *fail-fast* para detectar modificaciones concurrentes durante la iteración.

**El iterador propio** (`IteradorArray`, `IteradorHash`): no tiene esa protección. Si se agrega un elemento a `ColeccionArray` mientras el `IteradorArray` está activo, el `tamaño` aumenta y el iterador lo vería en el próximo `tieneSiguiente()` — podría iterar el elemento nuevo aunque no estuviera al crearse el iterador. Es una simplificación aceptable para el TPO.

---

#### ¿Relación con Iterable/for-each de Java? ¿Por qué el patrón está "incorporado" al lenguaje?

El for-each de Java:
```java
for (Envio e : coleccion) { ... }
```
es azúcar sintáctico para:
```java
java.util.Iterator<Envio> it = coleccion.iterator();
while (it.hasNext()) { Envio e = it.next(); }
```

`java.util.Iterator` (con `hasNext()`, `next()`, `remove()`) e `java.lang.Iterable` (con `iterator()`) son exactamente el patrón GoF Iterator incorporado al JDK. Los diseñadores de Java lo estandarizaron porque el recorrido uniforme sobre colecciones es tan fundamental que no tiene sentido reimplementarlo en cada proyecto. `IteradorEnvios` del proyecto es una reimplementación del mismo concepto con nombre en español para demostrar la comprensión del patrón.

---

### Mediator

#### Sin Mediator, 5 componentes todos-con-todos: ¿cuántas relaciones? ¿Y con Mediator?

- **Sin Mediator**: n(n-1)/2 = 5×4/2 = **10 relaciones bidireccionales**. Cada componente conoce a los otros 4.
- **Con Mediator**: **5 relaciones** — cada componente solo conoce al mediador.

Con 5 componentes (`CentroDistribucionMediator`, `ValidadorEnvioMediator`, `SistemaPago`, `SistemaNotificacion`, `SistemaAuditoria`), sin el patrón habría que actualizar hasta 4 clases cada vez que se agrega un componente nuevo.

---

#### ¿Los componentes se conocen entre sí? ¿Qué conocen?

Solo conocen al mediador. `CentroDistribucionMediator` declara:

```java
private final MediadorEnvios mediador;

public void crearEnvio(Envio envio) {
    mediador.notificar("ENVIO_CREADO", envio);  // no sabe quién reacciona
}
```

No tiene ni idea de que existe `ValidadorEnvioMediator`, `SistemaPago` o `SistemaNotificacion`. Publica un evento y el mediador decide qué hacer con él.

---

#### ¿Qué riesgo tiene el Mediator? ¿Cómo se evitó?

El riesgo es convertirse en un **God Object**: el mediador termina siendo el único lugar que conoce toda la lógica del sistema — exactamente lo que intentábamos evitar en los componentes.

`MediadorEnviosConcreto.notificar()` ya tiene 6 cases en el switch. Si el sistema creciera con más eventos y más lógica de enrutamiento, ese método se vuelve intratable.

¿Cómo se mitigó? El mediador **no tiene lógica de negocio** — solo enruta:
```java
case "ENVIO_CREADO":
    auditoria.registrar("ENVIO_CREADO", envioCreado);
    validador.validar(envioCreado);  // el Validador contiene la lógica de validación
    break;
```
El `validador` sabe cómo validar. El `pago` sabe cómo procesar pagos. El mediador solo sabe *a quién llamar ante cada evento*, no *cómo* hacerlo.

---

#### ¿Diferencia entre Mediator y Facade?

Ambos centralizan, pero con propósito distinto:

| | Facade | Mediator |
|---|---|---|
| **Comunicación** | Unidireccional — cliente llama, subsistemas no se llaman entre sí a través de la Facade | Bidireccional — los componentes emiten eventos, el mediador los enruta de vuelta a otros componentes |
| **Los subsistemas se conocen entre sí** | No importa — el cliente tampoco los conoce | No — solo conocen al mediador |
| **Intención** | Simplificar el acceso a un subsistema complejo | Coordinar la comunicación entre pares sin acoplamiento directo |
| **En LogiSmart** | `ServicioLogisticaFacade` — cliente llama `crearEnvio()`, adentro pasan 5 operaciones en silencio | `MediadorEnviosConcreto` — `CentroDistribucion` emite, `Validador` emite `VALIDACION_OK`, `Pago` emite `PAGO_CONFIRMADO` |

---

#### ¿Qué pasa si agregás un sexto componente (SistemaAduana)?

1. Crear `SistemaAduana` con campo `MediadorEnvios mediador`
2. Agregar `registrarAduana(SistemaAduana)` en la interfaz `MediadorEnvios` y en `MediadorEnviosConcreto`
3. En `notificar()`, agregar los casos para los eventos de aduana (ej. `ADUANA_APROBADA`, `ADUANA_RECHAZADA`)
4. Registrar en `SistemaLogisticaEventDriven`

Los 5 componentes existentes no se modifican. Solo se toca el mediador para el enrutamiento del nuevo evento.

---

### Memento

#### ¿Quiénes son Originator, Memento y Caretaker?

| Rol | Clase | Responsabilidad |
|---|---|---|
| **Originator** | `Envio` | Crea mementos con `crearMemento()` y se restaura con `restaurarDesdeMemento(MementoEnvio)` |
| **Memento** | `MementoEnvio` | Snapshot inmutable: `estado`, `origen`, `destino`, `peso`, `costo`, `timestamp` — todos `final` |
| **Caretaker** | `HistorialEnvios` | Almacena la lista de mementos y gestiona la navegación con `indiceActual` |

---

#### ¿Por qué el Caretaker no debe poder leer el contenido del memento? ¿Cómo se protege en Java?

El Caretaker debe almacenar y entregar mementos, pero no modificar el estado capturado. Si pudiera escribir en el memento, podría alterar el historial de estados del envío de forma no controlada.

En Java "clásico", la protección se logra con **clase interna privada**: `Envio.Memento` — solo la clase `Envio` puede instanciar y leer su propia clase interna privada. El Caretaker recibe objetos de tipo `Envio.Memento` que no puede constructirlos ni leer sus campos.

En la implementación, `MementoEnvio` es una clase pública con getters públicos. Es una flexibilización del patrón estricto — Java no tiene "friend classes" como C++. La defensa: `MementoEnvio` tiene solo getters (no setters) y todos los campos son `final`. El Caretaker puede leer el estado, pero no modificarlo. La mutación del `Envio` solo ocurre a través de `envio.restaurarDesdeMemento()`, que controla el propio Originator.

---

#### Diferencia con el undo de Command (Hito 10). ¿Cuándo conviene cada uno?

| | Command | Memento |
|---|---|---|
| **Mecanismo** | Conoce la operación inversa (`cancelar` = inverso de `crear`) | Guarda un snapshot completo del estado |
| **Cuándo aplica** | La operación tiene un inverso claro y barato | El estado es complejo, no hay inverso simple, o se necesita navegar a cualquier punto |
| **En LogiSmart** | `ComandoCrearEnvio.deshacer()` → llama `cancelarEnvio()` | `HistorialEnvios` → restaura el estado completo del `Envio` a cualquier punto anterior o posterior |

Pueden coexistir: Command para deshacer operaciones de alto nivel (crear/cancelar), Memento para guardar el estado interno del dominio antes del pipeline del Mediator.

---

#### ¿Qué costo tiene guardar un memento por cada cambio? ¿Cómo se mitigaría?

Cada `MementoEnvio` guarda 3 Strings + 2 doubles + 1 long ≈ ~100 bytes. Con 10.000 envíos y 50 cambios de estado cada uno: 500.000 mementos × 100 bytes = **~50 MB**. No trivial en producción.

Mitigaciones:
- **Límite de historial**: ventana deslizante de N mementos — descartar los más antiguos al superar el límite
- **Snapshots parciales (delta)**: guardar solo los campos que cambiaron, no el objeto completo
- **Puntos de control**: guardar solo en transiciones significativas (no en cada cambio menor)
- **Almacenamiento externo**: serializar y persistir en BD o archivo comprimido en vez de mantener en memoria

---

#### ¿El Memento rompe el encapsulamiento? Defendé por qué no.

No. La pregunta trampa es: "si `MementoEnvio` expone el estado de `Envio` con getters públicos, cualquiera puede leer el estado interno de `Envio` a través del Memento".

La respuesta: el encapsulamiento que importa es el de `Envio`. Nadie puede **modificar** el estado de `Envio` a través del `MementoEnvio` — este solo tiene getters, no setters, y todos sus campos son `final`.

- La **creación** del Memento es controlada por `Envio.crearMemento()` — el `Envio` decide qué capturar y cuándo
- La **restauración** es controlada por `Envio.restaurarDesdeMemento()` — el `Envio` decide cómo interpretarlo
- El `Caretaker` solo guarda y entrega objetos `MementoEnvio` sin poder alterar el estado del `Envio` directamente

Encapsulamiento = el objeto controla cómo se modifica su estado. Eso se mantiene intacto.

---

### Observer

#### ¿Quién es el sujeto y quiénes los observadores? ¿Quién dispara la notificación y cuándo?

**Sujeto**: `Envio` — mantiene la lista `List<ObservadorEnvio> observadores` y notifica en cada cambio de estado.

**Observadores**: `DashboardObservador`, `SistemaNotificacionObservador`, `CentroDistribucionObservador`, `SistemaAuditoria` (que también implementa `ObservadorEnvio`).

La notificación se dispara en cualquier método que cambia el estado:
```java
public void cambiarEstado(String nuevoEstado) {
    this.estado = nuevoEstado;
    notificarObservadores();  // siempre al final del cambio
}
// Igual en iniciar(), cancelar(), cerrar()
```

El patrón garantiza que ningún cambio de estado pase desapercibido para los observadores suscritos.

---

#### Push o pull: ¿cuál se usó?

**Push**: el sujeto envía el objeto completo al observador:

```java
private void notificarObservadores() {
    for (ObservadorEnvio obs : observadores) {
        obs.actualizar(this);  // 'this' es el Envio completo
    }
}
```

La alternativa **pull** sería que `actualizar()` no recibiera datos y el observador llamara `envio.getEstado()` por sí mismo — requiere que el observador guarde la referencia al sujeto.

Se eligió push porque los observadores de LogiSmart típicamente necesitan múltiples campos (`DashboardObservador` muestra id + estado; `SistemaAuditoria` registra todo el contexto). Pasar el objeto completo es más conveniente que forzar a cada observador a pedir cada campo por separado.

---

#### ¿En qué orden se notifican los observadores? ¿El código debería depender de ese orden?

El orden es el de inserción en el `ArrayList` — el orden en que se llamó `adjuntarObservador()`. No hay garantía de orden de negocio.

El código **no debería depender** de ese orden. Si `SistemaAuditoria` necesita ejecutarse después que `DashboardObservador`, eso es acoplamiento temporal implícito entre observadores — una señal de diseño incorrecto. Los observadores deben ser independientes entre sí; si hay dependencias de orden, probablemente se necesite Chain of Responsibility o Mediator en vez de Observer.

---

#### ¿Qué pasa si un observador lanza una excepción o tarda mucho?

```java
private void notificarObservadores() {
    for (ObservadorEnvio obs : observadores) {
        obs.actualizar(this);  // si lanza, los siguientes no se ejecutan
    }
}
```

Si un observador lanza una excepción sin capturar, el `for` se interrumpe — los observadores siguientes **no** reciben la notificación. Bug silencioso: el Dashboard podría no actualizarse porque la Auditoría falló antes.

Si un observador tarda mucho (ej. envía un email real), bloquea el hilo del sujeto y todos los demás observadores esperan.

Solución para código productivo: `try-catch` por observador para aislar fallos, o ejecución asíncrona (cada notificación en un `CompletableFuture`). En LogiSmart es aceptable porque los observadores son demos sin operaciones externas reales.

---

#### ¿Cómo evitás memory leaks con observadores que nunca se desuscriben?

Si un objeto `ObservadorEnvio` tiene una referencia en `envio.observadores` y el resto del sistema ya no lo referencia, el GC no puede recolectarlo — la lista en `Envio` lo mantiene vivo indefinidamente.

Solución inmediata: siempre llamar `desadjuntarObservador()` cuando el observador termina su ciclo de vida. `Envio` expone `desadjuntarObservador(ObservadorEnvio)` exactamente para eso.

Solución más robusta: usar `WeakReference<ObservadorEnvio>` en la lista — el GC puede recolectar el observador aunque esté en la lista, y la lista lo elimina en el próximo recorrido.

En el demo de Hito 11, los observadores son instancias locales que se crean por envío — no hay riesgo real. En producción (observadores de larga vida suscritos a envíos de corta vida) sería crítico.

---

### Integración / Event-Driven

#### ¿Por qué Observer + Mediator dan una arquitectura "event-driven"?

- **Observer** es el mecanismo de emisión 1→N anónimo: `Envio` cambia de estado y notifica a todos los suscritos sin saber quiénes son.
- **Mediator** es el bus de eventos con enrutamiento explícito: `CentroDistribucion` emite `ENVIO_CREADO`, el mediador decide llamar al Validador.

Juntos: el sistema reacciona a eventos en vez de invocar métodos directamente. `CentroDistribucion` no llama a `validador.validar(envio)` — publica un evento y el mediador enruta. El `Envio` no llama a `dashboard.actualizar()` — notifica y cada observador decide qué hacer.

---

#### En `procesarEnvios()`: los 4 patrones en el flujo

```java
for (Envio envio : envios) {
    historial.guardarEstado(envio);       // MEMENTO — snapshot estado inicial
    coleccion.agregar(envio);            // ITERATOR — incorpora a la colección iterable
    envio.adjuntarObservador(centroObs); // OBSERVER — suscribe observadores reactivos
    envio.adjuntarObservador(notifObs);
    envio.adjuntarObservador(auditoria);
    centro.crearEnvio(envio);            // MEDIATOR — lanza pipeline ENVIO_CREADO→...
    historial.guardarEstado(envio);      // MEMENTO — snapshot estado post-pipeline
}
// Al mostrar:
IteradorEnvios it = coleccion.crearIterador();  // ITERATOR — recorre sin conocer la estructura
while (it.tieneSiguiente()) { it.obtenerSiguiente(); }
```

---

#### Observer vs Mediator — ambos desacoplan, ¿cuál es la diferencia?

| | Observer | Mediator |
|---|---|---|
| **Relación** | 1 sujeto → N observadores anónimos | N componentes coordinados a través de 1 mediador |
| **El emisor conoce receptores** | No — solo sabe que implementan `ObservadorEnvio` | No — solo sabe el nombre del evento |
| **El receptor conoce al emisor** | Recibe el objeto en `actualizar(Envio)` | Solo conoce al mediador |
| **Enrutamiento** | Ninguno — todos los observadores reciben todo | El mediador decide qué componente reacciona a qué evento |
| **Uso** | Notificación reactiva a cambios de estado | Orquestación de un pipeline con pasos bien definidos |

Observer = "publico que algo cambió, no sé quién escucha ni qué hace". Mediator = "publico `ENVIO_CREADO`, el mediador sabe que debe llamar al Validador y a la Auditoría".

---

#### ¿Por qué `SeguimientoEnvio` y `MementoEnvio` viven en el dominio y no en infraestructura?

`MementoEnvio` captura datos puramente de dominio (estado del ciclo de vida, atributos logísticos). No es un mecanismo técnico genérico — es una fotografía de una entidad de negocio concreta. El propio Javadoc lo documenta:

> "Podría vivir en infraestructura.comportamiento.memento para mayor cohesión del patrón, pero eso crearía una dependencia dominio→infraestructura al ser usada por Envio como Originador."

Si `MementoEnvio` viviera en `infraestructura`, el `Envio` necesitaría importar desde infraestructura para crear mementos de sí mismo — violación de Clean Architecture (dominio no puede depender de infraestructura).

`SeguimientoEnvio` es una entidad de dominio con estado propio y ciclo de vida ligado al `Envio`. No es un mecanismo técnico — representa el concepto de negocio "seguimiento de un envío". En ambos casos: el flujo de dependencias va infraestructura → dominio, nunca al revés.

---

## 24. Hito 12 — State, Strategy, Template Method, Visitor

### State

#### ¿Diferencia entre State y un `switch(estado)` en cada método? ¿Qué pasa al agregar RETENIDO?

Con switch, `Envio` tendría algo así en cada uno de los 6 métodos:
```java
public void validar() {
    switch (estado) {
        case "CONFIRMADO": estado = "EN_TRANSITO"; break;
        case "EN_TRANSITO": System.out.println("Ya en tránsito"); break;
        case "CANCELADO":   System.out.println("No aplica"); break;
        // ...
    }
}
```
6 métodos × 6 estados = **36 combinaciones distribuidas en 36 ramas de switch**. Agregar `RETENIDO`:
- **Con switch**: abrir los 6 métodos, agregar un `case "RETENIDO"` en cada uno. 6 clases modificadas, 6 razones para introducir bugs.
- **Con State**: crear `EstadoRetenido` e implementar sus 6 métodos. Los 5 estados existentes no se tocan — Open/Closed principle.

---

#### ¿Quién decide la transición: el contexto o el estado concreto? ¿Por qué elegiste eso?

El **estado concreto** decide. En `EstadoConfirmado.validar()`:
```java
@Override
public void validar(Envio envio) {
    envio.cambiarEstado(new EstadoEnTransito());
}
```
`Envio` solo llama `estadoGoF.validar(this)` — no sabe hacia dónde va. La decisión de transición vive en el estado que la inicia.

Si el contexto (`Envio`) decidiera, necesitaría conocer todos los estados y las condiciones de transición — volvería a tener lógica centralizada, equivalente al switch. La ventaja de State es exactamente que esa lógica se distribuye en cada estado.

---

#### ¿Qué hace `EstadoCancelado` si llamás `entregar()`? Defendé la elección.

```java
@Override
public void entregar(Envio envio) {
    System.out.println("No se puede entregar un envio cancelado");
}
```

**No-op con mensaje**. La elección entre no-op, excepción o silencio total depende del contrato del sistema:

- **No-op + mensaje**: el sistema tolera la llamada inválida — útil cuando el llamador no puede garantizar el estado previo. El envío no cambia, el sistema sigue operando.
- **Excepción**: el sistema rechaza la llamada — el llamador debe validar el estado antes. Más estricto; útil cuando llegar aquí es un bug de programación, no un caso de uso.
- **Silencio total**: el más peligroso — ninguna evidencia de que se intentó una operación inválida.

Se eligió no-op con mensaje porque el sistema es demostrativo y los casos de prueba verifican que no haya efectos secundarios, no que se lance excepción. En producción, una excepción o un log de auditoría sería preferible.

---

#### ¿Por qué la interfaz tiene 6 métodos si la mayoría de los estados solo soporta 2-3 transiciones?

La interfaz define el **contrato completo** del ciclo de vida. Los estados implementan los métodos que tienen sentido y tratan el resto como no-ops.

Alternativa: interfaz con métodos `default` que imprimen "No aplica":
```java
interface EstadoEnvio {
    default void validar(Envio e) { System.out.println("No aplica"); }
    // ...
}
```
Las subclases solo sobrescriben las transiciones válidas — menos boilerplate. El riesgo: el no-op silencioso puede enmascarar que se olvidó implementar una transición real. Con la implementación explícita actual, cada estado declara visiblemente qué hace con cada operación — más verboso pero más trazable.

---

#### State vs Strategy: estructura idéntica, ¿cuál es la diferencia?

Ambos tienen: interfaz con método(s), clases concretas, contexto con campo de tipo interfaz. La diferencia es conceptual y de comportamiento:

| | State | Strategy |
|---|---|---|
| **Las concretas se conocen entre sí** | Sí — `EstadoConfirmado` instancia `new EstadoEnTransito()` | No — `EstrategiaPeso` no sabe de `EstrategiaDistancia` |
| **Quién cambia el campo del contexto** | El propio estado concreto llama `envio.cambiarEstado(new EstadoX())` | El cliente externo llama `envio.establecerEstrategia(nueva)` |
| **Propósito** | Modelar el ciclo de vida con transiciones bien definidas | Encapsular algoritmos intercambiables |
| **Tiempo de cambio** | Definido internamente por las transiciones | Elegido externamente por el cliente en runtime |

En `Envio` coexisten: `estadoGoF` (State) modela en qué etapa del ciclo de vida está el envío; `estrategia` (Strategy) modela cómo se calcula el costo. No se pisan porque modelan dimensiones ortogonales.

---

### Strategy

#### ¿Quién elige la estrategia y cuándo? ¿Puede cambiarse en runtime?

El **cliente externo** elige. `Envio` recibe la estrategia vía `establecerEstrategia(estrategia)` y la ejecuta cuando se llama `calcularCostoConEstrategia()`.

Sí puede cambiarse en runtime — basta con llamar `establecerEstrategia()` de nuevo antes del siguiente cálculo. Es la diferencia clave con Template Method: Template Method fija la variación en compilación (al extender la clase); Strategy la deja para runtime.

---

#### ¿Qué condicionales eliminó Strategy del código de costos?

Sin Strategy, `calcularCosto()` en `Envio` tendría:
```java
public double calcularCosto() {
    if      (tipo.equals("PESO"))      return peso * 5.0;
    else if (tipo.equals("DISTANCIA")) return ...;
    else if (tipo.equals("URGENCIA"))  return ...;
    else if (tipo.equals("VOLUMEN"))   return ...;
    else if (tipo.equals("HIBRIDA"))   return ...;
    else throw new IllegalArgumentException();
}
```
5 ramas que crecen con cada estrategia nueva. Con Strategy, `calcularCostoConEstrategia()` es:
```java
return estrategia.calcular(this);
```
Sin condicionales, sin branching. Agregar `EstrategiaDistanciaReal` no toca `Envio`.

---

#### `EstrategiaHibrida` combina otras: ¿composición? ¿Qué patrón recuerda?

```java
public class EstrategiaHibrida implements EstrategiaCalculoCosto {
    private final EstrategiaCalculoCosto distancia = new EstrategiaDistancia();
    private final EstrategiaCalculoCosto peso      = new EstrategiaPeso();
    private final EstrategiaCalculoCosto urgencia  = new EstrategiaUrgencia();

    @Override
    public double calcular(Envio envio) {
        return distancia.calcular(envio) * 0.40
             + peso.calcular(envio)      * 0.30
             + urgencia.calcular(envio)  * 0.30;
    }
}
```

Sí, es **composición de estrategias** — una estrategia que delega en otras. Recuerda al **Composite**: un nodo que agrega resultados de sus componentes. No es Decorator porque no wrappea en cadena — es suma ponderada en paralelo.

---

#### ¿Cómo testeás cada estrategia? ¿Por qué es más fácil que un método con 5 ramas?

```java
Envio e = new Envio.EnvioBuilder("t", "A", "B").peso(10.0).build();
e.establecerEstrategia(new EstrategiaPeso());
assert e.calcularCostoConEstrategia() == 50.0;  // 10.0 * 5.0
```

Cada estrategia es una clase con **una sola responsabilidad** — el test solo necesita los campos que esa estrategia usa. Con el método monolítico de 5 ramas, cada test necesita poner `Envio` en el estado correcto para llegar a la rama deseada (ej. setear `tipo = "PESO"`), las ramas comparten el mismo método y una modificación puede romper tests de otras ramas.

---

#### Relación con GRASP: ¿qué patrón del Hito 5 es la base?

**Protected Variations** (GRASP): el punto de variación (el algoritmo de cálculo de costo) está encapsulado detrás de `EstrategiaCalculoCosto`. Cuando se agrega `EstrategiaDistanciaReal`, nada del código que llama `estrategia.calcular()` cambia.

También **Polymorphism** (GRASP): el comportamiento variable se asigna a tipos polimórficos en lugar de condicionales explícitos. Ambos son los mismos principios que justifican Strategy en términos de GRASP.

---

### Template Method

#### ¿Qué define la clase abstracta y qué las subclases? ¿Cuáles pasos son fijos y cuáles hooks?

`ProcesoEnvio` define el **algoritmo esqueleto** fijo en `procesarEnvio()`:
```java
public final void procesarEnvio(Envio envio) {
    validar(envio);
    calcularCosto(envio);
    procesarPago(envio);
    notificar(envio);
}
```
Los 4 pasos y su **orden** son invariantes. Todos son `protected abstract` — no hay hooks opcionales: todos los pasos son obligatorios.

Las subclases proveen las implementaciones:

| Clase | `calcularCosto()` |
|---|---|
| `ProcesoNacional` | `peso × 100` |
| `ProcesoInternacional` | `peso × 250 + 1000` (incluye aduana) |
| `ProcesoUrgente` | `peso × 180 + 500` |

---

#### ¿Por qué `procesarEnvio()` es `final`?

El `final` garantiza que **ninguna subclase puede modificar el algoritmo esqueleto** — solo los pasos. Sin `final`, una subclase podría sobrescribir `procesarEnvio()` y romper el orden de ejecución, anulando el propósito del patrón. Con `final`, las subclases solo tienen poder sobre la implementación de cada paso, no sobre el esqueleto.

---

#### Principio de Hollywood: "no nos llames, nosotros te llamamos". ¿Cómo aplica?

`ProcesoNacional` implementa `validar()`, `calcularCosto()`, `procesarPago()`, `notificar()` — pero no los llama directamente. Es `ProcesoEnvio.procesarEnvio()` quien los invoca en secuencia.

La subclase "espera a ser llamada" por el framework (la clase abstracta). No hay `super.procesarEnvio()` en la subclase ni invocaciones directas entre pasos. La subclase entrega los ingredientes; el framework decide cuándo y en qué orden usarlos.

---

#### Template Method vs Strategy: herencia vs composición.

| | Template Method | Strategy |
|---|---|---|
| **Mecanismo** | Herencia — la variación está en métodos sobrescritos | Composición — la variación está en un objeto inyectado |
| **Tiempo de cambio** | Compilación — al extender `ProcesoEnvio`, el proceso queda fijo | Runtime — `establecerEstrategia()` puede llamarse en cualquier momento |
| **Granularidad** | Varía pasos individuales de un esqueleto fijo | Reemplaza el algoritmo completo |
| **Flexibilidad** | Menos — no se puede cambiar el proceso sin crear una subclase nueva | Más — se puede intercambiar la estrategia sin nueva clase |

En LogiSmart, ambos conviven en `Envio`: Template Method en `ProcesoEnvio` para el flujo fijo de procesamiento; Strategy en `EstrategiaCalculoCosto` para el algoritmo de costo intercambiable en runtime.

---

#### ¿Qué tiene que hacer un `ProcesoExpress` nuevo? ¿Qué NO puede cambiar?

1. Extender `ProcesoEnvio`
2. Implementar los 4 métodos abstractos: `validar()`, `calcularCosto()`, `procesarPago()`, `notificar()`

Lo que NO puede cambiar: el **orden** `validar → calcularCosto → procesarPago → notificar`. El `final` en `procesarEnvio()` lo garantiza — no puede ser sobrescrito. El proceso express puede tener validación más liviana y costo premium, pero siempre sigue el mismo esqueleto.

---

### Visitor

#### ¿Qué problema resuelve?

Permite agregar **nuevas operaciones sobre una jerarquía de clases estable sin modificar esas clases**.

Sin Visitor, agregar "calcular costo operativo" significaría agregar `calcularCostoOperativo()` a `ElementoDistribucion` y a cada clase concreta (`NodoCentroRegional`, `NodoPuntoEntrega`). Con cuatro operaciones (`VisitorCalculoOcupacion`, `VisitorCalculoCostoOperativo`, `VisitorGeneradorReporte`, `VisitorBusquedaPuntosCriticos`), eso serían 4 métodos nuevos por clase.

Con Visitor, se crea `VisitorCalculoCostoOperativo` sin tocar nada existente.

---

#### ¿Qué es double dispatch y por qué Visitor lo necesita? ¿Qué hace `aceptar(visitor)` exactamente?

En Java, el polimorfismo estándar es **single dispatch**: el método se elige según el tipo del receptor. Si tenés `ElementoDistribucion elemento` y llamás `elemento.aceptar(visitor)`, Java elige `aceptar()` según el tipo real de `elemento`.

El problema: dentro de `aceptar`, si llamás `visitor.visitar(this)` con `this` declarado como `ElementoDistribucion`, Java no puede elegir en compilación entre `visitar(NodoPuntoEntrega)` y `visitar(NodoCentroRegional)` — son overloads distintos.

**Double dispatch** encadena dos resoluciones polimórficas:
1. `elemento.aceptar(visitor)` → resuelto por el tipo real de `elemento` (ej. `NodoPuntoEntrega`)
2. `visitor.visitar(this)` → dentro de `NodoPuntoEntrega.aceptar()`, `this` es `NodoPuntoEntrega` — Java puede resolver el overload en compilación

`aceptar(visitor)` hace exactamente esto:
```java
// NodoPuntoEntrega
@Override
public void aceptar(VisitorCentro visitor) {
    visitor.visitar(this);  // 'this' = NodoPuntoEntrega → elige visitar(NodoPuntoEntrega)
}
```
```java
// NodoCentroRegional
@Override
public void aceptar(VisitorCentro visitor) {
    visitor.visitar(this);       // 'this' = NodoCentroRegional → elige visitar(NodoCentroRegional)
    for (ElementoDistribucion hijo : hijos) {
        hijo.aceptar(visitor);   // propaga recursivamente
    }
}
```

---

#### ¿Cuándo Visitor es mala idea?

Cuando la **jerarquía de elementos es inestable**. Agregar `NodoCentroUrbano` requiere agregar `visitar(NodoCentroUrbano)` a la interfaz `VisitorCentro` y a **todos** los visitors existentes — los 4 actuales se rompen si no implementan el nuevo método.

La extensibilidad se invierte respecto al enfoque sin Visitor: Visitor hace fácil agregar operaciones, pero difícil agregar tipos.

En LogiSmart, la jerarquía tiene 2 tipos de nodo (`NodoCentroRegional`, `NodoPuntoEntrega`) — estable. Los visitors son 4 y pueden crecer. Eso justifica la elección.

---

#### ¿Sobre qué estructura del Hito 8 trabajan los visitors? ¿Quién recorre: el visitor o el elemento?

El **Composite** de centros del Hito 8 — `NodoCentroRegional` tiene `List<ElementoDistribucion> hijos`, igual que el Composite de entonces. Los visitors del Hito 12 operan sobre esa misma estructura jerárquica.

El **elemento recorre**, no el visitor. `NodoCentroRegional.aceptar()` propaga recursivamente:
```java
visitor.visitar(this);
for (ElementoDistribucion hijo : hijos) {
    hijo.aceptar(visitor);  // el árbol se recorre a sí mismo
}
```
El visitor solo sabe qué hacer *en* cada nodo — no sabe cómo está organizado el árbol. `NodoPuntoEntrega` no itera (es hoja): solo llama `visitor.visitar(this)`.

---

#### Visitor vs agregar el método en cada clase: ¿qué trade-off hacés?

**Agregar en cada clase**: la operación queda dispersa entre todas las clases de la jerarquía. Cada nueva operación modifica todas las clases (viola OCP). Pero agregar un nuevo tipo de nodo es trivial — solo implementa la interfaz existente.

**Visitor**: la operación queda cohesiva en una clase. Todo el cálculo de costo operativo vive en `VisitorCalculoCostoOperativo`. Pero agregar `NodoCentroUrbano` rompe todos los visitors.

El trade-off es explícito: **la estabilidad de la jerarquía de tipos determina si Visitor es ganancia o deuda técnica**. Si los tipos cambian frecuentemente → evitar Visitor. Si las operaciones cambian frecuentemente → Visitor es la herramienta correcta.

---

### Integración

#### En `procesarEnvio()` de `SistemaLogisticaAvanzada`: identificá los 4 patrones

```java
// STATE — inicializa en estado inicial
envio.cambiarEstado(new EstadoConfirmado());

// STATE — transición interna: CONFIRMADO → EN_TRANSITO
envio.validar();

// STATE — no-op desde EstadoEnTransito (no puede entregar sin pasar por EN_REPARTO)
envio.entregar();

// STRATEGY — configura el algoritmo de costo
envio.establecerEstrategia(estrategia);
double costo = envio.calcularCostoConEstrategia();  // STRATEGY — ejecuta

// TEMPLATE METHOD — esqueleto fijo, pasos (validar/calcularCosto/procesarPago/notificar) variables
proceso.procesarEnvio(envio);

// STATE — transición final según el estado actual
envio.entregar();

// VISITOR (externo) — recorre la red sin tocar NodoCentroRegional/NodoPuntoEntrega
redDistribucion.aceptar(new VisitorCalculoOcupacion());
```

---

#### Si te dan un requerimiento nuevo, ¿cómo elegís el patrón?

| Requerimiento | Patrón |
|---|---|
| "Notificar a Dashboard, Auditoría y SMS cuando cambia el estado" | Observer |
| "Validar datos → inventario → pago → seguridad en secuencia, cortando al primer fallo" | Chain of Responsibility |
| "El costo varía según tipo de cliente (VIP, estándar, masivo)" | Strategy |
| "Poder deshacer la última operación del operador" | Command |
| "Guardar el estado completo del envío antes de cada operación crítica" | Memento |
| "El flujo de procesamiento tiene pasos fijos pero cada tipo de envío los implementa distinto" | Template Method |
| "Los componentes del pipeline no deben conocerse entre sí" | Mediator |
| "Analizar toda la red de distribución con múltiples cálculos sin tocar las clases de nodo" | Visitor |
| "Adaptar la API de un proveedor externo sin cambiar el dominio" | Adapter |

---

#### ¿Por qué State y Strategy aparecen juntos en `Envio`? ¿Se pisan? ¿Quién es dueño de `calcularCosto()`?

No se pisan porque modelan **dimensiones ortogonales**:
- `estadoGoF` (State) modela en qué etapa del ciclo de vida está el envío y qué transiciones son válidas
- `estrategia` (Strategy) modela cómo se calcula el costo cuando se solicita

`calcularCostoConEstrategia()` llama `estrategia.calcular(this)` independientemente del estado GoF. El estado no condiciona la estrategia — un envío EN_TRANSITO o CONFIRMADO puede calcular su costo con cualquier estrategia configurada.

`Envio` actúa como **contexto** para ambos patrones simultáneamente. El State gestiona el ciclo de vida; la Strategy gestiona el algoritmo de precio. Responsabilidades claramente separadas, sin superposición.

---

## 25. Hito 13 — Data Mapper, Repository, Unit of Work, Lazy Load, Arquitectura

### Data Mapper

#### ¿Qué significa que la entidad sea "pura"? ¿Qué NO sabe Envio?

`Envio` no tiene ningún método de persistencia. No hay `guardarEnBaseDatos()`, no hay `Connection`, no hay `@Column`, no hay `@Entity`. Solo tiene:
- Estado de dominio: `estado`, `origen`, `destino`, `peso`, `costo`
- Comportamiento de dominio: `cambiarEstado()`, `validar()`, `entregar()`, `crearMemento()`
- Patrones de dominio: Observer (`observadores`), Memento (`crearMemento()`), State (`estadoGoF`)

`EnvioMapperSQL` sabe todo lo de persistencia. Pero `Envio` no sabe que existe `EnvioMapperSQL`, ni que hay una tabla `envios`, ni que existe JDBC.

---

#### Data Mapper vs Active Record: ¿dónde vive la lógica de persistencia? ¿Por qué elegiste Mapper?

| | Data Mapper | Active Record |
|---|---|---|
| **Lógica de persistencia** | En el Mapper (`EnvioMapperSQL`) — separada del objeto | En la entidad misma (`envio.save()`, `envio.delete()`) |
| **Acoplamiento** | Bajo — la entidad no importa nada de JDBC | Alto — la entidad depende de la BD directamente |
| **Testeo del dominio** | Fácil — `Envio` se testea sin BD, sin mock | Difícil — cada test de la entidad necesita BD real o mock del acceso |
| **Complejidad** | Mayor — más clases y capas | Menor — todo en uno |

Se eligió Mapper porque el dominio es complejo (GoF patterns, reglas de negocio, ciclo de vida). Con Active Record, testear las transiciones de State o las notificaciones de Observer requeriría una BD activa. Con Mapper, los 148+ tests corren contra `RepositorioEnvioMemoria` — rápidos y deterministas.

---

#### ¿Cómo reconstruye EnvioMapperSQL un objeto desde una fila? ¿Por qué reutiliza EnvioBuilder?

```java
return new Envio.EnvioBuilder(
        rs.getString("id"),
        rs.getString("origen"),
        rs.getString("destino"))
    .peso(rs.getDouble("peso"))
    .estado(rs.getString("estado"))
    .costo(rs.getDouble("costo"))
    .tipo(rs.getString("tipo"))
    .build();
```

El `EnvioBuilder` es el **único punto de construcción válido** para un `Envio` con atributos logísticos completos. No se usa el constructor público de 4 args (`id, Empresa, prioridad, fechaProgramada`) porque ese requiere una `Empresa` (que no está en la fila) y no tiene los campos del Builder.

Si el Mapper construyera el objeto directamente con campos privados vía reflexión o setters masivos, violaría el encapsulamiento de `Envio`. El Builder encapsula la construcción y es el mismo mecanismo que usa cualquier otra parte del sistema — un solo punto de construcción válida.

---

#### ¿Qué pasa si cambia el esquema de la BD?

- **Solo se toca `EnvioMapperSQL`**: el mapping entre columnas y campos del objeto. Si `costo` se renombra a `costo_total` en la tabla, se cambia `rs.getString("costo")` por `rs.getString("costo_total")` — una línea en el mapper.
- **No se toca `Envio`**: la entidad de dominio no sabe cómo se llaman las columnas.
- **No se tocan los tests del dominio**: corren contra `RepositorioEnvioMemoria`, sin SQL.

Ese aislamiento entre el esquema físico de la BD y el modelo de dominio es la ventaja principal del patrón.

---

### Repository

#### ¿Diferencia entre Repository y Data Mapper si los dos "hablan con la BD"?

El **Mapper** hace una sola cosa: traducir entre objeto y fila. Su interfaz habla de persistencia: `insertar(Envio)`, `actualizar(Envio)`, `eliminar(String)`, `buscarPorId(String)`.

El **Repository** expone una **colección de dominio** — actúa como si los objetos estuvieran en una lista en memoria. Su interfaz habla el lenguaje del dominio: `buscarPorEstado("EN_TRANSITO")`, `obtenerTodos()`. El Repository usa al Mapper internamente para las operaciones individuales:

```java
// RepositorioEnvioSQL
@Override
public void guardar(Envio envio) {
    mapper.insertar(envio);  // delega en el mapper para el SQL
}
```

Repository = la colección. Mapper = el traductor. Son responsabilidades distintas: el Repository abstrae "cómo se encuentran los objetos"; el Mapper abstrae "cómo se convierte un objeto a filas y viceversa".

---

#### ¿Por qué la interfaz vive en infraestructura? ¿Qué principio aplica? ¿Cuál sería el ideal?

En el proyecto, `RepositorioEnvio` vive en `infraestructura.persistencia.repositorio` — una simplificación pragmática.

El **ideal de Clean Architecture** es que la interfaz viva en el **dominio**: el dominio define *qué necesita* de persistencia (`RepositorioEnvio`), e infraestructura provee la implementación. Eso es **Dependency Inversion** (DIP): el módulo de alto nivel (dominio) no depende del módulo de bajo nivel (SQL); ambos dependen de la abstracción (la interfaz).

El beneficio real se sigue logrando: la capa de aplicación (`ServicioEnvios`, `LogisticaFacade`) depende de la interfaz `RepositorioEnvio`, no de `RepositorioEnvioSQL`. Inyectando `RepositorioEnvioMemoria` en los tests, el código no cambia — es Protected Variations.

---

#### ¿Para qué sirve la implementación en memoria si nunca va a producción?

```java
public class RepositorioEnvioMemoria implements RepositorioEnvio {
    private final Map<String, Envio> almacen = new HashMap<>();
    // ...
}
```

Es el **doble de prueba** (test double). Los tests de `CasosDePruebaHito13` usan `RepositorioEnvioMemoria` porque:
- No requieren BD activa — los tests corren en cualquier máquina
- Son deterministas — `limpiar()` deja el almacén vacío entre tests
- Son rápidos — HashMap vs round-trip a BD
- El código bajo test no sabe la diferencia — `ServicioEnvios` recibe `RepositorioEnvio`, no sabe si es memoria o SQL

En Hito 9, el `ProxyRepositorioEnvios` también era un repositorio en memoria con cache. El patrón es el mismo; la implementación en memoria del Hito 13 es más directa.

---

#### Relación con Proxy y Facade

- **Proxy** (Hito 9): `ProxyRepositorioEnvios` es un repositorio que intercepta llamadas para agregar cache y control de acceso. En arquitectura real, el Proxy podría envolver al `RepositorioEnvioSQL` del Hito 13 — transparente para quien llama `guardar()`.
- **Facade** (Hito 13): `LogisticaFacade` agrupa los 4 servicios y el UnitOfWork. Los servicios usan los repositories internamente. La Facade simplifica "crear envío + pago" en una sola llamada, ocultando la coordinación con el UnitOfWork.

---

### Unit of Work

#### ¿Qué problema resuelve?

Sin Unit of Work, persistir "crear envío" implica múltiples operaciones separadas:
```java
repositorioEnvio.guardar(envio);   // OK
repositorioPago.guardar(pago);     // si falla aquí...
// el Envio ya está en BD, el Pago no → inconsistencia
```

Unit of Work agrupa todas las operaciones en una unidad lógica: **todo o nada**. Los objetos se registran durante el procesamiento; el SQL se ejecuta solo en `commit()`. Si algo falla, `rollback()` descarta todo.

---

#### ¿Qué registra y cuándo se ejecuta el SQL?

Registra tres categorías:
- `registrarNuevo(entidad)` → INSERT en `commit()`
- `registrarModificado(entidad)` → UPDATE en `commit()`
- `registrarEliminado(entidad)` → DELETE en `commit()`

El SQL **no se ejecuta al registrar** — se ejecuta en `commit()`:
```java
unitOfWork.registrarNuevo(envio);   // lista grows, sin SQL
unitOfWork.registrarNuevo(pago);    // lista grows, sin SQL
unitOfWork.commit();                // aquí: INSERT envio, INSERT pago (en orden)
```

Orden de `commit()`: INSERT → UPDATE → DELETE — garantiza integridad referencial (no eliminar un objeto que otro recién insertado referencia).

---

#### ¿Qué hace `rollback()`? ¿Qué pasa si falla el tercer INSERT de cinco?

```java
public void rollback() {
    int descartados = nuevos.size() + modificados.size() + eliminados.size();
    limpiar();  // vacía las tres listas, sin ejecutar nada
    System.out.println("[UnitOfWork] rollback() — " + descartados + " cambios descartados.");
}
```

Si el tercer INSERT falla, el código llama `rollback()` en el catch — las listas se vacían sin que ningún INSERT se haya ejecutado. La BD queda como si nada hubiera pasado.

En producción con JDBC: `connection.setAutoCommit(false)` antes de empezar, todos los INSERTs dentro de la misma transacción, y `connection.rollback()` en el catch. La BD deshace atómicamente los primeros dos INSERTs que sí se ejecutaron.

---

#### ¿Por qué sin UoW podrías guardar el Pago pero no el Envio? Ejemplo en LogiSmart

En `LogisticaFacade.procesarEnvioCompleto()`:
```java
pago.setEnvioId(envio.getId());  // el Cobro referencia al Envio
unitOfWork.registrarNuevo(envio);
unitOfWork.registrarNuevo(pago);
unitOfWork.commit();             // los dos, o ninguno
```

Sin UoW hipotético:
```java
repositorioEnvio.guardar(envio);    // OK — envío persistido
repositorioPago.guardar(pago);      // RuntimeException aquí
// Envio en BD, Cobro no — inconsistencia
```

`Cobro.envioId` apunta a un `Envio` que existe en la BD. Pero desde la vista del negocio, ese envío no tiene pago confirmado — el proceso quedó a medias. Unit of Work previene exactamente ese escenario.

---

#### ¿Qué framework real implementa esto?

**Hibernate/JPA**: la `Session` (Hibernate) o `EntityManager` (JPA) implementa Unit of Work. Mantiene el "persistence context" — un registro de todas las entidades cargadas y sus estados (`managed`, `detached`, `removed`). El SQL se ejecuta en el `flush()` (automático antes de cada query) o en el `commit()` de la transacción. El desarrollador solo llama `session.persist(envio)` — Hibernate trackea los cambios automáticamente y genera el SQL en commit.

---

### Lazy Load

#### ¿Qué patrón estructural del Hito 9 reaparece acá?

**Proxy virtual** — misma estructura que `ProxyRepositorioEnvios` del Hito 9. El `ClienteLazyProxy` intercepta el acceso al `ClienteFinal` y decide cuándo realmente cargarlo.

Diferencia de contexto:
- Hito 9: el Proxy añadía **cache y control de acceso** a un repositorio ya existente
- Hito 13: el Lazy Load Proxy aplaza la **carga inicial** del objeto hasta que sea necesario

Mismo mecanismo — campo nulo que se inicializa al primer acceso — aplicado a un propósito diferente.

---

#### ¿Cuándo se dispara la consulta? ¿Qué pasa en el segundo `getCliente()`?

```java
public ClienteFinal getCliente() {
    if (clienteCargado == null) {
        // Primera llamada: va al repositorio
        clienteCargado = repositorio.obtener(clienteId);
    }
    return clienteCargado;  // Segunda llamada: retorna la instancia ya cargada
}
```

Primera llamada: `clienteCargado == null` → va al repositorio, carga el objeto, lo guarda en `clienteCargado`.

Segunda llamada y siguientes: `clienteCargado != null` → retorna directamente. Sin ir al repositorio. Es un cache de una sola entrada.

`estaCargado()` permite verificar en tests que la carga no se disparó antes de tiempo — útil para confirmar que el patrón lazy funciona como se espera.

---

#### ¿Qué problema clásico genera Lazy Load mal usado?

**N+1 queries**: con N envíos, cada uno con un `ClienteLazyProxy`:

```java
List<Envio> envios = repositorioEnvio.obtenerTodos();  // 1 query
for (Envio e : envios) {
    e.getClienteProxy().getCliente().getNombre();       // N queries (1 por envío)
}
```

Total: 1 + N queries. Con 1.000 envíos → 1.001 queries a la BD. En producción, esto degrada el rendimiento severamente.

¿Cómo detectarlo? Log de queries SQL (Hibernate tiene `show_sql=true`, o `hibernate.generate_statistics`). Si ves el mismo `SELECT * FROM clientes WHERE id=?` repetido N veces con IDs distintos, es N+1.

---

#### ¿Cuándo conviene carga ansiosa (eager) en vez de lazy?

**Eager** conviene cuando:
- Siempre accedés los datos relacionados — si en cada caso de uso que carga el `Envio` también necesitás el `ClienteFinal`, cargarlos juntos en un JOIN es más eficiente que N+1.
- La cantidad de objetos relacionados es pequeña y acotada.

**Lazy** conviene cuando:
- Los datos relacionados son grandes o no siempre se usan: `HistorialEnviosLazyProxy` — el historial solo se necesita en la vista de detalle de un envío, no en el listado general de 500 envíos del día.
- Muchos objetos y cargar todo de golpe sería costoso en memoria y tiempo.

En LogiSmart: `HistorialEnviosLazyProxy` y `CentroDistribucionLazyProxy` tienen sentido lazy; el nombre y estado del cliente en el listado podría justificar eager si siempre se muestra.

---

### Arquitectura completa

#### Recorré las 5 capas con "crear envío" — ¿qué hace cada capa y qué tiene prohibido?

**Presentación** (no implementada en el TPO): recibe la request, valida formato de entrada. Prohibido: lógica de negocio, SQL directo, instanciar entidades de dominio.

**Aplicación** (`LogisticaFacade`, `ServicioEnvios`): orquesta el caso de uso — instancia el `Envio`, lo registra en el `UnitOfWork`, delega al repositorio para persistir. Prohibido: reglas de negocio (eso es del dominio), SQL directo (eso es de infraestructura).

**Dominio** (`dominio/envio/Envio`): ejecuta las reglas de negocio — `cambiarEstado("EN_CURSO")` dispara Observer, aplica Strategy de costo, valida transición de State. Prohibido: cualquier import de `infraestructura.*` o `persistencia.*`. `Envio` no sabe que existe `HashMap`, `Connection`, ni `EnvioMapperSQL`.

**Infraestructura** (`infraestructura/persistencia/repositorio/`): implementa repositorios y mappers. `RepositorioEnvioSQL` delega en `EnvioMapperSQL` para el SQL real. Prohibido: contener lógica de negocio — si `EnvioMapperSQL` calculara el costo, violaría la separación.

**Persistencia** (BD real, no ejecutada en el TPO): recibe el SQL de los mappers. Prohibido: reglas de negocio en triggers o stored procedures — eso rompería la trazabilidad del comportamiento del sistema.

---

#### ¿Por qué las dependencias apuntan hacia el dominio? ¿Qué capa no depende de ninguna?

La **regla de dependencia** de Clean Architecture: el código fuente solo puede apuntar hacia el dominio. Infraestructura importa clases de dominio. Aplicación importa clases de dominio. El dominio no importa nada de las otras capas.

Razón: el dominio es la parte más valiosa y más estable del sistema. Si `Envio` dependiera de `EnvioMapperSQL`, un cambio de JDBC a MongoDB obligaría a modificar las reglas de negocio — que son independientes del mecanismo de persistencia.

La capa que **no depende de ninguna** es el **dominio**. `Envio.java` no tiene ningún import de `infraestructura.*`, `persistencia.*`, ni `aplicacion.*`.

---

#### ¿Dónde quedaron los patrones de los hitos anteriores en esta arquitectura?

| Patrón | Ubicación en la arquitectura |
|---|---|
| **Singleton** | Infraestructura — `ConexionBD` (un punto de conexión compartido) |
| **Builder** | Dominio — `EnvioBuilder`, reutilizado por `EnvioMapperSQL` para reconstruir desde ResultSet |
| **Abstract Factory** | Infraestructura — `LogiSmartFactoryArgentina`/`Brasil` para variación por región |
| **Prototype** | Dominio — `Envio.clone()` para copiar envíos |
| **Adapter / Bridge** | Infraestructura — adaptadores de servicios externos y reportes |
| **Composite + Visitor** | Infraestructura — red de centros de distribución |
| **Decorator** | Infraestructura — `DecoradorSeguro`, `DecoradorRastreoGPS` sobre el costo |
| **Facade** | Aplicación — `LogisticaFacade` (Hito 13), `ServicioLogisticaFacade` (Hito 9) |
| **Proxy** | Infraestructura — `ProxyRepositorioEnvios` (Hito 9), `ClienteLazyProxy` (Hito 13) |
| **Chain / Command / Interpreter** | Infraestructura — validadores, cola de operaciones, reglas de filtrado |
| **Observer / Memento** | Dominio — en `Envio` directamente (ciclo de vida es dominio puro) |
| **State / Strategy** | Dominio — en `Envio` (ciclo de vida y cálculo de costo son dominio puro) |
| **Template Method** | Infraestructura — `ProcesoEnvio` y sus subclases |
| **Mediator / Iterator** | Infraestructura — pipeline event-driven y colecciones personalizadas |

---

#### La pregunta integradora: de los 36 patrones del TPO, ¿cuáles 3 fueron los más valiosos?

Respuesta defendida con justificación de diseño:

**1. Repository** — el más transversal. Abstraer la persistencia detrás de `RepositorioEnvio` permitió que 148+ tests corran sin BD real. Sin Repository, cada test del dominio requeriría setup de BD o mocks complejos. La interfaz permite intercambiar memoria/SQL/cualquier backend sin tocar el dominio ni la aplicación. Es la base que hace testeable toda la arquitectura.

**2. Observer** — el más generativo. `Envio` notifica cambios a Dashboard, Auditoría, Notificaciones y Centro sin saber que existen. Cada hito que necesitaba reaccionar a cambios de estado (Hito 11, 13) solo agregó un observer — sin modificar `Envio`. Demostró que el punto de variación correcto estaba en el dominio: el sujeto es la entidad central.

**3. State** — el más correctivo. Sin State, `Envio` tendría 36 combinaciones distribuidas en 6 métodos con switches. Con State, las transiciones inválidas son explícitas en cada `EstadoXxx` — imposible entregar un envío CANCELADO sin ver el no-op documentado. El ciclo de vida del envío es la regla de negocio más crítica de LogiSmart y State la hace visible y testeable.

---

*Última actualización: junio 2026*
