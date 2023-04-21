package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendsDao friendsDao;

    public void addFriend(int userId, int friendId) {
        friendsDao.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        friendsDao.deleteFriend(userId, friendId);
    }

    public Collection<User> getUserFriends(int id) {
        return friendsDao.getUserFriends(id);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        return friendsDao.getCommonFriends(userId, friendId);
    }

    public HashSet<Integer> getUserFriendsIds(int id) {
        return friendsDao.getUserFriendsIds(id);
    }
}
