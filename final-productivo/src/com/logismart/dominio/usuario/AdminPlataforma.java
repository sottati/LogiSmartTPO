package com.logismart.dominio.usuario;

public class AdminPlataforma extends Usuario implements IPermisos {
    private static final Rol ROL = Rol.ADMIN_PLATAFORMA;

    public AdminPlataforma(String id, String username, String email,
                           String passwordHash, String estado) {
        super(id, username, email, passwordHash, ROL, estado);
    }

    @Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
    @Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
    @Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
    @Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
    @Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
    @Override public boolean puedeRegistrarEntregas()   { return ROL.puedeRegistrarEntregas(); }
    @Override public boolean puedeConsultarUbicacion()  { return ROL.puedeConsultarUbicacion(); }
}
