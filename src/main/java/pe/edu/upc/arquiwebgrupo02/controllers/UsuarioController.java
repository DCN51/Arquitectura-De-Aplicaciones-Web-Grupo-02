package pe.edu.upc.arquiwebgrupo02.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.arquiwebgrupo02.dtos.RegistroUsuarioDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.RegistroUsuarioResponseDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.servicesInterfaces.iUserService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final iUserService usuarioService;

    public UsuarioController(iUserService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<RegistroUsuarioResponseDTO> registrar(
            @RequestBody RegistroUsuarioDTO registro) {
        Users user = usuarioService.registrar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegistroUsuarioResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().getRol()
                )
        );
    }
}
