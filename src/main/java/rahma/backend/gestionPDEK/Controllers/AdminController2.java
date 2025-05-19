package rahma.backend.gestionPDEK.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.EmailSenderPistoletService;
import rahma.backend.gestionPDEK.EmailSenderService;
import rahma.backend.gestionPDEK.EmailSenderTorsadageService;
import rahma.backend.gestionPDEK.Configuration.EmailPistoletRequest;
import rahma.backend.gestionPDEK.Configuration.EmailRequest;
import rahma.backend.gestionPDEK.Configuration.EmailValidationPDEK;
import rahma.backend.gestionPDEK.DTO.Admin;
import rahma.backend.gestionPDEK.DTO.Operateur;
import rahma.backend.gestionPDEK.DTO.ProjetDTO;
import rahma.backend.gestionPDEK.DTO.Users;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin2")
@CrossOrigin(origins = "http://localhost:4200") 
public class AdminController2 {


	@Autowired  UserRepository userRepository   ; 
	@Autowired  RoleRepository roleRepository   ; 
	@Autowired  ProjetRepository projetRepository ;
	@Autowired  OutilsContactRepository outilContactRepository ; 
	
	 @PostMapping("/addOperateur")
	 public ResponseEntity<?> addOperateur(@RequestBody User user) {
	     if (userRepository.findById(user.getMatricule()).isPresent()) {
	         return ResponseEntity.status(HttpStatus.CONFLICT)
	                 .body("utilisateur avec ce matricule existe déjà !");
	     }
	     Optional<Role> role = roleRepository.findByNom("OPERATEUR");
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

	
	
    @GetMapping("/getOperateur/{matricule}")
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
            response.put("poste", user.getPoste());
            response.put("segment", user.getSegment());
            response.put("machine", user.getMachine());
            response.put("typeOperation", user.getTypeOperation());
            response.put("numeroTelephone", user.getNumeroTelephone());
            response.put("sexe", user.getSexe());


            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));
        }
    }

    @GetMapping("/user/{matricule}")
    public ResponseEntity<?> getUtilisateur(@PathVariable int matricule) {
        Optional<User> userOptional = userRepository.findById(matricule);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Créer une réponse sous forme de Map (au lieu d'un DTO)
            Map<String, Object> response = new HashMap<>();
            response.put("matricule", user.getMatricule());
            response.put("plant", user.getPlant());
            response.put("segment", user.getSegment());
            response.put("nom", user.getNom());
            response.put("prenom", user.getPrenom());        
            response.put("poste", user.getPoste());
            response.put("segment", user.getSegment());
            response.put("typeOperation", user.getTypeOperation());
            response.put("numeroTelephone", user.getNumeroTelephone());
            response.put("sexe", user.getSexe());
            response.put("role", user.getRole().getNom());
            response.put("email", user.getEmail());


            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));
        }
    }
    
    @PutMapping("/updateUtilisateur/{matricule}")
    public ResponseEntity<Users> updateUtilisateur(@PathVariable int matricule, @RequestBody Users userDetails) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Role> roleOpt = roleRepository.findByNom(userDetails.getRole());
            if (roleOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setSegment(userDetails.getSegment());
            user.setNumeroTelephone(userDetails.getNumeroTelephone());
            user.setTypeOperation(TypesOperation.valueOf(userDetails.getTypeOperation()));
            user.setSexe(userDetails.getSexe());
            user.setEmail(userDetails.getEmail());
            user.setPlant(Plant.valueOf(userDetails.getPlant()));
            user.setRole(roleOpt.get());

            User updatedUser = userRepository.save(user);

            Users dto = new Users();
            dto.setMatricule(updatedUser.getMatricule());
            dto.setNom(updatedUser.getNom());
            dto.setPrenom(updatedUser.getPrenom());
            dto.setPlant(updatedUser.getPlant().toString());
            dto.setSegment(updatedUser.getSegment());
            dto.setTypeOperation(updatedUser.getTypeOperation().toString());
            dto.setSexe(updatedUser.getSexe());
            dto.setNumeroTelephone(updatedUser.getNumeroTelephone());
            dto.setEmail(updatedUser.getEmail());
            dto.setRole(updatedUser.getRole().getNom());

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/updateOperateur/{matricule}")
	 public ResponseEntity<Operateur> updateUser(@PathVariable int matricule, @RequestBody User userDetails) {
	     Optional<User> userOptional = userRepository.findById(matricule);
	     if (userOptional.isPresent()) {
	         User user = userOptional.get();
	         // Mise à jour des champs
	         user.setPlant(userDetails.getPlant());
	         user.setNom(userDetails.getNom());
	         user.setPrenom(userDetails.getPrenom());
	         user.setSegment(userDetails.getSegment());
	         user.setNumeroTelephone(userDetails.getNumeroTelephone());
	         user.setTypeOperation(userDetails.getTypeOperation())  ; 
	         user.setSexe(userDetails.getSexe());
	         user.setMachine(userDetails.getMachine());
	         user.setPoste(userDetails.getPoste());
	         User updatedUser = userRepository.save(user);
	         // Création de la réponse sous forme d'AdminDTO
	         Operateur dto = new Operateur();
	         dto.setMatricule(updatedUser.getMatricule());
	         dto.setNom(updatedUser.getNom());
	         dto.setPrenom(updatedUser.getPrenom());
	         dto.setPlant(updatedUser.getPlant().toString());
	         dto.setSegment(updatedUser.getSegment());
	         dto.setTypeOperation(user.getTypeOperation().toString()) ; 
	         dto.setGenre(updatedUser.getSexe());
	         dto.setNumeroTelephone(updatedUser.getNumeroTelephone());
	         dto.setMachine(updatedUser.getMachine());
	         dto.setPoste(updatedUser.getPoste());
	         return ResponseEntity.ok(dto);
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 } 
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();

        // Convertir la liste d'utilisateurs en liste de Map
        List<Map<String, Object>> userResponses = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("matricule", user.getMatricule());
            userMap.put("opération", user.getTypeOperation());
            userMap.put("plant", user.getPlant());
            userMap.put("nom", user.getNom());
            userMap.put("prenom", user.getPrenom());
            userMap.put("role", user.getRole().getNom());  // Juste le nom du rôle
            userMap.put("poste", user.getPoste());
            userMap.put("segment", user.getSegment());
            userMap.put("machine", user.getMachine());
            userMap.put("sexe", user.getSexe());

            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody Users dto) {
        if (userRepository.findById(dto.getMatricule()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Utilisateur avec ce matricule existe déjà !");
        }

        Optional<Role> role = roleRepository.findByNom(dto.getRole());
        if (role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : Rôle introuvable !");
        }

        User.UserBuilder builder = User.builder()
                .matricule(dto.getMatricule())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .role(role.get())
                .sexe(dto.getSexe())
                .email(dto.getEmail())
                .numeroTelephone(dto.getNumeroTelephone())
                .plant(Plant.valueOf(dto.getPlant()))
                .dateCreation(LocalDate.now().toString());

        // Ajouter le segment s'il est fourni
        if (dto.getSegment() != 0) {
            builder.segment(dto.getSegment());
        }

        // Ajouter le typeOperation s'il est fourni
        if (dto.getTypeOperation() != null && !dto.getTypeOperation().isBlank()) {
            try {
                builder.typeOperation(TypesOperation.valueOf(dto.getTypeOperation()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Type d'opération invalide !");
            }
        }

        User user = builder.build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }


@GetMapping("/getUtilisateursSaufOperateur")
public List<Users> getUsersByRoleNames() {
    List<String> roles = Arrays.asList("CHEF_DE_LIGNE", "AGENT_QUALITE", "TECHNICIEN");
    return userRepository.findByRole_NomIn(roles).stream()
            .map(user -> new Users(
                    user.getMatricule(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getEmail(),
                    user.getPlant() != null ? user.getPlant().toString() : null,
                    user.getSegment(),
                    user.getNumeroTelephone(),
                    user.getTypeOperation() != null ? user.getTypeOperation().toString() : null,
                    user.getSexe(),
                    user.getRole().getNom()))
            .toList();
}

    @GetMapping("/getOperateurs")
    public List<Operateur> getOperateurs() {
        List<String> roles = Arrays.asList("OPERATEUR");
        return userRepository.findByRole_NomIn(roles).stream()
                .map(user -> new Operateur(
                        user.getMatricule(),
                        user.getNom(),
                        user.getPrenom(),
                        user.getPlant().toString() ,
                        user.getSegment() , 
                        user.getSexe() ,
                        user.getNumeroTelephone() ,
                        user.getTypeOperation().toString() ,         
                        user.getMachine()  ,
                        user.getPoste()
                ))
                .toList();
    }
    /***************** Projets **************/
    @GetMapping("/getProjets")
    public List<ProjetDTO> getAllProjets() {
        List<Projet> projets = projetRepository.findAll();
        return projets.stream().map(p -> new ProjetDTO(
            p.getId(),
            p.getNom(),
            p.getPlant().toString() ,
            p.getPdeks().size() ,
            p.getDateCreation() 
        )).collect(Collectors.toList());
    }

    @GetMapping("/projet/{id}")
    public ProjetDTO getProjetParId(@PathVariable long id) {
        Projet projets = projetRepository.findById(id).get() ; 
        return new ProjetDTO(
        		projets.getId(),
        		projets.getNom(),
        		projets.getPlant().toString() ,
        		projets.getPdeks().size() ,
        		projets.getDateCreation() 
        );
    }


    // Ajouter un projet
    @PostMapping
    public Projet createProjet(@RequestBody Projet projet) {
        return projetRepository.save(projet);
    }
    
    @PutMapping("/projet/{id}")
    public ResponseEntity<Projet> updateProjet(@PathVariable Long id, @RequestBody Projet updatedProjet) {
        Optional<Projet> optionalProjet = projetRepository.findById(id);
        if (optionalProjet.isPresent()) {
            Projet projet = optionalProjet.get();
            projet.setNom(updatedProjet.getNom());
            projet.setPlant(updatedProjet.getPlant());
            return ResponseEntity.ok(projetRepository.save(projet));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE : supprimer un projet
    @DeleteMapping("/projet/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        if (projetRepository.existsById(id)) {
            projetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/outilContactAdd")
    public ResponseEntity<OutilContact> addOutilContact(@RequestBody OutilContact outilContact) {
        OutilContact savedOutil = outilContactRepository.save(outilContact);
        return ResponseEntity.ok(savedOutil);
    }
}
