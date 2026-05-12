# LogiSmart - Hito 9

Implementacion acumulada del TPO LogiSmart hasta Hito 9.

## Alcance actual

- Hito 6: Singleton y Factory Method
- Hito 7: Abstract Factory, Builder y Prototype
- Hito 8: Adapter, Bridge y Composite
- Hito 9: Decorator, Facade, Flyweight y Proxy

## Requisitos

- Java 17+
- Terminal con `javac` y `java`

## Estructura relevante

- `src/com/logismart/app/Main.java`
- `src/com/logismart/aplicacion/hito9/`
- `src/com/logismart/infraestructura/decorator/envio/`
- `src/com/logismart/infraestructura/flyweight/ubicacion/`
- `src/com/logismart/infraestructura/proxy/envio/`
- `skills/pds-clase-10/`
- `hitos/HITO_8.html`
- `hitos/HITO_9.html`
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
7. Casos de Hito 9

## Entregables incluidos

- Codigo Java compilable y funcional
- Skill local de Clase 10 en `skills/pds-clase-10/`
- Documento visual `hitos/HITO_9.html`
- Diagrama acumulativo actualizado
- `DOCUMENTACION.md` con decisiones y cobertura de hitos 6 a 9
