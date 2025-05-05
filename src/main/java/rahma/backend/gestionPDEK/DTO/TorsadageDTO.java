package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TorsadageDTO {
  
	private Long id;
	private String typeOperation ;
    private int segment ; 
    private String plant ; 
    private String numeroMachine ; 
    private String code;
    private String specificationMesure;
    private String sectionFil ; 
    private String date;
    private String heureCreation;
    private int numeroCycle;
    private int userTorsadage ; 
    private  double moyenne ; 
    private int etendu ; 
    private String ech1 ; 
    private String ech2 ; 
    private String ech3 ;
    private String ech4 ; 
    private String ech5 ; 
    private long  numCommande ; 
    private int quantiteTotale ; 
    private String numerofil ; 
    private int longueurFinalDebutCde ;  
    private int longueurFinalFinCde ;  
    private int longueurBoutDebutCdeC1 ;  
    private int longueurBoutDebutCdeC2 ;  
    private int longueurBoutFinCdeC1 ;  
    private int longueurBoutFinCdeC2 ;  
    private int decalageMaxDebutCdec1 ;  
    private int decalageMaxDebutCdec2 ;  
    private int decalageMaxFinCdec1 ;  
    private int decalageMaxFinCdec2 ;  
    private int quantiteAtteint;     
    private int matriculeAgentQualite ; 
    private int decision ; 
    private int rempliePlanAction ; 
    private long pdekId ; 
    private int numPage ;
    private String zone ; 
}
