package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        log.debug("Пришел /GET запрос на получение пользователя с id: {}", id);
        log.debug("Ответ отправлен: {}", userStorage.findUser(id));
        return userStorage.findUser(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Пришел /GET запрос на получение всех пользователей.");
        log.debug("Ответ отправлен: {}", userStorage.findAll());
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Пришел /POST на добавление пользователя запрос: {}", user);
        userStorage.create(user);
        log.debug("Пользователь добавлен: {}", userStorage.findAll().contains(user));
        return user;
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody User user) {
        log.debug("Пришел /PUT запрос на из мнение данных пользователя: {}", user);
        userStorage.update(user);
        log.debug("Пользователь добавлен: {}", userStorage.findAll().contains(user));
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Пришел /POST запрос на добавление друга с id: {} к пользователю с id: {}", friendId, id);
        userService.addFriend(id, friendId);
        User addingUser = userStorage.findUser(id);
        log.debug("Друг c id: {} добавлен: {}", friendId, addingUser.getFriends());
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Пришел /DELETE запрос на удаление друга с id {} у пользователя с id {}", friendId, id);
        userService.deleteFriend(id, friendId);
        User addingUser = userStorage.findUser(id);
        log.debug("Друг с id: {} удален: {}", friendId, addingUser.getFriends());
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsByUserId(@PathVariable int id) {
        log.debug("Пришел /GET запрос на получение списка друзей пользователя с id {}", id);
        log.debug("Ответ отправлен: {}", userService.getFriendsByUserId(id));
        return userService.getFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Пришел /GET запрос на получение списка общих друзей пользователя с id {} и пользователя с id {}", id, otherId);
        log.debug("Ответ отправлен: {}", userService.getCommonFriends(id, otherId));
        return userService.getCommonFriends(id, otherId);
    }
}
// Строки в Json для проверки программы.
//        {
//        "email": "dusag@mail.com",
//        "login": "dartsaider",
//        "name": "Cat",
//        "birthday": "2001-12-25"
//        }
