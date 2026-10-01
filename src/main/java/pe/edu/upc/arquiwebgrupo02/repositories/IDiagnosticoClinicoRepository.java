package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;

import java.util.List;

public interface IDiagnosticoClinicoRepository extends JpaRepository<DiagnosticoClinico, Long> {

    @Query(value = "select * from diagnosticos_clinicos " +
            "where usuario_id = :usuarioId " +
            "order by fecha_diagnostico desc", nativeQuery = true)
    public List<DiagnosticoClinico> buscarHistorialPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query(value = "select * from diagnosticos_clinicos " +
            "where validacion_psicologo_id is null " +
            "order by fecha_diagnostico asc", nativeQuery = true)
    public List<DiagnosticoClinico> buscarPendientesDeValidar();

    @Query(value = "select * from diagnosticos_clinicos " +
            "where sesion_clinica_id = :sesionClinicaId " +
            "limit 1", nativeQuery = true)
    public DiagnosticoClinico buscarPorSesion(@Param("sesionClinicaId") Long sesionClinicaId);

    @Query(value = "select * from diagnosticos_clinicos " +
            "where validacion_psicologo_id = :psicologoId " +
            "order by fecha_validacion desc", nativeQuery = true)
    public List<DiagnosticoClinico> buscarValidadosPorPsicologo(@Param("psicologoId") Long psicologoId);
}