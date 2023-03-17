package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

interface FilmStorage {
    Collection<Film> findAll();
    Film create(Film film);
    Film update(Film film);
}
