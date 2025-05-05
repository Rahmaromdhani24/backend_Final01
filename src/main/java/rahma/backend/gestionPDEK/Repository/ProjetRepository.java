package rahma.backend.gestionPDEK.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.Projet;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
	
	// recupere liste dess projets d'un pdek bien determiner 
	 @Query("SELECT p FROM Projet p JOIN p.pdeks pd WHERE pd.id = :pdekId")
	 List<Projet> findProjetsByPdekId(@Param("pdekId") Long pdekId);
	 
    List<Projet> findByPlant(Plant plant);
    Optional<Projet> findByNomAndPlant(String nom, Plant plant);
    Optional<Projet> findByNom(String nom);


    
}