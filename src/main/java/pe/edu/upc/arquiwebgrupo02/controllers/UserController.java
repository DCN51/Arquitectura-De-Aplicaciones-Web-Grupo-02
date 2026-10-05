package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.dtos.UserResponseDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.UserUpdateRequestDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(IUsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public List<UserResponseDTO> list(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String estadoCuenta) {
        List<Users> users = userRepository.buscarPorRolYEstado(roleId, hasText(estadoCuenta) ? estadoCuenta.trim() : null);
        return users.stream().map(this::toDTO).toList();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public UserResponseDTO get(@PathVariable Long userId) {
        return toDTO(findUser(userId));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public UserResponseDTO update(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDTO request) {
        Users user = findUser(userId);
        updateIfPresent(request, user);
        return toDTO(userRepository.save(user));
    }

    @PatchMapping("/{userId}/deactivate")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public UserResponseDTO deactivate(@PathVariable Long userId) {
        Users user = findUser(userId);
        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User account is already inactive");
        }
        user.setEstadoCuenta("INACTIVO");
        return toDTO(userRepository.save(user));
    }

    private void updateIfPresent(UserUpdateRequestDTO request, Users user) {
        if (hasText(request.getFirstName())) user.setNombres(request.getFirstName().trim());
        if (hasText(request.getLastName())) user.setApellidos(request.getLastName().trim());
        if (hasText(request.getEmail())) {
            String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
            if (userRepository.existsByCorreoElectronicoAndIdNot(email, user.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
            }
            user.setCorreoElectronico(email);
        }
        if (hasText(request.getPassword())) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (hasText(request.getPhone())) user.setTelefono(request.getPhone().trim());
        if (request.getBirthDate() != null) user.setFechaNacimiento(request.getBirthDate());
        if (hasText(request.getGender())) user.setGenero(request.getGender().trim());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private Users findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
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