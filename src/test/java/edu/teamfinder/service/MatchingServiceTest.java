package edu.teamfinder.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.teamfinder.dto.ApiDtos.CandidateSummary;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MatchingServiceTest {
    private final MatchingService matching = new MatchingService(null, null, null, null);

    @Test void gives_required_skills_more_weight_than_preferred_skills() {
        Project project = project("EdTech", "Java:REQUIRED", "Spring Boot:REQUIRED", "React:PREFERRED");
        Student student = student("Java", "React"); student.setInterests("EdTech, accessibility");

        CandidateSummary result = matching.score(student, project, Set.of("java", "spring boot"), Set.of("react"));

        assertEquals(60, result.matchPercentage()); // 40 required + 15 preferred + 5 matching interest
        assertTrue(result.matchingSkills().contains("Java"));
        assertTrue(result.missingSkills().contains("Spring Boot"));
    }

    @Test void cannot_score_more_than_one_hundred_percent() {
        Project project = project("Sustainability", "Python:REQUIRED", "Docker:PREFERRED");
        Student student = student("Python", "Docker"); student.setInterests("Sustainability");

        CandidateSummary result = matching.score(student, project, Set.of("python"), Set.of("docker"));

        assertEquals(100, result.matchPercentage());
    }

    private static Project project(String category, String... definitions) {
        Project p = new Project(); p.setCategory(category);
        for (String definition : definitions) { String[] bits = definition.split(":"); Skill skill = new Skill(bits[0]); ProjectSkill ps = new ProjectSkill(); ps.setProject(p); ps.setSkill(skill); ps.setSkillType(SkillType.valueOf(bits[1])); p.getSkills().add(ps); }
        return p;
    }
    private static Student student(String... names) {
        Student s = new Student(); s.setFullName("Example Student"); s.setBranch("Computer Science"); s.setAcademicYear(3); s.setAvailability(Availability.AVAILABLE); s.setEmail("example@campus.dev"); s.setPassword("not-used");
        for (String name : names) { StudentSkill ss = new StudentSkill(); ss.setStudent(s); ss.setSkill(new Skill(name)); ss.setProficiency(Proficiency.ADVANCED); s.getSkills().add(ss); }
        return s;
    }
}
