package com.logismart.infraestructura.abstractfactory;

/** Tarifa base 100 + peso * 15, con IVA 21% (Argentina). */
public class CalculadorCostosArgentina implements CalculadorCostos {

    @Override
    public double calcularCosto(double peso) {
        double subtotal = 100 + peso * 15;
        double total = subtotal * 1.21;
        System.out.println("[CalculadorAR] base=" + subtotal + " + IVA 21% = $" + total);
        return total;
    }
}
