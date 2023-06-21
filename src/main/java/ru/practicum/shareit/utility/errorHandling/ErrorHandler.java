package ru.practicum.shareit.utility.errorHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.utility.errorHandling.exceptions.*;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(ShareItEntityNotFound ex) {
        log.info("Level: ERROR HANDLER. Catched exception: " + ex.getClass() + ". Message: " + ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidEntityHandler(ShareItInvalidEntity ex) {
        log.info("Level: ERROR HANDLER. Catched exception: " + ex.getClass() + ". Message: " + ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse alreadyTakeValuHandler(ShareItValueAlreadyTaken ex) {
        log.info("Level: ERROR HANDLER. Catched exception: " + ex.getClass() + ". Message: " + ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse notAllowedHandler(ShareItNotAllowedAction ex) {
        log.info("Level: ERROR HANDLER. Catched exception: " + ex.getClass() + ". Message: " + ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse sqlExceptionHandeler(ShareItSQLException ex) {
        log.info("Level: ERROR HANDLER. Catched exception: " + ex.getClass() + ". Message: " + ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }
}
