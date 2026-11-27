package pe.edu.upc.arquiwebgrupo02.controllers;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<RoleDTO> list() {
        return roleService.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/registro")
    public List<RoleDTO> listarParaRegistro() {
        return roleService.listarParaRegistro().stream().map(this::toDTO).toList();
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO response = new RoleDTO();
        response.setRoleId(role.getId());
        response.setName(role.getRol());
        return response;
    }
}
