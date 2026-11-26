package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.Size;

public class RecommendedActivityCompletionDTO {
    @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
