package pe.edu.upc.arquiwebgrupo02.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.ActividadRecomendada;

public interface IActividadRecomendadaRepository extends JpaRepository<ActividadRecomendada, Integer> {
    List<ActividadRecomendada> findByUserId(Long userId);
    List<ActividadRecomendada> findByUserIdAndEstadoIgnoreCase(Long userId, String status);
}
