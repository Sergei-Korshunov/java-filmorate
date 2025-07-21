package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid  @RequestBody User user) {
        log.info("Добавлен новый пользователь по имени {}", user.getName());
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновлен пользователь с именем {}", user.getName());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Запрос на поиск пользователя по id - {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getListUsers() {
        List<User> users = userService.getListUsers();
        log.debug("Список всех пользователей {}", users);

        return users;
    }

    @DeleteMapping("/{id}")
    public boolean removeUser(@PathVariable long id) {
        log.info("Удаление пользователя по id - {}", id);
        return userService.removeUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь с id - {} отправил запрос в друзья к пользователю с id - {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable long id) {
        List<User> listFriends = userService.getListFriends(id);
        log.debug("Список друзей {}", listFriends);

        return listFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        List<User> listCommonFriends = userService.getListCommonFriends(id, otherId);
        log.debug("Список общик друзей {} пользователей с id - {} и {}", listCommonFriends, id, otherId);

        return listCommonFriends;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь с id - {} удалил из друзей пользователя с id - {}", id, friendId);
        return userService.removeFriend(id, friendId);
    }
}
