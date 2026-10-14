package edu.teamfinder.mapper;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import java.util.*;

public final class ApiMapper {
    private ApiMapper() { }
    public static StudentSummary student(Student s) {
        List<SkillSummary> skills = s.getSkills().stream().map(x -> new SkillSummary(x.getSkill().getId(), x.getSkill().getName(), x.getProficiency())).sorted(Comparator.comparing(SkillSummary::name)).toList();
        int fields = 0; if (notBlank(s.getBio())) fields++; if (notBlank(s.getInterests())) fields++; if (!skills.isEmpty()) fields++; if (notBlank(s.getAvatarUrl())) fields++;
        return new StudentSummary(s.getId(),s.getFullName(),s.getEmail(),s.getBranch(),s.getAcademicYear(),s.getBio(),s.getAvailability(),s.getRole(),s.getInterests(),s.getAvatarUrl(), 60 + fields * 10, skills);
    }
    public static ProjectSummary project(Project p) {
        List<ProjectSkillSummary> skills = p.getSkills().stream().map(x -> new ProjectSkillSummary(x.getSkill().getName(),x.getSkillType())).sorted(Comparator.comparing(ProjectSkillSummary::name)).toList();
        List<TeamMemberSummary> members = p.getMembers().stream().map(ApiMapper::member).sorted(Comparator.comparing(TeamMemberSummary::joinedAt)).toList();
        int count=members.size(); return new ProjectSummary(p.getId(),p.getTitle(),p.getShortDescription(),p.getDescription(),p.getCategory(),p.getTeamSize(),count,Math.max(0,p.getTeamSize()-count),p.getStatus(),p.getOwner().getId(),p.getOwner().getFullName(),skills,members,p.getCreatedAt(),p.getUpdatedAt());
    }
    public static TeamMemberSummary member(TeamMember m) { return new TeamMemberSummary(m.getId(),m.getStudent().getId(),m.getStudent().getFullName(),m.getStudent().getEmail(),m.getRole(),m.getJoinedAt()); }
    public static TeamRequestSummary request(TeamRequest r) { return new TeamRequestSummary(r.getId(),r.getProject().getId(),r.getProject().getTitle(),r.getFromStudent().getId(),r.getFromStudent().getFullName(),r.getToStudent().getId(),r.getToStudent().getFullName(),r.getStatus(),r.getCreatedAt()); }
    public static MilestoneSummary milestone(Milestone m) { return new MilestoneSummary(m.getId(),m.getTitle(),m.getDescription(),m.getDueDate(),m.getStatus(),m.getProgressPercentage(),m.getUpdatedAt()); }
    public static ActivitySummary activity(ActivityLog a) { return new ActivitySummary(a.getId(),a.getActivityType(),a.getMessage(),a.getStudent()==null?null:a.getStudent().getFullName(),a.getCreatedAt()); }
    private static boolean notBlank(String v) { return v != null && !v.isBlank(); }
}
