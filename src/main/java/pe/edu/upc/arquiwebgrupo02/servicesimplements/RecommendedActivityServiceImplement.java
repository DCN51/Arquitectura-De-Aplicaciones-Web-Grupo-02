package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IDiagnosticoClinicoRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IRecommendedActivityRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRecommendedActivityService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

@Service
public class RecommendedActivityServiceImplement implements IRecommendedActivityService {
    private static final String PENDIENTE = "PENDIENTE";
    private static final String COMPLETADA = "COMPLETADA";

    private final IRecommendedActivityRepository recommendedActivityRepository;
    private final IDiagnosticoClinicoRepository diagnosticoClinicoRepository;
    private final IUserService uS;

    public RecommendedActivityServiceImplement(IRecommendedActivityRepository recommendedActivityRepository,
                                               IDiagnosticoClinicoRepository diagnosticoClinicoRepository,
                                               IUserService uS) {
        this.recommendedActivityRepository = recommendedActivityRepository;
        this.diagnosticoClinicoRepository = diagnosticoClinicoRepository;
        this.uS = uS;
    }

    @Override
    @Transactional
    public RecommendedActivity crear(RecommendedActivityDTO datos, Users psicologo) {
        RecommendedActivity actividad = new RecommendedActivity();
        aplicar(datos, actividad, psicologo);
        actividad.setStatus(PENDIENTE);
        actividad.setAssignedDate(datos.getAssignedDate() == null ? LocalDate.now() : datos.getAssignedDate());
        actividad.setCompletedDate(null);
        actividad.setFeedback(null);
        return recommendedActivityRepository.save(actividad);
    }

    @Override
    @Transactional
    public RecommendedActivity actualizar(Integer recommendedActivityId, RecommendedActivityDTO datos, Users psicologo) {
        RecommendedActivity actividad = buscar(recommendedActivityId);
        validarPacienteDelPsicologo(actividad.getUser(), psicologo);
        String estado = actividad.getStatus();
        LocalDate fechaCompletado = actividad.getCompletedDate();
        String feedback = actividad.getFeedback();
        aplicar(datos, actividad, psicologo);
        actividad.setStatus(estado);
        actividad.setCompletedDate(fechaCompletado);
        actividad.setFeedback(feedback);
        return recommendedActivityRepository.save(actividad);
    }

    @Override
    @Transactional
    public void eliminar(Integer recommendedActivityId, Users psicologo) {
        RecommendedActivity actividad = buscar(recommendedActivityId);
        validarPacienteDelPsicologo(actividad.getUser(), psicologo);
        if (!esPendiente(actividad.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se pueden eliminar actividades pendientes");
        }
        recommendedActivityRepository.deleteById(recommendedActivityId);
    }

    @Override
    @Transactional
    public RecommendedActivity completar(Integer recommendedActivityId, String feedback, Users paciente) {
        RecommendedActivity actividad = buscar(recommendedActivityId);
        requireOwner(paciente, actividad);
        if (!esPendiente(actividad.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se pueden completar actividades pendientes");
        }
        actividad.setStatus(COMPLETADA);
        actividad.setCompletedDate(LocalDate.now());
        actividad.setFeedback(feedback == null || feedback.isBlank() ? null : feedback.trim());
        return recommendedActivityRepository.save(actividad);
    }

    @Override
    public List<RecommendedActivity> listarDelPaciente(Users paciente, String status, LocalDate from, LocalDate to) {
        return buscarPorPaciente(paciente.getId(), status, from, to);
    }

    @Override
    public List<RecommendedActivity> listarParaPsicologo(Long pacienteId, String status, LocalDate from, LocalDate to,
                                                         Users psicologo) {
        Users paciente = uS.buscarPorId(pacienteId);
        if (!uS.esPaciente(paciente)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no es un paciente");
        }
        validarPacienteDelPsicologo(paciente, psicologo);
        return buscarPorPaciente(pacienteId, status, from, to);
    }

    @Override
    public Optional<RecommendedActivity> findById(Integer recommendedActivityId) {
        return recommendedActivityRepository.findById(recommendedActivityId);
    }

    private List<RecommendedActivity> buscarPorPaciente(Long pacienteId, String status, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha desde no puede ser mayor que la fecha hasta");
        }
        String estado = status == null || status.isBlank() ? null : status.trim();
        return recommendedActivityRepository.buscarPorPaciente(pacienteId, estado, from, to);
    }

    private void aplicar(RecommendedActivityDTO datos, RecommendedActivity actividad, Users psicologo) {
        Users paciente = uS.buscarPorId(datos.getUserId());
        if (!uS.esPaciente(paciente)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no es un paciente");
        }
        validarPacienteDelPsicologo(paciente, psicologo);
        DiagnosticoClinico diagnostico = diagnosticoClinicoRepository.findById(datos.getDiagnosisId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El diagnóstico indicado no existe"));
        if (!diagnostico.getUsuario().getId().equals(paciente.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El diagnóstico no pertenece al paciente indicado");
        }
        actividad.setUser(paciente);
        actividad.setDiagnosticoClinico(diagnostico);
        actividad.setTitle(datos.getTitle().trim());
        actividad.setDescription(datos.getDescription().trim());
        actividad.setType(datos.getType().trim());
        if (datos.getAssignedDate() != null) {
            actividad.setAssignedDate(datos.getAssignedDate());
        }
    }

    private void validarPacienteDelPsicologo(Users paciente, Users psicologo) {
    }

    private void requireOwner(Users usuario, RecommendedActivity actividad) {
        if (usuario == null || !actividad.getUser().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta actividad no está asignada a ti");
        }
    }

    private RecommendedActivity buscar(Integer recommendedActivityId) {
        return recommendedActivityRepository.findById(recommendedActivityId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la actividad con el id: " + recommendedActivityId));
    }

    private boolean esPendiente(String estado) {
        return PENDIENTE.equalsIgnoreCase(estado) || "PENDING".equalsIgnoreCase(estado);
    }
}
