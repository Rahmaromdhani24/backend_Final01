package rahma.backend.gestionPDEK.DTO;

public class AjoutSertissageResultDTO {
    private Long pdekId;
    private int numeroPage;
    private long idSertissage ; 

    public AjoutSertissageResultDTO(Long pdekId, int numeroPage , long idSertissage) {
        this.pdekId = pdekId;
        this.numeroPage = numeroPage;
        this.idSertissage = idSertissage ; 
    }

    public Long getPdekId() {
        return pdekId;
    }

    public int getNumeroPage() {
        return numeroPage;
    }
    
    public long getIdSertissage() {
        return idSertissage;
    }
}
