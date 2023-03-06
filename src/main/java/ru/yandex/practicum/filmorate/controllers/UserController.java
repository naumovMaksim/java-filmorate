package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты введен не в верном формате.");
            throw new InvalidEmailException("Адрес электронной почты введен не в верном формате.");
        }
        if (users.containsKey(user.getEmail())) {
            log.error("Такой пользователь уже зарегистрирован.");
            throw new UserAlreadyExistException("Такой пользователь уже зарегистрирован.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин введен в неверном формате.");
            throw new InvalidLoginException("Логин введен в неверном формате.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может дальше сегодняшнего дня.");
            throw new InvalidBirthdayException("Дата рождения не может дальше сегодняшнего дня.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        log.debug("Добавление пользователя: {}", user);
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping
    public User updateOrCreate(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты введен не в верном формате.");
            throw new InvalidEmailException("Адрес электронной почты введен не в верном формате.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин введен в неверном формате.");
            throw new InvalidLoginException("Логин введен в неверном формате.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может дальше сегодняшнего дня.");
            throw new InvalidBirthdayException("Дата рождения не может дальше сегодняшнего дня.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getEmail())) {
            log.debug("Изменение данных пользователя c: {}, на: {}.", users.get(user.getEmail()), user);
            users.put(user.getEmail(), user);
        } else {
            user.setId(id++);
            log.debug("Добавление пользователя {}.", user);
            users.put(user.getEmail(), user);
        }
        return user;
    }
}
// Строки в Json для проверки программы.
//        {
//        "email": "dusag@mail.com",
//        "login": "dartsaider",
//        "name": "Cat",
//        "birthday": "2001-12-25"
//        }
