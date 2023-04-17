package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FriendsDao friendsDao;

    @Override
    public User findUser(int id) {
        String sql = "SELECT * FROM USERS where user_id = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> User.makeUser(rs), id);
            log.debug("Найден пользователь {}", user);
            assert user != null;
            user.setFriends(friendsDao.getUserFriendsIds(id));
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Пользователь с id = %d не найден", id));
        }
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM USERS";

        Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs));
        for (User user : users) {
            user.setFriends(friendsDao.getUserFriendsIds(user.getId()));
        }
        return users;
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS (email, login, name, birthday) VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int idKey = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(idKey);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        int update = jdbcTemplate.update(sql,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (update == 0) {
            log.error("Пользоватьель с id = {} не найден", user.getId());
            throw new DataNotFoundException(String.format("Пользоватьель с id = %d не найден", user.getId()));
        }
        user.setFriends(friendsDao.getUserFriendsIds(user.getId()));
        return user;
    }
}
