package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
public class StatPistolet {
	    private String categorie;
	    private String couleur;
	    private int nombrePdek;
	    private int nombreErreurs ; 
	    
		public StatPistolet(String categorie, String couleur, int nombrePdek , int nombreErreurs) {
			super();
			this.categorie = categorie;
			this.couleur = couleur;
			this.nombrePdek = nombrePdek;
			this.nombreErreurs = nombreErreurs;

		}
	    
	    
}
