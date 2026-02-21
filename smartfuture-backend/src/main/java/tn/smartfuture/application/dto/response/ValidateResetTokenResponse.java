package tn.smartfuture.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResetTokenResponse {
    private Boolean valid;
    private String message;
    private String email; // Email de l'utilisateur si token valide
}