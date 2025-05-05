package rahma.backend.gestionPDEK.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperateurErreurDTO {
 
	private int matricule;
    private String nomPrenom ; 
    private String poste;
    private String machine;
    private String role;
    private String plant;
    private int segment;
    private String typeOperation;
    private long nombreErreurs;
}
