# Clase 6: Patrones de Diseno GoF - Creacionales

Fuente: `clase_6.html` (contenido de `main-content`).

## Que es un patron de diseno

Un patron de diseno es una solucion reutilizable a un problema comun de diseno de software.

Caracteristicas de un buen patron:

- resuelve un problema especifico
- esta probado
- esta documentado
- es reutilizable

## Gang of Four (GoF)

En 1994, el libro de Gamma, Helm, Johnson y Vlissides documento 23 patrones clasificados en tres grupos:

1. Creacionales
2. Estructurales
3. De comportamiento

## Patrones creacionales

Resuelven problemas de como crear objetos sin acoplar el codigo al tipo concreto.

Los 5 patrones creacionales son:

1. Singleton
2. Factory Method
3. Abstract Factory
4. Builder
5. Prototype

En la clase se profundiza en los dos primeros.

## Singleton

Singleton asegura que una clase tenga una unica instancia y ofrece un unico punto de acceso.

Problema:

- no queremos multiples instancias de configuracion, conexion o logger

Solucion clasica:

- constructor privado
- instancia estatica
- metodo publico para obtenerla

Punto importante:

- en sistemas multihilo hay que cuidar la thread safety

Soluciones vistas:

- sincronizar el metodo
- eager initialization
- double-checked locking

Ventajas:

- unico punto de acceso
- control centralizado
- inicializacion diferida en algunas variantes

Desventajas:

- dificulta testing
- oculta dependencias
- puede terminar usandose como anti-patron

Uso tipico:

- base de datos
- logger
- configuracion
- cache global

## Factory Method

Factory Method crea objetos sin acoplar el codigo cliente a las clases concretas.

Problema:

- si el cliente instancia directamente subclases, queda acoplado a todas ellas

Solucion:

- encapsular la creacion en una factory
- devolver una abstraccion o interfaz

Ejemplos en LogiSmart:

- fabrica de envios
- fabrica de notificadores
- fabrica de vehiculos

Ventajas:

- desacoplamiento
- centralizacion de la creacion
- facilidad para agregar nuevos tipos
- mejor testabilidad

## Singleton vs Factory Method

Singleton:

- busca una unica instancia
- util para recursos globales

Factory Method:

- busca crear el tipo correcto de objeto
- util cuando hay variantes y queres desacoplar la creacion

Regla practica:

- usa Singleton para acceso unico a un recurso compartido
- usa Factory Method cuando la creacion cambia segun el contexto

## Aplicacion en LogiSmart

La clase propone una arquitectura donde:

- `ConexionBD` usa Singleton
- las factories crean envios, notificadores y vehiculos
- un `LogiSmartController` coordina el uso de esos objetos

Beneficios esperados:

- codigo desacoplado
- cambios centralizados
- facil extension
- mejor testabilidad

## Hito 6

El hito pide aplicar patrones creacionales en LogiSmart.

Las tareas principales son:

- definir donde conviene Singleton
- decidir que objetos se crean con factories
- justificar por que se usan esos patrones
- integrar la solucion en el controlador principal