package rahma.backend.gestionPDEK.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateCreation;
    private String heureCreation;
    @Enumerated(EnumType.STRING)
    private TypesOperation type_operation;
    
    private String plant;
    private int segment ;
    private String  sectionFil ;
    private String poste;
    private String machine;
    private int numeroPistolet ;
    @Enumerated(EnumType.STRING)
    private TypePistolet typePistolet;
    private String categoriePistolet ; 
    // User qui a rempli le plan d'action
    @ManyToOne
    @JoinColumn(name = "user_id")  // La colonne de jointure dans la table PlanAction
    private User userPlanAction;


    // Page du PDEK concernée
    @OneToOne
    @JoinColumn(name = "page_pdek_id", unique = true)
    private PagePDEK pagePDEK;
    
    @OneToMany(mappedBy = "planAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailsPlanAction> details = new ArrayList();

}
