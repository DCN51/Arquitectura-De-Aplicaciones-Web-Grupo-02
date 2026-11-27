package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import java.util.List;
import java.util.Optional;
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
    // Roles que usa la aplicación: no se pueden renombrar ni eliminar
    private static final List<String> ROLES_DEL_SISTEMA =
            List.of("ROLE_ADMINISTRADOR", "ROLE_PSICOLOGO", "ROLE_PACIENTE");

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

    @Override
    public Optional<Role> listId(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role insert(Role role) {
        role.setRol(normalizar(role.getRol()));
        if (roleRepository.existsByRolIgnoreCase(role.getRol())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un rol con ese nombre");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        Role existente = roleRepository.findById(role.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un rol con el id: " + role.getId()));

        if (ROLES_DEL_SISTEMA.contains(existente.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los roles del sistema no se pueden modificar");
        }

        String nuevoNombre = normalizar(role.getRol());
        if (!nuevoNombre.equalsIgnoreCase(existente.getRol())
                && roleRepository.existsByRolIgnoreCase(nuevoNombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un rol con ese nombre");
        }

        existente.setRol(nuevoNombre);
        return roleRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        Role existente = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un rol con el id: " + id));

        if (ROLES_DEL_SISTEMA.contains(existente.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los roles del sistema no se pueden eliminar");
        }
        if (userRepository.existsByRoleId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar un rol asignado a usuarios");
        }
        roleRepository.deleteById(id);
    }

    // "supervisor" -> "ROLE_SUPERVISOR" (Spring Security necesita el prefijo ROLE_)
    private String normalizar(String nombre) {
        String limpio = nombre.trim().toUpperCase().replace(' ', '_');
        return limpio.startsWith("ROLE_") ? limpio : "ROLE_" + limpio;
    }
}
