package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.AsignacionPaciente;

import java.util.List;

public interface IAsignacionPacienteService {
    List<AsignacionPaciente> listarActivas();
    AsignacionPaciente asignar(Long pacienteId, Long psicologoId);
    boolean estaAsignado(Long psicologoId, Long pacienteId);
    int contarAsignados(Long psicologoId);
    List<Object[]> contarPacientesPorPsicologo();
}
