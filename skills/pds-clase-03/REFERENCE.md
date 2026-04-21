# Clase 3: Del Diseño a la Implementación

## Por qué separar diseño de implementación

Separar el diseño de la implementación permite:

- Pensar en forma abstracta antes de comprometerse con detalles de código
- Comunicar la arquitectura en un lenguaje universal (UML) independiente del lenguaje de programación
- Detectar errores de diseño antes de escribir código, cuando el costo de corregirlos es mucho menor
- Mantener la visión global del sistema mientras se implementan las partes

## Eclipse IDE

Eclipse es el entorno de desarrollo integrado utilizado en la materia. Ofrece:

- Editor inteligente con resaltado de sintaxis y autocompletado
- Compilación integrada con detección de errores en tiempo de escritura
- Depurador integrado para seguir la ejecución paso a paso
- Gestión de proyectos y paquetes

Para crear un nuevo proyecto Java: File > New > Java Project.

## Estructura de Paquetes

Los paquetes organizan las clases en grupos lógicos. La convención es usar el dominio invertido como prefijo, por ejemplo `com.logismart`.

Estructura recomendada para LogiSmart:

- `com.logismart.dominio`: clases del modelo (entidades)
- `com.logismart.servicio`: lógica de negocio
- `com.logismart.persistencia`: acceso a base de datos
- `com.logismart.ui`: interfaz de usuario

## Traducción de UML a Java

La traducción de diagramas UML a código Java sigue una correspondencia directa:

**Visibilidad**:
- `-` privado → `private`
- `+` público → `public`
- `#` protegido → `protected`

**Atributos**: se traducen a campos privados tipados. Ejemplo: `- nombre: String` → `private String nombre;`

**Métodos**: se traducen a métodos Java con sus parámetros y tipo de retorno.

## Constructores y Atributos

Los constructores inicializan el estado del objeto. Se usa la palabra clave `this` para distinguir parámetros del constructor de los atributos de instancia.

Eclipse puede generar automáticamente getters y setters desde Source > Generate Getters and Setters.

## Implementación de Relaciones

**Asociación simple**: se implementa como un campo del tipo de la clase asociada.

```java
private Cliente cliente;
```

**Composición (uno a muchos)**: se implementa con `ArrayList<Tipo>` y métodos para agregar y obtener elementos.

```java
private ArrayList<PuntoDeEntrega> puntos = new ArrayList<>();

public void agregarPunto(PuntoDeEntrega punto) {
    puntos.add(punto);
}
```

**Herencia**: se implementa con la palabra clave `extends`. El constructor de la subclase llama al de la superclase con `super()`.

```java
public class Cliente extends Usuario {
    public Cliente(String nombre, String email) {
        super(nombre, email);
    }
}
```

## Jerarquía de Herencia: Ejemplo

Clase base `Usuario` con lógica de autenticación compartida. Subclases `Cliente` y `Operador` extienden con comportamientos específicos. Cada subclase llama a `super()` para inicializar los atributos heredados.

## Hito 3

El Hito 3 requiere implementar en código Java funcional el diseño UML producido en el Hito 2:

- Crear el proyecto Java con la estructura de paquetes
- Traducir cada clase UML a una clase Java
- Implementar las relaciones como campos y colecciones
- Implementar la jerarquía de herencia si corresponde
- Verificar que el código compila y ejecuta correctamente

---

## Perspectiva del profesor

### Insights clave

El profesor planteó en esta clase una pregunta fundamental que guía toda la carrera: ¿cuál es la diferencia crítica entre un programador experimentado y uno que recién empieza? La respuesta no es conocer más herramientas ni escribir más rápido.

> "Las primeras clases son de entender, presentar, proyectar las ideas antes de poner una línea de código."

El código correcto que resuelve el problema equivocado es un fracaso. El código mediocre que resuelve exactamente el problema correcto puede ser exitoso. La calidad del código importa, pero importa menos que entender qué construir.

Sobre el uso de herramientas de IA en desarrollo: son poderosas cuando se les dan parámetros correctos y contexto completo. Si le dás información incompleta a una IA, te va a dar lo primero que se le ocurra basado en su base de conocimiento, no lo que realmente necesitás. La calidad del output depende de la calidad del input, y eso requiere entender el dominio.

La diferencia crítica entre programadores está en el entendimiento del código, no en la velocidad para escribirlo. El que entiende puede hacer diagnóstico, puede anticipar problemas, puede diseñar soluciones. El que solo escribe, choca con la pared cuando las cosas salen mal.

> "Lo más importante del código: que sea usable, mantenible, con una arquitectura pensada. Y cómo definís eso depende de entender el contexto."

### Analogías y ejemplos reales

**El desarrollador como arquitecto**: así como un arquitecto no empieza a colocar ladrillos sin planos, el desarrollador no empieza a escribir código sin diseño. Hacerlo así genera deuda técnica que se paga cara más adelante.

**Dar parámetros incompletos a una IA**: si le pedís a Claude Code o cualquier herramienta que diseñe algo sin contexto, va a darte algo genérico basado en lo que conoce. El que sabe usar esas herramientas es el que sabe qué pedirle y con qué contexto. Eso requiere dominio del problema.

**El código que explota en producción**: hay sistemas que funcionan perfecto en desarrollo y explotan el día que hay carga real o un caso edge que no se previó. La diferencia está en si el diseño contempló esos escenarios desde el inicio.

### Consejo profesional

- Antes de abrir el IDE, revisá tu diagrama de clases. Si no lo entendés, no vas a poder traducirlo bien.
- Usá la estructura de paquetes desde el primer día. Reorganizar un proyecto mal estructurado es costoso.
- Los getters y setters no son decoración: son el mecanismo de acceso controlado al estado del objeto. Generarlos en Eclipse es rápido, pero entendé por qué existen.
- Aprendé a leer los errores del compilador. Eclipse te dice exactamente qué está mal y dónde.
- El código que escribís hoy lo vas a leer vos o alguien más en semanas o meses. Escribilo para que sea entendible, no solo para que compile.
- Herramienta de IA como Claude Code son multiplicadores de productividad solo si entendés lo que producen. Si no podés revisar el output, no podés confiar en él.