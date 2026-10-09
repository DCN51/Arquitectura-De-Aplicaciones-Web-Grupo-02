package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;

import java.util.Locale;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository usersRepository;

    public JWTUserDetailsService(IUsuarioRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado: " + username
                        )
                );

        String rol = user.getRole().getRol().toUpperCase(Locale.ROOT);
        String rolConPrefijo = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        new SimpleGrantedAuthority(rol),
                        new SimpleGrantedAuthority(rolConPrefijo)
                )
                .disabled(!user.isEnabled())
                .build();
    }
}
