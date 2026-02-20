package tn.smartfuture.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserRole role;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean emailVerified;
    private String message;
}