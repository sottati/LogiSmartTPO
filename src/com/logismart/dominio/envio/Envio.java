package com.logismart.dominio.envio;

import com.logismart.dominio.empresa.Empresa;
import com.logismart.infraestructura.comportamiento.state.EstadoEnCurso;
import com.logismart.infraestructura.comportamiento.state.EstadoCerrado;
import com.logismart.infraestructura.comportamiento.state.EstadoPendiente;
import com.logismart.infraestructura.comportamiento.state.EstadoEnvio;
import com.logismart.infraestructura.comportamiento.strategy.EstrategiaCalculoCosto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad central del sistema. Soporta dos formas de construcción:
 * – Constructor público original (usado por FabricaDeEnvios y sus subclases).
 * – Builder interno (EnvioBuilder) para envíos con atributos logísticos detallados.
 * Implementa Cloneable para permitir clonación superficial con copia profunda de ordenes.
 */
public class Envio implements Cloneable {
	// ─── Campos existentes ────────────────────────────────
	private String id;
	private Empresa empresa;
	private String prioridad;
	private LocalDateTime fechaProgramada;
	private List<Orden> ordenes;
	private SeguimientoEnvio seguimiento;
	private Entrega entrega;

	// ─── Campos del Builder ──────────────────────────────────────────────────
	private String origen;
	private String destino;
	private String descripcion;
	private double peso;
	private boolean fragil;
	private boolean requiereSignatura;
	private boolean requiereRefrigeracion;
	private boolean requiereAseguranza;
	private String instruccionesEspeciales;
	private String contactoEmergencia;
	private LocalTime horaEntregaPreferida;

	// ─── Campos Hito 10 (Chain / Command / Interpreter) ──────────────────────
	private double costo;

	// ─── Campos Hito 11 (Observer) ───────────────────────────────────────────
	private final java.util.List<ObservadorEnvio> observadores = new java.util.ArrayList<>();

	// ─── Campos Hito 12 (State / Strategy) ───────────────────────────────────
	private EstadoEnvio estadoGoF = new EstadoPendiente();
	private String tipo;
	private EstrategiaCalculoCosto estrategia;

	// Constructor original - lo usan FabricaDeEnvios y sus subclases
	public Envio(String id, Empresa empresa, String prioridad, LocalDateTime fechaProgramada) {
		this.id = id;
		this.empresa = empresa;
		this.estadoGoF = new EstadoPendiente();
		this.prioridad = prioridad;
		this.fechaProgramada = fechaProgramada;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(id + "-seg", "PENDIENTE");
		this.entrega = new Entrega(id + "-ent");
	}

	// Constructor privado - solo accesible desde EnvioBuilder.build()
	private Envio(EnvioBuilder builder) {
		this.id = builder.id;
		this.empresa = null;
		this.estadoGoF = EstadoEnvio.fromNombre(builder.estado);
		this.prioridad = "MEDIA";
		this.fechaProgramada = null;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(builder.id + "-seg", builder.estado);
		this.entrega = new Entrega(builder.id + "-ent");
		this.origen = builder.origen;
		this.destino = builder.destino;
		this.descripcion = builder.descripcion;
		this.peso = builder.peso;
		this.costo = builder.costo;
		this.tipo = builder.tipo;
		this.fragil = builder.fragil;
		this.requiereSignatura = builder.requiereSignatura;
		this.requiereRefrigeracion = builder.requiereRefrigeracion;
		this.requiereAseguranza = builder.requiereAseguranza;
		this.instruccionesEspeciales = builder.instruccionesEspeciales;
		this.contactoEmergencia = builder.contactoEmergencia;
		this.horaEntregaPreferida = builder.horaEntregaPreferida;
	}

	// ─── Prototype ────────────────────────────────────────────────────────────

	/**
	 * Copia superficial del Envio. La lista de ordenes se copia para que
	 * el clon tenga su propia referencia mutable; los demás campos son
	 * primitivos, inmutables (String, LocalTime) o se comparten sin riesgo.
	 */
	@Override
	public Envio clone() {
		try {
			Envio clonado = (Envio) super.clone();
			clonado.ordenes = new ArrayList<>(this.ordenes);
			return clonado;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Error al clonar Envio", e);
		}
	}

	// ─── Builder anidado ─────────────────────────────────────────────────────

	public static class EnvioBuilder {
		private final String id;
		private final String origen;
		private final String destino;
		private String estado = "PENDIENTE";
		private String descripcion = "";
		private double peso = 0.0;
		private double costo = 0.0;
		private String tipo = null;
		private boolean fragil = false;
		private boolean requiereSignatura = false;
		private boolean requiereRefrigeracion = false;
		private boolean requiereAseguranza = false;
		private String instruccionesEspeciales = "";
		private String contactoEmergencia = "";
		private LocalTime horaEntregaPreferida = null;

		public EnvioBuilder(String id, String origen, String destino) {
			this.id = id;
			this.origen = origen;
			this.destino = destino;
		}

		public EnvioBuilder(String id) {
			this.id = id;
			this.origen = "";
			this.destino = "";
		}

		public EnvioBuilder estado(String estado)             { this.estado = estado; return this; }
		public EnvioBuilder descripcion(String descripcion)   { this.descripcion = descripcion; return this; }
		public EnvioBuilder peso(double peso)                 { this.peso = peso; return this; }
		public EnvioBuilder costo(double costo)               { this.costo = costo; return this; }
		public EnvioBuilder tipo(String tipo)                 { this.tipo = tipo; return this; }
		public EnvioBuilder fragil(boolean fragil)            { this.fragil = fragil; return this; }
		public EnvioBuilder requiereSignatura(boolean r)      { this.requiereSignatura = r; return this; }
		public EnvioBuilder requiereRefrigeracion(boolean r)  { this.requiereRefrigeracion = r; return this; }
		public EnvioBuilder requiereAseguranza(boolean r)     { this.requiereAseguranza = r; return this; }
		public EnvioBuilder instruccionesEspeciales(String i) { this.instruccionesEspeciales = i; return this; }
		public EnvioBuilder contactoEmergencia(String c)      { this.contactoEmergencia = c; return this; }
		public EnvioBuilder horaEntregaPreferida(LocalTime h) { this.horaEntregaPreferida = h; return this; }

		public Envio build() { return new Envio(this); }
	}

	// ─── Getters campos existentes ───────────────────────────────────────────

	public String getId()                          { return id; }
	public void   setId(String id)                 { this.id = id; } // Prototype

	public Empresa getEmpresa() {
		return empresa;
	}

	public String getEstado() {
		return estadoGoF.obtenerNombre();
	}

	public String getPrioridad() {
		return prioridad;
	}

	public LocalDateTime getFechaProgramada() {
		return fechaProgramada;
	}

	public List<Orden> getOrdenes() {
		return Collections.unmodifiableList(ordenes);
	}

	public SeguimientoEnvio getSeguimiento() {
		return seguimiento;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public void agregarOrden(Orden orden) {
		orden.asociarEnvio(this);
		ordenes.add(orden);
	}

	public void reprogramar(LocalDateTime nuevaFechaProgramada) {
		fechaProgramada = nuevaFechaProgramada;
	}

	public void iniciar() {
		cambiarEstado(new EstadoEnCurso());
	}

	public void cancelar() {
		estadoGoF.cancelar(this);
	}

	public void cerrar() {
		cambiarEstado(new EstadoCerrado());
	}

	// ─── Getters del Builder ─────────────────────────────────────────────────

	public String   getOrigen()                   { return origen; }
	public String   getDestino()                  { return destino; }
	public String   getDescripcion()              { return descripcion; }
	public double   getPeso()                     { return peso; }
	public boolean  isFragil()                    { return fragil; }
	public boolean  isRequiereSignatura()         { return requiereSignatura; }
	public boolean  isRequiereRefrigeracion()     { return requiereRefrigeracion; }
	public boolean  isRequiereAseguranza()        { return requiereAseguranza; }
	public String   getInstruccionesEspeciales()  { return instruccionesEspeciales; }
	public String   getContactoEmergencia()       { return contactoEmergencia; }
	public LocalTime getHoraEntregaPreferida()    { return horaEntregaPreferida; }
	public String   getTipo()                     { return tipo; }

	// ─── Getters / Setters Hito 10 ───────────────────────────────────────────

	public double  getCosto()                     { return costo; }
	public void    setCosto(double costo)          { this.costo = costo; }

	// ─── Hito 11: Memento - Originador ───────────────────────────────────────

	/**
	 * Captura el estado actual del Envío en un MementoEnvio (snapshot inmutable).
	 * No viola encapsulación: el Cuidador (HistorialEnvios) sólo accede al estado
	 * a través de este método.
	 */
	public MementoEnvio crearMemento() {
		return new MementoEnvio(estadoGoF.obtenerNombre(), origen, destino, peso, costo);
	}

	/**
	 * Restaura el estado del Envío desde un snapshot previo.
	 * Equivalente al "undo" del patrón Memento.
	 */
	public void restaurarDesdeMemento(MementoEnvio memento) {
		this.estadoGoF = EstadoEnvio.fromNombre(memento.obtenerEstado());
		this.origen  = memento.obtenerOrigen();
		this.destino = memento.obtenerDestino();
		this.peso    = memento.obtenerPeso();
		this.costo   = memento.obtenerCosto();
	}

	/** Alias solicitado por la consigna - equivalente a getEstado(). */
	public String obtenerEstado() { return estadoGoF.obtenerNombre(); }

	// ─── Hito 12: State ──────────────────────────────────────────────────────

	public void cambiarEstado(EstadoEnvio nuevoEstado) {
		this.estadoGoF = nuevoEstado;
		System.out.println("[Envio " + id + "] Estado → " + nuevoEstado.obtenerNombre());
		notificarObservadores();
	}

	public void validar()  { estadoGoF.validar(this); }
	public void entregar() { estadoGoF.entregar(this); }
	public void retener()  { estadoGoF.retener(this); }
	public void devolver() { estadoGoF.devolver(this); }
	public void reclamar() { estadoGoF.reclamar(this); }

	public String obtenerNombreEstadoGoF() {
		return estadoGoF.obtenerNombre();
	}

	// ─── Hito 12: Strategy ───────────────────────────────────────────────────

	public void establecerEstrategia(EstrategiaCalculoCosto estrategia) {
		this.estrategia = estrategia;
	}

	public double calcularCostoConEstrategia() {
		if (estrategia == null) {
			throw new IllegalStateException("No hay estrategia de calculo configurada");
		}
		this.costo = estrategia.calcular(this);
		return costo;
	}

	// ─── Hito 11: Observer - Sujeto ──────────────────────────────────────────

	/** Suscribe un observador para recibir notificaciones de cambios de estado. */
	public void adjuntarObservador(ObservadorEnvio observador) {
		observadores.add(observador);
		System.out.println("✓ Observador adjuntado: " + observador.getClass().getSimpleName());
	}

	/** Elimina la suscripción de un observador. */
	public void desadjuntarObservador(ObservadorEnvio observador) {
		observadores.remove(observador);
		System.out.println("✓ Observador desadjuntado: " + observador.getClass().getSimpleName());
	}

	/** Propaga el cambio de estado a todos los observadores registrados. */
	private void notificarObservadores() {
		for (ObservadorEnvio obs : observadores) {
			obs.actualizar(this);
		}
	}

	@Override
	public String toString() {
		return "Envio{id='" + id + "', origen='" + origen + "', destino='" + destino
			 + "', peso=" + peso + ", fragil=" + fragil + "}";
	}
}
