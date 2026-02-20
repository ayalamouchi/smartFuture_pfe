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

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

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

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {
        log.info("Verifying OTP for: {}", request.getEmail());

        AuthResponse response = authenticationService.verifyOtp(
                request.getEmail(),
                request.getCode()
        );

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Vérification réussie")
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
}