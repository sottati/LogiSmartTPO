<!-- pagina 1 -->
Hito 10: Patrones de Comportamiento I 
Chain of Responsibility, Command, Interpreter 
Proyecto: LogiSmart - Sistema de Gestión de Logística 
Formato: Documento Markdown + Código Java 
Contexto del Hito 
Después de implementar patrones estructurales (Hitos 8 y 9), ahora aplicarás patrones de 
comportamiento que definen cómo los objetos interactúan: 
 
1 Chain of Responsibility: Validar envíos a través de múltiples niveles 
2 Command: Encapsular operaciones para undo/redo y logging 
3 Interpreter: Definir reglas de negocio complejas 
 
Objetivos del Hito 
Al completar este hito, habrás: 
 
• 
  Implementado Chain of Responsibility para validación en cadena 
• 
  Implementado Command para operaciones reversibles 
• 
  Implementado Interpreter para reglas de negocio 
• 
  Creado diagramas de secuencia para cada patrón 
• 
  Integrado los 3 patrones en un sistema unificado 
• 
  Documentado decisiones de diseño 
• 
  Creado 20+ casos de prueba 
 
Actividades Prácticas 
Actividad 1: Chain of Responsibility - Validación en Cadena 
Contexto: LogiSmart necesita validar envíos a través de múltiples niveles antes de 
confirmar: 
1 Validador de Datos (origen, destino, peso válidos) 
2 Validador de Inventario (hay stock disponible) 
3 Validador de Pago (método de pago válido) 
4 Validador de Seguridad (no hay restricciones) 
5 Validador de Capacidad (hay espacio en centros) 
 


<!-- pagina 2 -->
Paso 1: Define la interfaz del validador 
 
public
 abstract
 class
 ValidadorEnvio
 {  
    
protected
 ValidadorEnvio siguiente;
 
    
 
    
public
 void  setSiguiente
(ValidadorEnvio 
siguiente
) {
 
        
this
.siguiente 
= siguiente;
 
    
}  
    
 
    
/**  
     
* Valida el envío o lo pasa al siguiente validador
 
     
*/  
    
public
 abstract
 boolean
 validar
(Envio 
envio );  
    
 
    
/**  
     
* Obtiene el nombre del validador
 
     
*/  
    
public
 abstract
 String 
obtenerNombre
();  
}  
 
Paso 2: Implementa validadores concretos 
 
// Validador 1: Datos
 
public
 class
 ValidadorDatos
 extends
 ValidadorEnvio
 {  
    
@Override
 
    
public
 boolean
 validar
(Envio 
envio ) {
 
        
System.out.
println
( "["  + obtenerNombre
() 
+ "] Validando..."
);  
        
 
        
if  (envio.
getOrigen
() 
==  null
 ||  envio.
getOrigen
().
isEmpty
()) {
 
            
System.err.
println
( " ✗ Origen inválido"
);  
            
return
 false
;  
        
}  
        
 
        
if  (envio.
getDestino
() 
==  null
 ||  envio.
getDestino
().
isEmpty
()) {
 
            
System.err.
println
( " ✗ Destino inválido"
);  
            
return
 false
;  
        
}  
        
 
        
if  (envio.
getPeso
() 
<=  0) {
 
            
System.err.
println
( " ✗ Peso inválido"
);  
            
return
 false
;  
        
}  
        
 
        
System.out.
println
( "✓ Datos válidos"
);  


<!-- pagina 3 -->
        
 
        
if  (siguiente 
!=  null
) {
 
            
return
 siguiente.
validar
(envio);
 
        
}  
        
return
 true ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ValidadorDatos"
;  
    
}  
}  
  
// Validador 2: Inventario
 
public
 class
 ValidadorInventario
 extends
 ValidadorEnvio
 {  
    
private
 SistemaInventario inventario;
 
    
 
    
public
 ValidadorInventario
(SistemaInventario 
inventario
) {
 
        
this
.inventario 
= inventario;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 validar
(Envio 
envio ) {
 
        
System.out.
println
( "["  + obtenerNombre
() 
+ "] Verificando 
stock..."
);  
        
 
        
if  ( ! inventario.
verificarStock
(envio.
getProductoId
())) {
 
            
System.err.
println
( " ✗ Stock insuficiente"
);  
            
return
 false
;  
        
}  
        
 
        
System.out.
println
( "✓ Stock disponible"
);  
        
 
        
if  (siguiente 
!=  null
) {
 
            
return
 siguiente.
validar
(envio);
 
        
}  
        
return
 true ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ValidadorInventario"
;  
    
}  
}  
  
// Validador 3: Pago
 
public
 class
 ValidadorPago
 extends
 ValidadorEnvio
 {  
    
@Override
 
    
public
 boolean
 validar
(Envio 
envio ) {
 
        
System.out.
println
( "["  + obtenerNombre
() 
+ "] Verificando 
pago..."
);  


<!-- pagina 4 -->
        
 
        
if  (envio.
getCosto
() 
<=  0) {
 
            
System.err.
println
( " ✗ Costo inválido"
);  
            
return
 false
;  
        
}  
        
 
        
if  (envio.
getMetodoPago
() 
==  null
 ||  
envio.
getMetodoPago
().
isEmpty
()) {
 
            
System.err.
println
( " ✗ Método de pago no especificado"
);  
            
return
 false
;  
        
}  
        
 
        
System.out.
println
( "✓ Pago válido"
);  
        
 
        
if  (siguiente 
!=  null
) {
 
            
return
 siguiente.
validar
(envio);
 
        
}  
        
return
 true ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ValidadorPago"
;  
    
}  
}  
  
// Validador 4: Seguridad
 
public
 class
 ValidadorSeguridad
 extends
 ValidadorEnvio
 {  
    
@Override
 
    
public
 boolean
 validar
(Envio 
envio ) {
 
        
System.out.
println
( "["  + obtenerNombre
() 
+ "] Verificando 
restricciones..."
);  
        
 
        
if  (envio.
getDestino
().
contains
( "Restringido"
)) {
 
            
System.err.
println
( " ✗ Destino restringido"
);  
            
return
 false
;  
        
}  
        
 
        
System.out.
println
( "✓ Seguridad OK"
);  
        
 
        
if  (siguiente 
!=  null
) {
 
            
return
 siguiente.
validar
(envio);
 
        
}  
        
return
 true ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ValidadorSeguridad"
;  
    
}  
}  


<!-- pagina 5 -->
  
// Validador 5: Capacidad
 
public
 class
 ValidadorCapacidad
 extends
 ValidadorEnvio
 {  
    
private
 SistemaCapacidad sistemaCapacidad;
 
    
 
    
public
 ValidadorCapacidad
(SistemaCapacidad 
sistemaCapacidad
) {
 
        
this
.sistemaCapacidad 
= sistemaCapacidad;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 validar
(Envio 
envio ) {
 
        
System.out.
println
( "["  + obtenerNombre
() 
+ "] Verificando 
capacidad..."
);  
        
 
        
if  ( ! sistemaCapacidad.
hayEspacioDisponible
(envio.
getPeso
())) {
 
            
System.err.
println
( " ✗ No hay espacio disponible"
);  
            
return
 false
;  
        
}  
        
 
        
System.out.
println
( "✓ Capacidad disponible"
);  
        
 
        
if  (siguiente 
!=  null
) {
 
            
return
 siguiente.
validar
(envio);
 
        
}  
        
return
 true ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ValidadorCapacidad"
;  
    
}  
}  
 
Paso 3: Construcción de la cadena 
 
public
 class
 CadenaValidadores
 {  
    
private
 ValidadorEnvio primerValidador;
 
    
 
    
public
 CadenaValidadores
(SistemaInventario 
inventario
, 
SistemaCapacidad 
capacidad
) {
 
        
// Crear validadores
 
        
ValidadorEnvio validador1 
= new ValidadorDatos
();  
        
ValidadorEnvio validador2 
= new ValidadorInventario
(inventario);
 
        
ValidadorEnvio validador3 
= new ValidadorPago
();  
        
ValidadorEnvio validador4 
= new ValidadorSeguridad
();
 
        
ValidadorEnvio validador5 
= new ValidadorCapacidad
(capacidad);
 
        
 


<!-- pagina 6 -->
        
// Construir cadena
 
        
validador1.
setSiguiente
(validador2);
 
        
validador2.
setSiguiente
(validador3);
 
        
validador3.
setSiguiente
(validador4);
 
        
validador4.
setSiguiente
(validador5);
 
        
 
        
this
.primerValidador 
= validador1;
 
    
}  
    
 
    
public
 boolean
 validarEnvio
(Envio 
envio ) {
 
        
System.out.
println
( " \ n=== Validando Envío ==="
);  
        
return
 primerValidador.
validar
(envio);
 
    
}  
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Envío válido
 
Envio envio1 
= new Envio ( "Buenos Aires"
, "Córdoba"
, 5.0 , 150.0 , 
"TARJETA"
, "PROD - 001" );  
CadenaValidadores cadena 
= new CadenaValidadores
(inventario, capacidad);
 
if  (cadena.
validarEnvio
(envio1)) {
 
    
System.out.
println
( "✓ Envío aprobado
\ n" );  
}  
  
// Caso 2: Envío con datos inválidos
 
Envio envio2 
= new Envio ( "" , "Córdoba"
, 5.0 , 150.0 , "TARJETA"
, "PROD -
001" );  
if  ( ! cadena.
validarEnvio
(envio2)) {
 
    
System.out.
println
( " ✗ Envío rechazado
\ n" );  
}  
  
// Caso 3: Envío con peso inválido
 
Envio envio3 
= new Envio ( "Buenos Aires"
, "Córdoba"
, - 5.0 , 150.0 , 
"TARJETA"
, "PROD - 001" );  
if  ( ! cadena.
validarEnvio
(envio3)) {
 
    
System.out.
println
( " ✗ Envío rechazado
\ n" );  
}  
  
// Caso 4: Envío con costo inválido
 
Envio envio4 
= new Envio ( "Buenos Aires"
, "Córdoba"
, 5.0 , 0, "TARJETA"
, 
"PROD - 001" );  
if  ( ! cadena.
validarEnvio
(envio4)) {
 
    
System.out.
println
( " ✗ Envío rechazado
\ n" );  
}  
  


<!-- pagina 7 -->
// Caso 5: Envío con destino restringido
 
Envio envio5 
= new Envio ( "Buenos Aires"
, "Zona Restringida"
, 5.0 , 150.0 , 
"TARJETA"
, "PROD - 001" );  
if  ( ! cadena.
validarEnvio
(envio5)) {
 
    
System.out.
println
( " ✗ Envío rechazado
\ n" );  
}  
 
Entregable: 
• 
Clase abstracta ValidadorEnvio 
• 
5 validadores concretos 
• 
Clase CadenaValidadores 
• 
5 casos de prueba 
 
 
 
 


<!-- pagina 8 -->
Actividad 2: Command - Operaciones Reversibles 
Contexto: LogiSmart necesita registrar todas las operaciones para auditoría y permitir 
undo/redo: 
• 
Crear envío 
• 
Cancelar envío 
• 
Actualizar estado 
• 
Cambiar método de pago 
• 
Agregar servicio adicional 
 
Paso 1: Define la interfaz Comando 
 
public
 interface
 Comando  {  
    
void  ejecutar
();  
    
void  deshacer
();  
    
String 
obtenerDescripcion
();  
}  
 
Paso 2: Implementa comandos concretos 
 
// Comando: Crear Envío
 
public
 class
 ComandoCrearEnvio
 implements
 Comando  {  
    
private
 ServicioEnvios servicio;
 
    
private
 Envio envio;
 
    
private
 String numeroSeguimiento;
 
    
 
    
public
 ComandoCrearEnvio
(ServicioEnvios 
servicio
, Envio 
envio ) {
 
        
this
.servicio 
= servicio;
 
        
this
.envio 
= envio;
 
    
}  
    
 
    
@Override
 
    
public
 void  ejecutar
() {
 
        
System.out.
println
( "[Comando] Creando envío..."
);  
        
numeroSeguimiento 
= servicio.
crearEnvio
(envio);
 
        
System.out.
println
( "✓ Envío creado: "
 + numeroSeguimiento);
 
    
}  
    
 
    
@Override
 
    
public
 void  deshacer
() {
 
        
System.out.
println
( "[Comando] Deshaciendo: Cancelando envío..."
);  
        
servicio.
cancelarEnvio
(numeroSeguimiento);
 
        
System.out.
println
( "✓ Envío cancelado"
);  


<!-- pagina 9 -->
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerDescripcion
() {
 
        
return
 "Crear envío de "
 + envio.
getOrigen
() 
+ " a "
 + 
envio.
getDestino
();
 
    
}  
}  
  
// Comando: Cancelar Envío
 
public
 class
 ComandoCancelarEnvio
 implements
 Comando  {  
    
private
 ServicioEnvios servicio;
 
    
private
 String numeroSeguimiento;
 
    
 
    
public
 ComandoCancelarEnvio
(ServicioEnvios 
servicio
, String 
numeroSeguimiento
) {
 
        
this
.servicio 
= servicio;
 
        
this
.numeroSeguimiento 
= numeroSeguimiento;
 
    
}  
    
 
    
@Override
 
    
public
 void  ejecutar
() {
 
        
System.out.
println
( "[Comando] Cancelando envío: "
 + 
numeroSeguimiento);
 
        
servicio.
cancelarEnvio
(numeroSeguimiento);
 
        
System.out.
println
( "✓ Envío cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  deshacer
() {
 
        
System.out.
println
( "[Comando] Deshaciendo: Reactivando 
envío..."
);  
        
servicio.
reactivarEnvio
(numeroSeguimiento);
 
        
System.out.
println
( "✓ Envío reactivado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerDescripcion
() {
 
        
return
 "Cancelar envío: "
 + numeroSeguimiento;
 
    
}  
}  
  
// Comando: Actualizar Estado
 
public
 class
 ComandoActualizarEstado
 implements
 Comando  {  
    
private
 ServicioEnvios servicio;
 
    
private
 String numeroSeguimiento;
 
    
private
 String nuevoEstado;
 
    
private
 String estadoAnterior;
 
    
 
    
public
 ComandoActualizarEstado
(ServicioEnvios 
servicio
, String 
numeroSeguimiento
, String 
nuevoEstado
) {
 


<!-- pagina 10 -->
        
this
.servicio 
= servicio;
 
        
this
.numeroSeguimiento 
= numeroSeguimiento;
 
        
this
.nuevoEstado 
= nuevoEstado;
 
    
}  
    
 
    
@Override
 
    
public
 void  ejecutar
() {
 
        
System.out.
println
( "[Comando] Actualizando estado a: "
 + 
nuevoEstado);
 
        
estadoAnterior 
= servicio.
obtenerEstado
(numeroSeguimiento);
 
        
servicio.
actualizarEstado
(numeroSeguimiento, nuevoEstado);
 
        
System.out.
println
( "✓ Estado actualizado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  deshacer
() {
 
        
System.out.
println
( "[Comando] Deshaciendo: Restaurando estado 
anterior..."
);  
        
servicio.
actualizarEstado
(numeroSeguimiento, estadoAnterior);
 
        
System.out.
println
( "✓ Estado restaurado: "
 + estadoAnterior);
 
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerDescripcion
() {
 
        
return
 "Actualizar estado a: "
 + nuevoEstado;
 
    
}  
}  
  
// Comando: Cambiar Método de Pago
 
public
 class
 ComandoCambiarMetodoPago
 implements
 Comando  {  
    
private
 ServicioEnvios servicio;
 
    
private
 String numeroSeguimiento;
 
    
private
 String nuevoMetodo;
 
    
private
 String metodoAnterior;
 
    
 
    
public
 ComandoCambiarMetodoPago
(ServicioEnvios 
servicio
, String 
numeroSeguimiento
, String 
nuevoMetodo ) {
 
        
this
.servicio 
= servicio;
 
        
this
.numeroSeguimiento 
= numeroSeguimiento;
 
        
this
.nuevoMetodo 
= nuevoMetodo;
 
    
}  
    
 
    
@Override
 
    
public
 void  ejecutar
() {
 
        
System.out.
println
( "[Comando] Cambiando método de pago a: "
 + 
nuevoMetodo);
 
        
metodoAnterior 
= servicio.
obtenerMetodoPago
(numeroSeguimiento);
 
        
servicio.
cambiarMetodoPago
(numeroSeguimiento, nuevoMetodo);
 
        
System.out.
println
( "✓ Método de pago cambiado"
);  
    
}  
    
 


<!-- pagina 11 -->
    
@Override
 
    
public
 void  deshacer
() {
 
        
System.out.
println
( "[Comando] Deshaciendo: Restaurando método 
anterior..."
);  
        
servicio.
cambiarMetodoPago
(numeroSeguimiento, metodoAnterior);
 
        
System.out.
println
( "✓ Método de pago restaurado: "
 + 
metodoAnterior);
 
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerDescripcion
() {
 
        
return
 "Cambiar método de pago a: "
 + nuevoMetodo;
 
    
}  
}  
  
// Comando: Agregar Servicio
 
public
 class
 ComandoAgregarServicio
 implements
 Comando  {  
    
private
 ServicioEnvios servicio;
 
    
private
 String numeroSeguimiento;
 
    
private
 String nombreServicio;
 
    
 
    
public
 ComandoAgregarServicio
(ServicioEnvios 
servicio
, String 
numeroSeguimiento
, String 
nombreServicio
) {
 
        
this
.servicio 
= servicio;
 
        
this
.numeroSeguimiento 
= numeroSeguimiento;
 
        
this
.nombreServicio 
= nombreServicio;
 
    
}  
    
 
    
@Override
 
    
public
 void  ejecutar
() {
 
        
System.out.
println
( "[Comando] Agregando servicio: "
 + 
nombreServicio);
 
        
servicio.
agregarServicio
(numeroSeguimiento, nombreServicio);
 
        
System.out.
println
( "✓ Servicio agregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  deshacer
() {
 
        
System.out.
println
( "[Comando] Deshaciendo: Removiendo 
servicio..."
);  
        
servicio.
removerServicio
(numeroSeguimiento, nombreServicio);
 
        
System.out.
println
( "✓ Servicio removido"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerDescripcion
() {
 
        
return
 "Agregar servicio: "
 + nombreServicio;
 
    
}  
}  
 


<!-- pagina 12 -->
Paso 3: Cola de comandos con historial 
 
public
 class
 ColaComandos
 {  
    
private
 List<
Comando > historial 
= new ArrayList<>();
 
    
private
 int  indiceActual 
= - 1;  
    
 
    
/**  
     
* Ejecuta un comando y lo agrega al historial
 
     
*/  
    
public
 void  ejecutar
(Comando 
comando ) {
 
        
// Eliminar comandos deshacidos
 
        
while  (indiceActual 
< historial.
size
() 
-  1) {
 
            
historial.
remove (historial.
size
() 
-  1);  
        
}  
        
 
        
comando. ejecutar
();  
        
historial.
add (comando);
 
        
indiceActual
++ ;  
    
}  
    
 
    
/**  
     
* Deshace el último comando
 
     
*/  
    
public
 void  deshacer
() {
 
        
if  (indiceActual 
>=  0) {
 
            
Comando comando 
= historial.
get (indiceActual);
 
            
comando. deshacer
();  
            
indiceActual
-- ;  
            
System.out.
println
( "✓ Deshecho: "
 + 
comando. obtenerDescripcion
());
 
        
}  
    
}  
    
 
    
/**  
     
* Rehace el comando deshacido
 
     
*/  
    
public
 void  rehacer
() {
 
        
if  (indiceActual 
< historial.
size
() 
-  1) {
 
            
indiceActual
++ ;  
            
Comando comando 
= historial.
get (indiceActual);
 
            
comando. ejecutar
();  
            
System.out.
println
( "✓ Rehecho: "
 + 
comando. obtenerDescripcion
());
 
        
}  
    
}  
    
 
    
/**  
     
* Muestra el historial
 
     
*/  


<!-- pagina 13 -->
    
public
 void  mostrarHistorial
() {
 
        
System.out.
println
( " \ n=== Historial de Comandos ==="
);  
        
for  ( int  i = 0; i 
< historial.
size
(); i
++ ) {
 
            
String marca 
= i ==  indiceActual 
? "→" :  " " ;  
            
System.out.
println
(marca 
+ " "  + (i 
+ 1) + ". 
"  + 
historial.
get (i).
obtenerDescripcion
());
 
        
}  
    
}  
    
 
    
/**  
     
* Obtiene el número de comandos en el historial
 
     
*/  
    
public
 int  obtenerTamaño
() {
 
        
return
 historial.
size
();  
    
}  
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Crear envío
 
ColaComandos cola 
= new ColaComandos
();
 
ServicioEnvios servicio 
= new ServicioEnvios
();  
Envio envio 
= new Envio ( "Buenos Aires"
, "Córdoba"
, 5.0 , 150.0 , "TARJETA"
, 
"PROD - 001" );  
Comando cmd1 
= new ComandoCrearEnvio
(servicio, envio);
 
cola.
ejecutar
(cmd1);
 
  
// Caso 2: Actualizar estado
 
Comando cmd2 
= new ComandoActualizarEstado
(servicio, 
"ENV - 001" , "EN 
TRÁNSITO" );  
cola.
ejecutar
(cmd2);
 
  
// Caso 3: Cambiar método de pago
 
Comando cmd3 
= new ComandoCambiarMetodoPago
(servicio, 
"ENV - 001" , 
"EFECTIVO"
);  
cola.
ejecutar
(cmd3);
 
  
// Caso 4: Agregar servicio
 
Comando cmd4 
= new ComandoAgregarServicio
(servicio, 
"ENV - 001" , "Seguro"
);  
cola.
ejecutar
(cmd4);
 
  
// Caso 5: Mostrar historial
 
cola.
mostrarHistorial
();  
  
// Caso 6: Deshacer
 
cola.
deshacer
(); 
// Quita Seguro
 
cola.
deshacer
(); 
// Vuelve a TARJETA
 


<!-- pagina 14 -->
cola.
mostrarHistorial
();  
  
// Caso 7: Rehacer
 
cola.
rehacer
(); 
// Vuelve a EFECTIVO
 
cola.
mostrarHistorial
();  
 
Entregable: 
• 
Interfaz Comando 
• 
5 comandos concretos 
• 
Clase ColaComandos 
• 
7 casos de prueba 
 
 
 
 


<!-- pagina 15 -->
Actividad 3: Interpreter - Reglas de Negocio 
Contexto: LogiSmart necesita un lenguaje para definir reglas de envío complejas: 
• 
ORIGEN = "Buenos Aires" AND PESO < 10 
• 
DESTINO = "Córdoba" OR DESTINO = "Mendoza" 
• 
COSTO > 100 AND NOT RESTRINGIDO 
 
Paso 1: Define la interfaz Expresión 
 
public
 interface
 Expresion
 {  
    
boolean
 evaluar
(Envio 
envio );  
}  
 
Paso 2: Expresiones terminales 
 
// Expresión: ORIGEN = valor
 
public
 class
 ExpresionOrigen
 implements
 Expresion
 {  
    
private
 String valor;
 
    
 
    
public
 ExpresionOrigen
(String 
valor
) {
 
        
this
.valor 
= valor;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 envio.
getOrigen
().
equals
(valor);
 
    
}  
}  
  
// Expresión: DESTINO = valor
 
public
 class
 ExpresionDestino
 implements
 Expresion
 {  
    
private
 String valor;
 
    
 
    
public
 ExpresionDestino
(String 
valor
) {
 
        
this
.valor 
= valor;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 envio.
getDestino
().
equals
(valor);
 
    
}  
}  
  
// Expresión: PESO < valor
 


<!-- pagina 16 -->
public
 class
 ExpresionPeso
 implements
 Expresion
 {  
    
private
 double  valor;
 
    
private
 String operador; 
// "<", ">", "="
 
    
 
    
public
 ExpresionPeso
( double  valor
, String 
operador
) {
 
        
this
.valor 
= valor;
 
        
this
.operador 
= operador;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
switch
 (operador) {
 
            
case  "<" :  return
 envio.
getPeso
() 
< valor;
 
            
case  ">" :  return
 envio.
getPeso
() 
> valor;
 
            
case  "=" :  return
 envio.
getPeso
() 
==  valor;
 
            
default:
 return
 false
;  
        
}  
    
}  
}  
  
// Expresión: COSTO > valor
 
public
 class
 ExpresionCosto
 implements
 Expresion
 {  
    
private
 double  valor;
 
    
private
 String operador;
 
    
 
    
public
 ExpresionCosto
( double  valor
, String 
operador
) {
 
        
this
.valor 
= valor;
 
        
this
.operador 
= operador;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
switch
 (operador) {
 
            
case  "<" :  return
 envio.
getCosto
() 
< valor;
 
            
case  ">" :  return
 envio.
getCosto
() 
> valor;
 
            
case  "=" :  return
 envio.
getCosto
() 
==  valor;
 
            
default:
 return
 false
;  
        
}  
    
}  
}  
  
// Expresión: RESTRINGIDO
 
public
 class
 ExpresionRestringido
 implements
 Expresion
 {  
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 envio.
getDestino
().
contains
( "Restringido"
);  
    
}  
}  
 


<!-- pagina 17 -->
Paso 3: Expresiones no-terminales (operadores) 
 
// Expresión: AND
 
public
 class
 ExpresionAND
 implements
 Expresion
 {  
    
private
 Expresion izquierda;
 
    
private
 Expresion derecha;
 
    
 
    
public
 ExpresionAND
(Expresion 
izquierda
, Expresion 
derecha
) {
 
        
this
.izquierda 
= izquierda;
 
        
this
.derecha 
= derecha;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 izquierda.
evaluar
(envio) 
&& derecha.
evaluar
(envio);
 
    
}  
}  
  
// Expresión: OR
 
public
 class
 ExpresionOR
 implements
 Expresion
 {  
    
private
 Expresion izquierda;
 
    
private
 Expresion derecha;
 
    
 
    
public
 ExpresionOR
(Expresion 
izquierda
, Expresion 
derecha
) {
 
        
this
.izquierda 
= izquierda;
 
        
this
.derecha 
= derecha;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 izquierda.
evaluar
(envio) 
||  derecha.
evaluar
(envio);
 
    
}  
}  
  
// Expresión: NOT
 
public
 class
 ExpresionNOT
 implements
 Expresion
 {  
    
private
 Expresion expresion;
 
    
 
    
public
 ExpresionNOT
(Expresion 
expresion
) {
 
        
this
.expresion 
= expresion;
 
    
}  
    
 
    
@Override
 
    
public
 boolean
 evaluar
(Envio 
envio ) {
 
        
return
 ! expresion.
evaluar
(envio);
 
    
}  
}  


<!-- pagina 18 -->
Paso 4: Casos de prueba 
 
// Caso 1: Regla simple
 
Expresion regla1 
= new ExpresionOrigen
( "Buenos Aires"
);  
Envio envio1 
= new Envio ( "Buenos Aires"
, "Córdoba"
, 5.0 , 150.0 , 
"TARJETA"
, "PROD - 001" );  
System.out.
println
( "Envío 1 cumple regla 1: "
 + regla1.
evaluar
(envio1)); 
// true
 
  
// Caso 2: Regla con AND
 
Expresion regla2 
= new ExpresionAND
(  
    
new ExpresionOrigen
( "Buenos Aires"
),  
    
new ExpresionPeso
( 10 , "<" )  
);  
System.out.
println
( "Envío 1 cumple regla 2: "
 + regla2.
evaluar
(envio1)); 
// true
 
  
// Caso 3: Regla con OR
 
Expresion regla3 
= new ExpresionOR
(  
    
new ExpresionDestino
( "Córdoba"
),  
    
new ExpresionDestino
( "Mendoza" )  
);  
System.out.
println
( "Envío 1 cumple regla 3: "
 + regla3.
evaluar
(envio1)); 
// true
 
  
// Caso 4: Regla con NOT
 
Expresion regla4 
= new ExpresionNOT
( new ExpresionRestringido
());
 
System.out.
println
( "Envío 1 cumple regla 4: "
 + regla4.
evaluar
(envio1)); 
// true
 
  
// Caso 5: Regla compleja
 
Expresion regla5 
= new ExpresionAND
(  
    
new ExpresionAND
(  
        
new ExpresionOrigen
( "Buenos Aires"
),  
        
new ExpresionCosto
( 100 , ">" )  
    
),  
    
new ExpresionNOT
( new ExpresionRestringido
())  
);  
System.out.
println
( "Envío 1 cumple regla 5: "
 + regla5.
evaluar
(envio1)); 
// true
 
 
Entregable: 
• 
Interfaz Expresion 
• 
5 expresiones terminales 
• 
3 expresiones no-terminales 
• 
5 casos de prueba 


<!-- pagina 19 -->
 
 
Actividad 4: Diagramas de Secuencia 
Crea diagramas de secuencia para cada patrón: 
 
Chain of Responsibility: 
Cliente → ValidadorDatos → ValidadorInventario → ValidadorPago → 
ValidadorSeguridad 
 
Command: 
Cliente → ColaComandos → Comando → Servicio 
 
Interpreter: 
Cliente → ExpresionAND → ExpresionOrigen + ExpresionPeso → Envio 
 
Entregable: 
• 
3 diagramas de secuencia (ASCII o UML) 
 
 
 


<!-- pagina 20 -->
Actividad 5: Integración Completa  
Integra los 3 patrones en un sistema unificado: 
 
public
 class
 SistemaLogisticaCompleto
 {  
    
private
 CadenaValidadores validadores;
 
    
private
 ColaComandos cola;
 
    
private
 Map<String
, Expresion
> reglas;
 
    
 
    
public
 SistemaLogisticaCompleto
() {
 
        
this
.validadores 
= new CadenaValidadores
(...);
 
        
this
.cola 
= new ColaComandos
();
 
        
this
.reglas 
= new HashMap<>();
 
        
inicializarReglas
();  
    
}  
    
 
    
private
 void  inicializarReglas
() {
 
        
// Regla 1: Envíos a Córdoba con peso < 10
 
        
reglas.
put ( "REGLA_1"
, new ExpresionAND
(  
            
new ExpresionDestino
( "Córdoba"
),  
            
new ExpresionPeso
( 10 , "<" )  
        
));  
        
 
        
// Regla 2: Envíos costosos
 
        
reglas.
put ( "REGLA_2"
, new ExpresionCosto
( 100 , ">" ));
 
    
}  
    
 
    
public
 void  procesarEnvio
(Envio 
envio ) {
 
        
// 1. Validar con cadena
 
        
if  ( ! validadores.
validarEnvio
(envio)) {
 
            
System.out.
println
( "Envío rechazado en validación"
);  
            
return
;  
        
}  
        
 
        
// 2. Crear comando
 
        
Comando cmd 
= new ComandoCrearEnvio
(servicio, envio);
 
        
cola.
ejecutar
(cmd);
 
        
 
        
// 3. Evaluar reglas
 
        
for  (String nombreRegla 
:  reglas.
keySet
()) {
 
            
if  (reglas.
get (nombreRegla).
evaluar
(envio)) {
 
                
System.out.
println
( "✓ Cumple "
 + nombreRegla);
 
            
}  
        
}  
    
}  
}  
 


<!-- pagina 21 -->
Entregables 
Documento Markdown 
Incluir: 
• 
☐Descripción de cada patrón 
• 
☐Implementación paso a paso 
• 
☐Casos de prueba 
• 
☐Diagramas de secuencia 
• 
☐Decisiones de diseño 
• 
☐Análisis de ventajas/desventajas 
 
Código Java 
Incluir: 
• 
☐Chain: 1 clase abstracta + 5 validadores + 1 cadena = 7 clases 
• 
☐Command: 1 interfaz + 5 comandos + 1 cola = 7 clases 
• 
☐Interpreter: 1 interfaz + 5 terminales + 3 no-terminales = 9 clases 
• 
☐Integración: 1 clase = 1 clase 
 
Total: 24 clases Java 
 
Diagramas UML 
• 
☐Estructura de Chain of Responsibility 
• 
☐Estructura de Command 
• 
☐Estructura de Interpreter 
• 
☐Diagramas de secuencia (3) 
 
 
 


<!-- pagina 22 -->
Criterios de Evaluación 
Criterio 
Excelente 
Bueno 
Aceptable 
Insuficiente 
Chain 
5 validadores 
funcionales 
4 validadores 
3 validadores 
2 o menos 
Command 
5 comandos + 
undo/redo 
4 comandos + 
undo/redo 
3 comandos 
2 o menos 
Interpreter 
5 terminales + 3 no-
terminales 
4 terminales + 2 no-
terminales 
3 terminales + 1 no-
terminal 
Menos 
Diagramas 
3 diagramas claros 
3 diagramas básicos 
2 diagramas 
1 o menos 
Integración 
3 patrones integrados 
2 patrones integrados 
1 patrón 
Sin 
integración 
Código 
Limpio, documentado 
Limpio, documentado 
Funcional 
Con errores 
Documentación 
Completa, decisiones 
justificadas 
Buena, decisiones 
explicadas 
Básica 
Incompleta 
Casos de 
Prueba 
20+ casos, todos 
funcionan 
15+ casos, funcionan 
bien 
10+ casos 
Menos de 10 
 
