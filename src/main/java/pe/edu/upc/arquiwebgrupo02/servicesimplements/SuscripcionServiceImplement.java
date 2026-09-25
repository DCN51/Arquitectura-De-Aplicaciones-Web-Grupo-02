package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;
import pe.edu.upc.arquiwebgrupo02.repositories.ICatalogoPlanRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.ISuscripcionRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISuscripcionService;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuscripcionServiceImplement implements ISuscripcionService {
    private final ISuscripcionRepository sR;
    private final IUsuarioRepository uR;
    private final ICatalogoPlanRepository cpR;

    public SuscripcionServiceImplement(ISuscripcionRepository sR, IUsuarioRepository uR, ICatalogoPlanRepository cpR) {
        this.sR = sR;
        this.uR = uR;
        this.cpR = cpR;
    }


    // US05 - Contratar plan
    @Override
    public void insert(Suscripcion s) {
        // Si ya tiene una activa, no puede contratar otra
        if (sR.buscarActivaPorUsuario(s.getUsuario().getUsuarioId()) != null) {
            throw new RuntimeException("Ya tienes una suscripción activa");
        }
        guardarNueva(s);
    }

    @Override
    public List<Suscripcion> list() {
        return sR.findAll();
    }

    // US06 - Cambiar plan
    @Override
    @Transactional
    public void cambiarPlan(Suscripcion s) {
        Suscripcion actual = sR.buscarActivaPorUsuario(s.getUsuario().getUsuarioId());

        // Para cambiar, primero debe tener una activa
        if (actual == null) {
            throw new RuntimeException("No tienes una suscripción activa");
        }
        // No tiene sentido cambiar al mismo plan
        if (actual.getCatalogoPlan().getCatalogoPlanId().equals(s.getCatalogoPlan().getCatalogoPlanId())) {
            throw new RuntimeException("Ya estás suscrito a este plan");
        }

        // Se cancela la anterior y se guarda la nueva
        actual.setEstadoSuscripcion("cancelada");
        sR.save(actual);
        guardarNueva(s);
    }

    // US07 - Cancelar suscripcion
    @Override
    public void cancelar(Long usuarioId) {
        Suscripcion s = sR.buscarActivaPorUsuario(usuarioId);
        if (s == null) {
            throw new RuntimeException("No tienes una suscripción activa");
        }
        s.setEstadoSuscripcion("cancelada");
        sR.save(s);
    }

    // US08 - Consultar historial
    @Override
    public List<Suscripcion> historialPorUsuario(Long usuarioId) {
        return sR.buscarHistorialPorUsuario(usuarioId);
    }

    // Completa los datos de una suscripcion nueva y la guarda (lo usan US05 y US06)
    private void guardarNueva(Suscripcion s) {
        // 1. Buscar el plan elegido y verificar que se pueda contratar
        CatalogoPlan plan = cpR.findById(s.getCatalogoPlan().getCatalogoPlanId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        if (!plan.isActivoCatalogoPlan()) {
            throw new RuntimeException("El plan no está disponible");
        }
        // 2. Llenar lo que decide el sistema, no el cliente
        s.setCatalogoPlan(plan);
        s.setFechaInicio(LocalDate.now());
        s.setFechaFin(LocalDate.now().plusMonths(1));
        s.setEstadoSuscripcion("activa");

        // 3. Si el plan es gratis, no se guardan datos de pago
        if (plan.getPrecioMensualCatalogoPlan() == 0) {
            s.setMetodoPagoSuscripcion(null);
            s.setReferenciaPagoSuscripcion(null);
        }

        sR.save(s);
    }
}
