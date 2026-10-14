package edu.teamfinder.dto;

import edu.teamfinder.entity.Enums;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.*;

/** JSON contracts live together so the API vocabulary is easy to audit. */
public final class ApiDtos {
    private ApiDtos() { }

    public record RegisterRequest(
        @NotBlank @Size(max=100) String fullName,
        @NotBlank @Email @Size(max=150) String email,
        @NotBlank @Size(min=8,max=72) String password,
        @NotBlank @Size(max=100) String branch,
        @NotNull @Min(1) @Max(8) Integer academicYear,
        Enums.Role role
    ) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record AuthResponse(String token, String tokenType, StudentSummary user) {}
    public record SkillInput(@NotBlank @Size(max=80) String name, @NotNull Enums.Proficiency proficiency) {}
    public record SkillSummary(Long id, String name, Enums.Proficiency proficiency) {}
    public record StudentSummary(Long id, String fullName, String email, String branch, Integer academicYear, String bio,
                                 Enums.Availability availability, Enums.Role role, String interests, String avatarUrl,
                                 Integer profileCompletion, List<SkillSummary> skills) {}
    public record ProfileRequest(@NotBlank @Size(max=100) String fullName, @NotBlank @Size(max=100) String branch,
        @NotNull @Min(1) @Max(8) Integer academicYear, @Size(max=1200) String bio, @NotNull Enums.Availability availability,
        @Size(max=500) String interests, @Size(max=500) String avatarUrl, List<SkillInput> skills) {}
    public record ProjectSkillInput(@NotBlank @Size(max=80) String name, @NotNull Enums.SkillType skillType) {}
    public record ProjectRequest(@NotBlank @Size(max=140) String title, @NotBlank @Size(max=350) String shortDescription,
        @NotBlank @Size(max=5000) String description, @NotBlank @Size(max=100) String category,
        @NotNull @Min(2) @Max(20) Integer teamSize, @NotNull Enums.ProjectStatus status, @NotEmpty List<ProjectSkillInput> skills) {}
    public record ProjectSkillSummary(String name, Enums.SkillType skillType) {}
    public record TeamMemberSummary(Long id, Long studentId, String name, String email, Enums.TeamRole role, Instant joinedAt) {}
    public record ProjectSummary(Long id, String title, String shortDescription, String description, String category, Integer teamSize,
         Integer currentTeamSize, Integer remainingSlots, Enums.ProjectStatus status, Long ownerId, String ownerName,
         List<ProjectSkillSummary> skills, List<TeamMemberSummary> members, Instant createdAt, Instant updatedAt) {}
    public record CandidateSummary(Long studentId, String name, String branch, Integer academicYear, Integer matchPercentage,
        List<String> matchingSkills, List<String> missingSkills, String explanation) {}
    public record InvitationRequest(@NotNull Long studentId) {}
    public record TeamRequestSummary(Long id, Long projectId, String projectTitle, Long fromStudentId, String fromStudentName,
        Long toStudentId, String toStudentName, Enums.RequestStatus status, Instant createdAt) {}
    public record RoleUpdateRequest(@NotNull Enums.TeamRole role) {}
    public record MilestoneRequest(@NotBlank @Size(max=150) String title, @Size(max=2000) String description, LocalDate dueDate,
        @NotNull Enums.MilestoneStatus status, @NotNull @Min(0) @Max(100) Integer progressPercentage) {}
    public record MilestoneSummary(Long id, String title, String description, LocalDate dueDate, Enums.MilestoneStatus status,
        Integer progressPercentage, Instant updatedAt) {}
    public record ActivitySummary(Long id, String activityType, String message, String actorName, Instant createdAt) {}
    public record WorkspaceSummary(ProjectSummary project, Integer progressPercentage, List<MilestoneSummary> milestones,
        List<ActivitySummary> activities) {}
    public record DashboardSummary(StudentSummary profile, List<ProjectSummary> ownedProjects, List<ProjectSummary> teamProjects,
        List<TeamRequestSummary> invitations, List<ProjectSummary> openProjects) {}
    public record AdminStats(long totalStudents, long activeProjects, long openProjects, long teamsFormed, long pendingRequests,
        Map<String,Long> mostCommonSkills, Map<String,Long> projectsByCategory) {}
    public record ApiError(Instant timestamp, int status, String error, String message, String path, Map<String,String> fieldErrors) {}
}
