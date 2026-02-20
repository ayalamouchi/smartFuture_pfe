package tn.smartfuture.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.OtpCodeEntity;

import java.util.Optional;

@Repository
public interface JpaOtpRepository extends JpaRepository<OtpCodeEntity, Long> {
    Optional<OtpCodeEntity> findByUserIdAndCode(Long userId, String code);
    void deleteByUserId(Long userId);
}