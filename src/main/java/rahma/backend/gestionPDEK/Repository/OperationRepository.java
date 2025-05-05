package rahma.backend.gestionPDEK.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
