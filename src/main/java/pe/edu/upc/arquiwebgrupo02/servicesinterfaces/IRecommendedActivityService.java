package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;

public interface IRecommendedActivityService {
    RecommendedActivity create(RecommendedActivity recommendedActivity);
    List<RecommendedActivity> findByUserId(Long userId);
    List<RecommendedActivity> findByUserIdAndStatus(Long userId, String status);
    RecommendedActivity update(RecommendedActivity recommendedActivity);
    void deleteById(Integer recommendedActivityId);
    Optional<RecommendedActivity> findById(Integer recommendedActivityId);
}
