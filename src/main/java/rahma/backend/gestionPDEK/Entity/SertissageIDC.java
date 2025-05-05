package rahma.backend.gestionPDEK.Entity;

import java.util.List;
import java.util.Map;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "Sertissage_IDC")
public class SertissageIDC extends Operation {
    public static final List<String> SECTIONS_FILS = List.of("0.35 mm²" );
    public static final List<String> CODES_CONTROLES = List.of("A", "L","M", "R", "S", "w");

    public static final Map<String, String> CODES_CONTROLES_DESCRIPTION = Map.of(
        "A" , "Discontinuité du au fin matière" ,
        "L", "Fin de commande" ,
        "M", "Changement matière",
        "R", "Réparation ou réglage Outil/Machine",
        "S", "Démarrage du cycle"
      
      
    );

    @Column(name = "section_fil")
    private String  sectionFil;
    
    @Column(name = "force_traction")
    private String forceTraction;

    @Column(name = "hauteur_Sertissage_max")
    private double hauteurSertissageMax;
    
    @Column(name = "hauteur_Sertissage_min")
    private double hauteurSertissageMin;
    
    private int segment ; 
    
    @Column(name = "date_creation")
    private String date;
    private String heureCreation ; 
    
    @Column(name = "hauteur_Sertissage_c1_echantillon1")
    private double hauteurSertissageC1Ech1;
    
    @Column(name = "hauteur_Sertissage_c1_echantillon2")
    private double hauteurSertissageC1Ech2;
    
    @Column(name = "hauteur_Sertissage_c1_echantillon3")
    private double hauteurSertissageC1Ech3;
    
    @Column(name = "hauteur_Sertissage_c1_echantillonFin")
    private double hauteurSertissageC1EchFin;

    @Column(name = "hauteur_Sertissage_c2_echantillon1")
    private double hauteurSertissageC2Ech1;
    
    @Column(name = "hauteur_Sertissage_c2_echantillon2")
    private double hauteurSertissageC2Ech2;
    
    @Column(name = "hauteur_Sertissage_c2_echantillon3")
    private double hauteurSertissageC2Ech3;
    
    @Column(name = "hauteur_Sertissage_c2_echantillonFin")
    private double hauteurSertissageC2EchFin;
    
    @Column(name = "numero_cycle")
    private int numCycle ; 
    
    @Column(name = "code_controle")
    private String codeControle ; 

    private String produit ; 

    @Column(name = "serie_produit")
    private String serieProduit  ; 
    
    private String  zone;

    @Column(name = "quantite_cycle")
    private int quantiteCycle ; 
    
    @Column(name = "numero_machine")
    private int numeroMachine ; 
    
    @Column(name = "force_traction_c1_echantillon1")
    private double forceTractionC1Ech1 ; 

    @Column(name = "force_traction_c1_echantillon2")
    private double forceTractionC1Ech2 ; 

    @Column(name = "force_traction_c1_echantillon3")
    private double forceTractionC1Ech3 ; 

    @Column(name = "force_traction_c1_echFin")
    private double forceTractionC1EchFin ; 

    @Column(name = "force_traction_c2_echantillon1")
    private double forceTractionC2Ech1 ; 
    
    @Column(name = "force_traction_c2_echantillon2")
    private double forceTractionC2Ech2 ;

    @Column(name = "force_traction_c2_echantillon3")
    private double forceTractionC2Ech3;

    @Column(name = "force_traction_c2_echFin")
    private double forceTractionC2EchFin ; 

    private int decision ;
    private int rempliePlanAction ;
    /**************** Relations *********************/
    
    @ManyToOne   // relation "saisir" sertissage idc  
    @JoinColumn(name = "user_matricule")
    private User userSertissageIDC;
    
    
    @ManyToOne   // relation "associer"  PDEK a sertissage IDC 
    @JoinColumn(name = "pdek_id")
    private PDEK pdekSertissageIDC;
    
    @ManyToOne
    @JoinColumn(name = "page_pdek_id") // La clé étrangère qui relie à PagePDEK
    private PagePDEK pagePDEK; // **Doit correspondre à mappedBy dans PagePDEK**
    
    
    public static String getDescriptionForCode(String code) {
        return CODES_CONTROLES_DESCRIPTION.getOrDefault(code, "Description non trouvée");
    }
    
    
}
