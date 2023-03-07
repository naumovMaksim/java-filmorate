package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserValidator validator = new UserValidator();
    private int generatorId;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validator.userValidator(user);
        user.setId(generateId());
        log.debug("Добавление пользователя: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        validator.userValidator(user);
        log.debug("Изменение данных пользователя c: {}, на: {}.", users.get(user.getId()), user);
        users.put(user.getId(), user);
        return user;
    }

    private int generateId() {
        return ++generatorId;
    }
}
// Строки в Json для проверки программы.
//        {
//        "email": "dusag@mail.com",
//        "login": "dartsaider",
//        "name": "Cat",
//        "birthday": "2001-12-25"
//        }
