package com.ar.users.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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
import com.ar.users.dto.ErrorResponseDTO;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder errorMessage = new StringBuilder();
        fieldErrors.forEach(f -> errorMessage.append(f.getDefaultMessage() + ". "));

        List<ErrorDTO> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString()));
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> exception(HttpServletRequest request, Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        List<ErrorDTO> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage.toString()));
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> constraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        List<ErrorDTO> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString()));
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> dataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Message: ").append(e.getMessage()).append(" StackTrace: ").append(errors.toString());

        List<ErrorDTO> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), errorMessage.toString()));
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> customException(HttpServletRequest request, CustomException e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        List<ErrorDTO> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
