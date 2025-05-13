package rahma.backend.gestionPDEK.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.DTO.Admin;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.RoleRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/superAdmin")
@CrossOrigin(origins = "http://localhost:4200") 
public class SuperAdminController {

	@Autowired  UserRepository userRepository   ; 
	@Autowired  RoleRepository roleRepository   ; 


	 @GetMapping("/plants")
	    public ResponseEntity<List<String>> getAllPlants() {
	        List<String> plants = Arrays.stream(Plant.values())
	                                    .map(Enum::name)
	                                    .collect(Collectors.toList());
	        return ResponseEntity.ok(plants);
	    }
	 

	 @PostMapping("/addUserAdmin")
	 public ResponseEntity<?> addUser(@RequestBody User user) {
	     if (userRepository.findById(user.getMatricule()).isPresent()) {
	         return ResponseEntity.status(HttpStatus.CONFLICT)
	                 .body("utilisateur avec ce matricule existe déjà !");
	     }

	     Optional<Role> role = roleRepository.findByNom("ADMIN");
	     if (role.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                 .body("Erreur : Rôle introuvable !");
	     }

	     // Affecter le rôle ADMIN
	     user.setRole(role.get());

	     // Ajouter la date de création (format yyyy-MM-dd)
	     user.setDateCreation(LocalDate.now().toString());

	     User savedUser = userRepository.save(user);
	     return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	 }


	 @GetMapping("/admins")
	 public ResponseEntity<List<Admin>> getAllAdmins() {
	     List<User> admins = userRepository.findByRoleName("ADMIN");

	     // Convertir List<User> en List<Admin> DTO
	     List<Admin> adminDTOs = admins.stream().map(user -> {
	         Admin admin = new Admin();
	         admin.setMatricule(user.getMatricule());
	         admin.setNom(user.getNom());
	         admin.setPrenom(user.getPrenom());
	         admin.setEmail(user.getEmail());
	         admin.setPlant(user.getPlant().toString());
	         admin.setSegment(user.getSegment());
	         admin.setGenre(user.getSexe());
	         admin.setNumeroTelephone(user.getNumeroTelephone()) ; 
	         admin.setTypeAdmin(user.getTypeAdmin().toString());
	         return admin;
	     }).toList();

	     return ResponseEntity.ok(adminDTOs);
	 }
	 @PutMapping("/updateUser/{matricule}")
	 public ResponseEntity<Admin> updateUser(@PathVariable int matricule, @RequestBody User userDetails) {
	     Optional<User> userOptional = userRepository.findById(matricule);
	     if (userOptional.isPresent()) {
	         User user = userOptional.get();
	         // Mise à jour des champs
	         user.setPlant(userDetails.getPlant());
	         user.setNom(userDetails.getNom());
	         user.setPrenom(userDetails.getPrenom());
	         user.setSegment(userDetails.getSegment());
	         user.setEmail(userDetails.getEmail());
	         user.setNumeroTelephone(userDetails.getNumeroTelephone());
	         user.setTypeAdmin(userDetails.getTypeAdmin()) ; 
	         user.setSexe(userDetails.getSexe());
	         User updatedUser = userRepository.save(user);
	         // Création de la réponse sous forme d'AdminDTO
	         Admin dto = new Admin();
	         dto.setMatricule(updatedUser.getMatricule());
	         dto.setNom(updatedUser.getNom());
	         dto.setPrenom(updatedUser.getPrenom());
	         dto.setEmail(updatedUser.getEmail());
	         dto.setPlant(updatedUser.getPlant().toString());
	         dto.setSegment(updatedUser.getSegment());
	         dto.setTypeAdmin(user.getTypeAdmin().toString()) ; 
	         dto.setGenre(updatedUser.getSexe());
	         dto.setNumeroTelephone(updatedUser.getNumeroTelephone());
	         return ResponseEntity.ok(dto);
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 }



	    @DeleteMapping("/deleteUser/{matricule}")
	    public ResponseEntity<String> deleteUser(@PathVariable int matricule) {
	        Optional<User> userOptional = userRepository.findById(matricule);
	        if (userOptional.isPresent()) {
	            userRepository.delete(userOptional.get());
	            return ResponseEntity.status(HttpStatus.OK).body("Utilisateur supprimé avec succès");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
	        }
	    }
	    @GetMapping("/getUser/{matricule}")
	    public ResponseEntity<?> getUser(@PathVariable int matricule) {
	        Optional<User> userOptional = userRepository.findById(matricule);

	        if (userOptional.isPresent()) {
	            User user = userOptional.get();

	            // Créer une réponse sous forme de Map (au lieu d'un DTO)
	            Map<String, Object> response = new HashMap<>();
	            response.put("matricule", user.getMatricule());
	            response.put("plant", user.getPlant());
	            response.put("nom", user.getNom());
	            response.put("prenom", user.getPrenom());
	            response.put("email", user.getEmail());
	            response.put("segment", user.getSegment());
	            response.put("numeroTelephone", user.getNumeroTelephone());
	            response.put("typeAdmin", user.getTypeAdmin());
	            response.put("sexe", user.getSexe());
	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));
	        }
	    }

	    @GetMapping("/count-admins")
	    public ResponseEntity<Integer> countAdmins() {
	        int count = userRepository.findByRoleName("ADMIN").size();
	        return ResponseEntity.ok(count);
	    }
	    
	    @GetMapping("/count-users/{typeAdmin}")
	    public ResponseEntity<Map<String, List<Integer>>> countUsersByTypeAdminAndYear(@PathVariable String typeAdmin) {
	        int currentYear = LocalDate.now().getYear();
	        TypeAdmin typeAdminEnum = TypeAdmin.valueOf(typeAdmin);
	        List<User> users = userRepository.findAdminsByTypeAdmin(typeAdminEnum);
	        Map<String, Integer> monthCount = new HashMap<>();
	        // Initialiser 12 mois à 0
	        for (int i = 1; i <= 12; i++) {
	            String frenchMonth = Month.of(i).getDisplayName(TextStyle.FULL, Locale.FRENCH);
	            monthCount.put(frenchMonth, 0); }
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        for (User user : users) {
	            try {
	                LocalDate date = LocalDate.parse(user.getDateCreation(), formatter);
	                if (date.getYear() == currentYear) {
	                    String frenchMonth = date.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
	                    monthCount.put(frenchMonth, monthCount.get(frenchMonth) + 1);
	                }
	            } catch (Exception e) {
	                // ignorer les dates invalides
	            }
	        }

	        // Conversion finale vers le format attendu
	        Map<String, List<Integer>> response = new LinkedHashMap<>();
	        for (int i = 1; i <= 12; i++) {
	            String frenchMonth = Month.of(i).getDisplayName(TextStyle.FULL, Locale.FRENCH);
	            response.put(frenchMonth, Collections.singletonList(monthCount.get(frenchMonth)));
	        }

	        return ResponseEntity.ok(response);
	    }



}
