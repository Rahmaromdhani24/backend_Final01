package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutSertissageResultDTO;
import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.SertissageNormalServiceImplimentation;

@RestController
@RequestMapping("/operations/SertissageNormal")
public class SertissageNormalController {
	   @Autowired private SertissageNormalRepository sertissageNormalRepository ; 
       @Autowired  private  SertissageNormalServiceImplimentation serviceSertissageNormal;

      

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return SertissageNormal.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return SertissageNormal.getDescriptionForCode(code);
    }
 

   

/********************** Nouvelle partie *********************/
@PostMapping("/ajouterPdekSertissageNormal")
public ResponseEntity<String> ajouterSertissageNormal(
         @RequestParam int matricule,
         @RequestParam String nomProjet,
         @RequestBody SertissageNormal sertissageNormal) {
    try {
    	 AjoutSertissageResultDTO result = serviceSertissageNormal.ajoutPDEK_SertissageNormal( sertissageNormal , matricule, nomProjet);
    	  // Retourner un objet JSON structuré avec l'ID du PDEK et le numéro de la page
         String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage() 
        		 + "\", \"idSertissage\": \"" + result.getIdSertissage() +"\" }";
         return ResponseEntity.ok(jsonResponse);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
    }
}

  @GetMapping("/contacts")
  public List<String> getDistinctContacts(@RequestParam String numeroOutil) {
      return serviceSertissageNormal.getDistinctContactsByNumeroOutil(numeroOutil);
  }
      @GetMapping("/sections")
      public ResponseEntity<List<String>> getSectionsFil(@RequestParam String numeroOutil, @RequestParam String numeroContact) {
    	    List<String> sectionsFil = serviceSertissageNormal.getSectionsByOutilAndContact(numeroOutil, numeroContact);
    	    if (sectionsFil.isEmpty()) {
    	        return ResponseEntity.notFound().build();
    	    }
    	    return ResponseEntity.ok(sectionsFil);
    	}
      
      @GetMapping("/hauteurSertissage")
      public ResponseEntity<String> getHauteurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getHauteurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      
      
      @GetMapping("/largeurSertissage")
      public ResponseEntity<String> getLargeurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getLargeurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceLargeurSertissage")
      public ResponseEntity<String> getToleranceLargeurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceLargeurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/hauteurIsolant")
      public ResponseEntity<String> getHauteurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur isolant
              String hauteurSertissage = serviceSertissageNormal.getHauteurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceHauteurIsolant")
      public ResponseEntity<String> getToleranceHauteurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceHauteurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/largeurIsolant")
      public ResponseEntity<String> getLargeurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getLargeurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceLargeurIsolant")
      public ResponseEntity<String> getToleranceLargeurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceLargeurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/traction")
      public ResponseEntity<String> getTractionValeur(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la valeur de traction
              String traction = serviceSertissageNormal.getTractionValeur(numeroOutil, numeroContact, sectionFil);
      
              return ResponseEntity.ok()
                      .contentType(MediaType.TEXT_PLAIN) // Empêche l'encodage HTML
                      .body(traction);
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .contentType(MediaType.TEXT_PLAIN)
                      .body(e.getMessage());
          }
      }
      @GetMapping("/tolerance")
      public ResponseEntity<String> getToleranceValue(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la valeur de traction
              String traction = serviceSertissageNormal.getToleranceValue(numeroOutil, numeroContact, sectionFil);
      
              return ResponseEntity.ok()
                      .contentType(MediaType.TEXT_PLAIN) // Empêche l'encodage HTML
                      .body(traction);
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .contentType(MediaType.TEXT_PLAIN)
                      .body(e.getMessage());
          }
      }
      @GetMapping("/lgd")
      public ResponseEntity<String> getLgd(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String valueLGD = serviceSertissageNormal.getLGDeValue(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(valueLGD); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/dernier-numero-cycle")
    public ResponseEntity<?> getLastNumeroCycle(
            @RequestParam String sectionFil,
            @RequestParam int segment,
            @RequestParam Plant nomPlant,
            @RequestParam String projetName) {
    
        int dernierNumeroCycle = serviceSertissageNormal.getLastNumeroCycle(sectionFil, segment, nomPlant, projetName);
    
        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
        return ResponseEntity.ok(dernierNumeroCycle);
    }
 @GetMapping("/sertissageNormal-par-pdek")
    public Map<Integer, List<SertissageNormal_DTO>> getSertissagesNormalParPdek(
            @RequestParam String sectionFil,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceSertissageNormal.recupererSertissagesNormalesParPDEKGroupéesParPage(sectionFil ,segment, plant, nomProjet);
    }
 
 @GetMapping("/sertissages-par-pdek-et-page")
 public ResponseEntity<List<SertissageNormal_DTO>> getSertissagesParPdekEtPage(
         @RequestParam Long pdekId,
         @RequestParam int pageNumber) {

     List<SertissageNormal> sertissages = sertissageNormalRepository.findByPdekSertissageNormal_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

     List<SertissageNormal_DTO> sertissagesDTOs = sertissages.stream().map(n ->
         new SertissageNormal_DTO(
        		 n.getId(),
                 n.getClass().getSimpleName() ,
                 n.getUserSertissageNormal().getPlant().toString() ,
                 n.getHeureCreation() ,
                 n.getCodeControle() ,  
                 n.getSectionFil(),
                 n.getNumeroOutils() , 
                 n.getNumeroContacts()  ,
                 n.getDate(),
                 n.getNumCycle(),
                 n.getUserSertissageNormal().getMatricule(),  
                 n.getHauteurSertissageEch1(),
                 n.getHauteurSertissageEch2(),
                 n.getHauteurSertissageEch3(),
                 n.getHauteurSertissageEchFin(),
                 n.getLargeurSertissage(), 
                 n.getLargeurSertissageEchFin(), 
                 n.getHauteurIsolant(),
                 n.getLargeurIsolant(),
                 n.getLargeurIsolantEchFin(),
                 n.getHauteurIsolantEchFin(),
                 n.getTraction(),
                 n.getTractionFinEch(),
                 n.getProduit(),
                 n.getSerieProduit(),
                 n.getQuantiteCycle(),
                 n.getSegment(),
                 n.getNumeroMachine(),
                 n.getDecision(),
                 n.getRempliePlanAction() ,
                 n.getPdekSertissageNormal().getId()  ,
      	         n.getPagePDEK().getPageNumber() , 
      	         n.getPdekSertissageNormal().getLGD() ,
     	          n.getZone()

         )
     ).collect(Collectors.toList());

     return ResponseEntity.ok(sertissagesDTOs);
 }
 @GetMapping("/sertissages-non-validees-agents-Qualite")
	public List<SertissageNormal_DTO> getSouduresNonValidees() {
	    return serviceSertissageNormal.getSertissagesNonValidees() ; 
	}
	@GetMapping("/nbrNotificationsAgentsQualite")
	   public int getNombreNotification() {
	       return serviceSertissageNormal.getSertissagesNonValidees().size() ; 
	}
	
	@GetMapping("/sertissages-validees")
	public List<SertissageNormal_DTO>  getSouduresValidees() {
	    return serviceSertissageNormal.getSertissagesValidees() ; 
	}
	 @GetMapping("/sertissages-non-validees-plan-action")
	    public List<SertissageNormal_DTO> getSertissagesIDCNonValideesAvecPlanAction() {
	        return serviceSertissageNormal.getSertissagesNonValideesChefLigne() ; 
	    }
	 
	 @GetMapping("/nbrNotificationsChefLigne")
	    public int getNombresNotificationsPistoletsNonValiderDePlanAction() {
	        return serviceSertissageNormal.getSertissagesNonValideesChefLigne().size(); }
	   
	 @PutMapping("/validerSertissage")
	 public ResponseEntity<?> validerPistolet(@RequestParam Long id, @RequestParam Integer matriculeAgentQualite) {
		 serviceSertissageNormal.validerSertissage(id, matriculeAgentQualite);
	     return ResponseEntity.ok().build(); }
	
	

	 @GetMapping("/users-by-pdek/{id}")
	    public ResponseEntity<List<UserDTO>> getUserDTOsByPdek(@PathVariable Long id) {
	        List<UserDTO> userDTOs = serviceSertissageNormal.getUserDTOsByPdek(id);
	        return ResponseEntity.ok(userDTOs);
	    }
	 
	 @PutMapping("/plan-action-zone/{zone}/{id}")
public ResponseEntity<String> remplirPlanAction(@PathVariable Long id , @PathVariable String zone) {
     boolean success = serviceSertissageNormal.changerAttributRempliePlanActionSertissageeDe0a1(id , zone);
     if (success) {
         return ResponseEntity.ok("Attribut rempliePlanAction mis à jour !");
     } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Soudure non trouvée.");
     }
     
	 }
  }

