package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private int id = 0;
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым.");
            throw new InvalidNameException("Название не может быть пустым.");
        }
        if (films.containsKey(film.getName())) {
            log.error("Такой фильм уже добавлен");
            throw new FilmAlreadyExistException("Такой фильм уже добавлен");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Максимальная длина описания — 200 символов.");
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза — не раньше 28.12.1895.");
            throw new InvalidReleaseDateException("Дата релиза — не раньше 28.12.1895.");
        }
        if (film.getDuration() < 1) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new InvalidDurationException("Продолжительность фильма должна быть положительной.");
        }
        film.setId(id++);
        log.debug("Добавление нового фильма: {}.", film);
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым.");
            throw new InvalidNameException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Максимальная длина описания — 200 символов.");
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза — не раньше 28.12.1895.");
            throw new InvalidReleaseDateException("Дата релиза — не раньше 28.12.1895.");
        }
        if (film.getDuration() < 1) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new InvalidDurationException("Продолжительность фильма должна быть положительной.");
        }
        if (films.containsKey(film.getName())) {
            log.debug("Изменение данных фильма с {}, на {}.", films.get(film.getName()), film);
            films.put(film.getName(), film);
        } else {
            film.setId(id++);
            log.debug("Добавление нового фильма: {}.", film);
            films.put(film.getName(), film);
        }
        return film;
    }
}
