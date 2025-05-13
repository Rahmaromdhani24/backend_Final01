package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operateur {

	private int matricule;
	    private String nom;
	    private String prenom;
	    private String plant;
	    private int segment;
	    private String genre;
	    private long numeroTelephone;
	    private String typeOperation;
	    private String machine ; 
	    private String poste ; 
	    
}
