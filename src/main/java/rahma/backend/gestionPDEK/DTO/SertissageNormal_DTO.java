package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SertissageNormal_DTO {
    private Long id;
    private String typeOperation ; 
    private String plant ; 
    private String heureCreation ; 
    private String code;
    private String sectionFil;
    private String numOutil ; 
    private String numContact ; 
    private String date;
    private int numCycle;
    private int userSertissageNormal ; 
    private double hauteurSertissageEch1 ; 
    private double hauteurSertissageEch2 ; 
    private double hauteurSertissageEch3 ; 
    private double hauteurSertissageEchFin ; 

    private double largeurSertissage;
    private double largeurSertissageEchFin;

    private double hauteurIsolant;
    private double largeurIsolant;
    private double largeurIsolantEchFin;
    private double hauteurIsolantEchFin;


    private String traction;
    private double tractionFinEch;
    
    private String produit ; 
    private String serieProduit; 

    private int quantiteCycle; 
    private int  segment ; 
    private String numeroMachine ; 
    
    private int decision ;
    private int rempliePlanAction ;
    
    private long pdekId ; 
    private int numPage ;
    
    private String  LGD;
    private String zone  ;

}
