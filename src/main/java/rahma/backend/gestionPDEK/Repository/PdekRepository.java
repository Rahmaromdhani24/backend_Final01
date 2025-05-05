package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.*;

@Repository

public interface PdekRepository extends JpaRepository<PDEK , Long> {

	@Query("SELECT p FROM PDEK p WHERE p.sectionFil = :sectionFilSelectionne " +
		       "AND p.typeOperation = 'Soudure' " +
		       "AND p.segment = :segment " +
		       "AND p.plant = :nomPlant " +
		       "AND EXISTS (SELECT pr FROM Projet pr JOIN pr.pdeks pp WHERE pr.nom = :projetName AND pp.id = p.id)")
		Optional<PDEK> findUniquePDEK_SoudureUtrason(
		    @Param("sectionFilSelectionne") String sectionFilSelectionne, 
		    @Param("segment") int segment, 
		    @Param("nomPlant") Plant nomPlant,
		    @Param("projetName") String projetName
		);


	@Query("SELECT p FROM PDEK p WHERE p.sectionFil = :specificationMesurer " +
		       "AND p.typeOperation = 'Torsadage' " +
		       "AND p.segment = :segment " +
		       "AND p.plant = :nomPlant " +
		       "AND EXISTS (SELECT pr FROM Projet pr JOIN pr.pdeks pp WHERE pr.nom = :projetName AND pp.id = p.id)")
		Optional<PDEK> findUniquePDEK_Torsadage(
		    @Param("specificationMesurer") String specificationMesurer, 
		    @Param("segment") int segment, 
		    @Param("nomPlant") Plant nomPlant,
		    @Param("projetName") String projetName
		);
	
	@Query("SELECT p FROM PDEK p WHERE p.sectionFil = :sectionFilSelectionne " +
		       "AND p.typeOperation = 'Sertissage_IDC' " +
		       "AND p.segment = :segment " +
		       "AND p.plant = :nomPlant " +
		       "AND EXISTS (SELECT pr FROM Projet pr JOIN pr.pdeks pp WHERE pr.nom = :projetName AND pp.id = p.id)")
		Optional<PDEK> findUniquePDEK_SertissageIDC(
		    @Param("sectionFilSelectionne") String sectionFilSelectionne, 
		    @Param("segment") int segment, 
		    @Param("nomPlant") Plant nomPlant,
		    @Param("projetName") String projetName
		);

	
	@Query("SELECT p FROM PDEK p WHERE p.sectionFil = :sectionFilSelectionne " +
		       "AND p.typeOperation = 'Sertissage_Normal' " +
		       "AND p.segment = :segment " +
		       "AND p.plant = :nomPlant " +
		       "AND EXISTS (SELECT pr FROM Projet pr JOIN pr.pdeks pp WHERE pr.nom = :projetName AND pp.id = p.id)")
		Optional<PDEK> findUniquePDEK_SertissageNormal(
		    @Param("sectionFilSelectionne") String sectionFilSelectionne, 
		    @Param("segment") int segment, 
		    @Param("nomPlant") Plant nomPlant,
		    @Param("projetName") String projetName
		);

   // Optional<PDEK> findByTypePistolet(TypePistolet typePistolet);

	@Query("SELECT p FROM PDEK p WHERE " +
		       "p.typeOperation = 'montage_Pistolet' " +
		       "AND p.typePistolet = :typePistolet " +
		       "AND p.segment = :segment " +
		       "AND p.numeroPistolet = :numeroPistolet " +
		       "AND p.categoriePistolet = :categoriePistolet " +
		       "AND p.plant = :nomPlant")
		Optional<PDEK> findUniquePDEK_MontagePistolet(
		    @Param("typePistolet") TypePistolet typePistolet,
		    @Param("segment") int segment,
		    @Param("numeroPistolet") int numeroPistolet,
		    @Param("categoriePistolet") CategoriePistolet categoriePistolet,
		    @Param("nomPlant") Plant nomPlant
		);
	
	Optional<PDEK> findByTypePistoletAndCategoriePistoletAndNumeroPistolet(
		    TypePistolet typePistolet, 
		    CategoriePistolet categoriePistolet, 
		    int numeroPistolet
		);
	
	List<PDEK> findByTypeOperation(TypesOperation typeOperation);

	/********************** Statistiques *************************/
	@Query(value = "SELECT p.type_operation, COUNT(*) FROM pdek p " +
            "WHERE SUBSTRING(p.date_creation, 1, 4) = :year " +
            "GROUP BY p.type_operation", nativeQuery = true)
   List<Object[]> countPdekByTypeOperationForYear(@Param("year") String year);

}
