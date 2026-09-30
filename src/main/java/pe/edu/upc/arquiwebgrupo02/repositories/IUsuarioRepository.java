package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.Optional;
import java.util.List;

public interface IUsuarioRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByRoleId(Long roleId);
    List<Users> findByRoleId(Long roleId);
    List<Users> findByEstadoCuentaIgnoreCase(String accountStatus);
}
