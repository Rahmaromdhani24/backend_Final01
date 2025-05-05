package rahma.backend.gestionPDEK.DTO;

import lombok.*;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

@Getter
@Setter
public class PdekResultat {

    private Long id;
    private String typeOperation;
    private String typePistolet;
    private int numeroPistolet ; 
    private String categorie; 
    private String plant;
    private int segment;
    private int totalPages;
    private Object usersMatricules; // ✅ Champ ajouté

    // ✅ Constructeur complet avec tous les champs
    public PdekResultat(Long id, int numeroPistolet , TypesOperation typeOperation, TypePistolet typePistolet,
                        CategoriePistolet categorie, Plant plant, int segment,
                        int totalPages, Object usersMatricules ) {
        this.id = id;
        this.numeroPistolet = numeroPistolet ;
        this.typeOperation = typeOperation != null ? typeOperation.name() : null;
        this.typePistolet = typePistolet != null ? typePistolet.name() : null;
        this.categorie = categorie != null ? categorie.name() : null;
        this.plant = plant != null ? plant.name() : null;
        this.segment = segment;
        this.totalPages = totalPages;
        this.usersMatricules = usersMatricules;
    }

	
	
}
