// src/main/java/tn/smartfuture/domain/model/PasswordResetToken.java
package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private Boolean isUsed;
    private LocalDateTime createdAt;
}