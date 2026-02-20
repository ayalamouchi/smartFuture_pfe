package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.AccountStatus;
import tn.smartfuture.domain.enums.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    private Long id;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private UserRole role;
    private AccountStatus status;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}