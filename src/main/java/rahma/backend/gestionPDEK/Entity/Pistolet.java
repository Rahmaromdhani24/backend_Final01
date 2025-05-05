package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pistolet extends Operation {
    
    @Column(name = "numero_cycle")
    private int  numeroCycle;
	private int numeroPistolet;
    private String nbrCollierTester;
    private String specificationMesure ; 
    private String coupePropre;
    private int axeSerrage;
    private int semaine;
    private String limiteInterventionMax;
    private String limiteInterventionMin;
    private String dateCreation ; 
    private String heureCreation ; 
    private int segment; 
    @Enumerated(EnumType.STRING)
    private TypePistolet type; 

    @Enumerated(EnumType.STRING)
    private CategoriePistolet categorie; 
    
    private int ech1;
    private int ech2;
    private int ech3;
    private int ech4;
    private int ech5;
    private double moyenne;
    private int etendu;
    private int decision ;
    private int rempliePlanAction ;

    @ManyToOne   // relation "saisir" 
    @JoinColumn(name = "user_matricule")
    private User userPistolet;
    
    
    @ManyToOne   // relation "associer"  PDEK a torsadage 
    @JoinColumn(name = "pdek_id")
    private PDEK pdekPistolet;

    
    @ManyToOne
    @JoinColumn(name = "page_pdek_id") // relation avec PagePDEK 
    private PagePDEK pagePDEK ;

   
	public TypePistolet getTypePistolet() {
		// TODO Auto-generated method stub
		return type;
	}
	
    

}

