package iomp.project.iomp.repo;

import iomp.project.iomp.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepo extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCustomerId(Long customerId);

    static List<Complaint> findByStatus(String status) {
        return null;
    }
}
