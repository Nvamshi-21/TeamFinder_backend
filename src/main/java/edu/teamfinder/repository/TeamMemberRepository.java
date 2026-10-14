package edu.teamfinder.repository;

import edu.teamfinder.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByProjectIdAndStudentId(Long projectId, Long studentId);
    long countByProjectId(Long projectId);
    List<TeamMember> findByProjectId(Long projectId);
    List<TeamMember> findByStudentId(Long studentId);
}
