# LogiSmart — TPO Proceso de Desarrollo de Software · UADE 2026

Plataforma SaaS de logística para PyMEs. Implementación acumulada de 13 hitos.

## Alcance final — 36 patrones

| Familia | Patrones | Hitos |
|---|---|---|
| GRASP | Expert · Creator · Controller · Low Coupling · High Cohesion · Polymorphism · Pure Fabrication · Indirection · Protected Variations | 4–5 |
| GoF Creacionales | Singleton · Factory Method · Abstract Factory · Builder · Prototype | 6–7 |
| GoF Estructurales | Adapter · Bridge · Composite · Decorator · Facade · Flyweight · Proxy | 8–9 |
| GoF Comportamiento | Chain of Responsibility · Command · Interpreter · Iterator · Mediator · Memento · Observer · State · Strategy · Template Method · Visitor | 10–12 |
| Persistencia (PoEAA) | Repository · Data Mapper · Unit of Work · Lazy Load | 13 |

## Requisitos

- Java 17+
- Terminal con `javac` y `java`

## Estructura del proyecto

```
src/com/logismart/
├── app/                        ← punto de entrada (Main.java)
├── aplicacion/                 ← orquestación de CUs (Controller, Facade, Services)
│   ├── hito8/ … hito13/       ← demos y casos de prueba por hito
├── dominio/                    ← entidades y reglas de negocio
│   ├── usuario/                ← Usuario, AdminEmpresa, AdminPlataforma,
│   │                              ClienteFinal, OperadorLogistico,
│   │                              Transportista, IPermisos, Rol
│   ├── envio/                  ← Envio, Orden, Entrega, SeguimientoEnvio,
│   │                              MementoEnvio, ObservadorEnvio
│   ├── vehiculo/               ← Vehiculo, Auto, Camion, Moto, Flota
│   ├── ruta/                   ← Ruta, PuntoEntrega, PosicionGPS, ETA
│   └── empresa/                ← Empresa, Suscripcion, Cobro, Metrica, Reporte
└── infraestructura/
    ├── abstractfactory/        ← FactoryArgentina, FactoryBrasil
    ├── adapter/envio|pago/     ← DHL, FedEx, UPS · PayPal, Stripe
    ├── bridge/reporte/         ← PDF, Excel, JSON, CSV
    ├── comportamiento/         ← chain, command, interpreter, iterator,
    │                              mediator, memento, observer, state,
    │                              strategy, template, visitor
    ├── composite/centro/       ← red de centros de distribución
    ├── costo/                  ← estrategias de cálculo de costo
    ├── decorator/envio/        ← Seguro, GPS, Prioritario, SMS
    ├── fabrica/                ← Factory Methods de dominio
    ├── flyweight/              ← Ubicaciones compartidas
    ├── persistencia/           ← repositorio, mapper, unitofwork, lazy
    ├── proxy/envio/            ← caché + lazy loading
    ├── ruta/                   ← algoritmos de selección de ruta
    ├── singleton/              ← ConexionBD, Logger
    ├── tiempo/                 ← calculadores de ETA
    └── vehiculo/               ← asignadores de vehículo
```

## Compilar

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

O con `find` (sistemas sin ripgrep):

```bash
find src -name "*.java" | xargs javac -d bin
```

## Ejecutar demo acumulada

```bash
java -cp bin com.logismart.app.Main
```

La demo ejecuta en secuencia:

1. Singleton (Logger, ConexionBD)
2. Abstract Factory regional (Argentina / Brasil)
3. Factory Method (vehículos, notificadores)
4. Builder + Prototype (Envio)
5. Procesamiento regional
6. Casos Hito 8 (Adapter, Bridge, Composite)
7. Casos Hito 9 (Decorator, Facade, Flyweight, Proxy)
8. Casos Hito 10 (Chain, Command, Interpreter)
9. Casos Hito 11 (Iterator, Mediator, Memento, Observer)
10. Casos Hito 12 (State, Strategy, Template Method, Visitor)
11. Casos Hito 13 (Repository, Data Mapper, Unit of Work, Lazy Load)

## Decisiones de diseño destacadas

- **Multitenant:** permisos resueltos con `Rol` enum + `IPermisos`. El `LogiSmartController` valida antes de llegar al dominio — el dominio queda testeable en aislamiento.
- **Dominio sin persistencia:** `Envio` no tiene `guardarEnBaseDatos()`. El Repository define el contrato, la infraestructura lo implementa.
- **Builder reutilizado:** el `Envio.EnvioBuilder` diseñado en el Hito 7 es el mismo que usa `EnvioMapperSQL` en el Hito 13 para reconstruir entidades desde SQL.
- **Abstract Factory regional:** `FactoryArgentina` y `FactoryBrasil` garantizan coherencia de objetos por país (IVA 21% vs ICMS 12%, Google Maps vs HERE Maps).

## Refactoring post-Hito 12

Cambios de diseño aplicados al dominio (148/148 tests en verde):

- `Rol.java` (enum nuevo): centraliza la matriz de permisos 5×5, elimina 25 booleans duplicados en subclases de `Usuario`
- `PosicionGPS.haversineKm()`: reemplaza fórmula euclidiana por cálculo real sobre la esfera terrestre
- `Vehiculo.getCostoBaseKm()`: Information Expert — el vehículo conoce su propio costo operativo
- `Ruta.optimizar()`: ordena paradas por `ordenParada` y recalcula distancia real

## Reorganización del dominio (post-Hito 13)

Las 28 clases de `com.logismart.dominio` fueron agrupadas en subpaquetes semánticos:

| Subpaquete | Clases |
|---|---|
| `dominio.usuario` | Usuario, AdminEmpresa, AdminPlataforma, ClienteFinal, OperadorLogistico, Transportista, IPermisos, Rol |
| `dominio.envio` | Envio, Orden, Entrega, SeguimientoEnvio, MementoEnvio, ObservadorEnvio |
| `dominio.vehiculo` | Vehiculo, Auto, Camion, Moto, Flota |
| `dominio.ruta` | Ruta, PuntoEntrega, PosicionGPS, ETA |
| `dominio.empresa` | Empresa, Suscripcion, Cobro, Metrica, Reporte |

## Entregables

- Código Java compilable — 36 patrones, 148 tests en verde
- `hitos/HITO_10.html` — Hito 10 (Chain, Command, Interpreter)
- `hitos/HITO_11.html` — Hito 11 (Iterator, Mediator, Memento, Observer)
- `hitos/HITO_12.html` — Hito 12 (State, Strategy, Template Method, Visitor)
- `hitos/HITO_13.html` — Hito 13 (Persistencia)
- `DIAGRAMA_DE_CLASES_ACTUAL.html` — diagrama acumulativo
- `presentacion/` — slides de defensa y notas de estudio
- `DOCUMENTACION.md` — decisiones y cobertura de hitos 6 a 13

## Equipo

Rosario Presedo (LU 1146894) · Simón Ottati (LU 1155931) · Comisión Belgrano · UADE 2026
