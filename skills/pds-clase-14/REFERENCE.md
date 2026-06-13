# Clase 14: Patrones de Acceso a Datos

## ¿Por qué patrones de acceso a datos?

Sin estos patrones, el código de negocio se mezcla con SQL, lo que causa: tests que requieren base de datos real, cambios en la BD que afectan toda la aplicación, imposibilidad de cambiar de BD sin reescribir código, y duplicación de lógica de persistencia.

---

## Data Mapper

### Problema
¿Cómo mantener objetos de dominio independientes de la estructura de la base de datos?

### Solución
Crear un **Mapper** que traduce entre objetos de dominio y registros de BD. El objeto de dominio es puro: no sabe nada de SQL. El mapper hace todo el trabajo de traducción.

### Estructura
- **Objeto de dominio (puro)**: `Envio` con id, origen, destino, peso. Sin métodos de persistencia.
- **EnvioMapper**: recibe `Connection` en el constructor. Métodos: `insert(envio)`, `update(envio)`, `delete(envio)`, `findById(id)`. Cada uno construye el SQL con `PreparedStatement` y traduce `ResultSet` → `Envio`.

### Ventajas
Objeto de dominio sin SQL; fácil de testear (mock del mapper); cambios en BD no afectan el objeto; reutilizable para diferentes bases de datos.

### Desventajas
Código adicional (mapper por entidad); overhead de traducción; duplicación de propiedades entre clase y tabla.

---

## Repository

### Problema
¿Cómo proporcionar una interfaz uniforme y consistente para acceder a datos, independientemente de dónde vengan?

### Solución
Crear una **interfaz Repository** que define operaciones genéricas. La lógica de negocio trabaja contra la interfaz; la implementación concreta (SQL, memoria) es intercambiable.

### Estructura
- **RepositorioEnvio (interfaz)**: `guardar()`, `actualizar()`, `eliminar()`, `obtener(id)`, `obtenerTodos()`, `buscar(criterio)`.
- **RepositorioEnvioSQL**: implementa la interfaz usando `EnvioMapper` internamente.
- **RepositorioEnvioMemoria**: implementa la interfaz con un `Map<Integer, Envio>` en memoria. Ideal para tests sin base de datos real.
- **ServicioEnvios**: recibe `RepositorioEnvio` por inyección de dependencia; no sabe si es SQL o memoria.

### Ventajas
Interfaz consistente; fácil cambiar de implementación sin tocar lógica de negocio; testeable con implementación en memoria.

### Desventajas
Interfaz genérica puede ser limitante para queries muy específicas; overhead de abstracción.

### Relación con Data Mapper
Repository abstrae *qué* datos acceder. Data Mapper abstrae *cómo* mapear esos datos. En una arquitectura completa, `RepositorioEnvioSQL` usa `EnvioMapper` internamente.

---

## Unit of Work

### Problema
Si tres operaciones de guardado se ejecutan independientemente y la tercera falla, los primeros dos quedan inconsistentes en la BD.

### Solución
Registrar todos los cambios primero, y ejecutarlos en una única transacción atómica: todo o nada.

### Estructura
- **UnitOfWork**: tres listas (`nuevos`, `modificados`, `eliminados`) y referencia al `RepositorioEnvio` y `Connection`.
- **Registro**: `registrarNuevo(envio)`, `registrarModificado(envio)`, `registrarEliminado(envio)` solo agregan a las listas.
- **commit()**: desactiva autocommit, ejecuta todos los cambios en orden (insert → update → delete), llama `conexion.commit()`. Si hay excepción, llama `rollback()` y limpia las listas.
- **rollback()**: revierte la transacción y limpia las listas.

### Cuándo usar
Operaciones que involucran múltiples entidades que deben ser atómicas; necesitás que múltiples repositorios participen en la misma transacción.

### Desventajas
Complejidad adicional; requiere gestión manual del ciclo de vida del Unit of Work.

---

## Lazy Load

### Problema
Al obtener un `Envio`, cargar también el cliente, su historial completo (1000 registros) y sus documentos (100 archivos) cuando solo necesitabas el ID del envío es un desperdicio.

### Solución
Crear un **Proxy** que inicialmente no carga el objeto real. Solo lo carga cuando se accede a alguno de sus datos.

### Estructura
- **Cliente (objeto real)**: POJO normal con `getNombre()`, `getEmail()`.
- **ClienteLazyProxy**: guarda el `id` y el `repositorio` pero no carga el cliente. Tiene flag `cargado = false`. Método privado `cargar()`: si no está cargado, llama al repositorio y marca `cargado = true`. Todos los métodos públicos llaman `cargar()` antes de delegar al cliente real.

### Cuándo usar
Datos relacionados que pueden no usarse en la mayoría de los casos; objetos con datos grandes (imágenes, documentos); relaciones con muchos registros.

### Desventajas
Puede causar el problema N+1 queries (iterar una colección y activar Lazy Load en cada elemento); complejidad adicional.

---

## Arquitectura Lógica en Capas

Los cuatro patrones se integran en una arquitectura de cinco capas:

```
Presentación (Controlador)
      ↓
Aplicación (Servicio)
      ↓
Dominio (Entidades puras)
      ↓
Persistencia (Repository + Unit of Work + Data Mapper)
      ↓
Datos (Base de datos SQL)
```

**Flujo completo**: el controlador llama al servicio → el servicio crea la entidad y llama a `unitOfWork.registrarNuevo()` → en `commit()` el UoW llama al repository → el repository llama al mapper → el mapper ejecuta SQL.

**Principios clave**: separación de responsabilidades por capa; cambios en BD no afectan dominio; cada capa testeable de forma independiente.

---

## Comparación

| Patrón | Propósito | Complejidad | Cuándo usar |
|--------|-----------|-------------|-------------|
| Data Mapper | Independencia objeto-BD | Media | Modelo de objetos diferente a schema de BD |
| Repository | Interfaz uniforme de acceso | Media | Abstraer fuente de datos, facilitar tests |
| Unit of Work | Transaccionalidad | Alta | Múltiples cambios que deben ser atómicos |
| Lazy Load | Rendimiento bajo demanda | Media | Datos grandes que pueden no usarse |

**Combinación típica**: Repository usa Data Mapper internamente; Unit of Work coordina múltiples Repositories; Lazy Load se aplica a relaciones costosas dentro de las entidades que devuelve el Repository.
