// src/main/java/tn/smartfuture/application/service/PasswordResetService.java
package tn.smartfuture.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.smartfuture.domain.exception.InvalidOtpException;
import tn.smartfuture.domain.exception.OtpExpiredException;
import tn.smartfuture.domain.exception.UserNotFoundException;
import tn.smartfuture.domain.model.PasswordResetToken;
import tn.smartfuture.domain.model.User;
import tn.smartfuture.domain.ports.out.EmailSender;
import tn.smartfuture.domain.ports.out.PasswordResetTokenRepository;
import tn.smartfuture.domain.ports.out.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${password-reset.expiration-hours:24}")
    private int expirationHours;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    /**
     * Générer et envoyer le lien de réinitialisation de mot de passe
     */
    @Transactional
    public void generateAndSendResetLink(String email) {
        // Trouver l'utilisateur
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Aucun compte associé à cet email"));

        // Supprimer les anciens tokens de cet utilisateur
        tokenRepository.deleteByUserId(user.getId());

        // Générer un nouveau token
        String token = UUID.randomUUID().toString();

        // Créer l'entité token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getId());
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));
        resetToken.setIsUsed(false);
        resetToken.setCreatedAt(LocalDateTime.now());

        // Sauvegarder
        tokenRepository.save(resetToken);

        // Construire le lien de réinitialisation
        String resetLink = frontendUrl + "/mot-de-passe/reinitialiser?token=" + token;

        // Envoyer l'email
        emailSender.sendPasswordResetEmail(user.getEmail(), resetLink, getFirstName(user));

        log.info("Password reset link sent to: {}", email);
    }

    /**
     * Valider le token de réinitialisation
     */
    @Transactional(readOnly = true)
    public void validateResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidOtpException("Token de réinitialisation invalide"));

        // Vérifier si déjà utilisé
        if (resetToken.getIsUsed()) {
            throw new InvalidOtpException("Ce lien a déjà été utilisé");
        }

        // Vérifier l'expiration
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new OtpExpiredException("Le lien de réinitialisation a expiré");
        }
    }

    /**
     * Réinitialiser le mot de passe avec le token
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // Valider le token
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidOtpException("Token de réinitialisation invalide"));

        // Vérifier si déjà utilisé
        if (resetToken.getIsUsed()) {
            throw new InvalidOtpException("Ce lien a déjà été utilisé");
        }

        // Vérifier l'expiration
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new OtpExpiredException("Le lien de réinitialisation a expiré");
        }

        // Récupérer l'utilisateur
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        // Mettre à jour le mot de passe
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Marquer le token comme utilisé
        resetToken.setIsUsed(true);
        tokenRepository.save(resetToken);

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    /**
     * Obtenir le prénom de l'utilisateur
     */
    private String getFirstName(User user) {
        if (user instanceof tn.smartfuture.domain.model.Learner learner) {
            return learner.getFirstName();
        } else if (user instanceof tn.smartfuture.domain.model.Trainer trainer) {
            return trainer.getFirstName();
        } else if (user instanceof tn.smartfuture.domain.model.Company company) {
            return company.getContactName();
        }
        return "Utilisateur";
    }
}