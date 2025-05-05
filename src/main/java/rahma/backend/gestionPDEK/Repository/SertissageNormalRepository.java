package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.DTO.StatProcessus;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.SertissageNormal;
import rahma.backend.gestionPDEK.Entity.User;

@Repository

public interface SertissageNormalRepository extends JpaRepository<SertissageNormal, Long> {

    // Compter le nombre de SertissageIDC associés à une page donnée
    long countByPagePDEK(PagePDEK pagePDEK);

    // Trouver le dernier SertissageIDC sur une page donnée, trié par numCycle en ordre décroissant
    Optional<SertissageNormal> findTopByPagePDEKOrderByNumCycleDesc(PagePDEK pagePDEK);
    
     List<SertissageNormal> findByPdekSertissageNormal_Id(Long pdekId) ;
		// TODO Auto-generated method stub

     @Query("SELECT s.numCycle FROM SertissageNormal s WHERE s.pagePDEK.id = :pageId ORDER BY s.numCycle DESC LIMIT 1")
     Optional<Integer> findLastNumCycleByPage(@Param("pageId") Long pageId);
   /*******************************************************************************************/
    Optional<SertissageNormal> findTopByPagePDEK_IdOrderByNumCycleDesc(Long pageId);
	List<SertissageNormal> findByPdekSertissageNormal_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);
	List<SertissageNormal> findByDecision(int decision);
	List<SertissageNormal> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);
	 
   /********************* Modifier decision a 1 **********************************************/
	
	    @Modifying
	    @Transactional
	    @Query("UPDATE SertissageNormal p SET p.decision = 1 WHERE p.id = :id")
	    void validerSertissage(@Param("id") Long id);
	    
	    @Modifying
	    @Transactional
	    @Query("UPDATE SertissageNormal p SET p.rempliePlanAction = 0 WHERE p.id = :id")
	    void ajoutPlanActionByChefLigne(@Param("id") Long id);
	    
	    @Query("SELECT p.pagePDEK FROM SertissageNormal p WHERE p.id = :idSertissage")
	    PagePDEK findPDEKByPagePDEK(@Param("idSertissage") Long idSertissage);    
	    
	    @Query("SELECT DISTINCT p.userSertissageNormal FROM SertissageNormal  p WHERE p.pdekSertissageNormal.id = :idPdek")
	    List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
	    
    /************************* Statistiques *******************/
	    long countByDateBetweenAndZoneNotNull(String startDate, String endDate);

	    @Query("SELECT s.userSertissageNormal, COUNT(s.zone) AS nombreErreurs " +
	            "FROM SertissageNormal s " +
	            "WHERE s.zone IS NOT NULL " +
	            "GROUP BY s.userSertissageNormal " +
	            "ORDER BY nombreErreurs DESC")
	     List<Object[]> findTop3OperateursWithErrors();
	     
	     /******************* Statistiques principale  ***********************/
	     @Query("SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(" +
	    		  "'Sertissage', s.sectionFil, p.numMachine, COUNT(DISTINCT p.id), " +
	    	       "SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)) " +
	    	       "FROM SertissageNormal s " +
	    	       "JOIN s.pdekSertissageNormal p " +
	    	       "WHERE p.plant = :plant " +
	    	       "AND s.date LIKE CONCAT(:year, '%') " +
	    	       "GROUP BY s.sectionFil, p.numMachine")
	    	List<StatProcessus> getStatsSertissageNormalByPlant(
	    	    @Param("plant") Plant plant,
	    	    @Param("year") String year
	    	);

/*********************** * Chef de ligne **********************************/

	     
	     @Query("""
	    		    SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(
	    		        'Sertissage',
	    		        s.sectionFil,
	    		        p.numMachine,
	    		         COUNT(DISTINCT p.id),
	    		        SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)
	    		    )
	    		    FROM SertissageNormal s
	    		    JOIN s.pdekSertissageNormal p     
	    		    WHERE p.plant = :plant
	    		      AND p.segment = :segment
	    		    GROUP BY s.sectionFil, p.numMachine
	    		    ORDER BY s.sectionFil, p.numMachine
	    		""")
	    		List<StatProcessus> findStatsByChefLigneSertissageNormal(
	    		    @Param("plant") Plant plant,
	    		    @Param("segment") int segment
	    		);
}