package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.*;

@Repository

public interface UserRepository extends JpaRepository<User , Integer> {

  Optional<User> findByMatricule(int matricule) ; 
  Optional<User> findByEmail(String email);
  Optional<User> findByRole(Role role);

  // List<User> findByRoleAndPlant(Role role, Plant plant);
   List<User> findByRoleAndPlantAndTypeOperationIsNull(Role role, Plant plant);
   List<User> findByRoleAndPlantAndSegment(Role role, Plant plant , int segment);
   
   /*************** Partie Statistiques ***************************************/
   long countByRoleNomIn(List<String> nomsRoles);
   long countByRoleNomInAndSexe(List<String> nomsRoles, String sexe);

   @Query("SELECT COUNT(u) FROM User u WHERE u.role.nom IN :roles AND SUBSTRING(u.dateCreation, 1, 4) = :annee")
   long compterParRoleEtAnnee(@Param("roles") List<String> roles, @Param("annee") String annee);

}
