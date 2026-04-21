# Práctica de Clase 1 (Hito 1 del TPO): Análisis de Dominio y Casos de Uso

**Tema:** Proceso Unificado, Ingeniería de Requerimientos y Casos de Uso

## Objetivo

El objetivo de esta práctica es aplicar los conceptos del **Proceso Unificado (RUP)** para realizar un análisis
de dominio profundo a partir de un caso de negocio. Siguiendo un enfoque **guiado por Casos de Uso** , los
equipos deberán identificar los actores, stakeholders y los Casos de Uso de negocio más importantes que
entregan valor. Además, analizarán los atributos de calidad (requerimientos no funcionales) que darán
forma a la **arquitectura** del sistema. El resultado será un documento de Visión y Alcance que servirá
como la piedra angular para el diseño del sistema en las siguientes iteraciones.

## Actividades

En grupos de máximo 3 personas, deberán analizar el siguiente caso de negocio y producir el entregable
especificado.

#### Caso de Negocio: "LogiSmart - Plataforma de Optimización Logística para PyMEs"

Una consultora de logística ha detectado una brecha en el mercado: las pequeñas y medianas
empresas (PyMEs) de manufactura en Argentina luchan por competir con grandes corporaciones
debido a ineficiencias en su cadena de suministro. Estas PyMEs gestionan sus envíos y entregas
con herramientas dispares (email, planillas de Excel, WhatsApp), lo que resulta en altos costos de
transporte, falta de trazabilidad de la mercadería, rutas de entrega ineficientes y una pobre
comunicación con los clientes finales. La consultora quiere financiar el desarrollo de una
plataforma SaaS (Software as a Service) llamada "LogiSmart" para abordar este problema. La
visión es ofrecer una solución "todo en uno" que sea accesible económicamente y fácil de
implementar. La plataforma debe integrarse con sistemas de e-commerce populares (como
TiendaNube o MercadoShops) para importar pedidos automáticamente. Debe permitir a las
PyMEs planificar rutas de entrega óptimas para su flota de vehículos (o para transportistas
tercerizados), considerando variables como el tráfico en tiempo real, la capacidad de los vehículos
y las ventanas horarias de entrega de los clientes. Los clientes finales deben poder ver en un mapa
y en tiempo real dónde está su pedido. La monetización del sistema será un fee mensual basado


en la cantidad de envíos procesados. La seguridad de los datos y la alta disponibilidad del servicio
son cruciales, ya que una falla en el sistema podría detener las operaciones de sus clientes.

## Tareas a Realizar

**1. Análisis de Dominio y Requerimientos**
    - **Identificación de Actores y Stakeholders:** Identifiquen todos los actores (quienes interactúan
       con el sistema) y stakeholders (quienes tienen interés en el sistema) relevantes.
    - **Identificación de Casos de Uso:** A partir del caso de negocio, identifiquen un catálogo de al
       menos **8 Casos de Uso** críticos para el MVP. Un Caso de Uso representa una meta que un actor
       quiere alcanzar usando el sistema (Ej: "Planificar Rutas de Entrega", "Consultar Estado de
       Envío").
    - **Descripción de Casos de Uso Clave:** Elijan los **3 Casos de Uso más importantes** de su catálogo y
       descríbanlos en formato breve, incluyendo:
          - Nombre del Caso de Uso
          - Actor(es) Principal(es)
          - Resumen (una breve narrativa de la interacción)
    - **Identificación de Atributos de Calidad (Requerimientos No Funcionales):** Identifiquen y listen al
       menos **5 atributos de calidad** cruciales para el éxito del sistema. Para cada uno, expliquen su
       importancia en el contexto de LogiSmart y cómo podría impactar en la arquitectura.
    - **Análisis de Restricciones y Riesgos:** Identifiquen al menos **3 restricciones** y **3 riesgos** principales
       del proyecto.
**2. Formulación de la Visión y Alcance**
    - **Declaración de la Visión:** Redacten una declaración de visión para el producto que inspire y guíe
       el desarrollo.
    - **Diagrama de Contexto (Nivel 0):** Dibujen un diagrama de contexto que muestre al sistema
       "LogiSmart" y las entidades externas que interactúan con él.
**3. Preparación del Entregable**


- Consoliden toda la información en un único documento (HITO_1).

### Entregable

Un único archivo HITO_1 que contenga:

1. **Nombre del Proyecto e Integrantes.**
2. **Análisis de Dominio:**
    - Lista de Actores y Stakeholders.
    - Catálogo de Casos de Uso del MVP.
    - Descripción detallada de los 3 Casos de Uso principales.
    - Tabla de Atributos de Calidad, con su relevancia y posible impacto arquitectónico.
    - Tabla de Restricciones y Riesgos.
3. **Visión del Producto:**
    - Declaración de la Visión.
4. **Diagrama de Contexto:** (Incrustado como imagen).