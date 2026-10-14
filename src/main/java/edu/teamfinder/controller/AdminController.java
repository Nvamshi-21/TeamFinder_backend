package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.service.AdminService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService admin;public AdminController(AdminService admin){this.admin=admin;}
    @GetMapping("/stats") public AdminStats stats(){return admin.stats();}
    @GetMapping("/users") public Page<StudentSummary> users(@RequestParam(required=false) String q,@PageableDefault(size=15,sort="fullName") Pageable page){return admin.users(q,page);}
    @PatchMapping("/users/{id}/availability") public StudentSummary availability(@PathVariable Long id,@RequestParam Availability availability){return admin.setAvailability(id,availability);}
    @GetMapping("/projects") public Page<ProjectSummary> projects(@RequestParam(required=false) String q,@RequestParam(required=false) ProjectStatus status,@PageableDefault(size=15,sort="updatedAt",direction=Sort.Direction.DESC) Pageable page){return admin.projectList(q,status,page);}
    @PatchMapping("/projects/{id}/status") public ProjectSummary moderate(@PathVariable Long id,@RequestParam ProjectStatus status){return admin.moderateProject(id,status);}
}
