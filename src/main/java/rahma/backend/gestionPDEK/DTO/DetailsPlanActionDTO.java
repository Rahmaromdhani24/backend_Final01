package rahma.backend.gestionPDEK.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailsPlanActionDTO {
	

	private Long id;
    private String dateCreation;
	private String heureCreation;
	private String description_probleme;
    private int matricule_operateur;
    private int matricule_chef_ligne;
    private String description_decision;
    private int signature_qualite;
    private int signature_maintenance;
    private int signature_contermetre;
    private long idPlanAction ; 
    private int userPlanAction  ; 
    private String delais ; 
    private String responsable ; 

    
}
