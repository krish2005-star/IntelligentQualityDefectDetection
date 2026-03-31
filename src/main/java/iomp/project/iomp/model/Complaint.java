package iomp.project.iomp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
        status = ComplaintStatus.OPEN;
    }
}

enum ComplaintStatus {
    OPEN, UNDER_REVIEW, RESOLVED
}
