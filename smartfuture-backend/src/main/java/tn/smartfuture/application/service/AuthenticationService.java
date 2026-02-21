// src/main/java/tn/smartfuture/application/service/AuthenticationService.java
package tn.smartfuture.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.smartfuture.application.dto.request.*;
import tn.smartfuture.application.dto.response.AuthResponse;
import tn.smartfuture.application.dto.response.OtpSentResponse;
import tn.smartfuture.domain.enums.AccountStatus;
import tn.smartfuture.domain.enums.LearnerLevel;
import tn.smartfuture.domain.enums.UserRole;
import tn.smartfuture.domain.exception.InvalidOtpException;
import tn.smartfuture.domain.exception.UserAlreadyExistsException;
import tn.smartfuture.domain.exception.UserNotFoundException;
import tn.smartfuture.domain.model.*;
import tn.smartfuture.domain.ports.in.LoginUseCase;
import tn.smartfuture.domain.ports.in.RegisterUserUseCase;
import tn.smartfuture.domain.ports.in.VerifyOtpUseCase;
import tn.smartfuture.domain.ports.out.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements RegisterUserUseCase, VerifyOtpUseCase, LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtService jwtService;

    @Value("${verification.expiration-hours:24}")
    private int verificationExpirationHours;

    @Override
    @Transactional
    public OtpSentResponse registerLearner(Object request) {
        RegisterLearnerRequest req = (RegisterLearnerRequest) request;

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Cet email est déjà utilisé");
        }

        Learner learner = new Learner();
        learner.setEmail(req.getEmail());
        learner.setPhoneNumber(req.getTelephone());
        learner.setPasswordHash(passwordEncoder.encode(req.getMotDePasse()));
        learner.setRole(UserRole.LEARNER);
        learner.setStatus(AccountStatus.PENDING_VERIFICATION);
        learner.setEmailVerified(false);
        learner.setCreatedAt(LocalDateTime.now());
        learner.setFirstName(req.getPrenom());
        learner.setLastName(req.getNom());
        learner.setCompany(req.getCompany());
        learner.setVille(req.getVille());
        learner.setFonction(req.getFonction());
        learner.setProfilePicture(req.getProfilePictureUrl());
        learner.setLevel(LearnerLevel.BEGINNER);
        learner.setProfileCompletionRate(50);

        User savedUser = userRepository.save(learner);

        // Générer et envoyer le lien de vérification
        verificationTokenService.generateAndSendVerificationEmail(savedUser.getId(), savedUser.getEmail());

        log.info("Learner registered successfully: {}", req.getEmail());

        return OtpSentResponse.builder()
                .success(true)
                .message("Email de vérification envoyé")
                .email(req.getEmail())
                .expirationMinutes(verificationExpirationHours * 60)
                .build();
    }

    @Override
    @Transactional
    public OtpSentResponse registerTrainer(Object request) {
        RegisterTrainerRequest req = (RegisterTrainerRequest) request;

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Cet email est déjà utilisé");
        }

        Trainer trainer = new Trainer();
        trainer.setEmail(req.getEmail());
        trainer.setPhoneNumber(req.getTelephone());
        trainer.setPasswordHash(passwordEncoder.encode(req.getMotDePasse()));
        trainer.setRole(UserRole.TRAINER);
        trainer.setStatus(AccountStatus.PENDING_VERIFICATION);
        trainer.setEmailVerified(false);
        trainer.setCreatedAt(LocalDateTime.now());
        trainer.setFirstName(req.getPrenom());
        trainer.setLastName(req.getNom());
        trainer.setBio(req.getBio());
        trainer.setCvFileUrl(req.getCvFileUrl());
        trainer.setCvCNFCPPFileUrl(req.getCvCNFCPPFileUrl());
        trainer.setAverageRating(0.0f);
        trainer.setTotalSessionsAnimated(0);
        trainer.setIsValidated(false);

        User savedUser = userRepository.save(trainer);
        verificationTokenService.generateAndSendVerificationEmail(savedUser.getId(), savedUser.getEmail());

        log.info("Trainer registered successfully: {}", req.getEmail());

        return OtpSentResponse.builder()
                .success(true)
                .message("Email de vérification envoyé")
                .email(req.getEmail())
                .expirationMinutes(verificationExpirationHours * 60)
                .build();
    }

    @Override
    @Transactional
    public OtpSentResponse registerCompany(Object request) {
        RegisterCompanyRequest req = (RegisterCompanyRequest) request;

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Cet email est déjà utilisé");
        }

        Company company = new Company();
        company.setEmail(req.getEmail());
        company.setPhoneNumber(req.getTelephone());
        company.setPasswordHash(passwordEncoder.encode(req.getMotDePasse()));
        company.setRole(UserRole.COMPANY);
        company.setStatus(AccountStatus.PENDING_VERIFICATION);
        company.setEmailVerified(false);
        company.setCreatedAt(LocalDateTime.now());
        company.setCompanyName(req.getCompanyName());
        company.setContactName(req.getContactName());
        company.setAddress(req.getAddress());

        User savedUser = userRepository.save(company);
        verificationTokenService.generateAndSendVerificationEmail(savedUser.getId(), savedUser.getEmail());

        log.info("Company registered successfully: {}", req.getEmail());

        return OtpSentResponse.builder()
                .success(true)
                .message("Email de vérification envoyé")
                .email(req.getEmail())
                .expirationMinutes(verificationExpirationHours * 60)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(String email, String code) {
        // Cette méthode n'est plus utilisée avec le système de token
        // Garder pour compatibilité
        throw new UnsupportedOperationException("Utiliser verifyToken à la place");
    }

    @Transactional
    public AuthResponse verifyToken(String token) {
        // Vérifier le token
        verificationTokenService.verifyToken(token);

        // Récupérer l'utilisateur
        Long userId = verificationTokenService.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        // Activer le compte
        user.setEmailVerified(true);
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole(), user.getId());

        log.info("Email verified successfully for user: {}", user.getEmail());

        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .email(user.getEmail())
                .emailVerified(true)
                .message("Compte vérifié avec succès")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(String email, String password, UserRole role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email ou mot de passe incorrect"));

        if (!user.getRole().equals(role)) {
            throw new InvalidOtpException("Rôle incorrect pour cet utilisateur");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidOtpException("Email ou mot de passe incorrect");
        }

        if (!user.getEmailVerified()) {
            throw new InvalidOtpException("Veuillez vérifier votre email avant de vous connecter");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole(), user.getId());

        log.info("User logged in successfully: {}", email);

        String firstName = "";
        String lastName = "";

        if (user instanceof Learner learner) {
            firstName = learner.getFirstName();
            lastName = learner.getLastName();
        } else if (user instanceof Trainer trainer) {
            firstName = trainer.getFirstName();
            lastName = trainer.getLastName();
        } else if (user instanceof Company company) {
            firstName = company.getContactName();
            lastName = "";
        }

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole())
                .email(user.getEmail())
                .firstName(firstName)
                .lastName(lastName)
                .emailVerified(user.getEmailVerified())
                .message("Connexion réussie")
                .build();
    }
}