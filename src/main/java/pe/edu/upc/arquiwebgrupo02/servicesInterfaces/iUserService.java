package pe.edu.upc.arquiwebgrupo02.servicesInterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.dtos.RegistroUsuarioDTO;

import java.util.List;

public interface iUserService {
    Users registrar(RegistroUsuarioDTO registro);
    void insert(Users usuario);
    List<Users> list();
    void update(Users usuario);
    public void delete(Long idUsuario);
}
