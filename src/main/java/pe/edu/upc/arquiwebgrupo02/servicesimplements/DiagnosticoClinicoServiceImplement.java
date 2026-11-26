package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IDiagnosticoClinicoRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.ISesionClinicaRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IDiagnosticoClinicoService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DiagnosticoClinicoServiceImplement implements IDiagnosticoClinicoService {
    private final IDiagnosticoClinicoRepository dcR;
    private final ISesionClinicaRepository scR;
    private final IUsuarioRepository uR;
    private final IUserService uS;

    public DiagnosticoClinicoServiceImplement(IDiagnosticoClinicoRepository dcR, ISesionClinicaRepository scR,
                                              IUsuarioRepository uR, IUserService uS) {
        this.dcR = dcR;
        this.scR = scR;
        this.uR = uR;
        this.uS = uS;
    }

    @Override
    public DiagnosticoClinico registrar(DiagnosticoClinico d, Users paciente) {
        SesionClinica sesion = scR.findById(d.getSesionClinica().getSesionClinicaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada"));

        if (!sesion.getUsuario().getId().equals(paciente.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a esta sesión");
        }
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
        DiagnosticoClinico d = buscar(diagnosticoClinicoId);

        if (!uS.esPsicologo(psicologo)) {
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
    public DiagnosticoClinico buscarParaUsuario(Long diagnosticoClinicoId, Users actual) {
        DiagnosticoClinico d = buscar(diagnosticoClinicoId);
        if (uS.esPaciente(actual) && !d.getUsuario().getId().equals(actual.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver este diagnóstico");
        }
        return d;
    }

    @Override
    public List<DiagnosticoClinico> historialPorUsuario(Long usuarioId) {
        return dcR.buscarHistorialPorUsuario(usuarioId);
    }

    @Override
    public List<DiagnosticoClinico> historialParaUsuario(Long usuarioId, Users actual) {
        if (!uR.existsById(usuarioId)) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + usuarioId);
        }
        return dcR.buscarHistorialPorUsuario(usuarioId);
    }

    @Override
    public List<DiagnosticoClinico> listarPendientesDeValidar() {
        return dcR.buscarPendientesDeValidar();
    }

    @Override
    public List<DiagnosticoClinico> listarValidadosPorPsicologo(Long psicologoId, Users actual) {
        if (uS.esPsicologo(actual) && !actual.getId().equals(psicologoId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes consultar los diagnósticos que tú validaste");
        }
        return dcR.buscarValidadosPorPsicologo(psicologoId);
    }

    @Override
    public void delete(Long diagnosticoClinicoId) {
        DiagnosticoClinico d = buscar(diagnosticoClinicoId);
        if (d.getValidacionPsicologo() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un diagnóstico validado");
        }
        dcR.deleteById(diagnosticoClinicoId);
    }

    private DiagnosticoClinico buscar(Long diagnosticoClinicoId) {
        return dcR.findById(diagnosticoClinicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado"));
    }
}
