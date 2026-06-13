package com.logismart.dominio.ruta;

public class PosicionGPS {

    private final double lat;
    private final double lng;
    private final long   timestamp;

    public PosicionGPS(double lat, double lng) {
        this.lat       = lat;
        this.lng       = lng;
        this.timestamp = System.currentTimeMillis();
    }

    /** Distancia en km entre dos coordenadas geográficas (fórmula Haversine). */
    public static double haversineKm(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public double getLat()       { return lat; }
    public double getLng()       { return lng; }
    public long   getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "GPS{lat=" + lat + ", lng=" + lng + "}";
    }
}
