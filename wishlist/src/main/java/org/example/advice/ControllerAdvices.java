package org.example.advice;

import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, DataIntegrityViolationException.class})
    @ResponseBody
    private ResponseEntity<String> handleBAD_REQUEST(RuntimeException ex) {
        String message;

        if (ex instanceof DataIntegrityViolationException && !ex.getMessage().contains("duplicate")) {
            throw new IllegalArgumentException(ex.getMessage());
        } else if (ex instanceof DataIntegrityViolationException) {
            message = "application contains this data";
        } else {
            message = ex.getMessage();
        }

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalStateException.class})
    private @ResponseBody ResponseEntity<String> handleINTERNAL_SERVER_ERROR(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    private @ResponseBody ResponseEntity<String> handleFORBIDDEN(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
