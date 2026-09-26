package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.Size;

public class ActivityCompletionDTO {
    @Size(max = 255)
    private String feedback;

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
