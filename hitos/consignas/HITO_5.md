# Hito 5 del TPO: Asignación de Responsabilidades GRASP

# Parte II

## Aplicación de Controller, Polymorphism, Pure Fabrication,

## Indirection y Protected Variations

**Duración:** 90 minutos de práctica
**Objetivo:** Refactorizar el diseño del Hito 4 aplicando los 5 patrones de GRASP Parte II
**Entregable:** Documento Markdown + Diagramas actualizados + Código Java (opcional)

## 1. Contexto

En el Hito 4, aplicaste **GRASP Parte I** (Expert, Creator, Low Coupling, High Cohesion) para
crear un diagrama de clases inicial y diagramas de interacción. Ahora, en el Hito 5, vas a
refactorizar ese diseño aplicando **GRASP Parte II** para hacerlo más flexible, mantenible y
escalable.

Los 5 patrones que aplicarás son:

```
1 Controller: Intermediario entre UI y dominio
2 Polymorphism: Eliminar condicionales con interfaces
3 Pure Fabrication: Crear clases artificiales útiles
4 Indirection: Desacoplar componentes
5 Protected Variations: Proteger de cambios futuros
```
## 2. Actividades (90 minutos)

## Actividad 1: Análisis de Diseño Actual (15 minutos)

Revisa tu Hito 4 y responde estas preguntas:

```
6 ¿Hay un Controller? ¿Quién coordina las operaciones principales (crear envío,
asignar ruta, etc.)?
7 ¿Hay condicionales? ¿Dónde hay if/else o switch que podrían reemplazarse con
Polymorphism?
```

```
8 ¿Hay acoplamiento? ¿Qué clases dependen directamente de otras?
9 ¿Hay puntos de variación? ¿Qué podría cambiar en el futuro (algoritmos, servicios,
políticas)?
```
**Entregable:** Una lista de 5-10 problemas identificados en tu diseño actual.

### Actividad 2: Aplicar Controller (15 minutos)

Crea un **LogiSmartController** que actúe como intermediario entre la UI y el dominio.

**Responsabilidades del Controller:**

- Recibir solicitudes de la UI (crear envío, asignar ruta, notificar, etc.)
- Coordinar operaciones entre múltiples objetos del dominio
- Validar entrada y manejar errores
- Devolver resultados a la UI

**Ejemplo de métodos:**

```
class LogiSmartController {
```
```
private Logismart logismart;
private ServicioDeNotificaciones notificaciones;
```
```
// Crear un nuevo envío
public Envio crearEnvio (String origen , String destino ,
double peso, String idCliente ) {
// Validar entrada
// Crear envío
// Registrar en el sistema
// Notificar al cliente
// Devolver resultado
}
```
```
// Asignar ruta a un envío
public boolean asignarRuta (String idEnvio , String idRuta ) {
// Validar que envío existe
// Validar que ruta existe
// Asignar ruta
// Notificar
// Devolver resultado
}
```
```
// Obtener estado de un envío
```

```
public EstadoEnvio obtenerEstado (String idEnvio ) {
// Buscar envío
// Obtener estado
// Devolver resultado
}
```
```
}
```
**Preguntas:**

```
10 ¿Qué operaciones principales debería coordinar tu Controller?
11 ¿Cuáles son los métodos principales que necesita?
12 ¿Qué validaciones debería hacer?
```
**Entregable:** Código del Controller (pseudocódigo o Java) con 5-7 métodos principales.

### Actividad 3: Aplicar Polymorphism (20 minutos)

Identifica lugares donde hay condicionales y reemplázalos con Polymorphism.

**Puntos de Polymorphism en LogiSmart:**

```
13 Tipos de Usuarios: Cliente, Operador, Administrador
◦ Cada uno tiene permisos diferentes
◦ Reemplaza condicionales con interfaz Usuario
14 Tipos de Notificadores: Email, SMS, Push
◦ Cada uno envía notificaciones de forma diferente
◦ Reemplaza condicionales con interfaz Notificador
15 Tipos de Vehículos: Auto, Camión, Moto
◦ Cada uno tiene capacidad y velocidad diferentes
◦ Reemplaza condicionales con interfaz Vehiculo
16 Estrategias de Asignación de Ruta: Más cercana, menos congestionada, más barata
◦ Cada una calcula la ruta de forma diferente
◦ Reemplaza condicionales con interfaz CalculadorDeRuta
```
**Ejemplo:**

```
// ❌ ANTES: Condicionales
```
```
if (usuario instanceof Cliente) {
```

```
// Permitir prestar libro
} else if (usuario instanceof Operador) {
// Permitir más operaciones
}
```
```
// ✅ DESPUÉS: Polymorphism
interface Usuario {
boolean puedeCrearEnvio ();
boolean puedeAsignarRuta ();
boolean puedeVerReportes ();
}
```
```
class Cliente implements Usuario {
public boolean puedeCrearEnvio () { return true ; }
public boolean puedeAsignarRuta () { return false ; }
public boolean puedeVerReportes () { return false ; }
}
```
```
class Operador implements Usuario {
public boolean puedeCrearEnvio () { return true ; }
public boolean puedeAsignarRuta () { return true ; }
public boolean puedeVerReportes () { return true ; }
```
```
}
```
**Preguntas:**

```
17 ¿Dónde hay condicionales en tu diseño?
18 ¿Qué interfaces podrías crear?
19 ¿Qué implementaciones de cada interfaz necesitas?
```
**Entregable:** 3 - 4 interfaces con sus implementaciones (pseudocódigo o Java).

### Actividad 4: Pure Fabrication e Indirection (20 minutos)

Identifica responsabilidades que no encajan naturalmente en ninguna clase y crea **Pure
Fabrication** para ellas.

**Ejemplos de Pure Fabrication en LogiSmart:**

```
20 ServicioDeNotificaciones
◦ Responsabilidad: Enviar notificaciones (email, SMS, push)
◦ No encaja en: Envio, Ruta, Usuario
```

```
◦ Solución: Crear clase artificial ServicioDeNotificaciones
21 CalculadorDeRutas
◦ Responsabilidad: Calcular rutas óptimas
◦ No encaja en: Ruta, Vehiculo
◦ Solución: Crear clase artificial CalculadorDeRutas
22 RepositorioDeEnvios
◦ Responsabilidad: Persistencia de envíos
◦ No encaja en: Envio, Logismart
◦ Solución: Crear clase artificial RepositorioDeEnvios
23 ValidadorDeEnvios
◦ Responsabilidad: Validar envíos
◦ No encaja en: Envio, Logismart
◦ Solución: Crear clase artificial ValidadorDeEnvios
```
**Ejemplo:**

```
// Pure Fabrication: ServicioDeNotificaciones
```
```
class ServicioDeNotificaciones {
private Notificador notificador;
```
```
public void notificarCreacionDeEnvio (Envio envio , Cliente cliente ) {
String mensaje = "Envío creado: " + envio. getId ();
notificador. notificar (cliente. getEmail (), mensaje);
}
```
```
public void notificarAsignacionDeRuta (Envio envio , Ruta ruta ) {
String mensaje = "Ruta asignada: " + ruta. getId ();
notificador. notificar (envio. getCliente (). getEmail (), mensaje);
}
}
```
```
// Indirection: Interfaz Notificador
interface Notificador {
void notificar (String email , String mensaje );
}
```
```
class NotificadorEmail implements Notificador {
public void notificar (String email , String mensaje ) {
// Enviar email
}
}
```
```
class NotificadorSMS implements Notificador {
public void notificar (String email , String mensaje ) {
// Enviar SMS
```

```
}
```
```
}
```
**Preguntas:**

```
24 ¿Qué responsabilidades no encajan naturalmente en tus clases?
25 ¿Qué clases artificiales necesitas crear?
26 ¿Qué interfaces necesitas para desacoplar componentes?
```
**Entregable:** 3 - 4 clases de Pure Fabrication + interfaces de Indirection (pseudocódigo o
Java).

### Actividad 5: Protected Variations (15 minutos)

Identifica **puntos de variación** (lugares donde es probable que haya cambios) y protégelos
con abstracciones.

**Puntos de Variación en LogiSmart:**

```
27 Cálculo de Costo de Envío
◦ Podría cambiar: Tarifa fija, por peso, por distancia, combinada
◦ Solución: Interfaz CalculadorDeCosto
28 Asignación de Vehículos
◦ Podría cambiar: Disponibilidad, capacidad, proximidad
◦ Solución: Interfaz AsignadorDeVehiculos
29 Cálculo de Tiempo de Entrega
◦ Podría cambiar: Distancia, tráfico, horario
◦ Solución: Interfaz CalculadorDeTiempo
30 Generación de Reportes
◦ Podría cambiar: Formato (PDF, Excel, JSON), contenido
◦ Solución: Interfaz GeneradorDeReportes
```
**Ejemplo:**

```
// Protected Variations: Cálculo de Costo
```
```
interface CalculadorDeCosto {
double calcular (Envio envio );
}
```

```
class CalculadorDeCostoFijo implements CalculadorDeCosto {
public double calcular (Envio envio ) {
return 50.0 ; // Costo fijo
}
}
```
```
class CalculadorDeCostoPorPeso implements CalculadorDeCosto {
public double calcular (Envio envio ) {
return envio. getPeso () * 10.0; // $10 por kg
}
}
```
```
class CalculadorDeCostoPorDistancia implements CalculadorDeCosto {
public double calcular (Envio envio ) {
double distancia = envio. getRuta (). getDistancia ();
return distancia * 5.0; // $5 por km
}
}
```
```
// Uso:
class Envio {
private CalculadorDeCosto calculador;
```
```
public double calcularCosto () {
return calculador. calcular (this );
}
```
```
}
```
**Preguntas:**

```
31 ¿Qué podría cambiar en el futuro?
32 ¿Qué algoritmos o políticas son variables?
33 ¿Qué interfaces necesitas para proteger estos cambios?
```
**Entregable:** 3 - 4 interfaces de Protected Variations con al menos 2 implementaciones cada
una (pseudocódigo o Java).

### Actividad 6: Documentación (5 minutos)

Documenta las decisiones de diseño que tomaste al aplicar los 5 patrones.

**Para cada patrón, responde:**


```
34 ¿Dónde lo aplicaste?
35 ¿Cuál fue el problema que resolvió?
36 ¿Cómo mejora el diseño?
37 ¿Qué interfaces o clases creaste?
```
## 3. Entregables

### 3.1 Documento Markdown

Crea un documento Hito_5_GRASP_Parte_II.md con:

```
38 Análisis de Diseño Actual (Actividad 1)
◦ Problemas identificados en el Hito 4
◦ Cómo se resuelven con GRASP Parte II
39 Aplicación de Controller (Actividad 2)
◦ Diagrama del Controller
◦ Métodos principales
◦ Responsabilidades
40 Aplicación de Polymorphism (Actividad 3)
◦ Interfaces creadas
◦ Implementaciones
◦ Dónde reemplazó condicionales
41 Pure Fabrication e Indirection (Actividad 4)
◦ Clases artificiales creadas
◦ Interfaces de desacoplamiento
◦ Beneficios
42 Protected Variations (Actividad 5)
◦ Puntos de variación identificados
◦ Interfaces de protección
◦ Implementaciones alternativas
43 Conclusiones
◦ Cómo cambió el diseño
◦ Beneficios del nuevo diseño
◦ Próximos pasos
```
### 3.2 Diagramas Actualizados

```
44 Diagrama de Clases Completo
```

```
◦ Incluye Controller, interfaces, Pure Fabrication
◦ Muestra todas las relaciones
◦ Actualizado con respecto al Hito 4
45 Diagramas de Secuencia
◦ Caso de uso 1: Crear Envío (con Controller)
◦ Caso de uso 2: Asignar Ruta (con Polymorphism)
◦ Caso de uso 3: Notificar (con Pure Fabrication)
```
### 3.3 Código Java (Opcional)

Si lo deseas, puedes entregar código Java que implemente los patrones:

- LogiSmartController.java
- Usuario.java (interfaz) + implementaciones
- Notificador.java (interfaz) + implementaciones
- ServicioDeNotificaciones.java
- Otros según sea necesario

## 4. Criterios de Evaluación

```
Criterio Peso Descripción
```
```
Aplicación de Controller 15% ¿Hay un Controller que coordina operaciones? ¿Está bien diseñado?
```
```
Uso de Polymorphism 20% ¿Se eliminaron condicionales con interfaces? ¿Las implementaciones son correctas?
```
```
Pure Fabrication e
Indirection 20%^
```
```
¿Se crearon clases artificiales útiles? ¿Se desacoplaron
componentes?
```
```
Protected Variations 15% ¿Se identificaron puntos de variación? ¿Se protegieron con abstracciones?
```
```
Diagramas de Clases 15% ¿Están actualizados? ¿Muestran todas las relaciones? ¿Son claros?
```
```
Documentación 15% ¿Se justifican las decisiones? ¿Se explican los patrones? ¿Está bien escrito?
```

## 5. Preguntas Guía

Mientras trabajas en el Hito 5, háganse estas preguntas:

```
46 Controller: ¿Quién debería coordinar las operaciones principales? ¿Cómo se
comunica con la UI?
47 Polymorphism: ¿Dónde hay condicionales que podrían reemplazarse? ¿Qué
interfaces necesito?
48 Pure Fabrication: ¿Hay responsabilidades que no encajan en ninguna clase? ¿Qué
clases artificiales necesito?
49 Indirection: ¿Hay acoplamiento fuerte? ¿Qué interfaces podrían desacoplar
componentes?
50 Protected Variations: ¿Qué podría cambiar en el futuro? ¿Cómo protejo el código de
esos cambios?
51 Integración: ¿Cómo trabajan juntos los 5 patrones? ¿El diseño es coherente?
```
## 6. Recursos

- **Presentación:** clase_5_presentacion_v1.html
- **Guión del Orador:** clase_5_guion_v1.md
- **Ejemplos de Código:** Diagramas de Secuencia y Clases
- **Documentación GRASP:** GRASP - General Responsibility Assignment Software
    Patterns

## 7. Entrega

Entrega los siguientes archivos:

```
52 Hito_5_GRASP_Parte_II.md - Documento con análisis y decisiones
53 Diagrama_Clases_Hito5.png - Diagrama de clases actualizado
54 Diagramas_Secuencia_Hito5.png - Diagramas de secuencia
55 (Opcional) Código Java con las implementaciones
```
**Fecha de Entrega:** Según el calendario académico


## 8. Notas Importantes

- **No es perfección:** El objetivo no es crear el diseño perfecto, sino entender cómo los
    patrones mejoran el diseño.
- **Iteración:** Es normal que necesites revisar y ajustar tu diseño varias veces.
- **Documentación:** La documentación es tan importante como el código. Justifica tus
    decisiones.
- **Preguntas:** Si tienes dudas, pregunta. El objetivo es aprender.


