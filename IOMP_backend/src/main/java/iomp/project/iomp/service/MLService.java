package iomp.project.iomp.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

    @Service
    public class MLService {

        private final String ML_API_URL = "http://localhost:5001";
        private final RestTemplate restTemplate = new RestTemplate();

        public Map<String, Object> detectDefects(MultipartFile image) throws IOException {
            // Save file temporarily
            String tempDir = System.getProperty("java.io.tmpdir");
            Path tempFile = Paths.get(tempDir, image.getOriginalFilename());
            Files.write(tempFile, image.getBytes());

            // Prepare request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new FileSystemResource(tempFile.toFile()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Call Flask API
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    ML_API_URL + "/detect",
                    requestEntity,
                    Map.class
            );

            // Clean up temp file
            Files.deleteIfExists(tempFile);

            return response.getBody();
        }

        public Map<String, Object> checkHealth() {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    ML_API_URL + "/health",
                    Map.class
            );
            return response.getBody();
        }
    }
