package rahma.backend.gestionPDEK.Entity;

	public enum TypesOperation {
	    Soudure("Opération de soudure a ultrason"),
	    Torsadage("Opération de torsadage"),
		Sertissage_Normal("Opération de Sertissage Normal"),
		Sertissage_IDC("Opération de Sertissage IDC "),
		Montage_Pistolet("Opération de montage Pistolet");

	    private final String description;

	    TypesOperation(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }
	}
