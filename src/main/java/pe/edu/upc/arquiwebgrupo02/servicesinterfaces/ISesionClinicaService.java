package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;

import java.util.List;
import java.util.Optional;

public interface ISesionClinicaService {
    SesionClinica iniciar(SesionClinica s);
    SesionClinica finalizar(Long sesionClinicaId, SesionClinica datos);
    Optional<SesionClinica> listId(Long sesionClinicaId);
    List<SesionClinica> historialPorUsuario(Long usuarioId);
    List<SesionClinica> listarQueRequierenDerivacion();
    void delete(Long sesionClinicaId);
}