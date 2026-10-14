package edu.teamfinder.repository;

import edu.teamfinder.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {
    boolean existsByProjectIdAndToStudentIdAndStatus(Long projectId, Long toStudentId, Enums.RequestStatus status);
    Optional<TeamRequest> findByProjectIdAndToStudentId(Long projectId, Long toStudentId);
    List<TeamRequest> findByToStudentIdOrderByCreatedAtDesc(Long studentId);
    List<TeamRequest> findByProjectIdAndStatus(Long projectId, Enums.RequestStatus status);
    long countByStatus(Enums.RequestStatus status);
}
