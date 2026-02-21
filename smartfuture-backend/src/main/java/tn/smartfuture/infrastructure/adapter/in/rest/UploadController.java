package tn.smartfuture.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.smartfuture.application.dto.response.ApiResponse;
import tn.smartfuture.application.dto.response.UploadResponse;
import tn.smartfuture.application.service.FileUploadService;

@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<ApiResponse<UploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        log.info("Uploading file: {} with type: {}", file.getOriginalFilename(), type);

        try {
            UploadResponse response = fileUploadService.uploadFile(file, type);

            return ResponseEntity.ok(
                    ApiResponse.<UploadResponse>builder()
                            .success(true)
                            .message("Fichier uploadé avec succès")
                            .data(response)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<UploadResponse>builder()
                            .success(false)
                            .message("Erreur lors de l'upload du fichier")
                            .error(e.getMessage())
                            .build()
            );
        }
    }
}