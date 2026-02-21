// src/main/java/tn/smartfuture/infrastructure/adapter/out/persistence/mapper/VerificationTokenMapper.java
package tn.smartfuture.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.VerificationToken;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;

@Component
public class VerificationTokenMapper {

    public VerificationTokenEntity toEntity(VerificationToken token) {
        VerificationTokenEntity entity = new VerificationTokenEntity();
        entity.setId(token.getId());
        entity.setUserId(token.getUserId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setIsUsed(token.getIsUsed());
        entity.setCreatedAt(token.getCreatedAt());
        return entity;
    }

    public VerificationToken toDomain(VerificationTokenEntity entity) {
        VerificationToken token = new VerificationToken();
        token.setId(entity.getId());
        token.setUserId(entity.getUserId());
        token.setToken(entity.getToken());
        token.setExpiresAt(entity.getExpiresAt());
        token.setIsUsed(entity.getIsUsed());
        token.setCreatedAt(entity.getCreatedAt());
        return token;
    }
}