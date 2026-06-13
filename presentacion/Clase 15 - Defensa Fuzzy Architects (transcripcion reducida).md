# Clase 15 – Presentación TPO I

## Defensa del equipo **Fuzzy Architects** (transcripción reducida)

**Reunión:** Clase 15 – Presentación TPO I — 9 de junio de 2026
**Tramo:** 1:43:10 – 2:21:44
**Equipo:** Fuzzy Architects
**Presentan:** María del Rosario Presedo y Simón Ottati
**Docente:** Joaquín Salas
**Proyecto:** LogiSmart — SaaS de optimización logística para PyMEs

> Versión editada de la transcripción original. Se conservó únicamente la presentación y defensa de Fuzzy Architects; se limpiaron muletillas y errores de transcripción automática para facilitar la lectura, manteniendo el contenido.

---

## 1. Presentación

### Rosario — Contexto de negocio y metodología (1:43:10)

Nosotros somos Rosario y Simón, del equipo **Fuzzy Architects**. Lo que les vamos a mostrar hoy es cómo trabajamos este proyecto a partir de la problemática que nos trajo un cliente. Nuestra metodología fue **primero el negocio, después la arquitectura**: siempre nos basamos en los requerimientos que venían del cliente y de su problemática, y a partir de esos requerimientos implementamos nuestra arquitectura y nuestros patrones de diseño para satisfacer esas necesidades.

Respecto al cliente y su problemática: hoy son una empresa pequeña/mediana que tiene todo fragmentado. Gestionan las rutas por un lado en una planilla, el operador logístico se lo comunica a los transportistas por WhatsApp, no hay un seguimiento claro y los clientes consultan por llamada preguntando dónde está su envío. La necesidad es **centralizar toda esa operación en un único sistema** y usarlo como ventaja competitiva.

LogiSmart permite centralizar la operación: importo los pedidos al sistema, asigno rutas de manera automatizada, eso se comunica a los transportistas, que van actualizando el seguimiento en vivo, y los clientes que quieran tracking en tiempo real pueden ver dónde está su pedido (centro de distribución, sucursal, reparto) y cuánto tiempo falta.

**Casos de uso** (los más representativos para un primer MVP): importar los pedidos (integraciones externas con Tienda Nube, Mercado Shops), planificar rutas óptimas y seguir el envío en vivo.

**Actores internos:** operador logístico, administrador de la PyME, administrador de la plataforma (da de alta a las PyMEs y maneja suscripciones), transportista y cliente final.
**Sistemas externos:** carriers (DHL, FedEx, UPS), pasarelas de pago (PayPal, Stripe) y, a futuro, notificaciones, mapas y geolocalización.

**Atributos de calidad** (de mayor a menor criticidad):

1. **Alta disponibilidad** — si el sistema se cae, se frena la operatoria y el negocio pierde dinero, credibilidad y reputación. Objetivo: **99,9% de uptime**, nuestro atributo prioritario a proteger.
2. **Aislamiento de datos** — es un sistema multi-tenant. La analogía es un gran edificio donde cada departamento es una PyME y ninguna debería ver a las otras.
3. **Interoperabilidad** — integración fluida y transparente con sistemas externos.
4. **Elasticidad** — poder adaptarse en el tiempo (p. ej. hoy opero en Argentina y Brasil, mañana en Chile con sus reglas) agregando "un anexo" sin tocar mucho el sistema; eso lo hace sostenible.
5. **Eficiencia en la respuesta** — mostrar al usuario dónde está su envío en tiempo real.

Sostener estos atributos implica trade-offs: para ganar flexibilidad perdemos algo de simplicidad, pero eso mismo es lo que da elasticidad en el tiempo.

**Arquitectura por capas** (siguiendo el flujo de una petición):

- **Presentación** — preserva el aislamiento de datos.
- **Aplicación** — vive la alta disponibilidad; están los subsistemas que satisfacen los casos de uso.
- **Dominio** — el corazón, donde vive la lógica de negocio y la clase destacada **Envío**.
- **Persistencia** — abstrae al dominio de la implementación de base de datos.
- **Infraestructura** — transversal a todas las capas, les ofrece servicios.

Para cerrar el contexto: cada actor tiene sus privilegios dentro de la plataforma (p. ej. los reportes los ve un administrador de la empresa, no el cliente final). El ciclo de vida del **Envío** lo maneja **State** (confirmado, en tránsito, en reparto, entregado, con estados intermedios de cancelado o retenido). Hay tipos de proceso (nacional, internacional, urgente), estrategias de costos y servicios adicionales: ahí es donde se abren las reglas de negocio. Con **Strategy** puedo calcular el costo de un envío de una u otra manera y ampliarlo en el tiempo. El manejo de flota (3 vehículos, 2 regiones: Argentina y Brasil; auto por defecto en Argentina, moto en Brasil) se separa con **Abstract Factory**.

### Rosario — Capa de Presentación y Aplicación

En la **capa de presentación**, lo más destacable es el **aislamiento de datos**. En el **Controller** (patrón Controller) tenemos el primer punto de entrada, donde se validan los permisos del usuario que solicita ejecutar una acción. Manejamos los permisos con **polimorfismo**: un **enum** que es una matriz de permisos por tipo de rol (operador logístico, transportista, etc.), con cinco tipos de permiso (ver reportes, crear un envío, etc.) en `true`/`false`. Cuando un usuario implementa esa interfaz, simplemente tiene asignado su rol y se le pregunta "¿puedo crear un envío?", que responde `true`/`false`; el usuario queda abstraído de esa lógica.

En la **capa de aplicación** tenemos, por ejemplo, el patrón **Facade** (una fachada para proveedores externos que se comunica con adaptadores y APIs, abstrayendo al controlador) y **Chain of Responsibility**, implementado como una cadena de validadores: primero validamos los datos del envío, luego el inventario, luego el método de pago, luego seguridad (zona restringida) y por último la capacidad del centro de distribución / vehículo. Lo elegimos porque permite **fallar rápido**: si una validación falla, las otras no se ejecutan, se rechaza la creación y se pide la corrección que corresponda.

Ahora dejo a mi compañero Simón con la capa de dominio.

### Simón — Capa de Dominio (1:53:22)

Gracias, Rosario. Retomo desde la **capa de dominio**. Para nosotros la entidad **Envío** es el corazón del negocio: representa toda la vida del sistema logístico. Un envío se construye, se valida, cambia de estado, calcula costo, notifica cambios, guarda su historial y se persiste. Por eso alrededor de Envío aparecen varios patrones:

- **Builder** — construye el envío paso a paso, evitando un constructor enorme.
- **Prototype** — permite clonar envíos similares.
- **State** — modela los estados y su evolución.
- **Memento** — guarda snapshots (instantáneas) del estado en un momento dado para historial, restauración o analíticas.
- **Observer** — muy ligado a State; notifica a otros componentes sobre los cambios de estado.

No son patrones elegidos al azar: todos giran alrededor de la idea de que el envío es primordial y su ciclo de vida hace a todo el sistema.

Una de las promesas iniciales del proyecto era que el **cliente final pudiera ver el envío en tiempo real**. Para lograrlo nos apoyamos en **State, Observer y Memento**:

- **State** permite tener los estados del envío de forma desacoplada. Trade-off: hay más clases que con un `switch` gigante, pero si querés agregar un estado nuevo (p. ej. "en buque viniendo de China") creás una clase, la referenciás desde algún estado existente y la ruteás hacia los siguientes, sin tocar los estados ya existentes. Cada estado conoce su lógica y a qué paso siguiente puede ir.
- **Observer** — ante cada cambio de estado del envío, emite un evento para que otras entidades (que el envío no conoce) reaccionen: notificar al cliente, actualizar un dashboard, etc. Podés tener N observers sin tocar la implementación de Envío.
- **Memento** — guarda un snapshot inmutable del envío en un momento dado, conservando solo los campos que sirven (estado, origen, destino, costo, timestamp) sin exponer los campos privados de la clase. Sirve para análisis históricos y analíticas (p. ej. tiempos promedio de cambio de estado).

En síntesis: **State controla la evolución, Observer la comunica y Memento permite auditar o reconstruirla.**

### Simón — Capa de Persistencia

El objetivo es que las consultas sean eficientes y consistentes, **sin que el dominio (Java) toque nada de SQL**.

- **Repository + Data Mapper** — el Repository le da a la aplicación una forma simple y uniforme de trabajar con objetos (guardar, buscar, traer) sin saber si por detrás hay SQL, memoria, relacional o no relacional. El Data Mapper traduce filas/registros de la base en objetos del dominio; si cambia el esquema físico, cambia el mapper, no el dominio.
- **Proxy + Lazy Load** — referidos a eficiencia. **Proxy** agrega una capa de cacheo (p. ej. con un TTL de 1 hora) para bajar la cantidad de transacciones contra la base: si el dato está en caché lo devuelve, si no consulta la base. **Lazy Load** trae solo los datos necesarios (p. ej. en "/envíos" traés únicamente los envíos, paginados/por lotes si son muchos) para no sobrecargar la query.
- **Unit of Work** — toma transacciones que deben ser atómicas y las considera como una sola. Garantiza que todos los subprocesos se realicen de forma exitosa (p. ej. crear un envío implica registrar un cobro: no podés terminar uno sin el otro). Piensa la operación como un todo.

### Simón — Capa de Infraestructura

Es transversal a todo el sistema; aparecen patrones para hablar con sistemas externos y para escalar.

- **Adapter** — permite usar APIs externas incompatibles o con contratos propios (DHL, FedEx, UPS; PayPal, Stripe). Con una interfaz común podés agregar, p. ej., Correo Argentino o Mercado Pago sin tener que adaptar el código a cada API distinta.
- **Abstract Factory** — da coherencia por región. Cada región (Argentina, Brasil) tiene reglas distintas; una factory de Brasil asegura que se use el vehículo, la calculadora y el mapa de Brasil, sin mezclar (p. ej. un auto argentino con un mapa brasilero). Crea una familia compatible de clases manteniendo el acoplamiento entre regiones.
- **Bridge** — usado para reportes y formatos. Evita la explosión combinatoria de clases: separás el tipo de reporte (envíos, ingresos, desempeño) del formato de salida (PDF, Excel, JSON, CSV). Al agregar un formato no tenés que rehacerlo para cada reporte; la cantidad de clases baja notablemente.
- **Flyweight** — comparte ubicaciones repetidas. Si hay 10.000 envíos a 3 ciudades, en vez de 10.000 objetos de ubicación usamos 3 instancias compartidas, reduciendo memoria.

### Recorrido de una petición por las 5 capas

1. **Presentación** — el Controller recibe la operación; el método `puedoCrear` autoriza según la matriz de permisos, antes de delegar (todavía sin lógica de negocio).
2. **Aplicación** — Facade orquesta los subsistemas; Chain valida en orden y hace early return si algo falla.
3. **Infraestructura** — el envío se genera con la Factory regional correspondiente; Adapter usa el protocolo del carrier que toque.
4. **Dominio** — el envío cambia de estado en el tiempo; Observer avisa/broadcastea el evento a las partes del sistema que lo necesiten.
5. **Persistencia** — Proxy/Repository persisten el dato sin meter SQL ni el motor de base en el código del dominio.

Para sintetizar: los patrones no están elegidos al azar ni aislados; **todos colaboran entre sí en una petición real** — autorizan, orquestan subsistemas, usan integraciones externas, aplican reglas de negocio y guardan el dato.

### Rosario — Cierre con caso de uso (2:11:18)

Para cerrar, destacamos un caso de uso relevante: la **asignación de ruta**. Por el Controller entra la petición y se delega. Eso deriva en la elección de un vehículo (por capacidad o disponibilidad) y de una ruta (menos costosa, menos tránsito, más rápida) vía **Strategy**, que se puede escalar/ampliar. Una vez asignada la ruta, cambiamos el estado del envío a "en tránsito" hacia los centros de distribución; eso dispara, vía **Observer**, las notificaciones a los suscriptores sin que el envío se entere: se actualizan dashboards con métricas y reportes para el administrador, se envían SMS a los clientes suscritos al tracking en tiempo real y se registra todo en auditoría para trazabilidad. Si todo sale bien, se persiste con **Unit of Work**; si algo falla, se hace rollback, apoyándonos en **Memento** para volver a un estado válido.

Mil gracias por escucharnos. Como dijimos: primero el negocio, después la arquitectura. Estamos abiertos a preguntas.

---

## 2. Defensa (preguntas y respuestas)

### Docente (Joaquín Salas) — 2:13:23

> Muy bien estructurada la presentación, muy bien contada. Se nota la diferencia cuando alguien labura en serio en el proyecto y lo puede contar de manera fluida, a cuando alguien lee un texto. Los felicito.
>
> **Me queda una duda en persistencia: ¿por qué usar un Proxy en lugar de Decorator?**

**Rosario:** Porque sirven dos propósitos diferentes. **Decorator** es como capas de cebolla: tengo un envío básico con características base y le voy agregando cosas; por ejemplo, si el cliente quiere pagar un seguro, se le agrega esa decoración y cambia el costo, pero solo si el cliente o el negocio lo requieren. **Proxy** sirve para la eficiencia del sistema: manejar las consultas y no ir a la base de datos innecesariamente si ya tengo esa información en caché. Sirven dos propósitos distintos.

**Docente:** Uno controla acceso y el otro agrega funcionalidad. Excelente respuesta, era capciosa.

### Matías Barité — 2:14:58

> **¿Por qué usaron Chain of Responsibility para las validaciones en vez de poner todas las validaciones en un solo método?**

**Rosario:** Chain nos permite tener distintos validadores por separado, encapsular cada validación; si alguna falla, la siguiente no se llama. Cada validación tiene su propio sistema y sabe qué validar a partir del contexto que recibe. Separa bien los pasos y, si una falla, no se ejecuta el resto.

**Simón:** Además, al tener las validaciones en sus propias clases, si alguna circunstancia cambia el proceso o el orden, solo cambiás el orden de la validación, o agregás un nuevo paso, sin tocar los demás. Sin Chain terminarías con una lógica condicional metida en el código que se vuelve un monstruo; así queda todo desacoplado.

**Rosario:** Y protege el principio **Open/Closed**, haciendo más fácil de mantener el sistema.

**Matías:** Y no hace validaciones de más. Excelente, gracias.

### Mateo Díaz Valdez — 2:17:05

> **¿Usaron Singleton? Si no, ¿por qué? Y pensando en que la arquitectura es multi-tenant, ¿cuál es el problema que suele introducir ese patrón? ¿Lo tuvieron en cuenta?**

**Simón:** Sí, Singleton está implementado para la **conexión con la base de datos**: una única instancia accesible desde todo el sistema. Respecto al problema multi-tenant —que los datos de distintos tenants estén en una misma base y se crucen— eso es más un problema de modelado de base de datos: si modelás el esquema de modo que no lo permitís, validás todo por un **ID de tenant**, o usás (p. ej. en Postgres) **row level security** en el motor, no dejás que salgan datos cruzados.

**Mateo:** En realidad preguntaba qué estrategia usaron para el problema de Singleton con **procesos corriendo en hilos diferentes**, donde puede tener complicaciones.

**Rosario:** Con el **tenant ID** se mitiga ese problema de trazabilidad.

**Mateo:** Perfecto.

### Mateo Lewinzon — 2:19:42

> **¿Consideran que todo lo que hicieron es testeable y por qué?**

**Rosario:** Es testeable, primero, porque lo hicimos, y además porque mantuvimos **bajo acoplamiento y alta cohesión**. En el dominio, las clases se abstraen de la infraestructura y de la aplicación; el dominio es lo que principalmente querés testear. Al estar abstraído de las otras capas y tener cada una su propia responsabilidad, podés testearlas de manera granular/atómica. Igual es difícil mantener la testeabilidad cuando el sistema crece, pero esa fue la intención.

**Docente:** ¿Cuál es la profundidad de la pregunta, Mateo?

**Mateo Lewinzon:** Que se puede justificar de muchas formas: también podés hablar de cómo separás las interfaces de su implementación con algún patrón, por ejemplo Repository, o con Bridge y Adapter.

**Simón:** Sí, por ejemplo, para Envío tenemos el repositorio implementado.

**Docente (2:21:36):** La formulación también es clave. Excelente, gente, los felicito. Muchas gracias.

**Rosario / Simón:** Gracias, profe.

---

*Fin de la defensa de Fuzzy Architects.*
