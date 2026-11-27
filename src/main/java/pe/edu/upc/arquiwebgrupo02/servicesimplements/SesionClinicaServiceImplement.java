package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IDiagnosticoClinicoRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.ISesionClinicaRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISesionClinicaService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISuscripcionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SesionClinicaServiceImplement implements ISesionClinicaService {
    private final ISesionClinicaRepository scR;
    private final IDiagnosticoClinicoRepository dcR;
    private final ISuscripcionService sS;

    public SesionClinicaServiceImplement(ISesionClinicaRepository scR, IDiagnosticoClinicoRepository dcR,
                                         ISuscripcionService sS) {
        this.scR = scR;
        this.dcR = dcR;
        this.sS = sS;
    }

    @Override
    public SesionClinica iniciar(Users paciente) {
        if (scR.buscarEnCursoPorUsuario(paciente.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una sesión en curso");
        }

        CatalogoPlan plan = sS.planActivoDe(paciente.getId());
        int limite = plan.getLimiteSesionesMes();
        if (limite != -1 && scR.contarSesionesDelMes(paciente.getId()) >= limite) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alcanzaste el límite de sesiones de tu plan este mes");
        }

        SesionClinica s = new SesionClinica();
        s.setUsuario(paciente);
        s.setFechaHoraInicio(LocalDateTime.now());
        s.setFechaHoraFin(null);
        s.setEstado("en_curso");
        s.setNivelUrgencia("bajo");
        s.setRequiereDerivacion(false);
        s.setEmocionDetectada(null);
        s.setResumenIa(null);

        return scR.save(s);
    }

    @Override
    public SesionClinica finalizar(Long sesionClinicaId, SesionClinica datos, Users paciente) {
        SesionClinica actual = buscarDelPaciente(sesionClinicaId, paciente);

        if (!"en_curso".equals(actual.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sesión ya fue finalizada");
        }

        String urgencia = datos.getNivelUrgencia() == null ? actual.getNivelUrgencia() : datos.getNivelUrgencia().toLowerCase();
        if (!List.of("bajo", "medio", "alto", "critico").contains(urgencia)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel de urgencia inválido (bajo, medio, alto, critico)");
        }

        actual.setFechaHoraFin(LocalDateTime.now());
        actual.setEmocionDetectada(datos.getEmocionDetectada());
        actual.setResumenIa(datos.getResumenIa());
        actual.setNivelUrgencia(urgencia);

        boolean derivar = Boolean.TRUE.equals(datos.getRequiereDerivacion())
                || urgencia.equals("alto") || urgencia.equals("critico");
        actual.setRequiereDerivacion(derivar);

        actual.setEstado("finalizada");
        return scR.save(actual);
    }

    @Override
    public Optional<SesionClinica> listId(Long sesionClinicaId) {
        return scR.findById(sesionClinicaId);
    }

    @Override
    public SesionClinica buscarDelPaciente(Long sesionClinicaId, Users paciente) {
        SesionClinica s = scR.findById(sesionClinicaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada"));
        if (!s.getUsuario().getId().equals(paciente.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a esta sesión");
        }
        return s;
    }

    @Override
    public List<SesionClinica> historialPorUsuario(Long usuarioId) {
        return scR.buscarHistorialPorUsuario(usuarioId);
    }

    @Override
    public List<SesionClinica> listarDerivacionesDePsicologo(Users psicologo) {
        return scR.buscarDerivacionesDePsicologo(psicologo.getId());
    }

    @Override
    public void delete(Long sesionClinicaId, Users paciente) {
        SesionClinica s = buscarDelPaciente(sesionClinicaId, paciente);
        if ("en_curso".equals(s.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar una sesión en curso");
        }
        if (dcR.buscarPorSesion(sesionClinicaId) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar una sesión que ya tiene un diagnóstico");
        }
        scR.deleteById(sesionClinicaId);
    }
}
