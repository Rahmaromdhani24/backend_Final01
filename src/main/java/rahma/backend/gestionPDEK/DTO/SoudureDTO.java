package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoudureDTO {
    private Long id;
    private String typeOperation ; 
    private int segment ; 
    private String plant ; 
    private String  numeroMachine ; 
    private String code;
    private String sectionFil;
    private String date;
    private String heureCreation ; 
    private int numeroCycle;
    private int userSoudure ;
    private double moyenne ; 
    private int etendu ;
    private double ech1 ; 
    private double ech2 ; 
    private double ech3 ;
    private double ech4 ; 
    private double ech5 ; 
    private String pliage ; 
    private String distanceBC ; 
    private String traction ; 
    private long nbrKanban  ; 
    private String nbrNoeud ; 
    private long grendeurLot ; 
    private int matriculeOperateur ; 
    private int matriculeAgentQualite ; 
    private int decision ; 
    private int rempliePlanAction ; 
    private long pdekId ; 
    private int numPage ;
    private int quantiteAtteint ;
    private String zone ; 
}
