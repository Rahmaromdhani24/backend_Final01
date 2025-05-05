package rahma.backend.gestionPDEK.Configuration;

import lombok.*;

@Setter
@Getter
public  class EmailPistoletRequest {
    private String toEmail;
    private String nomResponsable;
    private String numPistolet ; 
    private String typePistolet;
    private String couleurPistolet;
    private String localisation;
    private String valeurMesuree;      
    private String limitesAcceptables;  
}