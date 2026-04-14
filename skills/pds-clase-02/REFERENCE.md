# Clase 2: Del Análisis al Diseño

## Del QUÉ al CÓMO

El análisis responde QUÉ hace el sistema (casos de uso, requisitos). El diseño responde CÓMO lo hace (estructura interna, clases, relaciones). El modelo de dominio es el puente entre ambas etapas.

## Programación Orientada a Objetos vs. Procedural

En la programación procedural, los datos y las funciones están separados. En POO, los objetos encapsulan estado (atributos) y comportamiento (métodos) en una unidad coherente.

Ventajas de POO:

- Metodologías reutilizables
- Construcción incremental por etapas
- Entendimiento del ecosistema completo, no solo de una parte
- Mejor calidad de código con menos reescritura

## Los Tres Pilares del Objeto

Todo objeto tiene:

1. **Identidad**: es único, distinguible de otros objetos del mismo tipo
2. **Estado**: conjunto de valores de sus atributos en un momento dado
3. **Comportamiento**: operaciones que puede realizar o recibir

Una clase es la plantilla o definición abstracta. Un objeto es una instancia concreta en memoria. Múltiples objetos pueden instanciarse desde una misma clase.

## Encapsulación

La encapsulación oculta el estado interno del objeto y expone comportamiento controlado. Protege la integridad del objeto mediante modificadores de acceso:

- `+` público: accesible desde cualquier clase
- `-` privado: accesible solo desde la propia clase
- `#` protegido: accesible desde la clase y sus subclases

El objetivo no es paranoia de seguridad: es gestionar el cambio. Si el estado interno cambia, el impacto queda contenido dentro de la clase, no se propaga por todo el sistema.

## Diagrama de Clases UML

El diagrama de clases es el artefacto central del diseño orientado a objetos. Cada clase se representa con tres secciones:

1. Nombre de la clase
2. Atributos con visibilidad y tipo
3. Métodos con parámetros y tipo de retorno

UML es un lenguaje técnico de comunicación universal. No depende del lenguaje de programación y permite comunicar diseño a cualquier equipo técnico.

## Relaciones Entre Clases

### Asociación
Relación básica "conoce a". Una clase tiene una referencia a otra. Se indica con multiplicidad (1..*, 0..1, etc.).

### Agregación
Relación débil "tiene un". Las partes pueden existir independientemente del todo. Se representa con un diamante vacío.

### Composición
Relación fuerte "es parte de". Las partes no pueden existir sin el todo. Se representa con un diamante relleno.

### Herencia
Relación "es un". Una subclase extiende a la superclase, heredando atributos y métodos. Se representa con una flecha con triángulo vacío. Aplica el principio de sustitución de Liskov.

## Alta Cohesión y Bajo Acoplamiento

**Alta cohesión**: cada clase tiene una única responsabilidad clara. Sus métodos y atributos están fuertemente relacionados entre sí.

**Bajo acoplamiento**: las clases dependen lo menos posible entre sí. Un cambio en una clase no obliga a cambiar otras.

Estos dos principios son el corazón del buen diseño orientado a objetos. No son metas absolutas: requieren equilibrio consciente según el contexto.

## Hito 2

El Hito 2 transforma el análisis del Hito 1 en un modelo de dominio:

- Identificar clases candidatas a partir de los sustantivos de los casos de uso
- Definir atributos y métodos de cada clase
- Establecer relaciones justificadas entre clases
- Revisar cohesión y acoplamiento del diseño

---

## Perspectiva del profesor

### Insights clave

El profesor insistió en que el QUÉ y el CÓMO son las dos preguntas más importantes en el desarrollo de software. Entender el QUÉ (requisitos) sin descuidar el CÓMO (estructura) es lo que diferencia a un buen desarrollador de uno que solo escribe código que funciona.

> "No necesitás aplicar el mismo nivel de control a cada dato. El balance entre complejidad y riesgo real importa."

La encapsulación no es un dogma de seguridad: es una herramienta de gestión del cambio. Cuando el estado interno de un objeto está bien encapsulado, modificarlo no genera efectos en cascada por todo el sistema.

Sobre herencia vs. composición: no toda relación "tiene sentido" como herencia. La herencia rígida puede hacer que una subclase se rompa cuando cambia la superclase. A veces la composición es más robusta.

El objetivo del diseño orientado a objetos es construir un ecosistema donde cada parte tiene un rol claro y puede ser reutilizada, modificada o reemplazada sin afectar el resto.

> "La clave es lograr mejores productos, en menos tiempo, a través del diseño inteligente y la reutilización de código."

### Analogías y ejemplos reales

**La bóveda bancaria**: la encapsulación es como una bóveda. No todo necesita estar en la bóveda. Controlás qué información es accesible y a quién. El exceso de protección paraliza; la falta de protección genera caos.

**La ciudad sobre-fortalecida**: un sistema con demasiado acoplamiento o rigidez es como una ciudad medieval sobre-fortalecida: extremadamente segura pero imposible de modificar. Llega un punto donde "no toques nada o todo se cae". El objetivo es flexibilidad con estructura.

**Herencia y el hijo que se rompe**: cuando la superclase cambia, las subclases pueden romperse sin que nadie lo esperara. La herencia mal usada crea dependencias frágiles. La composición permite cambiar partes sin afectar el todo.

**El equipo dividido sin visión de ecosistema**: si el equipo A programa el módulo de despacho y el equipo B programa recepción sin entender el ecosistema completo, ambos pueden resolver bien su parte y producir un sistema que no encaja. El diseño del dominio es lo que genera coherencia.

### Consejo profesional

- Al diseñar una clase, preguntate: ¿en qué es experta esta clase? ¿Qué sabe que nadie más debería saber?
- El diagrama de clases no es documentación decorativa: es el lenguaje técnico con el que hablan los equipos y los clientes técnicos.
- Cuando tengas dudas entre herencia y composición, inclinarte por composición suele ser más seguro.
- Diseñá pensando en reutilización desde el inicio. Un objeto bien diseñado hoy evita reescribir código mañana.
- No sobre-encapsules ni sobre-expongas: el balance depende del contexto real del problema.