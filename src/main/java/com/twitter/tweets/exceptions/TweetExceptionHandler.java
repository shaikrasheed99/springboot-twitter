package com.twitter.tweets.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class TweetExceptionHandler {
    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(value = {InvalidTweetRequestBodyException.class})
    public ResponseEntity<?> handleInvalidTweetRequestBodyException(InvalidTweetRequestBodyException invalidTweetRequestBodyException) throws JsonProcessingException {
        errorResponse.setError(invalidTweetRequestBodyException.getErrors());
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
    }

    @ExceptionHandler(value = {TweetNotFoundException.class})
    public ResponseEntity<?> handleTweetNotFoundException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseJson);
    }

    @ExceptionHandler(value = {AuthorMismatchException.class})
    public ResponseEntity<?> handleAuthorMismatchException(Exception exception) throws JsonProcessingException {
        errorResponse.setError(Collections.singletonMap("message", exception.getMessage()));
        String responseJson = errorResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseJson);
    }
}
