package com.logismart.aplicacion;

import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;
import com.logismart.aplicacion.cadena.CadenaValidadores;
import com.logismart.aplicacion.cadena.ContextoValidacion;
import com.logismart.persistencia.UnitOfWork;
import com.logismart.infraestructura.singleton.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * CU-01: Importar Pedidos desde E-commerce (TiendaNube, MercadoShops).
 *
 * Cuando llega un lote de órdenes con la misma empresa, origen y tipo de producto,
 * construir cada Envío desde cero sería redundante. En cambio, se define un prototipo
 * base (Prototype GoF) con los atributos comunes y se clonan N instancias, variando
 * solo el id y el destino por pedido.
 *
 * Flujo por pedido: clone() → personalizar id/destino → CadenaValidadores → UoW.
 * El commit es en lote: todos los pedidos válidos se persisten en una sola transacción.
 */
public class ServicioImportacion {

    private final CadenaValidadores cadena;
    private final UnitOfWork        uow;
    private final Logger            log = Logger.obtenerInstancia();

    public ServicioImportacion(CadenaValidadores cadena, UnitOfWork uow) {
        this.cadena = cadena;
        this.uow    = uow;
    }

    /**
     * Importa un lote de pedidos clonando el prototipo base para cada orden.
     *
     * @param prototipo    Envío plantilla: lleva origen, empresa, peso, prioridad, costo.
     * @param pedidos      Lista de órdenes e-commerce (id + destino).
     * @param cobro        Cobro template que acredita el pago en la plataforma externa.
     * @return             Ids de los envíos persistidos exitosamente.
     */
    public List<String> importarPedidos(Envio prototipo, List<PedidoEcommerce> pedidos, Cobro cobro) {
        List<String> importados = new ArrayList<>();
        int rechazados          = 0;

        for (PedidoEcommerce pedido : pedidos) {
            // Prototype (GoF): clonar plantilla en lugar de construir desde cero.
            // Cada clon es independiente: modificar destino/id no afecta al prototipo.
            Envio clon = prototipo.clone();
            clon.setId(pedido.getIdPedido());
            clon.setDestino(pedido.getDestino());

            ContextoValidacion ctx = new ContextoValidacion(clon, cobro);
            if (!cadena.validar(ctx)) {
                log.log("Importacion CU-01: pedido " + pedido.getIdPedido()
                        + " → " + pedido.getDestino() + " rechazado por validacion");
                rechazados++;
                continue;
            }

            uow.registrarNuevo(clon);
            importados.add(clon.getId());
            log.log("Importacion CU-01: pedido " + pedido.getIdPedido() + " → " + pedido.getDestino() + " validado");
        }

        if (!importados.isEmpty()) {
            boolean ok = uow.commit();
            if (!ok) {
                log.log("Importacion CU-01: commit fallido — lote descartado (" + importados.size() + " pedidos)");
                return new ArrayList<>();
            }
        }

        log.log("Importacion CU-01: lote finalizado — "
                + importados.size() + " importados, " + rechazados + " rechazados");
        return importados;
    }
}
