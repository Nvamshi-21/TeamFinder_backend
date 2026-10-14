package edu.teamfinder.entity;

import jakarta.persistence.*;

@Entity
@Table(name="activity_log", indexes=@Index(name="idx_activity_project_created",columnList="project_id,created_at"))
public class ActivityLog extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="project_id") private Project project;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="student_id") private Student student;
    @Column(nullable=false,length=80) private String activityType;
    @Column(nullable=false,length=500) private String message;
    public Long getId(){return id;} public Project getProject(){return project;} public void setProject(Project v){project=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;} public String getActivityType(){return activityType;} public void setActivityType(String v){activityType=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
}
