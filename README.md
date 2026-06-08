# LogiSmart — TPO Proceso de Desarrollo de Software · UADE 2026

Plataforma SaaS de logística para PyMEs, desarrollada en Java como trabajo práctico obligatorio. El proyecto se construyó de forma acumulada a lo largo de 13 hitos, aplicando 36 patrones de diseño (GRASP, GoF y persistencia).

## Requisitos

- Java 17+
- Terminal con `javac` y `java`

## Cómo compilar y ejecutar

```bash
# Compilar
find src -name "*.java" | xargs javac -d bin

# Ejecutar la demo acumulada (recorre los patrones de todos los hitos)
java -cp bin com.logismart.app.Main
```

## Estructura del repositorio

```
LogiSmartTPO/
├── src/                            ← código fuente Java
├── bin/                            ← clases compiladas (generado)
├── hitos/                          ← entregables por hito (HITO_N.html + consignas/)
├── diagramas/                      ← diagramas UML por hito
├── presentacion/                   ← slides de defensa y notas de estudio
├── DIAGRAMA_DE_CLASES_ACTUAL.html  ← diagrama de clases completo (fuente de verdad)
├── DOCUMENTACION.md                ← decisiones de diseño y detalle de hitos 6-13
└── index.html                      ← página de presentación del TPO
```

### Código fuente (`src/com/logismart/`)

```
├── app/              ← punto de entrada (Main.java)
├── aplicacion/       ← orquestación de casos de uso (Controller, Facade, Services)
│   └── hito8/ … hito13/   ← demos y casos de prueba por hito
├── dominio/          ← entidades y reglas de negocio, sin dependencias de infraestructura
│   ├── usuario/      ← Usuario y subtipos, permisos, roles
│   ├── envio/        ← Envio, Orden, Entrega, Seguimiento
│   ├── vehiculo/     ← Vehiculo, Auto, Camion, Moto, Flota
│   ├── ruta/         ← Ruta, PuntoEntrega, PosicionGPS, ETA
│   └── empresa/      ← Empresa, Suscripcion, Cobro, Metrica, Reporte
└── infraestructura/  ← implementaciones técnicas, organizadas por categoría
    ├── estructural/  ← patrones creacionales y estructurales GoF
    ├── comportamiento/ ← patrones de comportamiento GoF
    └── persistencia/   ← patrones de acceso a datos (PoEAA)
```

## Dónde encontrar cada patrón

| Familia | Patrones | Hitos | Ubicación principal |
|---|---|---|---|
| GRASP | Expert · Creator · Controller · Low Coupling · High Cohesion · Polymorphism · Pure Fabrication · Indirection · Protected Variations | 4–5 | transversal a `dominio/` y `aplicacion/` |
| GoF Creacionales | Singleton · Factory Method · Abstract Factory · Builder · Prototype | 6–7 | `infraestructura/estructural/singleton/`, `fabrica/`, `abstractfactory/` · Builder y Prototype en `dominio/envio/Envio.java` |
| GoF Estructurales | Adapter · Bridge · Composite · Decorator · Facade · Flyweight · Proxy | 8–9 | `infraestructura/estructural/adapter/`, `bridge/`, `composite/`, `decorator/`, `flyweight/`, `proxy/` · Facade en `aplicacion/` |
| GoF Comportamiento | Chain of Responsibility · Command · Interpreter · Iterator · Mediator · Memento · Observer · State · Strategy · Template Method · Visitor | 10–12 | `infraestructura/comportamiento/` |
| Persistencia (PoEAA) | Repository · Data Mapper · Unit of Work · Lazy Load | 13 | `infraestructura/persistencia/` |

## Cómo navegar el proyecto

- **Para entender el diseño completo:** abrir `DIAGRAMA_DE_CLASES_ACTUAL.html` en el navegador.
- **Para ver qué se hizo en cada hito:** abrir `hitos/HITO_N.html` (cada uno incluye explicación, código y diagramas de ese hito). Las consignas originales están en `hitos/consignas/`.
- **Para entender las decisiones de diseño:** leer `DOCUMENTACION.md`.
- **Para ver los patrones funcionando:** ejecutar la demo (`Main.java`), que recorre en secuencia los casos de prueba de los hitos 6 a 13.

## Equipo

Rosario Presedo (LU 1146894) · Simón Ottati (LU 1155931) · Comisión Belgrano · UADE 2026
