package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;

import java.time.LocalDate;
import java.util.List;

public interface ISuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    // US06 y US07: la suscripcion activa del usuario
    @Query(value = "select * from suscripciones " +
            "where usuario_id = :usuarioId and estado_suscripcion = 'activa' " +
            "limit 1", nativeQuery = true)
    public Suscripcion buscarActivaPorUsuario(@Param("usuarioId") Long usuarioId);

    // US08: historial del usuario, de la mas reciente a la mas antigua
    @Query(value = "select * from suscripciones " +
            "where usuario_id = :usuarioId " +
            "order by fecha_inicio desc", nativeQuery = true)
    public List<Suscripcion> buscarHistorialPorUsuario(@Param("usuarioId") Long usuarioId);

    // US09: suscripciones activas que vencen entre dos fechas
    @Query(value = "select * from suscripciones " +
            "where estado_suscripcion = 'activa' and fecha_fin between :desde and :hasta " +
            "order by fecha_fin asc", nativeQuery = true)
    public List<Suscripcion> buscarPorVencer(@Param("desde") LocalDate desde,
                                             @Param("hasta") LocalDate hasta);

    // US10: cantidad de suscriptores activos por plan
    @Query(value = "select c.nombre_catalogo_plan, count(s.suscripcion_id) " +
            "from catalogoplanes c left join suscripciones s " +
            "on c.catalogo_plan_id = s.catalogo_plan_id and s.estado_suscripcion = 'activa' " +
            "group by c.catalogo_plan_id, c.nombre_catalogo_plan " +
            "order by count(s.suscripcion_id) desc", nativeQuery = true)
    public List<Object[]> contarSuscriptoresActivosPorPlan();
}
