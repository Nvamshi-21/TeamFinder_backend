package edu.teamfinder.entity;

import jakarta.persistence.*;

@Entity
@Table(name="project_skill", uniqueConstraints=@UniqueConstraint(name="uk_project_skill", columnNames={"project_id","skill_id"}))
public class ProjectSkill {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="project_id") private Project project;
    @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="skill_id") private Skill skill;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Enums.SkillType skillType;
    public Long getId(){return id;} public Project getProject(){return project;} public void setProject(Project v){project=v;}
    public Skill getSkill(){return skill;} public void setSkill(Skill v){skill=v;} public Enums.SkillType getSkillType(){return skillType;} public void setSkillType(Enums.SkillType v){skillType=v;}
}
