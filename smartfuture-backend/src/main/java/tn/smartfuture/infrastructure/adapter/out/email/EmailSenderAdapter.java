// src/main/java/tn/smartfuture/infrastructure/adapter/out/email/EmailSenderAdapter.java
package tn.smartfuture.infrastructure.adapter.out.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import tn.smartfuture.domain.ports.out.EmailSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("SmartFuture - Code de vérification");
            message.setText(buildOtpEmailBody(otpCode));

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("SmartFuture - Vérification de votre compte");
            message.setText(buildVerificationEmailBody(verificationLink));

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("SmartFuture - Réinitialisation de votre mot de passe");
            message.setText(buildPasswordResetEmailBody(resetLink, firstName));

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    private String buildOtpEmailBody(String otpCode) {
        return String.format("""
            Bienvenue chez SmartFuture !
            
            Votre code de vérification est : %s
            
            Ce code expire dans 5 minutes.
            
            Si vous n'avez pas demandé ce code, veuillez ignorer cet email.
            
            Cordialement,
            L'équipe SmartFuture
            """, otpCode);
    }

    private String buildVerificationEmailBody(String verificationLink) {
        return String.format("""
            Bienvenue chez SmartFuture !
            
            Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :
            
            %s
            
            Ce lien est valide pendant 24 heures.
            
            Si vous n'avez pas créé de compte, veuillez ignorer cet email.
            
            Cordialement,
            L'équipe SmartFuture
            """, verificationLink);
    }

    private String buildPasswordResetEmailBody(String resetLink, String firstName) {
        return String.format("""
            Bonjour %s,
            
            Vous avez demandé la réinitialisation de votre mot de passe SmartFuture.
            
            Pour créer un nouveau mot de passe, cliquez sur le lien ci-dessous :
            
            %s
            
            Ce lien est valide pendant 24 heures.
            
            Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.
            Votre mot de passe actuel reste inchangé.
            
            Pour des raisons de sécurité, ne partagez jamais ce lien avec personne.
            
            Cordialement,
            L'équipe SmartFuture
            """, firstName, resetLink);
    }
}