# DOCUMENTACION - Hito 7

## 1) Objetivo

Integrar patrones creacionales avanzados en LogiSmart para mejorar:

- creacion de familias de objetos por region
- construccion de objetos complejos
- clonacion eficiente de objetos similares

## 2) Patrones implementados

### 2.1 Singleton

Clases:

- `src/com/logismart/infraestructura/singleton/Logger.java`
- `src/com/logismart/infraestructura/singleton/ConfiguracionSistema.java`

Uso:

- instancia unica de log
- configuracion global compartida por region/version

### 2.2 Factory Method

Clases:

- `src/com/logismart/infraestructura/fabrica/UsuarioFactory.java`
- `src/com/logismart/infraestructura/fabrica/VehiculoFactory.java`
- `src/com/logismart/infraestructura/fabrica/FabricaDeVehiculos.java`

Uso:

- creacion de usuarios por tipo (`cliente`, `operador`, `admin`)
- creacion de vehiculos por tipo textual o enum

### 2.3 Abstract Factory

Contrato:

- `src/com/logismart/infraestructura/abstractfactory/LogiSmartFactory.java`

Fabricas concretas:

- `LogiSmartFactoryArgentina`
- `LogiSmartFactoryBrasil`

Productos abstractos:

- `Vehiculo`
- `CalculadorCostos`
- `ProveedorMapas`

Productos concretos por region:

- Argentina: `Auto`, `CalculadorCostosArgentina`, `GoogleMapsArgentina`
- Brasil: `Moto`, `CalculadorCostosBrasil`, `HereMaps`

Resultado:

- `LogiSmartApp` queda desacoplada de clases concretas regionales
- coherencia de familia de objetos por pais

### 2.4 Builder

Clase:

- `src/com/logismart/dominio/Envio.java` (`Envio.EnvioBuilder` interno)

Decisiones:

- constructor privado de `Envio` para flujo controlado
- campos requeridos en constructor del builder (`id`, `origen`, `destino`)
- campos opcionales con metodos fluidos (`return this`)

Beneficio:

- evita explosion de constructores
- mejora legibilidad de objetos complejos

### 2.5 Prototype

Clase:

- `src/com/logismart/dominio/Envio.java`

Implementacion:

- `Envio` implementa `Cloneable`
- `clone()` usa `super.clone()`
- copia nueva de `ordenes` para no compartir referencia mutable

Beneficio:

- creacion masiva de envios a bajo costo

## 3) Integracion de patrones

Punto de orquestacion del hito:

- `src/com/logismart/app/LogiSmartApp.java`

Demostracion completa:

- `src/com/logismart/app/Main.java`

Flujo principal:

1. obtener singletons (`Logger`, `ConfiguracionSistema`)
2. crear app regional (seleccion de abstract factory)
3. crear usuarios por factory method
4. crear envio simple/complex por builder
5. clonar 100 envios por prototype
6. procesar envio con productos de abstract factory

## 4) Manejo de errores y validaciones

- region desconocida -> `IllegalArgumentException`
- tipo de usuario desconocido -> `IllegalArgumentException`
- tipo de vehiculo desconocido -> `IllegalArgumentException`

## 5) Diagramas y soporte visual

- `diagramas/diagrama_abstract_factory.mmd`
- `diagramas/diagrama_builder.mmd`
- `diagramas/diagrama_prototype.mmd`
- `hitos/HITO_7.html` (entregable visual del hito)

## 6) Ejecucion

Compilar:

```bash
rg --files src -g "*.java" -0 | xargs -0 javac -d bin
```

Ejecutar demo:

```bash
java -cp bin com.logismart.app.Main
```
