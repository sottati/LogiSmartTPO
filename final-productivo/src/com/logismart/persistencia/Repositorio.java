package com.logismart.persistencia;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T> {
    void        guardar(T entidad);
    Optional<T> obtener(String id);
    List<T>     obtenerTodos();
    void        eliminar(String id);
}
