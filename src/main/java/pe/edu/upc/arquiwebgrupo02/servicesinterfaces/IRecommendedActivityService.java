package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;

public interface IRecommendedActivityService {
    RecommendedActivity create(RecommendedActivity recommendedActivity);
    List<RecommendedActivity> buscarPorPaciente(Long userId, String status, LocalDate from, LocalDate to);
    RecommendedActivity update(RecommendedActivity recommendedActivity);
    void deleteById(Integer recommendedActivityId);
    Optional<RecommendedActivity> findById(Integer recommendedActivityId);
}
