package edu.teamfinder.entity;

/** Domain enums are nested to keep their vocabulary central and API-safe. */
public final class Enums {
    private Enums() { }
    public enum Role { STUDENT, ADMIN }
    public enum Availability { AVAILABLE, LIMITED, UNAVAILABLE }
    public enum Proficiency { BEGINNER, INTERMEDIATE, ADVANCED }
    public enum ProjectStatus { DRAFT, OPEN_FOR_MEMBERS, TEAM_FULL, IN_PROGRESS, COMPLETED, ARCHIVED }
    public enum SkillType { REQUIRED, PREFERRED }
    public enum RequestStatus { PENDING, ACCEPTED, REJECTED, WITHDRAWN }
    public enum TeamRole { TEAM_LEAD, BACKEND_DEVELOPER, FRONTEND_DEVELOPER, UI_UX_DESIGNER, DATABASE_DEVELOPER, TESTER, MEMBER }
    public enum MilestoneStatus { NOT_STARTED, IN_PROGRESS, COMPLETED, BLOCKED }
}
