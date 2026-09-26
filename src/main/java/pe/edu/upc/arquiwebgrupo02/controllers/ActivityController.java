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
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityCompletionDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRecommendedActivityService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final IRecommendedActivityService recommendedActivityService;
    private final IUsuarioRepository userRepository;

    public ActivityController(
            IRecommendedActivityService recommendedActivityService,
            IUsuarioRepository userRepository) {
        this.recommendedActivityService = recommendedActivityService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<RecommendedActivityDTO> create(@Valid @RequestBody RecommendedActivityDTO request) {
        RecommendedActivity recommendedActivity = new RecommendedActivity();
        apply(request, recommendedActivity);
        recommendedActivity.setStatus("PENDING");
        recommendedActivity.setAssignedDate(request.getAssignedDate() == null ? LocalDate.now() : request.getAssignedDate());
        RecommendedActivity createdActivity = recommendedActivityService.create(recommendedActivity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(createdActivity));
    }

    @GetMapping("/users/{userId}")
    public List<RecommendedActivityDTO> listForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            Authentication authentication) {
        requireOwnerOrPrivileged(authentication, userId);
        List<RecommendedActivity> activities = status == null || status.isBlank()
                ? recommendedActivityService.findByUserId(userId)
                : recommendedActivityService.findByUserIdAndStatus(userId, status);
        return activities.stream().map(this::toDTO).toList();
    }

    @PutMapping("/{activityId}/complete")
    public RecommendedActivityDTO complete(
            @PathVariable Integer activityId,
            @Valid @RequestBody RecommendedActivityCompletionDTO request,
            Authentication authentication) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        requireOwner(authentication, recommendedActivity.getUser());
        if (!"PENDING".equalsIgnoreCase(recommendedActivity.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be completed");
        }
        recommendedActivity.setStatus("COMPLETED");
        recommendedActivity.setCompletedDate(LocalDate.now());
        recommendedActivity.setFeedback(request.getFeedback());
        return toDTO(recommendedActivityService.update(recommendedActivity));
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public RecommendedActivityDTO update(@PathVariable Integer activityId, @Valid @RequestBody RecommendedActivityDTO request) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        apply(request, recommendedActivity);
        return toDTO(recommendedActivityService.update(recommendedActivity));
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR', 'PSICOLOGO', 'ROLE_PSICOLOGO')")
    public ResponseEntity<Void> delete(@PathVariable Integer activityId) {
        RecommendedActivity recommendedActivity = findActivity(activityId);
        if (!"PENDING".equalsIgnoreCase(recommendedActivity.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending activities can be deleted");
        }
        recommendedActivityService.deleteById(activityId);
        return ResponseEntity.noContent().build();
    }

    private void apply(RecommendedActivityDTO request, RecommendedActivity recommendedActivity) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        recommendedActivity.setUser(user);
        recommendedActivity.setDiagnosisId(request.getDiagnosisId());
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

    private RecommendedActivityDTO toDTO(RecommendedActivity recommendedActivity) {
        RecommendedActivityDTO response = new RecommendedActivityDTO();
        response.setRecommendedActivityId(recommendedActivity.getRecommendedActivityId());
        response.setUserId(recommendedActivity.getUser().getId());
        response.setDiagnosisId(recommendedActivity.getDiagnosisId());
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
