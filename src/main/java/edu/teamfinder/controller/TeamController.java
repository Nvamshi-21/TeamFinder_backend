package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.service.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class TeamController {
    private final MatchingService matching;private final TeamService teams;
    public TeamController(MatchingService matching,TeamService teams){this.matching=matching;this.teams=teams;}
    @GetMapping("/projects/{projectId}/candidates") public List<CandidateSummary> candidates(Authentication auth,@PathVariable Long projectId){return matching.candidates(auth.getName(),projectId);}
    @GetMapping("/projects/{projectId}/invitations") public List<TeamRequestSummary> projectInvitations(Authentication auth,@PathVariable Long projectId){return teams.projectInvitations(auth.getName(),projectId);}
    @PostMapping("/projects/{projectId}/invitations") public ResponseEntity<TeamRequestSummary> invite(Authentication auth,@PathVariable Long projectId,@Valid @RequestBody InvitationRequest body){return ResponseEntity.status(HttpStatus.CREATED).body(teams.invite(auth.getName(),projectId,body));}
    @GetMapping("/invitations") public List<TeamRequestSummary> invitations(Authentication auth){return teams.invitations(auth.getName());}
    @PostMapping("/invitations/{requestId}/accept") public TeamRequestSummary accept(Authentication auth,@PathVariable Long requestId){return teams.respond(auth.getName(),requestId,true);}
    @PostMapping("/invitations/{requestId}/reject") public TeamRequestSummary reject(Authentication auth,@PathVariable Long requestId){return teams.respond(auth.getName(),requestId,false);}
    @PostMapping("/invitations/{requestId}/withdraw") public TeamRequestSummary withdraw(Authentication auth,@PathVariable Long requestId){return teams.withdraw(auth.getName(),requestId);}
    @PatchMapping("/projects/{projectId}/members/{memberId}/role") public TeamMemberSummary role(Authentication auth,@PathVariable Long projectId,@PathVariable Long memberId,@Valid @RequestBody RoleUpdateRequest body){return teams.updateRole(auth.getName(),projectId,memberId,body);}
}
