package edu.teamfinder.security;

import edu.teamfinder.repository.StudentRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt; private final StudentRepository students;
    public JwtAuthenticationFilter(JwtService jwt, StudentRepository students) { this.jwt=jwt;this.students=students; }
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
        String auth=request.getHeader("Authorization");
        if(auth!=null && auth.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null) {
            jwt.subject(auth.substring(7)).flatMap(students::findByEmailIgnoreCase).ifPresent(student -> {
                var token=new UsernamePasswordAuthenticationToken(student.getEmail(),null,java.util.List.of(new SimpleGrantedAuthority("ROLE_"+student.getRole().name())));
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(token);
            });
        }
        chain.doFilter(request,response);
    }
}
