package rahma.backend.gestionPDEK.Repository;


import java.util.Optional;
import rahma.backend.gestionPDEK.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(String name);
}

