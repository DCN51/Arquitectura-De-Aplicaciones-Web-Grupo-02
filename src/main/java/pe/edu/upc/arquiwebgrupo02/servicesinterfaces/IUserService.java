package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import org.springframework.security.core.Authentication;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.dtos.CreateUserRequestDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.UserUpdateRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Users registrar(CreateUserRequestDTO registro);
    void insert(Users usuario);
    List<Users> list();
    void update(Users usuario);
    public void delete(Long idUsuario);
    Optional<Users> listId(Long id);
    Users usuarioActual(Authentication authentication);
    Users buscarPorId(Long id);
    List<Users> listarPorRolYEstado(Long roleId, String estadoCuenta);
    Users actualizar(Users usuario, UserUpdateRequestDTO datos);
    Users desactivar(Long id);
    boolean esPaciente(Users usuario);
    boolean esPsicologo(Users usuario);
    boolean esAdministrador(Users usuario);
    String nombreCompleto(Users usuario);
}
