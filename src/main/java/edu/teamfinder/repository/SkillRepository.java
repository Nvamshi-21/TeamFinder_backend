package edu.teamfinder.repository;

import edu.teamfinder.entity.Skill;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByNameIgnoreCase(String name);
    List<Skill> findTop10ByOrderByNameAsc();
}
