package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.OperateurErreurDTO;
import rahma.backend.gestionPDEK.DTO.OperateurErreurPistolet;
import rahma.backend.gestionPDEK.DTO.StatPistolet;
import rahma.backend.gestionPDEK.DTO.StatProcessus;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.DetailsPlanActionRepository;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import rahma.backend.gestionPDEK.Repository.PistoletRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import rahma.backend.gestionPDEK.Repository.SertissageIDCRepository;
import rahma.backend.gestionPDEK.Repository.SertissageNormalRepository;
import rahma.backend.gestionPDEK.Repository.SoudureRepository;
import rahma.backend.gestionPDEK.Repository.TorsadageRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.StatistiquesService;

@Service
public class StatistiquesImplimentation implements StatistiquesService {


	 @Autowired    private UserRepository userRepository;	
	 @Autowired    private SertissageNormalRepository sertissageNormalRepository;	
	 @Autowired    private SertissageIDCRepository sertissageIDCRepository;	
	 @Autowired    private SoudureRepository soudureRepository;	
	 @Autowired    private TorsadageRepository torsadageRepository;	
	 @Autowired    private PdekRepository pdekRepository;	
	 @Autowired    private PlanActionRepository planActionRepository;	
	 @Autowired    private PistoletRepository pistoletRepository;	
	 @Autowired    private DetailsPlanActionRepository detailsPlanActionRepository;	


	@Override
	public long nombreTotalOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE");
	    return userRepository.countByRoleNomIn(rolesOperateurs);
	}


	@Override
	public long nombreHommesOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE" , "TECHNICIEN");
	    return userRepository.countByRoleNomInAndSexe(rolesOperateurs , "homme");
	}


	@Override
	public long nombreFemmesOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE" ,"TECHNICIEN");
	    return userRepository.countByRoleNomInAndSexe(rolesOperateurs , "femme");
	}

	@Override
	public double calculerPourcentageAugmentationOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE", "TECHNICIEN");

	    String anneeActuelle = String.valueOf(LocalDate.now().getYear());
	    String anneePrecedente = String.valueOf(LocalDate.now().getYear() - 1);

	    long totalAnneePrecedente = userRepository.compterParRoleEtAnnee(rolesOperateurs, anneePrecedente);
	    long totalAnneeActuelle = userRepository.compterParRoleEtAnnee(rolesOperateurs, anneeActuelle);

	    // Éviter la division par zéro
	    if (totalAnneePrecedente == 0) {
	        return 0.0;
	    }

	    double variation = ((double)(totalAnneeActuelle - totalAnneePrecedente) / totalAnneePrecedente) * 100.0;
	    return variation;
	}

/************************ erreurs process ******************************************/
	@Override
	public long nombreErreursSertissageNormalCetteSemaine() {
		  // 1) Calcul des bornes de la semaine
        LocalDate today = LocalDate.now();
        LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 2) Formatage en String « yyyy-MM-dd »
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = lundi.format(fmt);
        String endDate   = today.format(fmt);

        // 3) Compte des erreurs en fonction de la zone (non nulle)
        return sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
    }


@Override
public long nombreErreursSertissageIDCCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
}


@Override
public long nombreErreursSoudureCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return soudureRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
}


@Override
public long nombreErreursTorsadgeCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return torsadageRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
	
}


@Override
public long nombreErreursTotalCetteSemaineSaufPistolet() {
	
	long erreursSertissagesNormal = nombreErreursSertissageNormalCetteSemaine() ; 
	long erreursSertissagesIDC = nombreErreursSertissageIDCCetteSemaine() ; 
	long erreursSoudure = nombreErreursSoudureCetteSemaine() ;  
	long erreursTorsadage = nombreErreursTorsadgeCetteSemaine() ; 
	return erreursSertissagesNormal + erreursSertissagesIDC + erreursSoudure + erreursTorsadage;
}

@Override
public double calculerPourcentageSemainePrecdant() {
    LocalDate today = LocalDate.now();
    LocalDate debutSemaineCourante = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate finSemaineCourante = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    LocalDate debutSemainePrecedente = debutSemaineCourante.minusWeeks(1);
    LocalDate finSemainePrecedente = debutSemaineCourante.minusDays(1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startCurrent = debutSemaineCourante.format(formatter);
    String endCurrent = finSemaineCourante.format(formatter);
    String startPrev = debutSemainePrecedente.format(formatter);
    String endPrev = finSemainePrecedente.format(formatter);

    long erreursCurrent =
        sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + soudureRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + torsadageRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent);

    long erreursPrev =
        sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + soudureRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + torsadageRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev);

    if (erreursPrev == 0) {
        return erreursCurrent == 0 ? 0.0 : erreursCurrent; // retourne le nombre brut d'erreurs comme "indice"
    }

    double difference = erreursCurrent - erreursPrev;
    return (difference / (double) erreursPrev) * 100.0;
}


@Override
public Map<String, Long> getNombrePdekParTypeOperation() {
    String currentYear = String.valueOf(LocalDate.now().getYear());
    List<Object[]> results = pdekRepository.countPdekByTypeOperationForYear(currentYear);
    Map<String, Long> map = new HashMap<>();
    for (Object[] row : results) {
        String type = (String) row[0]; // type_operation est maintenant une String si requête native
        Long count = ((Number) row[1]).longValue(); // sécurité pour éviter ClassCastException
        map.put(type, count);
    }
    return map;
}


@Override
public Map<String, Long> getNombrePlanActionParTypeOperation() {
	  String currentYear = String.valueOf(LocalDate.now().getYear());
	    List<Object[]> results = planActionRepository.countPlanActionByTypeOperationForYear(currentYear);
	    Map<String, Long> map = new HashMap<>();
	    for (Object[] row : results) {
	        String type = (String) row[0]; // type_operation est maintenant une String si requête native
	        Long count = ((Number) row[1]).longValue(); // sécurité pour éviter ClassCastException
	        map.put(type, count);
	    }
	    return map;
	}


@Override
public List<OperateurErreurDTO> getTopOperateursWithErrors() {
    List<OperateurErreurDTO> dtos = new ArrayList<>();

    // Sertissage Normal
    List<Object[]> sertissageResults = sertissageNormalRepository.findTop3OperateursWithErrors();
    for (Object[] result : sertissageResults) {
        User user = (User) result[0];
        long nombreErreurs = (long) result[1];

        dtos.add(buildOperateurErreurDTO(user, nombreErreurs));
    }

    // Soudure
    List<Object[]> soudureResults = soudureRepository.findTop3OperateursWithErrors();
    for (Object[] result : soudureResults) {
        User user = (User) result[0];
        long nombreErreurs = (long) result[1];

        dtos.add(buildOperateurErreurDTO(user, nombreErreurs));
    }

    // Torsadage
    List<Object[]> torsadageResults = torsadageRepository.findTop3OperateursWithErrors();
    for (Object[] result : torsadageResults) {
        User user = (User) result[0];
        long nombreErreurs = (long) result[1];

        dtos.add(buildOperateurErreurDTO(user, nombreErreurs));
    }

    // Sertissage IDC (si nécessaire)
    List<Object[]> sertissageIDCResults = sertissageIDCRepository.findTop3OperateursWithErrors();
    for (Object[] result : sertissageIDCResults) {
        User user = (User) result[0];
        long nombreErreurs = (long) result[1];

        dtos.add(buildOperateurErreurDTO(user, nombreErreurs));
    }

    return dtos;
}

private OperateurErreurDTO buildOperateurErreurDTO(User user, long nombreErreurs) {
    return new OperateurErreurDTO(
        user.getMatricule(),
        user.getPrenom() + " " + user.getNom(),
        user.getPoste(),
        user.getMachine(),
        user.getRole().getNom(),
        user.getPlant().toString(),
        user.getSegment(),
        user.getTypeOperation().toString(),
        nombreErreurs
    );
}


@Override
	 public List<StatProcessus> getStatsByPlant(Plant plant) {
	        String currentYear = String.valueOf(LocalDate.now().getYear());
	        List<StatProcessus> stats = new ArrayList<>();
	        stats.addAll(sertissageIDCRepository.getStatsSertissageIDCByPlant(plant , currentYear));
	        stats.addAll(sertissageNormalRepository.getStatsSertissageNormalByPlant(plant , currentYear));
	        stats.addAll(soudureRepository.getStatsSoudureByPlant(plant , currentYear));
	        stats.addAll(torsadageRepository.getStatsTorsadageByPlant(plant , currentYear));

	        return stats;
	    }


@Override
public List<StatProcessus> getStatsChefLigneSertissageIDC(Plant plant, int segment) {
	  return sertissageIDCRepository.findStatsByChefLigneSertissageIDC(plant, segment);

}
@Override
public List<StatProcessus> getStatsChefLigneSertissageNormal(Plant plant, int segment) {
	  return sertissageNormalRepository.findStatsByChefLigneSertissageNormal(plant, segment);

}


@Override
public List<StatProcessus> getStatsChefLigneSoudure(Plant plant, int segment) {
	  return soudureRepository.findStatsByChefLigneSoudure(plant, segment);
}


@Override
public List<StatProcessus> getStatsChefLigneTorsadage(Plant plant, int segment) {
	  return torsadageRepository.findStatsByChefLigneTorsadage(plant, segment);
}



@Override
public List<StatPistolet>  getNombrePdekPistoletsParCouleursPistoletsEtTypePistolet() {
	String currentYear = String.valueOf(LocalDate.now().getYear());  // ex: "2025"

    List<Object[]> rawResults = pistoletRepository.countPdekByCategorieAndCouleurForYear(currentYear);

    return rawResults.stream()
        .map(obj -> new StatPistolet(
            obj[0] != null ? obj[0].toString() : "Inconnue",
            obj[1] != null ? obj[1].toString() : "Inconnue",
            ((Number) obj[2]).intValue() ,
            0
        ))
        .toList();
}
@Override
public Map<String, Long> getNombrePlanActionPistoletsParCouleursPistoletsEtTypePistolet() {
    String currentYear = String.valueOf(LocalDate.now().getYear());
    List<Object[]> results = planActionRepository.countPlanActionByPistoletsForYear(currentYear);

    Map<String, Long> erreursParCategorie = new HashMap<>();
    for (Object[] row : results) {
        String typePistolet = (String) row[0];
        String categorie = (String) row[1];
        long count = ((Number) row[2]).longValue();

        String key = typePistolet + " - " + categorie;
        erreursParCategorie.put(key, count);
    }

    return erreursParCategorie;
}



@Override
public long nombreErreursPistoletsCetteSemaine() {
	 LocalDate today = LocalDate.now();
	    LocalDate lundiDerniereSemaine = today.minusWeeks(1).with(DayOfWeek.MONDAY);
	    LocalDate dimancheCetteSemaine = today.with(DayOfWeek.SUNDAY);

	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String startDate = lundiDerniereSemaine.format(fmt);
	    String endDate = dimancheCetteSemaine.format(fmt);

	    return detailsPlanActionRepository.countDetailsPlanActionBetweenDatesWithPistolet(startDate, endDate);
	}


@Override
public double calculerPourcentageErreursPistoletSemainePrecedente() {
    LocalDate today = LocalDate.now();
    LocalDate debutSemaineCourante = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate finSemaineCourante = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    LocalDate debutSemainePrecedente = debutSemaineCourante.minusWeeks(1);
    LocalDate finSemainePrecedente = debutSemaineCourante.minusDays(1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startCurrent = debutSemaineCourante.format(formatter);
    String endCurrent = finSemaineCourante.format(formatter);
    String startPrev = debutSemainePrecedente.format(formatter);
    String endPrev = finSemainePrecedente.format(formatter);

    long erreursCurrent = detailsPlanActionRepository.countDetailsPlanActionBetweenDatesWithPistolet(startCurrent, endCurrent);
    long erreursPrev = detailsPlanActionRepository.countDetailsPlanActionBetweenDatesWithPistolet(startPrev, endPrev);

    if (erreursPrev == 0) {
        return erreursCurrent == 0 ? 0.0 : erreursCurrent; // si 0 erreurs en semaine précédente, retourne le nombre brut
    }

    double difference = erreursCurrent - erreursPrev;
    return (difference / (double) erreursPrev) * 100.0;
}



@Override
public List<OperateurErreurPistolet> getTopAgentsQualitePistoletWithErrors() {
    int currentYear = LocalDate.now().getYear();
    List<Object[]> results = detailsPlanActionRepository.findTopOperateursPistoletWithErrors(currentYear);
    List<OperateurErreurPistolet> dtos = new ArrayList<>();

    for (Object[] row : results) {
        int matricule = ((Number) row[0]).intValue();
        String typePistoletStr = (row[1] instanceof TypePistolet) 
        	    ? ((TypePistolet) row[1]).name() 
        	    : (String) row[1];
        String categoriePistolet = (String) row[2];
        long nombreErreurs = ((Number) row[3]).longValue();

        Optional<User> optionalUser = userRepository.findById(matricule);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            OperateurErreurPistolet dto = new OperateurErreurPistolet(
                user.getMatricule(),
                user.getNom() + " " + user.getPrenom(),
                user.getPlant().toString(),
                user.getSegment(),
                typePistoletStr,
                categoriePistolet,
                nombreErreurs
            );
            dtos.add(dto);
        }
    }

    return dtos;
}



@Override
public List<StatProcessus> getStatsPistoletsByPlant(Plant plant) {
	 List<Object[]> raw = detailsPlanActionRepository.getStatsPistoletByTypeAndPlant(plant.name());

     return raw.stream()
         .map(row -> {
             // row[0] = TypePistolet (enum)
             // row[1] = nombre de PDEK distincts  (Long)
             // row[2] = nombre total de détails   (Long)
             String typePistolet = row[0] != null
                 ? row[0].toString()
                 : "Inconnu";
             String categoriePistolet = row[1] != null
                     ? row[1].toString()
                     : "Inconnu";
             long nombrePdek = row[2] != null
                 ? ((Number) row[2]).longValue()
                 : 0L;
             long nombreErreurs = row[3] != null
                 ? ((Number) row[3]).longValue()
                 : 0L;

             return new StatProcessus(typePistolet, categoriePistolet ,nombrePdek, nombreErreurs);
         })
         .collect(Collectors.toList());
 }

}
