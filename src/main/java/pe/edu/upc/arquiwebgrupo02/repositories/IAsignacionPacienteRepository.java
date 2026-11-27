package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.AsignacionPaciente;

import java.util.List;

public interface IAsignacionPacienteRepository extends JpaRepository<AsignacionPaciente, Long> {

    @Query(value = "select * from asignaciones_pacientes " +
            " where activa = true" +
            " order by fecha_asignacion desc", nativeQuery = true)
    public List<AsignacionPaciente> listarActivas();

    @Query(value = "select * from asignaciones_pacientes " +
            " where paciente_id = :pacienteId and activa = true" +
            " limit 1", nativeQuery = true)
    public AsignacionPaciente buscarActivaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query(value = "select count(*) from asignaciones_pacientes " +
            " where psicologo_id = :psicologoId and activa = true", nativeQuery = true)
    public int contarActivasPorPsicologo(@Param("psicologoId") Long psicologoId);

    @Query(value = "select count(*) from asignaciones_pacientes " +
            " where psicologo_id = :psicologoId and paciente_id = :pacienteId and activa = true", nativeQuery = true)
    public int contarActivaDePsicologoYPaciente(@Param("psicologoId") Long psicologoId,
                                                @Param("pacienteId") Long pacienteId);

    @Query(value = "select concat(u.nombres, ' ', u.apellidos), count(a.asignacion_paciente_id)\n" +
            " from users u inner join roles r on u.idrol = r.id\n" +
            " left join asignaciones_pacientes a on a.psicologo_id = u.id_user and a.activa = true\n" +
            " where upper(r.rol) in ('PSICOLOGO', 'ROLE_PSICOLOGO')\n" +
            " group by u.id_user, u.nombres, u.apellidos\n" +
            " order by count(a.asignacion_paciente_id) desc", nativeQuery = true)
    public List<Object[]> contarPacientesPorPsicologo();
}
