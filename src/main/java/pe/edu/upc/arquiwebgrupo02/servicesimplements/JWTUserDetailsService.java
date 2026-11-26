package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.securities.RolAuthority;

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

        GrantedAuthority authority = new SimpleGrantedAuthority(
                RolAuthority.normalizar(user.getRole().getRol())
        );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authority)
                .disabled(!user.isEnabled())
                .build();
    }
}
