package iomp.project.iomp.controller;

import iomp.project.iomp.model.Inspection;
import iomp.project.iomp.model.InspectionStatus;
import iomp.project.iomp.repo.InspectionRepo;
import iomp.project.iomp.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inspections")
@CrossOrigin(origins = "*")
public class InspectionController {

    @Autowired
    private InspectionRepo inspectionRepo;

    @Autowired
    private MLService mlService;

    private final String UPLOAD_DIR = "uploads/inspections/";

    @PostMapping("/inspect")
    public ResponseEntity<Inspection> inspectProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {

        // Save image
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filepath = Paths.get(UPLOAD_DIR + filename);
        Files.write(filepath, image.getBytes());

        // Run ML detection
        Map<String, Object> mlResult = mlService.detectDefects(image);

        // Create inspection record
        Inspection inspection = new Inspection();
        inspection.setImagePath(filepath.toString());

        // Set status based on detection
        List<Map<String, Object>> detections = (List<Map<String, Object>>) mlResult.get("detections");
        if (detections != null && !detections.isEmpty()) {
            inspection.setStatus(InspectionStatus.FAILED);
            inspection.setConfidenceScore((Double) detections.get(0).get("confidence"));
        } else {
            inspection.setStatus(InspectionStatus.PASSED);
        }

        return ResponseEntity.ok(inspectionRepo.save(inspection));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Inspection>> getAllInspections() {
        return ResponseEntity.ok(inspectionRepo.findAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        long total = inspectionRepo.count();
        long passed = inspectionRepo.findByStatus(InspectionStatus.PASSED).size();
        long failed = total - passed;

        return ResponseEntity.ok(Map.of(
                "total", total,
                "passed", passed,
                "failed", failed
        ));
    }
}