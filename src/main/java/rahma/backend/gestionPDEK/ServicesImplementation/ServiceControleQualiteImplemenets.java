package rahma.backend.gestionPDEK.ServicesImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.Repository.ControleQualiteRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServiceControleQualite;

@Service
public class ServiceControleQualiteImplemenets implements ServiceControleQualite {

	@Autowired  private ControleQualiteRepository controleQualiteRepository;
	

	@Override
	public int getUserIdByPdekIdAndPageAndOperation(Long pdekId, Long idInstanceOperation) {
		// TODO Auto-generated method stub
		   return controleQualiteRepository
	                .findByPdek_IdAndIdInstanceOperation(pdekId, idInstanceOperation)
	                .map(controleQualite -> controleQualite.getUser().getMatricule()) // Récupère le matricule (type int)
	                .orElse(0); 
	    }

}