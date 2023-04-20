package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genere;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genere> getAllGeners();

    Genere getGenereById(int id);
}
