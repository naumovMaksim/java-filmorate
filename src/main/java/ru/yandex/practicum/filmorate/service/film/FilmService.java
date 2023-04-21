package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmGenreService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;


@Service
@Slf4j
public class FilmService {
    private final FilmValidator filmValidator;
    private final UserValidator userValidator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesDao likesDao;
    private final FilmGenreService filmGenreService;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage, LikesDao likesDao, FilmValidator filmValidator, UserValidator userValidator, FilmGenreService filmGenreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesDao = likesDao;
        this.filmValidator = filmValidator;
        this.userValidator = userValidator;
        this.filmGenreService = filmGenreService;
    }

    public void addLike(int filmId, int userId) {
        User user = userStorage.findUser(userId);
        userValidator.validate(user);
        if (likesDao.getFilmLikes(filmId).contains(user)) {
            log.error("Фильм с id = {} не может быть лайкнут дважды", filmId);
            throw new ValidationException(String.format("Фильм с id = %d не может быть лайкнут дважды", filmId));
        }
        likesDao.likeFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        User user = userStorage.findUser(userId);
        userValidator.validate(user);
        if (!likesDao.getFilmLikes(filmId).contains(user)) {
            log.error("Вы уже убрали лайк с фильма id = {}", filmId);
            throw new ValidationException(String.format("Вы уже убрали лайк с фильма id = %d", filmId));
        }
        likesDao.deleteLike(filmId, userId);
    }

    public Collection<Film> popularFilms(Integer count) {
        final Collection<Film> films = filmStorage.getPopularFilms(count);
        films.forEach(film -> film.setGenres(filmGenreService.getFilmGenre(film.getId())));
        return films;
    }

    public Film findFilm(int id) {
        Film film = filmStorage.findFilm(id);
        filmValidator.validate(film);
        film.setGenres(filmGenreService.getFilmGenre(id));
        return film;
    }

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.findAll();
        for (Film film : films) {
            film.setGenres(filmGenreService.getFilmGenre(film.getId()));
        }
        return films;
    }

    public Film create(Film film) {
        filmValidator.validate(film);
        Film createdFilm = filmStorage.create(film);
        if (createdFilm.getGenres() != null) {
            filmGenreService.addGenreToFilm(createdFilm);
        }
        return createdFilm;
    }

    public void updateFilm(Film film) {
        filmValidator.validate(film);
        filmStorage.update(film);
        if (film.getGenres() != null) {
            filmGenreService.deleteAllFilmGenres(film);
            filmGenreService.addGenreToFilm(film);
        }
    }
}
