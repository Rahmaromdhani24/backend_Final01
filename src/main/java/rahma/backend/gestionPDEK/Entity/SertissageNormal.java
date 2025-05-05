package rahma.backend.gestionPDEK.Entity;

import java.util.List;
import java.util.Map;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "Sertissage_Normal")
public class SertissageNormal extends Operation {
  
    public static final List<String> CODES_CONTROLES = List.of("A", "L","M", "R", "S", "w");

    public static final Map<String, String> CODES_CONTROLES_DESCRIPTION = Map.of(
        "A" , "Discontinuité du au fin matière" ,
        "L", "Fin de commande" ,
        "M", "Changement matière",
        "R", "Réparation ou réglage Outil/Machine",
        "S", "Démarrage de la commande",
        "W" ,"Démarrage apprentissage du systeme de surveillance"
      
      
    );

    @Column(name = "numero_cycle")
    private int numCycle ; 
    
    @Column(name = "numero_outils")
    private String numeroOutils;
    
    @Column(name = "numero_contacts")
    private String numeroContacts;

    @Column(name = "section_fil")
    private String sectionFil;


    @Column(name = "hauteur_Sertisage_ech1")
    private double hauteurSertissageEch1;
    
    @Column(name = "hauteur_Sertisage_ech2")
    private double hauteurSertissageEch2;

    @Column(name = "hauteur_Sertisage_ech3")
    private double hauteurSertissageEch3;

    @Column(name = "hauteur_Sertisage_echFin")
    private double hauteurSertissageEchFin;

    @Column(name = "largeur_Sertissage")
    private double largeurSertissage;

    @Column(name = "largeur_Sertissage_EchFin")
    private double largeurSertissageEchFin;
    
    @Column(name = "hauteur_Isolant")
    private double hauteurIsolant;

    @Column(name = "hauteur_Isolant_EchFin")
    private double hauteurIsolantEchFin;

    @Column(name = "largeur_Isolant")
    private double largeurIsolant;
    
    @Column(name = "largeur_Isolant_EchFin")
    private double largeurIsolantEchFin;

    private String traction;

    @Column(name = "traction_FinEch")
    private double tractionFinEch;

    private String produit ; 

    @Column(name = "serie_produit")
    private String serieProduit; 

    @Column(name = "quantite_cycle")
    private int quantiteCycle; 
    
    private String codeControle; 
    
    private String date;
    private String heureCreation ;
    private int  segment ; 

    private double tolerance ; 

    @Column(name = "numero_machine")
    private String numeroMachine ; 
    
    private int decision ;
    private int rempliePlanAction ;
    private String zone ; 

    public static String getDescriptionForCode(String code) {
        return CODES_CONTROLES_DESCRIPTION.getOrDefault(code, "Description non trouvée");
    }
    
    /*********************************************************************************/
    @ManyToOne   // relation "saisir" sertissage idc  
    @JoinColumn(name = "user_matricule")
    private User userSertissageNormal;
    
    @ManyToOne   // relation "associer"  PDEK a sertissage Normal 
    @JoinColumn(name = "pdek_id")
    private PDEK pdekSertissageNormal;
    
    @ManyToOne
    @JoinColumn(name = "page_pdek_id") // La clé étrangère qui relie à PagePDEK
    private PagePDEK pagePDEK; // **Doit correspondre à mappedBy dans PagePDEK**
    
    
  
}
