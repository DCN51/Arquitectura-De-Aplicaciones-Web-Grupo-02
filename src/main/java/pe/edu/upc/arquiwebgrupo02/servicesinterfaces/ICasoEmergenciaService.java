package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.CasoEmergencia;

import java.util.List;
import java.util.Optional;

public interface ICasoEmergenciaService {
    public void insert(CasoEmergencia c);
    public List<CasoEmergencia> list();
    public void update(CasoEmergencia c);
    public void delete(Long id);
    public Optional<CasoEmergencia> listId(Long id);

    public List<CasoEmergencia> listarPendientesPorUrgencia();   // Query 1
    public List<Object[]> contarCasosPorPsicologo();            // Query 2
}