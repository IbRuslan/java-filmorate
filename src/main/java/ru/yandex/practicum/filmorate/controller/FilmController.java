package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getMovies() {
        return films.values();
    }

    @PostMapping
    public Film addMovie(@RequestBody Film film) {
        validateFilm(film);

        film.setId(getNextId());

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateMovie(@RequestBody Film film) {
        if(film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }

        validateFilm(film);

        films.put(film.getId(), film);
        return film;
    }


    private void validateFilm(Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }

        if(film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        if(film.getReleaseDate() == null || film.getReleaseDate().isBefore(earliestDate)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if(film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }


    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);

        return ++currentMaxId;
    }
}
