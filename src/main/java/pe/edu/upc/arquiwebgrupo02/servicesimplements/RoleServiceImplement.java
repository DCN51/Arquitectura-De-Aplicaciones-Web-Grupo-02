package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IRoleRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRoleService;

@Service
public class RoleServiceImplement implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IUsuarioRepository userRepository;

    public RoleServiceImplement(IRoleRepository roleRepository, IUsuarioRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Role> list() {
        return roleRepository.findAll();
    }


}
