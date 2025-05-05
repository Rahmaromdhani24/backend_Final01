package rahma.backend.gestionPDEK;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ProjetRepository projetRepository;

    public DataInitializer(RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           UserRepository userRepository,
                           ProjetRepository projetRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.projetRepository = projetRepository;
    }

    @Override
    public void run(String... args) {
        // Initialisation des permissions
        Map<String, Permission> permissionsMap = new HashMap<>();
        List<String> permissionsList = List.of("READ", "WRITE", "INSERT", "DELETE");

        for (String permissionName : permissionsList) {
            Permission permission = permissionRepository.findByNom(permissionName).orElseGet(() -> {
                Permission newPermission = new Permission();
                newPermission.setNom(permissionName);
                return permissionRepository.save(newPermission);
            });
            permissionsMap.put(permissionName, permission);
        }

        // Définition des permissions par rôle
        Map<String, Set<Permission>> rolePermissions = new HashMap<>();
        rolePermissions.put("OPERATEUR", Set.of(permissionsMap.get("READ"), permissionsMap.get("WRITE")));
        rolePermissions.put("CHEF_DE_LIGNE", Set.of(permissionsMap.get("READ"), permissionsMap.get("WRITE")));
        rolePermissions.put("AGENT_QUALITE", Set.of(permissionsMap.get("READ"), permissionsMap.get("WRITE")));
        rolePermissions.put("TECHNICIEN", Set.of(permissionsMap.get("READ"), permissionsMap.get("WRITE")));
        rolePermissions.put("ADMIN", new HashSet<>(permissionsMap.values())); // Admin a toutes les permissions

        // Création des rôles avec leurs permissions
        for (Map.Entry<String, Set<Permission>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            Set<Permission> rolePerms = entry.getValue();

            Role role = roleRepository.findByNom(roleName).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setNom(roleName);
                newRole.setPermissions(rolePerms);
                return roleRepository.save(newRole);
            });

            // Création de l'admin s'il n'existe pas encore
            if (roleName.equals("ADMIN") && userRepository.findByEmail("admin@pdek.com").isEmpty()) {
                User admin = User.builder()
                        .matricule(1234)
                        .nom("Admin")
                        .prenom("Super")
                        .email("admin@pdek.com")
                        .poste("Administrateur")
                        .machine("N/A")
                        .role(role)
                        .build();
                userRepository.save(admin);
            }
        }

        // Initialisation des projets par plant
        if (projetRepository.count() == 0) {
            projetRepository.save(Projet.builder().nom("Projet 1 VW").plant(Plant.VW).build());
            projetRepository.save(Projet.builder().nom("Projet 2 VW").plant(Plant.VW).build());

            projetRepository.save(Projet.builder().nom("Projet 1 DS").plant(Plant.DS).build());
            projetRepository.save(Projet.builder().nom("Projet 2 DS").plant(Plant.DS).build());

            projetRepository.save(Projet.builder().nom("Projet 1 BM").plant(Plant.BM).build());
            projetRepository.save(Projet.builder().nom("Projet 2 BM").plant(Plant.BM).build());


            projetRepository.save(Projet.builder().nom("Projet 1 PORSCHE").plant(Plant.PORSCHE).build());
            projetRepository.save(Projet.builder().nom("Projet 2 PORSCHE").plant(Plant.PORSCHE).build());


            projetRepository.save(Projet.builder().nom("Projet 1 MLB").plant(Plant.MLB).build());
            projetRepository.save(Projet.builder().nom("Projet 2 MLB").plant(Plant.MLB).build());

        }
    }
}
