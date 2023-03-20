package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NullException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);
        if (film.getUsersLikes().contains(userId)) {
            throw new ValidationException("Нельзя поставить лайк фильму дважды.");
        }
        film.setUsersLikes(user);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);
        if (!film.getUsersLikes().contains(userId)) {
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
}
