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
import rahma.backend.gestionPDEK.Entity.Torsadage;
import rahma.backend.gestionPDEK.Entity.User;

@Repository

public interface TorsadageRepository extends JpaRepository<Torsadage, Long> {


	@Query("SELECT MAX(s.numeroCycle) FROM Torsadage s WHERE s.pdekTorsadage.id = :pdekId")
	Integer findLastCycleByPdekTorsadage_Id(@Param("pdekId") Long pdekId);
	
	 List<Torsadage> findByPdekTorsadage_Id(Long pdekId);
	 
	 long countByPagePDEK(PagePDEK pagePDEK);
	 
	 @Query("SELECT s.numeroCycle FROM Torsadage s WHERE s.pagePDEK.id = :pageId ORDER BY s.numeroCycle DESC LIMIT 1")
     Optional<Integer> findLastNumeroCycleByPage(@Param("pageId") Long pageId);

	 
	 Optional<Torsadage> findTopByPagePDEK_IdOrderByNumeroCycleDesc(Long pageId);
     List<Torsadage> findByPdekTorsadage_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);

	 List<Torsadage> findByDecision(int decision);
	 List<Torsadage> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);
	    /********************* Modifier decision a 1 **********************************/
	    @Modifying
	    @Transactional
	    @Query("UPDATE Torsadage p SET p.decision = 1 WHERE p.id = :id")
	    void validerTorsadage(@Param("id") Long id);
	    
	    @Modifying
	    @Transactional
	    @Query("UPDATE Torsadage p SET p.rempliePlanAction = 0 WHERE p.id = :id")
	    void ajoutPlanActionByChefLigne(@Param("id") Long id);
	    
	    
	    @Query("SELECT p.pagePDEK FROM Torsadage p WHERE p.id = :idTorsadage")
	    PagePDEK findPDEKByPagePDEK(@Param("idTorsadage") Long idTorsadage);
	    
	    @Query("SELECT DISTINCT p.userTorsadage FROM Torsadage p WHERE p.pdekTorsadage.id = :idPdek")
	    List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
     
	    /************************* Statistiques *******************/
	    long countByDateBetweenAndZoneNotNull(String startDate, String endDate);
	    @Query("SELECT s.userTorsadage , COUNT(s.zone) AS nombreErreurs " +
	            "FROM Torsadage s " +
	            "WHERE s.zone IS NOT NULL " +
	            "GROUP BY s.userTorsadage " +
	            "ORDER BY nombreErreurs DESC")
	     List<Object[]> findTop3OperateursWithErrors();
	     
	     
	     /******************* Statistiques agent de qualite  ***********************/
	     @Query("SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(" +
	    		  "'Torsadage', s.specificationMesure, p.numMachine, COUNT(DISTINCT p.id), " +
	    	       "SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)) " +
	    	       "FROM Torsadage s " +
	    	       "JOIN s.pdekTorsadage p " +
	    	       "WHERE p.plant = :plant " +
	    	       "AND s.date LIKE CONCAT(:year, '%') " +
	    	       "GROUP BY s.specificationMesure, p.numMachine")
	    	List<StatProcessus> getStatsTorsadageByPlant(
	    	    @Param("plant") Plant plant,
	    	    @Param("year") String year
	    	);

	     /************************ Chef de ligne **********************************/

	     
	     @Query("""
	    		    SELECT new rahma.backend.gestionPDEK.DTO.StatProcessus(
	    		        'Torsadage',
	    		        s.specificationMesure,
	    		        p.numMachine,
	    		        COUNT(DISTINCT p.id),
	    		        SUM(CASE WHEN s.zone IS NOT NULL THEN 1 ELSE 0 END)
	    		    )
	    		    FROM Torsadage s
	    		    JOIN s.pdekTorsadage p
	    		    WHERE p.plant = :plant AND p.segment = :segment
	    		    GROUP BY s.specificationMesure, p.numMachine
	    		    ORDER BY s.specificationMesure, p.numMachine
	    		""")
	    		List<StatProcessus> findStatsByChefLigneTorsadage(
	    		    @Param("plant") Plant plant,
	    		    @Param("segment") int segment
	    		);
}

