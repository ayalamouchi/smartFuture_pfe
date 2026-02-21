package tn.smartfuture.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTOs pour la réinitialisation de mot de passe
 */

// ============ REQUEST PASSWORD RESET (Demande de lien) ============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPasswordResetRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
}