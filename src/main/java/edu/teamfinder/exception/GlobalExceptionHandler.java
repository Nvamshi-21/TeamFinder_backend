package edu.teamfinder.exception;

import edu.teamfinder.dto.ApiDtos.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiError> handleApi(ApiException ex, HttpServletRequest req) { return response(ex.getStatus(),ex.getMessage(),req,Map.of()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex,HttpServletRequest req) {
        Map<String,String> fields=new LinkedHashMap<>(); ex.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(),e.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST,"Please correct the highlighted fields.",req,fields);
    }
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    ResponseEntity<ApiError> conflict(Exception ex,HttpServletRequest req) { return response(HttpStatus.CONFLICT,"That action conflicts with existing data.",req,Map.of()); }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiError> denied(AccessDeniedException ex,HttpServletRequest req) { return response(HttpStatus.FORBIDDEN,"You do not have permission to perform this action.",req,Map.of()); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception ex,HttpServletRequest req) { return response(HttpStatus.INTERNAL_SERVER_ERROR,"Something unexpected happened. Please try again.",req,Map.of()); }
    private ResponseEntity<ApiError> response(HttpStatus status,String message,HttpServletRequest req,Map<String,String> fields) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(),status.value(),status.getReasonPhrase(),message,req.getRequestURI(),fields));
    }
}
