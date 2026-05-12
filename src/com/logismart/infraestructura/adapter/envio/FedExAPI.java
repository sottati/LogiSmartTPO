package com.logismart.infraestructura.adapter.envio;

public class FedExAPI {
    public int crearShipment(String from, String to, double weight) {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    public String getShipmentStatus(int shipmentId) {
        return "DELIVERED";
    }

    public float getShippingRate(String from, String to, double weight) {
        return (float) (weight * 12.0);
    }
}
