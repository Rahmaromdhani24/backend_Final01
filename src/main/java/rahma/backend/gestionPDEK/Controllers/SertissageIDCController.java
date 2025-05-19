package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutSertissageResultDTO;
import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.SertissageIDC_ServiceImplimenetation;

@RestController
@RequestMapping("/operations/SertissageIDC")
public class SertissageIDCController {
	
	   
       @Autowired private SertissageIDC_ServiceImplimenetation serviceSertissageIDC ; 
       @Autowired private PdekRepository pdekRepository  ; 
       @Autowired private SertissageIDCRepository repository   ; 
       
    @GetMapping("/sectionsFils")
    public List<String> getSectionsFils() {
        return SertissageIDC.SECTIONS_FILS;
    }

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return SertissageIDC.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return SertissageIDC.getDescriptionForCode(code);
    }
    @GetMapping("/findUniquePDEK")
    public ResponseEntity<?> findUniquePDEK_SertissageIDC(
            @RequestParam String sectionFilSelectionne,
            @RequestParam int segment,
            @RequestParam String nomPlant,
            @RequestParam String projetName) {
        try {
            Optional<PDEK> pdek = pdekRepository.findUniquePDEK_SertissageIDC(
                    sectionFilSelectionne, segment, Plant.valueOf(nomPlant), projetName);
            
            if (pdek.isPresent()) {
                return ResponseEntity.ok(pdek.get());  // Retourne l'objet PDEK trouvé
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun PDEK trouvé");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erreur : Plant invalide - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur serveur : " + e.getMessage());
        }
    }

    
    @PostMapping("/ajouterPdekSertissageIDC")
    public ResponseEntity<String> ajouterSertissageIDC(
    		 @RequestParam int matricule,
    		 @RequestParam String nomProjet,
             @RequestBody SertissageIDC sertissageIDC) {
        try {
        	 AjoutSertissageResultDTO result = serviceSertissageIDC.ajoutPDEK_SertissageIDC( sertissageIDC , matricule, nomProjet);
        	  String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage()  
        			  + "\", \"idSertissage\": \"" + result.getIdSertissage() +"\" }";
              return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @GetMapping("/dernier-numero-cycle")
    public ResponseEntity<?> getLastNumeroCycle(
            @RequestParam String sectionFil,
            @RequestParam int segment,
            @RequestParam Plant nomPlant,
            @RequestParam String projetName) {
    
        int dernierNumeroCycle = serviceSertissageIDC.getLastNumeroCycle(sectionFil, segment, nomPlant, projetName);
    
        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
        return ResponseEntity.ok(dernierNumeroCycle);
    }
    @GetMapping("/sertissages-par-pdek")
    public Map<Integer, List<SertissageIDC_DTO>> getSouduresParPdek(
            @RequestParam String sectionFil,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceSertissageIDC.recupererSertissagesParPDEKGroupéesParPage(sectionFil, segment, plant, nomProjet);
    }
    @GetMapping("/sertissagesIDC-non-validees-agents-Qualite")
	public List<SertissageIDC_DTO> getSouduresNonValidees() {
	    return serviceSertissageIDC.getSertissagesIDCNonValidees() ; 
	}
	@GetMapping("/nbrNotificationsAgentsQualite")
	   public int getNombreNotification() {
	       return serviceSertissageIDC.getSertissagesIDCNonValidees().size() ; 
	}
	
	@GetMapping("/sertissagesIDC-validees")
	public List<SertissageIDC_DTO>  getSouduresValidees() {
	    return serviceSertissageIDC.getSertissagesIDCValidees() ; 
	}
	 @GetMapping("/sertissagesIDC-non-validees-plan-action")
	    public List<SertissageIDC_DTO> getSertissagesIDCNonValideesAvecPlanAction() {
	        return serviceSertissageIDC.getSertissagesIDCNonValideesChefLigne() ; 
	    }
	 
	 @GetMapping("/nbrNotificationsChefLigne")
	    public int getNombresNotificationsPistoletsNonValiderDePlanAction() {
	        return serviceSertissageIDC.getSertissagesIDCNonValideesChefLigne().size(); }
	   
	 @PutMapping("/validerSertissageIDC")
	 public ResponseEntity<?> validerPistolet(@RequestParam Long id, @RequestParam Integer matriculeAgentQualite) {
		 serviceSertissageIDC.validerSertissageIDC(id, matriculeAgentQualite);
	     return ResponseEntity.ok().build(); }
	
	

	 @GetMapping("/users-by-pdek/{id}")
	    public ResponseEntity<List<UserDTO>> getUserDTOsByPdek(@PathVariable Long id) {
	        List<UserDTO> userDTOs = serviceSertissageIDC.getUserDTOsByPdek(id);
	        return ResponseEntity.ok(userDTOs);
	    }
	 
	 @PutMapping("/plan-action-zone/{zone}/{id}")
public ResponseEntity<String> remplirPlanAction(@PathVariable Long id , @PathVariable String zone) {
        boolean success = serviceSertissageIDC.changerAttributRempliePlanActionSertissageIDCeDe0a1(id , zone);
        if (success) {
            return ResponseEntity.ok("Attribut rempliePlanAction mis à jour !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Soudure non trouvée.");
        }
        
	 }
	 
	 @GetMapping("/sertissages-par-pdek-et-page")
	 public ResponseEntity<List<SertissageIDC_DTO>> getSertissagesParPdekEtPage(
	         @RequestParam Long pdekId,
	         @RequestParam int pageNumber) {

	     List<SertissageIDC> sertissages = repository.findByPdekSertissageIDC_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

	     List<SertissageIDC_DTO> sertissagesDTOs = sertissages.stream()
	    	.map(s ->  new SertissageIDC_DTO( 
	 	            	s.getId(),
	 	            	s.getClass().getSimpleName() ,
	        		    s.getCodeControle(),
	        		    s.getSectionFil(),
	        		    s.getDate().toString(),
	        		    s.getNumCycle(),
	        		    s.getUserSertissageIDC().getMatricule(),
	        		    s.getHauteurSertissageC1Ech1(),
	        		    s.getHauteurSertissageC1Ech2(),
	        		    s.getHauteurSertissageC1Ech3(),
	        		    s.getHauteurSertissageC1EchFin(),
	        		    s.getHauteurSertissageC2Ech1(),
	        		    s.getHauteurSertissageC2Ech2(),
	        		    s.getHauteurSertissageC2Ech3(),
	        		    s.getHauteurSertissageC2EchFin(),
	        		    s.getProduit(),
	        		    s.getSerieProduit(),
	        		    s.getQuantiteCycle(),
	        		    s.getNumeroMachine(),
	        		    s.getForceTractionC1Ech1(),
	        		    s.getForceTractionC1Ech2(),
	        		    s.getForceTractionC1Ech3(),
	        		    s.getForceTractionC1EchFin(),
	        		    s.getForceTractionC2Ech1(),
	        		    s.getForceTractionC2Ech2(),
	        		    s.getForceTractionC2Ech3(),
	        		    s.getForceTractionC2EchFin(),
	        		    s.getDecision(),
	        		    s.getRempliePlanAction(),
	        		    s.getPdekSertissageIDC().getId()  ,
	  	                s.getPagePDEK().getPageNumber()  ,
	     	            s.getZone() , 
	     	            s.getHeureCreation() ,
	     	            s.getUserSertissageIDC().getMatricule() , 
	     	            s.getSegment()

		         )
		     ).collect(Collectors.toList());
	    

	     return ResponseEntity.ok(sertissagesDTOs);
	 }


 
}
