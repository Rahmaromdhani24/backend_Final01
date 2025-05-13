package rahma.backend.gestionPDEK.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.Configuration.ErrorResponse;
import rahma.backend.gestionPDEK.Configuration.JWTService;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200") 
public class AuthController {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    
    public AuthController(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    // Demande avec @RequestBody pour envoyer un JSON contenant le matricule
    @PostMapping("/loginOperateur")
    public ResponseEntity<?> loginOperateur(@RequestBody LoginRequest loginRequest) {
        try {
            // Recherche de l'utilisateur par matricule
            User user = userRepository.findByMatricule(loginRequest.getMatricule())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

            // Génération du token si utilisateur trouvé
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (ResourceNotFoundException ex) {
            // En cas d'exception, retourne une réponse 404 avec le message d'erreur personnalisé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userRepository.findByMatricule(loginRequest.getMatricule())
                    .orElseThrow(() -> new ResourceNotFoundException("MATRICULE_INVALIDE"));

            String role = user.getRole().getNom() ; 
            if (role == null || !List.of("SUPER_ADMIN" , "ADMIN", "CHEF_DE_LIGNE", "AGENT_QUALITE", "TECHNICIEN").contains(role.toUpperCase())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ROLE_NOT_AUTHORIZED");
            }


            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Matricule inexistant", "MATRICULE_INVALIDE"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur interne", "ERREUR_GENERALE"));
        }
    }


    // Classe de réponse pour le token
    static class AuthResponse {
        private String token;

        public AuthResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }

    // Classe de requête pour encapsuler le matricule dans le corps de la requête
    static class LoginRequest {
        private int matricule;

        public int getMatricule() { return matricule; }
        public void setMatricule(int matricule) { this.matricule = matricule; }
    }

    // Gestionnaire d'exceptions intégré pour personnaliser les erreurs
    @ResponseStatus(value = HttpStatus.NOT_FOUND)  // Cette annotation indique qu'une exception de type ResourceNotFound renverra un 404.
    static class ResourceNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
    
    /************************************    Recuperation info user****************************************************************/
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
            response.put("role", user.getRole().getNom());  // Juste le nom du rôle
            response.put("poste", user.getPoste());
            response.put("segment", user.getSegment());
            response.put("machine", user.getMachine());
            response.put("operation", user.getTypeOperation());
            response.put("sexe", user.getSexe());

            

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));
        }
    }

}
