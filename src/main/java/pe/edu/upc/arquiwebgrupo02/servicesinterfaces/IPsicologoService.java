package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.List;

public interface IPsicologoService {
    List<Users> listar();
    Users verificar(Long psicologoId);
    Users cambiarCapacidad(Long psicologoId, Integer maxPacientes);
}
