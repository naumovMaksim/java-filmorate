package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final UserValidator userValidator;
    private final FriendsDao friendsDao;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, UserValidator userValidator, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.userValidator = userValidator;
        this.friendsDao = friendsDao;
    }

    public void addFriend(int addingUserId, int friendId) {
        if (friendsDao.getUserFriendsIds(addingUserId).contains(friendId)) {
            log.error("Пользователь уже есть у вас в друзьях.");
            throw new ValidationException("Пользователь уже есть у вас в друзьях.");
        }
        friendsDao.addFriend(addingUserId, friendId);
    }

    public void deleteFriend(int addingUserId, int friendId) {
        if (!friendsDao.getUserFriendsIds(addingUserId).contains(friendId)) {
            log.error("Такого пользователя нет у вас в друзьях.");
            throw new DataNotFoundException("Такого пользователя нет у вас в друзьях.");
        }
        friendsDao.deleteFriend(addingUserId, friendId);
    }

    public Collection<User> getFriendsByUserId(int id) {
        return friendsDao.getUserFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return friendsDao.getCommonFriends(id, otherId);
    }

    public User findUser(int id) {
        return userStorage.findUser(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        userValidator.validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        userValidator.validate(user);
        return userStorage.update(user);
    }
}
