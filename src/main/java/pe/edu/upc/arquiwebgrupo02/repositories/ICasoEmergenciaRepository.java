package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.arquiwebgrupo02.entities.CasoEmergencia;

import java.util.List;

public interface ICasoEmergenciaRepository extends JpaRepository<CasoEmergencia, Long> {

    // Query 1: casos pendientes, primero los mas urgentes y, dentro de cada nivel, los mas antiguos
    @Query(value = "select * from caso_emergencias\n" +
            " where estado = 'pendiente'\n" +
            " order by case nivel_urgencia\n" +
            "     when 'critico' then 1\n" +
            "     when 'alto' then 2\n" +
            "     when 'medio' then 3\n" +
            "     else 4 end,\n" +
            " fecha_emergencia asc", nativeQuery = true)
    public List<CasoEmergencia> listarPendientesPorUrgencia();

    // Query 2: cantidad de casos asignados a cada psicologo
    @Query(value = "select concat(u.nombres, ' ', u.apellidos), count(c.caso_emergencia_id)\n" +
            " from caso_emergencias c inner join users u\n" +
            " on c.psicologo_id = u.id_user\n" +
            " group by u.id_user, u.nombres, u.apellidos\n" +
            " order by count(c.caso_emergencia_id) desc", nativeQuery = true)
    public List<Object[]> contarCasosPorPsicologo();
}