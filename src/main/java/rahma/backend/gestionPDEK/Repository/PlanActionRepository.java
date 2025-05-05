package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.PlanAction;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

@Repository
public interface PlanActionRepository extends JpaRepository<PlanAction, Long> {

	Optional<PlanAction> findByPagePDEK(PagePDEK pagePDEK);
	
	@Query("SELECT pa FROM PlanAction pa WHERE pa.pagePDEK.id = :pageId")
	Optional<PlanAction> findByPagePDEKId(@Param("pageId") Long pageId);

	 @Query("SELECT p FROM PlanAction p WHERE p.type_operation = :type")
	 List<PlanAction> getByTypeOperation(@Param("type") TypesOperation typeOperation);
	 
	List<PlanAction> findAllByPagePDEK(PagePDEK pagePDEK);
	
	/************* Statistiques all process sauf pistolet  **************/
	
	@Query(value = """
		    SELECT p.type_operation, COUNT(*)
		    FROM plan_action p
		    WHERE SUBSTRING(p.date_creation, 1, 4) = :year
		      AND p.type_pistolet IS NULL
		    GROUP BY p.type_operation
		    """, nativeQuery = true)
		List<Object[]> countPlanActionByTypeOperationForYear(@Param("year") String year);
		
		/************* Statistiques  pistolet  **************/
		
		@Query(value = """
			    SELECT p.type_pistolet, p.categorie_pistolet, COUNT(*)
			    FROM plan_action p
			    WHERE SUBSTRING(p.date_creation, 1, 4) = :year
			      AND p.type_pistolet IS NOT NULL
			      AND p.type_operation IS NOT NULL
			    GROUP BY p.type_pistolet, p.categorie_pistolet
			    """, nativeQuery = true)
			List<Object[]> countPlanActionByPistoletsForYear(@Param("year") String year);

}
