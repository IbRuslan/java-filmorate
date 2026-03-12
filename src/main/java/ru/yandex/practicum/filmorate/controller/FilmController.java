package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getMovies() {
        return films.values();
    }

    @PostMapping
    public Film addMovie(@Valid @RequestBody Film film) {

        validateReleaseDate(film);

        film.setId(getNextId());

        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);

        return film;
    }

    @PutMapping
    public Film updateMovie(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("Попытка обновить фильм без id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        validateReleaseDate(film);

        films.put(film.getId(), film);
        log.info("Обновлён фильм: {}", film);

        return film;
    }


    private void validateReleaseDate(Film film) {

        LocalDate earliestDate = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(earliestDate)) {
            log.warn("Дата релиза {} раньше допустимой", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
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
