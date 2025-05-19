package rahma.backend.gestionPDEK.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operateur")
@CrossOrigin(origins = "http://localhost:4200") 
public class OperateurController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjetRepository projetRepository ;


    public OperateurController(UserRepository userRepository, RoleRepository roleRepository , ProjetRepository projetRepository  ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projetRepository = projetRepository ; 
    }
    
    @GetMapping("/getOperateur/{matricule}")
    public ResponseEntity<?> getUser(@PathVariable int matricule) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));}      
        User user = userOptional.get();
        // Vérifie si le rôle de l'utilisateur est bien OPERATEUR
        if (!user.getRole().getNom().equalsIgnoreCase("OPERATEUR")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Ce matricule ne représente pas un opérateur !")); }
        Map<String, Object> response = new HashMap<>();
        response.put("matricule", user.getMatricule());
        response.put("plant", user.getPlant());
        response.put("operation", user.getTypeOperation());
        response.put("nom", user.getNom());
        response.put("prenom", user.getPrenom());
        response.put("role", user.getRole().getNom());
        response.put("poste", user.getPoste());
        response.put("segment", user.getSegment());
        response.put("machine", user.getMachine());
        return ResponseEntity.ok(response);
    }
    
   @GetMapping("/projets/{plantName}")
public ResponseEntity<?> getProjetsByPlant(@PathVariable String plantName) {
    try {
        Plant plant = Plant.valueOf(plantName.toUpperCase());
        List<String> projetNames = projetRepository.findByPlant(plant).stream()
                .map(Projet::getNom)  // Map chaque projet à son nom
                .collect(Collectors.toList());  // Collecter dans une liste
        return ResponseEntity.ok(projetNames);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Plant invalide : " + plantName);
    }
}
@GetMapping("AgentQualiteParPlant")
public ResponseEntity<List<UserDTO>> getAgentsQualiteByPlant(@RequestParam String nomPlant) {
    Role agentQualiteRole = roleRepository.findByNom("AGENT_QUALITE")
                                          .orElseThrow(() -> new RuntimeException("Role not found"));

    Plant plant;
    try {
        plant = Plant.valueOf(nomPlant); // Cela va convertir la chaîne en l'énumération correspondante
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid Plant name: " + nomPlant);
    }

    List<User> agents = userRepository.findByRoleAndPlantAndTypeOperationIsNull(agentQualiteRole, plant);

    List<UserDTO> dtos = agents.stream()
            .map(UserDTO::fromEntity)
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}
@GetMapping("ChefLigneParPlantEtSegment")
public ResponseEntity<List<UserDTO>> getAgentsQualiteByPlantEtSegment(@RequestParam String nomPlant 
                                                                     , @RequestParam int segment
                                                                     , @RequestParam String operation) {
    Role chefDeLigne = roleRepository.findByNom("CHEF_DE_LIGNE")
                                          .orElseThrow(() -> new RuntimeException("Role not found"));

    Plant plant;
    try {
        plant = Plant.valueOf(nomPlant); // Cela va convertir la chaîne en l'énumération correspondante
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid Plant name: " + nomPlant);
    }

    @SuppressWarnings("unused")
	TypesOperation typeOperation  ; 
    try {
    	typeOperation = TypesOperation.valueOf(operation); // Cela va convertir la chaîne en l'énumération correspondante
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid nom type operation : " + operation );
    }

    List<User> agents = userRepository.findByRoleAndPlantAndSegment(chefDeLigne, plant , segment);

    List<UserDTO> dtos = agents.stream()
            .map(UserDTO::fromEntity)
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}
@GetMapping("TechniciensParPlantEtSegment")
public ResponseEntity<List<UserDTO>> getTechniciensByPlantEtSegment(@RequestParam String nomPlant 
                                                                     , @RequestParam int segment
                                                                     , @RequestParam String operation) {
    Role technicien = roleRepository.findByNom("TECHNICIEN")
                                          .orElseThrow(() -> new RuntimeException("Role not found"));

    Plant plant;
    try {
        plant = Plant.valueOf(nomPlant); // Cela va convertir la chaîne en l'énumération correspondante
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid Plant name: " + nomPlant);
    }

    @SuppressWarnings("unused")
    TypesOperation typeOperation  ; 
    try {
    	typeOperation = TypesOperation.valueOf(operation); // Cela va convertir la chaîne en l'énumération correspondante
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid nom type operation : " + operation );
    }

    List<User> techniciens = userRepository.findByRoleAndPlantAndSegment(technicien, plant , segment);

    List<UserDTO> dtos = techniciens.stream()
            .map(UserDTO::fromEntity)
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}

@GetMapping("/verifier-agent-qualite")
public ResponseEntity<Boolean> verifierAgentQualite(
        @RequestParam int matricule,
        @RequestParam String plant) {

    boolean estValide = userRepository.findByMatricule(matricule)
            .filter(utilisateur -> "AGENT_QUALITE".equals(utilisateur.getRole().getNom()))
            .filter(utilisateur -> {
                try {
                    Plant plantEnum = Plant.valueOf(plant.toUpperCase());
                    return utilisateur.getPlant() == plantEnum;
                } catch (IllegalArgumentException e) {
                    return false; // Le plant fourni n'existe pas dans l'enum
                }
            })
            .isPresent();

    return ResponseEntity.ok(estValide);
}

}