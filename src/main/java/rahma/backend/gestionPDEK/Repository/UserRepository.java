package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.DTO.Admin;
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

   @Query("SELECT u FROM User u WHERE u.role.nom = :roleName")
   List<User> findByRoleName(@Param("roleName") String roleName);

   @Query("SELECT COUNT(u) FROM User u WHERE u.role.nom = :typeOperation AND u.typeOperation = :typeOperation AND u.dateCreation LIKE CONCAT(:year, '-', :month, '%')")
   int countUsersByRoleAndMonth(@Param("role") String role,@Param("typeOperation") String typeOperation, @Param("year") String year, @Param("month") String month);

   @Query("SELECT FUNCTION('MONTH', FUNCTION('STR_TO_DATE', u.dateCreation, '%Y-%m-%d')), COUNT(u) " +
	       "FROM User u " +
	       "WHERE u.dateCreation IS NOT NULL " +
	       "AND u.typeAdmin = :typeAdmin " +
	       "AND FUNCTION('YEAR', FUNCTION('STR_TO_DATE', u.dateCreation, '%Y-%m-%d')) = :year " +
	       "AND u.role.nom = 'ADMIN' " +
	       "GROUP BY FUNCTION('MONTH', FUNCTION('STR_TO_DATE', u.dateCreation, '%Y-%m-%d')) " +
	       "ORDER BY FUNCTION('MONTH', FUNCTION('STR_TO_DATE', u.dateCreation, '%Y-%m-%d'))")
	List<Object[]> findMonthlyAdminCountByTypeAdmin(@Param("typeAdmin") TypeAdmin typeAdmin,
	                                                @Param("year") int year);


	@Query("SELECT u FROM User u WHERE u.dateCreation IS NOT NULL AND u.typeAdmin = :typeAdmin AND u.role.nom = 'ADMIN'")
	List<User> findAdminsByTypeAdmin(@Param("typeAdmin") TypeAdmin typeAdmin);

	
	
	List<User> findByRole_NomIn(List<String> noms);

}

