package com.logismart.infraestructura.adapter.envio;

public class UPSAPI {
    public String createShipment(double weightLbs) {
        return "UPS-" + System.currentTimeMillis();
    }
    public String trackShipment(String id) { return "DELIVERING"; }
    public double calculateRate(double weightLbs) { return weightLbs * 12.0; }
}
