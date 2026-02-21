// src/main/java/tn/smartfuture/infrastructure/adapter/in/rest/AuthController.java
package tn.smartfuture.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartfuture.application.dto.request.*;
import tn.smartfuture.application.dto.response.ApiResponse;
import tn.smartfuture.application.dto.response.AuthResponse;
import tn.smartfuture.application.dto.response.OtpSentResponse;
import tn.smartfuture.application.service.AuthenticationService;
import tn.smartfuture.application.service.PasswordResetService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register/learner")
    public ResponseEntity<ApiResponse<OtpSentResponse>> registerLearner(
            @Valid @RequestBody RegisterLearnerRequest request
    ) {
        log.info("Registering learner: {}", request.getEmail());
        OtpSentResponse response = authenticationService.registerLearner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OtpSentResponse>builder()
                        .success(true)
                        .message("Inscription réussie")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/register/trainer")
    public ResponseEntity<ApiResponse<OtpSentResponse>> registerTrainer(
            @Valid @RequestBody RegisterTrainerRequest request
    ) {
        log.info("Registering trainer: {}", request.getEmail());
        OtpSentResponse response = authenticationService.registerTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OtpSentResponse>builder()
                        .success(true)
                        .message("Inscription réussie")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/register/company")
    public ResponseEntity<ApiResponse<OtpSentResponse>> registerCompany(
            @Valid @RequestBody RegisterCompanyRequest request
    ) {
        log.info("Registering company: {}", request.getEmail());
        OtpSentResponse response = authenticationService.registerCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OtpSentResponse>builder()
                        .success(true)
                        .message("Inscription réussie")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyEmail(
            @RequestParam("token") String token
    ) {
        log.info("Verifying email with token");
        AuthResponse response = authenticationService.verifyToken(token);
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Email vérifié avec succès")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login attempt for: {} with role: {}", request.getEmail(), request.getRole());
        AuthResponse response = authenticationService.login(
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Connexion réussie")
                        .data(response)
                        .build()
        );
    }

    // ============ MOT DE PASSE OUBLIÉ ============

    /**
     * Demander un lien de réinitialisation de mot de passe
     */
    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(
            @Valid @RequestBody RequestPasswordResetRequest request
    ) {
        log.info("Password reset requested for email: {}", request.getEmail());

        passwordResetService.generateAndSendResetLink(request.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Si cet email existe, un lien de réinitialisation a été envoyé")
                        .build()
        );
    }

    /**
     * Valider le token de réinitialisation
     */
    @GetMapping("/password/validate-token")
    public ResponseEntity<ApiResponse<Void>> validateResetToken(
            @RequestParam("token") String token
    ) {
        log.info("Validating password reset token");

        passwordResetService.validateResetToken(token);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Token valide")
                        .build()
        );
    }

    /**
     * Réinitialiser le mot de passe avec le token
     */
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        log.info("Resetting password with token");

        // Vérifier que les mots de passe correspondent
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message("Les mots de passe ne correspondent pas")
                            .error("PASSWORD_MISMATCH")
                            .build()
            );
        }

        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Mot de passe réinitialisé avec succès")
                        .build()
        );
    }
}