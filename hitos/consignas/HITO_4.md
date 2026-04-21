# Hito 4 del TPO: Aplicación de GRASP (Parte I)

**Tema:** Refinamiento del Modelo de Dominio aplicando GRASP (Parte I)
**Entrega:** Documento + Diagramas UML actualizados + Diagramas de Secuencia

## Objetivo General

En este hito, tomarán el **Diagrama de Clases del Hito 2** y lo **refinarán aplicando los
patrones GRASP Parte I** (Expert, Creator, Low Coupling, High Cohesion). El objetivo es
asegurar que el diseño sea robusto, mantenible y siga las mejores prácticas de asignación de
responsabilidades.
Además, crearán **Diagramas de Secuencia** para visualizar cómo colaboran los objetos en los
casos de uso más importantes.

## Actividades

## Actividad 1: Análisis de Responsabilidades

**Objetivo:** Revisar cada clase y verificar que tiene responsabilidades claras y bien definidas.
**Tareas:**

1. **Para cada clase en tu diagrama, completa la siguiente tabla:
Clase Responsabilidades
Principales
¿Tiene sentido? Observaciones**
Usuario Almacenar datos del
usuario, autenticarse
Sí/No ...
Cliente Heredar de Usuario,
solicitar envíos
Sí/No ...
... ... ... ...


2. **Identifica responsabilidades problemáticas:**
    - ¿Hay clases que hacen demasiadas cosas no relacionadas?
    - ¿Hay responsabilidades que no tienen sentido en esa clase?
    - ¿Hay clases que mezclan lógica de negocio con persistencia o notificaciones?
3. **Documenta tus hallazgos:**
    - Escribe un párrafo explicando qué clases tienen baja cohesión
    - Presenta separación de responsabilidades si es necesario
**Entregable:** Tabla de análisis + Párrafo de hallazgos

### Actividad 2: Aplicación del Patrón Expert

**Objetivo:** Verificar que cada responsabilidad está asignada a la clase que tiene la información
necesaria.
**Tareas:**

1. **Para cada responsabilidad importante, identifica al experto:**
    - ¿Quién tiene la información necesaria para cumplir esta responsabilidad?
    - ¿Está esa responsabilidad en la clase correcta?
2. **Revisa estos casos específicos en LogiSmart:**
    - **Calcular el costo total de una ruta:** ¿Quién es el experto? (¿Ruta? ¿Envio?
       ¿Vehiculo?)
    - **Calcular la distancia total de una ruta:** ¿Quién es el experto?
    - **Validar si un vehículo puede transportar un envío:** ¿Quién es el experto?
    - **Determinar el estado de un envío:** ¿Quién es el experto?
3. **Para cada caso, justifica tu respuesta:**
    - ¿Qué información necesita la clase para cumplir esta responsabilidad?
    - ¿Quién tiene acceso a esa información?
    - ¿Hay delegación de responsabilidades (una clase delega a otra)?


4. **Presenta cambios si es necesario:**
    - Si encuentras responsabilidades mal asignadas, Presenta moverlas
    - Dibuja cómo quedaría el diagrama después del cambio
**Entregable:** Análisis de 4-5 responsabilidades clave + Justificaciones + Diagrama actualizado
(si hay cambios)

### Actividad 3: Aplicación del Patrón Creator

**Objetivo:** Verificar que está claro quién crea qué en el sistema.
**Tareas:**

1. **Para cada clase importante, identifica quién la crea:
Clase Creador Regla de Creator Justificación**
PuntoDeEntrega Ruta Contención Ruta contiene
PuntosDeEntrega
Ubicacion???
Envio???
... ... ... ...
2. **Verifica las reglas de Creator:**
- ¿La clase creadora contiene a la clase creada? (Contención)
- ¿O la clase creadora agrega a la clase creada? (Agregación)
- ¿O la clase creadora tiene los datos para inicializarla? (Datos)
- ¿O la clase creadora usa frecuentemente a la clase creada? (Uso Frecuente)
3. **Identifica problemas:**
- ¿Hay clases que se crean de múltiples formas?
- ¿Hay clases que no tienen un creador claro?
- ¿Hay creación inconsistente?


4. **Presenta soluciones:**
    - Especifica exactamente dónde y cómo se crea cada clase
    - Escribe pseudocódigo o código Java para mostrar cómo se crean
**Entregable:** Tabla de creadores + Identificación de problemas + Pseudocódigo de creación

### Actividad 4: Análisis de Coupling y Cohesion

**Objetivo:** Identificar y reducir acoplamiento innecesario, aumentar cohesión.
**Tareas:**

1. **Identifica dependencias (Coupling):**
    - Dibuja un diagrama de dependencias: ¿Qué clases dependen de qué?
    - ¿Hay dependencias cíclicas? (A depende de B, B depende de A)
    - ¿Hay clases que dependen de muchas otras?
2. **Presenta reducción de acoplamiento:**
    - ¿Dónde puedes usar inyección de dependencias?
    - ¿Dónde puedes usar interfaces para desacoplar?
    - ¿Dónde puedes eliminar dependencias innecesarias?
3. **Revisa cohesión:**
    - ¿Hay clases que hacen cosas no relacionadas?
    - ¿Debería haber separación de responsabilidades?
    - Presenta nuevas clases si es necesario (ej: Repository, Service, etc.)
4. **Crea un diagrama antes/después:**
    - Muestra el diseño actual (con problemas de coupling/cohesion)
    - Muestra el diseño propuesto (mejorado)
    - Explica los cambios
**Entregable:** Análisis de dependencias + Diagrama de coupling + Propuestas de mejora +
Diagrama mejorado


### Actividad 5: Diagramas de Secuencia

**Objetivo:** Crear diagramas de secuencia para visualizar cómo colaboran los objetos.
**Tareas:**

1. **Selecciona 2-3 casos de uso importantes:**
    - Caso de Uso 1: Crear un Envío
    - Caso de Uso 2: Crear una Ruta y asignar envíos
    - Caso de Uso 3: (Elige uno importante para tu sistema)
2. **Para cada caso de uso, crea un diagrama de secuencia:**
    - Identifica los actores/objetos que participan
    - Dibuja las líneas de vida (líneas verticales)
    - Dibuja los mensajes (flechas) en orden temporal
    - Incluye retornos (flechas punteadas)
    - Muestra la creación de objetos
3. **Valida el diagrama:**
    - ¿Los objetos se comunican en el orden correcto?
    - ¿Hay acoplamiento excesivo? (¿Demasiadas comunicaciones directas?)
    - ¿Las responsabilidades están bien asignadas?
    - ¿Hay oportunidades para mejorar el diseño?
4. **Dibuja los diagramas:**
    - Puedes usar Draw.io, Lucidchart, o incluso ASCII art
    - Exporta como imagen PNG
    - Incluye en tu documento
**Entregable:** 2-3 Diagramas de Secuencia + Análisis de validación


## Entregable Final

Tu entrega debe incluir:

### 1. Documento (Hito4_GRASP_Parte1) con:

- Análisis de responsabilidades (Actividad 1)
- Aplicación de Expert (Actividad 2)
- Aplicación de Creator (Actividad 3)
- Análisis de Coupling/Cohesion (Actividad 4)
- Descripción de Diagramas de Secuencia (Actividad 5)

### 2. Diagrama de Clases Actualizado

- Versión mejorada del Hito 2 aplicando GRASP
- Exportado como imagen PNG
- Incluido en el documento

### 3. Diagramas de Secuencia (2-3)

- Para los casos de uso más importantes
- Exportados como imágenes PNG
- Incluidos en el documento

### 4. Justificaciones y Análisis

- Explicaciones detalladas de cada decisión de diseño
- Comparación antes/después
- Beneficios de los cambios propuestos

## Criterios de Evaluación

```
Criterio Peso Descripción
Análisis de
Responsabilidades
15% Identificación correcta de
responsabilidades, baja
cohesión, problemas
Aplicación de Expert 20% Verificación de que
responsabilidades están en la
clase correcta, justificaciones
```

```
Criterio Peso Descripción
Aplicación de Creator 15% Identificación clara de
creadores, aplicación de
reglas, solución de
problemas
Coupling/Cohesion 20% Identificación de
dependencias, propuestas de
mejora, diagramas
antes/después
Diagramas de Secuencia 15% Claridad, corrección,
validación del diseño
Documentación y
Presentación
15% Claridad del documento,
organización, justificaciones
detalladas
```
## Preguntas Guía

Si te quedas atascado, responde estas preguntas:

1. **Expert:** ¿Quién tiene la información necesaria para hacer esto?
2. **Creator:** ¿Quién contiene a esta clase? ¿Quién la crea?
3. **Low Coupling:** ¿Puedo pasar esto como parámetro en lugar de crearlo internamente?
4. **High Cohesion:** ¿Tiene sentido que esta clase haga esto? ¿O debería estar en otra
    clase?
5. **Diagramas:** ¿Cómo colaboran los objetos? ¿Hay demasiadas comunicaciones
    directas?

## Recursos

- **Diagramas de Secuencia:** https://www.lucidchart.com/pages/uml-sequence-diagram
- **PlantUML Online:** plantuml.online
- **Mermaid:** https://mermaid.js.org/
- **Draw.io:** https://draw.io
- **Clase 4 - Presentación:** Revisa la presentación HTML para recordar los conceptos


## Notas Importantes

- **Trabajo en equipo:** Este es un trabajo grupal. Todos deben entender el diseño.
- **Iterativo:** No esperes que sea perfecto a la primera. Itera, mejora, refina.
- **Justificaciones:** No solo digas "cambié esto". Explica POR QUÉ lo cambiaste.
- **Realismo:** No intentes hacer un diseño perfecto. Busca un balance entre simplicidad y
    robustez.
- **Entrega:** Sube todo a la plataforma antes de la fecha límite.