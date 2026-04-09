package iomp.project.iomp.repo;

import iomp.project.iomp.model.Inspection;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface InspectionRepo extends JpaRepository<Inspection, Long> {

    List<Inspection> findByStatus(Object status);
}