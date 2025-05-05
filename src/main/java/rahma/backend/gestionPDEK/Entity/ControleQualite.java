package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "controle_qualite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControleQualite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Qui a contrôlé
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Quel PDEK a été contrôlé
    @ManyToOne
    @JoinColumn(name = "pdek_id")
    private PDEK pdek;
    private long nombrePage ;  
    private long idInstanceOperation ; 
    private String dateControle;
    private String heureControle;
    private String resultat; 
}
