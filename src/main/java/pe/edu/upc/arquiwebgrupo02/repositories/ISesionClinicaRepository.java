package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;

import java.util.List;

public interface ISesionClinicaRepository extends JpaRepository<SesionClinica, Long> {

    // Historial de sesiones del usuario, de la mas reciente a la mas antigua
    @Query(value = "select * from sesiones_clinicas " +
            "where usuario_id = :usuarioId " +
            "order by fecha_hora_inicio desc", nativeQuery = true)
    public List<SesionClinica> buscarHistorialPorUsuario(@Param("usuarioId") Long usuarioId);

    // Sesion que el usuario tiene abierta (en curso), si existe
    @Query(value = "select * from sesiones_clinicas " +
            "where usuario_id = :usuarioId and estado = 'en_curso' " +
            "limit 1", nativeQuery = true)
    public SesionClinica buscarEnCursoPorUsuario(@Param("usuarioId") Long usuarioId);

    // Sesiones que la IA marco para derivar a un psicologo
    @Query(value = "select * from sesiones_clinicas " +
            "where requiere_derivacion = true " +
            "order by fecha_hora_inicio desc", nativeQuery = true)
    public List<SesionClinica> buscarQueRequierenDerivacion();
}