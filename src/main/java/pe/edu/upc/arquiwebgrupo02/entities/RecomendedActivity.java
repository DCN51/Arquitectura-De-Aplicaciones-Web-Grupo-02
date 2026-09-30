package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Table(name = "recommendedActivities")
public class RecomendedActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendedActivityId")
    private Integer recommendedActivityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @Column(name = "diagnosisId", nullable = false)
    private Integer diagnosisId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "assignedDate", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "completedDate")
    private LocalDate completedDate;

    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "feedback", length = 255)
    private String feedback;

    public Integer getRecommendedActivityId() {
        return recommendedActivityId;
    }

    public RecomendedActivity() {
    }

    public RecomendedActivity(Integer recommendedActivityId, Users user, Integer diagnosisId, String title, String description, String type, LocalDate assignedDate, LocalDate completedDate, String status, String feedback) {
        this.recommendedActivityId = recommendedActivityId;
        this.user = user;
        this.diagnosisId = diagnosisId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.assignedDate = assignedDate;
        this.completedDate = completedDate;
        this.status = status;
        this.feedback = feedback;
    }

    public void setRecommendedActivityId(Integer recommendedActivityId) {
        this.recommendedActivityId = recommendedActivityId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

