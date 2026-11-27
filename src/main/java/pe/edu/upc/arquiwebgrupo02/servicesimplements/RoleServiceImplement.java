package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.repositories.IRoleRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRoleService;

@Service
public class RoleServiceImplement implements IRoleService {
    private static final List<String> ROLES_REGISTRO = List.of("PACIENTE", "PSICOLOGO");

    private final IRoleRepository roleRepository;

    public RoleServiceImplement(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> list() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> listarParaRegistro() {
        return roleRepository.findAll()
                .stream()
                .filter(role -> role.getRol() != null
                        && ROLES_REGISTRO.contains(role.getRol().toUpperCase(Locale.ROOT).replace("ROLE_", "")))
                .toList();
    }
}
