package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.User;


@Repository
public interface PistoletRepository extends JpaRepository<Pistolet, Long> {

	long countByPagePDEK(PagePDEK pagePDEK);

	Optional<User> findMaxNumeroCycleByPagePDEK(PagePDEK lastPage);
	
	 @Query("SELECT s.numeroCycle FROM Pistolet s WHERE s.pagePDEK.id = :pageId ORDER BY s.numeroCycle DESC LIMIT 1")
     Optional<Integer> findLastNumCycleByPage(@Param("pageId") Long pageId);

	 
	 Optional<Pistolet> findTopByPagePDEK_IdOrderByNumeroCycleDesc(Long pageId);
     List<Pistolet> findByPdekPistolet_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);
     List<Pistolet> findByDecision(int decision);
     List<Pistolet> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);

     /********************* Modifier decision a 1 **********************************/
     @Modifying
     @Transactional
     @Query("UPDATE Pistolet p SET p.decision = 1 WHERE p.id = :id")
     void validerPistolet(@Param("id") Long id);

     @Modifying
     @Transactional
     @Query("UPDATE Pistolet p SET p.rempliePlanAction = 0 WHERE p.id = :id")
     void ajoutPlanActionByTechnicien(@Param("id") Long id);

     Optional<Pistolet> findTopByNumeroPistoletAndTypeAndCategorieOrderByDateCreationDescHeureCreationDesc(
         int numeroPistolet,
         TypePistolet type,
         CategoriePistolet categorie
     );
     
     @Query("SELECT p.pagePDEK FROM Pistolet p WHERE p.id = :idPistolet")
     PagePDEK findPDEKByPagePDEK(@Param("idPistolet") Long idPistolet);
     @Query("SELECT DISTINCT p.userPistolet FROM Pistolet p WHERE p.pdekPistolet.id = :idPdek")
     List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
     
     /********************** Statistiques ***********************************/
     @Query(value = """
    		    SELECT pi.categorie, pi.type, COUNT(*) 
    		    FROM pistolet pi
    		    JOIN pdek p ON pi.pdek_id = p.id
    		    WHERE SUBSTRING(pi.date_creation, 1, 4) = :year
    		    GROUP BY pi.categorie, pi.type
    		""", nativeQuery = true)
    		List<Object[]> countPdekByCategorieAndCouleurForYear(@Param("year") String year);


} 
