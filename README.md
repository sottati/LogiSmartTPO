# LogiSmart - Hito 12

Implementacion acumulada del TPO LogiSmart hasta Hito 12.

## Alcance actual

- Hito 6: Singleton y Factory Method
- Hito 7: Abstract Factory, Builder y Prototype
- Hito 8: Adapter, Bridge y Composite
- Hito 9: Decorator, Facade, Flyweight y Proxy
- Hito 10: Chain of Responsibility, Command e Interpreter
- Hito 11: Iterator, Mediator, Memento, Observer
- Hito 12: State, Strategy, Template Method, Visitor

Total: 23 patrones GoF acumulados.

## Requisitos

- Java 17+
- Terminal con `javac` y `java`

## Estructura relevante

- `src/com/logismart/app/Main.java`
- `src/com/logismart/aplicacion/hito10/`
- `src/com/logismart/aplicacion/hito11/`
- `src/com/logismart/aplicacion/hito12/`
- `src/com/logismart/infraestructura/comportamiento/`
- `src/com/logismart/infraestructura/decorator/envio/`
- `src/com/logismart/infraestructura/flyweight/ubicacion/`
- `src/com/logismart/infraestructura/proxy/envio/`
- `hitos/HITO_10.html`
- `hitos/HITO_11.html`
- `hitos/HITO_12.html`
- `DIAGRAMA_DE_CLASES_ACTUAL.html`

## Compilar

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

## Ejecutar demo acumulada

```bash
java -cp bin com.logismart.app.Main
```

La demo ejecuta:

1. Singleton
2. Abstract Factory
3. Factory Method
4. Builder
5. Prototype
6. Procesamiento regional
7. Casos de Hito 8
8. Casos de Hito 9
9. Casos de Hito 10
10. Casos de Hito 11
11. Casos de Hito 12

## Refactoring post-Hito 12

Correcciones de diseño aplicadas al dominio tras completar el Hito 12 (148/148 tests en verde):

- `Rol.java` (nuevo enum): centraliza la matriz de permisos 5×5, elimina 25 booleans duplicados en subclases
- `PosicionGPS`: `distanciaA()` corregida a Haversine; nuevo método estático `haversineKm()`
- `Vehiculo`: nuevo `getCostoBaseKm()` (Information Expert)
- `Ruta`: usa `PosicionGPS.haversineKm()` y `vehiculo.getCostoBaseKm()`; `optimizar()` implementado
- `SeguimientoEnvio`: constructor delegation via `this(...)`
- `Entrega`: `pruebaAdjunta` inicializada en ambos constructores
- Subclases de `Usuario`: IPermisos delegado a `Rol`, stubs implementados, `saludar()` completo

## Entregables incluidos

- Codigo Java compilable y funcional
- Documentos visuales `hitos/HITO_10.html`, `hitos/HITO_11.html` y `hitos/HITO_12.html`
- Diagrama acumulativo `DIAGRAMA_DE_CLASES_ACTUAL.html`
- `DOCUMENTACION.md` con decisiones y cobertura de hitos 6 a 12 y refactoring
