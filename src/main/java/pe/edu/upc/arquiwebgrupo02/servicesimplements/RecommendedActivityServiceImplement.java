package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.time.LocalDate;
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
    public List<RecommendedActivity> buscarPorPaciente(Long userId, String status, LocalDate from, LocalDate to) {
        return recommendedActivityRepository.buscarPorPaciente(userId, status, from, to);
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
