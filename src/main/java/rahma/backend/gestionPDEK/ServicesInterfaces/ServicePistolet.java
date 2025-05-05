package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;

public interface ServicePistolet {

	public List<PistoletDTO> getPistoletsNonValideesAgentsQualite() ; 
	public List<PistoletDTO> getPistoletsNonValideesTechniciens() ; 
	public List<PistoletDTO> getPistoletsValidees() ; 
	public void validerPistolet(Long idPistolet, int matriculeUser)  ; 
	public List<UserDTO> getUserDTOsByPdek(Long idPdek) ; 
}
