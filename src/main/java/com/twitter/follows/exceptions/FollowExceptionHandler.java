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

    @ExceptionHandler(value = {UserAlreadyFollowingException.class})
    public ResponseEntity<?> handleUserAlreadyFollowingException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }

    @ExceptionHandler(value = {UserNotFollowingException.class})
    public ResponseEntity<?> handleUserNotFollowingException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }

    @ExceptionHandler(value = {UserIdsAreSame.class})
    public ResponseEntity<?> handleUserIdsAreSame(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }
}
