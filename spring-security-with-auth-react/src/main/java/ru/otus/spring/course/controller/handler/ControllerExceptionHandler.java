package ru.otus.spring.course.controller.handler;

import com.mongodb.MongoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<String> handleMongoExceptions(MongoException mongoException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mongoException.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
    }
}
