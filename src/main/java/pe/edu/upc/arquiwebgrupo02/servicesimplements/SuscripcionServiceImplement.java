package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.ICatalogoPlanRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.ISuscripcionRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISuscripcionService;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuscripcionServiceImplement implements ISuscripcionService {
    private final ISuscripcionRepository sR;
    private final ICatalogoPlanRepository cpR;

    public SuscripcionServiceImplement(ISuscripcionRepository sR, ICatalogoPlanRepository cpR) {
        this.sR = sR;
        this.cpR = cpR;
    }

    // HU015 - Contratar plan
    @Override
    @Transactional
    public void insert(Suscripcion s) {
        Suscripcion actual = sR.buscarActivaPorUsuario(s.getUsuario().getId());

        if (actual != null) {
            // Si ya paga un plan, no puede contratar otro (para eso es cambiar plan)
            if (actual.getCatalogoPlan().getPrecioMensualCatalogoPlan() > 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una suscripción activa");
            }
            // Si tiene el free y elige el free otra vez, no tiene sentido
            if (actual.getCatalogoPlan().getCatalogoPlanId().equals(s.getCatalogoPlan().getCatalogoPlanId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya estás suscrito a este plan");
            }
            // Si tiene el free, se cancela y se contrata el nuevo
            actual.setEstadoSuscripcion("cancelada");
            sR.save(actual);
        }
        guardarNueva(s);
    }

    // US06 - Cambiar plan
    @Override
    @Transactional
    public void cambiarPlan(Suscripcion s) {
        Suscripcion actual = sR.buscarActivaPorUsuario(s.getUsuario().getId());

        // Para cambiar, primero debe tener una activa
        if (actual == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tienes una suscripción activa");
        }
        // No tiene sentido cambiar al mismo plan
        if (actual.getCatalogoPlan().getCatalogoPlanId().equals(s.getCatalogoPlan().getCatalogoPlanId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya estás suscrito a este plan");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tienes una suscripción activa");
        }
        s.setEstadoSuscripcion("cancelada");
        sR.save(s);
    }

    // US08 - Consultar historial
    @Override
    public List<Suscripcion> historialPorUsuario(Long usuarioId) {
        return sR.buscarHistorialPorUsuario(usuarioId);
    }

    // US09 - Consultar suscripciones por vencer
    @Override
    public List<Suscripcion> porVencer(int dias) {
        if (dias < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de días no puede ser negativo");
        }
        LocalDate hoy = LocalDate.now();
        return sR.buscarPorVencer(hoy, hoy.plusDays(dias));
    }

    // US10 - Contar suscriptores activos por plan
    @Override
    public List<Object[]> contarSuscriptoresActivosPorPlan() {
        return sR.contarSuscriptoresActivosPorPlan();
    }

    // NUEVO - Asigna el plan free a un paciente recien registrado
    @Override
    public void asignarPlanFree(Users usuario) {
        CatalogoPlan free = cpR.buscarPlanFree();
        if (free == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No existe un plan 'free' activo en el catálogo");
        }
        Suscripcion s = new Suscripcion();
        s.setUsuario(usuario);
        s.setCatalogoPlan(free);
        guardarNueva(s);
    }

    // NUEVO - Plan vigente del usuario. Lo usan sesiones, diagnosticos y actividades
    // para revisar los beneficios. Si no tiene suscripcion activa, se aplica el plan free.
    @Override
    public CatalogoPlan planActivoDe(Long usuarioId) {
        Suscripcion s = sR.buscarActivaPorUsuario(usuarioId);
        if (s != null) {
            return s.getCatalogoPlan();
        }
        CatalogoPlan free = cpR.buscarPlanFree();
        if (free == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No existe un plan 'free' activo en el catálogo");
        }
        return free;
    }

    // Completa los datos de una suscripcion nueva y la guarda (lo usan US05, US06 y el plan free)
    private void guardarNueva(Suscripcion s) {
        // 1. Buscar el plan elegido y verificar que se pueda contratar
        CatalogoPlan plan = cpR.findById(s.getCatalogoPlan().getCatalogoPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan no encontrado"));
        if (!plan.isActivoCatalogoPlan()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan no está disponible");
        }

        // 2. Llenar lo que decide el sistema, no el cliente
        s.setCatalogoPlan(plan);
        s.setFechaInicio(LocalDate.now());
        s.setEstadoSuscripcion("activa");

        if (plan.getPrecioMensualCatalogoPlan() == 0) {
            // 3a. Plan gratis: no vence y no guarda datos de pago
            s.setFechaFin(null);
            s.setMetodoPagoSuscripcion(null);
            s.setReferenciaPagoSuscripcion(null);
        } else {
            // 3b. Plan pagado: vence en un mes
            s.setFechaFin(LocalDate.now().plusMonths(1));
        }

        sR.save(s);
    }
}