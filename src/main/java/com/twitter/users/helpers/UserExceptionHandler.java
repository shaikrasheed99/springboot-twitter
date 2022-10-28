package com.twitter.users.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.users.exceptions.UserAlreadyExistException;
import com.twitter.users.exceptions.UserNameNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class UserExceptionHandler {
    @Autowired
    private UserErrorResponse errorResponse;

    @ExceptionHandler(value = {UserNameNullException.class})
    public ResponseEntity<?> handleUserNameNullException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<?> handleUserAlreadyExistException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }
}
