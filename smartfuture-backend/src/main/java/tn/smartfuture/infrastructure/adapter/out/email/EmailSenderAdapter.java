// src/main/java/tn/smartfuture/infrastructure/adapter/out/email/EmailSenderAdapter.java
package tn.smartfuture.infrastructure.adapter.out.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tn.smartfuture.domain.ports.out.EmailSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSender {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendOtpEmail(String to, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("SmartFuture - Code de vérification");
            helper.setText(buildOtpEmailBody(otpCode), false);
            mailSender.send(message);
            log.info("OTP email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("SmartFuture - Vérification de votre compte");
            helper.setText(buildVerificationEmailBody(verificationLink), false);
            mailSender.send(message);
            log.info("Verification email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink, String firstName) {
        try {
            // Préparer le contexte Thymeleaf
            Context context = new Context();
            context.setVariable("firstName", firstName);
            context.setVariable("resetLink", resetLink);

            // Générer le HTML depuis le template
            String htmlContent = templateEngine.process("password-reset-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom("noreply@smartfuture.tn", "SmartFuture");
            helper.setSubject("SmartFuture - Réinitialisation de votre mot de passe");
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info("Password reset email sent to: {}", to);

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
}