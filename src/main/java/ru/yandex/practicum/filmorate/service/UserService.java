package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        normalizeUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            log.warn("Попытка обновить фильм без id");
            throw new ValidationException("Id должен быть указан");
        }

        userStorage.findById(user.getId());

        normalizeUser(user);

        User updated = userStorage.update(user);

        log.info("Обновлён пользователь: {}", updated);

        return updated;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = findById(userId);
        User other = findById(otherId);

        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(this::findById)
                .toList();
    }

    private void normalizeUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, установлено значение логина");
        }
    }
}
