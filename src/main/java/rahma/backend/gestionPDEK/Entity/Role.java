package rahma.backend.gestionPDEK.Entity;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String nom;

	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(
	        name = "roles_permissions",
	        joinColumns = @JoinColumn(name = "role_id"),
	        inverseJoinColumns = @JoinColumn(name = "permission_id"))
	    private Set<Permission> permissions = new HashSet<>();
	    
}
