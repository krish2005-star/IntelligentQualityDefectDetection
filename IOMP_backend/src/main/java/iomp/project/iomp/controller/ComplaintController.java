package iomp.project.iomp.controller;

import iomp.project.iomp.model.Complaint;
import iomp.project.iomp.model.ComplaintStatus;
import iomp.project.iomp.repo.ComplaintRepo;
import iomp.project.iomp.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ComplaintRepo complaintRepo;

    @PostMapping("/submit")
    public ResponseEntity<Complaint> submitComplaint(
            @RequestParam("description") String description,
            @RequestParam("customerId") Long customerId,
            @RequestParam("image") MultipartFile image) {
        try {
            Complaint complaint = new Complaint();
            complaint.setDescription(description);
            Complaint saved = complaintService.submitComplaint(complaint, image);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaint(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Complaint complaint = complaintService.getComplaintById(id);
        complaint.setStatus(ComplaintStatus.valueOf(status.toUpperCase()));

        complaintRepo.save(complaint);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Complaint>> getPendingComplaints() {
        return ResponseEntity.ok(
                ComplaintRepo.findByStatus("OPEN")
        );
    }
}
