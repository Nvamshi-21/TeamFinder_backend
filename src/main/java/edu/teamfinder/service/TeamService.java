package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final ProjectService projects; private final StudentService students; private final TeamRequestRepository requests; private final TeamMemberRepository members; private final ProjectRepository projectRepo; private final ActivityLogRepository activity;
    public TeamService(ProjectService projects,StudentService students,TeamRequestRepository requests,TeamMemberRepository members,ProjectRepository projectRepo,ActivityLogRepository activity){this.projects=projects;this.students=students;this.requests=requests;this.members=members;this.projectRepo=projectRepo;this.activity=activity;}
    @Transactional public TeamRequestSummary invite(String email,Long projectId,InvitationRequest input){
        Project p=projects.require(projectId); assertOwner(p,email); assertRecruiting(p); Student candidate=students.require(input.studentId());
        if(candidate.getAvailability()==Availability.UNAVAILABLE) throw ApiException.badRequest("This student is not currently available."); if(members.existsByProjectIdAndStudentId(projectId,candidate.getId())) throw ApiException.conflict("This student is already on the team.");
        TeamRequest request=requests.findByProjectIdAndToStudentId(projectId,candidate.getId()).orElseGet(TeamRequest::new); if(request.getId()!=null && request.getStatus()==RequestStatus.PENDING) throw ApiException.conflict("A pending invitation already exists for this student.");
        request.setProject(p);request.setFromStudent(p.getOwner());request.setToStudent(candidate);request.setStatus(RequestStatus.PENDING);request=requests.save(request); log(p,p.getOwner(),"INVITATION_SENT","Invitation sent to "+candidate.getFullName()+"."); return ApiMapper.request(request);
    }
    @Transactional public TeamRequestSummary withdraw(String email,Long requestId){TeamRequest r=require(requestId);assertOwner(r.getProject(),email);if(r.getStatus()!=RequestStatus.PENDING)throw ApiException.badRequest("Only pending invitations can be withdrawn.");r.setStatus(RequestStatus.WITHDRAWN);log(r.getProject(),r.getFromStudent(),"INVITATION_WITHDRAWN","Invitation to "+r.getToStudent().getFullName()+" was withdrawn.");return ApiMapper.request(requests.save(r));}
    @Transactional public TeamRequestSummary respond(String email,Long requestId,boolean accept){TeamRequest r=require(requestId);if(!r.getToStudent().getEmail().equalsIgnoreCase(email))throw ApiException.forbidden("Only the invited student can respond.");if(r.getStatus()!=RequestStatus.PENDING)throw ApiException.badRequest("This invitation has already been resolved."); if(!accept){r.setStatus(RequestStatus.REJECTED);return ApiMapper.request(requests.save(r));}
        Project p=r.getProject();assertRecruiting(p); if(members.countByProjectId(p.getId())>=p.getTeamSize())throw ApiException.conflict("The team is already full."); if(members.existsByProjectIdAndStudentId(p.getId(),r.getToStudent().getId()))throw ApiException.conflict("You are already a project member.");
        TeamMember m=new TeamMember();m.setProject(p);m.setStudent(r.getToStudent());m.setRole(TeamRole.MEMBER);members.save(m);p.getMembers().add(m);r.setStatus(RequestStatus.ACCEPTED); if(members.countByProjectId(p.getId())>=p.getTeamSize())p.setStatus(ProjectStatus.TEAM_FULL);projectRepo.save(p);log(p,r.getToStudent(),"MEMBER_JOINED",r.getToStudent().getFullName()+" joined the team.");return ApiMapper.request(requests.save(r));
    }
    @Transactional public TeamMemberSummary updateRole(String email,Long projectId,Long memberId,RoleUpdateRequest body){Project p=projects.require(projectId);assertOwner(p,email);TeamMember m=members.findById(memberId).orElseThrow(()->ApiException.notFound("Team member"));if(!m.getProject().getId().equals(projectId))throw ApiException.badRequest("This member does not belong to this project.");m.setRole(body.role());log(p,p.getOwner(),"ROLE_UPDATED",m.getStudent().getFullName()+" is now "+body.role().name().replace('_',' ').toLowerCase()+".");return ApiMapper.member(members.save(m));}
    @Transactional(readOnly=true) public List<TeamRequestSummary> invitations(String email){Student s=students.current(email);return requests.findByToStudentIdOrderByCreatedAtDesc(s.getId()).stream().map(ApiMapper::request).toList();}
    @Transactional(readOnly=true) public List<TeamRequestSummary> projectInvitations(String email,Long projectId){Project p=projects.require(projectId);assertOwner(p,email);return requests.findByProjectIdAndStatus(projectId,RequestStatus.PENDING).stream().map(ApiMapper::request).toList();}
    public boolean isMember(Project p,String email){return p.getOwner().getEmail().equalsIgnoreCase(email)||members.findByProjectId(p.getId()).stream().anyMatch(x->x.getStudent().getEmail().equalsIgnoreCase(email));}
    private TeamRequest require(Long id){return requests.findById(id).orElseThrow(()->ApiException.notFound("Invitation"));}
    private void assertOwner(Project p,String email){if(!p.getOwner().getEmail().equalsIgnoreCase(email))throw ApiException.forbidden("Only the project owner can manage its team.");}
    private void assertRecruiting(Project p){if(p.getStatus()!=ProjectStatus.OPEN_FOR_MEMBERS)throw ApiException.badRequest("This project is not open for new members."); if(members.countByProjectId(p.getId())>=p.getTeamSize())throw ApiException.conflict("The project team is already full.");}
    private void log(Project p,Student actor,String type,String message){ActivityLog a=new ActivityLog();a.setProject(p);a.setStudent(actor);a.setActivityType(type);a.setMessage(message);activity.save(a);}
}
