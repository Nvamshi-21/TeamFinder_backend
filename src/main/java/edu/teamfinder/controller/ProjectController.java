package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.Enums.ProjectStatus;
import edu.teamfinder.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projects; public ProjectController(ProjectService projects){this.projects=projects;}
    @GetMapping public Page<ProjectSummary> list(@RequestParam(required=false) String q,@RequestParam(required=false) String category,@RequestParam(required=false) ProjectStatus status,@RequestParam(required=false) String skill,@RequestParam(required=false) String branch,@PageableDefault(size=12,sort="updatedAt",direction=Sort.Direction.DESC) Pageable page){return projects.search(q,category,status,skill,branch,page);}
    @PostMapping public ResponseEntity<ProjectSummary> create(Authentication auth,@Valid @RequestBody ProjectRequest body){return ResponseEntity.status(HttpStatus.CREATED).body(projects.create(auth.getName(),body));}
    @GetMapping("/{id}") public ProjectSummary get(@PathVariable Long id){return projects.get(id);}
    @PutMapping("/{id}") public ProjectSummary update(Authentication auth,@PathVariable Long id,@Valid @RequestBody ProjectRequest body){return projects.update(auth.getName(),id,body);}
    @PostMapping("/{id}/archive") public ProjectSummary archive(Authentication auth,@PathVariable Long id){return projects.archive(auth.getName(),id);}
}
