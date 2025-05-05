package rahma.backend.gestionPDEK.DTO;

import lombok.Getter;
import lombok.Setter;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

@Getter
@Setter
public class PlanActionDTO {
	
	private Long id;
    private String dateCreation;
	private String heureCreation;
    private TypesOperation type_operation;
	private Long pagePdekId ;
	private Long pdekId ; 
	private int matriculeUser ; 
	private String poste ;
	private String machine ;
	private String plant ;
	private int segment ; 
	private String  sectionFil ;

}
