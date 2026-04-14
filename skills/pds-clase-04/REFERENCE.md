# Clase 4: GRASP Parte I

## Qué es GRASP

GRASP significa General Responsibility Assignment Software Patterns.

Su pregunta central es:

- ¿A qué clase le asigno esta responsabilidad?

El valor de GRASP está en mejorar la asignación de responsabilidades para lograr:

- Alta cohesión
- Bajo acoplamiento
- Mejor mantenibilidad
- Mejor extensibilidad

## Los 4 patrones vistos en la clase

1. Expert
2. Creator
3. Low Coupling
4. High Cohesion

## 1. Expert

Asignar la responsabilidad a la clase que tiene la información necesaria para cumplirla.

Idea clave:

- La clase experta conoce los datos necesarios
- La responsabilidad queda donde naturalmente pertenece

Ejemplos:

- `Factura` calcula el total porque conoce sus `Linea`
- `Ruta` puede calcular su distancia total porque conoce sus puntos
- `Ubicacion` puede calcular distancias entre ubicaciones

Beneficios:

- Encapsulación
- Bajo acoplamiento
- Alta cohesión
- Mantenibilidad

## 2. Creator

Asignar la responsabilidad de crear instancias a la clase que tiene relación especial con ellas.

Reglas principales:

1. Contención: si B contiene A, B crea A.
2. Agregación: si B agrega A, B crea A.
3. Datos: si B tiene los datos para inicializar A, B crea A.
4. Uso frecuente: si B usa A frecuentemente, B crea A.

Ejemplos en LogiSmart:

- `Ruta` crea `PuntoDeEntrega` porque lo contiene
- `FormularioDeEnvio` puede crear `Envio` porque ya tiene los datos necesarios

Beneficios:

- Consistencia
- Responsabilidad clara
- Validación previa a la creación
- Menos acoplamiento

## 3. Low Coupling

Minimizar dependencias entre clases.

La técnica más importante es la inyección de dependencias:

- Pasar objetos como parámetros
- No crearlos adentro de la clase si se puede evitar

Ejemplo:

- En vez de que `Ruta` cree su propio `Vehiculo`, recibirlo por constructor

Beneficios:

- Flexibilidad
- Testabilidad
- Reutilización
- Cambios con menor efecto dominó

## 4. High Cohesion

Mantener juntas las responsabilidades relacionadas y separar las que no lo están.

Una clase con baja cohesión suele mezclar:

- Lógica de dominio
- Persistencia
- Notificaciones
- Logging

La solución es dividir responsabilidades en clases distintas:

- `Envio` para reglas de negocio
- `EnvioRepository` para persistencia
- `NotificadorDeEnvios` para notificaciones

Beneficios:

- Claridad
- Mantenibilidad
- Testabilidad
- Reutilización

## Diagramas de interacción

La clase introduce diagramas que muestran cómo colaboran los objetos en el tiempo.

Tipos vistos:

- Diagrama de secuencia
- Diagrama de colaboración

Para leer un diagrama de secuencia, observar:

1. Actores u objetos
2. Líneas de vida
3. Mensajes entre objetos
4. Retornos
5. Activaciones

Uso práctico:

- Validar flujos de casos de uso importantes
- Revisar si las responsabilidades están bien distribuidas
- Detectar acoplamiento excesivo

## Aplicación en LogiSmart

La clase usa un ejemplo de `Ruta` con `Vehiculo`, `PuntoDeEntrega` y `Ubicacion` para mostrar cómo aplicar GRASP.

La idea general es:

- Asignar responsabilidad al experto
- Dejar que el creador cree lo que contiene o usa
- Reducir acoplamiento con dependencias inyectadas
- Separar responsabilidades no relacionadas

## Hito 4

El hito pide revisar el modelo y aplicar GRASP Parte I en el diseño de LogiSmart:

- Revisar clases y relaciones
- Aplicar Expert y Creator
- Mejorar bajo acoplamiento y alta cohesión
- Producir diagramas de secuencia para casos de uso clave

---

## Perspectiva del profesor

### Insights clave

El profesor abrió la clase con una idea que atraviesa toda la materia: la clave para programar bien no es escribir líneas de código sin entender qué estás haciendo. GRASP no es un conjunto de reglas a memorizar, sino un conjunto de estrategias para tomar mejores decisiones de diseño antes y durante la construcción.

> "La clave para programar bien no es saber tu línea de código sin entender qué estoy haciendo. Y entender qué estoy haciendo es fundamental."

GRASP ayuda a responder la pregunta más difícil del diseño orientado a objetos: ¿a qué clase le asigno esta responsabilidad? Sin este marco, se termina creando clases al azar, generando dependencias que luego son muy costosas de modificar.

Un sistema crece. Con el tiempo, lo que hoy tiene 10 clases pasa a tener 67, 120, más. Cuanto más grande el sistema, más se siente el impacto de una mala asignación de responsabilidades. Lo que hoy parece un detalle menor, mañana puede significar dos días de trabajo para hacer un cambio que debería tomar dos horas.

> "Cuanto más grande mi sistema, más dependencia tiene la clase de uso y más problemática."

Sobre los diagramas de interacción: no tienen que ser perfectos. Pueden ser dibujados a mano, en una herramienta o en cualquier formato que comunique claramente qué hace cada clase, qué acciones toma, de qué depende y cuál es el flujo cuando el usuario interactúa con el sistema. El objetivo es comunicar, no decorar.

Una advertencia importante sobre el uso de IA (ChatGPT, Claude Code, etc.) en diseño: las herramientas de IA generan código rápido, pero si no entendés los patrones de diseño, no podés identificar cuándo ese código está mal diseñado. Correcciones por prueba y error sobre código generado por IA sin criterio de diseño pueden costar más tiempo que hacerlo desde cero correctamente.

> "Lo vas a encontrar muchas cosas rápido, pero pierde muchas cosas. Esto más que a fuerza de prueba y error no hay mucha opción que fortalecer mis estrategias de diseño."

### Analogías y ejemplos reales

**Sistema de rutas de entrega**: una empresa de logística necesita calcular rutas óptimas desde un depósito a múltiples puntos de entrega. Dos clases candidatas: `Ubicacion` y `Ruta`. ¿A cuál le asignás la responsabilidad de calcular distancias? `Ubicacion` tiene los datos de coordenadas, `Ruta` conoce la secuencia de puntos. Aplicando Expert, la responsabilidad de calcular la distancia entre dos puntos va a `Ubicacion` porque tiene los datos necesarios. Si se la das a `Ruta`, creás una dependencia innecesaria: `Ruta` tiene que pedirle los datos a `Ubicacion` cada vez.

**La biblioteca automatizada**: una biblioteca de 4.500 m² con 450 empleados y 4.000 m² de estanterías quiere automatizar la búsqueda y préstamo de libros a través de 15 terminales. ¿Qué clase crea el `Prestamo`? `Biblioteca` tiene los libros. `Usuario` inicia la solicitud. `Terminal` recibe la interacción. Aplicando Creator: `Biblioteca` crea el `Prestamo` porque contiene los libros, que son el activo central del negocio. El profesor fue explícito: el diseño tiene que fluir desde el core del negocio, no desde cómo llega el pedido.

> "¿Cuál es el core del negocio? La biblioteca quiere saber sus libros. El asset más importante que tiene la biblioteca son sus libros. No lo suelto."

**No existe una estrategia ideal universal**: dos empresas del mismo rubro pueden necesitar diseños diferentes según cómo operan internamente. Lo que funciona para una empresa de logística puede no funcionar para otra del mismo sector porque el contexto de negocio es distinto. Cada equipo de desarrollo va a pensar diferente ante el mismo problema, y eso es válido, siempre y cuando la decisión esté justificada en el dominio.

### Consejo profesional

- Antes de asignar cualquier responsabilidad, recuperá el contexto: ¿qué estás construyendo y para qué? No solo la actividad técnica, sino el objetivo de negocio.
- Pasá solo los datos que necesitás entre clases, no el objeto completo. Pasar la clase entera crea acoplamiento innecesario con todo lo que esa clase contiene.
- Documentá las decisiones de diseño ahora. En 6 meses, cuando el sistema tenga 120 relaciones, nadie va a recordar por qué se tomó una decisión. La documentación clara es la diferencia entre un sistema mantenible y uno que nadie quiere tocar.
- Los diagramas de interacción no son burocracia. Son la forma más rápida de comunicar diseño a tu equipo y de detectar si una asignación de responsabilidades tiene sentido antes de escribir código.
- Usá herramientas de IA como asistentes, no como diseñadores. El código que generan puede ser incorrecto en términos de diseño aunque funcione. Necesitás criterio para revisarlo.
- Pensá en escala desde el inicio. Diseñá pensando que el sistema va a crecer. Una decisión de diseño que hoy parece irrelevante puede bloquear la evolución del sistema en 6 meses.