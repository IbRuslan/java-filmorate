package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void shouldThrowExceptionWhenEmailInvalid() {

        User user = new User();
        user.setLogin("login");

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLoginInvalid() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("bad login");

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayInFuture() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

}
