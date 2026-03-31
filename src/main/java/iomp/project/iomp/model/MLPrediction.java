package iomp.project.iomp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ml_predictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MLPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePath;

    @Column(columnDefinition = "TEXT")
    private String predictionResult; // JSON format

    private Double confidenceScore;

    @Enumerated(EnumType.STRING)
    private PredictionSource source;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

enum PredictionSource {
    FACTORY, CUSTOMER
}
