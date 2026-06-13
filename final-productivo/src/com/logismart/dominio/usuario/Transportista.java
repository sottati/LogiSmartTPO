package com.logismart.dominio.usuario;

import com.logismart.dominio.vehiculo.Vehiculo;

public class Transportista extends Usuario implements IPermisos {
    private static final Rol ROL = Rol.TRANSPORTISTA;

    private String  licencia;
    private boolean disponible;
    private Vehiculo vehiculoAsignado;

    public Transportista(String id, String username, String email,
                         String passwordHash, String estado,
                         String licencia) {
        super(id, username, email, passwordHash, ROL, estado);
        this.licencia   = licencia;
        this.disponible = true;
    }

    public void iniciarRecorrido() { disponible = false; }
    public void liberarse()        { disponible = true;  }

    public String   getLicencia()                      { return licencia; }
    public boolean  isDisponible()                     { return disponible; }
    public Vehiculo getVehiculoAsignado()              { return vehiculoAsignado; }
    public void     setVehiculoAsignado(Vehiculo v)    { this.vehiculoAsignado = v; }

    @Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
    @Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
    @Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
    @Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
    @Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
    @Override public boolean puedeRegistrarEntregas()   { return ROL.puedeRegistrarEntregas(); }
    @Override public boolean puedeConsultarUbicacion()  { return ROL.puedeConsultarUbicacion(); }
}
