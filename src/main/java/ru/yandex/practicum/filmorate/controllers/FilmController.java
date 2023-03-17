package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmValidator validator = new FilmValidator();
    private int generatorId;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен /GET запрос на получение списка всех фильмов.");
        log.debug("Ответ отправлен: {}", films.values());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен /POST запрос на добавление нового фильма : {}", film);
        validator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен: {}", films.containsValue(film));
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        log.debug("Получен /PUT запрос на изменение данных фильма: {}", film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм не найден");
        }
        validator.validate(film);
        films.put(film.getId(), film);
        log.debug("Данные фильма изменены: {}", films.containsValue(film));
        return film;
    }

    private int generateId() {
        return ++generatorId;
    }
}
