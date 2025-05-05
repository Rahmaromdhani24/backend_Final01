package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;

import rahma.backend.gestionPDEK.DTO.ContenuPagePdekDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;

public interface PDEKService {
	
										/****** Soudure utlrason *****/

	public boolean verifierExistencePDEK_soudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
	public PdekDTO recupererPdekSoudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet) ; 
	
	
	
                                      	/****** torsadage *****/
	public boolean verifierExistencePDEK_Torsadage(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
	public PdekDTO recupererPdekTorsadag(String sectionFil, int segment ,Plant plant , String nomProjet) ; 
	

	                                	/****** torsadage *****/
	public boolean verifierExistencePDEK_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) ; 
	public PistoletDTO recupererPdek_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) ; 
	

/*************************************************** Tous *********************************************************************/
	public List<ContenuPagePdekDTO> getContenuParPage(Long pdekId)  ; 
	
	
	
}
