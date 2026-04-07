package iomp.project.iomp.controller;

import iomp.project.iomp.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/defects")
@CrossOrigin(origins = "*")
public class DefectDetectionController {

    @Autowired
    private MLService mlService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = mlService.checkHealth();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/detect")
    public ResponseEntity<Map<String, Object>> detectDefects(
            @RequestParam("image") MultipartFile image) {
        try {
            Map<String, Object> result = mlService.detectDefects(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
