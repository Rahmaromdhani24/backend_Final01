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
import rahma.backend.gestionPDEK.Entity.Soudure;
import rahma.backend.gestionPDEK.Entity.User;


@Repository
public interface SoudureRepository extends JpaRepository<Soudure, Long> {

	@Query("SELECT MAX(s.numeroCycle) FROM Soudure s WHERE s.pdekSoudure.id = :pdekId")
	Integer findLastCycleByPdekSoudure_Id(@Param("pdekId") Long pdekId);
	
	 List<Soudure> findByPdekSoudure_Id(Long pdekId);
	 
	   // Compter le nombre de SertissageIDC associés à une page donnée
	    long countByPagePDEK(PagePDEK pagePDEK);
	    
		/*@Query(value = "SELECT s.numero_cycle FROM soudure s WHERE s.pagepdek_id = :pageId ORDER BY s.numero_cycle DESC LIMIT 1", nativeQuery = true)
		Optional<Integer> findLastNumeroCycleByPage(@Param("pageId") Long pageId);
		
*/
	Optional<Soudure> findTopByPagePDEK_IdOrderByNumeroCycleDesc(Long pageId);
	List<Soudure> findByPdekSoudure_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);
	List<Soudure> findByDecision(int decision);
	List<Soudure> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);
    /********************* Modifier decision a 1 **********************************/
    @Modifying
    @Transactional
    @Query("UPDATE Soudure p SET p.decision = 1 WHERE p.id = :id")
    void validerSoudure(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE Soudure p SET p.rempliePlanAction = 0 WHERE p.id = :id")
    void ajoutPlanActionByChefLigne(@Param("id") Long id);
    
    
    @Query("SELECT p.pagePDEK FROM Soudure p WHERE p.id = :idSoudure")
    PagePDEK findPDEKByPagePDEK(@Param("idSoudure") Long idPistolet);
    @Query("SELECT DISTINCT p.userSoudure FROM Soudure p WHERE p.pdekSoudure.id = :idPdek")
    List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
    
    /************************* Statistiques *******************/
    long countByDateBetweenAndZoneNotNull(String startDate, String endDate);
    
    @Query("SELECT s.userSoudure , COUNT(s.zone) AS nombreErreurs " +
            "FROM Soudure s " +
            "WHERE s.zone IS NOT NULL " +
            "GROUP BY s.userSoudure " +
            "ORDER BY nombreErreurs DESC")
     List<Object[]> findTop3OperateursWithErrors();
     
     /******************* Statistiques principale  ***********************/
     @Query("SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(" +
    		  "'Soudure', s.sectionFil, p.numMachine, COUNT(DISTINCT p.id), " +
    	       "SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)) " +
    	       "FROM Soudure s " +
    	       "JOIN s.pdekSoudure p " +
    	       "WHERE p.plant = :plant " +
    	       "AND s.date LIKE CONCAT(:year, '%') " +
    	       "GROUP BY s.sectionFil, p.numMachine")
    	List<StatProcessus> getStatsSoudureByPlant(
    	    @Param("plant") Plant plant,
    	    @Param("year") String year
    	);
     /*********************** * Chef de ligne **********************************/

     
     @Query("""
    		    SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(
    		        'Soudure',
    		        s.sectionFil,
    		        p.numMachine,
    		         COUNT(DISTINCT p.id),
    		        SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)
    		    )
    		    FROM Soudure s
    		    JOIN s.pdekSoudure p     
    		    WHERE p.plant = :plant
    		      AND p.segment = :segment
    		    GROUP BY s.sectionFil, p.numMachine
    		    ORDER BY s.sectionFil, p.numMachine
    		""")
    		List<StatProcessus> findStatsByChefLigneSoudure(
    		    @Param("plant") Plant plant,
    		    @Param("segment") int segment
    		);


}