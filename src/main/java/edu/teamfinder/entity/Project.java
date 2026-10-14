package edu.teamfinder.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "project", indexes = {@Index(name="idx_project_status", columnList="status"), @Index(name="idx_project_category", columnList="category"), @Index(name="idx_project_owner", columnList="owner_id")})
public class Project extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=140) private String title;
    @Column(nullable=false, length=350) private String shortDescription;
    @Column(nullable=false, length=5000) private String description;
    @Column(nullable=false, length=100) private String category;
    @Column(nullable=false) private Integer teamSize;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.ProjectStatus status = Enums.ProjectStatus.DRAFT;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="owner_id") private Student owner;
    @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true) private Set<ProjectSkill> skills = new LinkedHashSet<>();
    @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true) private Set<TeamMember> members = new LinkedHashSet<>();
    public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getShortDescription(){return shortDescription;} public void setShortDescription(String v){shortDescription=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public Integer getTeamSize(){return teamSize;} public void setTeamSize(Integer v){teamSize=v;}
    public Enums.ProjectStatus getStatus(){return status;} public void setStatus(Enums.ProjectStatus v){status=v;}
    public Student getOwner(){return owner;} public void setOwner(Student v){owner=v;}
    public Set<ProjectSkill> getSkills(){return skills;} public Set<TeamMember> getMembers(){return members;}
}
