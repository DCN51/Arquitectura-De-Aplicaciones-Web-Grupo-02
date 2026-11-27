package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.arquiwebgrupo02.entities.Role;

public interface IRoleService {
    List<Role> list();

    Optional<Role> listId(Long id);

    Role insert(Role role);

    Role update(Long id, String nombre);

    void delete(Long id);
}
