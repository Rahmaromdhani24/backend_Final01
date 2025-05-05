package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;

import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;

public interface ServiceSertissageNormal {

	public List<SertissageNormal_DTO> getSertissagesNonValidees() ; 
	public List<SertissageNormal_DTO> getSertissagesNonValideesChefLigne() ; 
	public List<SertissageNormal_DTO> getSertissagesValidees() ; 
	public void validerSertissage(Long idSertissage, int matriculeUser) ;
	public List<UserDTO> getUserDTOsByPdek(Long idPdek) ; 
	public boolean changerAttributRempliePlanActionSertissageeDe0a1(Long id , String couleurZone) ;
}
