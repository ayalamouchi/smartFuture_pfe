// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/mapper/PasswordResetTokenMapper.java
package tn.smartfuture.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.PasswordResetToken;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;

@Component
public class PasswordResetTokenMapper {

    public PasswordResetTokenEntity toEntity(PasswordResetToken token) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setId(token.getId());
        entity.setUserId(token.getUserId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setIsUsed(token.getIsUsed());
        entity.setCreatedAt(token.getCreatedAt());
        return entity;
    }

    public PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        PasswordResetToken token = new PasswordResetToken();
        token.setId(entity.getId());
        token.setUserId(entity.getUserId());
        token.setToken(entity.getToken());
        token.setExpiresAt(entity.getExpiresAt());
        token.setIsUsed(entity.getIsUsed());
        token.setCreatedAt(entity.getCreatedAt());
        return token;
    }
}