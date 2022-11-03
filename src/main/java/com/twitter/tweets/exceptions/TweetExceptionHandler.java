package com.twitter.tweets.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
