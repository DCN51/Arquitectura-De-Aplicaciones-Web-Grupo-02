package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;
import pe.edu.upc.arquiwebgrupo02.repositories.IRecommendedActivityRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRecommendedActivityService;

@Service
public class RecommendedActivityServiceImplement implements IRecommendedActivityService {
    private final IRecommendedActivityRepository recommendedActivityRepository;

    public RecommendedActivityServiceImplement(IRecommendedActivityRepository recommendedActivityRepository) {
        this.recommendedActivityRepository = recommendedActivityRepository;
    }

    @Override
    public RecommendedActivity create(RecommendedActivity recommendedActivity) {
        return recommendedActivityRepository.save(recommendedActivity);
    }

    @Override
    public List<RecommendedActivity> findByUserId(Long userId) {
        return recommendedActivityRepository.findByUserId(userId);
    }

    @Override
    public List<RecommendedActivity> findByUserIdAndStatus(Long userId, String status) {
        return recommendedActivityRepository.findByUserIdAndStatusIgnoreCase(userId, status);
    }

    @Override
    public RecommendedActivity update(RecommendedActivity recommendedActivity) {
        return recommendedActivityRepository.save(recommendedActivity);
    }

    @Override
    public void deleteById(Integer recommendedActivityId) {
        recommendedActivityRepository.deleteById(recommendedActivityId);
    }

    @Override
    public Optional<RecommendedActivity> findById(Integer recommendedActivityId) {
        return recommendedActivityRepository.findById(recommendedActivityId);
    }
}
