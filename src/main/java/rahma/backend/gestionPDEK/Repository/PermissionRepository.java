package rahma.backend.gestionPDEK.Repository;


import java.util.Optional;
import rahma.backend.gestionPDEK.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByNom(String name);
}

