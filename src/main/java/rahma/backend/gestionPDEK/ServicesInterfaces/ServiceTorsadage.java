package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;
import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;

public interface ServiceTorsadage {

	public AjoutTorsadageResultDTO ajoutPDEK_Torsadage (Torsadage instanceTorsadage ,  int matriculeOperateur , String projet) ; 
	public Map<Integer, List<TorsadageDTO>> recupererTorsadagesParPDEKGroupéesParPage(String specificationMesure, int segment, Plant plant, String nomProjet)  ; 
	public List<TorsadageDTO> getTorsadagsNonValidees() ; 
	public List<TorsadageDTO> getTorsadagesNonValideesTechniciens() ; 
	public List<TorsadageDTO> getTorsadagsValidees() ; 
	public void validerTorsadage(Long idTorsadage, int matriculeUser) ;
	public List<UserDTO> getUserDTOsByPdek(Long idPdek) ; 
	public boolean changerAttributRempliePlanActionTorsadageDe0a1(Long id , String zoneCouleur);

}
