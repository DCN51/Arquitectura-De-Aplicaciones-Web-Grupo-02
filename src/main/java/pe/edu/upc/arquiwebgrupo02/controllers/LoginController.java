package pe.edu.upc.arquiwebgrupo02.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.arquiwebgrupo02.dtos.LoginRequestDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.LoginResponseDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.securities.JwtTokenService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final IUsuarioRepository usuarioRepository;

    public LoginController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            IUsuarioRepository usuarioRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtTokenService.generateToken(userDetails);

        // Id del usuario que inicia sesión, para que el front no lo pida
        Long userId = usuarioRepository.findByUsername(userDetails.getUsername())
                .map(Users::getId)
                .orElse(null);

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        userDetails.getUsername(),
                        userDetails.getAuthorities().stream()
                                .map(authority -> authority.getAuthority())
                                .findFirst()
                                .orElseThrow(),
                        userId
                )
        );
    }
}
