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

    // HU-04: usuarios por rol y estado de cuenta (ambos filtros opcionales)
    @Query(value = "select * from users u\n" +
            " where (cast(:roleId as bigint) is null or u.idrol = cast(:roleId as bigint))\n" +
            " and (cast(:estadoCuenta as varchar) is null or upper(u.account_status) = upper(cast(:estadoCuenta as varchar)))", nativeQuery = true)
    List<Users> buscarPorRolYEstado(
            @Param("roleId") Long roleId,
            @Param("estadoCuenta") String estadoCuenta);
}