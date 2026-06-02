<!-- pagina 1 -->
Hito 13: Patrones de Acceso a Datos 
Data Mapper, Repository, Unit of Work, Lazy Load 
Proyecto: LogiSmart - Sistema de Gestión de Logística 
Formato: Documento Markdown + Código Java + Diagrama Arquitectónico 
Contexto del Hito 
Este es el Hito 13 (final) del TPO. Después de implementar todos los patrones de diseño 
(creacionales, estructurales y de comportamiento), ahora integrarás patrones de acceso a 
datos para crear una arquitectura profesional y escalable. 
 
Objetivo: Diseñar una arquitectura lógica completa que separe la lógica de negocio de la 
persistencia, usando todos los patrones de acceso a datos. 
 
Objetivos del Hito 
Al completar este hito, habrás: 
 
• 
  Implementado Data Mapper para mapear objetos a BD 
• 
  Implementado Repository para acceso uniforme a datos 
• 
  Implementado Unit of Work para gestionar transacciones 
• 
  Implementado Lazy Load para optimizar rendimiento 
• 
  Diseñado una arquitectura lógica completa 
• 
  Integrado todos los patrones anteriores 
• 
  Creado 40+ casos de prueba 
• 
  Documentado toda la arquitectura 
 
Actividades Prácticas 
Actividad 1: Data Mapper - Mapeo de Entidades 
Contexto: LogiSmart necesita mapear sus entidades principales a la BD: 
1 Envio 
2 Cliente 
3 CentroDistribucion 
4 Pago 
 


<!-- pagina 2 -->
Paso 1: Entidades de Dominio (Puras) 
 
// Entidad: Envio
 
public
 class
 Envio  {  
    
private
 int  id;  
    
private
 String origen;
 
    
private
 String destino;
 
    
private
 double  peso;  
    
private
 LocalDateTime fechaCreacion;
 
    
private
 EstadoEnvio estado;
 
    
 
    
public
 Envio ( int  id , String 
origen
, String 
destino
, double  peso ) {
 
        
this
.id 
= id;  
        
this
.origen 
= origen;
 
        
this
.destino 
= destino;
 
        
this
.peso 
= peso;  
        
this
.fechaCreacion 
= LocalDateTime.
now();  
        
this
.estado 
= EstadoEnvio.CONFIRMADO;
 
    
}  
    
 
    
// Getters
 
    
public
 int  getId
() { 
return
 id; }
 
    
public
 String 
getOrigen
() { 
return
 origen; }
 
    
public
 String 
getDestino
() { 
return
 destino; }
 
    
public
 double  getPeso
() { 
return
 peso; }
 
    
public
 LocalDateTime 
getFechaCreacion
() { 
return
 fechaCreacion; }
 
    
public
 EstadoEnvio 
getEstado
() { 
return
 estado; }
 
    
 
    
// Setters
 
    
public
 void  setEstado
(EstadoEnvio 
estado ) { 
this
.estado 
= estado; }
 
}  
  
// Entidad: Cliente
 
public
 class
 Cliente
 {  
    
private
 int  id;  
    
private
 String nombre;
 
    
private
 String email;
 
    
private
 String telefono;
 
    
 
    
public
 Cliente
( int  id , String 
nombre , String 
email , String 
telefono
) 
{  
        
this
.id 
= id;  
        
this
.nombre 
= nombre;  
        
this
.email 
= email;
 
        
this
.telefono 
= telefono;
 
    
}  
    
 
    
// Getters
 
    
public
 int  getId
() { 
return
 id; }
 


<!-- pagina 3 -->
    
public
 String 
getNombre () { 
return
 nombre; }
 
    
public
 String 
getEmail
() { 
return
 email; }
 
    
public
 String 
getTelefono
() { 
return
 telefono; }
 
}  
  
// Entidad: Centro de Distribución
 
public
 class
 CentroDistribucion
 {  
    
private
 int  id;  
    
private
 String nombre;
 
    
private
 String ciudad;
 
    
private
 double  capacidad;
 
    
private
 double  ocupacion;
 
    
 
    
public
 CentroDistribucion
( int  id , String 
nombre , String 
ciudad
, 
double  capacidad
) {
 
        
this
.id 
= id;  
        
this
.nombre 
= nombre;  
        
this
.ciudad 
= ciudad;
 
        
this
.capacidad 
= capacidad;
 
        
this
.ocupacion 
= 0;  
    
}  
    
 
    
// Getters y Setters
 
    
public
 int  getId
() { 
return
 id; }
 
    
public
 String 
getNombre () { 
return
 nombre; }
 
    
public
 String 
getCiudad
() { 
return
 ciudad; }
 
    
public
 double  getCapacidad
() { 
return
 capacidad; }
 
    
public
 double  getOcupacion
() { 
return
 ocupacion; }
 
    
public
 void  setOcupacion
( double  ocupacion
) { 
this
.ocupacion 
= 
ocupacion; }
 
}  
  
// Entidad: Pago
 
public
 class
 Pago  {  
    
private
 int  id;  
    
private
 int  envioId;
 
    
private
 double  monto;  
    
private
 LocalDateTime fecha;
 
    
private
 EstadoPago estado;
 
    
 
    
public
 Pago ( int  id , int  envioId
, double  monto ) {
 
        
this
.id 
= id;  
        
this
.envioId 
= envioId;
 
        
this
.monto 
= monto;  
        
this
.fecha 
= LocalDateTime.
now();  
        
this
.estado 
= EstadoPago.PENDIENTE;
 
    
}  
    
 
    
// Getters y Setters
 
    
public
 int  getId
() { 
return
 id; }
 
    
public
 int  getEnvioId
() { 
return
 envioId; }
 


<!-- pagina 4 -->
    
public
 double  getMonto () { 
return
 monto; }
 
    
public
 LocalDateTime 
getFecha
() { 
return
 fecha; }
 
    
public
 EstadoPago 
getEstado
() { 
return
 estado; }
 
    
public
 void  setEstado
(EstadoPago 
estado ) { 
this
.estado 
= estado; }
 
}  
  
// Enums
 
public
 enum EstadoEnvio
 {  
    
CONFIRMADO, EN_TRANSITO , EN_REPARTO , ENTREGADO , CANCELADO , RETENIDO  
}  
  
public
 enum EstadoPago
 {  
    
PENDIENTE , PROCESANDO , COMPLETADO, RECHAZADO  
}  
 
Paso 2: Data Mappers 
 
// Data Mapper: Envio
 
public
 class
 EnvioMapper
 {  
    
private
 Connection conexion;
 
    
 
    
public
 EnvioMapper
(Connection 
conexion
) {
 
        
this
.conexion 
= conexion;
 
    
}  
    
 
    
public
 void  insert
(Envio 
envio ) {
 
        
String sql 
= "INSERT INTO envios (id, origen, destino, peso, 
fecha_creacion, estado) VALUES (?, ?, ?, ?, ?, ?)"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, envio.
getId
());
 
            
stmt.
setString
( 2, envio.
getOrigen
());
 
            
stmt.
setString
( 3, envio.
getDestino
());
 
            
stmt.
setDouble
( 4, envio.
getPeso
());
 
            
stmt.
setTimestamp
( 5, 
Timestamp.
valueOf
(envio.
getFechaCreacion
()));
 
            
stmt.
setString
( 6, envio.
getEstado
().
toString
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Envío insertado: "
 + envio.
getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al insertar envío: "
 + 
e. getMessage
());
 
        
}  
    
}  
    
 
    
public
 Envio 
findById
( int  id ) {
 
        
String sql 
= "SELECT * FROM envios WHERE id=?"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 


<!-- pagina 5 -->
            
stmt.
setInt
( 1, id);
 
            
ResultSet rs 
= stmt.
executeQuery
();  
            
if  (rs.
next ()) {
 
                
Envio envio 
= new Envio (  
                    
rs. getInt
( "id" ),  
                    
rs. getString
( "origen"
),  
                    
rs. getString
( "destino"
),  
                    
rs. getDouble
( "peso" )  
                
);  
                
envio.
setEstado
(EstadoEnvio.
valueOf
(rs.
getString
( "estado"
)));
 
                
return
 envio;
 
            
}  
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al buscar envío: "
 + 
e. getMessage
());
 
        
}  
        
return
 null
;  
    
}  
    
 
    
public
 void  update (Envio 
envio ) {
 
        
String sql 
= "UPDATE envios SET origen=?, destino=?, peso=?, 
estado=? 
WHERE id=?"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setString
( 1, envio.
getOrigen
());
 
            
stmt.
setString
( 2, envio.
getDestino
());
 
            
stmt.
setDouble
( 3, envio.
getPeso
());
 
            
stmt.
setString
( 4, envio.
getEstado
().
toString
());
 
            
stmt.
setInt
( 5, envio.
getId
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Envío actualizado: "
 + envio.
getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al actualizar envío: "
 + 
e. getMessage
());
 
        
}  
    
}  
    
 
    
public
 void  delete
(Envio 
envio ) {
 
        
String sql 
= "DELETE FROM envios WHERE id=?"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, envio.
getId
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Envío eliminado: "
 + envio.
getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al eliminar envío: "
 + 
e. getMessage
());
 
        
}  
    
}  
}  
  
// Data Mapper: Cliente
 


<!-- pagina 6 -->
public
 class
 ClienteMapper
 {  
    
private
 Connection conexion;
 
    
 
    
public
 ClienteMapper
(Connection 
conexion
) {
 
        
this
.conexion 
= conexion;
 
    
}  
    
 
    
public
 void  insert
(Cliente 
cliente
) {
 
        
String sql 
= "INSERT INTO clientes (id, nombre, email, telefono) 
VALUES (?, ?, ?, ?)"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, cliente.
getId
());
 
            
stmt.
setString
( 2, cliente.
getNombre ());
 
            
stmt.
setString
( 3, cliente.
getEmail
());
 
            
stmt.
setString
( 4, cliente.
getTelefono
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Cliente insertado: "
 + 
cliente.
getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al insertar cliente: "
 + 
e. getMessage
());
 
        
}  
    
}  
    
 
    
public
 Cliente 
findById
( int  id ) {
 
        
String sql 
= "SELECT * FROM clientes WHERE id=?"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, id);
 
            
ResultSet rs 
= stmt.
executeQuery
();  
            
if  (rs.
next ()) {
 
                
return
 new Cliente
(  
                    
rs. getInt
( "id" ),  
                    
rs. getString
( "nombre" ),  
                    
rs. getString
( "email"
),  
                    
rs. getString
( "telefono"
)  
                
);  
            
}  
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al buscar cliente: "
 + 
e. getMessage
());
 
        
}  
        
return
 null
;  
    
}  
}  
  
// Data Mapper: Centro de Distribución
 
public
 class
 CentroDistribucionMapper
 {  
    
private
 Connection conexion;
 
    
 
    
public
 CentroDistribucionMapper
(Connection 
conexion
) {
 
        
this
.conexion 
= conexion;
 


<!-- pagina 7 -->
    
}  
    
 
    
public
 void  insert
(CentroDistribucion 
centro
) {
 
        
String sql 
= "INSERT INTO centros (id, nombre, ciudad, capacidad, 
ocupacion) VALUES (?, ?, ?, ?, ?)"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, centro.
getId
());
 
            
stmt.
setString
( 2, centro.
getNombre ());
 
            
stmt.
setString
( 3, centro.
getCiudad
());
 
            
stmt.
setDouble
( 4, centro.
getCapacidad
());
 
            
stmt.
setDouble
( 5, centro.
getOcupacion
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Centro insertado: "
 + centro.
getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al insertar centro: "
 + 
e. getMessage
());
 
        
}  
    
}  
    
 
    
public
 CentroDistribucion 
findById
( int  id ) {
 
        
String sql 
= "SELECT * FROM centros WHERE id=?"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, id);
 
            
ResultSet rs 
= stmt.
executeQuery
();  
            
if  (rs.
next ()) {
 
                
CentroDistribucion centro 
= new CentroDistribucion
(  
                    
rs. getInt
( "id" ),  
                    
rs. getString
( "nombre" ),  
                    
rs. getString
( "ciudad"
),  
                    
rs. getDouble
( "capacidad"
)  
                
);  
                
centro.
setOcupacion
(rs.
getDouble
( "ocupacion"
));  
                
return
 centro;
 
            
}  
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al buscar centro: "
 + 
e. getMessage
());
 
        
}  
        
return
 null
;  
    
}  
}  
  
// Data Mapper: Pago
 
public
 class
 PagoMapper  {  
    
private
 Connection conexion;
 
    
 
    
public
 PagoMapper (Connection 
conexion
) {
 
        
this
.conexion 
= conexion;
 
    
}  
    
 
    
public
 void  insert
(Pago 
pago ) {
 


<!-- pagina 8 -->
        
String sql 
= "INSERT INTO pagos (id, envio_id, monto, fecha, 
estado) VALUES (?, ?, ?, ?, ?)"
;  
        
try  (PreparedStatement stmt 
= conexion.
prepareStatement
(sql)) {
 
            
stmt.
setInt
( 1, pago.
getId
());
 
            
stmt.
setInt
( 2, pago.
getEnvioId
());
 
            
stmt.
setDouble
( 3, pago.
getMonto ());
 
            
stmt.
setTimestamp
( 4, Timestamp.
valueOf
(pago.
getFecha
()));
 
            
stmt.
setString
( 5, pago.
getEstado
().
toString
());
 
            
stmt.
executeUpdate
();  
            
System.out.
println
( "✓ Pago insertado: "
 + pago. getId
());
 
        
} catch  (SQLException 
e) {
 
            
System.out.
println
( " ✗ Error al insertar pago: "
 + 
e. getMessage
());
 
        
}  
    
}  
}  
 
Entregable: 
• 
4 entidades de dominio puras 
• 
4 data mappers 
• 
5 casos de prueba 
 
Actividad 2: Repository - Acceso Uniforme 
Contexto: Crear interfaces Repository para acceso uniforme a datos. 
 
Paso 1: Interfaces Repository 
 
// Interfaz genérica
 
public
 interface
 Repositorio
<T> {  
    
void  guardar
(T entidad
);  
    
void  actualizar
(T entidad
);  
    
void  eliminar
(T entidad
);  
    
T obtener
( int  id );  
    
List<
T> obtenerTodos
();  
}  
  
// Interfaz específica: Envio
 
public
 interface
 RepositorioEnvio
 extends
 Repositorio
<Envio > {  
    
List<
Envio > buscarPorEstado
(EstadoEnvio 
estado );  
    
List<
Envio > buscarPorOrigen
(String 
origen
);  
    
List<
Envio > buscarPorDestino
(String 
destino
);  
}  
  
// Interfaz específica: Cliente
 


<!-- pagina 9 -->
public
 interface
 RepositorioCliente
 extends
 Repositorio
<Cliente
> {  
    
Cliente 
buscarPorEmail
(String 
email );  
    
List<
Cliente
> buscarPorNombre
(String 
nombre );  
}  
  
// Interfaz específica: Centro
 
public
 interface
 RepositorioCentro
 extends
 
Repositorio
<CentroDistribucion
> {  
    
List<
CentroDistribucion
> buscarPorCiudad
(String 
ciudad
);  
    
CentroDistribucion 
buscarPorNombre
(String 
nombre );  
}  
  
// Interfaz específica: Pago
 
public
 interface
 RepositorioPago
 extends
 Repositorio
<Pago > {  
    
List<
Pago > buscarPorEnvio
( int  envioId
);  
    
List<
Pago > buscarPorEstado
(EstadoPago 
estado );  
}  
 
Paso 2: Implementaciones SQL 
 
// Implementación: RepositorioEnvioSQL
 
public
 class
 RepositorioEnvioSQL
 implements
 RepositorioEnvio
 {  
    
private
 EnvioMapper mapper;
 
    
 
    
public
 RepositorioEnvioSQL
(Connection 
conexion
) {
 
        
this
.mapper 
= new EnvioMapper
(conexion);
 
    
}  
    
 
    
@Override
 
    
public
 void  guardar
(Envio 
envio ) {
 
        
mapper. insert
(envio);
 
    
}  
    
 
    
@Override
 
    
public
 void  actualizar
(Envio 
envio ) {
 
        
mapper. update (envio);
 
    
}  
    
 
    
@Override
 
    
public
 void  eliminar
(Envio 
envio ) {
 
        
mapper. delete
(envio);
 
    
}  
    
 
    
@Override
 
    
public
 Envio 
obtener
( int  id ) {
 
        
return
 mapper. findById
(id);
 
    
}  


<!-- pagina 10 -->
    
 
    
@Override
 
    
public
 List<
Envio > obtenerTodos
() {
 
        
// Implementación
 
        
return
 new ArrayList<>();
 
    
}  
    
 
    
@Override
 
    
public
 List<
Envio > buscarPorEstado
(EstadoEnvio 
estado ) {
 
        
// Implementación
 
        
return
 new ArrayList<>();
 
    
}  
    
 
    
@Override
 
    
public
 List<
Envio > buscarPorOrigen
(String 
origen
) {
 
        
// Implementación
 
        
return
 new ArrayList<>();
 
    
}  
    
 
    
@Override
 
    
public
 List<
Envio > buscarPorDestino
(String 
destino
) {
 
        
// Implementación
 
        
return
 new ArrayList<>();
 
    
}  
}  
  
// Implementación: RepositorioClienteSQL
 
public
 class
 RepositorioClienteSQL
 implements
 RepositorioCliente
 {  
    
private
 ClienteMapper mapper;
 
    
 
    
public
 RepositorioClienteSQL
(Connection 
conexion
) {
 
        
this
.mapper 
= new ClienteMapper
(conexion);
 
    
}  
    
 
    
@Override
 
    
public
 void  guardar
(Cliente 
cliente
) {
 
        
mapper. insert
(cliente);
 
    
}  
    
 
    
@Override
 
    
public
 Cliente 
obtener
( int  id ) {
 
        
return
 mapper. findById
(id);
 
    
}  
    
 
    
@Override
 
    
public
 Cliente 
buscarPorEmail
(String 
email ) {
 
        
// Implementación
 
        
return
 null
;  
    
}  
    
 
    
// Otros métodos...
 


<!-- pagina 11 -->
}  
  
// Implementación: RepositorioCentroSQL
 
public
 class
 RepositorioCentroSQL
 implements
 RepositorioCentro
 {  
    
private
 CentroDistribucionMapper mapper;
 
    
 
    
public
 RepositorioCentroSQL
(Connection 
conexion
) {
 
        
this
.mapper 
= new CentroDistribucionMapper
(conexion);
 
    
}  
    
 
    
@Override
 
    
public
 void  guardar
(CentroDistribucion 
centro
) {
 
        
mapper. insert
(centro);
 
    
}  
    
 
    
@Override
 
    
public
 CentroDistribucion 
obtener
( int  id ) {
 
        
return
 mapper. findById
(id);
 
    
}  
    
 
    
// Otros métodos...
 
}  
  
// Implementación: RepositorioPagoSQL
 
public
 class
 RepositorioPagoSQL
 implements
 RepositorioPago
 {  
    
private
 PagoMapper mapper;
 
    
 
    
public
 RepositorioPagoSQL
(Connection 
conexion
) {
 
        
this
.mapper 
= new PagoMapper (conexion);
 
    
}  
    
 
    
@Override
 
    
public
 void  guardar
(Pago 
pago ) {
 
        
mapper. insert
(pago);
 
    
}  
    
 
    
// Otros métodos...
 
}  
 
Paso 3: Implementaciones en Memoria (para tests) 
 
// Implementación: RepositorioEnvioMemoria
 
public
 class
 RepositorioEnvioMemoria
 implements
 RepositorioEnvio
 {  
    
private
 Map<Integer
, Envio > almacen 
= new HashMap<>();
 
    
 
    
@Override
 
    
public
 void  guardar
(Envio 
envio ) {
 


<!-- pagina 12 -->
        
almacen.
put (envio.
getId
(), envio);
 
        
System.out.
println
( "✓ Envío guardado en memoria"
);  
    
}  
    
 
    
@Override
 
    
public
 Envio 
obtener
( int  id ) {
 
        
return
 almacen.
get (id);
 
    
}  
    
 
    
@Override
 
    
public
 List<
Envio > obtenerTodos
() {
 
        
return
 new ArrayList<>(almacen.
values
());
 
    
}  
    
 
    
// Otros métodos...
 
}  
  
// Implementación: RepositorioClienteMemoria
 
public
 class
 RepositorioClienteMemoria
 implements
 RepositorioCliente
 {  
    
private
 Map<Integer
, Cliente
> almacen 
= new HashMap<>();
 
    
 
    
@Override
 
    
public
 void  guardar
(Cliente 
cliente
) {
 
        
almacen.
put (cliente.
getId
(), cliente);
 
        
System.out.
println
( "✓ Cliente guardado en memoria"
);  
    
}  
    
 
    
// Otros métodos...
 
}  
 
Entregable: 
• 
4 interfaces Repository 
• 
4 implementaciones SQL 
• 
4 implementaciones en Memoria 
• 
5 casos de prueba 
 
Actividad 3: Unit of Work - Gestión de Transacciones 
Contexto: Coordinar múltiples cambios en objetos con consistencia transaccional. 
 
Paso 1: Unit of Work 
 
public
 class
 UnitOfWork
 {  
    
private
 Connection conexion;
 


<!-- pagina 13 -->
    
private
 List<
Envio > enviosNuevos 
= new ArrayList<>();
 
    
private
 List<
Envio > enviosModificados 
= new ArrayList<>();
 
    
private
 List<
Envio > enviosEliminados 
= new ArrayList<>();
 
    
 
    
private
 List<
Cliente
> clientesNuevos 
= new ArrayList<>();
 
    
private
 List<
Cliente
> clientesModificados 
= new ArrayList<>();
 
    
private
 List<
Cliente
> clientesEliminados 
= new ArrayList<>();
 
    
 
    
private
 List<
CentroDistribucion
> centrosNuevos 
= new ArrayList<>();
 
    
private
 List<
CentroDistribucion
> centrosModificados 
= new 
ArrayList<>();
 
    
private
 List<
CentroDistribucion
> centrosEliminados 
= new 
ArrayList<>();
 
    
 
    
private
 List<
Pago > pagosNuevos 
= new ArrayList<>();
 
    
private
 List<
Pago > pagosModificados 
= new ArrayList<>();
 
    
private
 List<
Pago > pagosEliminados 
= new ArrayList<>();
 
    
 
    
private
 RepositorioEnvio repositorioEnvio;
 
    
private
 RepositorioCliente repositorioCliente;
 
    
private
 RepositorioCentro repositorioCentro;
 
    
private
 RepositorioPago repositorioPago;
 
    
 
    
public
 UnitOfWork
(Connection 
conexion
,  
                      
RepositorioEnvio 
repositorioEnvio
,  
                      
RepositorioCliente 
repositorioCliente
,  
                      
RepositorioCentro 
repositorioCentro
,  
                      
RepositorioPago 
repositorioPago
) {
 
        
this
.conexion 
= conexion;
 
        
this
.repositorioEnvio 
= repositorioEnvio;
 
        
this
.repositorioCliente 
= repositorioCliente;
 
        
this
.repositorioCentro 
= repositorioCentro;
 
        
this
.repositorioPago 
= repositorioPago;
 
    
}  
    
 
    
// Registrar Envios
 
    
public
 void  registrarNuevoEnvio
(Envio 
envio ) {
 
        
enviosNuevos.
add (envio);
 
        
System.out.
println
( "✓ Envío registrado como nuevo: "
 + 
envio.
getId
());
 
    
}  
    
 
    
public
 void  registrarModificadoEnvio
(Envio 
envio ) {
 
        
enviosModificados.
add (envio);
 
        
System.out.
println
( "✓ Envío registrado como modificado: "
 + 
envio.
getId
());
 
    
}  
    
 
    
public
 void  registrarEliminadoEnvio
(Envio 
envio ) {
 
        
enviosEliminados.
add (envio);
 


<!-- pagina 14 -->
        
System.out.
println
( "✓ Envío registrado como eliminado: "
 + 
envio.
getId
());
 
    
}  
    
 
    
// Registrar Clientes
 
    
public
 void  registrarNuevoCliente
(Cliente 
cliente
) {
 
        
clientesNuevos.
add (cliente);
 
        
System.out.
println
( "✓ Cliente registrado como nuevo: "
 + 
cliente.
getId
());
 
    
}  
    
 
    
public
 void  registrarModificadoCliente
(Cliente 
cliente
) {
 
        
clientesModificados.
add (cliente);
 
        
System.out.
println
( "✓ Cliente registrado como modificado: "
 + 
cliente.
getId
());
 
    
}  
    
 
    
// Registrar Centros
 
    
public
 void  registrarNuevoCentro
(CentroDistribucion 
centro
) {
 
        
centrosNuevos.
add (centro);
 
        
System.out.
println
( "✓ Centro registrado como nuevo: "
 + 
centro.
getId
());
 
    
}  
    
 
    
public
 void  registrarModificadoCentro
(CentroDistribucion 
centro
) {
 
        
centrosModificados.
add (centro);
 
        
System.out.
println
( "✓ Centro registrado como modificado: "
 + 
centro.
getId
());
 
    
}  
    
 
    
// Registrar Pagos
 
    
public
 void  registrarNuevoPago
(Pago 
pago ) {
 
        
pagosNuevos.
add (pago);
 
        
System.out.
println
( "✓ Pago registrado como nuevo: "
 + 
pago. getId
());
 
    
}  
    
 
    
public
 void  registrarModificadoPago
(Pago 
pago ) {
 
        
pagosModificados.
add (pago);
 
        
System.out.
println
( "✓ Pago registrado como modificado: "
 + 
pago. getId
());
 
    
}  
    
 
    
// Commit (transacción)
 
    
public
 void  commit () {
 
        
try  {  
            
conexion.
setAutoCommit
( false
);  
            
 
            
// Procesar Envios
 
            
for  (Envio envio 
:  enviosNuevos) {
 
                
repositorioEnvio.
guardar
(envio);
 


<!-- pagina 15 -->
            
}  
            
for  (Envio envio 
:  enviosModificados) {
 
                
repositorioEnvio.
actualizar
(envio);
 
            
}  
            
for  (Envio envio 
:  enviosEliminados) {
 
                
repositorioEnvio.
eliminar
(envio);
 
            
}  
            
 
            
// Procesar Clientes
 
            
for  (Cliente cliente 
:  clientesNuevos) {
 
                
repositorioCliente.
guardar
(cliente);
 
            
}  
            
for  (Cliente cliente 
:  clientesModificados) {
 
                
repositorioCliente.
actualizar
(cliente);
 
            
}  
            
 
            
// Procesar Centros
 
            
for  (CentroDistribucion centro 
:  centrosNuevos) {
 
                
repositorioCentro.
guardar
(centro);
 
            
}  
            
for  (CentroDistribucion centro 
:  centrosModificados) {
 
                
repositorioCentro.
actualizar
(centro);
 
            
}  
            
 
            
// Procesar Pagos
 
            
for  (Pago pago 
:  pagosNuevos) {
 
                
repositorioPago.
guardar
(pago);
 
            
}  
            
for  (Pago pago 
:  pagosModificados) {
 
                
repositorioPago.
actualizar
(pago);
 
            
}  
            
 
            
conexion.
commit ();  
            
System.out.
println
( "✓ Transacción completada"
);  
            
limpiar
();  
        
} catch  (SQLException 
e) {
 
            
try  {  
                
conexion.
rollback
();  
                
System.out.
println
( " ✗ Transacción revertida: "
 + 
e. getMessage
());
 
            
} catch  (SQLException 
ex ) {
 
                
ex. printStackTrace
();  
            
}  
        
}  
    
}  
    
 
    
// Rollback
 
    
public
 void  rollback
() {
 
        
try  {  
            
conexion.
rollback
();  
            
System.out.
println
( "✓ Cambios revertidos"
);  


<!-- pagina 16 -->
            
limpiar
();  
        
} catch  (SQLException 
e) {
 
            
e. printStackTrace
();  
        
}  
    
}  
    
 
    
// Limpiar
 
    
private
 void  limpiar
() {
 
        
enviosNuevos.
clear
();  
        
enviosModificados.
clear
();  
        
enviosEliminados.
clear
();  
        
clientesNuevos.
clear
();  
        
clientesModificados.
clear
();  
        
clientesEliminados.
clear
();  
        
centrosNuevos.
clear
();  
        
centrosModificados.
clear
();  
        
centrosEliminados.
clear
();  
        
pagosNuevos.
clear
();  
        
pagosModificados.
clear
();  
        
pagosEliminados.
clear
();  
    
}  
}  
 
Paso 2: Casos de prueba 
 
// Caso 1: Transacción exitosa
 
UnitOfWork unitOfWork 
= new UnitOfWork
(conexion, repositorioEnvio, 
 
                                       
repositorioCliente, 
repositorioCentro, 
 
                                       
repositorioPago);
 
  
Envio envio1 
= new Envio ( 1, "Buenos Aires"
, "Córdoba"
, 5.0 );  
Cliente cliente1 
= new Cliente
( 1, "Juan Pérez"
, "juan@email.com"
, 
"1234567890"
);  
Pago pago1 
= new Pago ( 1, 1, 500.0 );  
  
unitOfWork.
registrarNuevoEnvio
(envio1);
 
unitOfWork.
registrarNuevoCliente
(cliente1);
 
unitOfWork.
registrarNuevoPago
(pago1);
 
unitOfWork.
commit ();  
  
// Caso 2: Transacción con modificaciones
 
unitOfWork.
registrarModificadoEnvio
(envio1);
 
unitOfWork.
registrarModificadoCliente
(cliente1);
 
unitOfWork.
commit ();  
  
// Caso 3: Rollback en error
 


<!-- pagina 17 -->
unitOfWork.
registrarNuevoEnvio
( new Envio ( 2, "Mendoza" , "San Juan"
, 3.0 ));  
unitOfWork.
rollback
();  
  
// Caso 4: Múltiples entidades
 
// Registrar múltiples envíos, clientes, centros y pagos
 
// Commit atómico
 
  
// Caso 5: Consistencia transaccional
 
// Verificar que todos los cambios se guardaron o ninguno
 
 
Entregable: 
• 
Clase UnitOfWork completa 
• 
Métodos para registrar cambios 
• 
Métodos commit y rollback 
• 
5 casos de prueba 
 
Actividad 4: Lazy Load - Carga Bajo Demanda 
Contexto: Optimizar rendimiento cargando datos bajo demanda. 
 
Paso 1: Proxies Lazy Load 
 
// Proxy Lazy Load: Cliente
 
public
 class
 ClienteLazyProxy
 {  
    
private
 int  id;  
    
private
 Cliente cliente;
 
    
private
 RepositorioCliente repositorio;
 
    
private
 boolean
 cargado 
= false
;  
    
 
    
public
 ClienteLazyProxy
( int  id , RepositorioCliente 
repositorio
) {
 
        
this
.id 
= id;  
        
this
.repositorio 
= repositorio;
 
    
}  
    
 
    
private
 void  cargar
() {
 
        
if  ( ! cargado) {
 
            
this
.cliente 
= repositorio.
obtener
(id);
 
            
this
.cargado 
= true ;  
            
System.out.
println
( "✓ Cliente cargado desde BD: "
 + id);
 
        
}  
    
}  
    
 
    
public
 String 
getNombre () {
 
        
cargar
();
 
        
return
 cliente.
getNombre ();  


<!-- pagina 18 -->
    
}  
    
 
    
public
 String 
getEmail
() {
 
        
cargar
();
 
        
return
 cliente.
getEmail
();  
    
}  
    
 
    
public
 String 
getTelefono
() {
 
        
cargar
();
 
        
return
 cliente.
getTelefono
();  
    
}  
}  
  
// Proxy Lazy Load: Centro
 
public
 class
 CentroDistribucionLazyProxy
 {  
    
private
 int  id;  
    
private
 CentroDistribucion centro;
 
    
private
 RepositorioCentro repositorio;
 
    
private
 boolean
 cargado 
= false
;  
    
 
    
public
 CentroDistribucionLazyProxy
( int  id , RepositorioCentro 
repositorio
) {
 
        
this
.id 
= id;  
        
this
.repositorio 
= repositorio;
 
    
}  
    
 
    
private
 void  cargar
() {
 
        
if  ( ! cargado) {
 
            
this
.centro 
= repositorio.
obtener
(id);
 
            
this
.cargado 
= true ;  
            
System.out.
println
( "✓ Centro cargado desde BD: "
 + id);
 
        
}  
    
}  
    
 
    
public
 String 
getNombre () {
 
        
cargar
();
 
        
return
 centro.
getNombre ();  
    
}  
    
 
    
public
 String 
getCiudad
() {
 
        
cargar
();
 
        
return
 centro.
getCiudad
();  
    
}  
    
 
    
public
 double  getCapacidad
() {
 
        
cargar
();
 
        
return
 centro.
getCapacidad
();  
    
}  
    
 
    
public
 double  getOcupacion
() {
 
        
cargar
();
 


<!-- pagina 19 -->
        
return
 centro.
getOcupacion
();  
    
}  
}  
  
// Proxy Lazy Load: Historial de Envios
 
public
 class
 HistorialEnviosLazyProxy
 {  
    
private
 int  clienteId;
 
    
private
 List<
Envio > historial;
 
    
private
 RepositorioEnvio repositorio;
 
    
private
 boolean
 cargado 
= false
;  
    
 
    
public
 HistorialEnviosLazyProxy
( int  clienteId
, RepositorioEnvio 
repositorio
) {
 
        
this
.clienteId 
= clienteId;
 
        
this
.repositorio 
= repositorio;
 
    
}  
    
 
    
private
 void  cargar
() {
 
        
if  ( ! cargado) {
 
            
this
.historial 
= repositorio.
obtenerTodos
(); 
// Simulación
 
            
this
.cargado 
= true ;  
            
System.out.
println
( "✓ Historial cargado desde BD"
);  
        
}  
    
}  
    
 
    
public
 List<
Envio > obtener
() {
 
        
cargar
();
 
        
return
 historial;
 
    
}  
    
 
    
public
 int  contar
() {
 
        
cargar
();
 
        
return
 historial.
size
();  
    
}  
}  
 
Paso 2: Casos de prueba 
 
// Caso 1: Crear proxy sin cargar
 
ClienteLazyProxy cliente 
= new ClienteLazyProxy
( 1, repositorioCliente);
 
System.out.
println
( "Proxy creado, cliente NO cargado"
);  
  
// Caso 2: Usar proxy (carga aquí)
 
String nombre 
= cliente.
getNombre ();  
// ← Carga desde BD
 
System.out.
println
( "Nombre: "
 + nombre);
 
  
// Caso 3: Acceso posterior (sin recargar)
 


<!-- pagina 20 -->
String email 
= cliente.
getEmail
();  
// ← Sin recargar
 
System.out.
println
( "Email: "
 + email);
 
  
// Caso 4: Proxy de Centro
 
CentroDistribucionLazyProxy centro 
= new CentroDistribucionLazyProxy
( 1, 
repositorioCentro);
 
String nombreCentro 
= centro.
getNombre ();  
// ← Carga aquí
 
  
// Caso 5: Proxy de Historial
 
HistorialEnviosLazyProxy historial 
= new HistorialEnviosLazyProxy
( 1, 
repositorioEnvio);
 
int  cantidad 
= historial.
contar
();  
// ← Carga aquí
 
System.out.
println
( "Total envíos: "
 + cantidad);
 
 
Entregable: 
• 
3 proxies Lazy Load 
• 
Métodos de carga bajo demanda 
• 
5 casos de prueba 
 
Actividad 5: Arquitectura Lógica Completa 
Contexto: Integrar todos los patrones en una arquitectura profesional. 
 
Paso 1: Servicios de Aplicación 
 
// Servicio: Gestión de Envios
 
public
 class
 ServicioEnvios
 {  
    
private
 RepositorioEnvio repositorio;
 
    
private
 UnitOfWork unitOfWork;
 
    
 
    
public
 ServicioEnvios
(RepositorioEnvio 
repositorio
, UnitOfWork 
unitOfWork
) {
 
        
this
.repositorio 
= repositorio;
 
        
this
.unitOfWork 
= unitOfWork;
 
    
}  
    
 
    
public
 void  crearEnvio
(String 
origen
, String 
destino
, double  peso ) {
 
        
Envio envio 
= new Envio ( 1, origen, destino, peso);
 
        
unitOfWork.
registrarNuevoEnvio
(envio);
 
        
unitOfWork.
commit ();  
        
System.out.
println
( "✓ Envío creado"
);  
    
}  
    
 
    
public
 Envio 
obtenerEnvio
( int  id ) {
 
        
return
 repositorio.
obtener
(id);
 


<!-- pagina 21 -->
    
}  
    
 
    
public
 List<
Envio > obtenerEnviosPorEstado
(EstadoEnvio 
estado ) {
 
        
return
 repositorio.
buscarPorEstado
(estado);
 
    
}  
}  
  
// Servicio: Gestión de Clientes
 
public
 class
 ServicioClientes
 {  
    
private
 RepositorioCliente repositorio;
 
    
private
 UnitOfWork unitOfWork;
 
    
 
    
public
 ServicioClientes
(RepositorioCliente 
repositorio
, UnitOfWork 
unitOfWork
) {
 
        
this
.repositorio 
= repositorio;
 
        
this
.unitOfWork 
= unitOfWork;
 
    
}  
    
 
    
public
 void  crearCliente
(String 
nombre , String 
email , String 
telefono
) {
 
        
Cliente cliente 
= new Cliente
( 1, nombre, email, telefono);
 
        
unitOfWork.
registrarNuevoCliente
(cliente);
 
        
unitOfWork.
commit ();  
        
System.out.
println
( "✓ Cliente creado"
);  
    
}  
    
 
    
public
 Cliente 
obtenerCliente
( int  id ) {
 
        
return
 repositorio.
obtener
(id);
 
    
}  
}  
  
// Servicio: Gestión de Centros
 
public
 class
 ServicioCentros
 {  
    
private
 RepositorioCentro repositorio;
 
    
private
 UnitOfWork unitOfWork;
 
    
 
    
public
 ServicioCentros
(RepositorioCentro 
repositorio
, UnitOfWork 
unitOfWork
) {
 
        
this
.repositorio 
= repositorio;
 
        
this
.unitOfWork 
= unitOfWork;
 
    
}  
    
 
    
public
 void  crearCentro
(String 
nombre , String 
ciudad
, double  
capacidad
) {
 
        
CentroDistribucion centro 
= new CentroDistribucion
( 1, nombre, 
ciudad, capacidad);
 
        
unitOfWork.
registrarNuevoCentro
(centro);
 
        
unitOfWork.
commit ();  
        
System.out.
println
( "✓ Centro creado"
);  
    
}  
    
 


<!-- pagina 22 -->
    
public
 CentroDistribucion 
obtenerCentro
( int  id ) {
 
        
return
 repositorio.
obtener
(id);
 
    
}  
}  
  
// Servicio: Gestión de Pagos
 
public
 class
 ServicioPagos
 {  
    
private
 RepositorioPago repositorio;
 
    
private
 UnitOfWork unitOfWork;
 
    
 
    
public
 ServicioPagos
(RepositorioPago 
repositorio
, UnitOfWork 
unitOfWork
) {
 
        
this
.repositorio 
= repositorio;
 
        
this
.unitOfWork 
= unitOfWork;
 
    
}  
    
 
    
public
 void  procesarPago
( int  envioId
, double  monto ) {
 
        
Pago pago 
= new Pago ( 1, envioId, monto);
 
        
unitOfWork.
registrarNuevoPago
(pago);
 
        
unitOfWork.
commit ();  
        
System.out.
println
( "✓ Pago procesado"
);  
    
}  
}  
 
Paso 2: Fachada de Aplicación 
 
public
 class
 LogisticaFacade
 {  
    
private
 ServicioEnvios servicioEnvios;
 
    
private
 ServicioClientes servicioClientes;
 
    
private
 ServicioCentros servicioCentros;
 
    
private
 ServicioPagos servicioPagos;
 
    
 
    
public
 LogisticaFacade
(ServicioEnvios 
servicioEnvios
,  
                           
ServicioClientes 
servicioClientes
,  
                           
ServicioCentros 
servicioCentros
,  
                           
ServicioPagos 
servicioPagos
) {
 
        
this
.servicioEnvios 
= servicioEnvios;
 
        
this
.servicioClientes 
= servicioClientes;
 
        
this
.servicioCentros 
= servicioCentros;
 
        
this
.servicioPagos 
= servicioPagos;
 
    
}  
    
 
    
public
 void  procesarEnvioCompleto
(String 
origen
, String 
destino
, 
double  peso ,  
                                      
String 
nombreCliente
, String 
email , 
String 
telefono
,  
                                      
double  monto ) {
 


<!-- pagina 23 -->
        
// Crear cliente
 
        
servicioClientes.
crearCliente
(nombreCliente, email, telefono);
 
        
 
        
// Crear envío
 
        
servicioEnvios.
crearEnvio
(origen, destino, peso);
 
        
 
        
// Procesar pago
 
        
servicioPagos.
procesarPago
( 1, monto);
 
        
 
        
System.out.
println
( "✓ Proceso completo de envío"
);  
    
}  
}  
 
Paso 3: Casos de prueba 
 
// Caso 1: Crear envío simple
 
servicioEnvios.
crearEnvio
( "Buenos Aires"
, "Córdoba"
, 5.0 );  
  
// Caso 2: Crear cliente
 
servicioClientes.
crearCliente
( "Juan Pérez"
, "juan@email.com"
, 
"1234567890"
);  
  
// Caso 3: Procesar pago
 
servicioPagos.
procesarPago
( 1, 500.0 );  
  
// Caso 4: Usar fachada para proceso completo
 
LogisticaFacade facada 
= new LogisticaFacade
(servicioEnvios, 
servicioClientes, 
 
                                              
servicioCentros, 
servicioPagos);
 
facada.
procesarEnvioCompleto
( "Buenos Aires"
, "Córdoba"
, 5.0 ,  
                             
"Juan Pérez"
, "juan@email.com"
, 
"1234567890"
,  
                             
500.0 );  
  
// Caso 5: Obtener con lazy load
 
ClienteLazyProxy cliente 
= new ClienteLazyProxy
( 1, repositorioCliente);
 
String nombre 
= cliente.
getNombre ();  
 
Entregable: 
• 
4 servicios de aplicación 
• 
1 fachada 
• 
5 casos de prueba 
 


<!-- pagina 24 -->
Entregables Finales 
Documento Markdown 
Incluir: 
• 
☐Descripción de cada patrón 
• 
☐Implementación paso a paso 
• 
☐Casos de prueba 
• 
☐Decisiones de diseño 
• 
☐Análisis de ventajas/desventajas 
• 
☐Integración de patrones 
• 
☐Reflexión sobre arquitectura 
 
Código Java 
Incluir: 
• 
☐Data Mapper: 4 entidades + 4 mappers = 8 clases 
• 
☐Repository: 4 interfaces + 4 impl SQL + 4 impl Memoria = 12 clases 
• 
☐Unit of Work: 1 clase 
• 
☐Lazy Load: 3 proxies = 3 clases 
• 
☐Servicios: 4 servicios = 4 clases 
• 
☐Fachada: 1 clase 
 
Total: 29 clases Java 
 
Diagrama Arquitectónico 
┌─────────────────────────────────────────────────────────┐ 
│ Capa de Presentación                                    │ 
│ (Controladores, Interfaz de Usuario)                   │ 
└─────────────────────────────────────────────────────────┘ 
                        ↓ 
┌─────────────────────────────────────────────────────────┐ 
│ Capa de Aplicación                                      │ 
│ (Servicios: Envios, Clientes, Centros, Pagos)         │ 
│ (Fachada: LogisticaFacade)                             │ 
└─────────────────────────────────────────────────────────┘ 
                        ↓ 
┌─────────────────────────────────────────────────────────┐ 
│ Capa de Dominio                                         │ 
│ (Entidades: Envio, Cliente, Centro, Pago)             │ 
└─────────────────────────────────────────────────────────┘ 
                        ↓ 


<!-- pagina 25 -->
┌─────────────────────────────────────────────────────────┐ 
│ Capa de Persistencia                                    │ 
│ ├─ Unit of Work (Transacciones)                        │ 
│ ├─ Repositorio (Acceso uniforme)                       │ 
│ ├─ Data Mapper (Mapeo objeto-BD)                       │ 
│ └─ Lazy Load (Carga bajo demanda)                      │ 
└─────────────────────────────────────────────────────────┘ 
                        ↓ 
┌─────────────────────────────────────────────────────────┐ 
│ Capa de Datos                                           │ 
│ (Base de Datos SQL)                                     │ 
└─────────────────────────────────────────────────────────┘ 
 
Criterios de Evaluación 
Criterio 
Excelente 
Bueno 
Aceptable 
Insuficiente 
Data Mapper 
4 mappers completos 
3 mappers 
2 mappers 
1 o menos 
Repository 
4 interfaces + 8 impl 
4 interfaces + 4 impl 
3 interfaces 
2 o menos 
Unit of Work 
Completo, transacciones 
Completo 
Básico 
Incompleto 
Lazy Load 
3 proxies funcionales 
2 proxies 
1 proxy 
Ninguno 
Servicios 
4 servicios + fachada 
4 servicios 
3 servicios 
2 o menos 
Diagramas 
Arquitectura completa 
Buena estructura 
Básica 
Incompleta 
Código 
Limpio, documentado 
Limpio 
Funcional 
Con errores 
Documentación 
Completa, decisiones 
Buena 
Básica 
Incompleta 
Casos de Prueba 
40+ casos, todos funcionan 
35+ casos 
30+ casos 
Menos de 30 
 
