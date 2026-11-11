package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IDiagnosticoClinicoRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.ISesionClinicaRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IDiagnosticoClinicoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DiagnosticoClinicoServiceImplement implements IDiagnosticoClinicoService {
    private final IDiagnosticoClinicoRepository dcR;
    private final ISesionClinicaRepository scR;

    public DiagnosticoClinicoServiceImplement(IDiagnosticoClinicoRepository dcR, ISesionClinicaRepository scR) {
        this.dcR = dcR;
        this.scR = scR;
    }

    @Override
    public DiagnosticoClinico registrar(DiagnosticoClinico d) {
        SesionClinica sesion = scR.findById(d.getSesionClinica().getSesionClinicaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada"));

        if (!"finalizada".equals(sesion.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se puede diagnosticar una sesión finalizada");
        }
        if (dcR.buscarPorSesion(sesion.getSesionClinicaId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta sesión ya tiene un diagnóstico");
        }

        String riesgo = d.getNivelRiesgo().toLowerCase(Locale.ROOT);
        if (!List.of("bajo", "medio", "alto", "critico").contains(riesgo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel de riesgo inválido (bajo, medio, alto, critico)");
        }

        d.setDiagnosticoClinicoId(null);
        d.setSesionClinica(sesion);
        d.setUsuario(sesion.getUsuario());
        d.setNivelRiesgo(riesgo);
        d.setFechaDiagnostico(LocalDateTime.now());
        d.setValidacionPsicologo(null);
        d.setFechaValidacion(null);

        return dcR.save(d);
    }

    @Override
    public DiagnosticoClinico validar(Long diagnosticoClinicoId, Users psicologo) {
        DiagnosticoClinico d = dcR.findById(diagnosticoClinicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado"));

        String rol = psicologo.getRole().getRol().toUpperCase(Locale.ROOT);
        if (!rol.equals("PSICOLOGO") && !rol.equals("ROLE_PSICOLOGO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo un psicólogo puede validar un diagnóstico");
        }
        if (d.getValidacionPsicologo() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El diagnóstico ya fue validado");
        }

        d.setValidacionPsicologo(psicologo);
        d.setFechaValidacion(LocalDateTime.now());
        return dcR.save(d);
    }

    @Override
    public Optional<DiagnosticoClinico> listId(Long diagnosticoClinicoId) {
        return dcR.findById(diagnosticoClinicoId);
    }

    @Override
    public List<DiagnosticoClinico> historialPorUsuario(Long usuarioId) {
        return dcR.buscarHistorialPorUsuario(usuarioId);
    }

    @Override
    public List<DiagnosticoClinico> listarPendientesDeValidar() {
        return dcR.buscarPendientesDeValidar();
    }

    @Override
    public List<DiagnosticoClinico> listarValidadosPorPsicologo(Long psicologoId) {
        return dcR.buscarValidadosPorPsicologo(psicologoId);
    }

    @Override
    public void delete(Long diagnosticoClinicoId) {
        DiagnosticoClinico d = dcR.findById(diagnosticoClinicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado"));
        if (d.getValidacionPsicologo() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un diagnóstico validado");
        }
        dcR.deleteById(diagnosticoClinicoId);
    }
}