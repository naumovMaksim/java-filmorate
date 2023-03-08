package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
    private final UserValidator validator = new UserValidator();
    private int generatorId;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Пришел /GET запрос.");
        log.debug("Ответ отправлен: {}", users.values());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен /POST запрос: {}", user);
        validator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        log.debug("Добавление пользователя: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody User user) {
        log.debug("Получен /PUT запрос: {}", user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не найден.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validator.validate(user);
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
