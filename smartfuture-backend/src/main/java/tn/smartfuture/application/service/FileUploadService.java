package tn.smartfuture.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.smartfuture.application.dto.response.UploadResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Value("${file.upload.base-url:http://localhost:8080/api/files}")
    private String baseUrl;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    public UploadResponse uploadFile(MultipartFile file, String type) throws IOException {
        // Validation
        validateFile(file, type);

        // Créer le dossier de destination
        Path uploadPath = Paths.get(uploadDir, type);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Construire l'URL
        String fileUrl = baseUrl + "/" + type + "/" + uniqueFileName;

        log.info("File uploaded successfully: {}", fileUrl);

        return UploadResponse.builder()
                .success(true)
                .message("Fichier uploadé avec succès")
                .fileUrl(fileUrl)
                .fileName(uniqueFileName)
                .build();
    }

    private void validateFile(MultipartFile file, String type) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide");
        }

        String contentType = file.getContentType();
        long fileSize = file.getSize();

        // Validation selon le type
        switch (type) {
            case "photo":
                if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                    throw new IllegalArgumentException("Format d'image non autorisé. Formats acceptés: JPG, PNG, WEBP");
                }
                if (fileSize > MAX_IMAGE_SIZE) {
                    throw new IllegalArgumentException("La taille de l'image ne doit pas dépasser 5MB");
                }
                break;

            case "cv":
            case "cv-cnfcpp":
                if (!ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
                    throw new IllegalArgumentException("Format de document non autorisé. Formats acceptés: PDF, DOC, DOCX");
                }
                if (fileSize > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException("La taille du document ne doit pas dépasser 10MB");
                }
                break;

            default:
                throw new IllegalArgumentException("Type de fichier non reconnu: " + type);
        }
    }

    public void deleteFile(String fileUrl) throws IOException {
        // Extraire le chemin du fichier depuis l'URL
        String relativePath = fileUrl.replace(baseUrl + "/", "");
        Path filePath = Paths.get(uploadDir, relativePath);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("File deleted: {}", fileUrl);
        }
    }
}