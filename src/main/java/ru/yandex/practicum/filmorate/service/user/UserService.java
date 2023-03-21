package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NullException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int addingUserId, int friendId) {
        User addingUser = userStorage.findUser(addingUserId);
        User friend = userStorage.findUser(friendId);
        if (addingUser.getFriends().contains(friendId)) {
            log.error("Пользователь уже есть у вас в друзьях.");
            throw new ValidationException("Пользователь уже есть у вас в друзьях.");
        }
        addingUser.setFriends(friend);
        friend.setFriends(addingUser);
    }

    public void deleteFriend(int addingUserId, int friendId) {
        User addingUser = userStorage.findUser(addingUserId);
        User friend = userStorage.findUser(friendId);
        if (!addingUser.getFriends().contains(friendId)) {
            log.error("Такого пользователя нет у вас в друзьях.");
            throw new NullException("Такого пользователя нет у вас в друзьях.");
        }
        addingUser.deleteFriend(friend);
        friend.deleteFriend(addingUser);
    }

    public Collection<User> getFriendsByUserId(int id) {
        User user = userStorage.findUser(id);
        Collection<User> users = new ArrayList<>();
        for (int i : user.getFriends()) {
            User user1 = userStorage.findUser(i);
            users.add(user1);
        }
        return users;
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        User user = userStorage.findUser(id);
        User otherUser = userStorage.findUser(otherId);
        Collection<User> commonFriends = new ArrayList<>();
        for (int i: user.getFriends()) {
            if (otherUser.getFriends().contains(i)) {
                User user1 = userStorage.findUser(i);
                commonFriends.add(user1);
            }
        }
        return commonFriends;
    }
}
