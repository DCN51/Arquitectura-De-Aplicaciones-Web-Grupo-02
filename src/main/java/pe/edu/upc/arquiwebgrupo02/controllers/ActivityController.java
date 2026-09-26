package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import pe.edu.upc.arquiwebgrupo02.dtos.ActivityCompletionDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.ActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.ActividadRecomendada;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IActividadRecomendadaService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final IActividadRecomendadaService activityService;
    private final IUsuarioRepository userRepository;

    public ActivityController(
            IActividadRecomendadaService activityService,
            IUsuarioRepository userRepository) {
        this.activityService = activityService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<ActivityDTO> create(@Valid @RequestBody ActivityDTO request) {
        ActividadRecomendada activity = new ActividadRecomendada();
        apply(request, activity);
        activity.setEstado("PENDING");
        activity.setFechaAsignacion(request.getAssignedDate() == null ? LocalDate.now() : request.getAssignedDate());
        activityService.insert(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(activity));
    }

    @GetMapping("/users/{userId}")
    public List<ActivityDTO> listForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            Authentication authentication) {
        requireOwnerOrPrivileged(authentication, userId);
        List<ActividadRecomendada> activities = status == null || status.isBlank()
                ? activityService.listByUserId(userId)
                : activityService.listByUserIdAndStatus(userId, status);
        return activities.stream().map(this::toDTO).toList();
    }

    @PutMapping("/{activityId}/complete")
    public ActivityDTO complete(
            @PathVariable Integer activityId,
            @Valid @RequestBody ActivityCompletionDTO request,
            Authentication authentication) {
        ActividadRecomendada activity = findActivity(activityId);
        requireOwner(authentication, activity.getUsuario());
        if (!"PENDING".equalsIgnoreCase(activity.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be completed");
        }
        activity.setEstado("COMPLETED");
        activity.setFechaCompletada(LocalDate.now());
        activity.setFeedbackUsuario(request.getFeedback());
        activityService.update(activity);
        return toDTO(activity);
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ActivityDTO update(@PathVariable Integer activityId, @Valid @RequestBody ActivityDTO request) {
        ActividadRecomendada activity = findActivity(activityId);
        apply(request, activity);
        activityService.update(activity);
        return toDTO(activity);
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<Void> delete(@PathVariable Integer activityId) {
        ActividadRecomendada activity = findActivity(activityId);
        if (!"PENDING".equalsIgnoreCase(activity.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be deleted");
        }
        activityService.delete(activityId);
        return ResponseEntity.noContent().build();
    }

    private void apply(ActivityDTO request, ActividadRecomendada activity) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        activity.setUsuario(user);
        activity.setDiagnosticoId(request.getDiagnosisId());
        activity.setTitulo(request.getTitle().trim());
        activity.setDescripcion(request.getDescription().trim());
        activity.setTipo(request.getType().trim());
        if (request.getAssignedDate() != null) {
            activity.setFechaAsignacion(request.getAssignedDate());
        }
    }

    private ActividadRecomendada findActivity(Integer activityId) {
        return activityService.listId(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));
    }

    private void requireOwnerOrPrivileged(Authentication authentication, Long userId) {
        if (isPrivileged(authentication)) {
            return;
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        requireOwner(authentication, user);
    }

    private void requireOwner(Authentication authentication, Users user) {
        if (authentication == null || !user.getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this activity");
        }
    }

    private boolean isPrivileged(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .anyMatch(role -> role.equals("ADMINISTRADOR") || role.equals("ROLE_ADMINISTRADOR")
                        || role.equals("PSICOLOGO") || role.equals("ROLE_PSICOLOGO"));
    }

    private ActivityDTO toDTO(ActividadRecomendada activity) {
        ActivityDTO response = new ActivityDTO();
        response.setActivityId(activity.getActividadId());
        response.setUserId(activity.getUsuario().getId());
        response.setDiagnosisId(activity.getDiagnosticoId());
        response.setTitle(activity.getTitulo());
        response.setDescription(activity.getDescripcion());
        response.setType(activity.getTipo());
        response.setAssignedDate(activity.getFechaAsignacion());
        response.setCompletedDate(activity.getFechaCompletada());
        response.setStatus(activity.getEstado());
        response.setFeedback(activity.getFeedbackUsuario());
        return response;
    }
}
