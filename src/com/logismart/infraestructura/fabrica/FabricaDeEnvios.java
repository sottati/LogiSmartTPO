package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.Empresa;
import com.logismart.dominio.Envio;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory Method para crear envios por tipo sin exponer subclases al cliente.
 */
public final class FabricaDeEnvios {

    private FabricaDeEnvios() {
    }

    public static Envio crearEnvio(TipoEnvio tipo, Empresa empresa, LocalDateTime fechaProgramada) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de envio invalido");
        }
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa obligatoria");
        }
        if (fechaProgramada == null) {
            throw new IllegalArgumentException("Fecha programada obligatoria");
        }

        String id = UUID.randomUUID().toString();

        return switch (tipo) {
            case EXPRESS -> new EnvioExpress(id, empresa, fechaProgramada);
            case STANDARD -> new EnvioStandard(id, empresa, fechaProgramada);
            case ECONOMICO -> new EnvioEconomico(id, empresa, fechaProgramada);
        };
    }

    public static TipoEnvio obtenerTipo(Envio envio) {
        if (envio instanceof EnvioTipado) {
            EnvioTipado envioTipado = (EnvioTipado) envio;
            return envioTipado.getTipoEnvio();
        }
        return TipoEnvio.desdePrioridad(envio.getPrioridad());
    }
}

interface EnvioTipado {
    TipoEnvio getTipoEnvio();

    int getDiasEntregaEstimados();

    double getMultiplicadorCosto();
}

abstract class EnvioCreacionalBase extends Envio implements EnvioTipado {

    private final TipoEnvio tipoEnvio;

    protected EnvioCreacionalBase(String id, Empresa empresa, LocalDateTime fechaProgramada, TipoEnvio tipoEnvio) {
        super(id, empresa, tipoEnvio.getPrioridadAsociada(), fechaProgramada);
        this.tipoEnvio = tipoEnvio;
    }

    @Override
    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    @Override
    public int getDiasEntregaEstimados() {
        return tipoEnvio.getDiasEntregaEstimados();
    }

    @Override
    public double getMultiplicadorCosto() {
        return tipoEnvio.getMultiplicadorCosto();
    }
}

final class EnvioExpress extends EnvioCreacionalBase {
    EnvioExpress(String id, Empresa empresa, LocalDateTime fechaProgramada) {
        super(id, empresa, fechaProgramada, TipoEnvio.EXPRESS);
    }
}

final class EnvioStandard extends EnvioCreacionalBase {
    EnvioStandard(String id, Empresa empresa, LocalDateTime fechaProgramada) {
        super(id, empresa, fechaProgramada, TipoEnvio.STANDARD);
    }
}

final class EnvioEconomico extends EnvioCreacionalBase {
    EnvioEconomico(String id, Empresa empresa, LocalDateTime fechaProgramada) {
        super(id, empresa, fechaProgramada, TipoEnvio.ECONOMICO);
    }
}
