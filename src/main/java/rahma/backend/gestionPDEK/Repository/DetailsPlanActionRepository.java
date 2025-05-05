package rahma.backend.gestionPDEK.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;

@Repository
public interface DetailsPlanActionRepository extends JpaRepository<DetailsPlanAction, Long> {

	List<DetailsPlanAction> findByPlanActionId(Long id);

	/******************* Statistiques ******************/
	@Query("""
		    SELECT COUNT(d)
		    FROM DetailsPlanAction d
		    WHERE d.dateCreation BETWEEN :startDate AND :endDate
		      AND d.numeroPistolet <> 0
		      AND d.typePistolet IS NOT NULL
		""")
		long countDetailsPlanActionBetweenDatesWithPistolet(
		    @Param("startDate") String startDate,
		    @Param("endDate") String endDate
		);

	/****************************************************/
	
	@Query("""
		    SELECT d.matricule_operateur, d.typePistolet, d.planAction.categoriePistolet, COUNT(*) AS nombreErreurs
		    FROM DetailsPlanAction d
		    WHERE d.numeroPistolet <> 0
		      AND d.typePistolet IS NOT NULL
		      AND SUBSTRING(d.dateCreation, 1, 4) = :year
		    GROUP BY d.matricule_operateur, d.typePistolet, d.planAction.categoriePistolet
		    ORDER BY nombreErreurs DESC
		""")
		List<Object[]> findTopOperateursPistoletWithErrors(@Param("year") int year);


		/**********************************************************************************/
		@Query("""
			    SELECT d.typePistolet,
			           d.planAction.categoriePistolet ,
			           COUNT(DISTINCT d.planAction.pagePDEK),  
			           COUNT(d)                                
			    FROM DetailsPlanAction d
			    WHERE d.numeroPistolet <> 0
			      AND d.typePistolet IS NOT NULL
			      AND d.planAction.plant = :plantName
			      AND d.planAction.pagePDEK IS NOT NULL
			    GROUP BY d.typePistolet
			""")
			List<Object[]> getStatsPistoletByTypeAndPlant(@Param("plantName") String plantName);

}
