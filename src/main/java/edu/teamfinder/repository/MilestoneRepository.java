package edu.teamfinder.repository;

import edu.teamfinder.entity.Milestone;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProjectIdOrderByDueDateAsc(Long projectId);
}
