package edu.teamfinder.entity;

import jakarta.persistence.*;

@Entity
@Table(name="request", uniqueConstraints=@UniqueConstraint(name="uk_project_request_recipient",columnNames={"project_id","to_student_id"}), indexes=@Index(name="idx_request_recipient_status",columnList="to_student_id,status"))
public class TeamRequest extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="project_id") private Project project;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="from_student_id") private Student fromStudent;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="to_student_id") private Student toStudent;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.RequestStatus status = Enums.RequestStatus.PENDING;
    public Long getId(){return id;} public Project getProject(){return project;} public void setProject(Project v){project=v;}
    public Student getFromStudent(){return fromStudent;} public void setFromStudent(Student v){fromStudent=v;}
    public Student getToStudent(){return toStudent;} public void setToStudent(Student v){toStudent=v;}
    public Enums.RequestStatus getStatus(){return status;} public void setStatus(Enums.RequestStatus v){status=v;}
}
