# Clase 3: Del Diseno a la Implementacion

Fuente: `clase_03.html` (solo contenido de `main-content`).

## Subtitulo

Traduciendo Diagramas de Clases UML a Codigo Java en Eclipse.

## Del diseno a la implementacion

La clase toma el trabajo del Hito 2 y lo lleva a codigo.

- Antes: rol de arquitectos, modelando estructura con UML.
- Ahora: rol de constructores, implementando esa estructura en Java.

### Por que separar diseno de implementacion

En ingenieria de software profesional, disenar primero permite:

#### Pensamiento abstracto

- enfocarse en estructura sin distracciones de sintaxis.

#### Comunicacion universal

- un UML puede ser implementado en Java, Python o C#.

#### Deteccion temprana de errores

- corregir una relacion en el diagrama es mucho mas barato que refactorizar mucho codigo.

#### Vision global

- el diagrama permite ver el sistema completo antes de entrar al detalle del codigo.

## Eclipse IDE: herramienta de construccion

Un IDE es una suite para escribir, compilar, depurar y gestionar codigo.

### Por que Eclipse

- es robusto,
- muy usado en desarrollo Java empresarial,
- open source,
- gratuito,
- con comunidad grande.

### Que ofrece Eclipse

#### Editor inteligente

- autocompletado,
- resaltado de sintaxis,
- deteccion de errores en tiempo real.

#### Compilador integrado

- traduce Java a bytecode para la JVM.

#### Depurador

- permite ejecutar linea por linea,
- inspeccionar variables,
- encontrar errores.

#### Gestion de proyectos

- organiza archivos, librerias y dependencias.

### Instalacion de Eclipse

- descargar desde `https://www.eclipse.org/downloads/`
- elegir `Eclipse IDE for Java Developers`
- seguir instalador.

## Creando un proyecto Java en Eclipse

Un proyecto es la carpeta especial donde viven codigo, librerias y configuracion.

### Paso a paso: crear un proyecto

1. `File > New > Java Project`
2. Definir `Project Name`, por ejemplo `LogiSmartTPO`
3. Verificar una version de Java (`JavaSE-11` o superior)
4. Dejar layout por defecto
5. `Finish`

Resultado esperado del layout por defecto:

- `src` para codigo fuente
- `bin` para codigo compilado

### Creando paquetes

Se recomienda usar nombre de dominio invertido.

Ejemplo:

- `com.logismart.dominio`

Pasos:

1. Click derecho en `src`
2. `New > Package`
3. Nombre: `com.logismart.dominio`
4. `Finish`

#### Estructura de proyecto recomendada

- `com.logismart.dominio`: clases del modelo de dominio
- `com.logismart.servicio`: servicios y logica de negocio
- `com.logismart.persistencia`: acceso a base de datos
- `com.logismart.ui`: interfaz de usuario

## Traduciendo clases UML a codigo Java

La traduccion de UML a Java es casi 1 a 1.

### Creando una clase en Eclipse

1. Click derecho en el package `com.logismart.dominio`
2. `New > Class`
3. Nombre: `Vehiculo`
4. `Finish`

Eclipse genera el esqueleto:

```java
package com.logismart.dominio;

public class Vehiculo {

}
```

### De UML a Java: traduccion directa

UML:

```text
Clase: Vehiculo
Atributos:
- id: String
- patente: String
- marca: String
- capacidadPeso: double
```

Java:

```java
public class Vehiculo {
    private String id;
    private String patente;
    private String marca;
    private double capacidadPeso;
}
```

Regla de traduccion:

- UML: `visibilidad nombre: tipo`
- Java: `visibilidad tipo nombre;`
- `-` en UML -> `private`
- `+` en UML -> `public`

## Atributos y constructores

Los atributos deben ser privados para respetar encapsulacion.

Ejemplo de atributos:

```java
public class Vehiculo {
    private String id;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private double capacidadPeso;
    private double capacidadVolumen;
}
```

### Que es un constructor

- metodo especial llamado al crear un objeto con `new`
- sirve para inicializar estado
- si no se define, Java genera uno vacio por defecto
- buena practica: definir uno propio

Ejemplo:

```java
public class Vehiculo {
    private String id;
    private String patente;
    private String marca;
    private double capacidadPeso;

    public Vehiculo(String id, String patente, String marca, double capacidadPeso) {
        this.id = id;
        this.patente = patente;
        this.marca = marca;
        this.capacidadPeso = capacidadPeso;
    }
}
```

### Uso de `this`

- desambigua entre parametro y atributo de la clase.

### Usando el constructor

```java
Vehiculo v1 = new Vehiculo("V001", "ABC-123", "Toyota", 1000.0);
Vehiculo v2 = new Vehiculo("V002", "XYZ-789", "Ford", 1500.0);
```

## Getters y setters

Como los atributos son privados, el acceso externo se hace con metodos publicos.

### Getter

- retorna el valor de un atributo privado
- convencion: `get` + nombre del atributo

Ejemplo:

```java
public class Vehiculo {
    private String patente;

    public String getPatente() {
        return this.patente;
    }
}
```

### Setter

- modifica el valor de un atributo privado
- convencion: `set` + nombre del atributo

Ejemplo:

```java
public class Vehiculo {
    private String marca;

    public void setMarca(String nuevaMarca) {
        this.marca = nuevaMarca;
    }
}
```

### Generacion automatica en Eclipse

1. Click derecho en el codigo de la clase
2. `Source > Generate Getters and Setters...`
3. Seleccionar atributos
4. `OK`

Resultado tipico:

```java
public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getPatente() {
    return patente;
}

public void setPatente(String patente) {
    this.patente = patente;
}
```

### Usando getters y setters

```java
Vehiculo v1 = new Vehiculo("V001", "ABC-123", "Toyota", 1000.0);

String patente = v1.getPatente();

v1.setMarca("Honda");

System.out.println(v1.getMarca());
```

## Traduciendo relaciones: asociacion y composicion

### Asociacion simple

Si una clase conoce a otra, en codigo se modela como atributo del tipo de la otra clase.

Ejemplo UML:

```text
Ruta -------- Vehiculo (1 a 1)
```

Java:

```java
public class Ruta {
    private Vehiculo vehiculoAsignado;
}
```

### Composicion (1 a muchos)

Si una clase se compone de multiples objetos, se usa una coleccion.

Ejemplo:

```java
import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private String id;
    private Vehiculo vehiculoAsignado;
    private List<PuntoDeEntrega> puntosDeEntrega;

    public Ruta(String id, Vehiculo vehiculo) {
        this.id = id;
        this.vehiculoAsignado = vehiculo;
        this.puntosDeEntrega = new ArrayList<>();
    }

    public void agregarPuntoDeEntrega(PuntoDeEntrega punto) {
        this.puntosDeEntrega.add(punto);
    }

    public List<PuntoDeEntrega> getPuntosDeEntrega() {
        return this.puntosDeEntrega;
    }
}
```

### Que es `ArrayList`

- una lista dinamica de Java
- puede crecer o achicarse
- `List<PuntoDeEntrega>` significa lista de objetos `PuntoDeEntrega`

## Herencia en Java: relacion "es un"

En UML se representa con flecha triangular vacia. En Java se implementa con `extends`.

### Creando una jerarquia de usuarios

Caso:

- `Usuario` como clase base
- `Cliente` y `Operador` como clases hijas

#### Paso 1: clase base `Usuario`

```java
public class Usuario {
    private String id;
    private String email;
    private String password;

    public Usuario(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean autenticar(String passwordIngresado) {
        return this.password.equals(passwordIngresado);
    }

    // Getters y setters...
}
```

#### Paso 2: clase hija `Cliente`

```java
public class Cliente extends Usuario {
    private String razonSocial;
    private String cuit;

    public Cliente(String id, String email, String password, String razonSocial) {
        super(id, email, password);
        this.razonSocial = razonSocial;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }
}
```

### Uso de `super`

- permite acceder a miembros de la clase padre
- `super(id, email, password)` llama al constructor de `Usuario`

### Usando la herencia

```java
Cliente c1 = new Cliente("U001", "juan@logismart.com", "pass123", "Juan's Logistics");

boolean autenticado = c1.autenticar("pass123");

String razon = c1.getRazonSocial();
```

### Prueba de Liskov

- un `Cliente` puede ser tratado como `Usuario`
- esa es la base de una herencia correcta

## Implementando comportamiento: metodos con logica

Hasta este punto las clases guardan estado. La POO busca combinar estado con comportamiento.

### Ejemplo: metodo con logica de negocio

Caso: `Ruta.agregarPuntoDeEntrega()` validando capacidad del vehiculo.

```java
public class Ruta {
    private Vehiculo vehiculoAsignado;
    private List<PuntoDeEntrega> puntosDeEntrega;

    public boolean agregarPuntoDeEntrega(PuntoDeEntrega nuevoPunto) {
        double pesoTotal = 0;

        for (PuntoDeEntrega p : this.puntosDeEntrega) {
            pesoTotal += p.getEnvio().getPeso();
        }

        if (this.vehiculoAsignado.verificarCapacidad(
            pesoTotal + nuevoPunto.getEnvio().getPeso())) {
            this.puntosDeEntrega.add(nuevoPunto);
            System.out.println("Punto agregado exitosamente.");
            return true;
        } else {
            System.out.println("Error: Capacidad del vehiculo excedida.");
            return false;
        }
    }
}
```

Idea central del ejemplo:

- `Ruta` colabora con `Vehiculo` y `PuntoDeEntrega`
- cada objeto conserva su propia responsabilidad
- `Ruta` no implementa internamente la validacion del vehiculo
- eso favorece bajo acoplamiento

## Hito 3: implementacion en Eclipse

El Hito 3 consiste en implementar en Java el diagrama del Hito 2 dentro de un proyecto Eclipse.

### Que deben entregar

- estructura de packages correcta
- todas las clases del diagrama implementadas como `.java`
- atributos con visibilidad correcta
- constructores para inicializar objetos
- getters y setters para todos los atributos
- relaciones implementadas con atributos y `extends` segun corresponda
- metodos principales definidos, aunque todavia sin toda la logica compleja

### Objetivo del Hito 3

- hacer una traduccion fiel del diseno a codigo
- dejar una base de codigo solida y bien estructurada
- preparar esa base para clases futuras con patrones y logica mas compleja

## Recursos adicionales

### Tutoriales de Eclipse

- `https://www.eclipse.org/documentation/`
- `https://www.youtube.com/results?search_query=eclipse+java+tutorial`

### Java y POO

- `https://docs.oracle.com/javase/tutorial/`
- `https://www.baeldung.com/java-oop`
- `https://www.geeksforgeeks.org/object-oriented-programming-oops-concept-in-java/`

### Getters y setters

- `https://www.baeldung.com/java-getters-setters`
- `https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html`

### Herencia en Java

- `https://www.baeldung.com/java-inheritance`
- `https://www.geeksforgeeks.org/inheritance-in-java/`

### Collections (`List`, `ArrayList`)

- `https://www.baeldung.com/java-collections`
- `https://docs.oracle.com/javase/tutorial/collections/`

### Debugging en Eclipse

- `https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php`
- `https://www.youtube.com/watch?v=kkFhLr53DTY`
