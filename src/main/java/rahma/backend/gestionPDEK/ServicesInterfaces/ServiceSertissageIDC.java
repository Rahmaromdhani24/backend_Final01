package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;

public interface ServiceSertissageIDC {

public List<SertissageIDC_DTO> getSertissagesIDCNonValidees() ; 
public List<SertissageIDC_DTO> getSertissagesIDCNonValideesChefLigne() ; 
public List<SertissageIDC_DTO> getSertissagesIDCValidees() ; 
public void validerSertissageIDC(Long idSertissageIDc, int matriculeUser) ;
public List<UserDTO> getUserDTOsByPdek(Long idPdek) ; 
public boolean changerAttributRempliePlanActionSertissageIDCeDe0a1(Long id , String couleurZone) ; 
}
