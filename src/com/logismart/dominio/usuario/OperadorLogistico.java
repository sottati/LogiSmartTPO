package com.logismart.dominio.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.vehiculo.Vehiculo;

public class OperadorLogistico extends Usuario implements IPermisos {
	private static final Rol ROL = Rol.OPERADOR;

	private Empresa empresa;
	private String zonaOperacion;
	private String turno;

	public OperadorLogistico(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			Empresa empresa,
			String zonaOperacion,
			String turno) {
		super(id, username, email, passwordHash, rol, estado);
		this.empresa = empresa;
		this.zonaOperacion = zonaOperacion;
		this.turno = turno;
	}

	public Empresa getEmpresa()                        { return empresa; }
	public void setEmpresa(Empresa empresa)            { this.empresa = empresa; }
	public String getZonaOperacion()                   { return zonaOperacion; }
	public void setZonaOperacion(String zonaOperacion) { this.zonaOperacion = zonaOperacion; }
	public String getTurno()                           { return turno; }
	public void setTurno(String turno)                 { this.turno = turno; }

	public void importarOrdenes() {
		System.out.println("[OperadorLogistico] Importando órdenes para zona: " + zonaOperacion + ".");
	}

	public void gestionarFlota() {
		System.out.println("[OperadorLogistico] Gestionando flota en turno: " + turno + ".");
	}

	public void planificarRuta() {
		System.out.println("[OperadorLogistico] Planificando ruta para zona: " + zonaOperacion + ".");
	}

	public Envio crearEnvio(String prioridad, LocalDateTime fechaProgramada) {
		if (empresa == null) {
			throw new IllegalStateException("El operador debe estar asociado a una empresa");
		}
		return new Envio(UUID.randomUUID().toString(), empresa, prioridad, fechaProgramada);
	}

	public Ruta planificarRuta(Vehiculo vehiculo, Transportista transportista) {
		Ruta ruta = new Ruta(UUID.randomUUID().toString(), 0.0, 0, "PLANIFICADA");
		ruta.asignarVehiculo(vehiculo);
		ruta.asignarTransportista(transportista);
		return ruta;
	}

	public void asignarRuta() {
		System.out.println("[OperadorLogistico] Asignando ruta...");
	}

	public String getNombre() { return getUsername(); }

	@Override
	public void saludar() {
		System.out.println("[Operador] Hola, soy el operador logístico " + getUsername() + ".");
	}

	@Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
	@Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
	@Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
	@Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
	@Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
}

