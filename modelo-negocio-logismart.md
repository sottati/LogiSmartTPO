# Modelo de Negocio de LogiSmart

## 1. Resumen ejecutivo

LogiSmart es una plataforma SaaS de optimizacion logistica para PyMEs de manufactura en Argentina.
Nace para resolver un problema concreto: muchas PyMEs gestionan envios con herramientas dispersas (Excel, WhatsApp, email), lo que genera sobrecostos, baja trazabilidad y mala experiencia del cliente final.

La propuesta central de LogiSmart es ofrecer una operacion de envios integrada de punta a punta:

- Importacion de ordenes desde canales de venta
- Creacion y gestion de envios
- Planificacion y asignacion de rutas
- Asignacion de vehiculos o transportistas
- Seguimiento en tiempo real con ETA
- Notificaciones a clientes
- Reportes para gestion y mejora continua

El modelo comercial base es fee mensual, con foco en cantidad de envios procesados y valor entregado por eficiencia operativa.

## 2. Problema de negocio que resuelve

Segun el enunciado del TPO, las PyMEs enfrentan cinco dolores principales:

1. Operacion fragmentada: datos y procesos en distintas herramientas sin integracion.
2. Costos elevados: rutas suboptimas, mala asignacion de recursos y retrabajos.
3. Falta de trazabilidad: baja visibilidad sobre donde esta cada envio.
4. Mala comunicacion con cliente final: incertidumbre, consultas repetidas, reclamos.
5. Riesgo operativo alto: ante fallas de sistema, la operacion se frena.

LogiSmart convierte ese escenario reactivo en una operacion planificada, medible y escalable.

## 3. Propuesta de valor

### Para PyMEs (cliente B2B principal)

- Menor costo logistico por mejor asignacion de rutas y vehiculos.
- Mayor productividad del equipo operativo.
- Menos errores manuales y menos trabajo administrativo.
- Trazabilidad completa de cada envio.
- Informacion para decidir: reportes y metricas.

### Para clientes finales de las PyMEs (usuario indirecto)

- Transparencia del estado del envio.
- ETA estimada y actualizaciones de seguimiento.
- Mejor experiencia de entrega por comunicacion proactiva.

### Para la plataforma (LogiSmart)

- Ingreso recurrente mensual.
- Escalabilidad por modelo SaaS multiempresa.
- Diferenciacion por integracion + optimizacion + tracking.

## 4. Segmentos y stakeholders

## Segmentos de clientes

- PyMEs de manufactura con necesidad de profesionalizar su logistica.
- Empresas con flota propia o esquema mixto con terceros.

## Stakeholders clave

- Direccion de PyME (costo, nivel de servicio, continuidad operativa).
- Operadores logisticos (eficiencia diaria y facilidad de uso).
- Administradores de empresa (configuracion, reportes, suscripcion).
- Clientes finales (informacion y cumplimiento de entrega).
- Administracion de plataforma LogiSmart (alta de empresas, billing, monitoreo).

## 5. Actores y responsabilidades operativas

Tomando el dominio actual, los roles se distribuyen asi:

- Admin de plataforma:
  - Administra empresas.
  - Gestiona facturacion/plataforma.
  - Puede operar transversalmente.
- Admin de empresa:
  - Configura la operacion de su empresa.
  - Consulta reportes.
  - Gestiona suscripcion.
- Operador logistico:
  - Crea envios.
  - Asigna rutas.
  - Gestiona flota.
  - Ejecuta operacion diaria.
- Cliente final:
  - Consulta tracking y ETA.
  - Confirma recepcion.

Esto refleja un modelo de permisos orientado a seguridad operativa y separacion de responsabilidades.

## 6. Capacidades de negocio (que vende y habilita LogiSmart)

LogiSmart no vende solo "software", vende capacidades operativas:

1. Orquestacion de envios
   - Creacion, modificacion, cancelacion y cierre de envios.
2. Planificacion logistica
   - Seleccion de rutas candidatas y asignacion de la mejor opcion.
3. Gestion de capacidad
   - Asignacion de vehiculos segun disponibilidad/capacidad.
4. Tracking y visibilidad
   - Estado actual, historial de posiciones y ETA.
5. Comunicacion automatizada
   - Notificaciones por eventos de negocio (alta, asignacion, cancelacion).
6. Analitica operativa
   - Reportes periodicos y metricas de desempeno.
7. Gobierno comercial
   - Suscripciones, cobros y administracion de cuentas empresa.

## 7. Flujo de valor de punta a punta

Un flujo tipico de negocio dentro de LogiSmart:

1. Ingreso de demanda
   - Las ordenes llegan desde canal comercial (idealmente integrado a e-commerce).
2. Consolidacion operativa
   - El operador crea un envio y asocia ordenes.
3. Planificacion
   - Se evalua ruta optima y se asigna vehiculo/transportista.
4. Ejecucion
   - El envio pasa a estado en curso, se actualiza ETA y tracking.
5. Entrega
   - Se registra resultado (exitosa/fallida), evidencia y observaciones.
6. Cierre y aprendizaje
   - Se generan reportes y metricas para ajustar operacion.

En terminos de valor, el beneficio aparece en dos frentes:

- Eficiencia interna (menos costo por envio).
- Experiencia externa (mejor cumplimiento y comunicacion).

## 8. Modelo de ingresos y monetizacion

El enunciado define una monetizacion base: fee mensual asociado al volumen de envios procesados.

Una forma clara de entenderlo:

- Ingreso recurrente principal: suscripcion mensual por empresa.
- Variable de valor: cantidad de envios y complejidad operativa.
- Mecanica financiera: suscripcion (plan, estado, vencimiento) + cobros (autorizado, pagado, fallido).

Opciones de evolucion comercial (naturales para este negocio):

- Planes por rangos de envios/mes.
- Modulos premium (reporting avanzado, integraciones extra, SLA superior).
- Precio por volumen incremental o por usuario operativo adicional.

## 9. Estructura de costos de LogiSmart (la empresa plataforma)

Para sostener el modelo SaaS, los costos principales suelen ser:

- Infraestructura cloud y disponibilidad.
- Integraciones externas (e-commerce, mapas/trafico, notificaciones).
- Desarrollo y mantenimiento evolutivo.
- Soporte operativo a clientes empresa.
- Seguridad y cumplimiento de datos.

La rentabilidad mejora cuando el costo marginal por nueva empresa baja mas rapido que el ingreso mensual recurrente.

## 10. KPIs de negocio y operacion

Para entender si el modelo funciona, hay que medir:

### KPIs de eficiencia logistica

- Costo promedio por envio.
- Km por entrega completada.
- Utilizacion de flota (% disponibilidad vs uso).
- Tasa de reprogramaciones/cancelaciones.

### KPIs de servicio

- Entregas en tiempo (% On-Time).
- Desvio promedio de ETA.
- Tasa de entrega exitosa en primer intento.
- Tiempo promedio de resolucion de incidencias.

### KPIs comerciales SaaS

- MRR (ingreso mensual recurrente).
- Churn de empresas.
- ARPU por empresa.
- NPS o satisfaccion cliente.

## 11. Atributos de calidad que condicionan el negocio

En LogiSmart, algunos no funcionales no son tecnicos "bonitos": impactan directo en ingresos y reputacion.

1. Disponibilidad alta
   - Si el sistema cae, la operacion de los clientes se detiene.
2. Seguridad de datos
   - Hay informacion sensible de empresas, pedidos y ubicaciones.
3. Escalabilidad
   - Debe soportar crecimiento de empresas, envios y tracking concurrente.
4. Confiabilidad de tracking/ETA
   - La promesa de valor depende de datos consistentes y oportunos.
5. Integrabilidad
   - La adopcion mejora si conecta facil con e-commerce y canales actuales.

## 12. Riesgos de negocio y mitigacion

### Riesgos principales

- Riesgo de adopcion: el cliente no cambia su operacion interna.
- Riesgo tecnico: baja calidad de datos de entrada afecta optimizacion.
- Riesgo economico: sensibilidad de PyMEs al precio.
- Riesgo de dependencia externa: APIs de mapas, mensajeria o terceros.
- Riesgo de continuidad: incidentes de servicio en picos operativos.

### Mitigaciones recomendadas

- Onboarding guiado y templates de operacion.
- Calidad de datos: validaciones tempranas y controles de integridad.
- Estrategia de planes escalonados por madurez del cliente.
- Arquitectura con desacople de proveedores externos.
- Observabilidad, alertas y playbooks de contingencia.

## 13. Alcance MVP vs evolucion

### MVP razonable

- Gestion de usuarios y empresas.
- Alta y gestion de envios.
- Asignacion de rutas y vehiculos.
- Tracking basico + ETA.
- Notificaciones por eventos clave.
- Reportes operativos iniciales.
- Suscripcion y cobro basico.

### Evolucion esperable

- Optimizacion de rutas avanzada con mas variables.
- Integraciones nativas profundas con marketplaces/e-commerce.
- Analitica predictiva (demanda, retrasos, saturacion de flota).
- Motor de recomendaciones de capacidad y pricing.

## 14. Mapa conceptual del dominio del negocio

Para entenderlo de forma simple, el negocio gira alrededor de tres nucleos:

1. Nucleo operativo
   - Orden -> Envio -> Ruta -> Entrega
2. Nucleo de experiencia
   - Seguimiento + ETA + Notificaciones
3. Nucleo comercial
   - Empresa + Suscripcion + Cobro + Reporte

Cuando esos tres nucleos funcionan coordinados, LogiSmart logra su promesa:

- reducir costo logistico,
- aumentar trazabilidad,
- y profesionalizar la operacion de PyMEs con un modelo sostenible de ingreso recurrente.

## 15. Conclusión

LogiSmart es, en esencia, un negocio de eficiencia operativa empaquetada como SaaS.
Su exito depende de equilibrar tres variables:

1. Valor tangible para la PyME (ahorro + control + servicio).
2. Confiabilidad tecnica de la plataforma (disponibilidad + seguridad + datos).
3. Escalabilidad comercial (suscripciones recurrentes con bajo costo marginal).

Si esos pilares se sostienen, LogiSmart puede convertirse en una capa operativa critica para PyMEs, no solo en una herramienta mas.
