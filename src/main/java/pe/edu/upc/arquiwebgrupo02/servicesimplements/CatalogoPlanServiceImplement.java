package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.repositories.ICatalogoPlanRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ICatalogoPlanService;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogoPlanServiceImplement implements ICatalogoPlanService {
    private final ICatalogoPlanRepository cpS;


    public CatalogoPlanServiceImplement(ICatalogoPlanRepository cpS) {
        this.cpS = cpS;
    }

    @Override
    public void insert(CatalogoPlan c) {
        cpS.save(c);
    }

    @Override
    public List<CatalogoPlan> list() {
        return cpS.findAll();
    }

    @Override
    public void update(CatalogoPlan c) {
        cpS.save(c);
    }

    @Override
    public void delete(Long catalogoPlanId) {
        cpS.deleteById(catalogoPlanId);
    }

    @Override
    public Optional<CatalogoPlan> listId(Long id) {
        return cpS.findById(id);
    }
}
