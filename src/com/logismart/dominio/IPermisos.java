package com.logismart.dominio;

/**
 * GRASP Polymorphism: Define el contrato de permisos para todos los tipos de usuario.
 * Elimina la necesidad de condicionales instanceof dispersos por el sistema.
 * Cada subclase de Usuario implementa sus propias reglas de permiso.
 */
public interface IPermisos {

    boolean puedeCrearEnvio();

    boolean puedeAsignarRuta();

    boolean puedeVerReportes();

    boolean puedeGestionarFlota();

    boolean puedeAdministrarEmpresas();
}
