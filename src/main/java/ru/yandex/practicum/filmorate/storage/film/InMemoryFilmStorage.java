package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidator validator = new FilmValidator();
    private int generatorId;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findFilm(int id) {
        if (!films.containsKey(id)) {
            log.error("Фильм не найден.");
            throw new DataNotFoundException("Фильм не найден.");
        }
        return films.get(id);
    }

    public Film create(Film film) {
        validator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден.");
            throw new DataNotFoundException("Фильм не найден");
        }
        validator.validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        return null;
    }

    private int generateId() {
        return ++generatorId;
    }
}
