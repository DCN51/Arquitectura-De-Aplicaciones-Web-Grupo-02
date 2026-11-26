package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.List;
import java.util.Optional;

public interface ISesionClinicaService {
    SesionClinica iniciar(Users paciente);
    SesionClinica finalizar(Long sesionClinicaId, SesionClinica datos, Users paciente);
    Optional<SesionClinica> listId(Long sesionClinicaId);
    SesionClinica buscarDelPaciente(Long sesionClinicaId, Users paciente);
    List<SesionClinica> historialPorUsuario(Long usuarioId);
    List<SesionClinica> listarQueRequierenDerivacion();
    void delete(Long sesionClinicaId, Users paciente);
}
