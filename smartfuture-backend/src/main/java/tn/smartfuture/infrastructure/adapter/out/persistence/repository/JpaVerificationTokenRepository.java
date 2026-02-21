// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/repository/JpaVerificationTokenRepository.java
package tn.smartfuture.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;

import java.util.Optional;

@Repository
public interface JpaVerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
    Optional<VerificationTokenEntity> findByToken(String token);
    void deleteByUserId(Long userId);
}