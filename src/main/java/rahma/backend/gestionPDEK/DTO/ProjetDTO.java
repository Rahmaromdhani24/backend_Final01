package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjetDTO {

	private long id ; 
	private String nom ; 
	private String plant ; 
	private int nombrePdek ; 
	private String dateCreation  ; 
}
