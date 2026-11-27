package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.arquiwebgrupo02.dtos.RecommendedActivityDTO;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

public interface IRecommendedActivityService {
    RecommendedActivity crear(RecommendedActivityDTO datos, Users psicologo);
    RecommendedActivity actualizar(Integer recommendedActivityId, RecommendedActivityDTO datos, Users psicologo);
    void eliminar(Integer recommendedActivityId, Users psicologo);
    RecommendedActivity completar(Integer recommendedActivityId, String feedback, Users paciente);
    List<RecommendedActivity> listarDelPaciente(Users paciente, String status, LocalDate from, LocalDate to);
    List<RecommendedActivity> listarParaPsicologo(Long pacienteId, String status, LocalDate from, LocalDate to, Users psicologo);
    Optional<RecommendedActivity> findById(Integer recommendedActivityId);
}
