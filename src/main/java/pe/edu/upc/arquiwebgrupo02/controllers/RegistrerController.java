package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.arquiwebgrupo02.dtos.CreateUserRequestDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.UserResponseDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

@RestController
@RequestMapping("/api/usuarios")
public class RegistrerController {
    private final pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService usuarioService;

    public RegistrerController(pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> registrar(
            @Valid @RequestBody CreateUserRequestDTO registro) {
        Users user = usuarioService.registrar(registro);
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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
