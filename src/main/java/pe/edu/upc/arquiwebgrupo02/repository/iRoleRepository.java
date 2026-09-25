package pe.edu.upc.arquiwebgrupo02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.Role;

public interface iRoleRepository extends JpaRepository<Role, Long> {
}
