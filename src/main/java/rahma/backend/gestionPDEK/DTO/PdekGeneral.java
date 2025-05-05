package rahma.backend.gestionPDEK.DTO;


import lombok.*;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypesOperation;


@Getter
@Setter
@NoArgsConstructor
public class PdekGeneral {
	 private Long id;
	    
	    private int totalPages;
	    
	    private String sectionFil;
	    
	    
	    private long frequenceControle;
	    
	    private int segment; 
	    
	    private String numMachine; 
	    
	    private String dateCreation;
	    
		private TypesOperation typeOperation;
	    
	    private Plant  plant;  

	    private String numeroOutils;
	    
	    private String numeroContacts;
	   
	    private String  LGD;
	    
	    private double tolerance;
	    
	    private String  posGradant;
	    
	    private Object usersMatricules;
	    
	    private String numPoste ; 

		public PdekGeneral(Long id, int totalPages, String sectionFil, 
				long frequenceControle, int segment, String numMachine, String dateCreation, TypesOperation typeOperation,
				Plant plant, String numeroOutils, String numeroContacts, String lGD, double tolerance,
				String posGradant, Object usersMatricules , String numPoste ) {
			super();
			this.id = id;
			this.totalPages = totalPages;
			this.sectionFil = sectionFil;
			this.frequenceControle = frequenceControle;
			this.segment = segment;
			this.numMachine = numMachine;
			this.dateCreation = dateCreation;
			this.typeOperation = typeOperation;
			this.plant = plant;
			this.numeroOutils = numeroOutils;
			this.numeroContacts = numeroContacts;
			this.LGD = lGD;
			this.tolerance = tolerance;
			this.posGradant = posGradant;
			this.usersMatricules = usersMatricules;
			this.numPoste = numPoste ; 
		}
	    
	    

}


