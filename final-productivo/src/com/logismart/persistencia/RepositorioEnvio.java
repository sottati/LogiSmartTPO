package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import java.util.List;

public interface RepositorioEnvio extends Repositorio<Envio> {
    List<Envio> buscarPorEstado(String estado);
    List<Envio> buscarPorEmpresa(String empresaId);
}
