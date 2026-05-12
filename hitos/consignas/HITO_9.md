# Hito 9: Patrones Estructurales II

## Patrones a aplicar

- Decorator
- Facade
- Flyweight
- Proxy

## Objetivos

- agregar funcionalidades opcionales a envios sin modificar clases existentes
- simplificar operaciones complejas del sistema logistico
- optimizar memoria en ubicaciones compartidas
- controlar acceso a servicios costosos con lazy loading y cache
- integrar los 4 patrones en un servicio unificado
- documentar decisiones de diseno
- crear casos de prueba completos

## Actividad 1 - Decorator

Entregable minimo:

- interfaz `Envio`
- clase `EnvioBasico`
- clase abstracta `DecoradorEnvio`
- 4 decoradores concretos
- 5 casos de prueba

Servicios opcionales:

- seguro de envio (+15%)
- rastreo GPS (+50)
- notificaciones SMS (+25)
- entrega prioritaria (+100)

## Actividad 2 - Facade

Entregable minimo:

- clase `ServicioLogisticaFacade`
- 5 casos de prueba

Subsistemas a coordinar:

- inventario
- pagos
- notificaciones
- rastreo
- reportes

## Actividad 3 - Flyweight

Entregable minimo:

- clase `Ubicacion` inmutable
- clase `FabricaUbicaciones`
- 5 casos de prueba

Objetivo:

- compartir ubicaciones repetidas para reducir memoria

## Actividad 4 - Proxy

Entregable minimo:

- interfaz `RepositorioEnvios`
- clase `RepositorioEnviosReal`
- clase `ProxyRepositorioEnvios`
- 6 casos de prueba

Objetivo:

- lazy loading
- cache
- validacion de acceso

## Actividad 5 - Integracion

Entregable minimo:

- clase `ServicioLogisticaCompleto`
- caso de prueba integrado

## Requisitos generales

- documento markdown completo
- codigo Java funcional
- diagrama UML actualizado
- minimo 15 casos de prueba funcionando
