package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpCode {
    private Long id;
    private Long userId;
    private String code;
    private LocalDateTime expiresAt;
    private Boolean isUsed;
    private Integer attempts;
    private LocalDateTime createdAt;
}