package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film findFilm(int id);

    Collection<Film> findAll();

    Film create(Film film);

    void update(Film film);

    Collection<Film> getPopularFilms(Integer count);
}
