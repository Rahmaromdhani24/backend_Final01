package rahma.backend.gestionPDEK.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.DTO.StatProcessus;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.SertissageIDC;
import rahma.backend.gestionPDEK.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SertissageIDCRepository extends JpaRepository<SertissageIDC, Long> {

    // Compter le nombre de SertissageIDC associés à une page donnée
    long countByPagePDEK(PagePDEK pagePDEK);

    // Trouver le dernier SertissageIDC sur une page donnée, trié par numCycle en ordre décroissant
    Optional<SertissageIDC> findTopByPagePDEKOrderByNumCycleDesc(PagePDEK pagePDEK);
    
     List<SertissageIDC> findByPdekSertissageIDC_Id(Long pdekId) ;
		// TODO Auto-generated method stub

     @Query("SELECT s.numCycle FROM SertissageIDC s WHERE s.pagePDEK.id = :pageId ORDER BY s.numCycle DESC LIMIT 1")
     Optional<Integer> findLastNumCycleByPage(@Param("pageId") Long pageId);

     /*******************************************************************************************/
    Optional<SertissageIDC> findTopByPagePDEK_IdOrderByNumCycleDesc(Long pageId);
    
	List<SertissageIDC> findByPdekSertissageIDC_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);


	 List<SertissageIDC> findByDecision(int decision);
	 List<SertissageIDC> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);
     
	 /********************* Modifier decision a 1 **********************************/
	    @Modifying
	    @Transactional
	    @Query("UPDATE SertissageIDC p SET p.decision = 1 WHERE p.id = :id")
	    void validerSertissageIDC(@Param("id") Long id);
	    
	    @Modifying
	    @Transactional
	    @Query("UPDATE SertissageIDC p SET p.rempliePlanAction = 0 WHERE p.id = :id")
	    void ajoutPlanActionByChefLigne(@Param("id") Long id);
	    
	    
	    @Query("SELECT p.pagePDEK FROM SertissageIDC p WHERE p.id = :idSertissageIDC")
	    PagePDEK findPDEKByPagePDEK(@Param("idSertissageIDC") Long idSertissageIDC);
	    @Query("SELECT DISTINCT p.userSertissageIDC FROM SertissageIDC p WHERE p.pdekSertissageIDC.id = :idPdek")
	    List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
	    
	    /************************* Statistiques *******************/
	    long countByDateBetweenAndZoneNotNull(String startDate, String endDate) ;
	    
	    @Query("SELECT s.userSertissageIDC, COUNT(s.zone) AS nombreErreurs " +
	            "FROM SertissageIDC s " +
	            "WHERE s.zone IS NOT NULL " +
	            "GROUP BY s.userSertissageIDC " +
	            "ORDER BY nombreErreurs DESC")
	     List<Object[]> findTop3OperateursWithErrors();
	     
	     /******************* Statistiques ***********************/
	     @Query("SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(" +
	    		  "'Sertissage IDC', s.sectionFil, p.numMachine, COUNT(DISTINCT p.id), " +
	    	       "SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)) " +
	    	       "FROM SertissageIDC s " +
	    	       "JOIN s.pdekSertissageIDC p " +
	    	       "WHERE p.plant = :plant " +
	    	       "AND s.date LIKE CONCAT(:year, '%') " +
	    	       "GROUP BY s.sectionFil, p.numMachine")
	    	List<StatProcessus> getStatsSertissageIDCByPlant(
	    	    @Param("plant") Plant plant,
	    	    @Param("year") String year
	    	);
/*********************** * Chef de ligne **********************************/

	     @Query("""
	    		    SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(
	    		        'Sertissage IDC',
	    		        s.sectionFil,
	    		        p.numMachine,
	    		        COUNT(DISTINCT p.id),
	    		        SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)
	    		    )
	    		    FROM SertissageIDC s
	    		    JOIN s.pdekSertissageIDC p     
	    		    WHERE p.plant = :plant
	    		      AND p.segment = :segment
	    		    GROUP BY s.sectionFil, p.numMachine
	    		    ORDER BY s.sectionFil, p.numMachine
	    		""")
	    		List<StatProcessus> findStatsByChefLigneSertissageIDC(
	    		    @Param("plant") Plant plant,
	    		    @Param("segment") int segment
	    		);
}