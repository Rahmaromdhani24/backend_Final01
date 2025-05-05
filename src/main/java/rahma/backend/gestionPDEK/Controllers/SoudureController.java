package rahma.backend.gestionPDEK.Controllers;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutSoudureResultDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.*;

@RestController
@RequestMapping("/operations/soudure")
public class SoudureController {
    @Autowired  private SoudureServiceImplimentation serviceSoudure;
    @Autowired  private SoudureRepository soudureRepository ; 
    @Autowired  private PDEK_ServiceImplimenetation servicePDEK ; 
    @Autowired  private ServiceControleQualiteImplemenets  serviceControleQualite ; 

    
    @GetMapping("/sectionsFils")
    public List<String> getSortedSections() {
        return Soudure.getSortedSectionFilKeys();
    }
    @GetMapping("/verifier-pdek")
    public boolean verifierPDEK(
            @RequestParam String sectionFil,
            @RequestParam int  segment,
            @RequestParam Plant nomPlant,
            @RequestParam String nomProjet) {
        return servicePDEK.verifierExistencePDEK_soudureUltrason(sectionFil,segment , nomPlant ,  nomProjet);
    }
    
    @GetMapping("/pdekExiste")
    public PdekDTO recupererNumCycleSiPDEKExist(
    		  @RequestParam String sectionFil,
              @RequestParam int  segment,
              @RequestParam Plant nomPlant,
              @RequestParam String nomProjet) {
    	return  servicePDEK.recupererPdekSoudureUltrason(sectionFil, segment , nomPlant , nomProjet);     
    }
    
 
    @GetMapping("/soudures-par-pdek")
    public Map<Integer, List<SoudureDTO>> getSouduresParPdek(
            @RequestParam String sectionFil,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceSoudure.recupererSouduresParPDEKGroupéesParPage(sectionFil, segment, plant, nomProjet);
    }

    @GetMapping("/pelage/{sectionFil}")// sans mm ( nombre secttion )
    public ResponseEntity<String> getPelageBySectionn(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] pelages = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (pelages != null && pelages.length > 0) {
            return ResponseEntity.ok(pelages[0]);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }

    @GetMapping("/pelage/nombre/{sectionFil}")
    public ResponseEntity<String> getPelageValeurBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] pelages = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);
    
        if (pelages != null && pelages.length > 0) {
            // Extraire uniquement la partie numérique en supprimant le "N"
            String pelageSansN = pelages[0].replaceAll("[^0-9]", ""); 
            return ResponseEntity.ok(pelageSansN);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }
    
    @GetMapping("/traction/{sectionFil}")
    public ResponseEntity<String> getTractionBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] tractions = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (tractions != null && tractions.length > 1) {
            return ResponseEntity.ok(tractions[1]);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }
    @GetMapping("/traction2/{sectionFil}")
public ResponseEntity<String> getTractionBySections(@PathVariable String sectionFil) {
    String[] valeurs = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(sectionFil);
    
    if (valeurs == null || valeurs.length < 2 || valeurs[1].isEmpty()) {
        return ResponseEntity.badRequest().body("Aucune valeur de traction trouvée pour la section " + sectionFil);
    }

    String traction = valeurs[1].replaceAll("[^0-9]", ""); // Supprime tout sauf les chiffres

    return ResponseEntity.ok(traction);
}

    @GetMapping("/etenduMax/{sectionFil}")
    public ResponseEntity<String> getEtenduMaxBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] etendus = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (etendus != null && etendus.length > 2) {
            return ResponseEntity.ok(etendus[2]);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }
    @GetMapping("/valeurMoyenneVertMin/{sectionFil}")
    public ResponseEntity<String> getMoyenneMinBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] moyennes = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (moyennes != null && moyennes.length > 3) {
            return ResponseEntity.ok(moyennes[3]);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }
    @GetMapping("/valeurMoyenneVertMax/{sectionFil}")
    public ResponseEntity<String> getMoyenneMaxBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";
        String[] moyennes = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (moyennes != null && moyennes.length > 4) {
            return ResponseEntity.ok(moyennes[4]);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section de fil " + sectionFil + " mm² non trouvée ou pas de pelage associé.");
        }
    }
    @GetMapping("/sectionsFilPelageAutresParties/{sectionFil}")
    public ResponseEntity<String> getPelageBySection(@PathVariable String sectionFil) {
        String formattedSection = sectionFil.replace(",", ".").trim() + " mm²";        
        String[] pelages = Soudure.SECTIONS_FILS_PELAGE_TRACTION_ETENDU_maxVert_maxMoyenne.get(formattedSection);

        if (pelages != null) {
            String pelagesConcat = String.join(", ", pelages);
            return ResponseEntity.ok(pelagesConcat);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La section de fil " + sectionFil + " mm² n'a pas été trouvée.");
        }
    }

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return Soudure.CODES_CONTROLES;
    }
    @GetMapping("/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return Soudure.getDescriptionForCode(code);
    }
    
    @PostMapping("/ajouterPDEK")
    public ResponseEntity<String> ajouterSoudureAvecPdek(
            @RequestBody Soudure soudure, 
            @RequestParam int matriculeOperateur, 
            @RequestParam String projet) {
    
        try {
            AjoutSoudureResultDTO result = serviceSoudure.ajoutPDEKSoudure(soudure, matriculeOperateur, projet);
            // Retourner un objet JSON structuré avec l'ID du PDEK et le numéro de la page
            String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage() +
            		"\", \"idSoudure\": \"" + result.getIdSoudure() +"\" }";
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }
    

    @GetMapping("/dernier-numero-cycle")
    public ResponseEntity<?> getLastNumeroCycle(
            @RequestParam String sectionFilSelectionne,
            @RequestParam int segment,
            @RequestParam Plant nomPlant,
            @RequestParam String projetName) {
    
        int dernierNumeroCycle = serviceSoudure.getLastNumeroCycle(sectionFilSelectionne, segment, nomPlant, projetName);
    
        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
        return ResponseEntity.ok(dernierNumeroCycle);
    }
    
    @GetMapping("/soudures-par-pdek-et-page")
    public ResponseEntity<List<SoudureDTO>> getSouduresParPdekEtPage(
            @RequestParam Long pdekId,
            @RequestParam int pageNumber) {

        List<Soudure> soudures = soudureRepository.findByPdekSoudure_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

        // Appel à la méthode qui récupère le matricule de l'agent qualité

        // Mapper les soudures en SoudureDTO
        List<SoudureDTO> soudureDTOs = soudures.stream().map(s -> 
            new SoudureDTO(
            		  s.getId(),
            	        s.getClass().getSimpleName(),
            	        s.getUserSoudure().getSegment() ,
                      s.getUserSoudure().getPlant().toString() ,
            	        s.getUserSoudure().getMachine() ,
                      s.getCode(),
                      s.getSectionFil(),
                      s.getDate(),
                      s.getHeureCreation() ,
                      s.getNumeroCycle(),
                      s.getUserSoudure().getMatricule(),
                      s.getMoyenne(),
                      s.getEtendu() ,
                      s.getPelageX1() ,
                      s.getPelageX2() ,
                      s.getPelageX3() ,
                      s.getPelageX4() ,
                      s.getPelageX5() ,
                      s.getPliage(),
                      s.getDistanceBC() ,
                      s.getTraction() , 
                      s.getNombreKanban() ,
                      s.getNombreNoeud() ,
                      s.getGrendeurLot() ,
                      s.getUserSoudure().getMatricule() ,
                      serviceControleQualite.getUserIdByPdekIdAndPageAndOperation(pdekId ,   s.getId()) ,
                      s.getDecision() , 
                      s.getRempliePlanAction(),
                      s.getPdekSoudure().getId()  ,
           	          s.getPagePDEK().getPageNumber() , 
           	          s.getQuantiteAtteint() ,
           	          s.getZone()
            )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(soudureDTOs);
    }



@GetMapping("/page-actuelle")
public ResponseEntity<List<SoudureDTO>> getTorsadagesParPageActuelle(
        @RequestParam String sectionFil,
        @RequestParam int segment,
        @RequestParam String plant, // à convertir si `Plant` est un enum ou objet
        @RequestParam String nomProjet
) {
    try {
        // Si Plant est un enum :
        Plant plantEnum = Plant.valueOf(plant.toUpperCase());

        List<SoudureDTO> liste = serviceSoudure.recupererSouduresParPageActuel(
            sectionFil, segment, plantEnum, nomProjet
        );

        return ResponseEntity.ok(liste);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build(); // Mauvais nom de plant
    }
}
	@GetMapping("/soudures-non-validees-agents-Qualite")
	public List<SoudureDTO> getSouduresNonValidees() {
	    return serviceSoudure.getSouduresNonValidees() ; 
	}
	@GetMapping("/nbrNotificationsAgentsQualite")
	   public int getNombreNotification() {
	       return serviceSoudure.getSouduresNonValidees().size() ; 
	}
	
	@GetMapping("/soudures-validees")
	public List<SoudureDTO>  getSouduresValidees() {
	    return serviceSoudure.getSouduresValidees();
	}
	 @GetMapping("/soudures-non-validees-plan-action")
	    public List<SoudureDTO> getPistoletsNonValideesAvecPlanAction() {
	        return serviceSoudure.getSouduresNonValideesTechniciens();
	    }
	 
	 @GetMapping("/nbrNotificationsTechniciens")
	    public int getNombresNotificationsPistoletsNonValiderDePlanAction() {
	        return serviceSoudure.getSouduresNonValideesTechniciens().size(); }
	   
	 @PutMapping("/validerSoudure")
	 public ResponseEntity<?> validerPistolet(@RequestParam Long id, @RequestParam Integer matriculeAgentQualite) {
		 serviceSoudure.validerSoudure(id, matriculeAgentQualite);
	     return ResponseEntity.ok().build(); }
	
	

	 @GetMapping("/users-by-pdek/{id}")
	    public ResponseEntity<List<UserDTO>> getUserDTOsByPdek(@PathVariable Long id) {
	        List<UserDTO> userDTOs = serviceSoudure.getUserDTOsByPdek(id);
	        return ResponseEntity.ok(userDTOs);
	    }
	 
	 @PutMapping("/plan-action-zone/{zone}/{id}")
  	 public ResponseEntity<String> remplirPlanActionEtZoneCouleur(@PathVariable Long id , @PathVariable String zone) {
  	         boolean success = serviceSoudure.changerAttributRempliePlanActionSoudureDe0a1(id , zone);
  	         if (success) {
  	             return ResponseEntity.ok("Attribut rempliePlanAction mis à jour et zone ajouter!");
  	         } else {
  	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Soudure non trouvée.");
  	         }
  	 }
  	 	 
}
