package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rahma.backend.gestionPDEK.DTO.*;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pdek")
@RequiredArgsConstructor
public class PdekController {

	 @Autowired  private  PDEK_ServiceImplimenetation  pdekService;
	 @Autowired  private  PdekRepository pdekRepository ;
	 @Autowired  private  PlanActionImplimenetation planActionService ;

	 @GetMapping("/pdeks/{typeOperation}")
	 public List<PdekResultat> getPdekLightByTypeOperation(@PathVariable String typeOperation) {
	     try {
	         TypesOperation operationEnum = TypesOperation.valueOf(typeOperation);
	         List<PDEK> pdeks = pdekRepository.findByTypeOperation(operationEnum);

	         return pdeks.stream().map(pdek ->
	         new PdekResultat(
	             pdek.getId(),
	             pdek.getNumeroPistolet() ,
	             pdek.getTypeOperation(),
	             pdek.getTypePistolet(),
	             pdek.getCategoriePistolet(),
	             pdek.getPlant(),
	             pdek.getSegment(),
	             pdek.getTotalPages(),
	             pdek.getUsersRempliePDEK() != null
	                 ? pdek.getUsersRempliePDEK().stream()
	                     .map(user -> user.getMatricule() + " - " + user.getNom() + " " + user.getPrenom())
	                     .toList()
	                 : List.of()
	         )
	     ).toList();



	     } catch (IllegalArgumentException e) {
	         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'opération invalide : " + typeOperation);
	     }
	 }

	 @GetMapping("/pdeksAllSaufPistolet/{typeOperation}")
	 public List<PdekGeneral> getPdekLightByTypeOperationSaufPistolet(@PathVariable String typeOperation) {
	     try {
	         TypesOperation operationEnum = TypesOperation.valueOf(typeOperation);
	         List<PDEK> pdeks = pdekRepository.findByTypeOperation(operationEnum);

	         return pdeks.stream().map(pdek -> {
	             List<User> users = pdek.getUsersRempliePDEK();

	             String numPoste = users != null && !users.isEmpty() ? users.get(0).getPoste() : null;

	             Object usersMatricules = users != null
	                 ? users.stream()
	                     .map(user -> user.getMatricule() + " - " + user.getNom() + " " + user.getPrenom())
	                     .toList()
	                 : List.of();

	             return new PdekGeneral(
	                 pdek.getId(),
	                 pdek.getTotalPages(),
	                 pdek.getSectionFil(),
	                 pdek.getFrequenceControle(),
	                 pdek.getSegment(),
	                 pdek.getNumMachine(),
	                 pdek.getDateCreation(),
	                 pdek.getTypeOperation(),
	                 pdek.getPlant(),
	                 pdek.getNumeroOutils(),
	                 pdek.getNumeroContacts(),
	                 pdek.getLGD(),
	                 pdek.getTolerance(),
	                 pdek.getPosGradant(),
	                 usersMatricules,
	                 numPoste
	             );
	         }).toList();

	     } catch (IllegalArgumentException e) {
	         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'opération invalide : " + typeOperation);
	     }
	 }

	 @GetMapping("/contenu-par-page/{id}")
	 public ResponseEntity<List<ContenuPagePdekDTO>> getContenuParPage(@PathVariable Long id) {
	     List<ContenuPagePdekDTO> contenu = pdekService.getContenuParPage(id);
	     return ResponseEntity.ok(contenu);
	 }

	 @GetMapping("/{id}")
	 public ResponseEntity<?> getPdekById(@PathVariable Long id) {
	     Object dto = pdekService.getPdekDTOById(id);
	     return ResponseEntity.ok(dto);
	 }
	
	 @GetMapping("/pdekEnServiceAvecPlans/{typeOperation}")
	 public List<PdekAvecPlansDTO> getPdeksEnServiceAvecPlans(@PathVariable String typeOperation) {
	     try {
	         TypesOperation operationEnum = TypesOperation.valueOf(typeOperation);
	         List<PDEK> pdeks = pdekRepository.findByTypeOperation(operationEnum);

	         return pdeks.stream().map(pdek -> {
	             List<User> users = pdek.getUsersRempliePDEK();

	             String numPoste = users != null && !users.isEmpty() ? users.get(0).getPoste() : null;
	             Object usersMatricules = users != null
	                 ? users.stream().map(user -> user.getMatricule() + " - " + user.getNom() + " " + user.getPrenom()).toList()
	                 : List.of();

	             List<PlanActionDTO> plans = planActionService.testerPdeksProcessPossedePlanAction(pdek.getId());

	             return new PdekAvecPlansDTO(
	                 pdek.getId(),
	                 pdek.getTotalPages(),
	                 pdek.getSectionFil(),
	                 pdek.getFrequenceControle(),
	                 pdek.getSegment(),
	                 pdek.getNumMachine(),
	                 pdek.getDateCreation(),
	                 pdek.getTypeOperation(),
	                 pdek.getPlant(),
	                 pdek.getNumeroOutils(),
	                 pdek.getNumeroContacts(),
	                 pdek.getLGD(),
	                 pdek.getTolerance(),
	                 pdek.getPosGradant(),
	                 usersMatricules,
	                 numPoste,
	                 plans
	             );
	         }).toList();

	     } catch (IllegalArgumentException e) {
	         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'opération invalide : " + typeOperation);
	     }
	 }

}
