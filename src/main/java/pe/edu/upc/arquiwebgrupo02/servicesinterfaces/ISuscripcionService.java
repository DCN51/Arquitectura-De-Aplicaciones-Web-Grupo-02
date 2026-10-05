package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;

import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import java.util.List;

public interface ISuscripcionService {

    public void insert(Suscripcion s);                              // US05 - Contratar plan

    public void cambiarPlan(Suscripcion s);                         // US06 - Cambiar plan
    public void cancelar(Long usuarioId);                           // US07 - Cancelar suscripcion
    public List<Suscripcion> historialPorUsuario(Long usuarioId);   // US08 - Consultar historial
    public List<Suscripcion> porVencer(int dias);                   // US09 - Suscripciones por vencer
    public List<Object[]> contarSuscriptoresActivosPorPlan();       // US10 - Suscriptores por plan

    public void asignarPlanFree(Users usuario);                     // NUEVO - plan free al registrarse
    public CatalogoPlan planActivoDe(Long usuarioId);               // NUEVO - plan vigente del usuario (o el free)
}