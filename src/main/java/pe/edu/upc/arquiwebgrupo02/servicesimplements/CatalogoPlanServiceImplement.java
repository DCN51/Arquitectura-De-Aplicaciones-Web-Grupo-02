package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        if (cpS.contarPorNombre(c.getNombreCatalogoPlan()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un plan con ese nombre");
        }
        cpS.save(c);
    }

    @Override
    public void update(CatalogoPlan c) {
        if (cpS.contarPorNombreExcluyendoId(c.getNombreCatalogoPlan(), c.getCatalogoPlanId()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un plan con ese nombre");
        }
        cpS.save(c);
    }

    @Override
    public List<CatalogoPlan> list() {
        return cpS.findAll();
    }

    @Override
    public Optional<CatalogoPlan> listId(Long id) {
        return cpS.findById(id);
    }
}
