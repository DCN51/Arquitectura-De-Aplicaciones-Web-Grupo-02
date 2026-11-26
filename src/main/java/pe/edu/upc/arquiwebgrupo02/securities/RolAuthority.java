package pe.edu.upc.arquiwebgrupo02.securities;

import java.util.Locale;

public final class RolAuthority {
    private static final String PREFIJO = "ROLE_";

    private RolAuthority() {
    }

    public static String normalizar(String rol) {
        String nombre = rol.trim().toUpperCase(Locale.ROOT);
        return nombre.startsWith(PREFIJO) ? nombre : PREFIJO + nombre;
    }
}
