# Clase 4: GRASP Parte I

Fuente: `clase_04.html` (contenido de `main-content`).

## Que es GRASP

GRASP significa General Responsibility Assignment Software Patterns.

Su pregunta central es:

- a que clase le asigno esta responsabilidad

El valor de GRASP esta en mejorar la asignacion de responsabilidades para lograr:

- alta cohesion
- bajo acoplamiento
- mejor mantenibilidad
- mejor extensibilidad

## Los 4 patrones vistos en la clase

1. Expert
2. Creator
3. Low Coupling
4. High Cohesion

## 1. Expert

Asignar la responsabilidad a la clase que tiene la informacion necesaria para cumplirla.

Idea clave:

- la clase experta conoce los datos necesarios
- la responsabilidad queda donde naturalmente pertenece

Ejemplo de clase:

- `Factura` calcula el total porque conoce sus `Linea`
- `Ruta` puede calcular su distancia total porque conoce sus puntos
- `Ubicacion` puede calcular distancias entre ubicaciones

Beneficios:

- encapsulacion
- bajo acoplamiento
- alta cohesion
- mantenibilidad

## 2. Creator

Asignar la responsabilidad de crear instancias a la clase que tiene relacion especial con ellas.

Reglas principales:

1. Contencion: si B contiene A, B crea A.
2. Agregacion: si B agrega A, B crea A.
3. Datos: si B tiene los datos para inicializar A, B crea A.
4. Uso frecuente: si B usa A frecuentemente, B crea A.

Ejemplo en LogiSmart:

- `Ruta` crea `PuntoDeEntrega` porque lo contiene
- `FormularioDeEnvio` puede crear `Envio` porque ya tiene los datos necesarios

Beneficios:

- consistencia
- responsabilidad clara
- validacion previa a la creacion
- menos acoplamiento

## 3. Low Coupling

Minimizar dependencias entre clases.

La tecnica mas importante es la inyeccion de dependencias:

- pasar objetos como parametros
- no crearlos adentro de la clase si se puede evitar

Ejemplo:

- en vez de que `Ruta` cree su propio `Vehiculo`, recibirlo por constructor

Beneficios:

- flexibilidad
- testabilidad
- reutilizacion
- cambios con menor efecto domino

## 4. High Cohesion

Mantener juntas las responsabilidades relacionadas y separar las que no lo estan.

Una clase con baja cohesion suele mezclar:

- logica de dominio
- persistencia
- notificaciones
- logging

La solucion es dividir responsabilidades en clases distintas, por ejemplo:

- `Envio` para reglas de negocio
- `EnvioRepository` para persistencia
- `NotificadorDeEnvios` para notificaciones

Beneficios:

- claridad
- mantenibilidad
- testabilidad
- reutilizacion

## Diagramas de interaccion

La clase introduce diagramas que muestran como colaboran los objetos en el tiempo.

Tipos vistos:

- diagrama de secuencia
- diagrama de colaboracion

Para leer un diagrama de secuencia, observar:

1. actores u objetos
2. lineas de vida
3. mensajes entre objetos
4. retornos
5. activaciones

Uso practico:

- validar flujos de casos de uso importantes
- revisar si las responsabilidades estan bien distribuidas
- detectar acoplamiento excesivo

## Aplicacion en LogiSmart

La clase usa un ejemplo de `Ruta` con `Vehiculo`, `PuntoDeEntrega` y `Ubicacion` para mostrar como aplicar GRASP.

La idea general es:

- asignar responsabilidad al experto
- dejar que el creador cree lo que contiene o usa
- reducir acoplamiento con dependencias inyectadas
- separar responsabilidades no relacionadas

## Hito 4

El hito pide revisar el modelo y aplicar GRASP Parte I en el diseno de LogiSmart.

En concreto:

- revisar clases y relaciones
- aplicar Expert y Creator
- mejorar bajo acoplamiento y alta cohesion
- producir diagramas de secuencia para casos de uso clave