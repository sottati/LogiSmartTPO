package com.logismart.dominio.usuario;

public interface IPermisos {
    boolean puedeCrearEnvio();
    boolean puedeAsignarRuta();
    boolean puedeVerReportes();
    boolean puedeGestionarFlota();
    boolean puedeAdministrarEmpresas();
    boolean puedeRegistrarEntregas();
    boolean puedeConsultarUbicacion();
    default String getEmpresaId() { return null; }
}
