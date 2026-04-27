# Hito 7 : Patrones Creacionales II - Abstract

# Factory, Builder, Prototype

## Introducción

#### En este hito, implementarás los patrones creacionales avanzados (Abstract Factory, Builder,

#### Prototype) para tu sistema LogiSmart. Este hito se construye sobre los Hitos 5 y 6 ,

#### integrando todo lo que has aprendido sobre GRASP y patrones creacionales básicos.

#### Duración: 90 minutos

#### Entregable: Proyecto Eclipse con código Java funcional + Documentación

## Contexto: Integración de Hitos 5 , 6 y 7

## Progresión

#### Hito 5 (GRASP): Diseñaste responsabilidades correctas

#### Hito 6 (Singleton + Factory Method): Implementaste patrones básicos

#### Hito 7 (Abstract Factory + Builder + Prototype): Implementas patrones avanzados

## Lo que Debes Tener del Hito 6

#### Antes de comenzar este hito, asegúrate de tener:

## • ✅ Clase ConfiguracionSistema.java (Singleton)

## • ✅ Clase Logger.java (Singleton)

## • ✅ Interfaz Vehiculo.java + Clases Auto, Moto, Camion + VehiculoFactory.java

## • ✅ Interfaz Usuario.java + Clases Cliente, Operador, Admin + UsuarioFactory.java

## • ✅ Proyecto compilable y funcional

## Actividades del Hito 7

## Actividad 1 : Diseñar Abstract Factory ( 15 minutos)

#### Objetivo: Crear una Abstract Factory que genere familias de objetos para diferentes

#### regiones.

#### Qué hacer:


#### 1. Analiza las diferencias entre Argentina y Brasil:

- Tipos de vehículos disponibles
- Cálculo de costos (impuestos, distancias, etc.)
- Proveedores de mapas disponibles
- Regulaciones locales

#### 2. Crea la interfaz LogiSmartFactory:

```
Java
```
```
public interface LogiSmartFactory {
Vehiculo crearVehiculo();
CalculadorCostos crearCalculadorCostos();
ProveedorMapas crearProveedorMapas();
}
```
#### 1. Crea dos implementaciones:

- LogiSmartFactoryArgentina
- LogiSmartFactoryBrasil

#### Código Esperado:

```
Java
```
```
public class LogiSmartFactoryArgentina implements LogiSmartFactory {
@Override
public Vehiculo crearVehiculo() {
return new Auto(); // Vehículo típico de Argentina
}
```
```
@Override
public CalculadorCostos crearCalculadorCostos() {
return new CalculadorCostosArgentina();
}
```
```
@Override
public ProveedorMapas crearProveedorMapas() {
return new GoogleMapsArgentina();
}
}
```
```
public class LogiSmartFactoryBrasil implements LogiSmartFactory {
@Override
public Vehiculo crearVehiculo() {
return new Moto(); // Vehículo típico de Brasil
```

##### }

```
@Override
public CalculadorCostos crearCalculadorCostos() {
return new CalculadorCostosBrasil();
}
```
```
@Override
public ProveedorMapas crearProveedorMapas() {
return new HereMaps();
}
}
```
#### Entregable:

- Interfaz LogiSmartFactory.java
- Clase LogiSmartFactoryArgentina.java
- Clase LogiSmartFactoryBrasil.java
- Clases concretas: CalculadorCostosArgentina, CalculadorCostosBrasil,

#### GoogleMapsArgentina, HereMaps, etc.

#### Criterios de Evaluación:

- ✅ Interfaz bien definida (^15 %)
- ✅ Implementaciones coherentes (^40 %)
- ✅ Clases concretas completas (^30 %)
- ✅ Código compilable (^15 %)

### Actividad 2 : Implementar Abstract Factory ( 15 minutos)

#### Objetivo: Crear un LogiSmartApp que use la Abstract Factory.

#### Qué hacer:

#### 1. Crea la clase LogiSmartApp que reciba una región

#### 2. Crea la factory correspondiente

#### 3. Usa los objetos creados por la factory

#### Código Esperado:

```
Java
```
```
public class LogiSmartApp {
private LogiSmartFactory factory;
```

```
private Vehiculo vehiculo;
private CalculadorCostos calculador;
private ProveedorMapas mapas;
private Logger logger;
```
```
public LogiSmartApp(String region) {
this.logger = Logger.getInstance();
```
```
if (region.equalsIgnoreCase("Argentina")) {
this.factory = new LogiSmartFactoryArgentina();
} else if (region.equalsIgnoreCase("Brasil")) {
this.factory = new LogiSmartFactoryBrasil();
} else {
throw new IllegalArgumentException("Región no soportada: " + region
}
```
```
this.vehiculo = factory.crearVehiculo();
this.calculador = factory.crearCalculadorCostos();
this.mapas = factory.crearProveedorMapas();
```
```
logger.info("LogiSmartApp inicializado para: " + region);
}
```
```
public void procesarEnvio(String origen, String destino, double peso) {
logger.info("Procesando envío de " + origen + " a " + destino);
```
```
vehiculo.conducir();
double costo = calculador.calcularCosto(peso);
mapas.obtenerRuta(origen, destino);
```
```
logger.info("Costo calculado: $" + costo);
}
}
```
#### Entregable:

- Clase LogiSmartApp.java completamente implementada

#### Criterios de Evaluación:

- ✅ Uso correcto de la factory (^40 %)
- ✅ Lógica de negocio implementada (^30 %)
- ✅ Manejo de errores (^15 %)
- ✅ Código compilable (^15 %)

### Actividad 3 : Diseñar EnvioBuilder ( 15 minutos)


#### Objetivo: Crear un Builder para la clase Envio con múltiples atributos opcionales.

#### Qué hacer:

#### 1. Modifica la clase Envio para que tenga un constructor privado

#### 2. Crea la clase EnvioBuilder anidada

#### 3. Implementa todos los atributos como opcionales

#### Código Esperado:

```
Java
```
```
public class Envio {
private String id;
private String origen;
private String destino;
private String descripcion;
private double peso;
private boolean fragil;
private boolean requiereSignatura;
private boolean requiereRefrigeracion;
private boolean requiereAseguranza;
private String instruccionesEspeciales;
private String contactoEmergencia;
private LocalTime horaEntregaPreferida;
```
```
// Constructor privado
private Envio(EnvioBuilder builder) {
this.id = builder.id;
this.origen = builder.origen;
this.destino = builder.destino;
this.descripcion = builder.descripcion;
this.peso = builder.peso;
this.fragil = builder.fragil;
this.requiereSignatura = builder.requiereSignatura;
this.requiereRefrigeracion = builder.requiereRefrigeracion;
this.requiereAseguranza = builder.requiereAseguranza;
this.instruccionesEspeciales = builder.instruccionesEspeciales;
this.contactoEmergencia = builder.contactoEmergencia;
this.horaEntregaPreferida = builder.horaEntregaPreferida;
}
```
```
// Builder anidado
public static class EnvioBuilder {
private String id;
private String origen;
private String destino;
private String descripcion = "";
```

```
private double peso = 0 ;
private boolean fragil = false;
private boolean requiereSignatura = false;
private boolean requiereRefrigeracion = false;
private boolean requiereAseguranza = false;
private String instruccionesEspeciales = "";
private String contactoEmergencia = "";
private LocalTime horaEntregaPreferida = null;
```
```
public EnvioBuilder(String id, String origen, String destino) {
this.id = id;
this.origen = origen;
this.destino = destino;
}
```
```
public EnvioBuilder descripcion(String descripcion) {
this.descripcion = descripcion;
return this;
}
```
```
public EnvioBuilder peso(double peso) {
this.peso = peso;
return this;
}
```
```
public EnvioBuilder fragil(boolean fragil) {
this.fragil = fragil;
return this;
}
```
```
// ... más métodos
```
```
public Envio build() {
return new Envio(this);
}
}
```
```
// Getters
public String getId() { return id; }
public String getOrigen() { return origen; }
public String getDestino() { return destino; }
// ... más getters
}
```
#### Entregable:

- Clase Envio.java con EnvioBuilder anidado


#### Criterios de Evaluación:

- ✅ Constructor privado (^15 %)
- ✅ Builder con todos los atributos (^40 %)
- ✅ Métodos fluidos (return this) (^20 %)
- ✅ Método build() correcto (^15 %)
- ✅ Código compilable (^10 %)

### Actividad 4 : Implementar EnvioBuilder ( 15 minutos)

#### Objetivo: Crear ejemplos de uso del Builder con casos simples y complejos.

#### Qué hacer:

#### 1. Crea un envío simple (solo atributos requeridos)

#### 2. Crea un envío complejo (con múltiples atributos opcionales)

#### 3. Demuestra la ventaja del Builder sobre múltiples constructores

#### Código Esperado:

```
Java
```
```
// Uso simple
Envio envio1 = new Envio.EnvioBuilder("ENV001", "Buenos Aires", "Córdoba")
.build();
```
```
// Uso complejo
Envio envio2 = new Envio.EnvioBuilder("ENV002", "Buenos Aires", "Mendoza")
.descripcion("Medicinas urgentes")
.peso(2.5)
.fragil(true)
.requiereSignatura(true)
.requiereRefrigeracion(true)
.requiereAseguranza(true)
.instruccionesEspeciales("Mantener en frío entre 2-8°C")
.contactoEmergencia("Dr. García: 555-1234")
.horaEntregaPreferida(LocalTime.of( 14 , 0 ))
.build();
```
```
// Ventaja: No necesitas 2^n constructores diferentes
// Con Builder, solo necesitas un constructor privado y métodos fluidos
```
#### Entregable:

- Ejemplos de uso en Main.java


- Documentación explicando ventajas del Builder

#### Criterios de Evaluación:

- ✅ Ejemplos simples y complejos (^40 %)
- ✅ Uso correcto de métodos fluidos (^30 %)
- ✅ Documentación clara (^20 %)
- ✅ Código compilable (^10 %)

### Actividad 5 : Implementar Prototype ( 15 minutos)

#### Objetivo: Implementar Cloneable en Envio para crear copias eficientemente.

#### Qué hacer:

#### 1. Implementa Cloneable en la clase Envio

#### 2. Implementa el método clone() correctamente

#### 3. Crea ejemplos de clonación para crear múltiples envíos

#### Código Esperado:

```
Java
```
```
public class Envio implements Cloneable {
// ... atributos y métodos anteriores
```
```
@Override
public Envio clone() {
try {
Envio clonado = (Envio) super.clone();
// Si hubiera atributos complejos, hacer deep copy aquí
return clonado;
} catch (CloneNotSupportedException e) {
throw new RuntimeException("Error al clonar Envio", e);
}
}
}
```
```
// Uso
Envio prototipo = new Envio.EnvioBuilder("ENV-PROTO", "Buenos Aires", "Córdoba"
.descripcion("Medicinas")
.peso(2.5)
.fragil(true)
.build();
```
```
List<Envio> envios = new ArrayList<>();
```

```
for (int i = 0 ; i < 100 ; i++) {
Envio clon = prototipo.clone();
clon.setId("ENV-" + ( 1000 + i));
envios.add(clon);
}
```
#### Entregable:

- Clase Envio.java con Cloneable implementado
- Ejemplos de clonación en Main.java

#### Criterios de Evaluación:

- ✅ Implementación de Cloneable (^20 %)
- ✅ Método clone() correcto (^30 %)
- ✅ Ejemplos de uso (^30 %)
- ✅ Código compilable (^20 %)

### Actividad 6 : Integración Completa ( 15 minutos)

#### Objetivo: Integrar todos los patrones (Hitos 5 , 6 y 7 ) en un LogiSmartController.

#### Qué hacer:

#### 1. Crea LogiSmartController que use:

- Singleton (Logger, ConfiguracionSistema)
- Factory Method (VehiculoFactory, UsuarioFactory)
- Abstract Factory (LogiSmartFactory)
- Builder (EnvioBuilder)
- Prototype (Envio.clone())

#### 2. Crea un Main.java que demuestre todo funcionando

#### Código Esperado:

```
Java
```
```
public class LogiSmartController {
private LogiSmartFactory factory;
private Logger logger;
private ConfiguracionSistema config;
private List<Envio> envios;
private List<Usuario> usuarios;
```
```
public LogiSmartController(String region) {
```

this.logger = Logger.getInstance();
this.config = ConfiguracionSistema.getInstance();
this.factory = crearFactory(region);
this.envios = new ArrayList<>();
this.usuarios = new ArrayList<>();

logger.info("LogiSmartController inicializado para región: " + region);
}

private LogiSmartFactory crearFactory(String region) {
if (region.equalsIgnoreCase("Argentina")) {
return new LogiSmartFactoryArgentina();
} else if (region.equalsIgnoreCase("Brasil")) {
return new LogiSmartFactoryBrasil();
}
throw new IllegalArgumentException("Región desconocida: " + region);
}

_// Crear envío individual con Builder_
public void crearEnvio(String origen, String destino) {
Envio envio = new Envio.EnvioBuilder("ENV-" + envios.size(), origen, des
.descripcion("Envío estándar")
.build();
envios.add(envio);
logger.info("Envío creado: " + envio.getId());
}

_// Crear múltiples envíos con Prototype_
public void crearEnviosMultiples(int cantidad) {
Envio prototipo = new Envio.EnvioBuilder("PROTO", "Buenos Aires", "Córdo
.descripcion("Estándar")
.peso(1.0)
.build();

for (int i = 0 ; i < cantidad; i++) {
Envio clon = prototipo.clone();
clon.setId("ENV-" + envios.size());
envios.add(clon);
}
logger.info("Se crearon " + cantidad + " envíos por clonación");
}

_// Crear usuario con Factory Method_
public void crearUsuario(String tipo, String nombre) {
Usuario usuario = UsuarioFactory.crearUsuario(tipo, nombre);
usuarios.add(usuario);
logger.info("Usuario creado: " + usuario.getNombre() + " (" + usuario.ge
}


```
// Usar Abstract Factory
public void procesarEnvio(String origen, String destino, double peso) {
Envio envio = new Envio.EnvioBuilder("ENV-" + envios.size(), origen, des
.peso(peso)
.build();
envios.add(envio);
logger.info("Envío procesado con Abstract Factory");
}
}
```
```
public class Main {
public static void main(String[] args) {
// Demostración de todos los patrones integrados
```
```
// Singleton
Logger logger = Logger.getInstance();
ConfiguracionSistema config = ConfiguracionSistema.getInstance();
```
```
// Abstract Factory
LogiSmartController app = new LogiSmartController("Argentina");
```
```
// Factory Method
app.crearUsuario("cliente", "Juan Pérez");
app.crearUsuario("operador", "María García");
```
```
// Builder
app.crearEnvio("Buenos Aires", "Córdoba");
```
```
// Prototype
app.crearEnviosMultiples( 100 );
```
```
// Procesamiento con Abstract Factory
app.procesarEnvio("Buenos Aires", "Mendoza", 5.0);
```
```
logger.info("Demostración completada");
}
}
```
#### Entregable:

- Clase LogiSmartController.java
- Clase Main.java con demostración completa
- Archivo README.md explicando la integración

#### Criterios de Evaluación:

- ✅ Uso de Singleton (^10 %)


- ✅ Uso de Factory Method (^15 %)
- ✅ Uso de Abstract Factory (^20 %)
- ✅ Uso de Builder (^20 %)
- ✅ Uso de Prototype (^20 %)
- ✅ Código compilable y funcional (^15 %)

## Entregable Final del Hito 7

### Estructura del Proyecto

```
Plain Text
```
```
LogiSmartHito7/
├── src/com/logismart/
│ ├── dominio/
│ │ ├── Envio.java (con Builder y Cloneable)
│ │ ├── Usuario.java
│ │ ├── Cliente.java
│ │ ├── Operador.java
│ │ ├── Admin.java
│ │ ├── Vehiculo.java
│ │ ├── Auto.java
│ │ ├── Moto.java
│ │ └── Camion.java
│ ├── patrones/
│ │ ├── singleton/
│ │ │ ├── Logger.java
│ │ │ └── ConfiguracionSistema.java
│ │ ├── factory/
│ │ │ ├── VehiculoFactory.java
│ │ │ └── UsuarioFactory.java
│ │ ├── abstractfactory/
│ │ │ ├── LogiSmartFactory.java
│ │ │ ├── LogiSmartFactoryArgentina.java
│ │ │ ├── LogiSmartFactoryBrasil.java
│ │ │ ├── CalculadorCostos.java
│ │ │ ├── CalculadorCostosArgentina.java
│ │ │ ├── CalculadorCostosBrasil.java
│ │ │ ├── ProveedorMapas.java
│ │ │ ├── GoogleMapsArgentina.java
│ │ │ └── HereMaps.java
│ │ └── builder/
│ │ └── (EnvioBuilder está en Envio.java)
```

```
│ ├── app/
│ │ ├── LogiSmartApp.java
│ │ ├── LogiSmartController.java
│ │ └── Main.java
│ └── diagramas/
│ ├── diagrama_abstract_factory.mmd
│ ├── diagrama_builder.mmd
│ └── diagrama_prototype.mmd
├── README.md
└── DOCUMENTACION.md
```
### Archivos Requeridos

#### 1. Código Java:

- Todas las clases mencionadas arriba compilables y funcionales
- Main.java demostrando todos los patrones

#### 2. Documentación:

- README.md explicando la estructura del proyecto
- DOCUMENTACION.md con:
    - Explicación de cada patrón
    - Decisiones de diseño
    - Cómo se integran los patrones
    - Ejemplos de uso

#### 3. Diagramas:

- Diagrama de clases (Mermaid o PlantUML)
- Diagrama de Abstract Factory
- Diagrama de Builder
- Diagrama de Prototype

### Criterios de Evaluación Global

```
Criterio Peso
```
```
Abstract Factory implementada correctamente 20 %
```
```
Builder implementado correctamente 20 %
```
```
Prototype implementado correctamente 20 %
```

## Checklist de Entrega

#### Proyecto Eclipse compilable sin errores

#### Main.java ejecutable y demostrando todos los patrones

#### Todas las clases implementadas

#### README.md completo

#### DOCUMENTACION.md con explicaciones

#### Diagramas UML (Mermaid o PlantUML)

#### Código comentado y legible

#### Ejemplos de uso claros

#### Integración con Hitos 5 y 6

## Recursos Adicionales

### Documentación Recomendada

- Gang of Four - Design Patterns (Libro)
- Refactoring.guru - Design Patterns
- Oracle Java Documentation

### Herramientas

- Eclipse IDE
- Mermaid.live para diagramas
- PlantUML.online para diagramas alternativos
- Git para versionar código

### Próximos Pasos

```
Integración de todos los patrones 15 %
```
```
Código Java compilable y funcional 15 %
```
```
Documentación clara y completa 10 %
```

#### Después de completar este hito, estarás listo para:

- Clase 8 : Patrones Estructurales
- Clase 9 : Patrones de Comportamiento
- Clase 10 : Integración completa y presentación final del TPO


