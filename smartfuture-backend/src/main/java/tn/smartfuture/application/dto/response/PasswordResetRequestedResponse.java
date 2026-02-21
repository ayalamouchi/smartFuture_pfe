package tn.smartfuture.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ============ PASSWORD RESET REQUESTED RESPONSE ============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequestedResponse {
    private Boolean success;
    private String message;
    private String email;
}