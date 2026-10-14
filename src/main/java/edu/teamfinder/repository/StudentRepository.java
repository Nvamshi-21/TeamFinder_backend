package edu.teamfinder.repository;

import edu.teamfinder.entity.Student;
import edu.teamfinder.entity.Enums.Availability;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    Page<Student> findByRoleAndFullNameContainingIgnoreCase(edu.teamfinder.entity.Enums.Role role, String name, Pageable pageable);
    @Query("select distinct s from Student s left join s.skills ss left join ss.skill sk where s.role = :role and (:skill is null or lower(sk.name) = lower(:skill)) and (:branch is null or lower(s.branch) = lower(:branch)) and (:year is null or s.academicYear = :year) and (:availability is null or s.availability = :availability)")
    Page<Student> search(edu.teamfinder.entity.Enums.Role role, String skill, String branch, Integer year, Availability availability, Pageable pageable);
}
