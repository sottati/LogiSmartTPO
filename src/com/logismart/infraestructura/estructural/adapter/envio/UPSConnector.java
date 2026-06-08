package com.logismart.infraestructura.estructural.adapter.envio;

public class UPSConnector {
    public boolean sendPackage(String sourceLocation, String destinationLocation, double packageWeight) {
        return true;
    }

    public String trackPackage(String trackingCode) {
        return "Out for delivery";
    }

    public double estimateCost(String from, String to, double weight) {
        return weight * 10.0;
    }
}
