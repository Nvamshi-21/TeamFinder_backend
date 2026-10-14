package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.DashboardSummary;
import edu.teamfinder.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboard;public DashboardController(DashboardService dashboard){this.dashboard=dashboard;}
    @GetMapping public DashboardSummary get(Authentication auth){return dashboard.student(auth.getName());}
}
