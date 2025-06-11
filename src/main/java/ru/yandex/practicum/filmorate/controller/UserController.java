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
    private long countId = 1;

    @PostMapping
    public User addUser(@Valid  @RequestBody User newUser) {
        newUser.setId(countId++);
        checkUserForName(newUser);
        users.put(countId, newUser);
        log.info("Добален новый пользователь по имени {}", newUser.getName());

        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updateUser) {
        if (!users.containsKey(updateUser.getId()))
            throw new NotFoundException("Пользователь с указанным id (" + updateUser.getId() + ") не найден");

        User newUser = users.get(updateUser.getId());
        newUser.setLogin(updateUser.getLogin());
        newUser.setName(updateUser.getName());
        newUser.setEmail(updateUser.getEmail());
        newUser.setBirthday(updateUser.getBirthday());
        checkUserForName(newUser);

        log.info("Обнавлен пользователь с именим {}", updateUser.getName());

        return updateUser;
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
