# DOCUMENTACION - Hito 11

## 1) Objetivo

Consolidar LogiSmart hasta Hito 11, incorporando los cuatro patrones GoF de comportamiento II (Iterator, Mediator, Memento, Observer) sobre la base acumulada de hitos anteriores.

## 2) Hitos cubiertos

- Hito 6: Singleton y Factory Method
- Hito 7: Abstract Factory, Builder y Prototype
- Hito 8: Adapter, Bridge y Composite
- Hito 9: Decorator, Facade, Flyweight y Proxy
- Hito 10: Chain of Responsibility, Command e Interpreter
- Hito 11: Iterator, Mediator, Memento y Observer

## 3) Hito 10 - Patrones implementados

### 3.1 Chain of Responsibility

Clases:

- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorDatos.java`
- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorInventario.java`
- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorPago.java`
- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorSeguridad.java`
- `src/com/logismart/infraestructura/comportamiento/chain/ValidadorCapacidad.java`
- `src/com/logismart/infraestructura/comportamiento/chain/CadenaValidadores.java`
- `src/com/logismart/infraestructura/comportamiento/chain/SistemaInventario.java`
- `src/com/logismart/infraestructura/comportamiento/chain/SistemaCapacidad.java`

Uso:

- valida envios con una cadena ordenada de reglas
- corta la ejecucion ante el primer rechazo
- permite agregar o reordenar validadores sin tocar el cliente

Decisiones:

- el orden es Datos -> Inventario -> Pago -> Seguridad -> Capacidad
- `CadenaValidadores` arma la cadena y oculta el detalle al caso de uso
- las validaciones baratas van primero para fallar rapido

### 3.2 Command

Clases:

- `src/com/logismart/infraestructura/comportamiento/command/Comando.java`
- `src/com/logismart/infraestructura/comportamiento/command/ColaComandos.java`
- `src/com/logismart/infraestructura/comportamiento/command/ServicioEnvios.java`
- `src/com/logismart/infraestructura/comportamiento/command/ComandoCrearEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/command/ComandoCancelarEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/command/ComandoActualizarEstado.java`
- `src/com/logismart/infraestructura/comportamiento/command/ComandoCambiarMetodoPago.java`
- `src/com/logismart/infraestructura/comportamiento/command/ComandoAgregarServicio.java`

Uso:

- encapsula operaciones sobre envios como objetos ejecutables
- mantiene historial con `undo` y `redo`
- separa invocador (`ColaComandos`) de receptor (`ServicioEnvios`)

Decisiones:

- los comandos guardan estado anterior cuando la operacion es reversible
- `ColaComandos` descarta el futuro si se ejecuta un comando nuevo despues de deshacer
- `ComandoCrearEnvio` expone el numero de seguimiento generado para pruebas e integracion

### 3.3 Interpreter

Clases:

- `src/com/logismart/infraestructura/comportamiento/interpreter/Expresion.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionOrigen.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionDestino.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionPeso.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionCosto.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionRestringido.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionAND.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionOR.java`
- `src/com/logismart/infraestructura/comportamiento/interpreter/ExpresionNOT.java`

Uso:

- define reglas de negocio evaluables sobre `Envio`
- combina expresiones terminales con operadores booleanos
- permite crear reglas compuestas sin condicionales grandes

Decisiones:

- la gramatica cubre origen, destino, peso, costo y destino restringido
- `AND`, `OR` y `NOT` forman arboles de expresiones recursivos
- las reglas se inicializan en `SistemaLogisticaCompleto` para evidenciar integracion

### 3.4 Integracion Hito 10

Clases:

- `src/com/logismart/aplicacion/hito10/SistemaLogisticaCompleto.java`
- `src/com/logismart/aplicacion/hito10/CasosDePruebaHito10.java`
- `src/com/logismart/dominio/Envio.java`

Uso:

- `SistemaLogisticaCompleto` valida con Chain, ejecuta creacion con Command y evalua reglas con Interpreter
- `Envio` agrega campos operativos para el hito: `costo`, `metodoPago` y `productoId`
- `Main` ejecuta Hitos 8, 9 y 10 dentro de la demo acumulada

## 4) Hito 9 - Patrones estructurales previos

- Decorator: `infraestructura/decorator/envio`, servicios opcionales sobre envios
- Facade: `ServicioLogisticaFacade`, interfaz simple para inventario, pagos, rastreo, notificaciones y reportes
- Flyweight: `Ubicacion` y `FabricaUbicaciones`, ubicaciones compartidas e inmutables
- Proxy: `ProxyRepositorioEnvios`, lazy loading, cache y validacion de acceso
- Integracion: `aplicacion/hito9/ServicioLogisticaCompleto`

## 5) Casos de prueba

Clase ejecutable principal:

- `src/com/logismart/aplicacion/hito10/CasosDePruebaHito10.java`

Cobertura Hito 10:

- Chain of Responsibility: 6 verificaciones
- Command: 10 verificaciones
- Interpreter: 10 verificaciones
- Integracion: 3 verificaciones

Total Hito 10:

- 29 verificaciones automaticas por excepcion si algo falla

Tambien quedan ejecutables:

- `src/com/logismart/aplicacion/hito8/CasosDePruebaHito8.java`
- `src/com/logismart/aplicacion/hito9/CasosDePruebaHito9.java`

## 6) Skills de clase

- Clase 1: `skills/pds-clase-01/SKILL.md` y `skills/pds-clase-01/REFERENCE.md`
- Clase 2: `skills/pds-clase-02/SKILL.md` y `skills/pds-clase-02/REFERENCE.md`
- Clase 3: `skills/pds-clase-03/SKILL.md` y `skills/pds-clase-03/REFERENCE.md`
- Clase 4: `skills/pds-clase-04/SKILL.md` y `skills/pds-clase-04/REFERENCE.md`
- Clase 5: `skills/pds-clase-05/SKILL.md` y `skills/pds-clase-05/REFERENCE.md`
- Clase 6: `skills/pds-clase-06/SKILL.md` y `skills/pds-clase-06/REFERENCE.md`
- Clase 7: `skills/pds-clase-07/SKILL.md` y `skills/pds-clase-07/REFERENCE.md`
- Clase 9: `skills/pds-clase-09/SKILL.md` y `skills/pds-clase-09/REFERENCE.md`
- Clase 10: `skills/pds-clase-10/SKILL.md` y `skills/pds-clase-10/REFERENCE.md`

Criterio aplicado:

- se parseo solo el contenido de `main-content`
- se removio ruido visual de sidebar, CSS y JS
- se preservaron secciones, tablas, listas y ejemplos tecnicos

## 3.5 Hito 11 - Patrones de Comportamiento II

### Iterator

Clases:

- `src/com/logismart/infraestructura/comportamiento/iterator/IteradorEnvios.java`
- `src/com/logismart/infraestructura/comportamiento/iterator/ColeccionEnvios.java`
- `src/com/logismart/infraestructura/comportamiento/iterator/ColeccionArray.java`
- `src/com/logismart/infraestructura/comportamiento/iterator/ColeccionLista.java`
- `src/com/logismart/infraestructura/comportamiento/iterator/ColeccionHash.java`

Uso:

- interfaz uniforme sobre Array, Lista enlazada y HashMap
- el cliente nunca accede a la estructura interna de la coleccion
- reiniciar() permite reutilizar el mismo iterador sin recrearlo

Decisiones:

- iteradores como clases internas para acceso directo a campos privados
- tres implementaciones muestran que el patron escala a cualquier estructura

### Mediator

Clases:

- `src/com/logismart/infraestructura/comportamiento/mediator/MediadorEnvios.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/MediadorEnviosConcreto.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/CentroDistribucion.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/ValidadorEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/SistemaPago.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/SistemaNotificacion.java`
- `src/com/logismart/infraestructura/comportamiento/mediator/SistemaAuditoria.java`

Uso:

- pipeline event-driven: ENVIO_CREADO -> VALIDACION_OK -> PAGO_CONFIRMADO -> NOTIFICACION_ENVIADA -> ENVIO_REGISTRADO
- VALIDACION_FALLIDA corta el pipeline antes del pago
- ningun componente referencia directamente a otro

Decisiones:

- eventos como String para maxima flexibilidad de extension
- SistemaAuditoria expone contarEventos(String) para tests precisos

### Memento

Clases:

- `src/com/logismart/dominio/MementoEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/memento/HistorialEnvios.java`
- Metodos en `src/com/logismart/dominio/Envio.java`: crearMemento(), restaurarDesdeMemento(), cambiarEstado()

Uso:

- HistorialEnvios guarda snapshots del ciclo de vida de un Envio
- navegacion bidireccional: irAlEstadoAnterior, irAlEstadoSiguiente, irAlEstado(int)
- guardar desde posicion intermedia descarta estados futuros (comportamiento tipo editor)

Decisiones:

- MementoEnvio vive en dominio (no en infraestructura) para evitar dependencia dominio->infraestructura
- trade-off documentado formalmente en HITO_11.html: DIP vs cohesion de patron GoF

### Observer

Clases:

- `src/com/logismart/dominio/ObservadorEnvio.java`
- `src/com/logismart/infraestructura/comportamiento/observer/CentroDistribucionObservador.java`
- `src/com/logismart/infraestructura/comportamiento/observer/SistemaNotificacionObservador.java`
- `src/com/logismart/infraestructura/comportamiento/observer/SistemaAuditoriaObservador.java`
- `src/com/logismart/infraestructura/comportamiento/observer/DashboardObservador.java`

Uso:

- Envio actua como Subject; adjuntar/desadjuntar observadores en runtime
- cambiarEstado(), iniciar() y cancelar() disparan notificaciones automaticas
- ObservadorEnvio es interfaz funcional: acepta lambdas directamente

Decisiones:

- ObservadorEnvio en dominio por DIP (ver trade-off en HITO_11.html)
- la lista de observadores arranca vacia: sin ruptura de pruebas de hitos anteriores

### Integracion Hito 11

Clases:

- `src/com/logismart/aplicacion/hito11/SistemaLogisticaEventDriven.java`
- `src/com/logismart/aplicacion/hito11/CasosDePruebaHito11.java`

Uso:

- SistemaLogisticaEventDriven activa los 4 patrones en cada ciclo de procesamiento
- Main ejecuta CasosDePruebaHito11 dentro de la demo acumulada

## 7) Entregables visuales

- `index.html`
- `hitos/HITO_8.html`
- `hitos/HITO_9.html`
- `hitos/HITO_10.html`
- `hitos/HITO_11.html`
- `DIAGRAMA_DE_CLASES_ACTUAL.html`

## 8) Ejecucion

Compilar:

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

Ejecutar demo acumulada:

```bash
java -cp bin com.logismart.app.Main
```

## 9) Riesgos y limites

- las pruebas son ejecutables por consola, no usan framework unitario externo
- inventario, capacidad y persistencia son simulaciones en memoria
- Interpreter cubre una gramatica acotada al dominio del hito, no un parser textual completo
- el diagrama acumulativo prioriza claridad conceptual sobre detalle exhaustivo de helpers internos
