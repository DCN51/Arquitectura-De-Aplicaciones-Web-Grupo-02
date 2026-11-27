package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IAsignacionPacienteService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IPsicologoService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.util.List;

@Service
public class PsicologoServiceImplement implements IPsicologoService {
    private final IUsuarioRepository uR;
    private final IUserService uS;
    private final IAsignacionPacienteService apS;

    public PsicologoServiceImplement(IUsuarioRepository uR, IUserService uS, IAsignacionPacienteService apS) {
        this.uR = uR;
        this.uS = uS;
        this.apS = apS;
    }

    @Override
    public List<Users> listar() {
        return uR.listarPsicologos();
    }

    @Override
    @Transactional
    public Users verificar(Long psicologoId) {
        Users psicologo = buscarPsicologo(psicologoId);
        if ("VERIFICADO".equalsIgnoreCase(psicologo.getEstadoVerificacion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El psicólogo ya está verificado");
        }
        psicologo.setEstadoVerificacion("VERIFICADO");
        return uR.save(psicologo);
    }

    @Override
    @Transactional
    public Users cambiarCapacidad(Long psicologoId, Integer maxPacientes) {
        Users psicologo = buscarPsicologo(psicologoId);
        int asignados = apS.contarAsignados(psicologoId);
        if (maxPacientes < asignados) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La capacidad no puede ser menor que los pacientes ya asignados (" + asignados + ")");
        }
        psicologo.setMaxPacientes(maxPacientes);
        return uR.save(psicologo);
    }

    private Users buscarPsicologo(Long psicologoId) {
        Users usuario = uS.buscarPorId(psicologoId);
        if (!uS.esPsicologo(usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no es un psicólogo");
        }
        return usuario;
    }
}
