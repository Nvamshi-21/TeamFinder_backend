package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.Enums.Availability;
import edu.teamfinder.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class StudentController {
    private final StudentService students; public StudentController(StudentService students){this.students=students;}
    @GetMapping("/students/me") public StudentSummary me(Authentication auth){return students.get(students.current(auth.getName()).getId());}
    @PutMapping("/students/me") public StudentSummary update(Authentication auth,@Valid @RequestBody ProfileRequest body){return students.update(auth.getName(),body);}
    @GetMapping("/students/{id}") public StudentSummary get(@PathVariable Long id){return students.get(id);}
    @GetMapping("/students") public Page<StudentSummary> search(@RequestParam(required=false) String skill,@RequestParam(required=false) String branch,@RequestParam(required=false) Integer year,@RequestParam(required=false) Availability availability,@PageableDefault(size=12,sort="fullName") Pageable page){return students.search(skill,branch,year,availability,page);}
    @GetMapping("/skills") public java.util.List<String> skills(){return students.skillNames();}
}
