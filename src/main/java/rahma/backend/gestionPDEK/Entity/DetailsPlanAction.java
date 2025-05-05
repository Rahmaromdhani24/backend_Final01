package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class DetailsPlanAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dateCreation;
    private String heureCreation;

    @Lob
    @Column(length = 10000)
    private String description_probleme;

    @Lob
    @Column(length = 10000)
    private String delais;
    
    @Lob
    @Column(length = 10000)
    private String responsable;
    
    private int matricule_operateur;
    private int matricule_chef_ligne;

    @Lob
    @Column(length = 10000)
    private String description_decision;

    private int signature_qualite;
    private int signature_maintenance;
    private int signature_contermetre;

    @ManyToOne
    @JoinColumn(name = "plan_action_id")
    private PlanAction planAction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userPlanAction;

    private int numeroPistolet ;
    @Enumerated(EnumType.STRING)
    private TypePistolet typePistolet;
}

