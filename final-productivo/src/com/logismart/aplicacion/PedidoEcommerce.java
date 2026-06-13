package com.logismart.aplicacion;

/**
 * DTO que representa una orden recibida de una plataforma e-commerce
 * (TiendaNube, MercadoShops, etc.).
 * Contiene solo los campos que varían entre órdenes del mismo lote:
 * el resto (origen, empresa, peso, prioridad) viene del prototipo base.
 */
public class PedidoEcommerce {
    private final String idPedido;
    private final String destino;
    private final String descripcion;

    public PedidoEcommerce(String idPedido, String destino, String descripcion) {
        this.idPedido    = idPedido;
        this.destino     = destino;
        this.descripcion = descripcion;
    }

    public PedidoEcommerce(String idPedido, String destino) {
        this(idPedido, destino, "");
    }

    public String getIdPedido()    { return idPedido; }
    public String getDestino()     { return destino; }
    public String getDescripcion() { return descripcion; }
}
