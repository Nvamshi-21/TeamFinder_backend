package edu.teamfinder.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="team_member", uniqueConstraints=@UniqueConstraint(name="uk_team_member",columnNames={"project_id","student_id"}))
public class TeamMember {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="project_id") private Project project;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id") private Student student;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.TeamRole role = Enums.TeamRole.MEMBER;
    @Column(nullable=false,updatable=false) private Instant joinedAt;
    @PrePersist void joined(){joinedAt=Instant.now();}
    public Long getId(){return id;} public Project getProject(){return project;} public void setProject(Project v){project=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;} public Enums.TeamRole getRole(){return role;} public void setRole(Enums.TeamRole v){role=v;} public Instant getJoinedAt(){return joinedAt;}
}
