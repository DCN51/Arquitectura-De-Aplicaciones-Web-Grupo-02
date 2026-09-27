package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;

import java.util.List;

public interface ISuscripcionService {

    public void insert(Suscripcion s);                              // US05 - Contratar plan
    public List<Suscripcion> list();

    public void cambiarPlan(Suscripcion s);                         // US06 - Cambiar plan
    public void cancelar(Long usuarioId);                   // US07 - Cancelar suscripcion
    public List<Suscripcion> historialPorUsuario(Long usuarioId);

}
