package edu.teamfinder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skill")
public class Skill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 80) private String name;
    public Skill() {} public Skill(String name) { this.name=name; }
    public Long getId() { return id; } public String getName() { return name; } public void setName(String name) { this.name=name; }
}
