package tn.smartfuture.domain.ports.in;

import tn.smartfuture.application.dto.response.AuthResponse;
import tn.smartfuture.domain.enums.UserRole;

public interface LoginUseCase {
    AuthResponse login(String email, String password, UserRole role);
}