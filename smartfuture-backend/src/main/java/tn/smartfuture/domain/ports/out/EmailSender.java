package tn.smartfuture.domain.ports.out;

public interface EmailSender {
    void sendOtpEmail(String to, String otpCode);
}