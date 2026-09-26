package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.ActividadRecomendada;
import pe.edu.upc.arquiwebgrupo02.repositories.IActividadRecomendadaRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IActividadRecomendadaService;

@Service
public class ActividadRecomendadaServiceImplement implements IActividadRecomendadaService {
    private final IActividadRecomendadaRepository activityRepository;

    public ActividadRecomendadaServiceImplement(IActividadRecomendadaRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void insert(ActividadRecomendada activity) {
        activityRepository.save(activity);
    }

    @Override
    public List<ActividadRecomendada> listByUserId(Long userId) {
        return activityRepository.findByUserId(userId);
    }

    @Override
    public List<ActividadRecomendada> listByUserIdAndStatus(Long userId, String status) {
        return activityRepository.findByUserIdAndEstadoIgnoreCase(userId, status);
    }

    @Override
    public void update(ActividadRecomendada activity) {
        activityRepository.save(activity);
    }

    @Override
    public void delete(Integer activityId) {
        activityRepository.deleteById(activityId);
    }

    @Override
    public Optional<ActividadRecomendada> listId(Integer activityId) {
        return activityRepository.findById(activityId);
    }
}
