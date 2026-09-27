package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;

public interface ICatalogoPlanRepository extends JpaRepository<CatalogoPlan, Long> {
}
