package pe.edu.upc.arquiwebgrupo02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.Optional;

public interface iUsuarioRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByCorreoElectronico(String correoElectronico);
}
