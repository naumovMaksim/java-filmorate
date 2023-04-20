package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikesDao implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getFilmLikes(int id) {
        String sql = "SELECT u.user_ID, u.email, u.login, u.name, u.birthday " +
                "FROM FILM_LIKES AS fl" +
                "LEFT JOIN USERS AS u ON flLEFT.USER_ID = u.USER_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
    }

    @Override
    public HashSet<Integer> getFilmLikesUserIds(int id) {
        String sql = "SELECT u.user_ID, u.email, u.login, u.name, u.birthday " +
                "FROM FILM_LIKES AS fl" +
                "LEFT JOIN USERS AS u ON flLEFT.USER_ID = u.USER_ID " +
                "WHERE FILM_ID = ?";
        Collection<User> usersLikes = jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
        HashSet<Integer> setLikes = new HashSet<>();
        for (User user : usersLikes) {
            setLikes.add(user.getId());
        }
        return setLikes;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?,?)";

        try {
            jdbcTemplate.update(sql, filmId, userId);
            log.debug("Фильм с id = {} был лайкнут пользоватлем с id = {}", filmId, userId);
        } catch (DataIntegrityViolationException e) {
            log.error("Фильм с id = {} или пользоватль с id = {} не найдены", filmId, userId);
            throw new DataNotFoundException(String.format("Фильм с id = %d или пользоватль с id = %d не найдены", filmId, userId));
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";

        try {
            jdbcTemplate.update(sql, filmId, userId);
            log.debug("Лайк пользователя с id = {} был удален с фильма с id = {}", userId, filmId);
        } catch (DataIntegrityViolationException e) {
            log.error("Фильм с id = {} или пользователь с id = {} не найдены", filmId, userId);
            throw new DataNotFoundException(String.format("Фильм с id = %d или пользователь с id = %d не найдены", filmId, userId));
        }
    }
}
