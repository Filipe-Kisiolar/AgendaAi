package kisiolar.filipe.Viviane.Ai.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFindException.class)
    public ResponseEntity<Map<String,String>> handleResouceNotFound(Exception exception){
        Map<String, String> body = new HashMap<>();
        body.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> handleBadRequestExcepition(Exception exception){
        Map<String, String> body = new HashMap<>();
        body.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(UsernameOrPasswordInvalidException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentialException(Exception exception){
        Map<String,String> body = new HashMap<>();
        body.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception exception) {
        Map<String, String> body = new HashMap<>();
        body.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(TooLargeException.class)
    public ResponseEntity<String> handleTooLarge(Exception ex) {
        return ResponseEntity.status(413).body("Arquivo maior que o permitido (máx. 5MB).");
    }
}


