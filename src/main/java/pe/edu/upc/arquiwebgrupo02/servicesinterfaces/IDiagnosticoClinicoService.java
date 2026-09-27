package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.List;
import java.util.Optional;

public interface IDiagnosticoClinicoService {
    DiagnosticoClinico registrar(DiagnosticoClinico d);
    DiagnosticoClinico validar(Long diagnosticoClinicoId, Users psicologo);
    Optional<DiagnosticoClinico> listId(Long diagnosticoClinicoId);
    List<DiagnosticoClinico> historialPorUsuario(Long usuarioId);
    List<DiagnosticoClinico> listarPendientesDeValidar();
    List<DiagnosticoClinico> listarValidadosPorPsicologo(Long psicologoId);
    void delete(Long diagnosticoClinicoId);
}