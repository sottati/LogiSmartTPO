package com.logismart.dominio;

import java.util.Set;
import java.util.stream.Collectors;

public class Flota {
	private String id;
	private String empresaId;
	private Set<Vehiculo> vehiculos;
	private String estado;
	
	public Flota(String id, String empresaId, Set<Vehiculo> vehiculos, String estado) {
		this.id = id;
		this.empresaId = empresaId;
		this.vehiculos = vehiculos;
		this.estado = estado;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Vehiculo> getVehiculos() {
		return vehiculos;
	}

	public String getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void agregarVehiculo(Vehiculo vehiculo) {
		this.vehiculos.add(vehiculo);
	}

	public void quitarVehiculo(Vehiculo vehiculo) {
		this.vehiculos.remove(vehiculo);
	}

	public Set<Vehiculo> obtenerDisponibles() {
		return vehiculos.stream().filter(Vehiculo::isDisponible).collect(Collectors.toSet());
	}
}
