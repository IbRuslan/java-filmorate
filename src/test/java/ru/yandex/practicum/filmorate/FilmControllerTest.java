package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmControllerTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private final FilmController filmController = new FilmController();

    @Test
    void shouldThrowExceptionWhenNameEmpty() {

        Film film = new Film();
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(100);

        var violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionTooLong() {

        Film film = new Film();
        film.setName("Film");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(100);

        var violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateTooEarly() {

        Film film = new Film();
        film.setName("Film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1800,1,1));
        film.setDuration(100);

        assertThrows(ValidationException.class,
                () -> filmController.addMovie(film));
    }

    @Test
    void shouldThrowExceptionWhenDurationNegative() {

        Film film = new Film();
        film.setName("Film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(-5);

        var violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }
}


