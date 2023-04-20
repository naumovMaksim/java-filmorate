package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    User user = User.builder()
            .email("cat@yandex.ru")
            .login("CatUser")
            .name("Cat")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    User user2 = User.builder()
            .email("dog@yandex.ru")
            .login("DogUser")
            .name("Dog")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();



    @BeforeEach
    void beforeEach() throws IOException {
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/schema1.sql")));
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/data1.sql")));
    }

    @Test
    void findUser() {
        userDbStorage.create(user);
        assertEquals(user, userDbStorage.findUser(1));
    }

    @Test
    void findAll() {
        userDbStorage.create(user);
        userDbStorage.create(user2);
        assertEquals(List.of(user, user2), userDbStorage.findAll());
    }

    @Test
    void create() {
        userDbStorage.create(user);
        assertEquals(user, userDbStorage.findUser(1));
    }

    @Test
    void update() {
        userDbStorage.create(user);
        User user2 = User.builder()
                .id(1)
                .email("mrr@yandex.ru")
                .login("mrrCat")
                .name("mrrrrrr")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userDbStorage.update(User.builder()
                .id(1)
                .email("mrr@yandex.ru")
                .login("mrrCat")
                .name("mrrrrrr")
                .birthday(LocalDate.of(2000, 1, 1))
                .build());
        assertEquals(user2, userDbStorage.findUser(1));
    }
}