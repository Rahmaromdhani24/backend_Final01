package rahma.backend.gestionPDEK.DTO;

public class AjoutPistoletResultDTO {
    private Long pdekId;
    private int numeroPage;

    public AjoutPistoletResultDTO(Long pdekId, int numeroPage) {
        this.pdekId = pdekId;
        this.numeroPage = numeroPage;
    }

    public Long getPdekId() {
        return pdekId;
    }

    public int getNumeroPage() {
        return numeroPage;
    }
}
