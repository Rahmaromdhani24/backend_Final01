package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.AjoutSoudureResultDTO;
import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServiceTorsadage;

@Service
public class TorsadageServiceImplimentation implements ServiceTorsadage {

	 @Autowired    private PdekRepository pdekRepository;
	 @Autowired    private TorsadageRepository torsadageRepository;	
	 @Autowired    private UserRepository userRepository;	
	 @Autowired    private ProjetRepository projetRepository;	
	 @Autowired    private PdekPageRepository pdekPageRepository;	
	 @Autowired    private AuditLogRepository auditLogRepository;
     @Autowired private PlanActionRepository planActionRepository ; 
     @Autowired private DetailsPlanActionRepository detailsPlanActionRepository ; 
	 @Autowired    private ControleQualiteRepository controleQualiteRepository;

	 
	 @Override
	public AjoutTorsadageResultDTO ajoutPDEK_Torsadage(Torsadage instanceTorsadage, int matriculeOperateur , String projet) {
	
    String specificationMesureSelectionner = instanceTorsadage.getSpecificationMesure() ; 
    User user = userRepository.findByMatricule(matriculeOperateur).get() ; 
    
    Torsadage  instance1 = new Torsadage() ; 
    instance1.setCode(instanceTorsadage.getCode());
    instance1.setSpecificationMesure(specificationMesureSelectionner);
    instance1.setDate(instanceTorsadage.getDate());
    instance1.setHeureCreation(instanceTorsadage.getHeureCreation());
    instance1.setNumCommande(instanceTorsadage.getNumCommande());
    instance1.setLongueurFinalDebutCde(instanceTorsadage.getLongueurFinalDebutCde());
    instance1.setLongueurBoutDebutCdeC1(instanceTorsadage.getLongueurBoutDebutCdeC1());
    instance1.setLongueurBoutDebutCdeC2(instanceTorsadage.getLongueurBoutDebutCdeC2());
    instance1.setLongueurBoutFinCdeC1(instanceTorsadage.getLongueurBoutFinCdeC1());
    instance1.setLongueurBoutFinCdeC2(instanceTorsadage.getLongueurBoutFinCdeC2());
    instance1.setDecalageMaxDebutCdec1(instanceTorsadage.getDecalageMaxDebutCdec1());
    instance1.setDecalageMaxDebutCdec2(instanceTorsadage.getDecalageMaxDebutCdec2());
    instance1.setNumerofil(instanceTorsadage.getNumerofil());
    instance1.setLongueurFinalFinCde(instanceTorsadage.getLongueurFinalFinCde());
    instance1.setEch1(instanceTorsadage.getEch1());
    instance1.setEch2(instanceTorsadage.getEch2());
    instance1.setEch3(instanceTorsadage.getEch3());
    instance1.setEch4(instanceTorsadage.getEch4());
    instance1.setEch5(instanceTorsadage.getEch5());
    instance1.setDecalageMaxFinCdec1(instanceTorsadage.getDecalageMaxFinCdec1());
    instance1.setDecalageMaxFinCdec2(instanceTorsadage.getDecalageMaxFinCdec2());
    instance1.setLongueurPasFinCde(instanceTorsadage.getLongueurPasFinCde());
    instance1.setQuantiteAtteint(instanceTorsadage.getQuantiteAtteint());
    instance1.setQuantiteTotale(instanceTorsadage.getQuantiteTotale());
    instance1.setEtendu(instanceTorsadage.getEtendu());
    instance1.setMoyenne(instanceTorsadage.getMoyenne());

	instance1.setUserTorsadage(user);
 /***** Recuperation PDEK ID s'il exise ****************/
  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_Torsadage(specificationMesureSelectionner , user.getSegment() , user.getPlant() , projet );

    if (pdekExiste.isPresent()) {
        // Si le Pdek existe, tu peux le récupérer et effectuer tes opérations
        PDEK pdek = pdekExiste.get();
        
        // ajout instance dans la table remplissage pdek 
        if (!pdek.getUsersRempliePDEK().contains(user)) {
            pdek.getUsersRempliePDEK().add(user);
        }
        // remplissage instance dans la table projet_pdek 
        if (!pdek.getProjets().contains(projetRepository.findByNom(projet).get())) {
            pdek.getProjets().add(projetRepository.findByNom(projet).get());
        }
        
        // 3. Trouver la dernière page du PDEK
        PagePDEK pagePDEK = pdekPageRepository.findFirstByPdekOrderByPageNumberDesc(pdek).get() ; 
            
        // 4. Compter le nombre de pistolets sur la page actuelle
        long nombreTorsadagesDansPage = torsadageRepository.countByPagePDEK(pagePDEK);
        int numeroCycle;

        if (nombreTorsadagesDansPage < 25) {
            // Ajouter le pistolet à la même page
            numeroCycle = (int) nombreTorsadagesDansPage + 1;
        }
        else {
            // Si la page est pleine, créer une nouvelle page
            pagePDEK = new PagePDEK(pdek.getTotalPages() + 1, false, pdek);
            pdekPageRepository.save(pagePDEK);
            // Mettre à jour le total de pages du PDEK
            pdek.setTotalPages(pdek.getTotalPages() + 1);
            pdekRepository.save(pdek);
            numeroCycle = 1; // Réinitialiser le cycle pour la nouvelle page
        }

        
         instance1.setPdekTorsadage(pdek);
         instance1.setPagePDEK(pagePDEK);
         instance1.setNumeroCycle(numeroCycle);
        torsadageRepository.save(instance1) ;
	    return new AjoutTorsadageResultDTO(pdek.getId(), pagePDEK.getPageNumber() , instance1.getId());
    } else {
      
    	PDEK newPDEK = new PDEK() ; 
    	newPDEK.setSectionFil(specificationMesureSelectionner);
    	newPDEK.setNombreEchantillons("3 Piéces ");
    	newPDEK.setSegment(user.getSegment());
    	newPDEK.setNumMachine(user.getMachine());
        newPDEK.setDateCreation(instanceTorsadage.getDate());
    	newPDEK.setTypeOperation(TypesOperation.Torsadage);
    	newPDEK.setPlant(user.getPlant());
    	newPDEK.setUsersRempliePDEK(List.of(user));
    	newPDEK.setTotalPages(1);
    	instance1.setNumeroCycle(1);
    	pdekRepository.save(newPDEK)  ;
    	PagePDEK newPage = new PagePDEK(1, false, newPDEK);
        pdekPageRepository.save(newPage);
        instance1.setPagePDEK(newPage);
	  if (!newPDEK.getProjets().contains(projetRepository.findByNom(projet).get())) {
		  newPDEK.getProjets().add(projetRepository.findByNom(projet).get()); // Ajouter le projet au PDEK
		  projetRepository.findByNom(projet).get().getPdeks().add(newPDEK);
    	projetRepository.save(projetRepository.findByNom(projet).get());
    	instance1.setPdekTorsadage(newPDEK);
    	torsadageRepository.save(instance1) ;   	

	      }
		  return new AjoutTorsadageResultDTO(newPDEK.getId(), newPage.getPageNumber() , instance1.getId());

    }
	 }

 @Override
	 public Map<Integer, List<TorsadageDTO>> recupererTorsadagesParPDEKGroupéesParPage(String specificationMesure, int segment, Plant plant, String nomProjet) {
	     Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_Torsadage(specificationMesure, segment, plant, nomProjet);

	     if (pdekExiste.isPresent()) {
	         PDEK pdek = pdekExiste.get();
	         List<Torsadage> torsadages = torsadageRepository.findByPdekTorsadage_Id(pdek.getId());

	         // Grouper les soudures par numéro de page
	         return torsadages.stream()
	                 .collect(Collectors.groupingBy(
	                         s -> s.getPagePDEK().getPageNumber(), // groupement par numéro de page
	                         Collectors.mapping(
	                                 s -> new TorsadageDTO(
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
	                          	            s.getRempliePlanAction(),
	                          	            s.getPdekTorsadage().getId()  ,
	                             	        s.getPagePDEK().getPageNumber() ,
	            	                	    s.getZone() 
),
	                                 Collectors.toList()
	                         )
	                 ));
	     } else {
	         return Map.of();
	     }
	 }

	 ///////
	 public int getLastNumeroCycle(String specificationMesureSelectionner, int segment, Plant nomPlant, String projetName) {
		 // 1️⃣ Récupérer le PDEK correspondant
		 Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_Torsadage(specificationMesureSelectionner, segment, nomPlant, projetName);
	 
		 if (pdekOpt.isEmpty()) {
			 // Aucun PDEK trouvé → retourner 0
			 return 0;
		 }
	 
		 PDEK pdek = pdekOpt.get();
	 
		 // 2️⃣ Récupérer la dernière page associée au PDEK
		 Optional<PagePDEK> lastPageOpt = pdekPageRepository.findFirstByPdekOrderByPageNumberDesc(pdek);
	 
		 if (lastPageOpt.isEmpty()) {
			 // Le PDEK existe, mais aucune page n'est encore créée → retourner 0
			 return 0;
		 }
	 
		 PagePDEK lastPage = lastPageOpt.get();
	 
		 // 3️⃣ Vérifier s'il existe des soudures dans cette page
		 long nombreSouduresDansPage = torsadageRepository.countByPagePDEK(lastPage);
	 
		 if (nombreSouduresDansPage == 0) {
			 // Si la page est vide, retourner 0
			 return 0;
		 }
	 
		 // 4️⃣ Récupérer le dernier numéro de cycle
		 Optional<Torsadage> lastSoudureOpt = torsadageRepository.findTopByPagePDEK_IdOrderByNumeroCycleDesc(lastPage.getId());
	 
		 if (lastSoudureOpt.isPresent()) {
			 // Si une soudure est présente, retourner son numéro de cycle
			 return lastSoudureOpt.get().getNumeroCycle();
		 }
	 
		 // Si aucune soudure n'est trouvée malgré les vérifications, retourner 0
		 return 0;
	 }
	 
	 public List<TorsadageDTO> recupererTorsadagesParPageActuel(String specificationMesure, int segment, Plant plant, String nomProjet) {
		Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_Torsadage(specificationMesure, segment, plant, nomProjet);
	
		if (pdekExiste.isPresent()) {
			PDEK pdek = pdekExiste.get();
			// Identifier la page PDEK actuelle (celle utilisée pour ajouter de nouvelles soudures)
			Optional<PagePDEK> pageActuelleOpt = pdekPageRepository.findPageActuelleByPdekId(pdek.getId());
	
			if (pageActuelleOpt.isPresent()) {
				int numeroPageActuelle = pageActuelleOpt.get().getPageNumber();
	
				List<Torsadage> torsadagesPageActuelle = torsadageRepository.findByPdekTorsadage_IdAndPagePDEK_PageNumber(
					pdek.getId(),
					numeroPageActuelle
				);
	
				return torsadagesPageActuelle.stream()
						.map(s -> new TorsadageDTO(
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
						            s.getRempliePlanAction(),
						            s.getPdekTorsadage().getId()  ,
                         	        s.getPagePDEK().getPageNumber() ,
        	                	    s.getZone() 
))
						.collect(Collectors.toList());
			}
		}
	
		return List.of(); // Si rien trouvé
	}
	 
	// pour agent de qualite
	@Override
	public List<TorsadageDTO> getTorsadagsNonValidees() {
	    List<Torsadage> torsadages = torsadageRepository.findByDecisionAndRempliePlanAction(0, 0);

	    return torsadages.stream()
	       .map(s -> new TorsadageDTO(
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

	        ))
	        .toList();
	}

	@Override
	public List<TorsadageDTO> getTorsadagesNonValideesTechniciens() {
		   List<Torsadage> torsadages = torsadageRepository.findByDecisionAndRempliePlanAction(0 , 1);

	        return torsadages.stream()
	        		   .map(s -> new TorsadageDTO(
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

	        		        ))
	        		        .toList();
	        		}

	@Override
	public List<TorsadageDTO> getTorsadagsValidees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validerTorsadage(Long idTorsadage, int matriculeUser) {
		 String heure = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		   String date  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    // Valider le pistolet
	    torsadageRepository.validerTorsadage(idTorsadage);

	    // Récupérer le pistolet concerné
	    Torsadage torsadage = torsadageRepository.findById(idTorsadage)
	        .orElseThrow(() -> new RuntimeException("Torsadage non trouvé avec ID : " + idTorsadage));

	    // Récupérer le PDEK associé
	    PDEK pdek = torsadage.getPdekTorsadage() ; 

	    // Récupérer l'utilisateur via son matricule
	    User userControleur = userRepository.findByMatricule(matriculeUser).get() ;

	    // Créer l'entrée de contrôle qualité
	    ControleQualite controle = ControleQualite.builder()
	        .user(userControleur)
	        .pdek(pdek)
	        .idInstanceOperation(torsadage.getId())
	        .nombrePage(pdek.getPages() != null ? pdek.getPages().size() : 0)
	        .dateControle(date)
	        .heureControle(heure)
	        .resultat("Validé")
	        .build();

	    // Sauvegarder le contrôle qualité
	    controleQualiteRepository.save(controle);
	    
	    
	    //valider Plan action si existe 
	    
	    // Étape 1 : récupérer la page PDEK du pistolet
	    PagePDEK page = torsadageRepository.findPDEKByPagePDEK(idTorsadage);
	    if (page == null) return;

	    // Étape 2 : récupérer le plan d’action
	    Optional<PlanAction> planOpt = planActionRepository.findByPagePDEKId(page.getId());
	    if (planOpt.isEmpty()) return;

	    PlanAction plan = planOpt.get();

	    // Étape 3 : récupérer les détails
	    List<DetailsPlanAction> detailsList = detailsPlanActionRepository.findByPlanActionId(plan.getId());

	    // Étape 4 : modifier les signatures si nécessaire
	    for (DetailsPlanAction detail : detailsList) {
	        if (detail.getMatricule_operateur() == (torsadage.getUserTorsadage().getMatricule()) && detail.getSignature_qualite() == 0) {
	            detail.setSignature_qualite(1);
	            detailsPlanActionRepository.save(detail); // sauvegarde
	        }
	    }
	}

	@Override
	public List<UserDTO> getUserDTOsByPdek(Long idPdek) {
		  List<User> users = torsadageRepository.findUsersByPdekId(idPdek);
	        return users.stream()
	                    .map(UserDTO::fromEntity)
	                    .toList(); // ou collect(Collectors.toList()) si tu es en Java 8
	    }

	@Override
	public boolean changerAttributRempliePlanActionTorsadageDe0a1(Long id , String zoneCouleur) {
		  Optional<Torsadage> optionalTorsadage = torsadageRepository.findById(id);
	        if (optionalTorsadage.isPresent()) {
	            Torsadage torsadage = optionalTorsadage.get();
	            torsadage.setRempliePlanAction(1);  
	            torsadage.setZone(zoneCouleur);  // changer à 1

	            torsadageRepository.save(torsadage);
	            return true;
	        }
	        return false;
	    
	}

	 
}
