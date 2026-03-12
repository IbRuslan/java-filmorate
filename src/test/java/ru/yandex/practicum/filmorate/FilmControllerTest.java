package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void shouldThrowExceptionWhenNameEmpty() {
        Film film = new Film();
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.addMovie(film));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionTooLong() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.addMovie(film));
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateTooEarly() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1800,1,1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.addMovie(film));
    }

    @Test
    void shouldThrowExceptionWhenDurationNegative() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(-5);

        assertThrows(ValidationException.class, () -> filmController.addMovie(film));
    }
}
