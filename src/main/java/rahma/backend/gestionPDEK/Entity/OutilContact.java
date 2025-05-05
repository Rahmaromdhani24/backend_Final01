package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "outils_contacts") 
@Entity
public class OutilContact {

	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", nullable = false)
	    private Long id;

	    @Column(name = "numéro_outil", length = 500)
	    private String numeroOutil;

	    @Column(name = "numéro_contact", length = 500)
	    private String numeroContact;

	    @Column(name = "section_fil", length = 500)
	    private String sectionFil;

	    @Column(name = "hauteur_sertissage", length = 500)
	    private String hauteurSertissage;

	    @Column(name = "tolérence", length = 500)
	    private String tolerance;

	    @Column(name = "fréquence_controcircle", length = 500)
	    private String frequenceControcircle;

	    @Column(name = "largeur_sertissage", length = 500)
	    private String largeurSertissage;

	    @Column(name = "hauteur_isolant", length = 500)
	    private String hauteurIsolant;

	    @Column(name = "largeur_isolant", length = 500)
	    private String largeurIsolant;

	    @Column(name = "traction", length = 500)
	    private String traction;

	    @Column(name = "lgd", length = 500)
	    private String lgd;

	    @Column(name = "tolerence_largeur_sertissage", length = 500)
	    private String tolerenceLargeurSertissage;

	    @Column(name = "tolerence_largeur_isolant", length = 500)
	    private String tolerenceLargeurIsolant;

	    @Column(name = "tolerence_hauteur_isolant", length = 500)
	    private String tolerenceHauteurIsolant;
}
