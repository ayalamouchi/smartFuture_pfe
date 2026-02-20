package tn.smartfuture.domain.ports.in;

import tn.smartfuture.application.dto.response.AuthResponse;

public interface VerifyOtpUseCase {
    AuthResponse verifyOtp(String email, String code);
}