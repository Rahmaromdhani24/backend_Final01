package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.OutilContact; 

@Repository
public interface OutilsContactRepository extends JpaRepository<OutilContact, Long> {
	
	/// pour recuperation liste des contacts depuis un numero d'outil 
	 @Query("SELECT o.numeroContact FROM OutilContact o WHERE o.numeroOutil = :numeroOutil")
	 List<String> findContactsByNumeroOutil(@Param("numeroOutil") String numeroOutil);
	 
	 /// permet de recuperer liste des sections des fils depuis un numero d'otuil et numero de contact 
	 @Query("SELECT o FROM OutilContact o WHERE o.numeroOutil = :numeroOutil AND o.numeroContact = :numeroContact")
	 List<OutilContact> findSectionsFilByOutilAndContact(@Param("numeroOutil") String numeroOutil, @Param("numeroContact") String numeroContact);
    
	 /// Récuperer section de fil depuis un num outil , num contact et une section de fil 
	    Optional<OutilContact> findByNumeroOutilAndNumeroContactAndSectionFil(String numeroOutil, String numeroContact, String sectionFil);

	

}
