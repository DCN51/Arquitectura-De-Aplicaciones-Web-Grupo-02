package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.dtos.RoleDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IRoleRepository;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleRepository roleRepository;
    private final IUsuarioRepository userRepository;

    public RoleController(IRoleRepository roleRepository, IUsuarioRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public List<RoleDTO> list() {
        return roleRepository.findAll().stream().map(this::toDTO).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO request) {
        Role role = new Role();
        role.setRol(request.getName().trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(roleRepository.save(role)));
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public RoleDTO update(@PathVariable Long roleId, @Valid @RequestBody RoleDTO request) {
        Role role = findRole(roleId);
        role.setRol(request.getName().trim());
        return toDTO(roleRepository.save(role));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long roleId) {
        Role role = findRole(roleId);
        if (userRepository.existsByRoleId(roleId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The role is assigned to users");
        }
        roleRepository.delete(role);
        return ResponseEntity.noContent().build();
    }

    private Role findRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO response = new RoleDTO();
        response.setRoleId(role.getId());
        response.setName(role.getRol());
        return response;
    }
}
