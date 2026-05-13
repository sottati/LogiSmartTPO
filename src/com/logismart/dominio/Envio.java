package com.logismart.dominio;

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
	// ─── Campos existentes (Hitos anteriores) ────────────────────────────────
	private String id;
	private Empresa empresa;
	private String estado;
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
	private String metodoPago;
	private String productoId;

	// Constructor Hito 10 — usado por Chain / Command / Interpreter
	public Envio(String origen, String destino, double peso, double costo, String metodoPago, String productoId) {
		this.id = "H10-" + java.util.UUID.randomUUID().toString().substring(0, 8);
		this.empresa = null;
		this.estado = "PENDIENTE";
		this.prioridad = "MEDIA";
		this.fechaProgramada = null;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(this.id + "-seg", "PENDIENTE");
		this.entrega = new Entrega(this.id + "-ent");
		this.origen = origen;
		this.destino = destino;
		this.peso = peso;
		this.costo = costo;
		this.metodoPago = metodoPago;
		this.productoId = productoId;
	}

	// Constructor original — lo usan FabricaDeEnvios y sus subclases
	public Envio(String id, Empresa empresa, String prioridad, LocalDateTime fechaProgramada) {
		this.id = id;
		this.empresa = empresa;
		this.estado = "PENDIENTE";
		this.prioridad = prioridad;
		this.fechaProgramada = fechaProgramada;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(id + "-seg", "PENDIENTE");
		this.entrega = new Entrega(id + "-ent");
	}

	// Constructor privado — solo accesible desde EnvioBuilder.build()
	private Envio(EnvioBuilder builder) {
		this.id = builder.id;
		this.empresa = null;
		this.estado = "PENDIENTE";
		this.prioridad = "MEDIA";
		this.fechaProgramada = null;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(builder.id + "-seg", "PENDIENTE");
		this.entrega = new Entrega(builder.id + "-ent");
		this.origen = builder.origen;
		this.destino = builder.destino;
		this.descripcion = builder.descripcion;
		this.peso = builder.peso;
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
		private String descripcion = "";
		private double peso = 0.0;
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

		public EnvioBuilder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
		public EnvioBuilder peso(double peso)               { this.peso = peso; return this; }
		public EnvioBuilder fragil(boolean fragil)          { this.fragil = fragil; return this; }
		public EnvioBuilder requiereSignatura(boolean r)    { this.requiereSignatura = r; return this; }
		public EnvioBuilder requiereRefrigeracion(boolean r){ this.requiereRefrigeracion = r; return this; }
		public EnvioBuilder requiereAseguranza(boolean r)   { this.requiereAseguranza = r; return this; }
		public EnvioBuilder instruccionesEspeciales(String i){ this.instruccionesEspeciales = i; return this; }
		public EnvioBuilder contactoEmergencia(String c)    { this.contactoEmergencia = c; return this; }
		public EnvioBuilder horaEntregaPreferida(LocalTime h){ this.horaEntregaPreferida = h; return this; }

		public Envio build() { return new Envio(this); }
	}

	// ─── Getters campos existentes ───────────────────────────────────────────

	public String getId()                          { return id; }
	public void   setId(String id)                 { this.id = id; } // Prototype

	public Empresa getEmpresa() {
		return empresa;
	}

	public String getEstado() {
		return estado;
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
		estado = "EN_CURSO";
	}

	public void cancelar() {
		estado = "CANCELADO";
	}

	public void cerrar() {
		estado = "CERRADO";
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

	// ─── Getters / Setters Hito 10 ───────────────────────────────────────────

	public double  getCosto()                     { return costo; }
	public void    setCosto(double costo)          { this.costo = costo; }
	public String  getMetodoPago()                { return metodoPago; }
	public void    setMetodoPago(String m)         { this.metodoPago = m; }
	public String  getProductoId()                { return productoId; }

	@Override
	public String toString() {
		return "Envio{id='" + id + "', origen='" + origen + "', destino='" + destino
			 + "', peso=" + peso + ", fragil=" + fragil + "}";
	}
}
