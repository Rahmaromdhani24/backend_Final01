package rahma.backend.gestionPDEK.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;


	@Data
	@AllArgsConstructor
	public class OperateurErreurPistolet {
	 
		private int matricule;
	    private String nomPrenom ; 
	    private String plant;
	    private int segment;
	    private String typePistolet;
	    private String categoriePistolet;
	    private long nombreErreurs;
	}
