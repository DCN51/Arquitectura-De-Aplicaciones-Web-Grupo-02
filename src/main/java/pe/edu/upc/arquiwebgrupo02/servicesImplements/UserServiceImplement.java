package pe.edu.upc.arquiwebgrupo02.servicesImplements;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.dtos.RegistroUsuarioDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Role;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.repository.iRoleRepository;
import pe.edu.upc.arquiwebgrupo02.repository.iUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesInterfaces.iUserService;

import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImplement implements iUserService {
    private final iUsuarioRepository usuarioRepository;
    private final iRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplement(
            iUsuarioRepository usuarioRepository,
            iRoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Users registrar(RegistroUsuarioDTO registro) {
        if (registro == null
                || isBlank(registro.getUsername())
                || isBlank(registro.getContrasena())
                || isBlank(registro.getNombres())
                || isBlank(registro.getApellidos())
                || isBlank(registro.getCorreoElectronico())
                || isBlank(registro.getTelefono())
                || registro.getFechaNacimiento() == null
                || isBlank(registro.getGenero())
                || registro.getRolId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Complete los datos obligatorios del registro"
            );
        }
        String username = registro.getUsername().trim();
        String email = registro.getCorreoElectronico().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya está registrado");
        }
        if (usuarioRepository.existsByCorreoElectronico(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo electrónico ya está registrado");
        }

        Role role = roleRepository.findById(registro.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol indicado no existe"));
        String roleName = role.getRol().toUpperCase(Locale.ROOT);
        boolean psychologist = roleName.equals("PSICOLOGO") || roleName.equals("ROLE_PSICOLOGO");
        boolean patient = roleName.equals("PACIENTE") || roleName.equals("ROLE_PACIENTE");
        if (!psychologist && !patient) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El rol debe ser PSICOLOGO o PACIENTE"
            );
        }
        if (psychologist && (isBlank(registro.getNumeroColegiatura())
                || isBlank(registro.getEspecializacion())
                || registro.getAnosExperiencia() == null
                || registro.getAnosExperiencia() < 0)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El registro de psicólogo requiere colegiatura, especialización y años de experiencia válidos"
            );
        }

        Users user = new Users();
        user.setUsername(username);
        user.setRole(role);
        user.setNombres(registro.getNombres().trim());
        user.setApellidos(registro.getApellidos().trim());
        user.setCorreoElectronico(email);
        user.setPassword(passwordEncoder.encode(registro.getContrasena()));
        user.setTelefono(registro.getTelefono().trim());
        user.setFechaNacimiento(registro.getFechaNacimiento());
        user.setGenero(registro.getGenero().trim());
        user.setNumeroColegiatura(emptyToNull(registro.getNumeroColegiatura()));
        user.setEspecializacion(emptyToNull(registro.getEspecializacion()));
        user.setAnosExperiencia(registro.getAnosExperiencia());
        return usuarioRepository.save(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String emptyToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    @Override
    public void insert(Users usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Users> list() {
        return usuarioRepository.findAll();
    }

    @Override
    public void update(Users usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
