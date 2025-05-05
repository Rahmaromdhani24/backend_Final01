package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rahma.backend.gestionPDEK.DTO.AjoutPistoletResultDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServicePistolet;

@Service
@RequiredArgsConstructor
public class PistoletServiceImplimenetation  implements ServicePistolet {

    @Autowired private PistoletRepository pistoletRepository;
    @Autowired private PdekRepository pdekRepository;
    @Autowired private PdekPageRepository pagePDEKRepository;
    @Autowired private UserRepository userRepository; 
    @Autowired private ControleQualiteRepository controleQualiteRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private PlanActionRepository planActionRepository ; 
    @Autowired private DetailsPlanActionRepository detailsPlanActionRepository ; 

    @Transactional
    public AjoutPistoletResultDTO ajouterPistolet(int matricule, Pistolet pistolet) {
        // 1. Vérifier si l'utilisateur existe
        User user = userRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérifier si un PDEK existe pour le type de pistolet
        PDEK pdek = pdekRepository.findUniquePDEK_MontagePistolet(pistolet.getType() ,user.getSegment() , pistolet.getNumeroPistolet() , pistolet.getCategorie() , user.getPlant() )
                .orElseGet(() -> {
                    PDEK newPdek = new PDEK();
                    newPdek.setTypePistolet(pistolet.getType());
                    newPdek.setDateCreation(pistolet.getDateCreation());
                    newPdek.setNombreEchantillons("5 Piéces") ; 
                    newPdek.setPlant(user.getPlant())  ; 
                    newPdek.setSegment(user.getSegment())  ; 
                    newPdek.setTypeOperation(TypesOperation.Montage_Pistolet) ;  
                    newPdek.setNumeroPistolet(pistolet.getNumeroPistolet()) ; 
                    newPdek.setCategoriePistolet(pistolet.getCategorie()) ; 
                    newPdek.setTotalPages(1);
                    return pdekRepository.save(newPdek);

                });

        // 3. Trouver la dernière page du PDEK
        PagePDEK pagePDEK = pagePDEKRepository.findFirstByPdekOrderByPageNumberDesc(pdek)
                .orElseGet(() -> {
                    PagePDEK newPage = new PagePDEK(1, false, pdek);
                    pagePDEKRepository.save(newPage);
                    return newPage;
                });

        // 4. Compter le nombre de pistolets sur la page actuelle
        long nombrePistoletsDansPage = pistoletRepository.countByPagePDEK(pagePDEK);
        int numeroCycle;

        if (nombrePistoletsDansPage < 25) {
            // Ajouter le pistolet à la même page
            numeroCycle = (int) nombrePistoletsDansPage + 1;
        } else {
            // Si la page est pleine, créer une nouvelle page
            pagePDEK = new PagePDEK(pdek.getTotalPages() + 1, false, pdek);
            pagePDEKRepository.save(pagePDEK);

            // Mettre à jour le total de pages du PDEK
            pdek.setTotalPages(pdek.getTotalPages() + 1);
            pdekRepository.save(pdek);

            numeroCycle = 1; // Réinitialiser le cycle pour la nouvelle page
        }

        // 5. Associer le Pistolet au PDEK et à l'utilisateur
        pistolet.setPdekPistolet(pdek);
        pistolet.setPagePDEK(pagePDEK);
        pistolet.setNumeroCycle(numeroCycle);
        pistolet.setUserPistolet(user); 
        pistolet.setSegment(user.getSegment()); 
        pistolet.setRempliePlanAction(pistolet.getRempliePlanAction()); 
        pistoletRepository.save(pistolet);

        // 6. Associer l'utilisateur au PDEK pour le remplissage (ManyToMany)
        if (pdek.getUsersRempliePDEK() == null) {
            pdek.setUsersRempliePDEK(new ArrayList<>());
        }

        if (!pdek.getUsersRempliePDEK().contains(user)) {
            pdek.getUsersRempliePDEK().add(user);
            pdekRepository.save(pdek);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        AuditLog audit = new AuditLog();
        audit.setAction("Ajout Pistolet");
        audit.setDescription("Ajout du pistolet N°" + pistolet.getNumeroPistolet() + " à la page " +
                             pagePDEK.getPageNumber() + " du PDEK ID " + pdek.getId());
        
        audit.setDateCreation(LocalDate.now().format(dateFormatter));
        audit.setHeureCreation(LocalTime.now().format(timeFormatter));
        audit.setUser(user);
        audit.setPdek_id(pdek.getId());
        audit.setPistolet_id(pistolet.getId());
        auditLogRepository.save(audit);
        
		return new AjoutPistoletResultDTO(pdek.getId(), pagePDEK.getPageNumber());
    }
    /*********************************************************************************************************************/
    public int getLastNumeroCycle(String typePistolet, int segment, int numPistolet ,String categorie , Plant nomPlant) {
		 // 1️⃣ Récupérer le PDEK correspondant
		 Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_MontagePistolet(TypePistolet.valueOf(typePistolet) ,segment , numPistolet , CategoriePistolet.valueOf(categorie) ,nomPlant);
	 
		 if (pdekOpt.isEmpty()) {
			 // Aucun PDEK trouvé → retourner 0
			 return 0;
		 }
	 
		 PDEK pdek = pdekOpt.get();
	 
		 // 2️⃣ Récupérer la dernière page associée au PDEK
		 Optional<PagePDEK> lastPageOpt = pagePDEKRepository.findFirstByPdekOrderByPageNumberDesc(pdek);
	 
		 if (lastPageOpt.isEmpty()) {
			 // Le PDEK existe, mais aucune page n'est encore créée → retourner 0
			 return 0;
		 }
	 
		 PagePDEK lastPage = lastPageOpt.get();
	 
		 // 3️⃣ Vérifier s'il existe des soudures dans cette page
		 long nombrePistoletDansPage = pistoletRepository.countByPagePDEK(lastPage);
	 
		 if (nombrePistoletDansPage == 0) {
			 // Si la page est vide, retourner 0
			 return 0;
		 }
	 
		 // 4️⃣ Récupérer le dernier numéro de cycle
		 Optional<Pistolet> lastPistoletOpt = pistoletRepository.findTopByPagePDEK_IdOrderByNumeroCycleDesc(lastPage.getId());
	 
		 if (lastPistoletOpt.isPresent()) {
			 // Si une soudure est présente, retourner son numéro de cycle
			 return lastPistoletOpt.get().getNumeroCycle();
		 }
	 
		 // Si aucune soudure n'est trouvée malgré les vérifications, retourner 0
		 return 0;
	 }
    // pour agent de qualite
    @Override
    public List<PistoletDTO> getPistoletsNonValideesAgentsQualite() {
        List<Pistolet> pistolets = pistoletRepository.findByDecisionAndRempliePlanAction(0 , 0);

        return pistolets.stream()
            .map(p -> new PistoletDTO( 
      	        p.getId() ,
      	        p.getPdekPistolet().getId()  ,
      	        p.getPagePDEK().getPageNumber() ,
      	        p.getSegment() ,
                p.getDateCreation(),
                p.getHeureCreation() ,
                p.getTypePistolet(),
                p.getNumeroPistolet(),
                p.getLimiteInterventionMax(),
                p.getLimiteInterventionMin(),
                "R",
                p.getCoupePropre(),
                p.getUserPistolet().getMatricule(),
                p.getEch1(),
                p.getEch2(),
                p.getEch3(),
                p.getEch4(),
                p.getEch5(),
                p.getMoyenne(),
                p.getEtendu(), 
  	            p.getCategorie() , 
                p.getNumeroCycle(),
                p.getNbrCollierTester(),
                p.getAxeSerrage(),
                p.getSemaine(),
                p.getDecision(),
                p.getUserPistolet().getMatricule() ,
  	            p.getRempliePlanAction()
            ))
            .toList();
    }
    // pour les techniciens 
    @Override
    public List<PistoletDTO> getPistoletsNonValideesTechniciens() {
        List<Pistolet> pistolets = pistoletRepository.findByDecisionAndRempliePlanAction(0, 1) ; 

        return pistolets.stream()
            .map(p -> new PistoletDTO( 
      	        p.getId() ,
      	        p.getPdekPistolet().getId()  ,
      	        p.getPagePDEK().getPageNumber() ,
      	        p.getSegment() ,
                p.getDateCreation(),
                p.getHeureCreation() ,
                p.getTypePistolet(),
                p.getNumeroPistolet(),
                p.getLimiteInterventionMax(),
                p.getLimiteInterventionMin(),
                "R",
                p.getCoupePropre(),
                p.getUserPistolet().getMatricule(),
                p.getEch1(),
                p.getEch2(),
                p.getEch3(),
                p.getEch4(),
                p.getEch5(),
                p.getMoyenne(),
                p.getEtendu(), 
  	            p.getCategorie() , 
                p.getNumeroCycle(),
                p.getNbrCollierTester(),
                p.getAxeSerrage(),
                p.getSemaine(),
                p.getDecision(),
                p.getUserPistolet().getMatricule() ,
  	            p.getRempliePlanAction()

            ))
            .toList();
    }

    @Override
    public List<PistoletDTO> getPistoletsValidees() {
        List<Pistolet> pistolets = pistoletRepository.findByDecision(1);

        return pistolets.stream()
            .map(p -> new PistoletDTO(  
      	        p.getId() ,  
      	        p.getPdekPistolet().getId()  ,
      	        p.getPagePDEK().getPageNumber() ,
      	        p.getSegment() ,
                p.getDateCreation(),
                p.getHeureCreation() ,
                p.getTypePistolet(),
                p.getNumeroPistolet(),
                p.getLimiteInterventionMax(),
                p.getLimiteInterventionMin(),
                "R",
                p.getCoupePropre(),
                p.getUserPistolet().getMatricule(),
                p.getEch1(),
                p.getEch2(),
                p.getEch3(),
                p.getEch4(),
                p.getEch5(),
                p.getMoyenne(),
                p.getEtendu(), 
  	            p.getCategorie() , 
                p.getNumeroCycle(),
                p.getNbrCollierTester(),
                p.getAxeSerrage(),
                p.getSemaine(),
                p.getDecision(),
                p.getUserPistolet().getMatricule() ,
  	            p.getRempliePlanAction()

            ))
            .toList();
    }
	@Override
	public void validerPistolet(Long idPistolet, int matriculeUser) {
		   String heure = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		   String date  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    // Valider le pistolet
	    pistoletRepository.validerPistolet(idPistolet);

	    // Récupérer le pistolet concerné
	    Pistolet pistolet = pistoletRepository.findById(idPistolet)
	        .orElseThrow(() -> new RuntimeException("Pistolet non trouvé avec ID : " + idPistolet));

	    // Récupérer le PDEK associé
	    PDEK pdek = pistolet.getPdekPistolet() ; 

	    // Récupérer l'utilisateur via son matricule
	    User userControleur = userRepository.findByMatricule(matriculeUser).get() ;

	    // Créer l'entrée de contrôle qualité
	    ControleQualite controle = ControleQualite.builder()
	        .user(userControleur)
	        .pdek(pdek)
	        .idInstanceOperation(pistolet.getId())
	        .nombrePage(pdek.getPages() != null ? pdek.getPages().size() : 0)
	        .dateControle(date)
	        .heureControle(heure)
	        .resultat("Validé")
	        .build();

	    // Sauvegarder le contrôle qualité
	    controleQualiteRepository.save(controle);
	    
	    
	    //valider Plan action si existe 
	    
	    // Étape 1 : récupérer la page PDEK du pistolet
	    PagePDEK page = pistoletRepository.findPDEKByPagePDEK(idPistolet);
	    if (page == null) return;

	    // Étape 2 : récupérer le plan d’action
	    Optional<PlanAction> planOpt = planActionRepository.findByPagePDEKId(page.getId());
	    if (planOpt.isEmpty()) return;

	    PlanAction plan = planOpt.get();

	    // Étape 3 : récupérer les détails
	    List<DetailsPlanAction> detailsList = detailsPlanActionRepository.findByPlanActionId(plan.getId());

	    // Étape 4 : modifier les signatures si nécessaire
	    for (DetailsPlanAction detail : detailsList) {
	        if (detail.getMatricule_operateur() == (matriculeUser) && detail.getSignature_qualite() == 0) {
	            detail.setSignature_qualite(1);
	            detailsPlanActionRepository.save(detail); // sauvegarde
	        }
	    }
	}
	@Override
	public List<UserDTO> getUserDTOsByPdek(Long idPdek) {
        List<User> users = pistoletRepository.findUsersByPdekId(idPdek);
        return users.stream()
                    .map(UserDTO::fromEntity)
                    .toList(); // ou collect(Collectors.toList()) si tu es en Java 8
    }
	
	

}
