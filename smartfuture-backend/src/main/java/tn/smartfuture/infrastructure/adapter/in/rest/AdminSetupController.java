package tn.smartfuture.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.smartfuture.domain.enums.AccountStatus;
import tn.smartfuture.domain.enums.UserRole;
import tn.smartfuture.domain.model.Administrator;
import tn.smartfuture.domain.ports.out.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin-setup")
@RequiredArgsConstructor
public class AdminSetupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Endpoint pour créer ou réinitialiser le compte admin
     * Appelez : http://localhost:8080/api/admin-setup/create-admin?email=ayalamouchi@ieee.org&password=Admin123
     */
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(
            @RequestParam String email,
            @RequestParam String password
    ) {
        try {
            // Vérifier si l'utilisateur existe déjà
            var existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                // Mettre à jour l'utilisateur existant
                Administrator admin = new Administrator();
                admin.setId(existingUser.get().getId());
                admin.setEmail(email);
                admin.setPhoneNumber("12345678");
                admin.setPasswordHash(passwordEncoder.encode(password));
                admin.setRole(UserRole.ADMIN);
                admin.setStatus(AccountStatus.ACTIVE);
                admin.setEmailVerified(true);
                admin.setCreatedAt(existingUser.get().getCreatedAt());
                admin.setLastLogin(null);

                userRepository.save(admin);

                log.info("Admin account updated: {}", email);
                return ResponseEntity.ok("Admin account updated successfully!\n" +
                        "Email: " + email + "\n" +
                        "Password: " + password + "\n" +
                        "Password Hash: " + admin.getPasswordHash());
            } else {
                // Créer un nouvel utilisateur
                Administrator admin = new Administrator();
                admin.setEmail(email);
                admin.setPhoneNumber("12345678");
                admin.setPasswordHash(passwordEncoder.encode(password));
                admin.setRole(UserRole.ADMIN);
                admin.setStatus(AccountStatus.ACTIVE);
                admin.setEmailVerified(true);
                admin.setCreatedAt(LocalDateTime.now());

                userRepository.save(admin);

                log.info("Admin account created: {}", email);
                return ResponseEntity.ok("Admin account created successfully!\n" +
                        "Email: " + email + "\n" +
                        "Password: " + password + "\n" +
                        "Password Hash: " + admin.getPasswordHash());
            }

        } catch (Exception e) {
            log.error("Error creating admin account", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint pour générer un hash BCrypt
     * Appelez : http://localhost:8080/api/admin-setup/generate-hash?password=Admin123
     */
    @GetMapping("/generate-hash")
    public ResponseEntity<String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok("Password: " + password + "\nBCrypt Hash:\n" + hash);
    }

    /**
     * Endpoint pour vérifier un hash
     * Appelez : http://localhost:8080/api/admin-setup/verify?password=Admin123&hash=...
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verifyHash(
            @RequestParam String password,
            @RequestParam String hash
    ) {
        boolean matches = passwordEncoder.matches(password, hash);
        return ResponseEntity.ok("Password: " + password + "\n" +
                "Hash: " + hash + "\n" +
                "Matches: " + matches);
    }
}