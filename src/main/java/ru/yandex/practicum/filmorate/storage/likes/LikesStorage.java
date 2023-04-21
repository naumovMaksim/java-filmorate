package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;

public interface LikesStorage {
    Collection<User> getFilmLikes(int id);

    HashSet<Integer> getFilmLikesUserIds(int id);

    void likeFilm(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
