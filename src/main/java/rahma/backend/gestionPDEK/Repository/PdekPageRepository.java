package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.PagePDEK;

@Repository
public interface PdekPageRepository extends JpaRepository<PagePDEK , Long> {

	Optional<PagePDEK> findTopByPdek_IdOrderByIdDesc(Long id);
    Optional<PagePDEK> findFirstByPdekOrderByPageNumberDesc(PDEK pdek);


    @Query("SELECT p FROM PagePDEK p WHERE p.pdek.id = :pdekId ORDER BY p.pageNumber DESC LIMIT 1")
    Optional<PagePDEK> findLastPageByPdek(@Param("pdekId") Long pdekId);

    Optional<PagePDEK> findPageActuelleByPdekId(Long pdekId);
   
    Optional<PagePDEK> findByPdekIdAndPageNumber(Long pdekId, int numeroPage);

    Optional<PagePDEK> findByPdekId(Long pdekId);
    
    Optional<PagePDEK> findById(Long pageID);
    
    List<PagePDEK> findAllByPdekId(Long pdekId);


}

	

