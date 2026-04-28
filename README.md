# LogiSmart - Hito 7

Implementacion de patrones creacionales avanzados sobre el proyecto LogiSmart.

## Alcance del hito

- Abstract Factory
- Builder
- Prototype
- Integracion con Singleton y Factory Method (hitos previos)

## Requisitos

- Java 17+ recomendado
- Terminal con `javac` y `java`

## Estructura relevante

- `src/com/logismart/app/Main.java`
- `src/com/logismart/app/LogiSmartApp.java`
- `src/com/logismart/dominio/Envio.java`
- `src/com/logismart/infraestructura/abstractfactory/`
- `src/com/logismart/infraestructura/fabrica/`
- `src/com/logismart/infraestructura/singleton/`
- `diagramas/`
- `hitos/HITO_7.html`

## Compilar

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

## Ejecutar demo del hito

```bash
java -cp bin com.logismart.app.Main
```

La demo ejecuta en orden:

1. Singleton
2. Abstract Factory
3. Factory Method
4. Builder
5. Prototype
6. Integracion final

## Entregables incluidos

- Codigo Java compilable y funcional
- Diagramas Mermaid (`diagramas/*.mmd`)
- Documento visual del hito (`hitos/HITO_7.html`)
- `DOCUMENTACION.md` con decisiones y detalles tecnicos
