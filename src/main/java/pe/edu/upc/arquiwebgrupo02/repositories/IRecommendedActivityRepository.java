package pe.edu.upc.arquiwebgrupo02.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.RecommendedActivity;

public interface IRecommendedActivityRepository extends JpaRepository<RecommendedActivity, Integer> {
    @Query("select a from RecommendedActivity a " +
            "where a.user.id = :userId " +
            "and (:status is null or upper(a.status) = upper(:status)) " +
            "and (:from is null or a.assignedDate >= :from) " +
            "and (:to is null or a.assignedDate <= :to) " +
            "order by a.assignedDate desc")
    List<RecommendedActivity> buscarPorPaciente(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
