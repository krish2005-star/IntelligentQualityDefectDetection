package iomp.project.iomp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "defects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inspection_id")
    private Inspection inspection;

    @Enumerated(EnumType.STRING)
    private DefectType defectType;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String boundingBox; // JSON format: {x, y, width, height}

    private Double confidenceScore;
}

enum DefectType {
    SCRATCH, CRACK, DISCOLORATION, OTHER
}

enum Severity {
    LOW, MEDIUM, HIGH
}
