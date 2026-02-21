// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/VerificationTokenRepositoryAdapter.java
package tn.smartfuture.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.VerificationToken;
import tn.smartfuture.domain.ports.out.VerificationTokenRepository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;
import tn.smartfuture.infrastructure.adapter.out.persistence.mapper.VerificationTokenMapper;
import tn.smartfuture.infrastructure.adapter.out.persistence.repository.JpaVerificationTokenRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationTokenRepositoryAdapter implements VerificationTokenRepository {

    private final JpaVerificationTokenRepository jpaRepository;
    private final VerificationTokenMapper mapper;

    @Override
    public VerificationToken save(VerificationToken token) {
        VerificationTokenEntity entity = mapper.toEntity(token);
        VerificationTokenEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}