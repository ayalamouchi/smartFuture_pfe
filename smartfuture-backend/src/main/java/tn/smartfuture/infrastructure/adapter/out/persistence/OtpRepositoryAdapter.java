package tn.smartfuture.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.OtpCode;
import tn.smartfuture.domain.ports.out.OtpRepository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.OtpCodeEntity;
import tn.smartfuture.infrastructure.adapter.out.persistence.mapper.OtpMapper;
import tn.smartfuture.infrastructure.adapter.out.persistence.repository.JpaOtpRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OtpRepositoryAdapter implements OtpRepository {

    private final JpaOtpRepository jpaOtpRepository;
    private final OtpMapper otpMapper;

    @Override
    public OtpCode save(OtpCode otpCode) {
        OtpCodeEntity entity = otpMapper.toEntity(otpCode);
        OtpCodeEntity saved = jpaOtpRepository.save(entity);
        return otpMapper.toDomain(saved);
    }

    @Override
    public Optional<OtpCode> findByUserIdAndCode(Long userId, String code) {
        return jpaOtpRepository.findByUserIdAndCode(userId, code)
                .map(otpMapper::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaOtpRepository.deleteByUserId(userId);
    }
}