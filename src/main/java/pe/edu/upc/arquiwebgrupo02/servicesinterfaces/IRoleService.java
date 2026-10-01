package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import java.util.List;
import pe.edu.upc.arquiwebgrupo02.entities.Role;

public interface IRoleService {
    List<Role> list();
    Role create(String rol);
    Role update(Long roleId, String rol);
    void delete(Long roleId);
}
