// src/main/java/tn/smartfuture/domain/ports/out/PasswordResetTokenRepository.java
package tn.smartfuture.domain.ports.out;

import tn.smartfuture.domain.model.PasswordResetToken;
import java.util.Optional;

public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserId(Long userId);
}