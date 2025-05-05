package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
public class AuditLog {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	    private String action;
	    @Lob
	    @Column(length = 10000)
	    private String description ;
	    private String dateCreation ;
	    private String heureCreation ;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	    
	    private long pdek_id ; 
	    private long pistolet_id ; 
	    private long soudure_id ; 
	    private long torsadage_id ;
	    private long sertissage_id ; 
	    private long sertissage_idc_id ; 

}
