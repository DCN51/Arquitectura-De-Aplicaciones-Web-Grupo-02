package pe.edu.upc.arquiwebgrupo02.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;

public interface IRecommendedActivityRepository extends JpaRepository<RecommendedActivity, Integer> {

    // HU-08: actividades del paciente, filtros opcionales, de la mas reciente a la mas antigua
    @Query(value = "select * from recommended_activities a\n" +
            " where a.user_id = :userId\n" +
            " and (cast(:status as varchar) is null or upper(a.status) = upper(cast(:status as varchar)))\n" +
            " and (cast(:from as date) is null or a.assigned_date >= cast(:from as date))\n" +
            " and (cast(:to as date) is null or a.assigned_date <= cast(:to as date))\n" +
            " order by a.assigned_date desc", nativeQuery = true)
    List<RecommendedActivity> buscarPorPaciente(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}