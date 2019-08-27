package ru.vtb.insurance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.vtb.insurance.exceptions.ClinicNotFoundException;

@ControllerAdvice
public class ClinicNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ClinicNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String clinicNotFoundHandler(ClinicNotFoundException e) {
        return e.getMessage();
    }
}
