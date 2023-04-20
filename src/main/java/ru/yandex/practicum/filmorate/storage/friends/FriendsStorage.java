package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendsStorage {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Collection<User> getUserFriends(int id);

    Collection<User> getCommonFriends(int userId, int friendId);
}
