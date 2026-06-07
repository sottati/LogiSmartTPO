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

*Última actualización: junio 2026*
