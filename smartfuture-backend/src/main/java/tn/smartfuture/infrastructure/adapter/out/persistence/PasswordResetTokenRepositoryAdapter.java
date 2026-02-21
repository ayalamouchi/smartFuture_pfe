// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/PasswordResetTokenRepositoryAdapter.java
package tn.smartfuture.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.PasswordResetToken;
import tn.smartfuture.domain.ports.out.PasswordResetTokenRepository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import tn.smartfuture.infrastructure.adapter.out.persistence.mapper.PasswordResetTokenMapper;
import tn.smartfuture.infrastructure.adapter.out.persistence.repository.JpaPasswordResetTokenRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final JpaPasswordResetTokenRepository jpaRepository;
    private final PasswordResetTokenMapper mapper;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = mapper.toEntity(token);
        PasswordResetTokenEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}