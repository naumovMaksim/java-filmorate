package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты введен не в верном формате.");
            throw new ValidationException("Адрес электронной почты введен не в верном формате.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин введен в неверном формате.");
            throw new ValidationException("Логин введен в неверном формате.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может дальше сегодняшнего дня.");
            throw new ValidationException("Дата рождения не может дальше сегодняшнего дня.");
        }
    }
}
