package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import org.springframework.http.HttpStatus;
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
    public List<UserResponseDTO> list(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String accountStatus,
            Authentication authentication) {
        requireAdministrator(authentication);
        List<Users> users = roleId != null ? userRepository.findByRoleId(roleId)
                : accountStatus != null ? userRepository.findByEstadoCuentaIgnoreCase(accountStatus)
                : userRepository.findAll();
        if (roleId != null && accountStatus != null) {
            users = users.stream().filter(user -> accountStatus.equalsIgnoreCase(user.getEstadoCuenta())).toList();
        }
        return users.stream().map(this::toDTO).toList();
    }

    @GetMapping("/{userId}")
    public UserResponseDTO get(@PathVariable Long userId, Authentication authentication) {
        Users user = findUser(userId);
        requireOwnerOrAdministrator(authentication, user);
        return toDTO(user);
    }

    @PutMapping("/{userId}")
    public UserResponseDTO update(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDTO request,
            Authentication authentication) {
        Users user = findUser(userId);
        requireOwnerOrAdministrator(authentication, user);
        updateIfPresent(request, user);
        return toDTO(userRepository.save(user));
    }

    @PatchMapping("/{userId}/deactivate")
    public UserResponseDTO deactivate(@PathVariable Long userId, Authentication authentication) {
        requireAdministrator(authentication);
        Users user = findUser(userId);
        user.setEstadoCuenta("INACTIVO");
        return toDTO(userRepository.save(user));
    }

    private void updateIfPresent(UserUpdateRequestDTO request, Users user) {
        if (hasText(request.getFirstName())) user.setNombres(request.getFirstName().trim());
        if (hasText(request.getLastName())) user.setApellidos(request.getLastName().trim());
        if (hasText(request.getEmail())) {
            String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
            if (!email.equalsIgnoreCase(user.getCorreoElectronico()) && userRepository.existsByCorreoElectronico(email)) {
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

    private void requireOwnerOrAdministrator(Authentication authentication, Users user) {
        if (!user.getUsername().equals(authentication.getName()) && !isAdministrator(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this user");
        }
    }

    private void requireAdministrator(Authentication authentication) {
        if (!isAdministrator(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator role is required");
        }
    }

    private boolean isAdministrator(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMINISTRADOR")
                        || authority.getAuthority().equals("ROLE_ADMINISTRADOR"));
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
