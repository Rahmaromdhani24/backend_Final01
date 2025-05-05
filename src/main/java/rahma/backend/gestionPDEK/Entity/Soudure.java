package rahma.backend.gestionPDEK.Entity;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Soudure extends Operation {

	public static final Map<String, String[]> SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne = Map.ofEntries(
		    Map.entry("0.13 mm²", new String[] { "7N"  , "60N" , "17" , "10" ,"35"}),
		    Map.entry("0.17 mm²", new String[] { "7N"  , "60N" , "17" , "10" ,"35"}),
		    Map.entry("0.22 mm²", new String[] { "7N"  , "60N" , "17" , "10" ,"35"}),
	        Map.entry("0.35 mm²", new String[] { "12N" , "72N" , "17" , "16" ,"62" }),
	        Map.entry("0.50 mm²",  new String[] { "15N" , "96N" , "23" , "19" ,"67"}),
	        Map.entry("0.75 mm²", new String[] { "23N" , "148N", "27" , "28" ,"76"}),
	        Map.entry("1 mm²",    new String[] { "35N" , "192N", "30" , "42" ,"90"}),
	        Map.entry("1.5 mm²",  new String[] { "45N" , "240N", "40" , "54" ,"102"}),
	        Map.entry("2 mm²",    new String[] { "60N" , "270N", "44" , "72" ,"120"}),
	        Map.entry("2.5 mm²",  new String[] { "70N" , "300N", "70" , "84" ,"132"}),
	        Map.entry("3 mm²",    new String[] { "80N" , "336N", "48" , "96" ,"184"}),
	        Map.entry("4 mm²",    new String[] { "100N", "420N", "50" , "120","168"}),
	        Map.entry("5 mm²",    new String[] { "115N", "540N", "60" , "138","238"}),
	        Map.entry("6 mm²",    new String[] { "130N", "600N", "70" , "156","204"}),
	        Map.entry("10 mm²",   new String[] { "165N", "840 N","80" , "198","246"})
	    );
	
	
    public static final List<String> CODES_CONTROLES = List.of("A", "R", "E", "P" , "B" , "S");

    public static final Map<String, String> CODES_CONTROLES_DESCRIPTION = Map.of(
        "A", "Démarrage du poste/ Changement de configuration /Fin du dernier poste /Arrêt",
        "R", "Réparation machine",
        "E", "Entretien machine",
        "P", "Changement des paramètres ou bien apprentissage" ,
        "B" , "Reprise de la même épissure" ,
        "S" , "Contrôle supplémentaire" 
    );
    
    @Column(name = "numero_cycle")
    private int  numeroCycle;
   
    @Column(name = "section_fil")
    private String  sectionFil;
    
    @Column(name = "limite_pelage")
    private String  limitePelage;

    private String pliage;
    private String distanceBC;
    private String traction;
    private int pelageX1;
    private int pelageX2;
    private int pelageX3;
    private int pelageX4;
    private int pelageX5;
    
    @Column(name = "nombre_kanban")
    private long nombreKanban;

    @Column(name = "grendeur_lot")
    private long grendeurLot;
    
    @Column(name = "nombre_noeud")
    private String  nombreNoeud;
    
    private double moyenne ;
    private int etendu;
   
    private String date;
    private String heureCreation ; 
    
    @Column(name = "quantite_atteint")
    private int quantiteAtteint; 
    private String code; 
    
    private String zone; 
    
    private int decision ;
    private int rempliePlanAction ;
    
    @ManyToOne   // Lien vers la page de PDEK 
    @JoinColumn(name = "page_pdek_id")
    private PagePDEK pagePDEK;
    
    @ManyToOne   // relation "associer"  PDEK a soudure 
    @JoinColumn(name = "pdek_id")
    private PDEK pdekSoudure ;
    
 
    @ManyToOne   // relation "saisir" 
    @JoinColumn(name = "user_matricule")
    private User userSoudure;
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*********************************************/

    public static String getDescriptionForCode(String code) {
        if (code == null) return "Code invalide";
        return CODES_CONTROLES_DESCRIPTION.getOrDefault(code.trim().toUpperCase(), "Description non trouvée");
    }

    public static Map<String, String[]> getSortedSectionsMap() {
        return SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    // Comparer les clés (sections de fil) en extrayant la valeur numérique
                    Double d1 = Double.parseDouble(entry1.getKey().replace(" mm²", ""));
                    Double d2 = Double.parseDouble(entry2.getKey().replace(" mm²", ""));
                    return d1.compareTo(d2); // Comparaison des doubles
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,  // Clé
                        Map.Entry::getValue, // Valeur
                        (e1, e2) -> e1, // En cas de conflit, conserver le premier élément
                        LinkedHashMap::new) // Utiliser LinkedHashMap pour conserver l'ordre
                );
    }

    public static void main(String[] args) {
        Map<String, String[]> sortedMap = getSortedSectionsMap();
        sortedMap.forEach((key, value) -> {
            System.out.print(key + ": ");
            System.out.println(Arrays.toString(value));  // Afficher les valeurs sous forme de tableau
        });
    }
     /***** constructure ************/
    public static List<String> getSortedSectionFilKeys() {
        Map<String, String[]> sortedMap = Soudure.getSortedSectionsMap();
        return sortedMap.keySet().stream()
                .collect(Collectors.toList());
    }
    }
 


