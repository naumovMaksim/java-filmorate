package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    public void addFriend(int userId, int friendId) {
        String sql = "MERGE INTO USER_FRIENDS AS uf USING (VALUES (?,?)) S(friend_id, user_id) " +
                "ON uf.FRIEND_ID = S.friend_id AND uf.USER_ID = S.user_id " +
                "WHEN NOT MATCHED THEN INSERT VALUES (S.friend_id, S.user_id)";
        try {
            jdbcTemplate.update(sql, friendId, userId);
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователи с идентификаторами id = {} и id = {} не найдены", userId, friendId);
            throw new DataNotFoundException(
                    String.format("Пользователи с идентификаторами id = %d и id = %d не найдены", userId, friendId));
        }
    }

    public void deleteFriend(int userId, int friendId) {
        String sql = "MERGE INTO USER_FRIENDS AS uf USING (VALUES (?,?)) S(friend_id, user_id) " +
                "ON uf.FRIEND_ID = S.friend_id AND uf.USER_ID = S.user_id " +
                "WHEN MATCHED THEN DELETE";

        try {
            jdbcTemplate.update(sql, friendId, userId);
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователи с идентификаторами id = {} и id = {} не найдены", userId, friendId);
            throw new DataNotFoundException(
                    String.format("Пользователи с идентификаторами id = %d и id = %d не найдены", userId, friendId));
        }
    }

    public Collection<User> getUserFriends(int id) {
        String sql = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY FROM USER_FRIENDS " +
                "LEFT JOIN USERS U on U.USER_ID = USER_FRIENDS.FRIEND_ID WHERE USER_FRIENDS.USER_ID = ?";
        Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
        for (User user : users) {
            user.setFriends(getUserFriendsIds(user.getId()));
        }
        return users;
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "FROM USER_FRIENDS AS uf " +
                "INNER JOIN USERS u on uf.FRIEND_ID = u.USER_ID " +
                "WHERE uf.USER_ID = ? AND uf.FRIEND_ID IN (SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID = ?)";

        try {
            Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), friendId, userId);
            for (User user : users) {
                user.setFriends(getUserFriendsIds(user.getId()));
            }
            return users;
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователи с идентификаторами id = {} и id = {} не найдены", userId, friendId);
            throw new DataNotFoundException(
                    String.format("Пользователи с идентификаторами id = %d и id = %d не найдены", userId, friendId));
        }
    }

    public HashSet<Integer> getUserFriendsIds(int id) {
        Collection<User> userFriends = getUserFriends(id);
        HashSet<Integer> userFriendsHashSet = new HashSet<>();
        for (User friend : userFriends) {
            userFriendsHashSet.add(friend.getId());
        }
        return userFriendsHashSet;
    }
}
