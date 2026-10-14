package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/projects/{projectId}")
public class WorkspaceController {
    private final WorkspaceService workspace; public WorkspaceController(WorkspaceService workspace){this.workspace=workspace;}
    @GetMapping("/workspace") public WorkspaceSummary get(Authentication auth,@PathVariable Long projectId){return workspace.workspace(auth.getName(),projectId);}
    @PostMapping("/milestones") public ResponseEntity<MilestoneSummary> create(Authentication auth,@PathVariable Long projectId,@Valid @RequestBody MilestoneRequest body){return ResponseEntity.status(HttpStatus.CREATED).body(workspace.create(auth.getName(),projectId,body));}
    @PutMapping("/milestones/{milestoneId}") public MilestoneSummary update(Authentication auth,@PathVariable Long projectId,@PathVariable Long milestoneId,@Valid @RequestBody MilestoneRequest body){return workspace.update(auth.getName(),projectId,milestoneId,body);}
    @DeleteMapping("/milestones/{milestoneId}") public ResponseEntity<Void> delete(Authentication auth,@PathVariable Long projectId,@PathVariable Long milestoneId){workspace.delete(auth.getName(),projectId,milestoneId);return ResponseEntity.noContent().build();}
}
