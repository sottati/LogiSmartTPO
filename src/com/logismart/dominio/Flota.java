package com.logismart.dominio;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Flota {
	private String id;
	private Empresa empresa;
	private Set<Vehiculo> vehiculos;
	private String estado;
	
	public Flota(String id, Empresa empresa, Set<Vehiculo> vehiculos, String estado) {
		this.id = id;
		this.empresa = empresa;
		this.vehiculos = vehiculos;
		this.estado = estado;
	}
	
	public String getId() {
		return id;
	}

	public Set<Vehiculo> getVehiculos() {
		return Collections.unmodifiableSet(vehiculos);
	}

	public Empresa getEmpresa() {
		return empresa;
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
