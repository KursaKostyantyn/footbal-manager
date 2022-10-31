package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.dto.CustomErrorDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorDTO> loginExistError(ConstraintViolationException e) {
        return new ResponseEntity<>(new CustomErrorDTO("Login already exists"), HttpStatus.BAD_REQUEST);
    }

}
