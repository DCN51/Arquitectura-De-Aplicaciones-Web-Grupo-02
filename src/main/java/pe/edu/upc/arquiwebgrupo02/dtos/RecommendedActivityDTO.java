package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class RecommendedActivityDTO {
    private Integer recommendedActivityId;

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Integer diagnosisId;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 50)
    private String type;

    private LocalDate assignedDate;
    private LocalDate completedDate;
    private String status;
    private String feedback;

    public Integer getRecommendedActivityId() { return recommendedActivityId; }
    public void setRecommendedActivityId(Integer recommendedActivityId) { this.recommendedActivityId = recommendedActivityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getDiagnosisId() { return diagnosisId; }
    public void setDiagnosisId(Integer diagnosisId) { this.diagnosisId = diagnosisId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
