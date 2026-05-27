package com.logismart.dominio;

public enum Rol {
    OPERADOR         (true,  true,  true,  true,  false),
    ADMIN_EMPRESA    (true,  false, true,  false, false),
    ADMIN_PLATAFORMA (true,  true,  true,  true,  true),
    CLIENTE          (false, false, false, false, false),
    TRANSPORTISTA    (false, false, false, false, false);

    private final boolean crearEnvio;
    private final boolean asignarRuta;
    private final boolean verReportes;
    private final boolean gestionarFlota;
    private final boolean administrarEmpresas;

    Rol(boolean crearEnvio, boolean asignarRuta, boolean verReportes,
            boolean gestionarFlota, boolean administrarEmpresas) {
        this.crearEnvio = crearEnvio;
        this.asignarRuta = asignarRuta;
        this.verReportes = verReportes;
        this.gestionarFlota = gestionarFlota;
        this.administrarEmpresas = administrarEmpresas;
    }

    public boolean puedeCrearEnvio()          { return crearEnvio; }
    public boolean puedeAsignarRuta()         { return asignarRuta; }
    public boolean puedeVerReportes()         { return verReportes; }
    public boolean puedeGestionarFlota()      { return gestionarFlota; }
    public boolean puedeAdministrarEmpresas() { return administrarEmpresas; }
}
