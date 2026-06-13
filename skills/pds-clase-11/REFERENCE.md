# Clase 11: Patrones de Comportamiento I

## Patrones de comportamiento

Los patrones de comportamiento se ocupan de la comunicación efectiva entre objetos y la asignación de responsabilidades. Definen los flujos de control que son difíciles de seguir en tiempo de ejecución. La Clase 11 cubre tres: Chain of Responsibility, Command e Interpreter.

## Chain of Responsibility

### Problema
Cuando una solicitud puede ser manejada por múltiples objetos y no se sabe a priori cuál la procesará. En LogiSmart, un envío pasa por ValidadorDatos → ValidadorInventario → ValidadorPago → ValidadorSeguridad.

### Solución
Crear una cadena de manejadores donde cada uno decide si procesa la solicitud o la pasa al siguiente. El cliente solo conoce el primer eslabón.

### Estructura
- **Manejador abstracto**: define `validar(envio)` y tiene referencia al `siguiente`.
- **Manejadores concretos**: cada uno valida su aspecto y si pasa, llama a `siguiente.validar(envio)`.
- **Construcción**: `validador1.setSiguiente(validador2); validador2.setSiguiente(validador3);`

### Cuándo usar
Múltiples objetos pueden manejar una solicitud; el conjunto y el orden de manejadores debe ser configurable dinámicamente; no querés acoplar el cliente a los manejadores específicos.

### Desventajas
Debugging difícil (hay que seguir la cadena); no hay garantía de que algún manejador procese la solicitud; múltiples llamadas pueden afectar rendimiento.

### Casos reales
Middleware web (Express, Django), manejo de excepciones (try-catch anidado), Log4j con múltiples appenders, workflows de aprobación empresarial.

---

## Command

### Problema
Necesitás encolar, deshacer o registrar operaciones: crear envío, cancelar envío, actualizar estado. ¿Cómo encapsulás estas operaciones para poder tratarlas como objetos?

### Solución
Encapsula una solicitud como un objeto con métodos `ejecutar()` y `deshacer()`. Un invocador (cola) gestiona el historial.

### Estructura
- **Interfaz Comando**: `ejecutar()`, `deshacer()`, `obtenerDescripcion()`.
- **Comandos concretos**: `ComandoCrearEnvio`, `ComandoCancelarEnvio`, `ComandoActualizarEstado`. Cada uno guarda el estado anterior para poder deshacer.
- **ColaComandos (Invocador)**: mantiene historial con `indiceActual`. `ejecutar()` agrega al historial; `deshacer()` retrocede el índice y llama `deshacer()` en el comando; `rehacer()` avanza el índice y vuelve a ejecutar.

### Cuándo usar
Necesitás undo/redo; querés encolar operaciones para ejecutarlas después; necesitás logging de operaciones; necesitás macros (secuencias de comandos).

### Desventajas
Muchas clases pequeñas; historial puede consumir mucha memoria; pequeña sobrecarga por encapsulación.

### Casos reales
Editores de texto (Word, Photoshop), colas de tareas (Celery, RabbitMQ), transacciones de bases de datos (ACID).

---

## Interpreter

### Problema
LogiSmart necesita evaluar reglas de negocio expresadas como texto: `ORIGEN = "Buenos Aires" AND PESO < 10`. ¿Cómo interpretás estas expresiones?

### Solución
Define una gramática para el lenguaje y una jerarquía de clases para representar y evaluar cada tipo de expresión. El árbol de objetos refleja el árbol de sintaxis abstracta (AST).

### Estructura
- **Interfaz Expresion**: `boolean evaluar(Envio envio)`.
- **Expresiones terminales**: `ExpresionOrigen`, `ExpresionDestino`, `ExpresionPeso`. Evalúan una condición concreta sobre el objeto.
- **Expresiones no-terminales (operadores)**: `ExpresionAND`, `ExpresionOR`, `ExpresionNOT`. Componen otras expresiones y delegan recursivamente.

### Cuándo usar
Necesitás interpretar un lenguaje específico del dominio; las reglas cambian con frecuencia y querés que sean configurables; la gramática es relativamente simple.

### Desventajas
Para gramáticas complejas genera muchas clases; la interpretación puede ser lenta; cambios en la gramática requieren cambios en código.

### Casos reales
SQL, XPath, regex engines, motores de reglas (Drools), lenguajes de scripting.

---

## Comparación

| Patrón | Pregunta clave | Estructura |
|--------|---------------|------------|
| Chain of Responsibility | ¿Múltiples manejadores en cadena? | Lista enlazada de manejadores |
| Command | ¿Necesitás undo/redo o cola de operaciones? | Objeto que encapsula una operación |
| Interpreter | ¿Necesitás evaluar expresiones de un lenguaje? | Árbol de expresiones (AST) |
