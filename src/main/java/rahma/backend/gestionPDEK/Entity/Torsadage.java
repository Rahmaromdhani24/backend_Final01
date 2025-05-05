package rahma.backend.gestionPDEK.Entity;

import java.util.List;
import java.util.Map;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Torsadage extends Operation {

    public static final List<String> SPECIFICATIONS_MESURES = List.of("10 mm²" , "11 mm²" , "12 mm²" , "13 mm²" , "15 mm²" , "18 mm²", "20 mm²" , "24 mm²" ,"25 mm²" , "30 mm²" , "40 mm²" ,"45 mm²" );
    public static final List<String> CODES_CONTROLES = List.of("A", "D", "R");
    public static final Map<String, String> CODES_CONTROLES_DESCRIPTION = Map.of(
        "A", "Rupture à cause défaut de matériel ou perturbation de moyen de travail /Arrét ",      
        "D", "Démarrage de commande",
        "R", "Machine réparé"

    );
    private int  numeroCycle;
   
    @Column(name = "specification_mesure")
    private String  specificationMesure;
    
    @Column(name = "numero_commande")
    private long  numCommande ; 
    
    @Column(name = "Long_final_debut_Cde")
    private int longueurFinalDebutCde ;  
    
    @Column(name = "Long_final_fin_Cde")
    private int longueurFinalFinCde ;  
    
    @Column(name = "Long_bout_debut_Cde_c1")
    private int longueurBoutDebutCdeC1 ;  
    
    @Column(name = "Long_bout_debut_Cde_c2")
    private int longueurBoutDebutCdeC2 ;  
    
    @Column(name = "Long_bout_fin_Cde_c1")
    private int longueurBoutFinCdeC1 ;  
    
    @Column(name = "Long_bout_fin_Cde_c2")
    private int longueurBoutFinCdeC2 ;  
    
    @Column(name = "decalage_Max_DebutCde_c1")
    private int decalageMaxDebutCdec1 ;  
    
    @Column(name = "decalage_Max_DebutCde_c2")
    private int decalageMaxDebutCdec2 ;  
    
    @Column(name = "decalage_Max_FinCde_c1")
    private int decalageMaxFinCdec1 ;  
    
    @Column(name = "decalage_Max_FinCde_c2")
    private int decalageMaxFinCdec2 ;  
    
    @Column(name = "numero_fil")
    private String numerofil ; 
    
    @Column(name = "longueur_pas_fin_cde")
    private double longueurPasFinCde ; 
    
    @Column(name = "echantillon_1")
    private String ech1 ; 
    
    @Column(name = "echantillon_2")
    private String ech2 ; 
    
    @Column(name = "echantillon_3")
    private String ech3 ; 
    
    @Column(name = "echantillon_4")
    private String ech4 ; 
    
    @Column(name = "echantillon_5")
    private String ech5 ; 

    private String date;
    
    private String heureCreation ; 
    
    @Column(name = "quantite_totale")
    private int quantiteTotale ; 
    
    @Column(name = "quantite_atteint")
    private int quantiteAtteint; 
    
    @Column(name = "code_controle")
    private String code; 
    private String zone; 

    private double moyenne;
    private int etendu; 
    
    private int decision ;
    private int rempliePlanAction ;
    
    @ManyToOne   // relation "saisir" 
    @JoinColumn(name = "user_matricule")
    private User userTorsadage;
    
    @ManyToOne   // relation "associer"  PDEK a torsadage 
    @JoinColumn(name = "pdek_id")
    private PDEK pdekTorsadage;
    
    
    @ManyToOne   // Lien vers la page de PDEK 
    @JoinColumn(name = "page_pdek_id")
    private PagePDEK pagePDEK;
    
    
    public static String getDescriptionForCode(String code) {
        return CODES_CONTROLES_DESCRIPTION.getOrDefault(code, "Description non trouvée");
    }
}
