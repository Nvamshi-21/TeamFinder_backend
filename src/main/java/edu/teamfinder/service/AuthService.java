package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.mapper.ApiMapper;
import edu.teamfinder.repository.StudentRepository;
import edu.teamfinder.security.JwtService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final StudentRepository students; private final PasswordEncoder passwords; private final JwtService jwt;
    public AuthService(StudentRepository students,PasswordEncoder passwords,JwtService jwt){this.students=students;this.passwords=passwords;this.jwt=jwt;}
    @Transactional public AuthResponse register(RegisterRequest request) {
        if(students.existsByEmailIgnoreCase(request.email())) throw ApiException.conflict("An account with that email already exists.");
        if(request.role()==Role.ADMIN) throw ApiException.forbidden("Administrator accounts are created by an administrator, not public registration.");
        Student s=new Student(); s.setFullName(request.fullName().trim());s.setEmail(request.email());s.setPassword(passwords.encode(request.password()));s.setBranch(request.branch().trim());s.setAcademicYear(request.academicYear());s.setRole(Role.STUDENT);students.save(s);
        return response(s);
    }
    @Transactional public AuthResponse login(LoginRequest request) {
        Student s=students.findByEmailIgnoreCase(request.email()).orElseThrow(()->ApiException.badRequest("Email or password is incorrect."));
        if(!passwords.matches(request.password(),s.getPassword())) throw ApiException.badRequest("Email or password is incorrect.");
        return response(s);
    }
    private AuthResponse response(Student s){return new AuthResponse(jwt.create(s.getEmail(),s.getRole().name()),"Bearer",ApiMapper.student(s));}
}
