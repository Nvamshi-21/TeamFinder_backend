package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository students; private final SkillRepository skills;
    public StudentService(StudentRepository students,SkillRepository skills){this.students=students;this.skills=skills;}
    @Transactional public Student current(String email){return students.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Your account"));}
    @Transactional(readOnly=true) public StudentSummary get(Long id){return ApiMapper.student(require(id));}
    @Transactional(readOnly=true) public Page<StudentSummary> search(String skill,String branch,Integer year,Availability availability,Pageable pageable){return students.search(Role.STUDENT,blankToNull(skill),blankToNull(branch),year,availability,pageable).map(ApiMapper::student);}
    @Transactional public StudentSummary update(String email,ProfileRequest request){
        Student s=current(email);s.setFullName(request.fullName().trim());s.setBranch(request.branch().trim());s.setAcademicYear(request.academicYear());s.setBio(trimNullable(request.bio()));s.setAvailability(request.availability());s.setInterests(request.interests());s.setAvatarUrl(trimNullable(request.avatarUrl()));
        s.getSkills().clear(); if(request.skills()!=null) for(SkillInput input: request.skills()) { String name=input.name().trim(); if(name.isEmpty()) continue; Skill skill=skills.findByNameIgnoreCase(name).orElseGet(()->skills.save(new Skill(name))); StudentSkill ss=new StudentSkill();ss.setStudent(s);ss.setSkill(skill);ss.setProficiency(input.proficiency());s.getSkills().add(ss); }
        return ApiMapper.student(students.save(s));
    }
    @Transactional(readOnly=true) public List<String> skillNames(){return skills.findTop10ByOrderByNameAsc().stream().map(Skill::getName).toList();}
    @Transactional(readOnly=true) public Student require(Long id){return students.findById(id).orElseThrow(()->ApiException.notFound("Student"));}
    @Transactional(readOnly=true) public Page<StudentSummary> admins(String q,Pageable page){return students.findByRoleAndFullNameContainingIgnoreCase(Role.STUDENT,q==null?"":q,page).map(ApiMapper::student);}
    private static String blankToNull(String s){return s==null||s.isBlank()?null:s.trim();} private static String trimNullable(String s){return s==null?null:s.trim();}
}
