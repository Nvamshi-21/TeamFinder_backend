package edu.teamfinder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_skill", uniqueConstraints = @UniqueConstraint(name="uk_student_skill", columnNames={"student_id","skill_id"}))
public class StudentSkill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="student_id") private Student student;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="skill_id") private Skill skill;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.Proficiency proficiency = Enums.Proficiency.INTERMEDIATE;
    public Long getId(){return id;} public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public Skill getSkill(){return skill;} public void setSkill(Skill v){skill=v;} public Enums.Proficiency getProficiency(){return proficiency;} public void setProficiency(Enums.Proficiency v){proficiency=v;}
}
