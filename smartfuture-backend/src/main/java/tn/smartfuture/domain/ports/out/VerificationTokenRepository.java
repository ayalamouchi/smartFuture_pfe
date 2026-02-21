// src/main/java/tn/smartfuture/domain/ports/out/VerificationTokenRepository.java
package tn.smartfuture.domain.ports.out;

import tn.smartfuture.domain.model.VerificationToken;
import java.util.Optional;

public interface VerificationTokenRepository {
    VerificationToken save(VerificationToken token);
    Optional<VerificationToken> findByToken(String token);
    void deleteByUserId(Long userId);
}