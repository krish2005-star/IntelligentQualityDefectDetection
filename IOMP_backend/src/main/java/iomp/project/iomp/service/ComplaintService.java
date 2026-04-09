package iomp.project.iomp.service;

import iomp.project.iomp.model.Complaint;
import iomp.project.iomp.repo.ComplaintRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepo complaintRepository;

    @Autowired
    private MLService mlService;

    private final String UPLOAD_DIR = "uploads/complaints/";

    public Complaint submitComplaint(Complaint complaint, MultipartFile image) throws IOException {

        // Save image to disk
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filepath = Paths.get(UPLOAD_DIR + filename);
        Files.write(filepath, image.getBytes());

        // Run ML detection
        Map<String, Object> mlResult = mlService.detectDefects(image);

        // Extract detection results
        if (mlResult.containsKey("detections")) {
            List<Map<String, Object>> detections = (List<Map<String, Object>>) mlResult.get("detections");
            if (!detections.isEmpty()) {
                Map<String, Object> topDetection = detections.get(0);
                complaint.setDefectDetected((String) topDetection.get("class"));
                complaint.setConfidenceScore((Double) topDetection.get("confidence"));
            }
        }

        // Save image path
        complaint.setImagePath(filepath.toString());

        // Save to database
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }
}
