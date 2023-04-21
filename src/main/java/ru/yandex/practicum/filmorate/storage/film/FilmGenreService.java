package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genere;

import java.util.LinkedHashSet;

public interface FilmGenreService {
    LinkedHashSet<Genere> getFilmGenre(int filmId);

    void addGenreToFilm(Film film);

    void deleteAllFilmGenres(Film film);
}
