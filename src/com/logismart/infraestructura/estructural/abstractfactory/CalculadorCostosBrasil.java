package com.logismart.infraestructura.estructural.abstractfactory;

/** Tarifa base 80 + peso * 12, con ICMS 12% (Brasil). */
public class CalculadorCostosBrasil implements CalculadorCostos {

    @Override
    public double calcularCosto(double peso) {
        double subtotal = 80 + peso * 12;
        double total = subtotal * 1.12;
        System.out.println("[CalculadorBR] base=" + subtotal + " + ICMS 12% = R$" + total);
        return total;
    }
}
