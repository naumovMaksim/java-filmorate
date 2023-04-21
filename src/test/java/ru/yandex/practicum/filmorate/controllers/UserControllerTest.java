package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    User user;
    private final JdbcTemplate jdbcTemplate;
    private final UserController controller;
    private final FriendsDao friendsDao;

    @BeforeEach
    void beforeEach() throws IOException {
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/schema1.sql")));
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/data1.sql")));
        user = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
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
    void findById() {
        controller.create(user);
        assertEquals(user, controller.findUser(user.getId()));
    }

    @Test
    void findUserWithWrongData() {
        controller.create(user);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.findUser(0));
        assertEquals("Пользователь с id = 0 не найден", exception.getParameter());
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
        assertEquals("Адрес электронной почты введен не в верном формате.", exception.getParameter());
    }

    @Test
    void createWithInvalidLoginException() {
        user.setLogin(null);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(user));
        assertEquals("Логин введен в неверном формате.", exception.getParameter());
    }

    @Test
    void createWithInvalidBirthdayException() {
        user.setBirthday(LocalDate.of(2024, 1, 1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(user));
        assertEquals("Дата рождения не может дальше сегодняшнего дня.", exception.getParameter());
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
                .email("Sa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
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
        assertEquals("Адрес электронной почты введен не в верном формате.", exception.getParameter());
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
        assertEquals("Логин введен в неверном формате.", exception.getParameter());
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
        assertEquals("Дата рождения не может дальше сегодняшнего дня.", exception.getParameter());
    }

    @Test
    void addFriend() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        controller.create(user);
        controller.create(user1);
        controller.addFriend(user.getId(), user1.getId());
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
    }

    @Test
    void addFriendWithWrongData() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        controller.create(user);
        controller.create(user1);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.addFriend(0, 0));
        assertEquals("Пользователи с идентификаторами id = 0 и id = 0 не найдены", exception.getParameter());
    }

    @Test
    void deleteFriend() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        controller.create(user);
        controller.create(user1);
        controller.addFriend(user.getId(), user1.getId());
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
        controller.deleteFriend(user.getId(), user1.getId());
        assertFalse(friendsDao.getUserFriends(user.getId()).contains(user1));
    }

    @Test
    void deleteFriendWithWrongData() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        controller.create(user);
        controller.create(user1);
        controller.addFriend(user.getId(), user1.getId());
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.deleteFriend(1, 0));
        assertEquals("Такого пользователя нет у вас в друзьях.", exception.getParameter());
    }

    @Test
    void getFriendByUserId() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        User user2 = User.builder()
                .email("Santa@mail.com")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();

        controller.create(user);
        controller.create(user1);
        controller.create(user2);
        controller.addFriend(user.getId(), user1.getId());
        controller.addFriend(user.getId(), user2.getId());
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user2));
        Collection<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        assertEquals(users, controller.getFriendsByUserId(user.getId()));
    }

    @Test
    void getFriendByUserIdWithWrongData() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        User user2 = User.builder()
                .email("Santa@mail.com")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();

        controller.create(user);
        controller.create(user1);
        controller.create(user2);
        assertEquals(Collections.EMPTY_LIST, controller.getFriendsByUserId(user.getId()));
    }

    @Test
    void getCommonFriends() {
        User user1 = User.builder()
                .email("Sta@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
                .build();

        User user2 = User.builder()
                .email("Santa@mail.r")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
                .build();
        controller.create(user);
        controller.create(user1);
        controller.create(user2);
        controller.addFriend(user.getId(), user1.getId());//0 + 1
        controller.addFriend(user.getId(), user2.getId());//0 + 2
        controller.addFriend(user1.getId(), user2.getId());// 1 + 2
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user2));
        assertTrue(friendsDao.getUserFriends(user1.getId()).contains(user2));
        Collection<User> users = new ArrayList<>();
        users.add(user2);
        assertEquals(users, controller.getCommonFriends(user.getId(), user1.getId()));
    }

    @Test
    void getCommonFriendsWithWrongData() {
        User user1 = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        User user2 = User.builder()
                .email("Santa@mail.com")
                .login("Santa")
                .name("Санта Клаус")
                .birthday(LocalDate.of(2021, 1, 1))
                .build();
        controller.create(user);
        controller.create(user1);
        controller.create(user2);
        controller.addFriend(user.getId(), user1.getId());//0 + 1
        assertTrue(friendsDao.getUserFriends(user.getId()).contains(user1));
        assertEquals(Collections.EMPTY_LIST, controller.getCommonFriends(user.getId(), user2.getId()));
    }
}