package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;

import java.util.List;

public interface ISuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    // US05: cuantas suscripciones activas tiene el usuario (0 o 1)
    @Query(value = "select count(*) from suscripciones " +
            " where usuario_id = :usuarioId and estado_suscripcion = 'activa'", nativeQuery = true)
    public int contarActivasPorUsuario(@Param("usuarioId") Long usuarioId);

    // US06 y US07: la suscripcion activa del usuario
    @Query(value = "select * from suscripciones " +
            " where usuario_id = :usuarioId and estado_suscripcion = 'activa'" +
            " limit 1", nativeQuery = true)
    public Suscripcion buscarActivaPorUsuario(@Param("usuarioId") Long usuarioId);

    // US08: historial del usuario, de la mas reciente a la mas antigua
    @Query(value = "select * from suscripciones " +
            " where usuario_id = :usuarioId" +
            " order by fecha_inicio desc", nativeQuery = true)
    public List<Suscripcion> buscarHistorialPorUsuario(@Param("usuarioId") Long usuarioId);
}
