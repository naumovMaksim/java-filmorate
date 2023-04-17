package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenereDao;
import ru.yandex.practicum.filmorate.model.Genere;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenereService {

    private final GenereDao genereDao;

    public Collection<Genere> getAllGeneres() {
        return genereDao.getAllGeners();
    }

    public Genere getGenereById(int id) {
        return genereDao.getGenereById(id);
    }
}
