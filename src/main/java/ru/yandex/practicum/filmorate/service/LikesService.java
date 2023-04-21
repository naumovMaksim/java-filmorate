package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesDao likesDao;

    public Collection<User> getFilmLikes(int id) {
        return likesDao.getFilmLikes(id);
    }

    public HashSet<Integer> getFilmLikesUserIds(int id) {
        return likesDao.getFilmLikesUserIds(id);
    }

    public void likeFilm(int filmId, int userId) {
        likesDao.likeFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        likesDao.deleteLike(filmId, userId);
    }
}
