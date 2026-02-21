// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/repository/JpaPasswordResetTokenRepository.java
package tn.smartfuture.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;

import java.util.Optional;

@Repository
public interface JpaPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
    void deleteByUserId(Long userId);
}