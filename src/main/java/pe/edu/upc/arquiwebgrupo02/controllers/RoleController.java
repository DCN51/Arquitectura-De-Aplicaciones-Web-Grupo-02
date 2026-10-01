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
import pe.edu.upc.arquiwebgrupo02.dtos.RoleDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public List<RoleDTO> list() {
        return roleService.list().stream().map(this::toDTO).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDTO(roleService.create(request.getName().trim())));
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public RoleDTO update(@PathVariable Long roleId, @Valid @RequestBody RoleDTO request) {
        return toDTO(roleService.update(roleId, request.getName().trim()));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO response = new RoleDTO();
        response.setRoleId(role.getId());
        response.setName(role.getRol());
        return response;
    }
}
