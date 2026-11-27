package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.AsignacionPaciente;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IAsignacionPacienteRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IAsignacionPacienteService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.time.LocalDate;
import java.util.List;

@Service
public class AsignacionPacienteServiceImplement implements IAsignacionPacienteService {
    private final IAsignacionPacienteRepository apR;
    private final IUserService uS;

    public AsignacionPacienteServiceImplement(IAsignacionPacienteRepository apR, IUserService uS) {
        this.apR = apR;
        this.uS = uS;
    }

    @Override
    public List<AsignacionPaciente> listarActivas() {
        return apR.listarActivas();
    }

    @Override
    @Transactional
    public AsignacionPaciente asignar(Long pacienteId, Long psicologoId) {
        Users paciente = uS.buscarPorId(pacienteId);
        Users psicologo = uS.buscarPorId(psicologoId);

        if (!uS.esPaciente(paciente)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado como paciente no tiene el rol PACIENTE");
        }
        if (!uS.esPsicologo(psicologo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado como psicólogo no tiene el rol PSICOLOGO");
        }
        if (!paciente.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta del paciente está inactiva");
        }
        if (!psicologo.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta del psicólogo está inactiva");
        }
        if (!"VERIFICADO".equalsIgnoreCase(psicologo.getEstadoVerificacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El psicólogo aún no está verificado");
        }
        if (psicologo.getMaxPacientes() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El psicólogo no tiene una capacidad máxima asignada");
        }
        if (estaAsignado(psicologoId, pacienteId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El paciente ya está asignado a este psicólogo");
        }
        if (contarAsignados(psicologoId) >= psicologo.getMaxPacientes()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El psicólogo alcanzó su máximo de " + psicologo.getMaxPacientes() + " pacientes");
        }

        AsignacionPaciente anterior = apR.buscarActivaPorPaciente(pacienteId);
        if (anterior != null) {
            anterior.setActiva(false);
            apR.save(anterior);
        }

        AsignacionPaciente nueva = new AsignacionPaciente();
        nueva.setPaciente(paciente);
        nueva.setPsicologo(psicologo);
        nueva.setFechaAsignacion(LocalDate.now());
        nueva.setActiva(true);
        return apR.save(nueva);
    }

    @Override
    public boolean estaAsignado(Long psicologoId, Long pacienteId) {
        return apR.contarActivaDePsicologoYPaciente(psicologoId, pacienteId) > 0;
    }

    @Override
    public int contarAsignados(Long psicologoId) {
        return apR.contarActivasPorPsicologo(psicologoId);
    }

    @Override
    public List<Object[]> contarPacientesPorPsicologo() {
        return apR.contarPacientesPorPsicologo();
    }
}
