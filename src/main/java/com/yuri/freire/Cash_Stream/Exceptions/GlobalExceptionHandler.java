package com.yuri.freire.Cash_Stream.Exceptions;

import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.ResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(HttpServletRequest request, Exception exception){
        List<String> errors = Arrays.asList(exception.getMessage());
        ApiResponse<Void> response = ResponseUtil.error(errors, "An error ocurred", 1000, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException entityNotFoundException){
        ApiResponse<Void> response = ResponseUtil.error(entityNotFoundException.getMessage(), "Resource not found", 404, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException dataIntegrityViolationException){
        ApiResponse<Void> response = ResponseUtil.error(dataIntegrityViolationException.getMessage(), "Data already exists. Unique constraint exception", 409, request.getRequestURI());
        System.out.println("Root cause: " + dataIntegrityViolationException.getRootCause());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException methodArgumentNotValidException){
        List<String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ApiResponse<Void> response  = ResponseUtil.error(errors, "Argument not valid exception. Check form fields", 400, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
