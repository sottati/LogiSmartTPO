package com.logismart.infraestructura.adapter.pago;

public class StripeAPI {
    public String charge(long amountCents, String currency, String description) {
        return "ch_" + System.currentTimeMillis();
    }
    public String retrieveCharge(String chargeId) { return "succeeded"; }
    public void refund(String chargeId, long amountCents) {}
}
