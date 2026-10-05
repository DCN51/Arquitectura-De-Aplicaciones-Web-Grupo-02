package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityCompletionDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IDiagnosticoClinicoRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRecommendedActivityService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private static final String PENDIENTE = "PENDIENTE";
    private static final String COMPLETADA = "COMPLETADA";

    private final IRecommendedActivityService recommendedActivityService;
    private final IUsuarioRepository userRepository;
    private final IDiagnosticoClinicoRepository diagnosticoClinicoRepository;

    public ActivityController(
            IRecommendedActivityService recommendedActivityService,
            IUsuarioRepository userRepository,
            IDiagnosticoClinicoRepository diagnosticoClinicoRepository) {
        this.recommendedActivityService = recommendedActivityService;
        this.userRepository = userRepository;
        this.diagnosticoClinicoRepository = diagnosticoClinicoRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<RecommendedActivityDTO> create(@Valid @RequestBody RecommendedActivityDTO request) {
        RecommendedActivity recommendedActivity = new RecommendedActivity();
        apply(request, recommendedActivity);
        recommendedActivity.setStatus(PENDIENTE);
        recommendedActivity.setAssignedDate(request.getAssignedDate() == null ? LocalDate.now() : request.getAssignedDate());
        recommendedActivity.setCompletedDate(null);
        recommendedActivity.setFeedback(null);
        RecommendedActivity createdActivity = recommendedActivityService.create(recommendedActivity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(createdActivity));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('PACIENTE', 'ROLE_PACIENTE')")
    public List<RecommendedActivityDTO> listForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Authentication authentication) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha desde no puede ser mayor que la fecha hasta");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (!isPaciente(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo un paciente puede consultar sus actividades");
        }
        requireOwner(authentication, user);

        String estado = status == null || status.isBlank() ? null : status.trim();
        List<RecommendedActivity> activities = recommendedActivityService.buscarPorPaciente(userId, estado, from, to);
        return activities.stream().map(this::toDTO).toList();
    }

    @PutMapping("/{activityId}/complete")
    @PreAuthorize("hasAnyAuthority('PACIENTE', 'ROLE_PACIENTE')")
    public RecommendedActivityDTO complete(
            @PathVariable Integer activityId,
            @Valid @RequestBody RecommendedActivityCompletionDTO request,
            Authentication authentication) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        requireOwner(authentication, recommendedActivity.getUser());
        if (!isPendiente(recommendedActivity.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be completed");
        }
        recommendedActivity.setStatus(COMPLETADA);
        recommendedActivity.setCompletedDate(LocalDate.now());
        recommendedActivity.setFeedback(request.getFeedback() == null ? null : request.getFeedback().trim());
        return toDTO(recommendedActivityService.update(recommendedActivity));
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO', 'ROLE_PSICOLOGO')")
    public RecommendedActivityDTO update(@PathVariable Integer activityId, @Valid @RequestBody RecommendedActivityDTO request) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        String estado = recommendedActivity.getStatus();
        LocalDate fechaCompletado = recommendedActivity.getCompletedDate();
        String feedback = recommendedActivity.getFeedback();
        apply(request, recommendedActivity);
        recommendedActivity.setStatus(estado);
        recommendedActivity.setCompletedDate(fechaCompletado);
        recommendedActivity.setFeedback(feedback);
        return toDTO(recommendedActivityService.update(recommendedActivity));
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<Void> delete(@PathVariable Integer activityId) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        if (!isPendiente(recommendedActivity.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be deleted");
        }
        recommendedActivityService.deleteById(activityId);
        return ResponseEntity.noContent().build();
    }

    private void apply(RecommendedActivityDTO request, RecommendedActivity recommendedActivity) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        if (!isPaciente(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no es un paciente");
        }
        DiagnosticoClinico diagnostico = diagnosticoClinicoRepository.findById(request.getDiagnosisId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El diagnóstico indicado no existe"));
        if (!diagnostico.getUsuario().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El diagnóstico no pertenece al paciente indicado");
        }
        recommendedActivity.setUser(user);
        recommendedActivity.setDiagnosticoClinico(diagnostico);
        recommendedActivity.setTitle(request.getTitle().trim());
        recommendedActivity.setDescription(request.getDescription().trim());
        recommendedActivity.setType(request.getType().trim());
        if (request.getAssignedDate() != null) {
            recommendedActivity.setAssignedDate(request.getAssignedDate());
        }
    }

    private RecommendedActivity findActivity(Integer activityId) {
        return recommendedActivityService.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));
    }

    private boolean isPaciente(Users user) {
        String rol = user.getRole().getRol().toUpperCase(Locale.ROOT);
        return rol.equals("PACIENTE") || rol.equals("ROLE_PACIENTE");
    }

    private void requireOwner(Authentication authentication, Users user) {
        if (authentication == null || !user.getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this activity");
        }
    }

    private boolean isPendiente(String estado) {
        return PENDIENTE.equalsIgnoreCase(estado) || "PENDING".equalsIgnoreCase(estado);
    }

    private RecommendedActivityDTO toDTO(RecommendedActivity recommendedActivity) {
        RecommendedActivityDTO response = new RecommendedActivityDTO();
        response.setRecommendedActivityId(recommendedActivity.getRecommendedActivityId());
        response.setUserId(recommendedActivity.getUser().getId());
        response.setDiagnosisId(recommendedActivity.getDiagnosticoClinico().getDiagnosticoClinicoId());
        response.setTitle(recommendedActivity.getTitle());
        response.setDescription(recommendedActivity.getDescription());
        response.setType(recommendedActivity.getType());
        response.setAssignedDate(recommendedActivity.getAssignedDate());
        response.setCompletedDate(recommendedActivity.getCompletedDate());
        response.setStatus(recommendedActivity.getStatus());
        response.setFeedback(recommendedActivity.getFeedback());
        return response;
    }
}