# Clase 1: De la Idea a la Arquitectura

## El verdadero desafío del software

El mayor desafío no es escribir código difícil. Es decidir qué construir y cómo estructurarlo. El código es una herramienta, como un martillo o un destornillador. El valor real está en la decisión arquitectónica que precede a escribir la primera línea.

## Fuzzy Front-End

El Fuzzy Front-End es la etapa inicial de un proyecto donde todo es incierto: el problema no está claro, los requisitos son ambiguos y las decisiones son costosas de revertir. La mayoría de los proyectos fracasan aquí, antes de escribir una sola línea de código.

El objetivo de la materia es aplicar rigor de ingeniería a esta etapa difusa, usando el Proceso Unificado (RUP) como marco metodológico.

## Proceso Unificado (RUP)

RUP organiza el desarrollo en torno a tres pilares:

1. **Iterativo e incremental**: el proyecto se divide en ciclos cortos, cada uno con análisis, diseño, implementación y pruebas. Cada iteración entrega un incremento funcional y verificable.
2. **Casos de uso**: el foco está en los objetivos del usuario, no en las funciones del sistema. Los casos de uso son el hilo conductor de todo el proceso.
3. **Arquitectura céntrica**: las decisiones de calidad (rendimiento, seguridad, escalabilidad, mantenibilidad) guían la arquitectura antes que los detalles de implementación.

## Casos de Uso

Un caso de uso describe una interacción entre un actor y el sistema para lograr un objetivo concreto.

Estructura de un caso de uso:

- Actor principal
- Precondiciones
- Flujo básico (pasos del escenario principal)
- Flujos alternativos (excepciones, variantes)
- Postcondiciones

Ejemplo: reserva en restaurante. El actor es el cliente. El flujo básico describe cómo realiza la reserva exitosamente. Los flujos alternativos cubren mesa no disponible, datos inválidos, etc.

## Atributos de Calidad

Los atributos de calidad son requisitos no funcionales que determinan la arquitectura:

- **Rendimiento**: tiempo de respuesta bajo carga
- **Seguridad**: protección contra accesos no autorizados
- **Confiabilidad**: disponibilidad y tolerancia a fallos
- **Escalabilidad**: capacidad de crecer sin rediseñar
- **Mantenibilidad**: facilidad de modificar y extender el sistema
- **Usabilidad**: facilidad de uso para el usuario final

Cada atributo tiene impacto directo en decisiones de diseño. No existe una arquitectura perfecta; existe la arquitectura correcta para las prioridades específicas de cada negocio.

## Trade-offs de Arquitectura

Las decisiones arquitectónicas implican compromisos inevitables:

- Seguridad vs. rendimiento
- Escalabilidad vs. simplicidad
- Flexibilidad vs. usabilidad

El trabajo del arquitecto es entender qué prioridades dominan en el contexto del negocio y tomar decisiones fundamentadas.

## Visión y Alcance

La visión es el documento que actúa como "norte" del proyecto. Define:

- El problema que se resuelve
- Los actores principales
- Los objetivos del sistema
- Las restricciones conocidas

El diagrama de contexto (Nivel 0) muestra los límites del sistema y sus interacciones con actores externos, sin entrar en detalles internos.

## Hito 1

El Hito 1 aplica estos conceptos al caso LogiSmart:

- Analizar el dominio del problema
- Identificar actores y sus objetivos
- Definir casos de uso principales
- Documentar visión y alcance
- Construir el diagrama de contexto

---

## Perspectiva del profesor

### Insights clave

El profesor Salas Joaquin planteó desde la primera clase que programar no es el núcleo del trabajo del desarrollador de software. La diferencia entre construir un buen sistema y uno deficiente está en lo que sucede antes de escribir código.

> "La programación es una herramienta. El verdadero desafío es decidir QUÉ construir y CÓMO estructurarlo."

Un sistema puede funcionar perfectamente en términos técnicos y ser un fracaso total porque resuelve el problema equivocado. Esto ocurre cuando se saltea el análisis y se va directo a implementar.

El Fuzzy Front-End no es un obstáculo a eliminar rápido: es la etapa más importante. Dedicarle tiempo acorta el total del proyecto porque evita rediseños costosos.

RUP no es burocracia: es el resultado de aprender de décadas de proyectos fracasados. Los requisitos cambian, la tecnología cambia, el negocio cambia. Un modelo en cascada no sobrevive a esa realidad.

### Analogías y ejemplos reales

**La bicicleta y el camión**: si no hacés las preguntas correctas desde el principio, podés construir una bicicleta cuando el cliente necesitaba un camión. Convertir una bicicleta en camión es gigantesco, o imposible. El costo de no entender bien el problema al inicio es exponencial.

**Amazon vs. un comercio pequeño**: dos sistemas de e-commerce con funcionalidad idéntica (hacer un pedido) requieren arquitecturas radicalmente distintas. La diferencia no está en lo que hacen sino en los atributos de calidad que priorizan: carga, disponibilidad, geografía, seguridad. La arquitectura correcta no es la más sofisticada, sino la que responde a las prioridades reales del negocio.

### Consejo profesional

- Invertí tiempo en entender el problema antes de proponer soluciones técnicas.
- No hay arquitectura perfecta: sí hay la arquitectura correcta para ese contexto y esas prioridades.
- Documentá la visión y el alcance desde el día uno. Es el contrato de entendimiento con el cliente.
- Los casos de uso son herramientas de comunicación, no solo documentación. Usarlos para validar que entendiste bien el objetivo del usuario.
- Aprendé a convivir con la incertidumbre inicial: el objetivo es reducirla, no eliminarla de golpe.