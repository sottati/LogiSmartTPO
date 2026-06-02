<!-- pagina 1 -->
Hito 11: Patrones de Comportamiento II 
Iterator, Mediator, Memento, Observer 
Proyecto: LogiSmart - Sistema de Gestión de Logística 
 
Formato: Documento Markdown + Código Java 
 
Contexto del Hito 
Después de implementar patrones de comportamiento I (Hito 10), ahora aplicarás patrones 
que definen cómo los objetos se comunican y cambian: 
 
1 Iterator: Acceder a colecciones sin exponer estructura 
2 Mediator: Centralizar comunicación entre componentes 
3 Memento: Capturar y restaurar estados 
4 Observer: Notificar cambios automáticamente 
 
Objetivos del Hito 
Al completar este hito, habrás: 
 
• 
  Implementado Iterator para múltiples colecciones 
• 
  Implementado Mediator para comunicación centralizada 
• 
  Implementado Memento para historial de estados 
• 
  Implementado Observer para notificaciones 
• 
  Creado arquitectura event-driven 
• 
  Integrado los 4 patrones en un sistema unificado 
• 
  Documentado decisiones de diseño 
• 
  Creado 25+ casos de prueba 
 
 
 


<!-- pagina 2 -->
Actividades Prácticas 
Actividad 1: Iterator - Múltiples Colecciones (20 minutos) 
Contexto: LogiSmart almacena envíos en diferentes estructuras: 
1 Array de envíos 
2 Lista enlazada de envíos 
3 Árbol de envíos por región 
4 Tabla hash de envíos por ID 
 
Paso 1: Define la interfaz Iterator 
 
public
 interface
 IteradorEnvios
 {  
    
boolean
 tieneSiguiente
();  
    
Envio 
obtenerSiguiente
();  
    
void  reiniciar
();  
}  
 
Paso 2: Define la interfaz Colección 
 
public
 interface
 ColeccionEnvios
 {  
    
IteradorEnvios 
crearIterador
();  
    
void  agregar
(Envio 
envio );  
    
void  remover (Envio 
envio );  
    
int  obtenerTamaño
();  
}  
 
Paso 3: Implementa colecciones concretas 
 
// Colección: Array
 
public
 class
 ColeccionArray
 implements
 ColeccionEnvios
 {  
    
private
 Envio [] envios 
= new Envio [ 1000 ];  
    
private
 int  tamaño = 0;  
    
 
    
@Override
 
    
public
 IteradorEnvios 
crearIterador
() {
 
        
return
 new IteradorArray
();  
    
}  
    
 


<!-- pagina 3 -->
    
@Override
 
    
public
 void  agregar
(Envio 
envio ) {
 
        
if  (tamaño 
< envios.length) {
 
            
envios[tamaño
++ ] = envio;
 
        
}  
    
}  
    
 
    
@Override
 
    
public
 void  remover (Envio 
envio ) {
 
        
for  ( int  i = 0; i 
< tamaño; i
++ ) {
 
            
if  (envios[i].
getId
().
equals
(envio.
getId
())) {
 
                
System.
arraycopy
(envios, i 
+ 1, envios, i, tamaño 
-  i -  
1);  
                
tamaño -- ;  
                
break ;  
            
}  
        
}  
    
}  
    
 
    
@Override
 
    
public
 int  obtenerTamaño
() {
 
        
return
 tamaño;  
    
}  
    
 
    
private
 class
 IteradorArray
 implements
 IteradorEnvios
 {  
        
private
 int  indice 
= 0;  
        
 
        
@Override
 
        
public
 boolean
 tieneSiguiente
() {
 
            
return
 indice 
< tamaño;  
        
}  
        
 
        
@Override
 
        
public
 Envio 
obtenerSiguiente
() {
 
            
return
 envios[indice
++ ];  
        
}  
        
 
        
@Override
 
        
public
 void  reiniciar
() {
 
            
indice 
= 0;  
        
}  
    
}  
}  
  
// Colección: Lista Enlazada
 
public
 class
 ColeccionLista
 implements
 ColeccionEnvios
 {  
    
private
 Nodo cabeza;
 
    
private
 int  tamaño = 0;  
    
 
    
@Override
 
    
public
 IteradorEnvios 
crearIterador
() {
 


<!-- pagina 4 -->
        
return
 new IteradorLista
();  
    
}  
    
 
    
@Override
 
    
public
 void  agregar
(Envio 
envio ) {
 
        
Nodo nuevoNodo 
= new Nodo(envio);
 
        
if  (cabeza 
==  null
) {
 
            
cabeza 
= nuevoNodo;  
        
} else  {  
            
Nodo actual 
= cabeza;
 
            
while  (actual.siguiente 
!=  null
) {
 
                
actual 
= actual.siguiente;
 
            
}  
            
actual.siguiente 
= nuevoNodo;  
        
}  
        
tamaño ++ ;  
    
}  
    
 
    
@Override
 
    
public
 void  remover (Envio 
envio ) {
 
        
if  (cabeza 
!=  null
 && cabeza.envio.
getId
().
equals
(envio.
getId
())) 
{  
            
cabeza 
= cabeza.siguiente;
 
            
tamaño -- ;  
            
return
;  
        
}  
        
 
        
Nodo actual 
= cabeza;
 
        
while  (actual 
!=  null
 && actual.siguiente 
!=  null
) {
 
            
if  (actual.siguiente.envio.
getId
().
equals
(envio.
getId
())) {
 
                
actual.siguiente 
= actual.siguiente.siguiente;
 
                
tamaño -- ;  
                
return
;  
            
}  
            
actual 
= actual.siguiente;
 
        
}  
    
}  
    
 
    
@Override
 
    
public
 int  obtenerTamaño
() {
 
        
return
 tamaño;  
    
}  
    
 
    
private
 class
 IteradorLista
 implements
 IteradorEnvios
 {  
        
private
 Nodo actual 
= cabeza;
 
        
 
        
@Override
 
        
public
 boolean
 tieneSiguiente
() {
 
            
return
 actual 
!=  null
;  
        
}  
        
 


<!-- pagina 5 -->
        
@Override
 
        
public
 Envio 
obtenerSiguiente
() {
 
            
Envio envio 
= actual.envio;
 
            
actual 
= actual.siguiente;
 
            
return
 envio;
 
        
}  
        
 
        
@Override
 
        
public
 void  reiniciar
() {
 
            
actual 
= cabeza;
 
        
}  
    
}  
    
 
    
private
 class
 Nodo {  
        
Envio envio;
 
        
Nodo siguiente;
 
        
 
        
Nodo(Envio 
envio ) {
 
            
this
.envio 
= envio;
 
        
}  
    
}  
}  
  
// Colección: Tabla Hash
 
public
 class
 ColeccionHash
 implements
 ColeccionEnvios
 {  
    
private
 Map<String
, Envio > envios 
= new HashMap<>();
 
    
 
    
@Override
 
    
public
 IteradorEnvios 
crearIterador
() {
 
        
return
 new IteradorHash
();  
    
}  
    
 
    
@Override
 
    
public
 void  agregar
(Envio 
envio ) {
 
        
envios.
put (envio.
getId
(), envio);
 
    
}  
    
 
    
@Override
 
    
public
 void  remover (Envio 
envio ) {
 
        
envios.
remove (envio.
getId
());
 
    
}  
    
 
    
@Override
 
    
public
 int  obtenerTamaño
() {
 
        
return
 envios.
size
();  
    
}  
    
 
    
private
 class
 IteradorHash
 implements
 IteradorEnvios
 {  
        
private
 Iterator<
Envio > iterador 
= envios.
values
().
iterator
();  
        
 
        
@Override
 


<!-- pagina 6 -->
        
public
 boolean
 tieneSiguiente
() {
 
            
return
 iterador.
hasNext
();
 
        
}  
        
 
        
@Override
 
        
public
 Envio 
obtenerSiguiente
() {
 
            
return
 iterador.
next ();  
        
}  
        
 
        
@Override
 
        
public
 void  reiniciar
() {
 
            
iterador 
= envios.
values
().
iterator
();  
        
}  
    
}  
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Iterar sobre Array
 
ColeccionEnvios coleccionArray 
= new ColeccionArray
();  
coleccionArray.
agregar
( new Envio ( "ENV - 001" , "Buenos Aires"
, "Córdoba"
, 
5.0 , 150.0 ));  
coleccionArray.
agregar
( new Envio ( "ENV - 002" , "Rosario"
, "Mendoza" , 8.0 , 
200.0 ));  
  
IteradorEnvios iterador 
= coleccionArray.
crearIterador
();  
while  (iterador.
tieneSiguiente
()) {
 
    
Envio envio 
= iterador.
obtenerSiguiente
();  
    
System.out.
println
( "Array: "
 + envio.
getId
());
 
}  
  
// Caso 2: Iterar sobre Lista
 
ColeccionEnvios coleccionLista 
= new ColeccionLista
();  
coleccionLista.
agregar
( new Envio ( "ENV - 003" , "Córdoba"
, "Salta"
, 3.0 , 
100.0 ));  
coleccionLista.
agregar
( new Envio ( "ENV - 004" , "Mendoza" , "La Plata"
, 6.0 , 
180.0 ));  
  
iterador 
= coleccionLista.
crearIterador
();  
while  (iterador.
tieneSiguiente
()) {
 
    
Envio envio 
= iterador.
obtenerSiguiente
();  
    
System.out.
println
( "Lista: "
 + envio.
getId
());
 
}  
  
// Caso 3: Iterar sobre Hash
 
ColeccionEnvios coleccionHash 
= new ColeccionHash
();  


<!-- pagina 7 -->
coleccionHash.
agregar
( new Envio ( "ENV - 005" , "La Plata"
, "Junín"
, 7.0 , 
160.0 ));  
coleccionHash.
agregar
( new Envio ( "ENV - 006" , "Junín"
, "Bahía Blanca"
, 4.0 , 
120.0 ));  
  
iterador 
= coleccionHash.
crearIterador
();  
while  (iterador.
tieneSiguiente
()) {
 
    
Envio envio 
= iterador.
obtenerSiguiente
();  
    
System.out.
println
( "Hash: "
 + envio.
getId
());
 
}  
  
// Caso 4: Reiniciar iterador
 
iterador.
reiniciar
();  
System.out.
println
( "Reiterando..."
);  
while  (iterador.
tieneSiguiente
()) {
 
    
Envio envio 
= iterador.
obtenerSiguiente
();  
    
System.out.
println
( "Reiterado: "
 + envio.
getId
());
 
}  
  
// Caso 5: Remover y reiterar
 
coleccionArray.
remover ( new Envio ( "ENV - 001" , "" , "" , 0, 0));  
iterador 
= coleccionArray.
crearIterador
();  
System.out.
println
( "Después de remover:"
);  
while  (iterador.
tieneSiguiente
()) {
 
    
Envio envio 
= iterador.
obtenerSiguiente
();  
    
System.out.
println
( "Array: "
 + envio.
getId
());
 
}  
 
Entregable: 
• 
Interfaz IteradorEnvios 
• 
Interfaz ColeccionEnvios 
• 
3 colecciones concretas 
• 
5 casos de prueba 
 
 
 


<!-- pagina 8 -->
Actividad 2: Mediator - Comunicación Centralizada 
Contexto: LogiSmart tiene múltiples componentes que necesitan comunicarse: 
5 Centro de distribución 
6 Validador de envíos 
7 Sistema de pago 
8 Sistema de notificación 
9 Sistema de auditoría 
 
Paso 1: Define la interfaz Mediator 
 
public
 interface
 MediadorEnvios
 {  
    
void  registrarCentro
(CentroDistribucion 
centro
);  
    
void  registrarValidador
(ValidadorEnvio 
validador
);  
    
void  registrarPago
(SistemaPago 
pago );  
    
void  registrarNotificador
(SistemaNotificacion 
notificador
);  
    
void  registrarAuditoria
(SistemaAuditoria 
auditoria
);  
    
 
    
void  notificar
(String 
evento , Object 
datos );  
}  
 
Paso 2: Mediator concreto 
 
public
 class
 MediadorEnviosConcreto
 implements
 MediadorEnvios
 {  
    
private
 CentroDistribucion centro;
 
    
private
 ValidadorEnvio validador;
 
    
private
 SistemaPago pago;
 
    
private
 SistemaNotificacion notificador;
 
    
private
 SistemaAuditoria auditoria;
 
    
 
    
@Override
 
    
public
 void  registrarCentro
(CentroDistribucion 
centro
) {
 
        
this
.centro 
= centro;
 
    
}  
    
 
    
@Override
 
    
public
 void  registrarValidador
(ValidadorEnvio 
validador
) {
 
        
this
.validador 
= validador;
 
    
}  
    
 
    
@Override
 
    
public
 void  registrarPago
(SistemaPago 
pago ) {
 
        
this
.pago 
= pago;  


<!-- pagina 9 -->
    
}  
    
 
    
@Override
 
    
public
 void  registrarNotificador
(SistemaNotificacion 
notificador
) {
 
        
this
.notificador 
= notificador;
 
    
}  
    
 
    
@Override
 
    
public
 void  registrarAuditoria
(SistemaAuditoria 
auditoria
) {
 
        
this
.auditoria 
= auditoria;
 
    
}  
    
 
    
@Override
 
    
public
 void  notificar
(String 
evento , Object 
datos ) {
 
        
switch
 (evento) {
 
            
case  "ENVIO_CREADO"
:  
                
Envio envio 
= (Envio) datos;
 
                
System.out.
println
( "[Mediator] Envío creado: "
 + 
envio.
getId
());
 
                
auditoria.
registrar
( "ENVIO_CREADO"
, envio);
 
                
validador.
validar
(envio);
 
                
break ;  
                
 
            
case  "VALIDACION_OK"
:  
                
System.out.
println
( "[Mediator] Validación OK"
);  
                
auditoria.
registrar
( "VALIDACION_OK"
, datos);
 
                
pago. procesarPago
((Envio) datos);
 
                
break ;  
                
 
            
case  "PAGO_CONFIRMADO" :  
                
System.out.
println
( "[Mediator] Pago confirmado"
);  
                
auditoria.
registrar
( "PAGO_CONFIRMADO" , datos);
 
                
notificador.
enviarConfirmacion
((Envio) datos);
 
                
break ;  
                
 
            
case  "NOTIFICACION_ENVIADA"
:  
                
System.out.
println
( "[Mediator] Notificación enviada"
);  
                
auditoria.
registrar
( "NOTIFICACION_ENVIADA"
, datos);
 
                
centro.
registrarEnvio
((Envio) datos);
 
                
break ;  
                
 
            
case  "ENVIO_REGISTRADO"
:  
                
System.out.
println
( "[Mediator] Envío registrado en 
sistema"
);  
                
auditoria.
registrar
( "ENVIO_REGISTRADO"
, datos);
 
                
break ;  
        
}  
    
}  
}  


<!-- pagina 10 -->
 
Paso 3: Componentes que usan Mediator 
 
public
 class
 CentroDistribucion
 {  
    
private
 MediadorEnvios mediador;
 
    
 
    
public
 CentroDistribucion
(MediadorEnvios 
mediador ) {
 
        
this
.mediador 
= mediador;
 
    
}  
    
 
    
public
 void  crearEnvio
(Envio 
envio ) {
 
        
System.out.
println
( "[Centro] Creando envío..."
);  
        
mediador.
notificar
( "ENVIO_CREADO"
, envio);
 
    
}  
    
 
    
public
 void  registrarEnvio
(Envio 
envio ) {
 
        
System.out.
println
( "[Centro] Registrando envío en sistema"
);  
        
mediador.
notificar
( "ENVIO_REGISTRADO"
, envio);
 
    
}  
}  
  
public
 class
 ValidadorEnvio
 {  
    
private
 MediadorEnvios mediador;
 
    
 
    
public
 ValidadorEnvio
(MediadorEnvios 
mediador ) {
 
        
this
.mediador 
= mediador;
 
    
}  
    
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( "[Validador] Validando envío..."
);  
        
if  (envio.
getCosto
() 
> 0 && envio.
getPeso
() 
> 0) {
 
            
System.out.
println
( "[Validador] ✓ Válido"
);  
            
mediador.
notificar
( "VALIDACION_OK"
, envio);
 
        
}  
    
}  
}  
  
public
 class
 SistemaPago
 {  
    
private
 MediadorEnvios mediador;
 
    
 
    
public
 SistemaPago
(MediadorEnvios 
mediador ) {
 
        
this
.mediador 
= mediador;
 
    
}  
    
 
    
public
 void  procesarPago
(Envio 
envio ) {
 
        
System.out.
println
( "[Pago] Procesando pago de $"
 + 
envio.
getCosto
());
 
        
System.out.
println
( "[Pago] ✓ Pago confirmado"
);  
        
mediador.
notificar
( "PAGO_CONFIRMADO" , envio);
 


<!-- pagina 11 -->
    
}  
}  
  
public
 class
 SistemaNotificacion
 {  
    
private
 MediadorEnvios mediador;
 
    
 
    
public
 SistemaNotificacion
(MediadorEnvios 
mediador ) {
 
        
this
.mediador 
= mediador;
 
    
}  
    
 
    
public
 void  enviarConfirmacion
(Envio 
envio ) {
 
        
System.out.
println
( "[Notificador] Enviando confirmación a 
cliente..."
);  
        
System.out.
println
( "[Notificador] ✓ Confirmación enviada"
);  
        
mediador.
notificar
( "NOTIFICACION_ENVIADA"
, envio);
 
    
}  
}  
  
public
 class
 SistemaAuditoria
 {  
    
private
 List<
String
> logs 
= new ArrayList<>();
 
    
 
    
public
 void  registrar
(String 
evento , Object 
datos ) {
 
        
String log 
= "[Auditoría] "
 + evento 
+ " -  "  + datos;
 
        
logs.
add (log);
 
        
System.out.
println
(log);
 
    
}  
    
 
    
public
 void  mostrarLogs
() {
 
        
System.out.
println
( " \ n=== Logs de Auditoría ==="
);  
        
for  (String log 
:  logs) {
 
            
System.out.
println
(log);
 
        
}  
    
}  
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Flujo completo
 
MediadorEnvios mediador 
= new MediadorEnviosConcreto
();  
CentroDistribucion centro 
= new CentroDistribucion
(mediador);
 
ValidadorEnvio validador 
= new ValidadorEnvio
(mediador);
 
SistemaPago pago 
= new SistemaPago
(mediador);
 
SistemaNotificacion notificador 
= new SistemaNotificacion
(mediador);
 
SistemaAuditoria auditoria 
= new SistemaAuditoria
();  
  
mediador.
registrarCentro
(centro);
 
mediador.
registrarValidador
(validador);
 


<!-- pagina 12 -->
mediador.
registrarPago
(pago);
 
mediador.
registrarNotificador
(notificador);
 
mediador.
registrarAuditoria
(auditoria);
 
  
Envio envio1 
= new Envio ( "ENV - 001" , "Buenos Aires"
, "Córdoba"
, 5.0 , 
150.0 );  
centro.
crearEnvio
(envio1);
 
  
auditoria.
mostrarLogs
();  
  
// Caso 2: Múltiples envíos
 
Envio envio2 
= new Envio ( "ENV - 002" , "Rosario"
, "Mendoza" , 8.0 , 200.0 );  
centro.
crearEnvio
(envio2);
 
  
// Caso 3: Envío con datos inválidos
 
Envio envio3 
= new Envio ( "ENV - 003" , "La Plata"
, "Salta"
, 0, 0);  
centro.
crearEnvio
(envio3);
 
  
// Caso 4: Componentes desacoplados
 
// Validador no conoce a Pago, Pago no conoce a Notificador, etc.
 
System.out.
println
( " \ n✓ Componentes completamente desacoplados"
);  
  
// Caso 5: Agregar nuevo componente
 
// Solo necesitas agregar un nuevo mediador y registrar el componente
 
System.out.
println
( "✓ Fácil agregar nuevos componentes"
);  
 
Entregable: 
• 
Interfaz MediadorEnvios 
• 
Clase MediadorEnviosConcreto 
• 
5 componentes concretos 
• 
5 casos de prueba 
 
 
 


<!-- pagina 13 -->
Actividad 3: Memento - Historial de Estados 
Contexto: LogiSmart necesita guardar el historial de estados de un envío: 
10 CONFIRMADO (inicial) 
11 EN_TRANSITO 
12 EN_REPARTO 
13 ENTREGADO 
 
Paso 1: Memento 
 
public
 class
 MementoEnvio
 {  
    
private
 String estado;
 
    
private
 String origen;
 
    
private
 String destino;
 
    
private
 double  peso;  
    
private
 double  costo;
 
    
private
 long  timestamp;
 
    
 
    
public
 MementoEnvio
(String 
estado , String 
origen
, String 
destino
,  
                        
double  peso , double  costo ) {
 
        
this
.estado 
= estado;
 
        
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
.costo 
= costo;
 
        
this
.timestamp 
= System.
currentTimeMillis
();  
    
}  
    
 
    
public
 String 
obtenerEstado
() { 
return
 estado; }
 
    
public
 String 
obtenerOrigen
() { 
return
 origen; }
 
    
public
 String 
obtenerDestino
() { 
return
 destino; }
 
    
public
 double  obtenerPeso
() { 
return
 peso; }
 
    
public
 double  obtenerCosto
() { 
return
 costo; }
 
    
public
 long  obtenerTimestamp
() { 
return
 timestamp; }
 
}  
 
Paso 2: Originador 
 
public
 class
 Envio  {  
    
private
 String id;
 
    
private
 String estado;
 
    
private
 String origen;
 
    
private
 String destino;
 
    
private
 double  peso;  


<!-- pagina 14 -->
    
private
 double  costo;
 
    
 
    
public
 Envio (String 
id , String 
origen
, String 
destino
, double  peso , 
double  costo ) {
 
        
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
.costo 
= costo;
 
        
this
.estado 
= "CONFIRMADO" ;  
    
}  
    
 
    
public
 MementoEnvio 
crearMemento
() {
 
        
return
 new MementoEnvio
(estado, origen, destino, peso, costo);
 
    
}  
    
 
    
public
 void  restaurarDesdeMemento
(MementoEnvio 
memento) {
 
        
this
.estado 
= memento. obtenerEstado
();  
        
this
.origen 
= memento. obtenerOrigen
();  
        
this
.destino 
= memento. obtenerDestino
();  
        
this
.peso 
= memento. obtenerPeso
();  
        
this
.costo 
= memento. obtenerCosto
();  
    
}  
    
 
    
public
 void  cambiarEstado
(String 
nuevoEstado
) {
 
        
this
.estado 
= nuevoEstado;
 
    
}  
    
 
    
public
 String 
obtenerEstado
() { 
return
 estado; }
 
    
public
 String 
getId
() { 
return
 id; }
 
}  
 
Paso 3: Cuidador 
 
public
 class
 HistorialEnvios
 {  
    
private
 List<
MementoEnvio
> historial 
= new ArrayList<>();
 
    
private
 int  indiceActual 
= - 1;  
    
 
    
public
 void  guardarEstado
(Envio 
envio ) {
 
        
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
        
 
        
historial.
add (envio.
crearMemento
());
 
        
indiceActual
++ ;  
        
System.out.
println
( "✓ Estado guardado: "
 + 
envio.
obtenerEstado
());
 


<!-- pagina 15 -->
    
}  
    
 
    
public
 void  irAlEstadoAnterior
(Envio 
envio ) {
 
        
if  (indiceActual 
> 0) {
 
            
indiceActual
-- ;  
            
envio.
restaurarDesdeMemento
(historial.
get (indiceActual));
 
            
System.out.
println
( "✓ Restaurado a: "
 + 
envio.
obtenerEstado
());
 
        
}  
    
}  
    
 
    
public
 void  irAlEstadoSiguiente
(Envio 
envio ) {
 
        
if  (indiceActual 
< historial.
size
() 
-  1) {
 
            
indiceActual
++ ;  
            
envio.
restaurarDesdeMemento
(historial.
get (indiceActual));
 
            
System.out.
println
( "✓ Restaurado a: "
 + 
envio.
obtenerEstado
());
 
        
}  
    
}  
    
 
    
public
 void  mostrarHistorial
() {
 
        
System.out.
println
( " \ n=== Historial de Estados ==="
);  
        
for  ( int  i = 0; i 
< historial.
size
(); i
++ ) {
 
            
MementoEnvio m 
= historial.
get (i);
 
            
String marca 
= i ==  indiceActual 
? "→" :  " " ;  
            
System.out.
println
(marca 
+ " "  + (i 
+ 1) + ". 
"  + 
m.obtenerEstado
());
 
        
}  
    
}  
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Guardar estados
 
Envio envio 
= new Envio ( "ENV - 001" , "Buenos Aires"
, "Córdoba"
, 5.0 , 
150.0 );  
HistorialEnvios historial 
= new HistorialEnvios
();  
  
historial.
guardarEstado
(envio);
 
envio.
cambiarEstado
( "EN_TRANSITO"
);  
historial.
guardarEstado
(envio);
 
envio.
cambiarEstado
( "EN_REPARTO"
);  
historial.
guardarEstado
(envio);
 
envio.
cambiarEstado
( "ENTREGADO" );  
historial.
guardarEstado
(envio);
 
  
historial.
mostrarHistorial
();  


<!-- pagina 16 -->
  
// Caso 2: Ir al estado anterior
 
historial.
irAlEstadoAnterior
(envio);
 
historial.
mostrarHistorial
();  
  
// Caso 3: Ir al estado anterior nuevamente
 
historial.
irAlEstadoAnterior
(envio);
 
historial.
mostrarHistorial
();  
  
// Caso 4: Ir al estado siguiente
 
historial.
irAlEstadoSiguiente
(envio);
 
historial.
mostrarHistorial
();  
  
// Caso 5: Múltiples cambios y restauraciones
 
envio.
cambiarEstado
( "CANCELADO" );  
historial.
guardarEstado
(envio);
 
historial.
mostrarHistorial
();  
 
Entregable: 
• 
Clase MementoEnvio 
• 
Clase Envio (modificada) 
• 
Clase HistorialEnvios 
• 
5 casos de prueba 
 
 
 


<!-- pagina 17 -->
Actividad 4: Observer - Notificaciones Automáticas 
Contexto: LogiSmart necesita notificar a múltiples observadores cuando cambia el estado de 
un envío: 
14 Centro de distribución 
15 Sistema de notificación 
16 Sistema de auditoría 
17 Dashboard en tiempo real 
 
Paso 1: Interfaz Observador 
 
public
 interface
 ObservadorEnvio
 {  
    
void  actualizar
(Envio 
envio );  
}  
 
Paso 2: Sujeto (Observable) 
 
public
 class
 Envio  {  
    
private
 String id;
 
    
private
 String estado;
 
    
private
 List<
ObservadorEnvio
> observadores 
= new ArrayList<>();
 
    
 
    
public
 Envio (String 
id , String 
estado ) {
 
        
this
.id 
= id;  
        
this
.estado 
= estado;
 
    
}  
    
 
    
public
 void  adjuntarObservador
(ObservadorEnvio 
observador
) {
 
        
observadores.
add (observador);
 
        
System.out.
println
( "✓ Observador adjuntado"
);  
    
}  
    
 
    
public
 void  desadjuntarObservador
(ObservadorEnvio 
observador
) {
 
        
observadores.
remove (observador);
 
        
System.out.
println
( "✓ Observador desadjuntado"
);  
    
}  
    
 
    
private
 void  notificarObservadores
() {
 
        
for  (ObservadorEnvio observador 
:  observadores) {
 
            
observador.
actualizar
( this
);  
        
}  
    
}  
    
 


<!-- pagina 18 -->
    
public
 void  cambiarEstado
(String 
nuevoEstado
) {
 
        
this
.estado 
= nuevoEstado;
 
        
System.out.
println
( "[Envio] Estado cambiado a: "
 + nuevoEstado);
 
        
notificarObservadores
();  
    
}  
    
 
    
public
 String 
obtenerEstado
() { 
return
 estado; }
 
    
public
 String 
getId
() { 
return
 id; }
 
}  
 
Paso 3: Observadores concretos 
 
public
 class
 CentroDistribucionObservador
 implements
 ObservadorEnvio
 {  
    
@Override
 
    
public
 void  actualizar
(Envio 
envio ) {
 
        
System.out.
println
( "[Centro] Envío "
 + envio.
getId
() 
+  
                          
" cambió a: "
 + envio.
obtenerEstado
());
 
    
}  
}  
  
public
 class
 SistemaNotificacionObservador
 implements
 ObservadorEnvio
 {  
    
@Override
 
    
public
 void  actualizar
(Envio 
envio ) {
 
        
System.out.
println
( "[Notificador] Enviando email sobre cambio de 
estado..."
);  
    
}  
}  
  
public
 class
 SistemaAuditoriaObservador
 implements
 ObservadorEnvio
 {  
    
@Override
 
    
public
 void  actualizar
(Envio 
envio ) {
 
        
System.out.
println
( "[Auditoría] Registrando cambio: "
 + 
envio.
getId
() 
+  
                          
" → "  + envio.
obtenerEstado
());
 
    
}  
}  
  
public
 class
 DashboardObservador
 implements
 ObservadorEnvio
 {  
    
@Override
 
    
public
 void  actualizar
(Envio 
envio ) {
 
        
System.out.
println
( "[Dashboard] Actualizando interfaz en tiempo 
real..."
);  
    
}  
}  
 


<!-- pagina 19 -->
Paso 4: Casos de prueba 
 
// Caso 1: Crear envío y adjuntar observadores
 
Envio envio 
= new Envio ( "ENV - 001" , "CONFIRMADO" );  
  
ObservadorEnvio centro 
= new CentroDistribucionObservador
();
 
ObservadorEnvio notificador 
= new SistemaNotificacionObservador
();  
ObservadorEnvio auditoria 
= new SistemaAuditoriaObservador
();
 
ObservadorEnvio dashboard 
= new DashboardObservador
();  
  
envio.
adjuntarObservador
(centro);
 
envio.
adjuntarObservador
(notificador);
 
envio.
adjuntarObservador
(auditoria);
 
envio.
adjuntarObservador
(dashboard);
 
  
// Caso 2: Cambiar estado (notifica a todos)
 
envio.
cambiarEstado
( "EN_TRANSITO"
);  
  
// Caso 3: Cambiar estado nuevamente
 
System.out.
println
();  
envio.
cambiarEstado
( "ENTREGADO" );  
  
// Caso 4: Desadjuntar observador
 
System.out.
println
();  
envio.
desadjuntarObservador
(dashboard);
 
envio.
cambiarEstado
( "CONFIRMADO_RECIBIDO"
);  
  
// Caso 5: Múltiples envíos con observadores
 
System.out.
println
();  
Envio envio2 
= new Envio ( "ENV - 002" , "CONFIRMADO" );  
envio2.
adjuntarObservador
(centro);
 
envio2.
adjuntarObservador
(auditoria);
 
envio2.
cambiarEstado
( "EN_TRANSITO"
);  
 
Entregable: 
• 
Interfaz ObservadorEnvio 
• 
Clase Envio (con observadores) 
• 
4 observadores concretos 
• 
5 casos de prueba 
 
 
 
 


<!-- pagina 20 -->
Actividad 5: Integración Completa - Event-Driven 
Integra los 4 patrones en un sistema event-driven: 
 
public
 class
 SistemaLogisticaEventDriven
 {  
    
private
 MediadorEnvios mediador;
 
    
private
 ColeccionEnvios coleccion;
 
    
private
 HistorialEnvios historial;
 
    
 
    
public
 SistemaLogisticaEventDriven
() {
 
        
this
.mediador 
= new MediadorEnviosConcreto
();  
        
this
.coleccion 
= new ColeccionArray
();  
        
this
.historial 
= new HistorialEnvios
();  
        
 
        
// Registrar componentes
 
        
CentroDistribucion centro 
= new CentroDistribucion
(mediador);
 
        
ValidadorEnvio validador 
= new ValidadorEnvio
(mediador);
 
        
SistemaPago pago 
= new SistemaPago
(mediador);
 
        
SistemaNotificacion notificador 
= new 
SistemaNotificacion
(mediador);
 
        
SistemaAuditoria auditoria 
= new SistemaAuditoria
();
 
        
 
        
mediador.
registrarCentro
(centro);
 
        
mediador.
registrarValidador
(validador);
 
        
mediador.
registrarPago
(pago);
 
        
mediador.
registrarNotificador
(notificador);
 
        
mediador.
registrarAuditoria
(auditoria);
 
    
}  
    
 
    
public
 void  procesarEnvios
(List<
Envio > envios
) {
 
        
// Usar Iterator para iterar
 
        
for  (Envio envio 
:  envios) {
 
            
coleccion.
agregar
(envio);
 
            
 
            
// Usar Memento para guardar estado
 
            
historial.
guardarEstado
(envio);
 
            
 
            
// Usar Observer para notificaciones
 
            
envio.
adjuntarObservador
( new CentroDistribucionObservador
());
 
            
 
            
// Usar Mediator para comunicación
 
            
mediador.
notificar
( "ENVIO_CREADO"
, envio);
 
        
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
☐Decisiones de diseño 
• 
☐Análisis de ventajas/desventajas 
• 
☐Arquitectura event-driven 
 
Código Java 
Incluir: 
• 
☐Iterator: 2 interfaces + 3 colecciones = 5 clases 
• 
☐Mediator: 1 interfaz + 1 mediador + 5 componentes = 7 clases 
• 
☐Memento: 1 memento + 1 originador + 1 cuidador = 3 clases 
• 
☐Observer: 1 interfaz + 1 sujeto + 4 observadores = 6 clases 
• 
☐Integración: 1 clase = 1 clase 
 
Total: 22 clases Java 
 
Diagramas UML 
• 
☐Estructura de Iterator 
• 
☐Estructura de Mediator 
• 
☐Estructura de Memento 
• 
☐Estructura de Observer 
• 
☐Arquitectura event-driven 
 
 
 


<!-- pagina 22 -->
Criterios de Evaluación 
Criterio 
Excelente 
Bueno 
Aceptable 
Insuficiente 
Iterator 
3 colecciones funcionales 
2 colecciones 
1 colección 
Ninguna 
Mediator 
5 componentes + 
mediador 
4 componentes 
3 componentes 
2 o menos 
Memento 
Historial completo 
Historial básico 
Memento 
simple 
Incompleto 
Observer 
4 observadores 
3 observadores 
2 observadores 
1 o menos 
Diagramas 
4 diagramas claros 
4 diagramas básicos 
3 diagramas 
2 o menos 
Integración 
4 patrones integrados 
3 patrones integrados 
2 patrones 
1 o menos 
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
25+ casos, todos 
funcionan 
20+ casos, funcionan 
bien 
15+ casos 
Menos de 15 
 
