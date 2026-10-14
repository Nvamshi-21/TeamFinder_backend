package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final StudentRepository students;private final ProjectRepository projects;private final TeamMemberRepository teams;private final TeamRequestRepository requests;
    public AdminService(StudentRepository students,ProjectRepository projects,TeamMemberRepository teams,TeamRequestRepository requests){this.students=students;this.projects=projects;this.teams=teams;this.requests=requests;}
    @Transactional(readOnly=true) public AdminStats stats(){
        List<Student> all=students.findAll();Map<String,Long> common=all.stream().flatMap(s->s.getSkills().stream()).map(x->x.getSkill().getName()).collect(Collectors.groupingBy(Function.identity(),LinkedHashMap::new,Collectors.counting()));common=common.entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).limit(8).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(a,b)->a,LinkedHashMap::new));
        Map<String,Long> categories=projects.findAll().stream().collect(Collectors.groupingBy(Project::getCategory,LinkedHashMap::new,Collectors.counting()));return new AdminStats(all.stream().filter(s->s.getRole()==Role.STUDENT).count(),projects.countByStatus(ProjectStatus.IN_PROGRESS)+projects.countByStatus(ProjectStatus.OPEN_FOR_MEMBERS)+projects.countByStatus(ProjectStatus.TEAM_FULL),projects.countByStatus(ProjectStatus.OPEN_FOR_MEMBERS),teams.findAll().stream().map(TeamMember::getProject).map(Project::getId).distinct().count(),requests.countByStatus(RequestStatus.PENDING),common,categories);
    }
    @Transactional(readOnly=true) public Page<StudentSummary> users(String q,Pageable page){return students.findByRoleAndFullNameContainingIgnoreCase(Role.STUDENT,q==null?"":q,page).map(ApiMapper::student);}
    @Transactional(readOnly=true) public Page<ProjectSummary> projectList(String q,ProjectStatus status,Pageable page){return projects.search(q==null||q.isBlank()?null:q,null,status,null,null,page).map(ApiMapper::project);}
    @Transactional public ProjectSummary moderateProject(Long id,ProjectStatus status){Project p=projects.findDetailedById(id).orElseThrow(()->ApiException.notFound("Project"));p.setStatus(status);return ApiMapper.project(projects.save(p));}
    @Transactional public StudentSummary setAvailability(Long id,Availability availability){Student s=students.findById(id).orElseThrow(()->ApiException.notFound("Student"));s.setAvailability(availability);return ApiMapper.student(students.save(s));}
}
