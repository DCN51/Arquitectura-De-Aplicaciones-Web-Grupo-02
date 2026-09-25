package pe.edu.upc.arquiwebgrupo02.servicesImplements;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repository.iUsuarioRepository;

import java.util.Locale;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    private final iUsuarioRepository usersRepository;

    public JWTUserDetailsService(iUsuarioRepository usersRepository) {
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

        GrantedAuthority authority = new SimpleGrantedAuthority(
                user.getRole().getRol().toUpperCase(Locale.ROOT)
        );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authority)
                .disabled(!user.isEnabled())
                .build();
    }
}
