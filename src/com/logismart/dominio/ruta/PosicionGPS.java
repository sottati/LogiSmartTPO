package com.logismart.dominio.ruta;

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

	public double getLat()              { return lat; }
	public double getLng()              { return lng; }
	public LocalDateTime getTimestamp() { return timestamp; }
	public double getPrecision()        { return precision; }

	public boolean esValida() {
		return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
	}

	public double distanciaA(PosicionGPS otra) {
		return haversineKm(this.lat, this.lng, otra.lat, otra.lng);
	}

	public static double haversineKm(double lat1, double lng1, double lat2, double lng2) {
		final double R = 6371.0;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	}
}

