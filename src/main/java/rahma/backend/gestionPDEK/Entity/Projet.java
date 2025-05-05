package rahma.backend.gestionPDEK.Entity;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private Plant plant;
    
    @ManyToMany(mappedBy ="projets")  // Relation avec plusieurs PDEK
    private List<PDEK> pdeks = new ArrayList<>();
}
