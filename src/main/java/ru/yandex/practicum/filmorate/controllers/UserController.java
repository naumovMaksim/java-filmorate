package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        log.debug("Пришел /GET запрос на получение пользователя с id: {}", id);
        User user = userService.findUser(id);
        log.debug("Ответ отправлен: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Пришел /GET запрос на получение всех пользователей.");
        Collection<User> users = userService.findAll();
        log.debug("Ответ отправлен: {}", users);
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Пришел /POST на добавление пользователя запрос: {}", user);
        User user1 = userService.create(user);
        log.debug("Пользователь добавлен: {}", user1);
        return user1;
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody User user) {
        log.debug("Пришел /PUT запрос на из мнение данных пользователя: {}", user);
        User user1 = userService.updateUser(user);
        log.debug("Данные пользователя изменены: {}", user1);
        return user1;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Пришел /POST запрос на добавление друга с id: {} к пользователю с id: {}", friendId, id);
        userService.addFriend(id, friendId);
        User addingUser = userService.findUser(id);
        log.debug("Друг c id: {} добавлен: {}", friendId, addingUser.getFriends());
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Пришел /DELETE запрос на удаление друга с id {} у пользователя с id {}", friendId, id);
        userService.deleteFriend(id, friendId);
        User addingUser = userService.findUser(id);
        log.debug("Друг с id: {} удален: {}", friendId, addingUser.getFriends());
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsByUserId(@PathVariable int id) {
        log.debug("Пришел /GET запрос на получение списка друзей пользователя с id {}", id);
        Collection<User> users = userService.getFriendsByUserId(id);
        log.debug("Ответ отправлен: {}", users);
        return users;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Пришел /GET запрос на получение списка общих друзей пользователя с id {} и пользователя с id {}", id, otherId);
        Collection<User> users = userService.getCommonFriends(id, otherId);
        log.debug("Ответ отправлен: {}", users);
        return users;
    }
}
// Строки в Json для проверки программы.
//        {
//                "email": "dusa@gmail.com",
//                "login": "dusa",
//                "name": "max",
//                "birthday": "1999-12-12"
//         }
