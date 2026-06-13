# TPO – Consignas completas

**Proyecto:** LogiSmart – Plataforma de Optimización Logística para PyMEs
**Materia:** Procesos de Desarrollo de Software
**Documento:** compilación fiel de las consignas de todos los hitos (documento general + Hitos 1 a 13).

> Este archivo reúne, hito por hito, lo que pide cada enunciado: objetivo, patrones/artefactos a producir, actividades y entregables. Sirve como insumo único para analizar cuál es el mejor entregable final de LogiSmart.

---

## Documento general del TPO

**Objetivo general.** Desarrollar de forma iterativa e incremental un sistema orientado a objetos aplicando el Proceso Unificado (RUP): guiado por casos de uso, centrado en la arquitectura, e incorporando progresivamente principios de diseño (SOLID, GRASP) y patrones GoF a lo largo de los hitos.

**Modalidad.** Grupos de 3 integrantes. Tema asignado: LogiSmart, una plataforma SaaS de optimización logística para PyMEs (multi-tenant, integración con plataformas de e-commerce como TiendaNube/MercadoShops, optimización de rutas, seguimiento en tiempo real, monetización por fee por envío; la alta disponibilidad y la seguridad de los datos son críticas).

**Estructura por clases → entregables.** Cada clase corresponde a un hito con un entregable asociado. Las clases 16 y 17 son entrega y defensa finales.

**Criterios de evaluación (transversales a todos los hitos):**

- Correcta aplicación de RUP (iterativo-incremental, dirigido por casos de uso, centrado en la arquitectura).
- Aplicación de principios SOLID y patrones GRASP.
- Aplicación correcta y justificada de patrones GoF.
- Justificación de las decisiones de diseño.
- Claridad y calidad de la documentación.
- Progreso incremental coherente entre hitos.

---

## Hito 1 – Análisis de Dominio y Casos de Uso

**Objetivo.** Establecer la visión del producto y el alcance del MVP a partir del análisis del dominio y de los casos de uso.

**Actividades / contenido a producir:**

- Identificación de actores y stakeholders.
- Mínimo 8 casos de uso del MVP, con al menos 3 descritos en detalle.
- Mínimo 5 atributos de calidad relevantes para LogiSmart.
- Mínimo 3 restricciones y los riesgos asociados.
- Documento de visión del producto.
- Diagrama de contexto.

**Entregable:** documento `HITO_1` con actores/stakeholders, casos de uso del MVP (≥8, 3 detallados), atributos de calidad (≥5), restricciones y riesgos (≥3), visión y diagrama de contexto.

---

## Hito 2 – Modelo de Dominio (Diagrama de Clases UML)

**Objetivo.** Construir el modelo de dominio como diagrama de clases UML, derivado de los casos de uso del Hito 1.

**Actividades / contenido a producir:**

- Identificación de clases candidatas.
- Atributos y métodos con su visibilidad.
- Relaciones entre clases (asociación, agregación, composición, herencia, multiplicidades).
- Análisis de cohesión y acoplamiento.
- Justificación de las decisiones de modelado.

**Entregable:** `HITO_2_DIAGRAMA_CLASES` con clases candidatas, atributos/métodos con visibilidad, relaciones, análisis de cohesión/acoplamiento y justificación.

---

## Hito 3 – Implementación en Java (Eclipse)

**Objetivo.** Traducir el modelo de dominio del Hito 2 a código Java en Eclipse.

**Actividades / contenido a producir:**

- Paquete `com.logismart.dominio`.
- Una clase `.java` por cada clase del diagrama UML.
- Atributos privados, constructores, getters/setters.
- Implementación de las relaciones entre clases.
- El proyecto debe compilar sin errores.

**Entregable:** ZIP `HITO_3` con el proyecto Eclipse (paquete de dominio, una clase por clase UML, encapsulamiento, constructores, relaciones, compilación limpia).

---

## Hito 4 – GRASP I (Patrones de Asignación de Responsabilidades, parte 1)

**Objetivo.** Aplicar los primeros patrones GRASP sobre el modelo de dominio.

**Patrones GRASP de esta parte:**

- Experto en información (Information Expert).
- Creador (Creator).
- Bajo acoplamiento (Low Coupling).
- Alta cohesión (High Cohesion).

**Actividades / contenido a producir:**

- Documento explicando la aplicación de cada patrón.
- Diagrama de clases actualizado.
- 2–3 diagramas de secuencia que muestren la asignación de responsabilidades.

**Entregable:** documento `Hito4_GRASP_Parte1` + diagrama de clases actualizado + diagramas de secuencia.

---

## Hito 5 – GRASP II (Patrones de Asignación de Responsabilidades, parte 2)

**Objetivo.** Completar la aplicación de los patrones GRASP restantes e introducir abstracciones e indirecciones.

**Patrones GRASP de esta parte:**

- Controlador (Controller).
- Polimorfismo (Polymorphism).
- Fabricación Pura (Pure Fabrication).
- Indirección (Indirection).
- Variaciones Protegidas (Protected Variations).

**Elementos introducidos:**

- `LogiSmartController` como controlador.
- Interfaces: `Usuario`, `Notificador`, `Vehiculo`, `CalculadorDeRuta`.
- Clases de Fabricación Pura: `ServicioDeNotificaciones`, `CalculadorDeRutas`, `RepositorioDeEnvios`, `ValidadorDeEnvios`.
- Variantes de `CalculadorDeCosto`.

**Entregable:** `Hito_5_GRASP_Parte_II.md` + diagramas + código Java (opcional).

---

## Hito 6 – Patrones Creacionales I

**Objetivo.** Aplicar los primeros patrones creacionales GoF.

**Patrones:**

- **Singleton:** `ConexionBD`, `Logger`.
- **Factory Method:** `FabricaDeEnvios`, `FabricaDeNotificadores`, `FabricaDeVehiculos`.

**Entregable:** `Hito_6_Patrones_Creacionales.md` + código Java + diagramas.

---

## Hito 7 – Patrones Creacionales II

**Objetivo.** Completar los patrones creacionales GoF.

**Patrones:**

- **Abstract Factory:** `LogiSmartFactory` con familias Argentina / Brasil.
- **Builder:** `EnvioBuilder`.
- **Prototype:** `Envio.clone()`.

**Entregable:** proyecto Eclipse `LogiSmartHito7` + `README.md` + `DOCUMENTACION.md` + diagramas.

---

## Hito 8 – Patrones Estructurales I

**Objetivo.** Aplicar los primeros patrones estructurales GoF para integrar servicios y estructuras jerárquicas.

**Patrones:**

- **Adapter:** integración de couriers (DHL/FedEx/UPS) y pasarelas de pago (PayPal/Stripe).
- **Bridge:** `Reporte` × `GeneradorReporte` (PDF/Excel/JSON/CSV).
- **Composite:** jerarquía `CentroDistribucion` / `CentroRegional` / `PuntoEntrega`.

**Integración:** `ServicioLogisticaUnificado`.

**Entregable:** `Hito_8_Patrones_Estructurales_I.md` + código Java + diagramas.

---

## Hito 9 – Patrones Estructurales II

**Objetivo.** Completar los patrones estructurales GoF, agregando comportamiento dinámico y optimización.

**Patrones:**

- **Decorator:** `DecoradorSeguro`, `RastreoGPS`, `NotificacionesSMS`, `Prioritario`.
- **Facade:** `ServicioLogisticaFacade`.
- **Flyweight:** `Ubicacion` + `FabricaUbicaciones`.
- **Proxy:** `ProxyRepositorioEnvios` (lazy loading + caché).

**Integración:** `ServicioLogisticaCompleto` (13 clases Java).

**Entregable:** documento `.md` + 13 clases Java + diagrama.

---

## Hito 10 – Patrones de Comportamiento I

**Objetivo.** Aplicar los primeros patrones de comportamiento GoF.

**Patrones:**

- **Chain of Responsibility:** `ValidadorEnvio` + 5 validadores (Datos / Inventario / Pago / Seguridad / Capacidad) + `CadenaValidadores`.
- **Command:** interfaz `Comando` + 5 comandos + `ColaComandos` con undo/redo.
- **Interpreter:** `Expresion` + 5 terminales + 3 no-terminales (AND / OR / NOT).

**Integración:** `SistemaLogisticaCompleto`. Total ~24 clases Java.

**Entregable:** documento `Hito_10` (`.md`) + código Java + diagramas.

---

## Hito 11 – Patrones de Comportamiento II

**Patrones:** Iterator, Mediator, Memento, Observer.

**Contexto.** Tras el Hito 10, se aplican patrones que definen cómo los objetos se comunican y cambian: Iterator (acceder a colecciones sin exponer su estructura), Mediator (centralizar la comunicación entre componentes), Memento (capturar y restaurar estados) y Observer (notificar cambios automáticamente).

**Objetivos del hito:**

- Implementar Iterator para múltiples colecciones.
- Implementar Mediator para comunicación centralizada.
- Implementar Memento para historial de estados.
- Implementar Observer para notificaciones.
- Crear una arquitectura event-driven.
- Integrar los 4 patrones en un sistema unificado.
- Documentar decisiones de diseño.
- Crear 25+ casos de prueba.

**Actividades:**

1. **Iterator – múltiples colecciones.** Interfaz `IteradorEnvios` (`tieneSiguiente`, `obtenerSiguiente`, `reiniciar`) e interfaz `ColeccionEnvios` (`crearIterador`, `agregar`, `remover`, `obtenerTamaño`). Colecciones concretas: `ColeccionArray`, `ColeccionLista` (lista enlazada), `ColeccionHash`. Entregable: 2 interfaces + 3 colecciones + 5 casos de prueba.
2. **Mediator – comunicación centralizada.** Interfaz `MediadorEnvios` y `MediadorEnviosConcreto` coordinando 5 componentes: `CentroDistribucion`, `ValidadorEnvio`, `SistemaPago`, `SistemaNotificacion`, `SistemaAuditoria`. Entregable: interfaz + mediador + 5 componentes + 5 casos de prueba.
3. **Memento – historial de estados.** `MementoEnvio`, `Envio` (originador, con `crearMemento` / `restaurarDesdeMemento`), `HistorialEnvios` (cuidador, con undo/redo sobre estados CONFIRMADO → EN_TRANSITO → EN_REPARTO → ENTREGADO). Entregable: 3 clases + 5 casos de prueba.
4. **Observer – notificaciones automáticas.** Interfaz `ObservadorEnvio`, sujeto `Envio` (adjuntar/desadjuntar/notificar), y 4 observadores: `CentroDistribucionObservador`, `SistemaNotificacionObservador`, `SistemaAuditoriaObservador`, `DashboardObservador`. Entregable: interfaz + sujeto + 4 observadores + 5 casos de prueba.
5. **Integración event-driven.** `SistemaLogisticaEventDriven` que combina Iterator + Mediator + Memento + Observer.

**Entregables del hito:**

- Documento Markdown (descripción de cada patrón, implementación paso a paso, casos de prueba, decisiones de diseño, análisis de ventajas/desventajas, arquitectura event-driven).
- Código Java: Iterator 5 clases + Mediator 7 clases + Memento 3 clases + Observer 6 clases + Integración 1 clase = **22 clases Java**.
- Diagramas UML: estructura de Iterator, Mediator, Memento, Observer y arquitectura event-driven.

---

## Hito 12 – Patrones de Comportamiento III

**Patrones:** State, Strategy, Template Method, Visitor.

**Contexto.** Patrones que definen cómo los objetos cambian de comportamiento y se procesan: State (cambiar comportamiento según estado interno), Strategy (seleccionar algoritmo en tiempo de ejecución), Template Method (definir el esqueleto de un algoritmo) y Visitor (procesar elementos de una estructura sin modificarla).

**Objetivos del hito:**

- Implementar State para la máquina de estados de envíos.
- Implementar Strategy para múltiples cálculos de costo.
- Implementar Template Method para procesos de envío.
- Implementar Visitor para análisis de estructura.
- Crear 30+ casos de prueba.
- Integrar los 4 patrones en un sistema unificado.
- Documentar decisiones de diseño y demostrar ejercicios de selección de patrones.

**Actividades:**

1. **State – máquina de estados de envíos.** Interfaz `EstadoEnvio` (`validar`, `entregar`, `cancelar`, `retener`, `devolver`, `reclamar`, `obtenerNombre`). 6 estados concretos: `EstadoConfirmado`, `EstadoEnTransito`, `EstadoEnReparto`, `EstadoEntregado`, `EstadoRetenido`, `EstadoCancelado`. Contexto `Envio`. Entregable: interfaz + 6 estados + contexto + 5 casos de prueba.
2. **Strategy – cálculo de costos.** Interfaz `EstrategiaCalculoCosto`. 5 estrategias: `EstrategiaDistancia`, `EstrategiaPeso`, `EstrategiaUrgencia`, `EstrategiaVolumen`, `EstrategiaHibrida`. Contexto `Envio` con `establecerEstrategia`/`calcularCosto`. Entregable: interfaz + 5 estrategias + contexto + 5 casos de prueba.
3. **Template Method – procesos de envío.** Clase abstracta `ProcesoProcesosEnvio` con el método plantilla `procesarEnvio` (validar → calcularCosto → procesarPago → notificar). Subclases: `ProcesoNacional`, `ProcesoInternacional`, `ProcesoUrgente`. Entregable: clase abstracta + 3 subclases + 5 casos de prueba.
4. **Visitor – análisis de estructura.** Interfaz `VisitorCentro` (`visitar(PuntoEntrega)`, `visitar(CentroRegional)`) e interfaz elemento `CentroDistribucion` (`aceptar`). Elementos: `PuntoEntrega`, `CentroRegional`. 4 visitors: `VisitorCalculoOcupacion`, `VisitorGeneradorReporte`, `VisitorCalculoCostoOperativo`, `VisitorBusquedaPuntosCriticos`. Entregable: 2 interfaces + 2 elementos + 4 visitors + 5 casos de prueba.
5. **Integración completa.** `SistemaLogisticaAvanzada` que combina State + Strategy + Template Method + Visitor.

**Entregables del hito:**

- Documento Markdown (descripción de cada patrón, implementación paso a paso, casos de prueba, decisiones de diseño, análisis de ventajas/desventajas, integración).
- Código Java: State 7 clases + Strategy 6 clases + Template Method 4 clases + Visitor 8 clases + Integración 1 clase = **26 clases Java**.
- Diagramas UML: estructura de State, Strategy, Template Method, Visitor e integración completa.

---

## Hito 13 – Patrones de Acceso a Datos (Hito final)

**Patrones:** Data Mapper, Repository, Unit of Work, Lazy Load.

**Contexto.** Hito final del TPO. Tras implementar todos los patrones de diseño (creacionales, estructurales y de comportamiento), se integran patrones de acceso a datos para una arquitectura profesional y escalable. **Objetivo:** diseñar una arquitectura lógica completa que separe la lógica de negocio de la persistencia, usando todos los patrones de acceso a datos.

**Objetivos del hito:**

- Implementar Data Mapper para mapear objetos a la BD.
- Implementar Repository para acceso uniforme a datos.
- Implementar Unit of Work para gestionar transacciones.
- Implementar Lazy Load para optimizar rendimiento.
- Diseñar una arquitectura lógica completa e integrar todos los patrones anteriores.
- Crear 40+ casos de prueba y documentar toda la arquitectura.

**Actividades:**

1. **Data Mapper – mapeo de entidades.** Entidades de dominio puras: `Envio`, `Cliente`, `CentroDistribucion`, `Pago` (+ enums `EstadoEnvio`, `EstadoPago`). Data mappers: `EnvioMapper`, `ClienteMapper`, `CentroDistribucionMapper`, `PagoMapper` (CRUD vía JDBC). Entregable: 4 entidades puras + 4 mappers + 5 casos de prueba.
2. **Repository – acceso uniforme.** Interfaz genérica `Repositorio<T>` (`guardar`, `actualizar`, `eliminar`, `obtener`, `obtenerTodos`) e interfaces específicas `RepositorioEnvio`, `RepositorioCliente`, `RepositorioCentro`, `RepositorioPago`. Implementaciones SQL y en memoria (para tests). Entregable: 4 interfaces + 4 implementaciones SQL + 4 implementaciones en memoria + 5 casos de prueba.
3. **Unit of Work – gestión de transacciones.** Clase `UnitOfWork` que registra entidades nuevas/modificadas/eliminadas y aplica `commit`/`rollback` atómico sobre los repositorios. Entregable: clase `UnitOfWork` completa + métodos de registro + commit/rollback + 5 casos de prueba.
4. **Lazy Load – carga bajo demanda.** Proxies: `ClienteLazyProxy`, `CentroDistribucionLazyProxy`, `HistorialEnviosLazyProxy` (cargan desde el repositorio recién al primer acceso). Entregable: 3 proxies + métodos de carga bajo demanda + 5 casos de prueba.
5. **Arquitectura lógica completa.** Servicios de aplicación `ServicioEnvios`, `ServicioClientes`, `ServicioCentros`, `ServicioPagos` y fachada `LogisticaFacade`. Entregable: 4 servicios + 1 fachada + 5 casos de prueba.

**Arquitectura objetivo (capas):**

```
Presentación (Controladores, UI)
        ↓
Aplicación (Servicios: Envios, Clientes, Centros, Pagos; Fachada: LogisticaFacade)
        ↓
Dominio (Entidades: Envio, Cliente, Centro, Pago)
        ↓
Persistencia (Unit of Work, Repository, Data Mapper, Lazy Load)
        ↓
Datos (Base de Datos SQL)
```

**Entregables finales del hito:**

- Documento Markdown (descripción de cada patrón, implementación paso a paso, casos de prueba, decisiones de diseño, análisis de ventajas/desventajas, integración de patrones y reflexión sobre la arquitectura).
- Código Java: Data Mapper 8 clases + Repository 12 clases + Unit of Work 1 clase + Lazy Load 3 clases + Servicios 4 clases + Fachada 1 clase = **29 clases Java**.
- Diagrama arquitectónico de las 5 capas.

---

## Resumen de patrones por hito

| Hito | Categoría | Patrones / artefactos |
|------|-----------|------------------------|
| 1 | Análisis | Actores, casos de uso, atributos de calidad, restricciones/riesgos, visión, contexto |
| 2 | Modelado | Diagrama de clases UML (dominio) |
| 3 | Implementación | Java en Eclipse (paquete de dominio) |
| 4 | GRASP I | Experto, Creador, Bajo Acoplamiento, Alta Cohesión |
| 5 | GRASP II | Controlador, Polimorfismo, Fabricación Pura, Indirección, Variaciones Protegidas |
| 6 | Creacionales I | Singleton, Factory Method |
| 7 | Creacionales II | Abstract Factory, Builder, Prototype |
| 8 | Estructurales I | Adapter, Bridge, Composite |
| 9 | Estructurales II | Decorator, Facade, Flyweight, Proxy |
| 10 | Comportamiento I | Chain of Responsibility, Command, Interpreter |
| 11 | Comportamiento II | Iterator, Mediator, Memento, Observer |
| 12 | Comportamiento III | State, Strategy, Template Method, Visitor |
| 13 | Acceso a Datos | Data Mapper, Repository, Unit of Work, Lazy Load |
