package com.logismart.dominio;

import java.time.LocalDateTime;

public class PosicionGPS {
	private double lat;
	private double lng;
	private LocalDateTime timestamp;
	private double precision;

	public PosicionGPS(double lat, double lng, LocalDateTime timestamp, double precision) {
		this.lat = lat;
		this.lng = lng;
		this.timestamp = timestamp;
		this.precision = precision;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public double getPrecision() {
		return precision;
	}

	public boolean esValida() {
		return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
	}

	public double distanciaA(PosicionGPS otra) {
		double dLat = lat - otra.lat;
		double dLng = lng - otra.lng;
		return Math.sqrt(dLat * dLat + dLng * dLng);
	}
}
