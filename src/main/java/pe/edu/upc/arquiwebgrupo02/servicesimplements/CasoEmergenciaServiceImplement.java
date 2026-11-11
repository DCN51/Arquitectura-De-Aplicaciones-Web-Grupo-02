package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.CasoEmergencia;
import pe.edu.upc.arquiwebgrupo02.repositories.ICasoEmergenciaRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ICasoEmergenciaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CasoEmergenciaServiceImplement implements ICasoEmergenciaService {
    private final ICasoEmergenciaRepository ceR;

    public CasoEmergenciaServiceImplement(ICasoEmergenciaRepository ceR) {
        this.ceR = ceR;
    }

    @Override
    public void insert(CasoEmergencia c) {
        // Si no mandan fecha, se pone la actual
        if (c.getFechaEmergencia() == null) {
            c.setFechaEmergencia(LocalDateTime.now());
        }
        // Todo caso nuevo empieza como pendiente
        if (c.getEstado() == null) {
            c.setEstado("pendiente");
        }
        ceR.save(c);
    }

    @Override
    public List<CasoEmergencia> list() {
        return ceR.findAll();
    }

    @Override
    public void update(CasoEmergencia c) {
        // Si el psicologo cierra el caso, se registra la fecha de cierre
        if ("cerrado".equals(c.getEstado()) && c.getFechaCierre() == null) {
            c.setFechaCierre(LocalDateTime.now());
        }
        ceR.save(c);
    }

    @Override
    public void delete(Long id) {
        ceR.deleteById(id);
    }

    @Override
    public Optional<CasoEmergencia> listId(Long id) {
        return ceR.findById(id);
    }

    @Override
    public List<CasoEmergencia> listarPendientesPorUrgencia() {
        return ceR.listarPendientesPorUrgencia();
    }

    @Override
    public List<Object[]> contarCasosPorPsicologo() {
        return ceR.contarCasosPorPsicologo();
    }
}