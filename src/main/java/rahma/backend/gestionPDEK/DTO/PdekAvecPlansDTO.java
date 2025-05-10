package rahma.backend.gestionPDEK.DTO;

import java.util.List;

import lombok.*;
import rahma.backend.gestionPDEK.Entity.*;

@Getter
@Setter
public class PdekAvecPlansDTO {
    private Long id;
    private int totalPages;
    private String sectionFil;
    private long frequenceControle;
    private int segment;
    private String numMachine;
    private String dateCreation;
    private TypesOperation typeOperation;
    private Plant plant;
    private String numeroOutils;
    private String numeroContacts;
    private String LGD;
    private double tolerance;
    private String posGradant;
    private Object users;
    private String poste;
    private List<PlanActionDTO> plans;

    public PdekAvecPlansDTO(Long id, int totalPages, String sectionFil, long frequenceControle, int segment,
                            String numMachine, String dateCreation, TypesOperation typeOperation, Plant plant,
                            String numeroOutils, String numeroContacts, String LGD, double tolerance, String posGradant,
                            Object users, String poste, List<PlanActionDTO> plans) {
        this.id = id;
        this.totalPages = totalPages;
        this.sectionFil = sectionFil;
        this.frequenceControle = frequenceControle;
        this.segment = segment;
        this.numMachine = numMachine;
        this.dateCreation = dateCreation;
        this.typeOperation = typeOperation;
        this.plant = plant;
        this.numeroOutils = numeroOutils;
        this.numeroContacts = numeroContacts;
        this.LGD = LGD;
        this.tolerance = tolerance;
        this.posGradant = posGradant;
        this.users = users;
        this.poste = poste;
        this.plans = plans;
    }




}
