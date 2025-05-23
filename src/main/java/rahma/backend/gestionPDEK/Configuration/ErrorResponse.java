package rahma.backend.gestionPDEK.Configuration;

import lombok.*;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
