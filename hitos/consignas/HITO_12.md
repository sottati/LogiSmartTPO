<!-- pagina 1 -->
 Hito 12: Patrones de Comportamiento III 
State, Strategy, Template Method, Visitor 
Proyecto: LogiSmart - Sistema de Gestión de Logística 
Formato: Documento Markdown + Código Java 
 
Contexto del Hito 
Después de implementar patrones de comportamiento II (Hito 11), ahora aplicarás patrones 
que definen cómo los objetos cambian comportamiento y se procesan: 
 
1 State: Cambiar comportamiento según estado interno 
2 Strategy: Seleccionar algoritmo en tiempo de ejecución 
3 Template Method: Definir esqueleto de algoritmo 
4 Visitor: Procesar elementos de estructura sin cambiarla 
 
Objetivos del Hito 
Al completar este hito, habrás: 
 
• 
  Implementado State para máquina de estados de envíos 
• 
  Implementado Strategy para múltiples cálculos de costo 
• 
  Implementado Template Method para procesos de envío 
• 
  Implementado Visitor para análisis de estructura 
• 
  Creado 30+ casos de prueba 
• 
  Integrado los 4 patrones en un sistema unificado 
• 
  Documentado decisiones de diseño 
• 
  Demostrado ejercicios de selección de patrones 
 
 
 


<!-- pagina 2 -->
Actividades Prácticas 
Actividad 1: State - Máquina de Estados de Envíos 
Contexto: LogiSmart necesita una máquina de estados para los envíos: 
1 CONFIRMADO → puede validarse o cancelarse 
2 EN_TRANSITO → puede entregarse o retenerse 
3 EN_REPARTO → puede entregarse o devolvarse 
4 ENTREGADO → puede reclamarse 
5 CANCELADO → estado final 
6 RETENIDO → puede liberarse o cancelarse 
 
Paso 1: Interfaz State 
 
public
 interface
 EstadoEnvio
 {  
    
void  validar
(Envio 
envio );  
    
void  entregar
(Envio 
envio );  
    
void  cancelar
(Envio 
envio );  
    
void  retener
(Envio 
envio );  
    
void  devolver
(Envio 
envio );  
    
void  reclamar
(Envio 
envio );  
    
String 
obtenerNombre
();  
}  
 
Paso 2: Estados concretos 
 
// Estado: CONFIRMADO
 
public
 class
 EstadoConfirmado
 implements
 EstadoEnvio
 {  
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío validado"
);  
        
envio.
cambiarEstado
( new EstadoEnTransito
());
 
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede entregar, debe estar en 
tránsito"
);  
    
}  
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 


<!-- pagina 3 -->
        
System.out.
println
( "✓ Envío cancelado"
);  
        
envio.
cambiarEstado
( new EstadoCancelado
());
 
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede retener, debe estar en 
tránsito"
);  
    
}  
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede devolver, debe estar en 
reparto"
);  
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede reclamar, debe estar 
entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "CONFIRMADO" ;  
    
}  
}  
  
// Estado: EN_TRANSITO
 
public
 class
 EstadoEnTransito
 implements
 EstadoEnvio
 {  
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está validado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío en reparto"
);  
        
envio.
cambiarEstado
( new EstadoEnReparto
());
 
    
}  
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede cancelar, está en tránsito"
);  
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío retenido"
);  
        
envio.
cambiarEstado
( new EstadoRetenido
());
 
    
}  


<!-- pagina 4 -->
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede devolver, debe estar en 
reparto"
);  
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede reclamar, debe estar 
entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "EN_TRANSITO"
;  
    
}  
}  
  
// Estado: EN_REPARTO
 
public
 class
 EstadoEnReparto
 implements
 EstadoEnvio
 {  
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está validado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío entregado"
);  
        
envio.
cambiarEstado
( new EstadoEntregado
());
 
    
}  
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede cancelar, está en reparto"
);  
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está en reparto"
);  
    
}  
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío devuelto"
);  
        
envio.
cambiarEstado
( new EstadoEnTransito
());
 
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 


<!-- pagina 5 -->
        
System.out.
println
( " ✗ No se puede reclamar, debe estar 
entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "EN_REPARTO"
;  
    
}  
}  
  
// Estado: ENTREGADO
 
public
 class
 EstadoEntregado
 implements
 EstadoEnvio
 {  
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya fue entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede cancelar, ya fue entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya fue entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya fue entregado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Reclamo registrado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "ENTREGADO" ;  
    
}  
}  
  
// Estado: RETENIDO
 
public
 class
 EstadoRetenido
 implements
 EstadoEnvio
 {  


<!-- pagina 6 -->
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío retenido"
);  
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío liberado, continuando entrega"
);  
        
envio.
cambiarEstado
( new EstadoEnReparto
());
 
    
}  
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío cancelado"
);  
        
envio.
cambiarEstado
( new EstadoCancelado
());
 
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está retenido"
);  
    
}  
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( "✓ Envío devuelto"
);  
        
envio.
cambiarEstado
( new EstadoEnTransito
());
 
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ No se puede reclamar, está retenido"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "RETENIDO"
;  
    
}  
}  
  
// Estado: CANCELADO
 
public
 class
 EstadoCancelado
 implements
 EstadoEnvio
 {  
    
@Override
 
    
public
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  entregar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío cancelado"
);  
    
}  


<!-- pagina 7 -->
    
 
    
@Override
 
    
public
 void  cancelar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Ya está cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  retener
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  devolver
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 void  reclamar
(Envio 
envio ) {
 
        
System.out.
println
( " ✗ Envío cancelado"
);  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "CANCELADO" ;  
    
}  
}  
 
Paso 3: Contexto 
 
public
 class
 Envio  {  
    
private
 String id;
 
    
private
 EstadoEnvio estado;
 
    
 
    
public
 Envio (String 
id ) {
 
        
this
.id 
= id;  
        
this
.estado 
= new EstadoConfirmado
();  
    
}  
    
 
    
public
 void  cambiarEstado
(EstadoEnvio 
nuevoEstado
) {
 
        
this
.estado 
= nuevoEstado;
 
        
System.out.
println
( "[Envio] Estado cambiado a: "
 + 
nuevoEstado.
obtenerNombre
());
 
    
}  
    
 
    
public
 void  validar
() { estado.
validar
( this
); }
 
    
public
 void  entregar
() { estado.
entregar
( this
); }
 


<!-- pagina 8 -->
    
public
 void  cancelar
() { estado.
cancelar
( this
); }
 
    
public
 void  retener
() { estado.
retener
( this
); }
 
    
public
 void  devolver
() { estado.
devolver
( this
); }
 
    
public
 void  reclamar
() { estado.
reclamar
( this
); }
 
    
 
    
public
 String 
obtenerEstado
() { 
return
 estado.
obtenerNombre
(); }
 
    
public
 String 
getId
() { 
return
 id; }
 
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Flujo normal
 
Envio envio1 
= new Envio ( "ENV - 001" );  
envio1.
validar
();      
// CONFIRMADO → EN_TRANSITO
 
envio1.
entregar
();     
// EN_TRANSITO → EN_REPARTO
 
envio1.
entregar
();     
// EN_REPARTO → ENTREGADO
 
  
// Caso 2: Cancelación
 
Envio envio2 
= new Envio ( "ENV - 002" );  
envio2.
cancelar
();     
// CONFIRMADO → CANCELADO
 
  
// Caso 3: Retención
 
Envio envio3 
= new Envio ( "ENV - 003" );  
envio3.
validar
();      
// CONFIRMADO → EN_TRANSITO
 
envio3.
retener
();      
// EN_TRANSITO → RETENIDO
 
envio3.
entregar
();     
// RETENIDO → EN_REPARTO
 
  
// Caso 4: Devolución
 
Envio envio4 
= new Envio ( "ENV - 004" );  
envio4.
validar
();      
// CONFIRMADO → EN_TRANSITO
 
envio4.
entregar
();     
// EN_TRANSITO → EN_REPARTO
 
envio4.
devolver
();     
// EN_REPARTO → EN_TRANSITO
 
  
// Caso 5: Reclamo
 
Envio envio5 
= new Envio ( "ENV - 005" );  
envio5.
validar
();      
// CONFIRMADO → EN_TRANSITO
 
envio5.
entregar
();     
// EN_TRANSITO → EN_REPARTO
 
envio5.
entregar
();     
// EN_REPARTO → ENTREGADO
 
envio5.
reclamar
();     
// ENTREGADO → Reclamo
 
 
 
 
 


<!-- pagina 9 -->
Entregable: 
• 
Interfaz EstadoEnvio 
• 
6 estados concretos 
• 
Clase Envio (contexto) 
• 
5 casos de prueba 
 
 
 
 


<!-- pagina 10 -->
Actividad 2: Strategy - Cálculo de Costos 
Contexto: LogiSmart necesita calcular costos con diferentes estrategias: 
1 Por distancia 
2 Por peso 
3 Por urgencia 
4 Por volumen 
5 Híbrida (combinación) 
 
Paso 1: Interfaz Strategy 
 
public
 interface
 EstrategiaCalculoCosto
 {  
    
double  calcular
(Envio 
envio );  
    
String 
obtenerNombre
();  
}  
 
Paso 2: Estrategias concretas 
 
// Estrategia: Por Distancia
 
public
 class
 EstrategiaDistancia
 implements
 EstrategiaCalculoCosto
 {  
    
private
 double  costoPorKm 
= 10.0 ;  
    
 
    
@Override
 
    
public
 double  calcular
(Envio 
envio ) {
 
        
double  distancia 
= calcularDistancia
(envio.
getOrigen
(), 
envio.
getDestino
());
 
        
return
 distancia 
* costoPorKm;
 
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "Por Distancia"
;  
    
}  
    
 
    
private
 double  calcularDistancia
(String 
origen
, String 
destino
) {
 
        
// Simulación
 
        
return
 Math. random () 
* 500 ;  
    
}  
}  
  
// Estrategia: Por Peso
 
public
 class
 EstrategiaPeso
 implements
 EstrategiaCalculoCosto
 {  
    
private
 double  costoPorKg 
= 5.0 ;  


<!-- pagina 11 -->
    
 
    
@Override
 
    
public
 double  calcular
(Envio 
envio ) {
 
        
return
 envio.
getPeso
() 
* costoPorKg;
 
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "Por Peso"
;  
    
}  
}  
  
// Estrategia: Por Urgencia
 
public
 class
 EstrategiaUrgencia
 implements
 EstrategiaCalculoCosto
 {  
    
@Override
 
    
public
 double  calcular
(Envio 
envio ) {
 
        
if  ( "URGENTE" . equals
(envio.
getTipo
())) {
 
            
return
 500.0 ;  
        
} else  if  ( "EXPRESS"
. equals
(envio.
getTipo
())) {
 
            
return
 300.0 ;  
        
}  
        
return
 100.0 ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "Por Urgencia"
;  
    
}  
}  
  
// Estrategia: Por Volumen
 
public
 class
 EstrategiaVolumen
 implements
 EstrategiaCalculoCosto
 {  
    
@Override
 
    
public
 double  calcular
(Envio 
envio ) {
 
        
double  volumen 
= envio.
getPeso
() 
* 2; // Simulación
 
        
return
 volumen 
* 2.0 ;  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "Por Volumen"
;  
    
}  
}  
  
// Estrategia: Híbrida
 
public
 class
 EstrategiaHibrida
 implements
 EstrategiaCalculoCosto
 {  
    
@Override
 
    
public
 double  calcular
(Envio 
envio ) {
 
        
double  costoDistancia 
= new 
EstrategiaDistancia
().
calcular
(envio);
 
        
double  costoPeso 
= new EstrategiaPeso
().
calcular
(envio);
 


<!-- pagina 12 -->
        
double  costoUrgencia 
= new EstrategiaUrgencia
().
calcular
(envio);
 
        
 
        
return
 (costoDistancia 
* 0.4 ) + (costoPeso 
* 0.3 ) + 
(costoUrgencia 
* 0.3 );  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 "Híbrida"
;  
    
}  
}  
 
Paso 3: Contexto 
 
public
 class
 Envio  {  
    
private
 String id;
 
    
private
 String origen;
 
    
private
 String destino;
 
    
private
 double  peso;  
    
private
 String tipo;
 
    
private
 EstrategiaCalculoCosto estrategia;
 
    
 
    
public
 Envio (String 
id , String 
origen
, String 
destino
, double  peso , 
String 
tipo ) {
 
        
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
.tipo 
= tipo;
 
        
this
.estrategia 
= new EstrategiaDistancia
(); 
// Default
 
    
}  
    
 
    
public
 void  establecerEstrategia
(EstrategiaCalculoCosto 
estrategia
) {
 
        
this
.estrategia 
= estrategia;
 
        
System.out.
println
( "[Envio] Estrategia cambiada a: "
 + 
estrategia.
obtenerNombre
());
 
    
}  
    
 
    
public
 double  calcularCosto
() {
 
        
return
 estrategia.
calcular
( this
);  
    
}  
    
 
    
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
 String 
getTipo
() { 
return
 tipo; }
 


<!-- pagina 13 -->
}  
 
Paso 4: Casos de prueba 
 
// Caso 1: Estrategia por defecto (distancia)
 
Envio envio1 
= new Envio ( "ENV - 001" , "Buenos Aires"
, "Córdoba"
, 5.0 , 
"NORMAL" );  
System.out.
println
( "Costo (Distancia): $"
 + envio1.
calcularCosto
());
 
  
// Caso 2: Cambiar a estrategia por peso
 
envio1.
establecerEstrategia
( new EstrategiaPeso
());
 
System.out.
println
( "Costo (Peso): $"
 + envio1.
calcularCosto
());
 
  
// Caso 3: Cambiar a estrategia por urgencia
 
envio1.
establecerEstrategia
( new EstrategiaUrgencia
());
 
System.out.
println
( "Costo (Urgencia): $"
 + envio1.
calcularCosto
());
 
  
// Caso 4: Cambiar a estrategia por volumen
 
envio1.
establecerEstrategia
( new EstrategiaVolumen
());
 
System.out.
println
( "Costo (Volumen): $"
 + envio1.
calcularCosto
());
 
  
// Caso 5: Cambiar a estrategia híbrida
 
envio1.
establecerEstrategia
( new EstrategiaHibrida
());
 
System.out.
println
( "Costo (Híbrida): $"
 + envio1.
calcularCosto
());
 
 
Entregable: 
• 
Interfaz EstrategiaCalculoCosto 
• 
5 estrategias concretas 
• 
Clase Envio (contexto) 
• 
5 casos de prueba 
 
 
 


<!-- pagina 14 -->
Actividad 3: Template Method - Procesos de Envío 
Contexto: LogiSmart tiene procesos similares para diferentes tipos de envíos: 
6 Nacional 
7 Internacional 
8 Urgente 
 
Paso 1: Clase base abstracta 
 
public
 abstract
 class
 ProcesoProcesosEnvio
 {  
    
public
 final
 void  procesarEnvio
(Envio 
envio ) {
 
        
System.out.
println
( "[Proceso] Iniciando procesamiento..."
);  
        
validar
(envio);
 
        
calcularCosto
(envio);
 
        
procesarPago
(envio);
 
        
notificar
(envio);
 
        
System.out.
println
( "[Proceso] ✓ Procesamiento completado
\ n" );  
    
}  
    
 
    
protected
 abstract
 void  validar
(Envio 
envio );  
    
protected
 abstract
 void  calcularCosto
(Envio 
envio );  
    
protected
 abstract
 void  procesarPago
(Envio 
envio );  
    
protected
 abstract
 void  notificar
(Envio 
envio );  
}  
 
Paso 2: Subclases concretas 
 
// Proceso: Nacional
 
public
 class
 ProcesoNacional
 extends
 ProcesoProcesosEnvio
 {  
    
@Override
 
    
protected
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( "[Nacional] Validando envío nacional..."
);  
    
}  
    
 
    
@Override
 
    
protected
 void  calcularCosto
(Envio 
envio ) {
 
        
double  costo 
= 100.0  + (envio.
getPeso
() 
* 5);  
        
System.out.
println
( "[Nacional] Costo: $"
 + costo);
 
    
}  
    
 
    
@Override
 
    
protected
 void  procesarPago
(Envio 
envio ) {
 
        
System.out.
println
( "[Nacional] Procesando pago..."
);  
    
}  


<!-- pagina 15 -->
    
 
    
@Override
 
    
protected
 void  notificar
(Envio 
envio ) {
 
        
System.out.
println
( "[Nacional] Enviando notificación..."
);  
    
}  
}  
  
// Proceso: Internacional
 
public
 class
 ProcesoInternacional
 extends
 ProcesoProcesosEnvio
 {  
    
@Override
 
    
protected
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( "[Internacional] Validando documentación 
aduanal..."
);  
    
}  
    
 
    
@Override
 
    
protected
 void  calcularCosto
(Envio 
envio ) {
 
        
double  costoBase 
= 200.0  + (envio.
getPeso
() 
* 10 );  
        
double  costoAduanas 
= costoBase 
* 0.15 ;  
        
System.out.
println
( "[Internacional] Costo: $"
 + (costoBase 
+ 
costoAduanas));
 
    
}  
    
 
    
@Override
 
    
protected
 void  procesarPago
(Envio 
envio ) {
 
        
System.out.
println
( "[Internacional] Procesando pago 
internacional..."
);  
    
}  
    
 
    
@Override
 
    
protected
 void  notificar
(Envio 
envio ) {
 
        
System.out.
println
( "[Internacional] Enviando información 
aduanal..."
);  
    
}  
}  
  
// Proceso: Urgente
 
public
 class
 ProcesoUrgente
 extends
 ProcesoProcesosEnvio
 {  
    
@Override
 
    
protected
 void  validar
(Envio 
envio ) {
 
        
System.out.
println
( "[Urgente] Validación acelerada..."
);  
    
}  
    
 
    
@Override
 
    
protected
 void  calcularCosto
(Envio 
envio ) {
 
        
double  costo 
= 500.0  + (envio.
getPeso
() 
* 15 );  
        
System.out.
println
( "[Urgente] Costo prioritario: $"
 + costo);
 
    
}  
    
 
    
@Override
 
    
protected
 void  procesarPago
(Envio 
envio ) {
 


<!-- pagina 16 -->
        
System.out.
println
( "[Urgente] Procesando pago inmediato..."
);  
    
}  
    
 
    
@Override
 
    
protected
 void  notificar
(Envio 
envio ) {
 
        
System.out.
println
( "[Urgente] Enviando SMS urgente..."
);  
    
}  
}  
 
Paso 3: Casos de prueba 
 
// Caso 1: Proceso nacional
 
Envio envio1 
= new Envio ( "ENV - 001" );  
ProcesoProcesosEnvio procesoNacional 
= new ProcesoNacional
();
 
procesoNacional.
procesarEnvio
(envio1);
 
  
// Caso 2: Proceso internacional
 
Envio envio2 
= new Envio ( "ENV - 002" );  
ProcesoProcesosEnvio procesoInternacional 
= new ProcesoInternacional
();  
procesoInternacional.
procesarEnvio
(envio2);
 
  
// Caso 3: Proceso urgente
 
Envio envio3 
= new Envio ( "ENV - 003" );  
ProcesoProcesosEnvio procesoUrgente 
= new ProcesoUrgente
();  
procesoUrgente.
procesarEnvio
(envio3);
 
  
// Caso 4: Múltiples procesos
 
List<
Envio > envios 
= Arrays.
asList
(envio1, envio2, envio3);
 
List<
ProcesoProcesosEnvio
> procesos 
= Arrays.
asList
(  
    
procesoNacional, procesoInternacional, procesoUrgente
 
);  
  
// Caso 5: Extensibilidad
 
// Fácil agregar nuevo proceso (ej: ProcesoExpress)
 
 
Entregable: 
• 
Clase abstracta ProcesoProcesosEnvio 
• 
3 subclases concretas 
• 
5 casos de prueba 
 


<!-- pagina 17 -->
Actividad 4: Visitor - Análisis de Estructura 
Contexto: LogiSmart tiene una estructura jerárquica de centros. Necesita múltiples 
operaciones: 
9 Calcular ocupación total 
10 Generar reporte de estructura 
11 Calcular costo operativo 
12 Buscar puntos por ocupación 
 
Paso 1: Interfaz Visitor 
 
public
 interface
 VisitorCentro
 {  
    
void  visitar
(PuntoEntrega 
punto );  
    
void  visitar
(CentroRegional 
centro
);  
}  
 
Paso 2: Interfaz Elemento 
 
public
 interface
 CentroDistribucion
 {  
    
void  aceptar
(VisitorCentro 
visitor
);  
    
String 
obtenerNombre
();  
}  
 
Paso 3: Elementos concretos 
 
// Elemento: Punto de Entrega
 
public
 class
 PuntoEntrega
 implements
 CentroDistribucion
 {  
    
private
 String nombre;
 
    
private
 double  ocupacion;
 
    
 
    
public
 PuntoEntrega
(String 
nombre , double  ocupacion
) {
 
        
this
.nombre 
= nombre;  
        
this
.ocupacion 
= ocupacion;
 
    
}  
    
 
    
@Override
 
    
public
 void  aceptar
(VisitorCentro 
visitor
) {
 
        
visitor.
visitar
( this
);  
    
}  


<!-- pagina 18 -->
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 nombre;  
    
}  
    
 
    
public
 double  obtenerOcupacion
() {
 
        
return
 ocupacion;
 
    
}  
}  
  
// Elemento: Centro Regional
 
public
 class
 CentroRegional
 implements
 CentroDistribucion
 {  
    
private
 String nombre;
 
    
private
 List<
CentroDistribucion
> subcentros 
= new ArrayList<>();
 
    
 
    
public
 CentroRegional
(String 
nombre ) {
 
        
this
.nombre 
= nombre;  
    
}  
    
 
    
public
 void  agregarSubcentro
(CentroDistribucion 
centro
) {
 
        
subcentros.
add (centro);
 
    
}  
    
 
    
@Override
 
    
public
 void  aceptar
(VisitorCentro 
visitor
) {
 
        
visitor.
visitar
( this
);  
        
for  (CentroDistribucion centro 
:  subcentros) {
 
            
centro.
aceptar
(visitor);
 
        
}  
    
}  
    
 
    
@Override
 
    
public
 String 
obtenerNombre
() {
 
        
return
 nombre;  
    
}  
    
 
    
public
 List<
CentroDistribucion
> obtenerSubcentros
() {
 
        
return
 subcentros;
 
    
}  
}  
 
Paso 4: Visitors concretos 
 
// Visitor: Calcular Ocupación
 
public
 class
 VisitorCalculoOcupacion
 implements
 VisitorCentro
 {  
    
private
 double  ocupacionTotal 
= 0;  


<!-- pagina 19 -->
    
private
 int  puntosCont 
= 0;  
    
 
    
@Override
 
    
public
 void  visitar
(PuntoEntrega 
punto ) {
 
        
ocupacionTotal 
+=  punto.
obtenerOcupacion
();  
        
puntosCont
++ ;  
    
}  
    
 
    
@Override
 
    
public
 void  visitar
(CentroRegional 
centro
) {
 
        
System.out.
println
( "[Ocupación] Centro: "
 + 
centro.
obtenerNombre
());
 
    
}  
    
 
    
public
 double  obtenerOcupacionPromedio
() {
 
        
return
 puntosCont 
> 0 ? ocupacionTotal 
/  puntosCont 
:  0;  
    
}  
}  
  
// Visitor: Generar Reporte
 
public
 class
 VisitorGeneradorReporte
 implements
 VisitorCentro
 {  
    
private
 StringBuilder reporte 
= new StringBuilder
();  
    
private
 int  nivel 
= 0;  
    
 
    
@Override
 
    
public
 void  visitar
(PuntoEntrega 
punto ) {
 
        
agregarIndentacion
();  
        
reporte.
append ( " -  " ). append (punto.
obtenerNombre
())  
               
. append ( " 
(" ). append (punto.
obtenerOcupacion
()).
append ( "%) \ n" );  
    
}  
    
 
    
@Override
 
    
public
 void  visitar
(CentroRegional 
centro
) {
 
        
agregarIndentacion
();  
        
reporte.
append ( "+ "
). append (centro.
obtenerNombre
()).
append ( " \ n" );  
        
nivel
++ ;  
    
}  
    
 
    
private
 void  agregarIndentacion
() {
 
        
for  ( int  i = 0; i 
< nivel; i
++ ) {
 
            
reporte.
append ( "  "
);  
        
}  
    
}  
    
 
    
public
 String 
obtenerReporte
() {
 
        
return
 reporte.
toString
();  
    
}  
}  
  
// Visitor: Calcular Costo Operativo
 


<!-- pagina 20 -->
public
 class
 VisitorCalculoCostoOperativo
 implements
 VisitorCentro
 {  
    
private
 double  costoTotal 
= 0;  
    
 
    
@Override
 
    
public
 void  visitar
(PuntoEntrega 
punto ) {
 
        
double  costo 
= punto.
obtenerOcupacion
() 
* 10 ;  
        
costoTotal 
+=  costo;
 
    
}  
    
 
    
@Override
 
    
public
 void  visitar
(CentroRegional 
centro
) {
 
        
System.out.
println
( "[Costo] Centro: "
 + centro.
obtenerNombre
());
 
    
}  
    
 
    
public
 double  obtenerCostoTotal
() {
 
        
return
 costoTotal;
 
    
}  
}  
  
// Visitor: Buscar Puntos Críticos
 
public
 class
 VisitorBusquedaPuntosCriticos
 implements
 VisitorCentro
 {  
    
private
 List<
String
> puntosCriticos 
= new ArrayList<>();
 
    
private
 double  umbral 
= 80.0 ;  
    
 
    
@Override
 
    
public
 void  visitar
(PuntoEntrega 
punto ) {
 
        
if  (punto.
obtenerOcupacion
() 
> umbral) {
 
            
puntosCriticos.
add (punto.
obtenerNombre
() 
+ " ("
 + 
punto.
obtenerOcupacion
() 
+ "%)" );  
        
}  
    
}  
    
 
    
@Override
 
    
public
 void  visitar
(CentroRegional 
centro
) {
 
        
// No hacer nada
 
    
}  
    
 
    
public
 List<
String
> obtenerPuntosCriticos
() {
 
        
return
 puntosCriticos;
 
    
}  
}  
 
Paso 5: Casos de prueba 
 
// Crear estructura
 
CentroRegional centroNacional 
= new CentroRegional
( "Centro Nacional"
);  
  


<!-- pagina 21 -->
CentroRegional centroCABA 
= new CentroRegional
( "Centro CABA"
);  
centroCABA.
agregarSubcentro
( new PuntoEntrega
( "Punto San Telmo"
, 75.0 ));  
centroCABA.
agregarSubcentro
( new PuntoEntrega
( "Punto Recoleta"
, 85.0 ));  
  
CentroRegional centroMendoza 
= new CentroRegional
( "Centro Mendoza"
);  
centroMendoza.
agregarSubcentro
( new PuntoEntrega
( "Punto Mendoza Centro"
, 
60.0 ));  
  
centroNacional.
agregarSubcentro
(centroCABA);
 
centroNacional.
agregarSubcentro
(centroMendoza);
 
  
// Caso 1: Calcular ocupación
 
VisitorCalculoOcupacion visitorOcupacion 
= new VisitorCalculoOcupacion
();  
centroNacional.
aceptar
(visitorOcupacion);
 
System.out.
println
( "Ocupación promedio: "
 + 
visitorOcupacion.
obtenerOcupacionPromedio
() 
+ "%\ n" );  
  
// Caso 2: Generar reporte
 
VisitorGeneradorReporte visitorReporte 
= new VisitorGeneradorReporte
();  
centroNacional.
aceptar
(visitorReporte);
 
System.out.
println
(visitorReporte.
obtenerReporte
());
 
  
// Caso 3: Calcular costo operativo
 
VisitorCalculoCostoOperativo visitorCosto 
= new 
VisitorCalculoCostoOperativo
();  
centroNacional.
aceptar
(visitorCosto);
 
System.out.
println
( "Costo operativo total: $"
 + 
visitorCosto.
obtenerCostoTotal
() 
+ " \ n" );  
  
// Caso 4: Buscar puntos críticos
 
VisitorBusquedaPuntosCriticos visitorCriticos 
= new 
VisitorBusquedaPuntosCriticos
();  
centroNacional.
aceptar
(visitorCriticos);
 
System.out.
println
( "Puntos críticos: "
 + 
visitorCriticos.
obtenerPuntosCriticos
());
 
  
// Caso 5: Múltiples visitors en secuencia
 
System.out.
println
( " \ n=== Análisis Completo ==="
);  
centroNacional.
aceptar
(visitorOcupacion);
 
centroNacional.
aceptar
(visitorCosto);
 
centroNacional.
aceptar
(visitorCriticos);
 
 
Entregable: 
• 
Interfaz VisitorCentro 
• 
Interfaz CentroDistribucion 
• 
2 elementos concretos 
• 
4 visitors concretos 


<!-- pagina 22 -->
• 
5 casos de prueba 
 
 
 


<!-- pagina 23 -->
Actividad 5: Integración Completa 
Integra los 4 patrones en un sistema unificado: 
 
public
 class
 SistemaLogisticaAvanzada
 {  
    
private
 Envio envio;
 
    
private
 EstrategiaCalculoCosto estrategia;
 
    
private
 ProcesoProcesosEnvio proceso;
 
    
private
 CentroDistribucion estructura;
 
    
 
    
public
 void  procesarEnvioCompleto
() {
 
        
// State: Máquina de estados
 
        
envio.
validar
();  
        
envio.
entregar
();  
        
 
        
// Strategy: Calcular costo
 
        
estrategia 
= new EstrategiaHibrida
();  
        
double  costo 
= envio.
calcularCosto
();  
        
 
        
// Template Method: Procesar
 
        
proceso.
procesarEnvio
(envio);
 
        
 
        
// Visitor: Analizar estructura
 
        
VisitorCalculoOcupacion visitor 
= new VisitorCalculoOcupacion
();  
        
estructura.
aceptar
(visitor);
 
    
}  
}  
 
 
 


<!-- pagina 24 -->
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
☐Integración de patrones 
 
Código Java 
Incluir: 
• 
☐State: 1 interfaz + 6 estados = 7 clases 
• 
☐Strategy: 1 interfaz + 5 estrategias = 6 clases 
• 
☐Template Method: 1 abstracta + 3 concretas = 4 clases 
• 
☐Visitor: 2 interfaces + 2 elementos + 4 visitors = 8 clases 
• 
☐Integración: 1 clase = 1 clase 
 
Total: 26 clases Java 
 
Diagramas UML 
• 
☐Estructura de State 
• 
☐Estructura de Strategy 
• 
☐Estructura de Template Method 
• 
☐Estructura de Visitor 
• 
☐Integración completa 
 
 
 


<!-- pagina 25 -->
Criterios de Evaluación 
Criterio 
Excelente 
Bueno 
Aceptable 
Insuficiente 
State 
6 estados funcionales 
5 estados 
4 estados 
3 o menos 
Strategy 
5 estrategias 
4 estrategias 
3 
estrategias 
2 o menos 
Template 
Method 
3 procesos + extensible 
3 procesos 
2 procesos 
1 o menos 
Visitor 
4 visitors + 2 elementos 
3 visitors 
2 visitors 
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
Casos de Prueba 
30+ casos, todos funcionan 
25+ casos, funcionan 
bien 
20+ casos 
Menos de 20 
 
