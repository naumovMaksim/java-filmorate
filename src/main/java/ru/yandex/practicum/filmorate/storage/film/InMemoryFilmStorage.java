package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NullException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidator validator = new FilmValidator();
    private int generatorId;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findFilm(int id) {
        if (!films.containsKey(id)) {
            throw new NullException("Фильм не найден.");
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
            throw new NullException("Фильм не найден");
        }
        validator.validate(film);
        films.put(film.getId(), film);
        return film;
    }

    private int generateId() {
        return ++generatorId;
    }
}
