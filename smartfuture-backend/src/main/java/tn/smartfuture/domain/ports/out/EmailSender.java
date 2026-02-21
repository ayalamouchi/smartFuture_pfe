// src/main/java/tn/smartfuture/domain/ports/out/EmailSender.java
package tn.smartfuture.domain.ports.out;

public interface EmailSender {
    void sendOtpEmail(String to, String otpCode);
    void sendVerificationEmail(String to, String verificationLink);
    void sendPasswordResetEmail(String to, String resetLink, String firstName);
}