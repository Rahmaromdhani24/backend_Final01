package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;

import rahma.backend.gestionPDEK.DTO.OperateurErreurDTO;
import rahma.backend.gestionPDEK.DTO.OperateurErreurPistolet;
import rahma.backend.gestionPDEK.DTO.StatPistolet;
import rahma.backend.gestionPDEK.DTO.StatProcessus;
import rahma.backend.gestionPDEK.Entity.Plant;

public interface StatistiquesService {

	public long nombreTotalOperateurs() ; 
	public long nombreHommesOperateurs()  ;
	public long nombreFemmesOperateurs()  ; 
	public double calculerPourcentageAugmentationOperateurs()  ; 
	
	/******************************** Erreurs pour process **********************/
	public long nombreErreursSertissageNormalCetteSemaine() ; 
	public long nombreErreursSertissageIDCCetteSemaine() ; 
	public long nombreErreursSoudureCetteSemaine() ; 
	public long nombreErreursTorsadgeCetteSemaine() ; 
	public long nombreErreursTotalCetteSemaineSaufPistolet() ; 
	public double calculerPourcentageSemainePrecdant()  ; 
	
	
	/****************** pdek  *******************/
	public Map<String, Long> getNombrePdekParTypeOperation() ; 
	public Map<String, Long> getNombrePlanActionParTypeOperation() ; 

	
	/******************** top 5 operateurs ***********************/
	public List<OperateurErreurDTO> getTopOperateursWithErrors() ;
	/********** pour agents de qualite *******************/
	public List<StatProcessus> getStatsByPlant(Plant plant) ;
	
	/********* pour chef de ligne *****************/
	public List<StatProcessus> getStatsChefLigneSertissageIDC(Plant plant, int segment) ; 
	public List<StatProcessus> getStatsChefLigneSertissageNormal(Plant plant, int segment) ; 
	public List<StatProcessus> getStatsChefLigneSoudure(Plant plant, int segment) ; 
	public List<StatProcessus> getStatsChefLigneTorsadage(Plant plant, int segment) ; 


	/************************************* Process Pistolet *****************************************************/
	public long nombreErreursPistoletsCetteSemaine() ; 
	public double calculerPourcentageErreursPistoletSemainePrecedente()  ; 
	public List<StatPistolet> getNombrePdekPistoletsParCouleursPistoletsEtTypePistolet();////////
	public Map<String, Long> getNombrePlanActionPistoletsParCouleursPistoletsEtTypePistolet() ;
	public List<OperateurErreurPistolet> getTopAgentsQualitePistoletWithErrors() ;
	public List<StatProcessus> getStatsPistoletsByPlant(Plant plant) ;


}
