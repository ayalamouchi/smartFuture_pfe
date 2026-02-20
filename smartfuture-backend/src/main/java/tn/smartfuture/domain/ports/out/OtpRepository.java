package tn.smartfuture.domain.ports.out;

import tn.smartfuture.domain.model.OtpCode;
import java.util.Optional;

public interface OtpRepository {
    OtpCode save(OtpCode otpCode);
    Optional<OtpCode> findByUserIdAndCode(Long userId, String code);
    void deleteByUserId(Long userId);
}