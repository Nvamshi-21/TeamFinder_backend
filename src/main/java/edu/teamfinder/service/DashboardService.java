package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.ProjectStatus;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.ProjectRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final StudentService students; private final ProjectService projects; private final ProjectRepository projectRepo; private final TeamService teams;
    public DashboardService(StudentService students,ProjectService projects,ProjectRepository projectRepo,TeamService teams){this.students=students;this.projects=projects;this.projectRepo=projectRepo;this.teams=teams;}
    @Transactional(readOnly=true) public DashboardSummary student(String email){Student s=students.current(email);List<ProjectSummary> team=projectRepo.findByMembersStudentId(s.getId()).stream().filter(p->!p.getOwner().getId().equals(s.getId())).map(ApiMapper::project).toList();List<ProjectSummary> open=projectRepo.findTop6ByStatusOrderByUpdatedAtDesc(ProjectStatus.OPEN_FOR_MEMBERS).stream().map(ApiMapper::project).toList();return new DashboardSummary(ApiMapper.student(s),projects.byOwner(email),team,teams.invitations(email),open);}
}
