package com.logismart.dominio.envio;

import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.estado.EstadoConfirmado;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad central del dominio. Soporta dos formas de construcción:
 *   – Constructor con Empresa (usado por la capa de aplicación al importar pedidos).
 *   – Builder interno (EnvioBuilder) para construcción paso a paso con atributos logísticos.
 * Implementa Cloneable (Prototype) para clonar plantillas durante importación masiva (CU-01).
 * Actúa como Originador del patrón Memento y como Sujeto del patrón Observer.
 */
public class Envio implements Cloneable {

    private String id;
    private Empresa empresa;
    private String prioridad;
    private LocalDateTime fechaProgramada;

    private String origen;
    private String destino;
    private String descripcion;
    private double peso;
    private double costo;
    private String tipo;
    private boolean fragil;
    private boolean requiereSignatura;
    private boolean requiereRefrigeracion;
    private boolean requiereAseguranza;
    private String instruccionesEspeciales;
    private String contactoEmergencia;
    private LocalTime horaEntregaPreferida;

    private EstadoEnvio estado;
    private EstrategiaCalculoCosto estrategia;
    private final List<ObservadorEnvio> observadores = new ArrayList<>();

    // ── Constructor para importación desde e-commerce (CU-01) ──────────────────

    public Envio(String id, Empresa empresa, String prioridad, LocalDateTime fechaProgramada) {
        this.id              = id;
        this.empresa         = empresa;
        this.prioridad       = prioridad;
        this.fechaProgramada = fechaProgramada;
        this.estado          = new EstadoConfirmado();
    }

    private Envio(EnvioBuilder b) {
        this.id                    = b.id;
        this.empresa               = b.empresa;
        this.origen                = b.origen;
        this.destino               = b.destino;
        this.descripcion           = b.descripcion;
        this.peso                  = b.peso;
        this.costo                 = b.costo;
        this.tipo                  = b.tipo;
        this.prioridad             = b.prioridad;
        this.fragil                = b.fragil;
        this.requiereSignatura     = b.requiereSignatura;
        this.requiereRefrigeracion = b.requiereRefrigeracion;
        this.requiereAseguranza    = b.requiereAseguranza;
        this.instruccionesEspeciales = b.instruccionesEspeciales;
        this.contactoEmergencia    = b.contactoEmergencia;
        this.horaEntregaPreferida  = b.horaEntregaPreferida;
        this.estado                = EstadoEnvio.fromNombre(b.estado);
    }

    // ── Builder (GoF) ──────────────────────────────────────────────────────────

    public static class EnvioBuilder {
        private final String id;
        private final String origen;
        private final String destino;
        private Empresa empresa    = null;
        private String estado      = "CONFIRMADO";
        private String descripcion = "";
        private double peso        = 0.0;
        private double costo       = 0.0;
        private String tipo        = "NACIONAL";
        private String prioridad   = "MEDIA";
        private boolean fragil                = false;
        private boolean requiereSignatura     = false;
        private boolean requiereRefrigeracion = false;
        private boolean requiereAseguranza    = false;
        private String instruccionesEspeciales = "";
        private String contactoEmergencia      = "";
        private LocalTime horaEntregaPreferida = null;

        public EnvioBuilder(String id, String origen, String destino) {
            this.id      = id;
            this.origen  = origen;
            this.destino = destino;
        }

        public EnvioBuilder empresa(Empresa v)             { this.empresa = v; return this; }
        public EnvioBuilder estado(String v)               { this.estado = v; return this; }
        public EnvioBuilder descripcion(String v)          { this.descripcion = v; return this; }
        public EnvioBuilder peso(double v)                 { this.peso = v; return this; }
        public EnvioBuilder costo(double v)                { this.costo = v; return this; }
        public EnvioBuilder tipo(String v)                 { this.tipo = v; return this; }
        public EnvioBuilder prioridad(String v)            { this.prioridad = v; return this; }
        public EnvioBuilder fragil(boolean v)              { this.fragil = v; return this; }
        public EnvioBuilder requiereSignatura(boolean v)   { this.requiereSignatura = v; return this; }
        public EnvioBuilder requiereRefrigeracion(boolean v){ this.requiereRefrigeracion = v; return this; }
        public EnvioBuilder requiereAseguranza(boolean v)  { this.requiereAseguranza = v; return this; }
        public EnvioBuilder instruccionesEspeciales(String v){ this.instruccionesEspeciales = v; return this; }
        public EnvioBuilder contactoEmergencia(String v)   { this.contactoEmergencia = v; return this; }
        public EnvioBuilder horaEntregaPreferida(LocalTime v){ this.horaEntregaPreferida = v; return this; }

        public Envio build() { return new Envio(this); }
    }

    // ── Prototype (GoF) ────────────────────────────────────────────────────────

    /**
     * Crea una copia independiente de este Envío.
     * Se usa en CU-01 para importar lotes de órdenes similares desde e-commerce:
     * se toma un prototipo base y se clonan N instancias variando sólo el destino y el id.
     */
    @Override
    public Envio clone() {
        try {
            return (Envio) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar Envio", e);
        }
    }

    // ── State (GoF) ────────────────────────────────────────────────────────────

    public void cambiarEstado(EstadoEnvio nuevoEstado) {
        this.estado = nuevoEstado;
        notificarObservadores();
    }

    public void validar()  { estado.validar(this); }
    public void entregar() { estado.entregar(this); }
    public void cancelar() { estado.cancelar(this); }
    public void retener()  { estado.retener(this); }
    public void devolver() { estado.devolver(this); }
    public void reclamar() { estado.reclamar(this); }

    // ── Observer (GoF) ─────────────────────────────────────────────────────────

    public void adjuntarObservador(ObservadorEnvio obs)   { observadores.add(obs); }
    public void desadjuntarObservador(ObservadorEnvio obs) { observadores.remove(obs); }

    private void notificarObservadores() {
        for (ObservadorEnvio obs : observadores) {
            obs.actualizar(this);
        }
    }

    // ── Memento (GoF) — Originador ─────────────────────────────────────────────

    public MementoEnvio crearMemento() {
        return new MementoEnvio(estado.obtenerNombre(), origen, destino, peso, costo);
    }

    public void restaurarDesdeMemento(MementoEnvio m) {
        this.estado  = EstadoEnvio.fromNombre(m.obtenerEstado());
        this.origen  = m.obtenerOrigen();
        this.destino = m.obtenerDestino();
        this.peso    = m.obtenerPeso();
        this.costo   = m.obtenerCosto();
    }

    // ── Strategy (GoF) ────────────────────────────────────────────────────────

    public void establecerEstrategia(EstrategiaCalculoCosto estrategia) {
        this.estrategia = estrategia;
    }

    public double calcularCosto() {
        if (estrategia == null) throw new IllegalStateException("Sin estrategia de cálculo configurada.");
        this.costo = estrategia.calcular(this);
        return costo;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public String          getId()                     { return id; }
    public void            setId(String id)            { this.id = id; }
    public Empresa         getEmpresa()                { return empresa; }
    public String          getEstado()                 { return estado.obtenerNombre(); }
    public String          getPrioridad()              { return prioridad; }
    public LocalDateTime   getFechaProgramada()        { return fechaProgramada; }
    public String          getOrigen()                 { return origen; }
    public void            setOrigen(String origen)    { this.origen = origen; }
    public String          getDestino()                { return destino; }
    public void            setDestino(String destino)  { this.destino = destino; }
    public String          getDescripcion()            { return descripcion; }
    public double          getPeso()                   { return peso; }
    public double          getCosto()                  { return costo; }
    public void            setCosto(double costo)      { this.costo = costo; }
    public String          getTipo()                   { return tipo; }
    public boolean         isFragil()                  { return fragil; }
    public boolean         isRequiereSignatura()       { return requiereSignatura; }
    public boolean         isRequiereRefrigeracion()   { return requiereRefrigeracion; }
    public boolean         isRequiereAseguranza()      { return requiereAseguranza; }
    public String          getInstruccionesEspeciales(){ return instruccionesEspeciales; }
    public String          getContactoEmergencia()     { return contactoEmergencia; }
    public LocalTime       getHoraEntregaPreferida()   { return horaEntregaPreferida; }

    public List<ObservadorEnvio> getObservadores() {
        return Collections.unmodifiableList(observadores);
    }

    @Override
    public String toString() {
        return "Envio{id='" + id + "', origen='" + origen + "', destino='" + destino
             + "', peso=" + peso + ", estado='" + getEstado() + "'}";
    }
}
