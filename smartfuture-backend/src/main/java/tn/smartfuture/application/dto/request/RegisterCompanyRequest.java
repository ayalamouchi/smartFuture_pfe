package tn.smartfuture.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyRequest {

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String companyName;

    @NotBlank(message = "Le nom du contact est obligatoire")
    private String contactName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]{8,15}$")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    private String ville;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8)
    private String motDePasse;

    private Boolean accepteConditions = false;
    private Boolean accepteConfidentialite = false;
}