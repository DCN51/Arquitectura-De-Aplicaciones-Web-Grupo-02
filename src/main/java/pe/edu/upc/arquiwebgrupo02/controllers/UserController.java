package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.arquiwebgrupo02.dtos.UserResponseDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.UserUpdateRequestDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService uS;

    public UserController(IUserService uS) {
        this.uS = uS;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UserResponseDTO> list(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String estadoCuenta) {
        return uS.listarPorRolYEstado(roleId, estadoCuenta).stream().map(this::toDTO).toList();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO','ADMINISTRADOR')")
    public UserResponseDTO me(Authentication authentication) {
        return toDTO(uS.usuarioActual(authentication));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserResponseDTO get(@PathVariable Long userId) {
        return toDTO(uS.buscarPorId(userId));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO','ADMINISTRADOR')")
    public UserResponseDTO updateMe(
            @Valid @RequestBody UserUpdateRequestDTO request,
            Authentication authentication) {
        Users actual = uS.usuarioActual(authentication);
        return toDTO(uS.actualizar(actual, request));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserResponseDTO update(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDTO request) {
        return toDTO(uS.actualizar(uS.buscarPorId(userId), request));
    }

    @PatchMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserResponseDTO deactivate(@PathVariable Long userId) {
        return toDTO(uS.desactivar(userId));
    }

    private UserResponseDTO toDTO(Users user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRoleId(user.getRole().getId());
        response.setRoleName(user.getRole().getRol());
        response.setFirstName(user.getNombres());
        response.setLastName(user.getApellidos());
        response.setEmail(user.getCorreoElectronico());
        response.setPhone(user.getTelefono());
        response.setBirthDate(user.getFechaNacimiento());
        response.setGender(user.getGenero());
        response.setAccountStatus(user.getEstadoCuenta());
        return response;
    }
}
