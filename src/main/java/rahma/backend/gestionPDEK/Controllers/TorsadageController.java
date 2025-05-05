package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.PDEK_ServiceImplimenetation;
import rahma.backend.gestionPDEK.ServicesImplementation.TorsadageServiceImplimentation;

@RestController
@RequestMapping("/operations/torsadage")
public class TorsadageController {

	@Autowired  private TorsadageServiceImplimentation serviceTorsadage;
    @Autowired  private TorsadageRepository torsadageRepository ; 
    @Autowired  private PDEK_ServiceImplimenetation servicePDEK ; 


    @GetMapping("/specificationsMesure")
    public List<String> getSectionsFils() {
        return Torsadage.SPECIFICATIONS_MESURES;
    }

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return Torsadage.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return Torsadage.getDescriptionForCode(code);
    }

    /******************************* Partie PDEK *******************************/
     @GetMapping("/verifier-pdek")
    public boolean verifierPDEK(
            @RequestParam String specificationMesure,
            @RequestParam int  segment,
            @RequestParam Plant nomPlant,
            @RequestParam String nomProjet) {
        return servicePDEK.verifierExistencePDEK_Torsadage(specificationMesure,segment , nomPlant ,  nomProjet);
    }
    
    @GetMapping("/pdekExiste")
    public PdekDTO recupererNumCycleSiPDEKExist(
    		  @RequestParam String specificationMesure,
              @RequestParam int  segment,
              @RequestParam Plant nomPlant,
              @RequestParam String nomProjet) {
    	return  servicePDEK.recupererPdekTorsadag(specificationMesure, segment , nomPlant , nomProjet);     
    }
    

    @GetMapping("/torsadage-par-pdek")
    public Map<Integer, List<TorsadageDTO>>  getTorsadagesParPdek(
            @RequestParam String specificationMesure,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceTorsadage.recupererTorsadagesParPDEKGroupéesParPage(specificationMesure,segment , plant ,  nomProjet);
    }
    
     @PostMapping("/ajouterPDEK")
    public ResponseEntity<String> ajouterTorsadageAvecPdek(
            @RequestBody Torsadage torsadage, 
            @RequestParam int matriculeOperateur, 
            @RequestParam String projet) {

        try {
        AjoutTorsadageResultDTO result = serviceTorsadage.ajoutPDEK_Torsadage(torsadage, matriculeOperateur, projet);
           String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage() +
           		"\", \"idTorsadage\": \"" + result.getIdTorsadage() +"\" }";
           return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }


     @GetMapping("/dernier-numero-cycle")
     public ResponseEntity<?> getLastNumeroCycle(
             @RequestParam String specificationMesureSelectionner,
             @RequestParam int segment,
             @RequestParam Plant nomPlant,
             @RequestParam String projetName) {
     
         int dernierNumeroCycle = serviceTorsadage.getLastNumeroCycle(specificationMesureSelectionner, segment, nomPlant, projetName);
     
         //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
         return ResponseEntity.ok(dernierNumeroCycle);
     }
     @GetMapping("/page-actuelle")
     public ResponseEntity<List<TorsadageDTO>> getTorsadagesParPageActuelle(
             @RequestParam String specificationMesure,
             @RequestParam int segment,
             @RequestParam String plant, // à convertir si `Plant` est un enum ou objet
             @RequestParam String nomProjet
     ) {
         try {
             // Si Plant est un enum :
             Plant plantEnum = Plant.valueOf(plant.toUpperCase());
 
             List<TorsadageDTO> liste = serviceTorsadage.recupererTorsadagesParPageActuel(
                     specificationMesure, segment, plantEnum, nomProjet
             );
 
             return ResponseEntity.ok(liste);
         } catch (IllegalArgumentException e) {
             return ResponseEntity.badRequest().build(); // Mauvais nom de plant
         }
     }
     @GetMapping("/torsadages-par-pdek-et-page")
public ResponseEntity<List<TorsadageDTO>> getTorsadageParPdekEtPage(
        @RequestParam Long pdekId,
        @RequestParam int pageNumber) {

    List<Torsadage> torsadages = torsadageRepository.findByPdekTorsadage_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

    List<TorsadageDTO> soudureDTOs = torsadages.stream().map(s ->
        new TorsadageDTO(
        		s.getId(),
        	    s.getClass().getSimpleName() ,
        	    s.getUserTorsadage().getSegment() ,
   	            s.getUserTorsadage().getPlant().toString() ,
                s.getUserTorsadage().getMachine() ,
  	            s.getCode(),
  	            s.getSpecificationMesure(),
                s.getSpecificationMesure(),
  	            s.getDate(),
	            s.getHeureCreation(), 
  	            s.getNumeroCycle(),
  	            s.getUserTorsadage().getMatricule(),
  	            s.getMoyenne(),
  	            s.getEtendu(),
  	            s.getEch1(),
  	            s.getEch2(),
  	            s.getEch3(),
  	            s.getEch4(),
  	            s.getEch5(),
  	            s.getNumCommande(),
  	            s.getQuantiteTotale(),
  	            s.getNumerofil(),
  	            s.getLongueurFinalDebutCde(),
  	            s.getLongueurFinalFinCde(),
  	            s.getLongueurBoutDebutCdeC1(),
  	            s.getLongueurBoutDebutCdeC2(),
  	            s.getLongueurBoutFinCdeC1(),
  	            s.getLongueurBoutFinCdeC2(),
  	            s.getDecalageMaxDebutCdec1(),
  	            s.getDecalageMaxDebutCdec2(),
  	            s.getDecalageMaxFinCdec1(),
  	            s.getDecalageMaxFinCdec2(),
  	            s.getQuantiteAtteint(),
  	            s.getUserTorsadage().getMatricule(),
  	            s.getDecision(),
  	            s.getRempliePlanAction() ,
  	            s.getPdekTorsadage().getId()  ,
   	            s.getPagePDEK().getPageNumber() ,
   	            s.getZone()
        )
    ).collect(Collectors.toList());

    return ResponseEntity.ok(soudureDTOs);
}

     @GetMapping("/torsadages-non-validees-agents-Qualite")
 	public List<TorsadageDTO> getTorsadagesNonValidees() {
 	    return serviceTorsadage.getTorsadagsNonValidees() ; 
 	}
 	@GetMapping("/nbrNotificationsAgentsQualite")
 	   public int getNombreNotification() {
 	       return serviceTorsadage.getTorsadagsNonValidees().size() ; 
 	}
 	
 	@GetMapping("/torsadages-validees")
 	public List<TorsadageDTO>  getTorsadagesValidees() {
 	    return serviceTorsadage.getTorsadagsValidees();
 	}
 	 @GetMapping("/torsadages-non-validees-plan-action")
 	    public List<TorsadageDTO> getPistoletsNonValideesAvecPlanAction() {
 	        return serviceTorsadage.getTorsadagesNonValideesTechniciens() ; 
 	    }
 	 
 	 @GetMapping("/nbrNotificationsTechniciens")
 	    public int getNombresNotificationsPistoletsNonValiderDePlanAction() {
 	        return serviceTorsadage.getTorsadagesNonValideesTechniciens().size(); }
 	   
 	 @PutMapping("/validerTorsadage")
 	 public ResponseEntity<?> validerPistolet(@RequestParam Long id, @RequestParam Integer matriculeAgentQualite) {
 		serviceTorsadage.validerTorsadage(id, matriculeAgentQualite);
 	     return ResponseEntity.ok().build(); }
 	
 	

 	 @GetMapping("/users-by-pdek/{id}")
 	    public ResponseEntity<List<UserDTO>> getUserDTOsByPdek(@PathVariable Long id) {
 	        List<UserDTO> userDTOs = serviceTorsadage.getUserDTOsByPdek(id);
 	        return ResponseEntity.ok(userDTOs);
 	    }
 	 

  	 @PutMapping("/plan-action-zone/{zone}/{id}")
  	 public ResponseEntity<String> remplirPlanActionEtZoneCouleur(@PathVariable Long id , @PathVariable String zone) {
  	         boolean success = serviceTorsadage.changerAttributRempliePlanActionTorsadageDe0a1(id , zone);
  	         if (success) {
  	             return ResponseEntity.ok("Attribut rempliePlanAction mis à jour et zone ajouter!");
  	         } else {
  	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Torsadage non trouvée.");
  	         }
  	 }
  	 	 
 }

 

