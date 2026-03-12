package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void shouldThrowExceptionWhenEmailInvalid() {
        User user = new User();
        user.setLogin("login");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowExceptionWhenLoginInvalid() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("bad login");

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldThrowExceptionWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

}
