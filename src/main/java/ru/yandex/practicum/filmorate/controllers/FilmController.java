package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int generatorId;
    private final Map<Integer, Film> films = new HashMap<>();
    FilmValidator validator = new FilmValidator();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validator.filmValidator(film);
        film.setId(generateId());
        log.debug("Добавление нового фильма: {}.", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFound("Фильм не найден");
        }
        validator.filmValidator(film);
        log.debug("Изменение данных фильма с {}, на {}.", films.get(film.getId()), film);
        films.put(film.getId(), film);
        return film;
    }

    private int generateId() {
        return ++generatorId;
    }
}
