# Clase 2: Del Analisis al Diseno

Fuente: `clase_2.html` (solo contenido de `main-content`).

## Del analisis al diseno

La clase avanza del **QUE** (casos de uso y analisis) al **COMO** (estructura interna del sistema).

Primer artefacto de diseno: **Modelo de Dominio**.

## Que es el modelo de dominio

Es un vocabulario visual del problema:

- conceptos/entidades relevantes del dominio
- relaciones entre esos conceptos

No es aun diseno de implementacion detallada: es modelado del problema.

Ejemplos de conceptos por dominio:

- E-commerce: Cliente, Producto, Orden, Carrito, Pago, Envio.
- Aerolineas: Pasajero, Vuelo, Asiento, Aeropuerto, Equipaje, Reserva.
- LogiSmart: Cliente, Envio, Vehiculo, Ruta, Operador, Punto de Entrega.

Herramienta central: **Diagrama de Clases UML**.

## POO vs procedural

Procedural:

- datos y funciones separados.

POO:

- datos + comportamiento unidos en objetos.
- cada objeto tiene estado y metodos sobre su propio estado.

Ejemplo cuenta bancaria:

- Procedural: `saldo` separado de `depositar()/extraer()`.
- POO: objeto `CuentaBancaria` encapsula saldo y controla cambios via metodos.

Ventajas POO:

- modularidad
- reutilizacion
- mantenibilidad
- escalabilidad

## 3 pilares de un objeto

1. **Identidad**: unicidad del objeto.
2. **Estado**: atributos/datos.
3. **Comportamiento**: metodos/acciones.

Analogia auto:

- identidad: chasis
- estado: velocidad, combustible, etc.
- comportamiento: acelerar, frenar, girar

## Clases y objetos

Clase:

- plantilla o definicion abstracta.

Objeto:

- instancia concreta en memoria.

Comparativa:

| Aspecto | Clase | Objeto |
| --- | --- | --- |
| Naturaleza | Plantilla abstracta | Instancia concreta |
| Cantidad | Una por tipo | Multiples instancias |
| Existencia | Codigo fuente | Memoria en ejecucion |
| Estado | Define atributos | Tiene estado propio |
| Ejemplo | Clase `Cliente` | `Juan Perez`, `Maria Garcia` |

## Encapsulacion

Principio: ocultar estado interno y exponer comportamiento controlado.

Objetivo:

- proteger integridad del objeto
- evitar cambios invalidos directos

Ejemplo:

- `saldo` privado, cambio solo via `extraer(monto)` con validaciones.

Visibilidad UML:

| Modificador | Simbolo | Acceso | Uso tipico |
| --- | --- | --- | --- |
| public | + | desde cualquier lugar | metodos de comportamiento |
| private | - | solo dentro de la clase | atributos de estado |
| protected | # | clase y subclases | extension por herencia |

Beneficios:

- menor acoplamiento
- mayor cohesion
- cambio interno sin romper contrato publico

## UML: diagrama de clases

Una clase UML tiene 3 secciones:

```text
+-------------------------------+
| NombreDeLaClase               |
+-------------------------------+
| - atributo1: Tipo             |
| - atributo2: Tipo             |
+-------------------------------+
| + metodo1(param): Retorno     |
| + metodo2(): void             |
+-------------------------------+
```

Sintaxis:

- Atributo: `visibilidad nombre: tipo [= valorInicial]`
- Metodo: `visibilidad nombre(parametros): tipoRetorno`

Ejemplo (CuentaBancaria):

```text
+----------------------------------+
| CuentaBancaria                   |
+----------------------------------+
| - numeroCuenta: String           |
| - titular: String                |
| - saldo: double = 0.0            |
| - clave: String                  |
+----------------------------------+
| + depositar(monto: double): void |
| + extraer(monto: double): bool   |
| + obtenerSaldo(): double         |
| + cambiarClave(nueva: String)    |
| - validarClave(clave: String)    |
+----------------------------------+
```

Observacion de clase:

- atributos privados
- metodos publicos

## Relaciones entre clases

### 1) Asociacion ("conoce a")

- relacion basica entre clases
- se modela con linea simple
- puede tener multiplicidad

Ejemplo Cliente-Orden:

- un cliente: `0..*` ordenes
- una orden: `1` cliente

### 2) Agregacion ("tiene un" debil)

- parte-todo debil
- rombo blanco
- partes pueden existir sin el todo

Ejemplo:

- Departamento tiene Profesores.
- si desaparece Departamento, Profesores siguen.

### 3) Composicion ("es parte de" fuerte)

- parte-todo fuerte
- rombo negro
- partes no existen sin el todo

Ejemplo:

- Orden compuesta por ItemsDeOrden.
- si se elimina orden, items desaparecen.

Tabla comparativa:

| Tipo | Simbolo | Significado | Dependencia |
| --- | --- | --- | --- |
| Asociacion | Linea simple | Conoce a | Debil |
| Agregacion | Rombo blanco | Tiene un (debil) | Debil |
| Composicion | Rombo negro | Es parte de (fuerte) | Fuerte |

## Herencia: relacion "es un"

- subclase hereda atributos/metodos de superclase
- se representa con flecha triangular vacia

Regla de uso:

- prueba de Liskov (sustitucion)
- una clase hija debe poder usarse donde se espera la padre

Ejemplo LogiSmart:

- `Usuario` base: email, password, cambiarPassword().
- `Cliente` hereda y agrega: historial, calificacion, solicitarEnvio().
- `Operador` hereda y agrega: numeroEmpleado, asignaciones, aceptarEnvio().

Advertencia:

- priorizar composicion cuando aplique.
- ejemplo correcto: Auto tiene un Motor (no Auto hereda de Motor).

## Principios de diseno: cohesion y acoplamiento

### Alta cohesion

- una clase, una responsabilidad clara.
- evita clases "Dios" con funciones de dominios mezclados.

Ejemplo baja cohesion:

- `Usuario` con autenticar + guardarBD + enviarEmail + calcularImpuestos.

Mejor separacion:

- `Usuario`
- `AutenticacionService`
- `RepositorioUsuario`
- `EmailService`
- `ImpuestoService`

### Bajo acoplamiento

- minimizar dependencias entre clases.
- cambios en A no deberian romper B/C/D.

Ejemplo alto acoplamiento:

- `Orden` crea directamente `BaseDatos` y `Email`.

Mejora:

- inyeccion de dependencias (recibir dependencias).

Beneficios globales:

- mantenibilidad
- testabilidad
- reutilizacion
- escalabilidad

## Hito 2: modelo de dominio de LogiSmart

Objetivo del hito:

- transformar analisis de Hito 1 en diagrama de clases del modelo de dominio.

Trabajo pedido:

1. Identificar clases candidatas desde sustantivos de casos de uso.
2. Definir atributos/metodos con visibilidad correcta.
3. Establecer relaciones, tipo y multiplicidad justificadas.
4. Revisar cohesion y acoplamiento.
5. Debatir y justificar decisiones (no hay unica respuesta).

Importancia:

- diagrama como columna vertebral de clases siguientes.
- patrones de diseno futuros se apoyan en este modelo.
