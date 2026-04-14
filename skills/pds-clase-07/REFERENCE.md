# Clase 7: Patrones Creacionales II

Fuente: `clase_07.html` (contenido de `main-content`).

## Estructura de la clase

1. Repaso de GRASP Parte II (Clase 5)
2. Repaso de Patrones Creacionales I (Clase 6)
3. Abstract Factory
4. Builder
5. Prototype
6. Comparacion de los 5 patrones

## Abstract Factory

Abstract Factory es un patron que permite crear familias de objetos relacionados sin especificar sus clases concretas.

Problema tipico:

- cuando tienes multiples variantes (regiones, configuraciones, etc)
- necesitas crear conjuntos cohesivos de objetos

Ejemplo en LogiSmart:

- diferentes vehiculos, mapas, impuestos para Argentina, Brasil, Mexico

Solucion:

- crear una interfaz de factory para cada familia
- cada region implementa su propia factory
- la factory crea todos los objetos relacionados de esa region

Analoga:

- una fabrica que produce "todo lo que necesitas para Argentina"

Ventajas:

- garantiza coherencia entre objetos de la familia
- facil agregar nuevas regiones
- desacoplamiento del cliente

## Builder

Builder es un patron que permite construir objetos complejos paso a paso.

Problema tipico:

- objetos con muchos atributos, muchos opcionales
- constructores con demasiados parametros
- dificil leer y mantener

Ejemplo en LogiSmart:

- `Envio` con 12+ atributos, la mayoria opcionales

Solucion:

- crear una clase Builder
- el Builder tiene metodos para cada atributo opcional
- cada metodo retorna `this` (fluent interface)
- al final, un metodo `build()` crea el objeto

Implementacion:

1. Clase principal con constructor privado
2. Clase Builder que recibe los parametros requeridos
3. Metodos fluidos para cada parametro opcional
4. Metodo build() que retorna la instancia final

Ventajas:

- codigo muy legible y expresivo
- facil agregar nuevos atributos opcionales
- imposible olvidar atributos requeridos

## Prototype

Prototype es un patron que permite crear nuevas instancias clonando un objeto existente.

Problema tipico:

- necesitas crear muchos objetos similares
- es mas economico clonar que crear desde cero

Ejemplo en LogiSmart:

- crear 100 envios con la misma configuracion

Solucion:

- implementar Cloneable en la clase
- sobrescribir el metodo clone()
- clonar el prototipo tantas veces como sea necesario

Punto critico:

- Shallow copy vs Deep copy
- si el objeto contiene listas u objetos, hay que hacer deep copy
- en el ejemplo de Envio, hay que clonar tambien las listas internas

Ventajas:

- crear objetos es mas rapido que con Builder
- util para objetos complejos

## Comparacion: Los 5 patrones creacionales

Singleton:

- para objetos que deben existir una unica vez
- ejemplo: logger, configuracion
- complejidad: baja

Factory Method:

- para esconder la clase concreta
- ejemplo: crear un tipo de objeto sin saber su clase
- complejidad: baja-media

Abstract Factory:

- para crear familias de objetos relacionados
- ejemplo: diferentes suites de objetos por region
- complejidad: media

Builder:

- para objetos complejos con muchas opciones
- ejemplo: Envio con 12+ atributos
- complejidad: media-alta

Prototype:

- para clonar objetos existentes
- ejemplo: crear 100 copias de un objeto
- complejidad: media

## Arbol de decision

Para elegir el patron correcto:

1. ¿Necesitas exactamente 1 instancia? → Singleton
2. ¿Necesitas crear familias de objetos? → Abstract Factory
3. ¿El objeto es muy complejo con muchas opciones? → Builder
4. ¿Necesitas clonar objetos existentes? → Prototype
5. ¿Necesitas esconder la clase concreta? → Factory Method

## Shallow copy vs Deep copy

Shallow copy (copia superficial):

- copia los valores primitivos
- comparte referencias a objetos
- problema: cambiar una lista afecta todos los clones

Deep copy (copia profunda):

- copia todo, incluso objetos internos
- mas lento pero cada clon es independiente

En Prototype, es importante hacer deep copy de listas y objetos internos.

## Aplicacion en LogiSmart

Contexto de la clase:

- `Singleton`: ConfiguracionSistema
- `Factory Method`: VehiculoFactory
- `Abstract Factory`: VehiculoFactoryArgentina, VehiculoFactoryBrasil
- `Builder`: EnvioBuilder para crear envios complejos
- `Prototype`: Clonar envios con la misma configuracion

## Hito 7

El hito pide implementar los patrones creacionales en LogiSmart y justificar por que se eligio cada uno.
