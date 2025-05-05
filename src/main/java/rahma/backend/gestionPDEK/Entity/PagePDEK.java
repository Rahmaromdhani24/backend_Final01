package rahma.backend.gestionPDEK.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePDEK {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

	private int pageNumber;  
    private boolean status;  

    
    
    @ManyToOne // Lien vers le fichier PDEK auquel la page appartient
    @JoinColumn(name = "pdek_id")
    private PDEK pdek; 

    @OneToMany(mappedBy = "pagePDEK", cascade = CascadeType.ALL, fetch = FetchType.LAZY)// Liste des soudures associées à cette page
    private List<Soudure> pagesSoudures; 

    @OneToMany(mappedBy = "pagePDEK", cascade = CascadeType.ALL, fetch = FetchType.LAZY)// Liste des torsadages  associées à cette page
    private List<Torsadage> pageTorsadages; 
    
    @OneToMany(mappedBy = "pagePDEK", cascade = CascadeType.ALL, fetch = FetchType.LAZY)// Liste des torsadages  associées à cette page
    private List<Pistolet> pagePistoles;     
    
    @OneToMany(mappedBy = "pagePDEK", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SertissageIDC> pageSertissageIDC;

    @OneToMany(mappedBy = "pagePDEK", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SertissageNormal> pageSertissageNormal;
    
    @OneToOne(mappedBy = "pagePDEK", cascade = CascadeType.ALL)
    private PlanAction planAction;

    
    
    public PagePDEK(int pageNumber, boolean status, PDEK pdek) {
 		super();
 		this.pageNumber = pageNumber;
 		this.status = status;
 		this.pdek = pdek;
 	}

}


