package pe.edu.upc.arquiwebgrupo02.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.arquiwebgrupo02.dtos.CreateUserRequestDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.CreateUserResponseDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

@RestController
@RequestMapping("/usuarios")
public class RegistrerController {
    private final pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService usuarioService;

    public RegistrerController(pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<CreateUserResponseDTO> registrar(
            @RequestBody CreateUserRequestDTO registro) {
        Users user = usuarioService.registrar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateUserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().getRol()
                )
        );
    }
}
