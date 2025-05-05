package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SertissageIDC_DTO {
    private Long id;
    private String typeOperation ; 
    private String code;
    private String sectionFil;
    private String date;
    private int numCycle;
    private int userSertissageIDC ; 
    private double hauteurSertissageC1Ech1;
    private double hauteurSertissageC1Ech2;
    private double hauteurSertissageC1Ech3;
    private double hauteurSertissageC1EchFin;
    private double hauteurSertissageC2Ech1;
    private double hauteurSertissageC2Ech2;
    private double hauteurSertissageC2Ech3;
    private double hauteurSertissageC2EchFin;
    private String produit ; 
    private String serieProduit  ; 
    private int quantiteCycle ; 
    private int numeroMachine ; 
    private double forceTractionC1Ech1 ; 
    private double forceTractionC1Ech2 ; 
    private double forceTractionC1Ech3 ; 
    private double forceTractionC1EchFin ; 
    private double forceTractionC2Ech1 ; 
    private double forceTractionC2Ech2 ;
    private double forceTractionC2Ech3;
    private double forceTractionC2EchFin ; 
    private int decision ;
    private int rempliePlanAction ;
    private long pdekId ; 
    private int numPage ; 
    private String zone  ; 
    private String heureCreation ; 
}
