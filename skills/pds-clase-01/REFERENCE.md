# Clase 1: De la Idea a la Arquitectura

Fuente: `clase_01.html` (solo contenido de `main-content`).

## Introduccion: el verdadero desafio

La materia apunta a disenar y construir software profesional, no solo a programar.

Idea central:

- El mayor desafio no es escribir codigo dificil.
- El mayor desafio es decidir **que** construir y **como estructurarlo**.
- Codigo que funciona no alcanza: debe ser mantenible, flexible y escalable.

Analogia del vehiculo:

- Si no se aclara el problema al inicio, se puede construir una bicicleta cuando se necesitaba un camion.
- Corregir eso tarde es costosisimo.

## Fuzzy Front-End

El "Fuzzy Front-End" es la fase inicial borrosa: hay idea y problema de negocio, pero mucha incertidumbre.

Por que es critica:

- Muchos proyectos fracasan ahi, antes de codificar.
- Error comun: construir sin plano.

Mision del curso:

- Llevar ingenieria a la fase borrosa.
- Usar proceso sistematico para pasar de idea difusa a plan robusto.
- Marco elegido: Proceso Unificado.

## Proceso Unificado (RUP)

RUP es un marco/fundacion para construir software en forma profesional, adaptable y orientado a riesgos.

Tres pilares:

1. Iterativo e incremental.
2. Guiado por casos de uso.
3. Centrado en la arquitectura.

## Pilar 1: Iterativo e incremental

Se abandona cascada lineal unica. El proyecto se divide en iteraciones cortas.

Cada iteracion recorre:

- analisis
- diseno
- implementacion
- pruebas

Resultado por iteracion:

- un incremento funcional y probado.

Ejemplo e-commerce por iteraciones:

1. Catalogo y busqueda.
2. Carrito.
3. Checkout y pagos.
4. Recomendaciones.

Ventajas:

- Feedback temprano.
- Reduccion de riesgo.
- Adaptabilidad ante cambios de requisitos.

## Pilar 2: Casos de uso

En lugar de pensar en features sueltas, se trabaja con objetivos del usuario.

Definicion:

- Caso de uso = narrativa de interaccion actor-sistema para lograr valor.

Estructura base:

| Componente | Descripcion |
| --- | --- |
| Nombre | Verbo + objeto. Ej: "Generar Reporte de Ventas" |
| Actor principal | Quien inicia |
| Precondiciones | Que debe ser cierto antes |
| Flujo basico | Camino feliz |
| Flujos alternativos | Desvios, errores |
| Postcondiciones | Estado resultante |

Ejemplo detallado: Reservar Mesa

```text
Caso de Uso: Reservar Mesa
Actor Principal: Cliente
Precondicion: Cliente autenticado.
Resumen: Busca restaurante, elige fecha/hora/comensales y confirma reserva.

Flujo Basico:
1) Cliente busca restaurante.
2) Sistema muestra coincidencias.
3) Cliente selecciona restaurante.
4) Sistema muestra detalle y disponibilidad.
5) Cliente elige fecha/hora/personas.
6) Sistema valida disponibilidad.
7) Cliente confirma.
8) Sistema registra reserva, envia email y notifica al restaurante.

Flujo Alternativo: sin disponibilidad
6a) Sistema informa y sugiere horarios alternativos.

Postcondicion:
Reserva registrada y partes notificadas.
```

Por que son poderosos:

- Centran el diseno en valor de usuario.
- Generan lenguaje comun entre negocio/dev/test.
- Sirven como base para diseno (clases/metodos/secuencias).
- Sirven como base para pruebas.

## Pilar 3: Atributos de calidad y arquitectura

Casos de uso dicen el **QUE**.
Atributos de calidad dicen el **COMO**.

Son requerimientos no funcionales que condicionan decisiones arquitectonicas.

Comparacion conceptual:

- Dos sistemas pueden tener el mismo caso de uso (ej. comprar producto).
- Pero arquitectura cambia mucho segun exigencias de performance, escala o confiabilidad.

Atributos clave:

- Performance (latencia/throughput).
- Seguridad (autenticacion/autorizacion/encriptacion).
- Confiabilidad (fallas, continuidad).
- Usabilidad (facilidad de uso).
- Escalabilidad (crecimiento de carga).
- Mantenibilidad (evolucion y costo futuro).

Impactos tipicos en arquitectura:

- Performance -> cache, DB optimizada, algoritmos eficientes.
- Seguridad -> capas de autenticacion, cifrado en transito y reposo.
- Confiabilidad -> redundancia, replicacion, recovery.
- Escalabilidad -> distribuido, microservicios, balanceo.

## Trade-offs

No existe arquitectura perfecta. Mejorar un atributo suele afectar otro.

Ejemplos:

- Seguridad vs performance.
- Flexibilidad vs usabilidad.
- Escalabilidad vs simplicidad.

Trabajo del arquitecto:

- Entender prioridades de negocio.
- Tomar decisiones justificadas.
- Elegir compromisos adecuados al contexto.

## Documentando la vision

Para salir del Fuzzy Front-End se formalizan decisiones.

### Declaracion de vision

Frase corta que actua como "estrella polar".
Debe responder:

- para quien es
- que problema resuelve
- que lo diferencia

Ejemplo de vision (LogiSmart):

> Convertirnos en el sistema nervioso central de la logistica para cada PyME en Latinoamerica, transformando su cadena de suministro de un centro de costos a una ventaja competitiva, a traves de una plataforma inteligente, accesible y radicalmente facil de usar.

### Diagrama de contexto (nivel 0)

Representa el sistema como caja negra y sus interacciones externas.

Sirve para:

- definir limites del sistema
- identificar actores
- listar sistemas externos integrados
- delimitar responsabilidades

## Introduccion a la practica (Hito 1)

Caso: LogiSmart.
Objetivo: producir Vision y Alcance inicial aplicando conceptos de clase.

Trabajo esperado del equipo:

1. Analizar dominio.
2. Identificar actores y stakeholders.
3. Definir catalogo de casos de uso.
4. Detallar 3 casos de uso clave.
5. Analizar atributos de calidad e implicaciones.
6. Consolidar documento de Vision y Alcance.

Importancia del hito:

- Es la fundacion del resto del proyecto del cuatrimestre.
- Decisiones iniciales condicionan clases siguientes.
