package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private long countId = 0;

    @PostMapping
    public User addUser(@Valid  @RequestBody User user) {
        user.setId(++countId);
        checkUserForName(user);
        users.put(user.getId(), user);
        log.info("Добален новый пользователь по имени {}", user.getName());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь с указанным id (" + user.getId() + ") не найден");

        checkUserForName(user);
        users.put(user.getId(), user);

        log.info("Обнавлен пользователь с именим {}", user.getName());

        return user;
    }

    @GetMapping
    public List<User> getAllUser() {
        log.debug("Список всех пользователей {}", users.values());
        return new ArrayList<>(users.values());
    }

    private void checkUserForName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
