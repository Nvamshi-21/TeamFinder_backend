package edu.teamfinder.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.teamfinder.dto.ApiDtos.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwt) throws Exception {
        return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/actuator/health").permitAll().requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated())
            .exceptionHandling(e->e.authenticationEntryPoint((r,res,x)->write(res,401,"Authentication is required.",r.getRequestURI())).accessDeniedHandler((r,res,x)->write(res,403,"You do not have permission to perform this action.",r.getRequestURI())))
            .addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();
    }
    private static void write(HttpServletResponse response,int status,String message,String path) throws java.io.IOException { response.setStatus(status);response.setContentType("application/json");new ObjectMapper().writeValue(response.getOutputStream(),new ApiError(Instant.now(),status,status==401?"Unauthorized":"Forbidden",message,path,Map.of())); }
}
