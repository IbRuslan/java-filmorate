package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validateReleaseDate(film);

        Film addedFilm = filmStorage.create(film);

        log.info("Добавлен фильм: {}", addedFilm);

        return addedFilm;
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            log.warn("Попытка обновить фильм без id");
            throw new ValidationException("Id должен быть указан");
        }

        filmStorage.findById(film.getId());

        validateReleaseDate(film);

        Film updatedFilm = filmStorage.update(film);

        log.info("Обновлён фильм: {}", updatedFilm);

        return updatedFilm;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findById(filmId);
        userStorage.findById(userId);

        film.getLikes().add(filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        userStorage.findById(userId);

        Film film = findById(filmId);

        film.getLikes().remove(userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed().thenComparing(Film::getId))
                .limit(count)
                .toList();
    }

    private void validateReleaseDate(Film film) {

        LocalDate earliestDate = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(earliestDate)) {
            log.warn("Дата релиза {} раньше допустимой", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
