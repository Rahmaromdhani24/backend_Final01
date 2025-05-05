package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;

import rahma.backend.gestionPDEK.DTO.DetailsPlanActionDTO;
import rahma.backend.gestionPDEK.DTO.PlanActionDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

public interface PlanActionService {
	
	/********************** Pistolet *****************************/
	 public DetailsPlanActionDTO ajouterPlanActionOuDetails(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
             int numeroPistolet , TypePistolet typePistolet ,CategoriePistolet categoriePistolet) ;
	 
	public PlanActionDTO testerPdekPistoletPossedePlanAction(long pdekId) ; 
    public List<DetailsPlanActionDTO> getDetailsByPlanActionId(Long id)  ; 
    public List<PlanActionDTO> getPlansActionByTypeOperation(TypesOperation typeOperation) ; 
    public List<UserDTO> getUsersByPlanActionId(Long planActionId) ; 
    
    
	public List<PlanActionDTO> testerPdeksProcessPossedePlanAction(Long pdekId) ; 

	
	/********************** Soudure  *****************************/
	 public DetailsPlanActionDTO ajouterPlanActionOuDetailsSoudure(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
            long idSoudre) ;

	public PlanActionDTO testerPdekSoudrePossedePlanAction(long pdekId) ; 
	
	
	/********************** Torsadag  *****************************/
	 public DetailsPlanActionDTO ajouterPlanActionOuDetailsTorsadage(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
	            long idTorsadage) ;
		 
	 /********************** Sertissage Normal  *****************************/
	 public DetailsPlanActionDTO ajouterPlanActionOuDetailsSertissageNormal(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
	            long idSertissage) ;
		 
	 public DetailsPlanActionDTO ajouterPlanActionOuDetailsSertissageIDC(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
	            long idSertissage) ;
	 
    //public List<DetailsPlanActionDTO> getDetailsByPlanActionId(Long id)  ; 
    //public List<PlanActionDTO> getPlansActionByTypeOperation(TypesOperation typeOperation) ; 
    // public List<UserDTO> getUsersByPlanActionId(Long planActionId) ; 
	//public List<PlanActionDTO> testerPdeksProcessPossedePlanAction(long pdekId) ; 

	
    /*public void ajouterPlanActionTorsadage() ; 
    public void ajouterPlanActionSertissageNormal() ; 
    public void ajouterPlanActionSertissageIDC() ;*/
	
}
