package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.*;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.*;

@RestController
@RequestMapping("/statistiques")
public class StatistiquesController {

 
    @Autowired  private StatistiquesService service ; 


/*************************************** All process sauf pistolet **********************************/
    
    @GetMapping("/nombre-operateurs")
    public ResponseEntity<Long> getAllOperateurs() {
        return ResponseEntity.ok(service.nombreTotalOperateurs());
    }

    @GetMapping("/nombre-operateurs-hommes")
    public ResponseEntity<Long> getNombreHommesOperateurs() {
        return ResponseEntity.ok(service.nombreHommesOperateurs());
    }

    @GetMapping("/nombre-operateurs-femmes")
    public ResponseEntity<Long> getNombreFemmesOperateurs() {
        return ResponseEntity.ok(service.nombreFemmesOperateurs());
    }

    @GetMapping("/pourcentage-augmentation")
    public ResponseEntity<Double> getPourcentageAugmentation() {
    	double pourcentage = service.calculerPourcentageAugmentationOperateurs();
        return ResponseEntity.ok(pourcentage);
    }

    @GetMapping("/erreurs-semaine")
    public ResponseEntity<Long> getErreursAllProcessCetteSemaine() {
        long count = service.nombreErreursTotalCetteSemaineSaufPistolet();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/pourcentage-erreurs-semaine")
    public ResponseEntity<Double> getPourcentagesErreursAllProcessCetteSemaine() {
    	double count = service.calculerPourcentageSemainePrecdant();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/pdek-count-by-type")
    public ResponseEntity<Map<String, Long>> getPdekCountByTypeOperation() {
        Map<String, Long> stats = service.getNombrePdekParTypeOperation();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/planAction-count-by-type")
    public ResponseEntity<Map<String, Long>> getPlanActionCountByTypeOperation() {
        Map<String, Long> stats = service.getNombrePlanActionParTypeOperation() ;
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/top5-operateurs-erreurs")
    public ResponseEntity<List<OperateurErreurDTO>> getTop5OperateursWithErrors() {
        return ResponseEntity.ok(service.getTopOperateursWithErrors());
    }

    @GetMapping("/par-processus")
    public List<StatProcessus> getStatistiques(@RequestParam("plant") Plant plant) {
        return service.getStatsByPlant(plant); }
   
    @GetMapping("/chart-sertissage-idc")
    public List<StatProcessus> statsSertissageIDC( @RequestParam("plant") Plant plant, @RequestParam("segment") int segment ) {
        return service.getStatsChefLigneSertissageIDC(plant, segment);
    }
    
    @GetMapping("/chart-sertissage-normal")
    public List<StatProcessus> statsSertissageNormal( @RequestParam("plant") Plant plant, @RequestParam("segment") int segment ) {
        return service.getStatsChefLigneSertissageNormal(plant, segment);
    }
    
    @GetMapping("/chart-soudure")
    public List<StatProcessus> statsSoudure( @RequestParam("plant") Plant plant, @RequestParam("segment") int segment ) {
        return service.getStatsChefLigneSoudure(plant, segment);
    }
    
    @GetMapping("/chart-torsadage")
    public List<StatProcessus> statsTorsadage( @RequestParam("plant") Plant plant, @RequestParam("segment") int segment ) {
        return service.getStatsChefLigneTorsadage(plant, segment);
    }
    
    /***************** Partie Pistolet *******************/
    
    
    @GetMapping("/pistolet/stats-par-couleur-et-categorie")
    public ResponseEntity<List<StatPistolet>> getStatsPistoletsParCouleurEtCategorie() {
        List<StatPistolet> stats = service.getNombrePdekPistoletsParCouleursPistoletsEtTypePistolet();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/planActionPistolet-by-year")
    public ResponseEntity<Map<String, Long>> getPlanActionPistolets() {
        Map<String, Long> stats = service.getNombrePlanActionPistoletsParCouleursPistoletsEtTypePistolet() ; 
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/nombreErreursPistoletsCetteSemaine")
    public long nombreErreursPistoletsCetteSemaine() {
    	return service.nombreErreursPistoletsCetteSemaine() ;         
    }

    @GetMapping("/pourcentageErreursPistoletsCetteSemaine")
    public double pourcentageErreursPistoletsCetteSemaine() {
    	return service.calculerPourcentageErreursPistoletSemainePrecedente() ;         
    }

    
    @GetMapping("/topOperateursPistolets")
    public ResponseEntity<List<OperateurErreurPistolet>> getTop5OperateursPistoletsWithErrors() {
    return ResponseEntity.ok(service.getTopAgentsQualitePistoletWithErrors());
    	    }
    
    @GetMapping("/pistolet-statistiques-par-plant")
    public List<StatProcessus> getStatistiquesPistoletsParPlant(@RequestParam("plant") Plant plant) {
        return service.getStatsPistoletsByPlant(plant); }
 }

 

