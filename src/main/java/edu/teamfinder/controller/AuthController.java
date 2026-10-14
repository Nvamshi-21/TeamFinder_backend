package edu.teamfinder.controller;

import edu.teamfinder.dto.ApiDtos.*;
import edu.teamfinder.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth; public AuthController(AuthService auth){this.auth=auth;}
    @PostMapping("/register") public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(request));}
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest request){return auth.login(request);}
    @PostMapping("/logout") public ResponseEntity<Void> logout(){return ResponseEntity.noContent().build();}
}
