# LogiSmart - Hito 10

Implementacion acumulada del TPO LogiSmart hasta Hito 10.

## Alcance actual

- Hito 6: Singleton y Factory Method
- Hito 7: Abstract Factory, Builder y Prototype
- Hito 8: Adapter, Bridge y Composite
- Hito 9: Decorator, Facade, Flyweight y Proxy
- Hito 10: Chain of Responsibility, Command e Interpreter

## Requisitos

- Java 17+
- Terminal con `javac` y `java`

## Estructura relevante

- `src/com/logismart/app/Main.java`
- `src/com/logismart/aplicacion/hito9/`
- `src/com/logismart/aplicacion/hito10/`
- `src/com/logismart/infraestructura/decorator/envio/`
- `src/com/logismart/infraestructura/flyweight/ubicacion/`
- `src/com/logismart/infraestructura/proxy/envio/`
- `src/com/logismart/infraestructura/comportamiento/`
- `skills/pds-clase-01/`
- `skills/pds-clase-02/`
- `skills/pds-clase-03/`
- `skills/pds-clase-04/`
- `skills/pds-clase-05/`
- `skills/pds-clase-06/`
- `skills/pds-clase-07/`
- `skills/pds-clase-09/`
- `skills/pds-clase-10/`
- `hitos/HITO_8.html`
- `hitos/HITO_9.html`
- `hitos/HITO_10.html`
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

## Entregables incluidos

- Codigo Java compilable y funcional
- Skills locales de Clase 1 a 7, 9 y 10 en `skills/`
- Documentos visuales `hitos/HITO_8.html`, `hitos/HITO_9.html` y `hitos/HITO_10.html`
- Diagrama acumulativo actualizado
- `DOCUMENTACION.md` con decisiones y cobertura de hitos 6 a 10
