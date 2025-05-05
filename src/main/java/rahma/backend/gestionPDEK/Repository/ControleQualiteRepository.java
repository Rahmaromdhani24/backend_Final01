package rahma.backend.gestionPDEK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.ControleQualite;

@Repository
public interface ControleQualiteRepository extends JpaRepository<ControleQualite, Long> {

    Optional<ControleQualite> findByPdek_IdAndIdInstanceOperation(Long pdekId, Long idInstanceOperation);

}
