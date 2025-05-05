package rahma.backend.gestionPDEK.DTO;

public class AjoutTorsadageResultDTO {
    private Long pdekId;
    private int numeroPage;
    private long idTorsadage ; 

    public AjoutTorsadageResultDTO(Long pdekId, int numeroPage , long idTorsadage) {
        this.pdekId = pdekId;
        this.numeroPage = numeroPage;
        this.idTorsadage =idTorsadage ; 
    }

    public Long getPdekId() {
        return pdekId;
    }

    public Long getIdTorsadage() {
        return idTorsadage;
    }
    public int getNumeroPage() {
        return numeroPage;
    }
}
