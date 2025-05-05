package rahma.backend.gestionPDEK.DTO;

public class AjoutSoudureResultDTO {
    private Long pdekId;
    private int numeroPage;
    private long idSoudure ; 

    public AjoutSoudureResultDTO(Long pdekId, int numeroPage , long idSoudure) {
        this.pdekId = pdekId;
        this.numeroPage = numeroPage;
        this.idSoudure = idSoudure ; 
    }

    public Long getPdekId() {
        return pdekId;
    }

    public Long getIdSoudure() {
        return idSoudure;
    }
    
    public int getNumeroPage() {
        return numeroPage;
    }
}
