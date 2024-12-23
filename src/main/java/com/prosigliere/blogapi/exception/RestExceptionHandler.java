package com.prosigliere.blogapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosigliere.blogapi.model.Response;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String error = "Malformed JSON request";
        try {
            return buildResponseEntity(new Response<>(HttpStatus.BAD_REQUEST, error, ex));
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    private ResponseEntity<Object> buildResponseEntity(Response<Object> response) {
        return new ResponseEntity<>(response, response.getHeader().getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        try {
            return buildResponseEntity(new Response<>(HttpStatus.NOT_FOUND, "Entity not found", ex));
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }
}
