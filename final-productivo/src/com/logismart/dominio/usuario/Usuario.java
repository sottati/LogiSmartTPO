package com.logismart.dominio.usuario;

public class Usuario {

    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private Rol    rol;
    private String estado;
    private String empresaId;

    public Usuario(String id, String username, String email,
                   String passwordHash, Rol rol, String estado) {
        this.id           = id;
        this.username     = username;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.rol          = rol;
        this.estado       = estado;
    }

    public boolean verificarPassword(String hash) {
        return hash != null && this.passwordHash.equals(hash);
    }

    public void cambiarPassword(String nuevoHash) {
        if (nuevoHash == null || nuevoHash.isBlank())
            throw new IllegalArgumentException("El hash de contraseña no puede estar vacío.");
        this.passwordHash = nuevoHash;
    }

    public IPermisos permisos() { return rol; }

    public String getId()       { return id; }
    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public Rol    getRol()      { return rol; }
    public String getEstado()   { return estado; }
    public void   setEmail(String email)      { this.email = email; }
    public void   setEstado(String estado)    { this.estado = estado; }
    public String getEmpresaId()              { return empresaId; }
    public void   setEmpresaId(String empId)  { this.empresaId = empId; }
}
