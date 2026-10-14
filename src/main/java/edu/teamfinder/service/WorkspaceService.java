package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
    private final ProjectService projects; private final TeamService teams; private final StudentService students; private final MilestoneRepository milestones; private final ActivityLogRepository activity;
    public WorkspaceService(ProjectService projects,TeamService teams,StudentService students,MilestoneRepository milestones,ActivityLogRepository activity){this.projects=projects;this.teams=teams;this.students=students;this.milestones=milestones;this.activity=activity;}
    @Transactional(readOnly=true) public WorkspaceSummary workspace(String email,Long projectId){Project p=projects.require(projectId);assertMember(p,email);return summary(p);}
    @Transactional public MilestoneSummary create(String email,Long projectId,MilestoneRequest request){Project p=projects.require(projectId);assertMember(p,email);Milestone m=new Milestone();m.setProject(p);apply(m,request);m=milestones.save(m);log(p,students.current(email),"MILESTONE_CREATED","Milestone ‘"+m.getTitle()+"’ was created.");return ApiMapper.milestone(m);}
    @Transactional public MilestoneSummary update(String email,Long projectId,Long milestoneId,MilestoneRequest request){Project p=projects.require(projectId);assertMember(p,email);Milestone m=milestones.findById(milestoneId).orElseThrow(()->ApiException.notFound("Milestone"));if(!m.getProject().getId().equals(projectId))throw ApiException.badRequest("This milestone does not belong to the project.");apply(m,request);m=milestones.save(m);log(p,students.current(email),"MILESTONE_UPDATED","Milestone ‘"+m.getTitle()+"’ was updated.");return ApiMapper.milestone(m);}
    @Transactional public void delete(String email,Long projectId,Long milestoneId){Project p=projects.require(projectId);assertMember(p,email);Milestone m=milestones.findById(milestoneId).orElseThrow(()->ApiException.notFound("Milestone"));if(!m.getProject().getId().equals(projectId))throw ApiException.badRequest("This milestone does not belong to the project.");milestones.delete(m);log(p,students.current(email),"MILESTONE_REMOVED","A milestone was removed.");}
    private WorkspaceSummary summary(Project p){List<MilestoneSummary> list=milestones.findByProjectIdOrderByDueDateAsc(p.getId()).stream().map(ApiMapper::milestone).toList();int progress=list.isEmpty()?0:(int)Math.round(list.stream().mapToInt(MilestoneSummary::progressPercentage).average().orElse(0));List<ActivitySummary> activities=activity.findByProjectIdOrderByCreatedAtDesc(p.getId(),PageRequest.of(0,12)).stream().map(ApiMapper::activity).toList();return new WorkspaceSummary(ApiMapper.project(p),progress,list,activities);}
    private void apply(Milestone m,MilestoneRequest r){m.setTitle(r.title().trim());m.setDescription(r.description()==null?null:r.description().trim());m.setDueDate(r.dueDate());m.setStatus(r.status());m.setProgressPercentage(r.progressPercentage());}
    private void assertMember(Project p,String email){if(!teams.isMember(p,email))throw ApiException.forbidden("Only project team members can access this workspace.");}
    private void log(Project p,Student s,String type,String message){ActivityLog a=new ActivityLog();a.setProject(p);a.setStudent(s);a.setActivityType(type);a.setMessage(message);activity.save(a);}
}
