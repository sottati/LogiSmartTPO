# Clase 5: GRASP Parte II

Fuente: `clase_5.html` (contenido de `main-content`).

## Idea general

La clase continua GRASP con cinco patrones orientados a hacer el diseño mas flexible, mantenible y escalable.

Los patrones vistos son:

1. Controller
2. Polymorphism
3. Pure Fabrication
4. Indirection
5. Protected Variations

## 1. Controller

El Controller actua como intermediario entre la UI y el dominio.

Problema:

- cuando la UI maneja directamente la logica de negocio, el sistema queda fuertemente acoplado

Solucion:

- crear una clase controller que reciba solicitudes, coordine operaciones y devuelva resultados

Tipos mencionados:

- Facade Controller
- Use Case Controller

Ventajas:

- desacoplamiento
- testabilidad
- reutilizacion
- mantenibilidad

## 2. Polymorphism

Se usa para eliminar condicionales anidados y evitar codigo fragil.

Problema tipico:

- `instanceof`
- `if/else` largos
- violacion de Open/Closed

Solucion:

- definir una interfaz o supertipo
- hacer que cada tipo concrete su comportamiento

Ejemplo conceptual:

- `Usuario` define capacidades
- `Cliente`, `Operador` o `Administrador` implementan su version

Ventajas:

- extensibilidad
- mantenibilidad
- testabilidad
- codigo mas expresivo

## 3. Pure Fabrication

Se inventa una clase que no pertenece al dominio real pero que ayuda al diseño.

Cuandos se usa:

- cuando aplicar Expert o Creator forzaria responsabilidades incorrectas
- cuando una tarea no encaja naturalmente en una entidad del dominio

Ejemplos de este tipo de clase:

- servicio de notificaciones
- logger
- processor de pagos
- generador de reportes

Ventajas:

- separacion de responsabilidades
- reutilizacion
- testabilidad
- mejor mantenibilidad

## 4. Indirection

Introduce un intermediario para desacoplar componentes.

Problema:

- dependencias directas entre componentes concretos

Solucion:

- usar una interfaz o abstraccion entre ambos lados

Esto facilita:

- cambiar implementaciones sin tocar el cliente
- usar mocks en pruebas
- integrar inyeccion de dependencias

## 5. Protected Variations

Protege el sistema de cambios futuros mediante abstracciones en puntos de variacion.

Idea clave:

- identificar donde es probable que cambie el comportamiento
- aislar ese cambio detras de una interfaz o estrategia

Ejemplo de variaciones comunes:

- algoritmos
- fuentes de datos
- servicios externos
- politicas de negocio

Ventajas:

- anticipacion
- estabilidad
- flexibilidad
- menos codigo afectado por cambios

## Como trabajan juntos

Los cinco patrones se complementan:

- Controller coordina solicitudes
- Polymorphism evita condicionales
- Pure Fabrication saca responsabilidades que no pertenecen al dominio
- Indirection desacopla componentes
- Protected Variations protege puntos de cambio

## Aplicacion en LogiSmart

La clase usa ejemplos como:

- `BibliotecaController` como analogia de Controller
- interfaces para usuarios o notificadores
- clases de servicio como Pure Fabrication
- interfaces para desacoplar servicios
- estrategias para politicas de negocio variables

## Hito 5

El hito pide aplicar GRASP Parte II al diseno de LogiSmart.

Las tareas principales son:

- analizar el diseno previo
- agregar un controller principal
- reemplazar condicionales por polimorfismo
- crear clases de servicio cuando haga falta
- aislar puntos de variacion
- documentar las decisiones tomadas