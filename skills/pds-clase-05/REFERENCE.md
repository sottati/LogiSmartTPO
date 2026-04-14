# Clase 5: GRASP Parte II

## Por qué estos 5 patrones

El software cambia constantemente: los requisitos evolucionan, la tecnología cambia, el negocio se transforma. Un buen diseño de software tiene que anticipar esas realidades. Los 5 patrones de GRASP Parte II enseñan exactamente eso: cómo construir sistemas que puedan evolucionar sin romperse.

Código rígido es difícil de cambiar. Código flexible con buenos patrones hace que los cambios sean baratos y seguros.

## Los 5 patrones

1. Controller
2. Polymorphism
3. Pure Fabrication
4. Indirection
5. Protected Variations

## 1. Controller

El Controller actúa como intermediario entre la interfaz de usuario y la lógica de dominio. Recibe las solicitudes de la UI, coordina las operaciones necesarias y devuelve el resultado.

Dos variantes:

- **Facade Controller**: un único controlador para todo el sistema o subsistema
- **Use Case Controller**: un controlador por caso de uso

Beneficios:

- Desacopla la UI del dominio
- Mejora la testabilidad (se puede probar el dominio sin UI)
- Permite reutilizar la lógica con distintas interfaces
- Centraliza la coordinación de operaciones

Ejemplo en LogiSmart: `LogiSmartController` recibe la solicitud de crear un envío, coordina la validación, la creación y la notificación, sin que la UI conozca esos detalles.

## 2. Polymorphism

El polimorfismo reemplaza condicionales por tipo con comportamiento variable según el tipo del objeto. Usa interfaces o supertipos con implementaciones concretas distintas.

Problema que resuelve:

```java
// Mal: condicional por tipo
if (envio.getTipo().equals("express")) {
    calcularTarifaExpress(envio);
} else if (envio.getTipo().equals("normal")) {
    calcularTarifaNormal(envio);
}
```

Solución:

```java
// Bien: polimorfismo
envio.calcularTarifa(); // cada tipo sabe cómo calcular la suya
```

Beneficios:

- Elimina condicionales por tipo
- Facilita agregar nuevos tipos sin modificar código existente
- Mejora la legibilidad y mantenibilidad

## 3. Pure Fabrication

Pure Fabrication crea clases artificiales que no representan conceptos del dominio pero mejoran la distribución de responsabilidades. Se usa cuando Expert o Creator obligarían a poner responsabilidades donde no corresponden.

Ejemplo: una clase `NotificadorDeEnvios` no es un concepto del dominio logístico, pero tiene sentido crearla para encapsular la lógica de notificación en un lugar único y reutilizable.

Otros ejemplos de Pure Fabrication:

- `EnvioRepository` (persistencia)
- `LogiSmartController` (coordinación)
- `TarifaCalculator` (cálculo de tarifas)

Beneficios:

- Mantiene las clases de dominio limpias
- Agrupa comportamientos relacionados artificialmente
- Mejora la reutilización

## 4. Indirection

Indirection introduce un intermediario entre dos componentes para reducir el acoplamiento directo entre ellos.

El intermediario puede ser una interfaz, una clase de servicio o cualquier capa de abstracción.

Beneficios:

- Permite cambiar implementaciones sin afectar al cliente
- Facilita el uso de mocks en tests
- Permite inyección de dependencias
- Desacopla componentes que cambian a ritmos distintos

Ejemplo en LogiSmart: en vez de que `GestorDeEnvios` dependa directamente de `EmailNotifier`, depende de la interfaz `Notificador`. Así se puede cambiar a SMS o WhatsApp sin tocar `GestorDeEnvios`.

## 5. Protected Variations

Protected Variations identifica los puntos del sistema que probablemente van a cambiar y los aísla detrás de abstracciones. El objetivo es que el cambio en una parte no rompa otras partes.

Puntos típicos de variación:

- Algoritmos (diferentes formas de calcular tarifas)
- Fuentes de datos (base de datos, API, archivo)
- Servicios externos (proveedores de mapas, pasarelas de pago)
- Reglas de negocio (políticas de precios, regulaciones)

Técnica: usar interfaces o clases abstractas para aislar el punto de variación. El cliente programa contra la abstracción, no contra la implementación concreta.

Beneficios:

- Cambios aislados no generan efecto dominó
- El sistema es extensible sin ser modificado (Open/Closed)
- Facilita las pruebas con implementaciones alternativas

## Aplicación en LogiSmart (Hito 5)

El Hito 5 requiere aplicar estos patrones:

- Agregar un controlador principal que coordine los casos de uso
- Reemplazar condicionales por tipo con polimorfismo
- Crear clases de servicio (Pure Fabrication) donde corresponda
- Aislar los puntos de variación identificados
- Documentar las decisiones de diseño

---

## Perspectiva del profesor

### Insights clave

El profesor enfatizó que estos 5 patrones tienen un propósito unificado: hacer que el software sea capaz de cambiar sin romperse.

> "El software cambia. Los requisitos cambian, la tecnología cambia. Un buen diseño tiene que anticipar eso. Estos 5 patrones te enseñan cómo."

La flexibilidad no es un lujo: es una necesidad económica. Código rígido tiene un costo real: cada cambio tarda más, rompe más cosas, genera más bugs. Inversamente, código bien diseñado con estos patrones hace que los cambios sean rápidos y seguros.

El núcleo de los 5 patrones es: más código flexible = más mantenible = menos bugs = desarrollo más rápido.

Un error común es esperar a que algo cambie para recién pensar en flexibilidad. La clave de Protected Variations es ser proactivo: identificar qué va a cambiar antes de que cambie, y blindar esos puntos.

> "El código muy rígido es muy difícil de cambiar. Y el cambio no es opcional en desarrollo de software real."

Sobre Pure Fabrication: a veces la mejor decisión de diseño es inventar una clase que no existe en el dominio. Si eso mejora la cohesión y reduce el acoplamiento, es una decisión válida y elegante.

### Analogías y ejemplos reales

**El controlador como gerente**: el Controller es como un gerente que coordina trabajadores. La UI no necesita saber cómo funciona cada parte del sistema; solo le dice al controlador qué hacer, y él coordina el resto.

**Polimorfismo como control remoto universal**: el mismo botón hace cosas distintas según el dispositivo. El cliente (el que aprieta el botón) no necesita saber si está controlando un televisor, un proyector o un sistema de audio.

**Pure Fabrication como contratar un especialista**: cuando ningún miembro del equipo tiene la habilidad para una tarea, contratás a alguien externo. Pure Fabrication es lo mismo: cuando ninguna clase existente debería tener la responsabilidad, creás una clase nueva para eso.

**Indirection como intermediario de negocios**: en el mundo real, si dos empresas no quieren tener una relación directa, usan un intermediario. En software, la interfaz es ese intermediario que permite cambiar cualquiera de las dos partes sin que la otra se entere.

**Protected Variations como paredes modulares**: si diseñás una casa con paredes modulares, podés cambiar la distribución sin tocar los cimientos. Si diseñás tu software con puntos de variación bien aislados, podés cambiar implementaciones sin tocar el resto del sistema.

### Consejo profesional

- No esperés a que el cambio llegue para pensar en flexibilidad. Identificá los puntos de variación desde el diseño inicial.
- Cuando veas una cadena de if-else por tipo, pensá en polimorfismo. Es casi siempre la solución correcta.
- El Controller es el primer lugar donde aplicar la separación de responsabilidades entre UI y dominio.
- Estos 5 patrones trabajan juntos como sistema: Controller desacopla la UI, Polymorphism elimina condicionales, Pure Fabrication limpia el dominio, Indirection reduce dependencias, Protected Variations aísla el cambio.
- El objetivo siempre vuelve a lo mismo: flexibilidad, mantenibilidad, escalabilidad. Si una decisión de diseño mejora esas tres cosas, probablemente sea correcta.