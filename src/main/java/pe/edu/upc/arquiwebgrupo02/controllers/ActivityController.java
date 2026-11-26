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
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityCompletionDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRecommendedActivityService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final IRecommendedActivityService recommendedActivityService;
    private final IUserService uS;

    public ActivityController(IRecommendedActivityService recommendedActivityService, IUserService uS) {
        this.recommendedActivityService = recommendedActivityService;
        this.uS = uS;
    }

    @PostMapping
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<RecommendedActivityDTO> create(@Valid @RequestBody RecommendedActivityDTO request,
                                                         Authentication authentication) {
        RecommendedActivity creada = recommendedActivityService.crear(request, uS.usuarioActual(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creada));
    }

    @GetMapping("/mis-actividades")
    @PreAuthorize("hasRole('PACIENTE')")
    public List<RecommendedActivityDTO> misActividades(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Authentication authentication) {
        return recommendedActivityService.listarDelPaciente(uS.usuarioActual(authentication), status, from, to)
                .stream().map(this::toDTO).toList();
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public List<RecommendedActivityDTO> listForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Authentication authentication) {
        return recommendedActivityService.listarParaPsicologo(userId, status, from, to, uS.usuarioActual(authentication))
                .stream().map(this::toDTO).toList();
    }

    @PutMapping("/{activityId}/complete")
    @PreAuthorize("hasRole('PACIENTE')")
    public RecommendedActivityDTO complete(
            @PathVariable Integer activityId,
            @Valid @RequestBody RecommendedActivityCompletionDTO request,
            Authentication authentication) {
        return toDTO(recommendedActivityService.completar(activityId, request.getFeedback(), uS.usuarioActual(authentication)));
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public RecommendedActivityDTO update(@PathVariable Integer activityId,
                                         @Valid @RequestBody RecommendedActivityDTO request,
                                         Authentication authentication) {
        return toDTO(recommendedActivityService.actualizar(activityId, request, uS.usuarioActual(authentication)));
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<Void> delete(@PathVariable Integer activityId, Authentication authentication) {
        recommendedActivityService.eliminar(activityId, uS.usuarioActual(authentication));
        return ResponseEntity.noContent().build();
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
