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
            message.setText(buildEmailBody(otpCode));

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    private String buildEmailBody(String otpCode) {
        return String.format("""
            Bienvenue chez SmartFuture !
            
            Votre code de vérification est : %s
            
            Ce code expire dans 5 minutes.
            
            Si vous n'avez pas demandé ce code, veuillez ignorer cet email.
            
            Cordialement,
            L'équipe SmartFuture
            """, otpCode);
    }
}