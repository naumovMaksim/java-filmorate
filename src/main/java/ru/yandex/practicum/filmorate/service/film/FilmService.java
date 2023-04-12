package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);
        if (film.getUsersLikes().contains(userId)) {
            log.error("Нельзя поставить лайк фильму дважды.");
            throw new ValidationException("Нельзя поставить лайк фильму дважды.");
        }
        film.setUsersLikes(user);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);
        if (!film.getUsersLikes().contains(userId)) {
            log.error("Вы уже убрали лайк или еще не поставили его.");
            throw new ValidationException("Вы уже убрали лайк или еще не поставили его.");
        }
        film.deleteUsersLikes(user);
    }

    public Collection<Film> popularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(f -> -f.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilm(int id) {
        return filmStorage.findFilm(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }
}
