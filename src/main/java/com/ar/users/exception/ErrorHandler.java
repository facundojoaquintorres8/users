package com.ar.users.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ar.users.dto.ErrorDTO;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder errorMessage = new StringBuilder();
        fieldErrors.forEach(f -> errorMessage.append(f.getDefaultMessage() + ". "));

        ErrorDTO response = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> exception(HttpServletRequest request, Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        ErrorDTO response = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> constraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        ErrorDTO response = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> dataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        ErrorDTO response = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDTO> customException(HttpServletRequest request, CustomException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        ErrorDTO response = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
