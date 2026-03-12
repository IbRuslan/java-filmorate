package ru.yandex.practicum.filmorate.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();


        log.warn("Ошибка валидации: {}", errorMessage);

        return errorMessage;
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException e) {

        log.warn("Ошибка валидации: {}", e.getMessage());

        return e.getMessage();
    }
}
