package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.arquiwebgrupo02.entities.ActividadRecomendada;

public interface IActividadRecomendadaService {
    void insert(ActividadRecomendada activity);
    List<ActividadRecomendada> listByUserId(Long userId);
    List<ActividadRecomendada> listByUserIdAndStatus(Long userId, String status);
    void update(ActividadRecomendada activity);
    void delete(Integer activityId);
    Optional<ActividadRecomendada> listId(Integer activityId);
}
