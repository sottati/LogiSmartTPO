package com.logismart.infraestructura.proxy.envio;

import com.logismart.dominio.Envio;
import java.util.List;

public interface RepositorioEnvios {
    Envio obtenerEnvio(String id);
    List<Envio> obtenerEnvios();
    void guardarEnvio(Envio envio);
    void eliminarEnvio(String id);
}
