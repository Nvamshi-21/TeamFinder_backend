package edu.teamfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="milestone", indexes=@Index(name="idx_milestone_project_due",columnList="project_id,due_date"))
public class Milestone extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="project_id") private Project project;
    @Column(nullable=false,length=150) private String title;
    @Column(length=2000) private String description;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.MilestoneStatus status = Enums.MilestoneStatus.NOT_STARTED;
    @Column(nullable=false) private Integer progressPercentage = 0;
    public Long getId(){return id;} public Project getProject(){return project;} public void setProject(Project v){project=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;} public Enums.MilestoneStatus getStatus(){return status;} public void setStatus(Enums.MilestoneStatus v){status=v;}
    public Integer getProgressPercentage(){return progressPercentage;} public void setProgressPercentage(Integer v){progressPercentage=v;}
}
