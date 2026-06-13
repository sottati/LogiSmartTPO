package com.logismart.dominio.usuario;

/**
 * Matriz de permisos por tipo de rol (GRASP Polymorphism).
 * El Controller consulta puedeXxx() en lugar de encadenar instanceof o switch.
 */
public enum Rol implements IPermisos {
    //                           crear   asignar reportes flota   empresas entregas ubicacion
    OPERADOR         (true,  true,  true,  true,  false, false, true),
    ADMIN_EMPRESA    (true,  false, true,  false, false, false, true),
    ADMIN_PLATAFORMA (true,  true,  true,  true,  true,  true,  true),
    CLIENTE          (false, false, false, false, false, false, true),
    TRANSPORTISTA    (false, false, false, false, false, true,  true);

    private final boolean crearEnvio;
    private final boolean asignarRuta;
    private final boolean verReportes;
    private final boolean gestionarFlota;
    private final boolean administrarEmpresas;
    private final boolean registrarEntregas;
    private final boolean consultarUbicacion;

    Rol(boolean crearEnvio, boolean asignarRuta, boolean verReportes,
        boolean gestionarFlota, boolean administrarEmpresas,
        boolean registrarEntregas, boolean consultarUbicacion) {
        this.crearEnvio          = crearEnvio;
        this.asignarRuta         = asignarRuta;
        this.verReportes         = verReportes;
        this.gestionarFlota      = gestionarFlota;
        this.administrarEmpresas = administrarEmpresas;
        this.registrarEntregas   = registrarEntregas;
        this.consultarUbicacion  = consultarUbicacion;
    }

    @Override public boolean puedeCrearEnvio()          { return crearEnvio; }
    @Override public boolean puedeAsignarRuta()         { return asignarRuta; }
    @Override public boolean puedeVerReportes()         { return verReportes; }
    @Override public boolean puedeGestionarFlota()      { return gestionarFlota; }
    @Override public boolean puedeAdministrarEmpresas() { return administrarEmpresas; }
    @Override public boolean puedeRegistrarEntregas()   { return registrarEntregas; }
    @Override public boolean puedeConsultarUbicacion()  { return consultarUbicacion; }
}
