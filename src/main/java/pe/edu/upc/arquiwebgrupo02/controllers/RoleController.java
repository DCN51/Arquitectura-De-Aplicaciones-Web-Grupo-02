package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.RoleDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    // Listar roles: público, porque lo usa la pantalla de Registro
    @GetMapping
    public List<RoleDTO> list() {
        return roleService.list().stream().map(this::toDTO).toList();
    }

    // Registrar rol
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> registrar(@Valid @RequestBody RoleDTO dto) {
        Role role = new Role();
        role.setRol(dto.getName());
        Role creado = roleService.insert(role);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(creado));
    }

    // Buscar rol por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> buscarPorId(@PathVariable Long id) {
        Role role = roleService.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe un rol con el id: " + id)
                );
        return ResponseEntity.ok(toDTO(role));
    }

    // Actualizar rol
    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> actualizar(@Valid @RequestBody RoleDTO dto) {
        Role existente = roleService.listId(dto.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe un rol con el id: " + dto.getRoleId())
                );
        existente.setRol(dto.getName());
        Role actualizado = roleService.update(existente);
        return ResponseEntity.ok(toDTO(actualizado));
    }

    // Eliminar rol
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO response = new RoleDTO();
        response.setRoleId(role.getId());
        response.setName(role.getRol());
        return response;
    }
}
