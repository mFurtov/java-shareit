package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({EntityNotFoundException.class, OwnerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(final RuntimeException e) {
        log.debug("Искомый объект не найден 404 Not found {}", e.getMessage());
        return new ErrorResponse("Искомый объект не найден");
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validCountException(final MethodArgumentNotValidException e) {
        log.debug("Ошибка валидации 400 Bad request {}", e.getMessage());
        return new ErrorResponse("Ошибка валидации");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailConflictException(final RuntimeException e) {
        log.debug("Конфликт запроса 409 Conflict {}", e.getMessage());
        return new ErrorResponse("Конфликт запроса");
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.debug("Произошла непредвиденная ошибка {}", e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
