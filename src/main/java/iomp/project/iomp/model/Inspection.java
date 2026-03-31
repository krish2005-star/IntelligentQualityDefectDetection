package iomp.project.iomp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private User inspector;

    private LocalDateTime inspectionDate;

    @Enumerated(EnumType.STRING)
    private InspectionStatus status;

    private Double confidenceScore;
    private String imagePath;

    @PrePersist
    protected void onCreate() {
        inspectionDate = LocalDateTime.now();
    }
}

enum InspectionStatus {
    PASSED, FAILED
}
