package tn.smartfuture.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.smartfuture.domain.exception.InvalidOtpException;
import tn.smartfuture.domain.exception.OtpExpiredException;
import tn.smartfuture.domain.model.OtpCode;
import tn.smartfuture.domain.ports.out.EmailSender;
import tn.smartfuture.domain.ports.out.OtpRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailSender emailSender;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.expiration-minutes:5}")
    private int expirationMinutes;

    @Transactional
    public void generateAndSendOtp(Long userId, String email) {
        // Supprimer les anciens codes OTP
        otpRepository.deleteByUserId(userId);

        // Générer un nouveau code
        String code = generateOtpCode();

        // Créer l'entité OTP
        OtpCode otpCode = new OtpCode();
        otpCode.setUserId(userId);
        otpCode.setCode(code);
        otpCode.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));
        otpCode.setIsUsed(false);
        otpCode.setAttempts(0);
        otpCode.setCreatedAt(LocalDateTime.now());

        // Sauvegarder
        otpRepository.save(otpCode);

        // Envoyer l'email
        emailSender.sendOtpEmail(email, code);
    }

    @Transactional
    public void verifyOtp(Long userId, String code) {
        OtpCode otpCode = otpRepository.findByUserIdAndCode(userId, code)
                .orElseThrow(() -> new InvalidOtpException("Code OTP invalide"));

        // Vérifier si déjà utilisé
        if (otpCode.getIsUsed()) {
            throw new InvalidOtpException("Ce code a déjà été utilisé");
        }

        // Vérifier l'expiration
        if (LocalDateTime.now().isAfter(otpCode.getExpiresAt())) {
            throw new OtpExpiredException("Le code OTP a expiré");
        }

        // Marquer comme utilisé
        otpCode.setIsUsed(true);
        otpRepository.save(otpCode);
    }

    private String generateOtpCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}