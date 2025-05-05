package rahma.backend.gestionPDEK.Entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract  class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String nom;
  
}
