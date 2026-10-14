package edu.teamfinder.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "student", indexes = {@Index(name = "idx_student_branch_year", columnList = "branch,academic_year"), @Index(name = "idx_student_availability", columnList = "availability")})
public class Student extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String fullName;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false, length = 100) private String branch;
    @Column(nullable = false) private Integer academicYear;
    @Column(length = 1200) private String bio;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.Availability availability = Enums.Availability.AVAILABLE;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.Role role = Enums.Role.STUDENT;
    @Column(length = 500) private String interests = "";
    @Column(length = 500) private String avatarUrl;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true) private Set<StudentSkill> skills = new LinkedHashSet<>();
    public Long getId() { return id; } public String getFullName() { return fullName; } public void setFullName(String v) { fullName=v; }
    public String getEmail() { return email; } public void setEmail(String v) { email=v.toLowerCase(Locale.ROOT).trim(); }
    public String getPassword() { return password; } public void setPassword(String v) { password=v; }
    public String getBranch() { return branch; } public void setBranch(String v) { branch=v; }
    public Integer getAcademicYear() { return academicYear; } public void setAcademicYear(Integer v) { academicYear=v; }
    public String getBio() { return bio; } public void setBio(String v) { bio=v; }
    public Enums.Availability getAvailability() { return availability; } public void setAvailability(Enums.Availability v) { availability=v; }
    public Enums.Role getRole() { return role; } public void setRole(Enums.Role v) { role=v; }
    public String getInterests() { return interests; } public void setInterests(String v) { interests=v == null ? "" : v; }
    public String getAvatarUrl() { return avatarUrl; } public void setAvatarUrl(String v) { avatarUrl=v; }
    public Set<StudentSkill> getSkills() { return skills; }
}
