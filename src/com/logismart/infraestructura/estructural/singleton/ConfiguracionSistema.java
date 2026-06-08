package com.logismart.infraestructura.estructural.singleton;

/**
 * Singleton ConfiguracionSistema - configuración global del sistema.
 * Patrón Singleton con double-checked locking (thread-safe).
 */
public final class ConfiguracionSistema {

    private static volatile ConfiguracionSistema instance;

    private final String empresa;
    private final String version;
    private String region;

    private ConfiguracionSistema() {
        this.empresa = "LogiSmart";
        this.version = "7.0";
        this.region  = "Argentina";
    }

    public static ConfiguracionSistema getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionSistema.class) {
                if (instance == null) {
                    instance = new ConfiguracionSistema();
                }
            }
        }
        return instance;
    }

    public String getEmpresa()  { return empresa; }
    public String getVersion()  { return version; }
    public String getRegion()   { return region; }
    public void setRegion(String region) { this.region = region; }

    @Override
    public String toString() {
        return empresa + " v" + version + " [" + region + "]";
    }
}
