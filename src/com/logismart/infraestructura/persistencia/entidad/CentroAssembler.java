package com.logismart.infraestructura.persistencia.entidad;

/**
 * Assembler que proyecta un CentroDistribucionEntity del dominio Composite
 * a la entidad de persistencia plana.
 *
 * Mitigacion de desfasaje (Hito 13): el Composite es la unica fuente de
 * verdad de capacidad/ocupacion. Este assembler genera la proyeccion snapshot
 * al momento de guardar — la entidad de persistencia nunca se edita por su cuenta.
 * Por construccion, el snapshot siempre refleja el estado actual del dominio.
 */
public class CentroAssembler {

    private CentroAssembler() {}

    /**
     * Proyecta el CentroDistribucionEntity Composite (abstracto, jerarquico) a la
     * entidad de persistencia plana (fila de tabla).
     * Lee obtenerCapacidad() y obtenerOcupacion() del Composite para calcular
     * los valores actuales.
     *
     * @param id  identificador unico para la fila de persistencia
     * @param c   instancia concreta del Composite
     * @return    entidad de persistencia lista para ser mapeada
     */
    public static CentroDistribucionEntity aPersistencia(
            String id,
            com.logismart.infraestructura.composite.centro.CentroDistribucionComposite c) {
        return new CentroDistribucionEntity(
                id,
                c.getNombre(),
                c.getUbicacion(),
                c.getCodigo(),
                c.obtenerCapacidad(),
                c.obtenerOcupacion()
        );
    }
}
