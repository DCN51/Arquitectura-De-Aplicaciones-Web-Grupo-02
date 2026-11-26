package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.List;
import java.util.Optional;

public interface IDiagnosticoClinicoService {
    DiagnosticoClinico registrar(DiagnosticoClinico d, Users paciente);
    DiagnosticoClinico validar(Long diagnosticoClinicoId, Users psicologo);
    Optional<DiagnosticoClinico> listId(Long diagnosticoClinicoId);
    DiagnosticoClinico buscarParaUsuario(Long diagnosticoClinicoId, Users actual);
    List<DiagnosticoClinico> historialPorUsuario(Long usuarioId);
    List<DiagnosticoClinico> historialParaUsuario(Long usuarioId, Users actual);
    List<DiagnosticoClinico> listarPendientesDeValidar();
    List<DiagnosticoClinico> listarValidadosPorPsicologo(Long psicologoId, Users actual);
    void delete(Long diagnosticoClinicoId);
}
