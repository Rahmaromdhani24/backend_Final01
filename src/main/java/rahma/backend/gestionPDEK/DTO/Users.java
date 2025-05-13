package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

	 private int  matricule ; 
	  private String nom ; 
	  private String prenom ;
	  private String email ;
	  private String plant ;
	  private int segment ;
	  private long numeroTelephone ;
	  private String typeOperation ;
	  private String sexe ; 
	  private String role  ; 
}
