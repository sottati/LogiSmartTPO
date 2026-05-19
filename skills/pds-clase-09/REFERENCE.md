# Clase 9: Patrones Estructurales I

Fuente: `clase_09.html` (contenido de `main-content`).

## Estructura de la clase

1. Introduccion a patrones estructurales.
2. Adapter Pattern.
3. Bridge Pattern.
4. Composite Pattern.
5. Comparacion de Adapter, Bridge y Composite.
6. Casos reales.
7. Hito 8: actividad practica en LogiSmart.

## Introduccion

Los patrones estructurales organizan objetos y clases en estructuras mas grandes. A diferencia de los patrones creacionales, no se enfocan en como crear objetos sino en como relacionarlos.

Los 7 patrones estructurales GoF son:

- Adapter: permite que interfaces incompatibles trabajen juntas.
- Bridge: desacopla una abstraccion de su implementacion.
- Composite: compone objetos en estructuras de arbol.
- Decorator: agrega responsabilidades dinamicamente.
- Facade: proporciona una interfaz simplificada.
- Flyweight: comparte objetos para economizar memoria.
- Proxy: proporciona un sustituto o marcador de posicion.

En esta clase se cubren Adapter, Bridge y Composite.

## Adapter

Adapter resuelve el problema de interfaces incompatibles. Se usa cuando el sistema necesita integrar una libreria, servicio o componente externo cuya interfaz no coincide con la interfaz esperada.

La solucion es crear una clase intermedia que implementa la interfaz esperada por el sistema y delega internamente al componente externo, traduciendo metodos, tipos de datos y convenciones.

Participantes:

- Client: codigo que necesita usar el servicio.
- Target: interfaz que el cliente espera.
- Adapter: clase traductora.
- Adaptee: componente externo o heredado.

### Variantes

Class Adapter:

- Usa herencia.
- El adapter hereda del adaptee e implementa el target.
- Menos comun en lenguajes modernos.

Object Adapter:

- Usa composicion.
- El adapter contiene una instancia del adaptee.
- Es la variante preferida por su flexibilidad.

Ejemplo conceptual:

```java
public class Adapter implements Target {
    private Adaptee adaptee;

    public void operacionEsperada() {
        adaptee.operacionDelAdaptee();
    }
}
```

### Ejemplo de pagos heredados

El sistema espera `ProcesadorPagos` con:

- `procesarPago(double monto, String tarjeta)`
- `obtenerEstado()`

El sistema legado expone:

- `procesarTransaccion(float cantidad, String numeroTarjeta)`
- `consultarEstado()`

El adapter traduce `double` a `float`, resultado `int` a `boolean`, y nombres de metodos.

### Ventajas

- Permite integrar componentes incompatibles.
- No requiere cambiar codigo existente.
- Sigue Open/Closed Principle.
- Facilita testing porque el adaptee puede mockearse.

### Desventajas

- Agrega una capa de indireccion.
- Puede ser excesivo si solo adapta un metodo.
- Interfaces muy diferentes pueden ser dificiles de adaptar.

### Cuando usar Adapter

- Cuando se integra una libreria externa incompatible.
- Cuando se trabaja con codigo heredado que no puede cambiarse.
- Cuando se quieren multiples implementaciones de una interfaz uniforme.
- Cuando hay que traducir convenciones, nombres o tipos de datos.

## Bridge

Bridge resuelve la explosion de subclases cuando existen dos dimensiones de variacion acopladas.

Ejemplo de problema:

- Tipos de vehiculo: Auto, Moto, Camion.
- Tipos de motor: Gasolina, Diesel, Electrico.
- Con herencia pura aparecen 9 clases: AutoGasolina, AutoDiesel, AutoElectrico, etc.

Bridge separa ambas dimensiones en dos jerarquias independientes:

- Abstraccion: interfaz de alto nivel, por ejemplo Vehiculo.
- Implementacion: detalle especifico, por ejemplo Motor.

Resultado: 3 tipos + 3 motores = 6 clases, no 9 combinaciones.

Ejemplo conceptual:

```java
public abstract class Vehiculo {
    protected Motor motor;

    public Vehiculo(Motor motor) {
        this.motor = motor;
    }

    public abstract void conducir();
}
```

### Bridge vs Adapter

| Aspecto | Adapter | Bridge |
|---|---|---|
| Proposito | Hacer que interfaces incompatibles funcionen juntas | Separar abstraccion de implementacion |
| Momento | Despues, para integrar componentes existentes | Antes, como decision de diseno |
| Estructura | Traductor entre dos interfaces | Dos dimensiones de variacion |
| Flexibilidad | Permite trabajar con lo existente | Permite evolucion independiente |

### Ventajas

- Evita explosion de subclases.
- Permite variar abstraccion e implementacion independientemente.
- Facilita agregar nuevas abstracciones o implementaciones.
- Sigue Single Responsibility Principle.

### Desventajas

- Es mas complejo que herencia simple.
- Requiere identificar dimensiones de variacion.
- Puede ser excesivo para sistemas simples.

### Cuando usar Bridge

- Cuando hay dos dimensiones de variacion independientes.
- Cuando se quiere evitar explosion de subclases.
- Cuando abstraccion e implementacion deben evolucionar por separado.
- Cuando se quieren compartir implementaciones entre multiples abstracciones.

## Composite

Composite resuelve la representacion de jerarquias parte-todo. Permite tratar objetos individuales y compuestos de forma uniforme.

Ejemplos naturales:

- Sistema de archivos: carpetas y archivos.
- Menus con submenus.
- Organigramas.
- Grupos de permisos.

Participantes:

- Component: interfaz o clase base comun.
- Leaf: objeto individual sin hijos.
- Composite: contenedor con una lista de componentes hijos.

Ejemplo conceptual:

```java
public abstract class ComponenteArchivo {
    public abstract int obtenerTamano();
    public abstract void mostrar(int profundidad);
}
```

La operacion `obtenerTamano()` en un composite recorre sus hijos y suma recursivamente sus tamanos.

### Variantes

Enfoque transparente:

- Leaf y Composite implementan todas las operaciones.
- Leaf puede no hacer nada o lanzar excepciones para operaciones de contenedor.

Enfoque seguro:

- Solo Composite implementa operaciones de contenedor.
- El cliente debe verificar el tipo antes de agregar hijos.

### Ventajas

- Simplifica el codigo cliente.
- Permite tratar hojas y composites igual.
- Representa jerarquias naturalmente.
- Facilita agregar nuevos tipos de componentes.
- Sigue Open/Closed Principle.

### Desventajas

- Puede hacer el diseno demasiado general.
- Es dificil restringir tipos de componentes dentro de contenedores.
- Puede llevar a arboles profundos y complejos.

### Cuando usar Composite

- Cuando se necesitan jerarquias parte-todo.
- Cuando el cliente no debe distinguir entre objetos simples y compuestos.
- Cuando la estructura es naturalmente recursiva.
- Cuando hay operaciones que deben funcionar en toda la jerarquia.

## Comparacion rapida

| Patron | Que resuelve | Cuando elegir |
|---|---|---|
| Adapter | Interfaces incompatibles | Necesito usar una libreria pero su API no coincide |
| Bridge | Dos dimensiones de variacion | Tengo muchos tipos de X combinados con muchos tipos de Y |
| Composite | Objetos individuales vs grupos | Tengo un arbol con hojas y contenedores |

## Casos reales

Adapter:

- Integracion de proveedores de email como Gmail, Outlook, SendGrid o SMTP.
- Cada proveedor tiene una API distinta.
- Los adapters implementan una interfaz comun como `ServicioEmail`.

Bridge:

- Sistema de notificaciones multicanal.
- Abstraccion: tipo de notificacion, como urgente o marketing.
- Implementacion: proveedor/canal, como email, SMS, push o Slack.

Composite:

- Sistema de permisos jerarquico.
- Hoja: permiso individual.
- Composite: grupo de permisos.
- Un usuario puede tener grupos anidados y permisos directos.

## Hito 8 en LogiSmart

El Hito 8 pide aplicar tres patrones estructurales:

- Adapter: integrar APIs externas de proveedores logisticos como DHL, FedEx y UPS.
- Bridge: separar tipos de reportes de motores de generacion como PDF, Excel y JSON.
- Composite: modelar la jerarquia de centros de distribucion, almacenes y puntos de entrega.

La situacion de negocio indica que LogiSmart crecio y necesita integrar proveedores logisticos externos, soportar multiples tipos de reportes con distintos formatos y representar una estructura jerarquica de centros de distribucion.
