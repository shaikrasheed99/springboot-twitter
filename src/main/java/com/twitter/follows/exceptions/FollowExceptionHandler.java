package com.twitter.follows.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class FollowExceptionHandler {
    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(value = {
            UserAlreadyFollowingException.class,
            UserNotFollowingException.class,
            UserIdsAreSame.class
    })
    public ResponseEntity<?> handleMultipleExceptions(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }
}
