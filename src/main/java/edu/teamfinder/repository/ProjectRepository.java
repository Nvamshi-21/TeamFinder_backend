package edu.teamfinder.repository;

import edu.teamfinder.entity.*;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths={"owner", "skills", "skills.skill", "members", "members.student"})
    Optional<Project> findDetailedById(Long id);
    @Query("select distinct p from Project p left join p.skills ps left join ps.skill sk where (:q is null or lower(p.title) like lower(concat('%', :q, '%')) or lower(p.category) like lower(concat('%', :q, '%'))) and (:category is null or lower(p.category)=lower(:category)) and (:status is null or p.status=:status) and (:skill is null or lower(sk.name)=lower(:skill)) and (:branch is null or lower(p.owner.branch)=lower(:branch))")
    Page<Project> search(String q, String category, Enums.ProjectStatus status, String skill, String branch, Pageable pageable);
    long countByStatus(Enums.ProjectStatus status);
    long countByOwnerId(Long ownerId);
    List<Project> findTop6ByStatusOrderByUpdatedAtDesc(Enums.ProjectStatus status);
    @EntityGraph(attributePaths={"owner", "skills", "skills.skill", "members", "members.student"})
    List<Project> findByMembersStudentId(Long studentId);
}
