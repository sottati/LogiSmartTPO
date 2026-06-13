package tests.persistencia;

import com.logismart.dominio.envio.Envio;
import com.logismart.persistencia.ClienteLazyProxy;
import com.logismart.persistencia.EnvioMapperMemoria;
import com.logismart.persistencia.ProxyRepositorioEnvio;
import com.logismart.persistencia.RepositorioEnvioMemoria;
import com.logismart.persistencia.UnitOfWork;

public class TestsPersistencia {

    private static int total = 0;
    private static int ok    = 0;

    public static void main(String[] args) {
        testRepositorioDataMapper();
        testProxy();
        testLazyLoad();
        testUnitOfWork();

        System.out.println("\n========================================");
        System.out.println(total + " casos ejecutados, " + ok + " OK");
        System.out.println("========================================");
    }

    // ── Repositorio + Data Mapper ─────────────────────────────────────────────

    static void testRepositorioDataMapper() {
        System.out.println("\n--- Repositorio + Data Mapper ---");
        RepositorioEnvioMemoria repo   = new RepositorioEnvioMemoria();
        EnvioMapperMemoria      mapper = new EnvioMapperMemoria();

        Envio e1 = new Envio.EnvioBuilder("ENV-01","BsAs","Cordoba").peso(5.0).build();
        mapper.insertar(e1);
        repo.guardar(e1);
        assertar("Repositorio: guardar y obtener", repo.obtener("ENV-01").isPresent());
        assertar("DataMapper: insertar registra fila", mapper.existe("ENV-01"));

        e1.getClass(); // acceso sin modificar estado
        // Advance to EN_TRANSITO via validar()
        e1.validar();
        mapper.actualizar(e1);
        assertar("DataMapper: actualizar refleja nuevo estado EN_TRANSITO",
                "EN_TRANSITO".equals(mapper.obtenerEstado("ENV-01")));

        repo.eliminar("ENV-01");
        mapper.eliminar("ENV-01");
        assertar("Repositorio: eliminar quita el envio", repo.obtener("ENV-01").isEmpty());
        assertar("DataMapper: eliminar quita la fila", !mapper.existe("ENV-01"));
    }

    // ── Proxy (cache TTL + invalidacion) ─────────────────────────────────────

    static void testProxy() {
        System.out.println("\n--- Proxy ---");
        RepositorioEnvioMemoria real  = new RepositorioEnvioMemoria();
        ProxyRepositorioEnvio   proxy = new ProxyRepositorioEnvio(real);

        Envio e = new Envio.EnvioBuilder("ENV-P01","A","B").peso(2.0).build();

        // MISS: primera solicitud va al repo real
        assertar("Proxy: MISS inicial (envio ausente)", proxy.obtener("ENV-P01").isEmpty());

        // guardar vía proxy rellena cache
        proxy.guardar(e);
        assertar("Proxy: cache tiene 1 entrada tras guardar", proxy.tamanioCache() == 1);

        // HIT: segunda solicitud usa cache (mismo objeto)
        assertar("Proxy: HIT retorna el mismo envio", proxy.obtener("ENV-P01").isPresent());

        // escritura invalida cacheTotal pero deja cacheId intacto
        proxy.eliminar("ENV-P01");
        assertar("Proxy: eliminar → cache queda vacio", proxy.tamanioCache() == 0);
    }

    // ── Lazy Load ─────────────────────────────────────────────────────────────

    static void testLazyLoad() {
        System.out.println("\n--- Lazy Load ---");
        ClienteLazyProxy cliente = new ClienteLazyProxy("CL-001","juan","juan@t.com");

        assertar("LazyLoad: no cargado al instanciar", !cliente.estaCargado());

        // El primer acceso a un dato pesado dispara la carga
        int envios = cliente.getCantidadEnvios();
        assertar("LazyLoad: cargado tras primer acceso", cliente.estaCargado());

        // Carga fue una sola vez: segunda llamada no recarga
        int envios2 = cliente.getCantidadEnvios();
        assertar("LazyLoad: segunda llamada no recarga (mismo valor)", envios == envios2);

        // Categoria derivada correctamente
        String cat = cliente.getCategoriaFidelidad();
        boolean catOk = (envios >= 10 && "PREMIUM".equals(cat))
                     || (envios < 10  && "ESTANDAR".equals(cat));
        assertar("LazyLoad: categoria derivada segun cantidad de envios", catOk);
    }

    // ── Unit of Work ──────────────────────────────────────────────────────────

    static void testUnitOfWork() {
        System.out.println("\n--- Unit of Work ---");
        RepositorioEnvioMemoria base   = new RepositorioEnvioMemoria();
        EnvioMapperMemoria      mapper = new EnvioMapperMemoria();
        ProxyRepositorioEnvio   proxy  = new ProxyRepositorioEnvio(base);
        UnitOfWork              uow    = new UnitOfWork(proxy, mapper);

        Envio e1 = new Envio.EnvioBuilder("UOW-01","A","B").peso(1.0).build();
        Envio e2 = new Envio.EnvioBuilder("UOW-02","A","C").peso(2.0).build();

        // registrarNuevo + commit → ambos persistidos
        uow.registrarNuevo(e1);
        uow.registrarNuevo(e2);
        assertar("UoW: 2 pendientes antes del commit", uow.pendientes() == 2);

        boolean ok = uow.commit();
        assertar("UoW: commit retorna true",          ok);
        assertar("UoW: 0 pendientes tras commit",     uow.pendientes() == 0);
        assertar("UoW: envios en repositorio",        base.tamanio() == 2);

        // modificar + commit actualiza el mapper
        e1.validar(); // CONFIRMADO → EN_TRANSITO
        uow.registrarModificado(e1);
        uow.commit();
        assertar("UoW: commit modificado actualiza DataMapper",
                "EN_TRANSITO".equals(mapper.obtenerEstado("UOW-01")));
    }

    // ── Utilidad ──────────────────────────────────────────────────────────────

    static void assertar(String nombre, boolean condicion) {
        total++;
        if (condicion) { ok++; System.out.println("  [OK] " + nombre); }
        else           {       System.out.println("  [FALLO] " + nombre); }
    }
}
