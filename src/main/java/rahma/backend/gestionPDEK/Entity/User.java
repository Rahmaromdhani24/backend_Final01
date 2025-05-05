package rahma.backend.gestionPDEK.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "matricule")}) 
public class User {
	@Id
	  private int matricule ; 
	  private String nom;
	  private String prenom;
	  private String sexe;
	  private String poste ;
	  private int segment ;
	  private String machine ;
	  private String email;
	  @Column(name = "date_creation")
	  private String dateCreation  ; 
	  
	  @Enumerated(EnumType.STRING)
	   @Column(name = "type_operation")
	    private TypesOperation typeOperation;
	  
	  @Enumerated(EnumType.STRING)
	    @Column(name = "plant")
	    private Plant plant;  
	  
	  @ManyToOne
	    @JoinColumn(name = "role_id")
	    private Role role;
	  
	  @ManyToMany(mappedBy = "usersRempliePDEK") // relation remplissage de PDEK ==> nommer  "creer" 
	    private List<PDEK> pdeks = new ArrayList<>();

	  @OneToMany(mappedBy = "userSoudure") // relation "saisir" entre user et soudre  
	    private List<Soudure> soudures;
	  
	  @OneToMany(mappedBy = "userTorsadage") // relation "saisir" entre user et torsadage  
	    private List<Torsadage> torsadages;
	  
	  @OneToMany(mappedBy = "userPistolet") // relation "saisir" entre user et pistolet  
	    private List<Pistolet> pistoles;
	  
	  @OneToMany(mappedBy = "userSertissageIDC") // relation "saisir" entre user et sertissage idc   
	    private List<SertissageIDC> sertissagesIDC;
	  
	  @OneToMany(mappedBy = "userSertissageNormal") // relation "saisir" entre user et sertissage idc   
	    private List<SertissageNormal> sertissagesNormal;
	  
	  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	  private List<ControleQualite> controlesQualite = new ArrayList<ControleQualite>();
	  

	  // Relation OneToMany avec PlanAction
	    @OneToMany(mappedBy = "userPlanAction", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<PlanAction> plansAction = new ArrayList<>();

	  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	  private List<AuditLog> auditLogs;

	  @OneToMany(mappedBy = "userPlanAction", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<DetailsPlanAction> details = new ArrayList();

}
