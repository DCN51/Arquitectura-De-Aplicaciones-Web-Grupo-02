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
import pe.edu.upc.arquiwebgrupo02.securities.JwtTokenService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final IUserService uS;

    public LoginController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            IUserService uS) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.uS = uS;
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

        Users usuario = uS.usuarioActual(authentication);

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        usuario.getId(),
                        userDetails.getUsername(),
                        userDetails.getAuthorities().stream()
                                .map(authority -> authority.getAuthority())
                                .findFirst()
                                .orElseThrow(),
                        usuario.getNombres(),
                        usuario.getApellidos()
                )
        );
    }
}
