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
public class ProjectService {
    private final ProjectRepository projects; private final SkillRepository skills; private final TeamMemberRepository teamMembers; private final ActivityLogRepository activity; private final StudentRepository students;
    public ProjectService(ProjectRepository projects,SkillRepository skills,TeamMemberRepository teamMembers,ActivityLogRepository activity,StudentRepository students){this.projects=projects;this.skills=skills;this.teamMembers=teamMembers;this.activity=activity;this.students=students;}
    @Transactional(readOnly=true) public Page<ProjectSummary> search(String q,String category,ProjectStatus status,String skill,String branch,Pageable page){return projects.search(blank(q),blank(category),status,blank(skill),blank(branch),page).map(ApiMapper::project);}
    @Transactional(readOnly=true) public ProjectSummary get(Long id){return ApiMapper.project(require(id));}
    @Transactional public ProjectSummary create(String email,ProjectRequest request){
        Student owner=owner(email); Project p=new Project();p.setOwner(owner);apply(p,request);p=projects.save(p);
        TeamMember lead=new TeamMember(); lead.setProject(p);lead.setStudent(owner);lead.setRole(TeamRole.TEAM_LEAD);teamMembers.save(lead);p.getMembers().add(lead);log(p,owner,"PROJECT_CREATED",owner.getFullName()+" created the project.");return ApiMapper.project(p);
    }
    @Transactional public ProjectSummary update(String email,Long id,ProjectRequest request){Project p=require(id);assertOwner(p,email);if(request.teamSize()<teamMembers.countByProjectId(id))throw ApiException.badRequest("Team size cannot be smaller than the current membership.");apply(p,request);return ApiMapper.project(projects.save(p));}
    @Transactional public ProjectSummary archive(String email,Long id){Project p=require(id);assertOwner(p,email);p.setStatus(ProjectStatus.ARCHIVED);log(p,p.getOwner(),"PROJECT_ARCHIVED","Project archived by its owner.");return ApiMapper.project(projects.save(p));}
    @Transactional(readOnly=true) public Project require(Long id){return projects.findDetailedById(id).orElseThrow(()->ApiException.notFound("Project"));}
    @Transactional(readOnly=true) public List<ProjectSummary> byOwner(String email){Student s=owner(email);return projects.findAll().stream().filter(p->p.getOwner().getId().equals(s.getId())).map(ApiMapper::project).toList();}
    private void apply(Project p,ProjectRequest r){p.setTitle(r.title().trim());p.setShortDescription(r.shortDescription().trim());p.setDescription(r.description().trim());p.setCategory(r.category().trim());p.setTeamSize(r.teamSize());p.setStatus(r.status());p.getSkills().clear();for(ProjectSkillInput input:r.skills()){Skill skill=skills.findByNameIgnoreCase(input.name().trim()).orElseGet(()->skills.save(new Skill(input.name().trim())));ProjectSkill ps=new ProjectSkill();ps.setProject(p);ps.setSkill(skill);ps.setSkillType(input.skillType());p.getSkills().add(ps);}}
    private Student owner(String email){return students.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Your account"));}
    private void assertOwner(Project p,String email){if(!p.getOwner().getEmail().equalsIgnoreCase(email))throw ApiException.forbidden("Only the project owner can make this change.");}
    private void log(Project p,Student actor,String type,String message){ActivityLog a=new ActivityLog();a.setProject(p);a.setStudent(actor);a.setActivityType(type);a.setMessage(message);activity.save(a);}
    private static String blank(String s){return s==null||s.isBlank()?null:s.trim();}
}
