package rahma.backend.gestionPDEK.ServicesImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.*;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.*;

@Service
public class PDEK_ServiceImplimenetation implements PDEKService {

	
	@Autowired PdekRepository pdekRepository ;
	@Autowired ProjetRepository projetRepository  ;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private ControleQualiteRepository controleQualityRepository ; 


	public boolean verifierExistencePDEK_soudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil , segment , plant , nomProjet );
	    return pdekExiste.isPresent();
	}

	public PdekDTO recupererPdekSoudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet) {
	    return pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil , segment , plant , nomProjet )
	        .map(pdek -> new PdekDTO(
	                pdek.getId(),
	                pdek.getSectionFil(),
	                nomProjet , 
	                pdek.getDateCreation(),
	                pdek.getFrequenceControle()
	        ))
	        .orElse(null); // Ou Optional<PdekDTO> si tu veux gérer l'absence
	}

	
	public boolean verifierExistencePDEK_Torsadage(String sectionFil, int segment ,Plant plant , String nomProjet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_Torsadage(sectionFil , segment , plant , nomProjet );
	    return pdekExiste.isPresent();
	}

	public PdekDTO recupererPdekTorsadag(String sectionFil, int segment ,Plant plant , String nomProjet) {
	    return pdekRepository.findUniquePDEK_Torsadage(sectionFil , segment , plant , nomProjet )
	        .map(pdek -> new PdekDTO(
	                pdek.getId(),
	                pdek.getSectionFil(),
	                nomProjet , 
	                pdek.getDateCreation(),
	                pdek.getFrequenceControle()
	        ))
	        .orElse(null); // Ou Optional<PdekDTO> si tu veux gérer l'absence
	}

	@Override
	public boolean verifierExistencePDEK_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findByTypePistoletAndCategoriePistoletAndNumeroPistolet(typePistolet   , categoriePistolet ,numeroPistolet );
		    return pdekExiste.isPresent();
	}
	
	@Override
	public PistoletDTO recupererPdek_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) {
	    // Recherche un PDEK selon le type de pistolet
	    Optional<PDEK> pdekOpt = pdekRepository.findByTypePistoletAndCategoriePistoletAndNumeroPistolet(typePistolet   , categoriePistolet ,numeroPistolet );
	    
	    // Si le PDEK existe, on cherche un Pistolet associé au type de Pistolet
	    if (pdekOpt.isPresent()) {
	        PDEK pdek = pdekOpt.get();
	        
	        // Chercher le premier Pistolet qui correspond au type de Pistolet
	        return pdek.getPdekPistoles().stream()
	                .filter(pistolet -> pistolet.getTypePistolet() == typePistolet) // Filtrage par type de pistolet
	                .findFirst() // Récupère le premier Pistolet trouvé
	                .map(pistolet -> new PistoletDTO(
	                        pistolet.getId(),
	                        pistolet.getDateCreation(),
	                        pistolet.getTypePistolet(),
	                        pistolet.getNumeroPistolet(),
	                        pistolet.getLimiteInterventionMax(),
	                        pistolet.getLimiteInterventionMin(),
	                        pistolet.getPdekPistolet() // Associer le PDEK
	                ))
	                .orElse(null); // Si aucun Pistolet n'est trouvé, retourner null
	    }
	    
	    // Si aucun PDEK n'est trouvé, retourner null
	    return null;
	}

	@Override
	public List<ContenuPagePdekDTO> getContenuParPage(Long pdekId) {
	    Optional<PDEK> pdekOptional = pdekRepository.findById(pdekId);

	    if (pdekOptional.isEmpty()) {
	        throw new RuntimeException("PDEK non trouvé avec ID: " + pdekId);}
	    
	    PDEK pdek = pdekOptional.get();
	    List<ContenuPagePdekDTO> result = new ArrayList<>();

	    if (pdek.getPages() != null) {
	        for (PagePDEK page : pdek.getPages()) {
	            int numeroPage = page.getPageNumber();
	            List<Object> contenu = new ArrayList<>();

	            if (pdek.getPdekSoudures() != null) {
	                pdek.getPdekSoudures().stream()
	                    .filter(p -> p.getPagePDEK() != null && p.getPagePDEK().getPageNumber() == numeroPage)
	                    .map(p -> {
	                        Integer matriculeQM = controleQualityRepository
	                            .findByPdek_IdAndIdInstanceOperation(pdek.getId(), p.getId())
	                            .map(c -> c.getUser().getMatricule())
	                            .orElse(0); // valeur par défaut si non trouvé

	                        return new SoudureDTO(
	                            p.getId(),
	                            p.getClass().getSimpleName(),
	                            p.getUserSoudure().getSegment(),
	                            p.getUserSoudure().getPlant().toString(),
	                            p.getUserSoudure().getMachine(),
	                            p.getCode(),
	                            p.getSectionFil(),
	                            p.getDate(),
	                            p.getHeureCreation(),
	                            p.getNumeroCycle(),
	                            p.getUserSoudure().getMatricule(),
	                            p.getMoyenne(),
	                            p.getEtendu(),
	                            p.getPelageX1(),
	                            p.getPelageX2(),
	                            p.getPelageX3(),
	                            p.getPelageX4(),
	                            p.getPelageX5(),
	                            p.getPliage(),
	                            p.getDistanceBC(),
	                            p.getTraction(),
	                            p.getNombreKanban(),
	                            p.getNombreNoeud(),
	                            p.getGrendeurLot(),
	                            p.getUserSoudure().getMatricule(),
	                            matriculeQM, // valeur traitée ici
	                            p.getDecision(),
	                            p.getRempliePlanAction(),
	                            p.getPdekSoudure().getId(),
	                            p.getPagePDEK().getPageNumber(),
	                            p.getQuantiteAtteint(),
	                            p.getZone()
	                        );
	                    })
	                    .forEach(contenu::add);
	            }
	  
	   
	            if (pdek.getPdekTorsadages() != null) {
	                pdek.getPdekTorsadages().stream()
	                    .filter(t -> t.getPagePDEK() != null && t.getPagePDEK().getPageNumber() == numeroPage)
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
	                    .forEach(contenu::add); // ✅ Ajout ici
	            }

	                    // 🎯 Filtrage des pistolets liés à cette page
	                    if (pdek.getPdekPistoles() != null) {
	                        pdek.getPdekPistoles().stream()
	                            .filter(p -> p.getPagePDEK() != null && p.getPagePDEK().getPageNumber() == numeroPage)
	                            .map(p -> new PistoletDTO(
	                                p.getId(),
	                                p.getPdekPistolet().getId() ,
	                                numeroPage,
	                                p.getSegment(),
	                                p.getDateCreation(),
	                                p.getHeureCreation(),
	                                p.getType(),
	                                p.getNumeroPistolet(),
	                                p.getLimiteInterventionMax(),
	                                p.getLimiteInterventionMin(),
	                                "R" ,
	                                p.getCoupePropre(),
	                                p.getUserPistolet().getMatricule() ,
	                                p.getEch1(),
	                                p.getEch2(),
	                                p.getEch3(),
	                                p.getEch4(),
	                                p.getEch5(),
	                                p.getMoyenne(),
	                                p.getEtendu(),
	                                p.getCategorie(),
	                                p.getNumeroCycle() ,
	                                p.getNbrCollierTester(),
	                                p.getAxeSerrage(),
	                                p.getSemaine(),
	                                p.getDecision(),
	                                p.getUserPistolet().getMatricule() ,
	                                p.getRempliePlanAction()
	                            ))
	                            .forEach(contenu::add);
	                    }

	                    if (pdek.getPdekSertissageIDC() != null) {
	                        pdek.getPdekSertissageIDC().stream()
	                            .filter(p -> p.getPagePDEK() != null && p.getPagePDEK().getPageNumber() == numeroPage)
	                            .map(p -> {
	                                Integer matriculeQM = controleQualityRepository
	                                    .findByPdek_IdAndIdInstanceOperation(pdek.getId(), p.getId())
	                                    .map(c -> c.getUser().getMatricule())
	                                    .orElse(0); // Valeur par défaut si non trouvé

	                                return new SertissageIDC_DTO(
	                                    p.getId(),
	                                    p.getClass().getSimpleName(),
	                                    p.getCodeControle(),
	                                    p.getSectionFil(),
	                                    p.getDate() != null ? p.getDate().toString() : null,
	                                    p.getNumCycle(),
	                                    p.getUserSertissageIDC() != null ? p.getUserSertissageIDC().getMatricule() : 0,
	                                    p.getHauteurSertissageC1Ech1(),
	                                    p.getHauteurSertissageC1Ech2(),
	                                    p.getHauteurSertissageC1Ech3(),
	                                    p.getHauteurSertissageC1EchFin(),
	                                    p.getHauteurSertissageC2Ech1(),
	                                    p.getHauteurSertissageC2Ech2(),
	                                    p.getHauteurSertissageC2Ech3(),
	                                    p.getHauteurSertissageC2EchFin(),
	                                    p.getProduit(),
	                                    p.getSerieProduit(),
	                                    p.getQuantiteCycle(),
	                                    p.getNumeroMachine(),
	                                    p.getForceTractionC1Ech1(),
	                                    p.getForceTractionC1Ech2(),
	                                    p.getForceTractionC1Ech3(),
	                                    p.getForceTractionC1EchFin(),
	                                    p.getForceTractionC2Ech1(),
	                                    p.getForceTractionC2Ech2(),
	                                    p.getForceTractionC2Ech3(),
	                                    p.getForceTractionC2EchFin(),
	                                    p.getDecision(),
	                                    p.getRempliePlanAction(),
	                                    p.getPdekSertissageIDC() != null ? p.getPdekSertissageIDC().getId() : null,
	                                    p.getPagePDEK() != null ? p.getPagePDEK().getPageNumber() : 0,
	                                    p.getZone(),
	                                    p.getHeureCreation(),
	                                    matriculeQM
	                                );
	                            })
	                            .forEach(contenu::add);
	                    }


	            if (pdek.getPdekSertissageNormal() != null) {
	                pdek.getPdekSertissageNormal().stream()
	                .filter(p -> p.getPagePDEK() != null && p.getPagePDEK().getPageNumber() == numeroPage)
                    .map(p -> {
                        Integer matriculeQM = controleQualityRepository
                            .findByPdek_IdAndIdInstanceOperation(pdek.getId(), p.getId())
                            .map(c -> c.getUser().getMatricule())
                            .orElse(0); // Valeur par défaut si non trouvé

                        return new SertissageNormal_DTO(
	                    		 p.getId(),
	                             p.getClass().getSimpleName() ,
	                             p.getUserSertissageNormal().getPlant().toString() ,
	                             p.getHeureCreation() ,
	                             p.getCodeControle() ,  
	                             p.getSectionFil(),
	                             p.getNumeroOutils() , 
	                             p.getNumeroContacts()  ,
	                             p.getDate(),
	                             p.getNumCycle(),
	                             p.getUserSertissageNormal().getMatricule(),  
	                             p.getHauteurSertissageEch1(),
	                             p.getHauteurSertissageEch2(),
	                             p.getHauteurSertissageEch3(),
	                             p.getHauteurSertissageEchFin(),
	                             p.getLargeurSertissage(), 
	                             p.getLargeurSertissageEchFin(), 
	                             p.getHauteurIsolant(),
	                             p.getLargeurIsolant(),
	                             p.getLargeurIsolantEchFin(),
	                             p.getHauteurIsolantEchFin(),
	                             p.getTraction(),
	                             p.getTractionFinEch(),
	                             p.getProduit(),
	                             p.getSerieProduit(),
	                             p.getQuantiteCycle(),
	                             p.getSegment(),
	                             p.getNumeroMachine(),
	                             p.getDecision(),
	                             p.getRempliePlanAction() ,
	                             p.getPdekSertissageNormal().getId()  ,
	                  	         p.getPagePDEK().getPageNumber() ,
	                  	         p.getPdekSertissageNormal().getLGD() ,
		                	     p.getZone() ,
		                	     matriculeQM
                                );
                            })
                            .forEach(contenu::add);
                    }


	            if (!contenu.isEmpty()) {
	                result.add(new ContenuPagePdekDTO(numeroPage, contenu));
	            }
	        }
	    }

	    return result;
	}
	public Object getPdekDTOById(Long id) {
	    PDEK pdek = pdekRepository.findById(id).get() ; 

	    switch (pdek.getTypeOperation()) {
	        case Montage_Pistolet:
	            return buildPdekResultat(pdek); // appelle la méthode de mapping personnalisée
	        case Soudure:
	            return buildSoudureDTO(pdek);
	        case Torsadage:
	            return buildTorsadageDTO(pdek);
	        case Sertissage_IDC:
	        	return buildSertissageIDCDTO(pdek);
	        case Sertissage_Normal:
	            return buildSertissageNormalDTO(pdek);
	        default:
	            throw new IllegalArgumentException("Unknown typeOperation: " + pdek.getTypeOperation());
	    }
	}
/******************************************* Build instances **************************/
	public PdekResultat buildPdekResultat(PDEK pdek) {
	    return new PdekResultat(
	        pdek.getId(),
	        pdek.getNumeroPistolet(),
	        pdek.getTypeOperation(), // Enum TypesOperation
	        pdek.getTypePistolet(),  // Enum TypePistolet
	        pdek.getCategoriePistolet(), // Enum CategoriePistolet
	        pdek.getPlant(),         // Enum Plant
	        pdek.getSegment(),
	        0,
	        0
	    );
	}
	public SoudureDTO buildSoudureDTO(PDEK pdek) {
	    SoudureDTO dto = new SoudureDTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // suppose que getCode() existe
	    dto.setSectionFil(pdek.getSectionFil()); // suppose que getSectionFil() existe
	    dto.setDate(pdek.getDateCreation()); // ou autre champ selon ton modèle
	    dto.setNumeroCycle(0); // suppose que getNumeroCycle() existe
	    dto.setUserSoudure(0); // ou getUserSoudure() si tu as un champ dédié
	    dto.setMoyenne(0);
	    dto.setEtendu(0);

	    return dto;
	}

	public TorsadageDTO buildTorsadageDTO(PDEK pdek) {
	    TorsadageDTO dto = new TorsadageDTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // suppose que getCode() existe
	    dto.setSpecificationMesure(null); // suppose que ce champ existe
	    dto.setDate(pdek.getDateCreation()); // ou un autre champ date
	    dto.setNumeroCycle(0); // champ existant ?
	    dto.setUserTorsadage(0); // ou pdek.getUserTorsadage()
	    dto.setMoyenne(0);
	    dto.setEtendu(0);

	    return dto;
	}
	public SertissageIDC_DTO buildSertissageIDCDTO(PDEK pdek) {
	    SertissageIDC_DTO dto = new SertissageIDC_DTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // Assure-toi que getCode() existe
	    dto.setSectionFil(pdek.getSectionFil()); // idem
	    dto.setDate(pdek.getDateCreation()); // ou autre champ si nécessaire
	    dto.setNumCycle(0); // ou pdek.getNumCycle() selon le nom réel
	    dto.setUserSertissageIDC(0); // ou getUserSertissageIDC()

	    return dto;
	}
	public SertissageNormal_DTO buildSertissageNormalDTO(PDEK pdek) {
	    SertissageNormal_DTO dto = new SertissageNormal_DTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // Assurez-vous que ce champ existe
	    dto.setSectionFil(pdek.getSectionFil()); // idem
	    dto.setNumOutil(pdek.getNumeroOutils()); // à adapter si le nom réel est différent
	    dto.setNumContact(pdek.getNumeroContacts()); // idem
	    dto.setDate(pdek.getDateCreation()); // ou une autre méthode pour la date
	    dto.setNumCycle(0); // idem
	    dto.setUserSertissageNormal(0); // ou getUserSertissageNormal()

	    dto.setHauteurSertissageEch1(0);
	    dto.setHauteurSertissageEch2(0);
	    dto.setHauteurSertissageEch3(0);
	    dto.setHauteurSertissageEchFin(0);

	    return dto;
	}

}
