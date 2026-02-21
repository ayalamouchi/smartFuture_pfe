// src/main/java/tn/smartfuture/application/service/VerificationTokenService.java
package tn.smartfuture.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.smartfuture.domain.exception.InvalidOtpException;
import tn.smartfuture.domain.exception.OtpExpiredException;
import tn.smartfuture.domain.model.VerificationToken;
import tn.smartfuture.domain.ports.out.EmailSender;
import tn.smartfuture.domain.ports.out.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final EmailSender emailSender;

    @Value("${verification.expiration-hours:24}")
    private int expirationHours;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Transactional
    public void generateAndSendVerificationEmail(Long userId, String email) {
        // Supprimer les anciens tokens
        tokenRepository.deleteByUserId(userId);

        // Générer un token unique
        String token = UUID.randomUUID().toString();

        // Créer l'entité token
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserId(userId);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));
        verificationToken.setIsUsed(false);
        verificationToken.setCreatedAt(LocalDateTime.now());

        // Sauvegarder
        tokenRepository.save(verificationToken);

        // Construire le lien de vérification
        String verificationLink = frontendUrl + "/inscription/verify?token=" + token;

        // Envoyer l'email
        emailSender.sendVerificationEmail(email, verificationLink);
    }

    @Transactional
    public void verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidOtpException("Token de vérification invalide"));

        // Vérifier si déjà utilisé
        if (verificationToken.getIsUsed()) {
            throw new InvalidOtpException("Ce lien a déjà été utilisé");
        }

        // Vérifier l'expiration
        if (LocalDateTime.now().isAfter(verificationToken.getExpiresAt())) {
            throw new OtpExpiredException("Le lien de vérification a expiré");
        }

        // Marquer comme utilisé
        verificationToken.setIsUsed(true);
        tokenRepository.save(verificationToken);
    }

    public Long getUserIdFromToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidOtpException("Token invalide"));
        return verificationToken.getUserId();
    }
}