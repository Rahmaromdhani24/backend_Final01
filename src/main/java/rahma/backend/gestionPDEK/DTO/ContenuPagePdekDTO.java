package rahma.backend.gestionPDEK.DTO;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContenuPagePdekDTO {
    private int numeroPage;
    private List<Object> contenu;
}