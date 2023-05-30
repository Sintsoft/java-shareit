package ru.practicum.shareit.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.utility.exception.EntityCollisionExcption;
import ru.practicum.shareit.utility.exception.NotFoundException;

import javax.validation.ValidationException;
import javax.validation.executable.ValidateOnExecution;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityConflictHandler(final EntityCollisionExcption e) {
        String errorMessage = String.format("Got entity collision - %s", e.getMessage());
        log.info(errorMessage);
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(final ValidationException e) {
        String errorMessage = String.format("Validation exception - %s", e.getMessage());
        log.info(errorMessage);
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(final NotFoundException e) {
        String errorMessage = String.format("Entity not found - %s", e.getMessage());
        log.info(errorMessage);
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse nullExceptionHandler(final NullPointerException e) {
        String errorMessage = String.format("Null field found - %s", e.getMessage());
        log.info(errorMessage);
        return new ErrorResponse(errorMessage);
    }
}
