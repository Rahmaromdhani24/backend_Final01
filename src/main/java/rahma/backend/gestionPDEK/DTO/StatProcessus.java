package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
public class StatProcessus {
    private String processus;
    private String sectionFil;
    private String machine;
    private long nombrePdek;
    private long nombreErreurs;
    private String categoriePistolet ; 

    public StatProcessus(String processus, String sectionFil, String machine, long nombrePdek, long nombreErreurs) {
        this.processus = processus;
        this.sectionFil = sectionFil;
        this.machine = machine;
        this.nombrePdek = nombrePdek;
        this.nombreErreurs = nombreErreurs;
    }

    public StatProcessus(String processus , String categoriePistolet , long nombrePdek , long nombreErreurs ) {
        this.processus = processus;
        this.categoriePistolet = categoriePistolet;
        this.nombrePdek = nombrePdek;
        this.nombreErreurs = nombreErreurs;
    }
}
