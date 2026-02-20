package tn.smartfuture.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.OtpCode;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.OtpCodeEntity;

@Component
public class OtpMapper {

    public OtpCodeEntity toEntity(OtpCode otpCode) {
        OtpCodeEntity entity = new OtpCodeEntity();
        entity.setId(otpCode.getId());
        entity.setUserId(otpCode.getUserId());
        entity.setCode(otpCode.getCode());
        entity.setExpiresAt(otpCode.getExpiresAt());
        entity.setIsUsed(otpCode.getIsUsed());
        entity.setAttempts(otpCode.getAttempts());
        entity.setCreatedAt(otpCode.getCreatedAt());
        return entity;
    }

    public OtpCode toDomain(OtpCodeEntity entity) {
        OtpCode otpCode = new OtpCode();
        otpCode.setId(entity.getId());
        otpCode.setUserId(entity.getUserId());
        otpCode.setCode(entity.getCode());
        otpCode.setExpiresAt(entity.getExpiresAt());
        otpCode.setIsUsed(entity.getIsUsed());
        otpCode.setAttempts(entity.getAttempts());
        otpCode.setCreatedAt(entity.getCreatedAt());
        return otpCode;
    }
}