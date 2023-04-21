package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genere;

import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class FilmGenreService {
    private final FilmGenreDao filmGenreDao;

    public LinkedHashSet<Genere> getFilmGenre(int filmId) {
        return filmGenreDao.getFilmGenre(filmId);
    }

    public void addGenreToFilm(Film film) {
        filmGenreDao.addGenreToFilm(film);
    }

    public void deleteAllFilmGenres(Film film) {
        filmGenreDao.deleteAllFilmGenres(film);
    }
}
