package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;

public interface ICatalogoPlanRepository extends JpaRepository<CatalogoPlan, Long> {
    // NUEVO: el plan free activo (el que se asigna a todo paciente nuevo)
    @Query(value = "select * from catalogoplanes " +
            " where lower(nombre_catalogo_plan) = 'free' and activo_catalogo_plan = true" +
            " limit 1", nativeQuery = true)
    public CatalogoPlan buscarPlanFree();
    // HU011 - para registrar: ¿ya existe un plan con ese nombre?
    @Query(value = "select count(*) from catalogoplanes " +
            " where lower(nombre_catalogo_plan) = lower(:nombre)", nativeQuery = true)
    public int contarPorNombre(@Param("nombre") String nombre);

    // HU012 - para actualizar: igual, pero sin contar al mismo plan que se edita
    @Query(value = "select count(*) from catalogoplanes " +
            " where lower(nombre_catalogo_plan) = lower(:nombre)" +
            " and catalogo_plan_id <> :id", nativeQuery = true)
    public int contarPorNombreExcluyendoId(@Param("nombre") String nombre, @Param("id") Long id);
}
