package com.logismart.infraestructura.adapter.envio;

public class FedExAPI {
    public String shipPackage(String from, String to, double weightKg) {
        return "FX-" + System.currentTimeMillis();
    }
    public String getTrackingStatus(String trackingNumber) { return "IN_TRANSIT"; }
    public double getRateQuote(double weightKg) { return weightKg * 18.0; }
}
