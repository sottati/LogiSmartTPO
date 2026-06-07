package com.logismart.aplicacion;

import com.logismart.dominio.empresa.Reporte;

public class ReporteService {
	public String exportar(Reporte reporte) {
		if (reporte == null) {
			throw new IllegalArgumentException("El reporte no puede ser nulo");
		}
		return "REPORTE-" + reporte.getId();
	}

	public void compartir(Reporte reporte, String destino) {
		if (reporte == null) {
			throw new IllegalArgumentException("El reporte no puede ser nulo");
		}
		if (destino == null || destino.isBlank()) {
			throw new IllegalArgumentException("El destino no puede estar vacio");
		}
	}
}

