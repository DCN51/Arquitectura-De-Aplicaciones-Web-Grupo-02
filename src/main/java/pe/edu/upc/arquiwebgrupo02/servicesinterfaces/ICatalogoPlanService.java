package pe.edu.upc.arquiwebgrupo02.servicesinterfaces;


import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;

import java.util.List;
import java.util.Optional;

public interface ICatalogoPlanService {
    public void insert (CatalogoPlan c);
    public List<CatalogoPlan> list ();
    public void update (CatalogoPlan c);
    public void delete (Long catalogoPlanId);
    public Optional<CatalogoPlan> listId(Long id);

}
