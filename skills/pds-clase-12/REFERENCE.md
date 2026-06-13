# Clase 12: Patrones de Comportamiento II

## Diferencia con Clase 11
La Clase 11 cubría patrones centrados en distribuir responsabilidades (Chain, Command, Interpreter). La Clase 12 cubre patrones centrados en comunicación, estado y notificación: Iterator, Mediator, Memento, Observer.

---

## Iterator

### Problema
LogiSmart almacena envíos en distintas estructuras (array, lista enlazada, árbol, hash). ¿Cómo iterás sobre ellas con el mismo código cliente sin conocer su estructura interna?

### Solución
Define una interfaz `IteradorEnvios` con `tieneSiguiente()`, `obtenerSiguiente()` y `reiniciar()`. Cada colección crea su propio iterador interno.

### Estructura
- **Interfaz IteradorEnvios**: operaciones de recorrido.
- **Interfaz ColeccionEnvios**: `crearIterador()`, `agregar()`, `remover()`.
- **Implementaciones concretas**: `ColeccionArray` con `IteradorArray` interno; `ColeccionLista` con `IteradorLista` interno. Cada iterador mantiene su estado de posición independientemente.

### Cuándo usar
Querés una interfaz uniforme para diferentes colecciones; necesitás múltiples recorridos simultáneos; querés ocultar la representación interna.

### Desventajas
Más clases; pequeña sobrecarga respecto a acceso directo.

### Casos reales
Java Collections (ArrayList, LinkedList), cursores de bases de datos, ResultSet, streams de datos.

---

## Mediator

### Problema
Centro de distribución, validador, sistema de pago y notificador necesitan comunicarse. Sin mediador, cada componente conoce a todos los demás → acoplamiento N².

### Solución
Centralizar la comunicación en un mediador. Los componentes solo conocen al mediador y le notifican eventos; el mediador decide qué llamar.

### Estructura
- **Interfaz MediadorEnvios**: `notificar(evento, datos)` y métodos de registro.
- **Mediador concreto**: implementa la lógica de routing. Por ejemplo, al recibir `ENVIO_CREADO` llama al validador; al recibir `VALIDACION_OK` llama al sistema de pago.
- **Componentes**: cada uno recibe el mediador en el constructor y llama `mediador.notificar(...)` en lugar de llamar directamente a otros componentes.

### Cuándo usar
Muchos objetos que necesitan comunicarse de forma compleja; el acoplamiento entre componentes es alto y difícil de mantener; querés centralizar lógica de orquestación.

### Desventajas
El mediador puede volverse un "objeto dios" complejo; punto único de fallo.

### Casos reales
Controladores MVC, servidores de chat (sala centralizada), torres de control de tráfico aéreo.

---

## Memento

### Problema
LogiSmart necesita guardar estados de un envío (CONFIRMADO → EN_TRANSITO → ENTREGADO) y poder restaurarlos sin exponer los internos del objeto.

### Solución
El originador (Envio) crea snapshots (Memento) de su estado. Un cuidador (HistorialEnvios) los almacena y puede pedir restauración sin conocer los detalles internos.

### Estructura
- **MementoEnvio**: almacena estado, origen, destino, peso, costo y timestamp. Solo el originador puede interpretarlo.
- **Envio (Originador)**: `crearMemento()` genera un snapshot; `restaurarDesdeMemento(memento)` restaura el estado.
- **HistorialEnvios (Cuidador)**: lista de mementos con `indiceActual`. `guardarEstado()` agrega al historial; `irAlEstadoAnterior()` e `irAlEstadoSiguiente()` navegan.

### Cuándo usar
Necesitás undo/redo; querés guardar historial de estados; necesitás checkpoints para recuperación de errores; no querés exponer internals del objeto.

### Desventajas
Puede consumir mucha memoria si los estados son grandes o frecuentes; más clases.

### Casos reales
Editores (Word, Photoshop undo/redo), snapshots de bases de datos, checkpoints, save/load en videojuegos.

---

## Observer

### Problema
Cuando un envío cambia de estado, múltiples objetos necesitan reaccionar: centro de distribución, notificador por email, sistema de auditoría, dashboard en tiempo real. ¿Cómo notificarlos sin acoplar el envío a cada uno?

### Solución
Define una relación uno-a-muchos. El sujeto (Envio) mantiene una lista de observadores y los notifica automáticamente al cambiar de estado.

### Estructura
- **Interfaz ObservadorEnvio**: `actualizar(Envio envio)`.
- **Sujeto (Envio)**: lista de observadores; `adjuntarObservador()`, `desadjuntarObservador()`, `notificarObservadores()` (privado, llamado en `cambiarEstado()`).
- **Observadores concretos**: `CentroDistribucionObservador`, `SistemaNotificacionObservador`, `SistemaAuditoriaObservador`, `DashboardObservador`. Cada uno reacciona a su manera.

### Cuándo usar
Un objeto cambia y otros necesitan reaccionar; no sabés a priori cuántos o cuáles objetos dependen; querés bajo acoplamiento.

### Desventajas
Orden de notificación no garantizado; observadores que no se desregistran pueden causar memory leaks; flujo de control difícil de seguir en debugging.

### Casos reales
Eventos GUI (listeners de botones), publicador-suscriptor (RabbitMQ, Kafka), patrones MVC/MVVM (cambio de modelo notifica vista), WebSockets.

---

## Arquitecturas Event-Driven

Observer es la base de event-driven architecture. La diferencia clave: en Observer el sujeto conoce a sus observadores (aunque sea por interfaz); en event-driven hay un bus de eventos intermedio que desacopla completamente productor de consumidor.

- **EventoEnvio**: tipo, envio y timestamp.
- **BusEventos**: lista de consumidores; `publicarEvento()` itera y llama `procesarEvento()` en cada uno.
- **ConsumidorEventos**: interfaz con `procesarEvento(EventoEnvio)`.

Ventajas adicionales: asincronía, auditabilidad completa (todos los eventos se registran), escalabilidad horizontal.

Casos reales: Kafka, RabbitMQ, AWS SNS/SQS, sistemas de IoT.

---

## Comparación

| Patrón | Pregunta clave | Estructura |
|--------|---------------|------------|
| Iterator | ¿Múltiples colecciones distintas? | Interfaz de recorrido uniforme |
| Mediator | ¿Comunicación compleja entre muchos objetos? | Hub central de comunicación |
| Memento | ¿Necesitás historial o undo/redo? | Snapshots del estado interno |
| Observer | ¿Cambios que deben notificarse a N objetos? | Suscripción uno-a-muchos |
