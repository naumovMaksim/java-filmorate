package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NullException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final UserValidator validator = new UserValidator();
    private int generatorId;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User findUser(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь не найден.");
            throw new NullException("Пользователь не найден.");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        validator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден.");
            throw new NullException("Пользователь не найден.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validator.validate(user);
        users.put(user.getId(), user);
        return user;
    }

    private int generateId() {
        return ++generatorId;
    }
}
