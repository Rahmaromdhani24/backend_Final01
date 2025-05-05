package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.AjoutSertissageResultDTO;
import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.ControleQualite;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.OutilContact;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.PlanAction;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.SertissageIDC;
import rahma.backend.gestionPDEK.Entity.SertissageNormal;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.AuditLogRepository;
import rahma.backend.gestionPDEK.Repository.ControleQualiteRepository;
import rahma.backend.gestionPDEK.Repository.DetailsPlanActionRepository;
import rahma.backend.gestionPDEK.Repository.OutilsContactRepository;
import rahma.backend.gestionPDEK.Repository.PdekPageRepository;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import rahma.backend.gestionPDEK.Repository.ProjetRepository;
import rahma.backend.gestionPDEK.Repository.SertissageNormalRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServiceSertissageNormal;

@Service
public class SertissageNormalServiceImplimentation implements ServiceSertissageNormal {

	 @Autowired private OutilsContactRepository  outilContactRepository;
	 @Autowired    private PdekRepository pdekRepository;
	 @Autowired    private SertissageNormalRepository sertissageNormalRepository;	
	 @Autowired    private UserRepository userRepository;	
	 @Autowired    private ProjetRepository projetRepository;	
	 @Autowired    private PdekPageRepository pdekPageRepository;	
	 @Autowired    private ControleQualiteRepository controleQualiteRepository;
	 @Autowired    private AuditLogRepository auditLogRepository;
     @Autowired    private PlanActionRepository planActionRepository ; 
     @Autowired    private DetailsPlanActionRepository detailsPlanActionRepository ; 
     
	 // Récupération liste des contacts depuis numero  de outil 
	 public List<String> getDistinctContactsByNumeroOutil(String numeroOutil) {
	        List<String> contacts = outilContactRepository.findContactsByNumeroOutil(numeroOutil);
	        return contacts.stream().distinct().collect(Collectors.toList());  
	    }
	 
	 // Récupération des sections de fil uniques à partir du numéro d'outil et du numéro de contact
	 public List<String> getSectionsByOutilAndContact(String numeroOutil, String numeroContact) {
		    // Récupérer toutes les instances de OutilContact en fonction des paramètres
		    List<OutilContact> outilsContacts = outilContactRepository.findSectionsFilByOutilAndContact(numeroOutil, numeroContact);

		    if (outilsContacts.isEmpty()) {
		        throw new RuntimeException("Aucun OutilContact trouvé pour les paramètres fournis");
		    }

		    // Récupérer toutes les sections de fil à partir des instances OutilContact
		    return outilsContacts.stream()
		            .map(OutilContact::getSectionFil)           // Récupérer la sectionFil de chaque OutilContact
		            .filter(sectionFil -> sectionFil != null && !sectionFil.isEmpty())  // Filtrer les sections non nulles et non vides
		            .flatMap(sectionFil -> Arrays.stream(sectionFil.split(",")))  // Séparer chaque sectionFil par des virgules
		            .map(String::trim)  // Enlever les espaces supplémentaires
		            .distinct()         // Enlever les doublons
		            .collect(Collectors.toList());  // Collecter dans une liste
		}
	 
	 /// Récuperer hauteur de sertissage depuis num outil , num contact et une section de fil 
	 public String getHauteurSertissage(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 1"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getHauteurSertissage();
	    }
	 
	 /// Récuperer largeur de sertissage depuis num outil , num contact et une section de fil 
	 public String getLargeurSertissage(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 2"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getLargeurSertissage();
	    }

	 /// Récuperer hauteur de isolant  depuis num outil , num contact et une section de fil 
	 public String getHauteurIsolant(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 3"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getHauteurIsolant();
	    }
	 
	 /// Récuperer largeur de isolant depuis num outil , num contact et une section de fil 
	 public String getLargeurIsolant(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 4"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getLargeurIsolant();
	    }

		/// Récuperer traction depuis num outil , num contact et une section de fil 
		public String getTractionValeur(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 5"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getTraction();
	    }

		public String getToleranceLargeurSertissage(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 6"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getTolerenceLargeurSertissage();
	    }
		public String getToleranceHauteurIsolant(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 7"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getTolerenceHauteurIsolant();
	    }
		public String getToleranceLargeurIsolant(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 8"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getTolerenceLargeurIsolant();
	    }

		public String getToleranceValue(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 9"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getTolerance();
	    }
		
		public String getLGDeValue(String numeroOutil, String numeroContact, String sectionFil) {
	        // Récupère un OutilContact en fonction des trois critères
	        OutilContact outilContact = outilContactRepository
	                .findByNumeroOutilAndNumeroContactAndSectionFil(numeroOutil, numeroContact, sectionFil)
	                .orElseThrow(() -> new RuntimeException("OutilContact non trouvé 10"));

	        // Retourne la hauteur de sertissage associée
	        return outilContact.getLgd();
	    }

		/******************************************************************************************************************************/
		public AjoutSertissageResultDTO ajoutPDEK_SertissageNormal (SertissageNormal sertissageNormal, int matriculeOperateur , String projet) {
			
		    String sectionFilSelectionner = sertissageNormal.getSectionFil() ; 
		    User user = userRepository.findByMatricule(matriculeOperateur).get() ; 
		    
		    SertissageNormal  instance1 = new SertissageNormal() ; 
		    instance1.setCodeControle(sertissageNormal.getCodeControle());
		    instance1.setSectionFil(sectionFilSelectionner +"  mm²");
		    instance1.setDate(sertissageNormal.getDate());
		    instance1.setHeureCreation(sertissageNormal.getHeureCreation());
		    instance1.setHauteurSertissageEch1(sertissageNormal.getHauteurSertissageEch1());
		    instance1.setHauteurSertissageEch2(sertissageNormal.getHauteurSertissageEch2());
		    instance1.setHauteurSertissageEch3(sertissageNormal.getHauteurSertissageEch3());
		    instance1.setHauteurSertissageEchFin(sertissageNormal.getHauteurSertissageEch1());
		    instance1.setLargeurSertissage(sertissageNormal.getLargeurSertissage());
		    instance1.setLargeurSertissageEchFin(sertissageNormal.getLargeurSertissageEchFin());
		    instance1.setHauteurIsolant(sertissageNormal.getHauteurIsolant());
		    instance1.setHauteurIsolantEchFin(sertissageNormal.getHauteurIsolantEchFin());
		    instance1.setLargeurIsolant(sertissageNormal.getLargeurIsolant());
		    instance1.setLargeurIsolantEchFin(sertissageNormal.getLargeurIsolantEchFin());
		    instance1.setTraction(sertissageNormal.getTraction());
		    instance1.setTractionFinEch(sertissageNormal.getTractionFinEch());
		    instance1.setSerieProduit(sertissageNormal.getSerieProduit());
		    instance1.setProduit(sertissageNormal.getProduit());
		    instance1.setNumeroMachine(sertissageNormal.getNumeroMachine());
		    instance1.setQuantiteCycle(sertissageNormal.getQuantiteCycle());
		    instance1.setSegment(user.getSegment());
		    instance1.setTolerance(sertissageNormal.getTolerance());
		    instance1.setNumeroOutils(sertissageNormal.getNumeroOutils());
			instance1.setNumeroContacts(sertissageNormal.getNumeroContacts());

			instance1.setUserSertissageNormal(user);
		 /************************** Recuperation PDEK ID s'il exise ****************/
		  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SertissageNormal(sectionFilSelectionner  , user.getSegment() , user.getPlant() , projet );

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
		        long nombreSertissageNormalDansPage = sertissageNormalRepository.countByPagePDEK(pagePDEK);
		        int numeroCycle;

		        if (nombreSertissageNormalDansPage < 8) {
		            // Ajouter le pistolet à la même page
		            numeroCycle = (int) nombreSertissageNormalDansPage + 1;
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

		        
		         instance1.setPdekSertissageNormal(pdek);
		         instance1.setPagePDEK(pagePDEK);
		         instance1.setNumCycle(numeroCycle);
		         sertissageNormalRepository.save(instance1) ;
		        
		   
		     	return new AjoutSertissageResultDTO(pdek.getId(), pagePDEK.getPageNumber()  , instance1.getId());


		    } else {
		       
		    	PDEK newPDEK = new PDEK() ; 
		    	newPDEK.setSectionFil(sectionFilSelectionner);
		    	newPDEK.setNombreEchantillons("3 Piéces ");
		    	newPDEK.setSegment(user.getSegment());
		    	newPDEK.setNumMachine(user.getMachine());
		    	newPDEK.setDateCreation(sertissageNormal.getDate());
		    	newPDEK.setTypeOperation(TypesOperation.Sertissage_Normal);
		    	newPDEK.setPlant(user.getPlant());
		    	newPDEK.setUsersRempliePDEK(List.of(user));
		    	newPDEK.setTotalPages(1);
		    	newPDEK.setNumeroOutils(instance1.getNumeroOutils());
		    	newPDEK.setNumeroContacts(instance1.getNumeroContacts());
		    	newPDEK.setTolerance(instance1.getTolerance());
		    	newPDEK.setLGD(getLGDeValue(instance1.getNumeroOutils() ,instance1.getNumeroContacts() , sectionFilSelectionner));
		    	instance1.setNumCycle(1);
		    	pdekRepository.save(newPDEK)  ;
		    	PagePDEK newPage = new PagePDEK(1, false, newPDEK);
		        pdekPageRepository.save(newPage);
		        instance1.setPagePDEK(newPage);
			  if (!newPDEK.getProjets().contains(projetRepository.findByNom(projet).get())) {
				  newPDEK.getProjets().add(projetRepository.findByNom(projet).get()); // Ajouter le projet au PDEK
				  projetRepository.findByNom(projet).get().getPdeks().add(newPDEK);
		    	projetRepository.save(projetRepository.findByNom(projet).get());
		    	instance1.setPdekSertissageNormal(newPDEK);
		    	sertissageNormalRepository.save(instance1) ;   	

			      }
		     	return new AjoutSertissageResultDTO(newPDEK.getId(), newPage.getPageNumber() , instance1.getId());

		    }
			 }

		 public Map<Integer, List<SertissageNormal_DTO>> recupererSertissagesNormalesParPDEKGroupéesParPage(String sectionFil  , int segment, Plant plant, String nomProjet) {
	     Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SertissageNormal(sectionFil ,  segment, plant, nomProjet);

	     if (pdekExiste.isPresent()) {
	         PDEK pdek = pdekExiste.get();
	         List<SertissageNormal> sertissageNormals = sertissageNormalRepository.findByPdekSertissageNormal_Id(pdek.getId());

	         // Grouper les soudures par numéro de page
	         return sertissageNormals.stream()
	                 .collect(Collectors.groupingBy(
	                         s -> s.getPagePDEK().getPageNumber(), // groupement par numéro de page
	                         Collectors.mapping(
	                                 n -> new SertissageNormal_DTO(
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
	                              	         n.getZone()),
	                                 Collectors.toList()
	                         )
	                 ));
	     } else {
	         return Map.of();
	     }
	 }

	 ///////
	 public int getLastNumeroCycle(String sectionFil ,  int segment, Plant nomPlant, String projetName) {
		 // 1️⃣ Récupérer le PDEK correspondant
		 Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_SertissageNormal(sectionFil , segment, nomPlant, projetName);
	 
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
		 long nombreSertissageDansPage = sertissageNormalRepository.countByPagePDEK(lastPage);
	 
		 if (nombreSertissageDansPage == 0) {
			 // Si la page est vide, retourner 0
			 return 0;
		 }
	 
		 // 4️⃣ Récupérer le dernier numéro de cycle
		 Optional<SertissageNormal> lastSertissageOpt = sertissageNormalRepository.findTopByPagePDEK_IdOrderByNumCycleDesc(lastPage.getId());
	 
		 if (lastSertissageOpt.isPresent()) {
			 // Si une soudure est présente, retourner son numéro de cycle
			 return lastSertissageOpt.get().getNumCycle();
		 }
	 
		 // Si aucune soudure n'est trouvée malgré les vérifications, retourner 0
		 return 0;
	 }

	@Override
	public List<SertissageNormal_DTO> getSertissagesNonValidees() {
		 List<SertissageNormal> sertissages = sertissageNormalRepository.findByDecisionAndRempliePlanAction(0 , 0);

		  return sertissages.stream()
		            .map(n -> new SertissageNormal_DTO( 
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

		            		  ))
		            .toList();
		    
		}

	@Override
	public List<SertissageNormal_DTO> getSertissagesNonValideesChefLigne() {
		 List<SertissageNormal> sertissages = sertissageNormalRepository.findByDecisionAndRempliePlanAction(0 , 1);

	        return sertissages.stream()
	            .map(n -> new SertissageNormal_DTO( 
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

	            		  ))
	            .toList();
	    
	}
	@Override
	public List<SertissageNormal_DTO> getSertissagesValidees() {
		 List<SertissageNormal> sertissages = sertissageNormalRepository.findByDecision(1);

	        return sertissages.stream()
	            .map(n -> new SertissageNormal_DTO( 
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

	            		  ))
	            .toList();
	    
	}

	@Override
	public void validerSertissage(Long idSertissage, int matriculeUser) {
		 String heure = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		   String date  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    // Valider le pistolet
		   sertissageNormalRepository.validerSertissage(idSertissage);

	    // Récupérer le pistolet concerné
	    SertissageNormal  sertissageIDC = sertissageNormalRepository.findById(idSertissage)
	        .orElseThrow(() -> new RuntimeException("Sertissage IDC  non trouvé avec ID : " + idSertissage));

	    // Récupérer le PDEK associé
	    PDEK pdek = sertissageIDC.getPdekSertissageNormal() ;  

	    // Récupérer l'utilisateur via son matricule
	    User userControleur = userRepository.findByMatricule(matriculeUser).get() ;

	    // Créer l'entrée de contrôle qualité
	    ControleQualite controle = ControleQualite.builder()
	        .user(userControleur)
	        .pdek(pdek)
	        .idInstanceOperation(sertissageIDC.getId())
	        .nombrePage(pdek.getPages() != null ? pdek.getPages().size() : 0)
	        .dateControle(date)
	        .heureControle(heure)
	        .resultat("Validé")
	        .build();

	    // Sauvegarder le contrôle qualité
	    controleQualiteRepository.save(controle);
	    
	    
	    //valider Plan action si existe 
	    
	    // Étape 1 : récupérer la page PDEK du pistolet
	    PagePDEK page = sertissageNormalRepository.findPDEKByPagePDEK(idSertissage);
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
		 List<User> users = sertissageNormalRepository.findUsersByPdekId(idPdek);
	        return users.stream()
	                    .map(UserDTO::fromEntity)
	                    .toList(); // ou collect(Collectors.toList()) si tu es en Java 8
	    }

	@Override
	public boolean changerAttributRempliePlanActionSertissageeDe0a1(Long id , String couleurZone) {
		 Optional<SertissageNormal> optionalSertissage = sertissageNormalRepository.findById(id);
	        if (optionalSertissage.isPresent()) {
	        	SertissageNormal sertissage = optionalSertissage.get();
	        	sertissage.setRempliePlanAction(1); 
	        	sertissage.setZone(couleurZone); 
	            sertissageNormalRepository.save(sertissage);
	            return true;
	        }
	        return false;
	    
	}
}