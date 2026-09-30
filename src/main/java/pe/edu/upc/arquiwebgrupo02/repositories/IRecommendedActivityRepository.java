package pe.edu.upc.arquiwebgrupo02.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;

public interface IRecommendedActivityRepository extends JpaRepository<RecommendedActivity, Integer> {
    List<RecommendedActivity> findByUserId(Long userId);
    List<RecommendedActivity> findByUserIdAndStatusIgnoreCase(Long userId, String status);
}
