package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    User user;
    UserController controller = new UserController();

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .build();
    }

    @Test
    void findAllWithoutData() {
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void findAll() {
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void create() {
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createWithInvalidEmailException() {
        user.setEmail("Santa");
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(user));
        assertEquals("Адрес электронной почты введен не в верном формате.", exception.getMessage());
    }

    @Test
    void createWithInvalidLoginException() {
        user.setLogin(null);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(user));
        assertEquals("Логин введен в неверном формате.", exception.getMessage());
    }

    @Test
    void createWithInvalidBirthdayException() {
        user.setBirthday(LocalDate.of(2024, 1, 1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(user));
        assertEquals("Дата рождения не может дальше сегодняшнего дня.", exception.getMessage());
    }

    @Test
    void createWithoutName() {
        user.setName(null);
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
        assertEquals("Santa", user.getName());
    }


    @Test
    void update() {
        User user1 = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .build();

        controller.create(user);
        controller.updateOrCreate(user1);
        assertTrue(controller.findAll().contains(user1));
    }

    @Test
    void updateWithInvalidEmailException() {
        User user1 = User.builder()
                .id(1)
                .email("Santa")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .build();

        controller.create(user);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(user1));
        assertEquals("Адрес электронной почты введен не в верном формате.", exception.getMessage());
    }

    @Test
    void updateWithInvalidLoginException() {
        User user1 = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("San ta")
                .name("Санта Клаус")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .build();

        controller.create(user);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(user1));
        assertEquals("Логин введен в неверном формате.", exception.getMessage());
    }

    @Test
    void updateWithInvalidBirthdayException() {
        User user1 = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2024, 1, 1))
                .build();

        controller.create(user);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(user1));
        assertEquals("Дата рождения не может дальше сегодняшнего дня.", exception.getMessage());
    }
}