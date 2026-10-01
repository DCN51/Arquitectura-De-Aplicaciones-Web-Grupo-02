package pe.edu.upc.arquiwebgrupo02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.arquiwebgrupo02.entities.Users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface IUsuarioRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByRoleId(Long roleId);
    boolean existsByCorreoElectronicoAndIdNot(String correoElectronico, Long id);

    @Query("select u from Users u " +
            "where (:roleId is null or u.role.id = :roleId) " +
            "and (:estadoCuenta is null or upper(u.estadoCuenta) = upper(:estadoCuenta))")
    List<Users> buscarPorRolYEstado(
            @Param("roleId") Long roleId,
            @Param("estadoCuenta") String estadoCuenta);
}
