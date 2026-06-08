package com.logismart.infraestructura.estructural.adapter.pago;

public class StripeAPI {
    public boolean charge(double amountInCents, String description) {
        return amountInCents > 0;
    }

    public String getChargeStatus(String chargeId) {
        return "succeeded";
    }
}
